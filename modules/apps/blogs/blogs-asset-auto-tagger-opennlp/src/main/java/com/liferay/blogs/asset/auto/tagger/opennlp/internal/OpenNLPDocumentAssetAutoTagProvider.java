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

package com.liferay.blogs.asset.auto.tagger.opennlp.internal;

import com.liferay.asset.auto.tagger.AssetAutoTagProvider;
import com.liferay.asset.auto.tagger.opennlp.api.OpenNLPDocumentAssetAutoTagger;
import com.liferay.blogs.asset.auto.tagger.opennlp.internal.configuration.OpenNLPDocumentAssetAutoTagProviderCompanyConfiguration;
import com.liferay.blogs.model.BlogsEntry;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.util.ContentTypes;

import java.util.Collection;
import java.util.Collections;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Cristina González
 * @author Alicia García
 */
@Component(
	property = "model.class.name=com.liferay.blogs.model.BlogsEntry",
	service = AssetAutoTagProvider.class
)
public class OpenNLPDocumentAssetAutoTagProvider
	implements AssetAutoTagProvider<BlogsEntry> {

	@Override
	public Collection<String> getTagNames(BlogsEntry blogsEntry) {
		try {
			return _getTagNames(blogsEntry);
		}
		catch (Exception e) {
			_log.error(e, e);

			return Collections.emptySet();
		}
	}

	private Collection<String> _getTagNames(BlogsEntry blogsEntry)
		throws Exception {

		OpenNLPDocumentAssetAutoTagProviderCompanyConfiguration
			openNLPDocumentAssetAutoTagProviderCompanyConfiguration =
				_configurationProvider.getCompanyConfiguration(OpenNLPDocumentAssetAutoTagProviderCompanyConfiguration.
						class, blogsEntry.getCompanyId());

		if (!openNLPDocumentAssetAutoTagProviderCompanyConfiguration.
				enabled()) {

			return Collections.emptySet();
		}

		return _openNLPDocumentAssetAutoTagger.getTagNames(
			blogsEntry.getCompanyId(),
			blogsEntry.getContent(),
			ContentTypes.TEXT_HTML_UTF8);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OpenNLPDocumentAssetAutoTagProvider.class);

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private OpenNLPDocumentAssetAutoTagger _openNLPDocumentAssetAutoTagger;


}