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

package com.liferay.portal.workflow.kaleo.service.base;

import com.liferay.petra.function.UnsafeFunction;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalServiceImpl;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.service.change.tracking.CTService;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.service.persistence.change.tracking.CTPersistence;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.workflow.kaleo.model.KaleoTimerInstanceToken;
import com.liferay.portal.workflow.kaleo.service.KaleoTimerInstanceTokenLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoTimerInstanceTokenLocalServiceUtil;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoActionPersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoConditionPersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoDefinitionPersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoDefinitionVersionPersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoInstancePersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoInstanceTokenPersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoLogPersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoNodePersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoNotificationPersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoNotificationRecipientPersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoTaskAssignmentInstancePersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoTaskAssignmentPersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoTaskFormInstancePersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoTaskFormPersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoTaskInstanceTokenFinder;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoTaskInstanceTokenPersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoTaskPersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoTimerInstanceTokenPersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoTimerPersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoTransitionPersistence;

import java.io.Serializable;

import java.lang.reflect.Field;

import java.util.List;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the kaleo timer instance token local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.portal.workflow.kaleo.service.impl.KaleoTimerInstanceTokenLocalServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.portal.workflow.kaleo.service.impl.KaleoTimerInstanceTokenLocalServiceImpl
 * @generated
 */
public abstract class KaleoTimerInstanceTokenLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements AopService, IdentifiableOSGiService,
			   KaleoTimerInstanceTokenLocalService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>KaleoTimerInstanceTokenLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>KaleoTimerInstanceTokenLocalServiceUtil</code>.
	 */

	/**
	 * Adds the kaleo timer instance token to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect KaleoTimerInstanceTokenLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param kaleoTimerInstanceToken the kaleo timer instance token
	 * @return the kaleo timer instance token that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public KaleoTimerInstanceToken addKaleoTimerInstanceToken(
		KaleoTimerInstanceToken kaleoTimerInstanceToken) {

		kaleoTimerInstanceToken.setNew(true);

		return kaleoTimerInstanceTokenPersistence.update(
			kaleoTimerInstanceToken);
	}

	/**
	 * Creates a new kaleo timer instance token with the primary key. Does not add the kaleo timer instance token to the database.
	 *
	 * @param kaleoTimerInstanceTokenId the primary key for the new kaleo timer instance token
	 * @return the new kaleo timer instance token
	 */
	@Override
	@Transactional(enabled = false)
	public KaleoTimerInstanceToken createKaleoTimerInstanceToken(
		long kaleoTimerInstanceTokenId) {

		return kaleoTimerInstanceTokenPersistence.create(
			kaleoTimerInstanceTokenId);
	}

	/**
	 * Deletes the kaleo timer instance token with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect KaleoTimerInstanceTokenLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param kaleoTimerInstanceTokenId the primary key of the kaleo timer instance token
	 * @return the kaleo timer instance token that was removed
	 * @throws PortalException if a kaleo timer instance token with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public KaleoTimerInstanceToken deleteKaleoTimerInstanceToken(
			long kaleoTimerInstanceTokenId)
		throws PortalException {

		return kaleoTimerInstanceTokenPersistence.remove(
			kaleoTimerInstanceTokenId);
	}

	/**
	 * Deletes the kaleo timer instance token from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect KaleoTimerInstanceTokenLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param kaleoTimerInstanceToken the kaleo timer instance token
	 * @return the kaleo timer instance token that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public KaleoTimerInstanceToken deleteKaleoTimerInstanceToken(
		KaleoTimerInstanceToken kaleoTimerInstanceToken) {

		return kaleoTimerInstanceTokenPersistence.remove(
			kaleoTimerInstanceToken);
	}

	@Override
	public <T> T dslQuery(DSLQuery dslQuery) {
		return kaleoTimerInstanceTokenPersistence.dslQuery(dslQuery);
	}

	@Override
	public int dslQueryCount(DSLQuery dslQuery) {
		Long count = dslQuery(dslQuery);

		return count.intValue();
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			KaleoTimerInstanceToken.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return kaleoTimerInstanceTokenPersistence.findWithDynamicQuery(
			dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.workflow.kaleo.model.impl.KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return kaleoTimerInstanceTokenPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.workflow.kaleo.model.impl.KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return kaleoTimerInstanceTokenPersistence.findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return kaleoTimerInstanceTokenPersistence.countWithDynamicQuery(
			dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		DynamicQuery dynamicQuery, Projection projection) {

		return kaleoTimerInstanceTokenPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public KaleoTimerInstanceToken fetchKaleoTimerInstanceToken(
		long kaleoTimerInstanceTokenId) {

		return kaleoTimerInstanceTokenPersistence.fetchByPrimaryKey(
			kaleoTimerInstanceTokenId);
	}

	/**
	 * Returns the kaleo timer instance token with the primary key.
	 *
	 * @param kaleoTimerInstanceTokenId the primary key of the kaleo timer instance token
	 * @return the kaleo timer instance token
	 * @throws PortalException if a kaleo timer instance token with the primary key could not be found
	 */
	@Override
	public KaleoTimerInstanceToken getKaleoTimerInstanceToken(
			long kaleoTimerInstanceTokenId)
		throws PortalException {

		return kaleoTimerInstanceTokenPersistence.findByPrimaryKey(
			kaleoTimerInstanceTokenId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(
			kaleoTimerInstanceTokenLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(KaleoTimerInstanceToken.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"kaleoTimerInstanceTokenId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			kaleoTimerInstanceTokenLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(
			KaleoTimerInstanceToken.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"kaleoTimerInstanceTokenId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(
			kaleoTimerInstanceTokenLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(KaleoTimerInstanceToken.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"kaleoTimerInstanceTokenId");
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return kaleoTimerInstanceTokenPersistence.create(
			((Long)primaryKeyObj).longValue());
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return kaleoTimerInstanceTokenLocalService.
			deleteKaleoTimerInstanceToken(
				(KaleoTimerInstanceToken)persistedModel);
	}

	@Override
	public BasePersistence<KaleoTimerInstanceToken> getBasePersistence() {
		return kaleoTimerInstanceTokenPersistence;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return kaleoTimerInstanceTokenPersistence.findByPrimaryKey(
			primaryKeyObj);
	}

	/**
	 * Returns a range of all the kaleo timer instance tokens.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.workflow.kaleo.model.impl.KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @return the range of kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> getKaleoTimerInstanceTokens(
		int start, int end) {

		return kaleoTimerInstanceTokenPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of kaleo timer instance tokens.
	 *
	 * @return the number of kaleo timer instance tokens
	 */
	@Override
	public int getKaleoTimerInstanceTokensCount() {
		return kaleoTimerInstanceTokenPersistence.countAll();
	}

	/**
	 * Updates the kaleo timer instance token in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect KaleoTimerInstanceTokenLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param kaleoTimerInstanceToken the kaleo timer instance token
	 * @return the kaleo timer instance token that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public KaleoTimerInstanceToken updateKaleoTimerInstanceToken(
		KaleoTimerInstanceToken kaleoTimerInstanceToken) {

		return kaleoTimerInstanceTokenPersistence.update(
			kaleoTimerInstanceToken);
	}

	@Deactivate
	protected void deactivate() {
		_setLocalServiceUtilService(null);
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			KaleoTimerInstanceTokenLocalService.class,
			IdentifiableOSGiService.class, CTService.class,
			PersistedModelLocalService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		kaleoTimerInstanceTokenLocalService =
			(KaleoTimerInstanceTokenLocalService)aopProxy;

		_setLocalServiceUtilService(kaleoTimerInstanceTokenLocalService);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return KaleoTimerInstanceTokenLocalService.class.getName();
	}

	@Override
	public CTPersistence<KaleoTimerInstanceToken> getCTPersistence() {
		return kaleoTimerInstanceTokenPersistence;
	}

	@Override
	public Class<KaleoTimerInstanceToken> getModelClass() {
		return KaleoTimerInstanceToken.class;
	}

	@Override
	public <R, E extends Throwable> R updateWithUnsafeFunction(
			UnsafeFunction<CTPersistence<KaleoTimerInstanceToken>, R, E>
				updateUnsafeFunction)
		throws E {

		return updateUnsafeFunction.apply(kaleoTimerInstanceTokenPersistence);
	}

	protected String getModelClassName() {
		return KaleoTimerInstanceToken.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource =
				kaleoTimerInstanceTokenPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(
				dataSource, sql);

			sqlUpdate.update();
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
	}

	private void _setLocalServiceUtilService(
		KaleoTimerInstanceTokenLocalService
			kaleoTimerInstanceTokenLocalService) {

		try {
			Field field =
				KaleoTimerInstanceTokenLocalServiceUtil.class.getDeclaredField(
					"_service");

			field.setAccessible(true);

			field.set(null, kaleoTimerInstanceTokenLocalService);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	@Reference
	protected KaleoActionPersistence kaleoActionPersistence;

	@Reference
	protected KaleoConditionPersistence kaleoConditionPersistence;

	@Reference
	protected KaleoDefinitionPersistence kaleoDefinitionPersistence;

	@Reference
	protected KaleoDefinitionVersionPersistence
		kaleoDefinitionVersionPersistence;

	@Reference
	protected KaleoInstancePersistence kaleoInstancePersistence;

	@Reference
	protected KaleoInstanceTokenPersistence kaleoInstanceTokenPersistence;

	@Reference
	protected KaleoLogPersistence kaleoLogPersistence;

	@Reference
	protected KaleoNodePersistence kaleoNodePersistence;

	@Reference
	protected KaleoNotificationPersistence kaleoNotificationPersistence;

	@Reference
	protected KaleoNotificationRecipientPersistence
		kaleoNotificationRecipientPersistence;

	@Reference
	protected KaleoTaskPersistence kaleoTaskPersistence;

	@Reference
	protected KaleoTaskAssignmentPersistence kaleoTaskAssignmentPersistence;

	@Reference
	protected KaleoTaskAssignmentInstancePersistence
		kaleoTaskAssignmentInstancePersistence;

	@Reference
	protected KaleoTaskFormPersistence kaleoTaskFormPersistence;

	@Reference
	protected KaleoTaskFormInstancePersistence kaleoTaskFormInstancePersistence;

	@Reference
	protected KaleoTaskInstanceTokenPersistence
		kaleoTaskInstanceTokenPersistence;

	@Reference
	protected KaleoTaskInstanceTokenFinder kaleoTaskInstanceTokenFinder;

	@Reference
	protected KaleoTimerPersistence kaleoTimerPersistence;

	protected KaleoTimerInstanceTokenLocalService
		kaleoTimerInstanceTokenLocalService;

	@Reference
	protected KaleoTimerInstanceTokenPersistence
		kaleoTimerInstanceTokenPersistence;

	@Reference
	protected KaleoTransitionPersistence kaleoTransitionPersistence;

	@Reference
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.ClassNameLocalService
		classNameLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.ResourceLocalService
		resourceLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.UserLocalService
		userLocalService;

}