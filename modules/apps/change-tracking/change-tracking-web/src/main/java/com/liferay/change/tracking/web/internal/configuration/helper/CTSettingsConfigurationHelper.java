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

package com.liferay.change.tracking.web.internal.configuration.helper;

import com.liferay.change.tracking.configuration.CTSettingsConfiguration;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;

import java.util.Collections;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author David Truong
 */
@Component(immediate = true, service = CTSettingsConfigurationHelper.class)
public class CTSettingsConfigurationHelper {

	public boolean enabled(long companyId) {
		CTSettingsConfiguration configuration = _getConfiguration(companyId);

		return configuration.enabled();
	}

	public CTSettingsConfiguration getCTSettingsConfiguration(long companyId) {
		return _getConfiguration(companyId);
	}

	public boolean sandboxEnabled(long companyId) {
		CTSettingsConfiguration configuration = _getConfiguration(companyId);

		return configuration.sandboxEnabled();
	}

	public void save(long companyId, boolean enabled, boolean sandboxEnabled)
		throws PortalException {

		_configurationProvider.saveCompanyConfiguration(
			CTSettingsConfiguration.class, companyId,
			HashMapDictionaryBuilder.<String, Object>put(
				"enabled", enabled
			).put(
				"sandboxEnabled", sandboxEnabled
			).build());
	}

	private CTSettingsConfiguration _getConfiguration(long companyId) {
		CTSettingsConfiguration configuration =
			ConfigurableUtil.createConfigurable(
				CTSettingsConfiguration.class, Collections.emptyMap());

		try {
			configuration = _configurationProvider.getCompanyConfiguration(
				CTSettingsConfiguration.class, companyId);
		}
		catch (ConfigurationException configurationException) {
			_log.error(configurationException);
		}

		return configuration;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CTSettingsConfigurationHelper.class.getName());

	@Reference
	private ConfigurationProvider _configurationProvider;

}