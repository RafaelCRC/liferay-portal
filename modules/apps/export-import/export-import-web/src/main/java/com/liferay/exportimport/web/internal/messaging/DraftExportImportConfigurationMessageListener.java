/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.exportimport.web.internal.messaging;

import com.liferay.exportimport.kernel.configuration.constants.ExportImportConfigurationConstants;
import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.exportimport.kernel.service.ExportImportConfigurationLocalService;
import com.liferay.exportimport.web.internal.configuration.ExportImportWebConfigurationValues;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.background.task.model.BackgroundTask;
import com.liferay.portal.background.task.service.BackgroundTaskLocalService;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.Order;
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelper;
import com.liferay.portal.kernel.scheduler.SchedulerEntry;
import com.liferay.portal.kernel.scheduler.SchedulerEntryImpl;
import com.liferay.portal.kernel.scheduler.TimeUnit;
import com.liferay.portal.kernel.scheduler.Trigger;
import com.liferay.portal.kernel.scheduler.TriggerFactory;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Levente Hudák
 * @author Daniel Kocsis
 */
@Component(
	immediate = true,
	service = DraftExportImportConfigurationMessageListener.class
)
public class DraftExportImportConfigurationMessageListener
	extends BaseMessageListener {

	@Activate
	protected void activate() {
		Class<?> clazz = getClass();

		String className = clazz.getName();

		Trigger trigger = _triggerFactory.createTrigger(
			className, className, null, null,
			ExportImportWebConfigurationValues.
				DRAFT_EXPORT_IMPORT_CONFIGURATION_CHECK_INTERVAL,
			TimeUnit.HOUR);

		SchedulerEntry schedulerEntry = new SchedulerEntryImpl(
			className, trigger);

		_schedulerEngineHelper.register(
			this, schedulerEntry, DestinationNames.SCHEDULER_DISPATCH);
	}

	@Deactivate
	protected void deactivate() {
		_schedulerEngineHelper.unregister(this);
	}

	@Override
	protected void doReceive(Message message) throws PortalException {
		if (ExportImportWebConfigurationValues.
				DRAFT_EXPORT_IMPORT_CONFIGURATION_CLEAN_UP_COUNT == -1) {

			return;
		}

		Date lastCreateDate;

		if (ExportImportWebConfigurationValues.
				DRAFT_EXPORT_IMPORT_CONFIGURATION_CLEAN_UP_COUNT == 0) {

			lastCreateDate = new Date();
		}
		else {
			DynamicQuery dynamicQuery =
				_exportImportConfigurationLocalService.dynamicQuery();

			_addCommonCriterions(dynamicQuery);

			Order order = OrderFactoryUtil.desc("createDate");

			dynamicQuery.addOrder(order);

			dynamicQuery.setLimit(
				QueryUtil.ALL_POS,
				ExportImportWebConfigurationValues.
					DRAFT_EXPORT_IMPORT_CONFIGURATION_CLEAN_UP_COUNT);

			dynamicQuery.setProjection(
				ProjectionFactoryUtil.property("createDate"));

			List<Date> createDates =
				_exportImportConfigurationLocalService.dynamicQuery(
					dynamicQuery);

			if (ListUtil.isEmpty(createDates)) {
				return;
			}

			lastCreateDate = createDates.get(createDates.size() - 1);
		}

		ActionableDynamicQuery actionableDynamicQuery =
			_exportImportConfigurationLocalService.getActionableDynamicQuery();

		Property createDate = PropertyFactoryUtil.forName("createDate");

		actionableDynamicQuery.setAddCriteriaMethod(
			dynamicQuery -> {
				_addCommonCriterions(dynamicQuery);

				dynamicQuery.add(createDate.lt(lastCreateDate));
			});

		actionableDynamicQuery.setPerformActionMethod(
			(ExportImportConfiguration exportImportConfiguration) -> {
				List<BackgroundTask> backgroundTasks =
					_getParentBackgroundTasks(exportImportConfiguration);

				if (ListUtil.isEmpty(backgroundTasks)) {
					_exportImportConfigurationLocalService.
						deleteExportImportConfiguration(
							exportImportConfiguration);

					return;
				}

				// BackgroundTaskModelListener deletes the linked
				// configuration automatically

				for (BackgroundTask backgroundTask : backgroundTasks) {
					if (_isLiveGroup(backgroundTask.getGroupId())) {
						continue;
					}

					_backgroundTaskLocalService.deleteBackgroundTask(
						backgroundTask.getBackgroundTaskId());
				}
			});

		actionableDynamicQuery.performActions();
	}

	private void _addCommonCriterions(DynamicQuery dynamicQuery) {
		Property typeProperty = PropertyFactoryUtil.forName("type");

		dynamicQuery.add(
			typeProperty.ne(
				ExportImportConfigurationConstants.
					TYPE_SCHEDULED_PUBLISH_LAYOUT_LOCAL));
		dynamicQuery.add(
			typeProperty.ne(
				ExportImportConfigurationConstants.
					TYPE_SCHEDULED_PUBLISH_LAYOUT_REMOTE));

		Property statusProperty = PropertyFactoryUtil.forName("status");

		dynamicQuery.add(statusProperty.eq(WorkflowConstants.STATUS_DRAFT));
	}

	private List<BackgroundTask> _getParentBackgroundTasks(
			ExportImportConfiguration exportImportConfiguration)
		throws PortalException {

		DynamicQuery dynamicQuery = _backgroundTaskLocalService.dynamicQuery();

		Property completedProperty = PropertyFactoryUtil.forName("completed");

		dynamicQuery.add(completedProperty.eq(Boolean.TRUE));

		Property taskContextMapProperty = PropertyFactoryUtil.forName(
			"taskContextMap");

		dynamicQuery.add(
			taskContextMapProperty.like(
				StringBundler.concat(
					"%\"exportImportConfigurationId\":",
					exportImportConfiguration.getExportImportConfigurationId(),
					StringPool.PERCENT)));

		return _backgroundTaskLocalService.dynamicQuery(dynamicQuery);
	}

	private boolean _isLiveGroup(long groupId) {
		Group group = _groupLocalService.fetchGroup(groupId);

		if (group == null) {
			return false;
		}

		if (group.hasStagingGroup()) {
			return true;
		}

		return false;
	}

	@Reference
	private BackgroundTaskLocalService _backgroundTaskLocalService;

	@Reference
	private ExportImportConfigurationLocalService
		_exportImportConfigurationLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED)
	private ModuleServiceLifecycle _moduleServiceLifecycle;

	@Reference
	private SchedulerEngineHelper _schedulerEngineHelper;

	@Reference
	private TriggerFactory _triggerFactory;

}