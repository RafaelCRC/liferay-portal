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

package com.liferay.translation.web.internal.portlet.action;

import com.liferay.info.exception.NoSuchInfoItemException;
import com.liferay.info.field.InfoFieldValue;
import com.liferay.info.item.InfoItemServiceTracker;
import com.liferay.info.item.provider.InfoItemFieldValuesProvider;
import com.liferay.info.item.provider.InfoItemObjectProvider;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.kernel.zip.ZipWriterFactoryUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.segments.constants.SegmentsExperienceConstants;
import com.liferay.segments.model.SegmentsExperience;
import com.liferay.translation.constants.TranslationPortletKeys;
import com.liferay.translation.exporter.TranslationInfoItemFieldValuesExporter;
import com.liferay.translation.exporter.TranslationInfoItemFieldValuesExporterTracker;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Locale;
import java.util.Optional;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Tardín
 */
@Component(
	property = {
		"javax.portlet.name=" + TranslationPortletKeys.TRANSLATION,
		"mvc.command.name=/translation/export_translation"
	},
	service = MVCResourceCommand.class
)
public class ExportTranslationMVCResourceCommand implements MVCResourceCommand {

	@Override
	public boolean serveResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws PortletException {

		try {
			long[] segmentsExperienceIds = ParamUtil.getLongValues(
				resourceRequest, "segmentsExperienceIds");

			String className = _getClassName(
				resourceRequest, segmentsExperienceIds);

			String exportMimeType = ParamUtil.getString(
				resourceRequest, "exportMimeType");
			String sourceLanguageId = ParamUtil.getString(
				resourceRequest, "sourceLanguageId");
			String[] targetLanguageIds = ParamUtil.getStringValues(
				resourceRequest, "targetLanguageIds");

			ZipWriter zipWriter = ZipWriterFactoryUtil.getZipWriter();

			for (long classPK :
					_getClassPKs(resourceRequest, segmentsExperienceIds)) {

				_addZipEntry(
					zipWriter, className, classPK, exportMimeType,
					sourceLanguageId, targetLanguageIds,
					_portal.getLocale(resourceRequest));
			}

			try (InputStream inputStream = new FileInputStream(
					zipWriter.getFile())) {

				PortletResponseUtil.sendFile(
					resourceRequest, resourceResponse,
					_getZipFileName(
						_getModelClassName(resourceRequest),
						_getModelClassPK(resourceRequest), sourceLanguageId,
						_portal.getLocale(resourceRequest)),
					inputStream, ContentTypes.APPLICATION_ZIP);
			}

			return false;
		}
		catch (IOException | PortalException exception) {
			throw new PortletException(exception);
		}
	}

	private void _addZipEntry(
			ZipWriter zipWriter, String className, long classPK,
			String exportMimeType, String sourceLanguageId,
			String[] targetLanguageIds, Locale locale)
		throws IOException, PortalException {

		InfoItemFieldValuesProvider<Object> infoItemFieldValuesProvider =
			_infoItemServiceTracker.getFirstInfoItemService(
				InfoItemFieldValuesProvider.class, className);

		InfoItemObjectProvider<Object> infoItemObjectProvider =
			_infoItemServiceTracker.getFirstInfoItemService(
				InfoItemObjectProvider.class, className);

		Object object = infoItemObjectProvider.getInfoItem(classPK);

		InfoFieldValue<Object> infoFieldValue = _getTitleInfoFieldValue(
			infoItemFieldValuesProvider, object);

		String escapedTitle = StringUtil.removeSubstrings(
			(String)infoFieldValue.getValue(locale),
			PropsValues.DL_CHAR_BLACKLIST);

		Optional<TranslationInfoItemFieldValuesExporter>
			exportFileFormatOptional =
				_translationInfoItemFieldValuesExporterTracker.
					getTranslationInfoItemFieldValuesExporterOptional(
						exportMimeType);

		TranslationInfoItemFieldValuesExporter
			translationInfoItemFieldValuesExporter =
				exportFileFormatOptional.orElseThrow(
					() -> new PortalException(
						"Unknown export mime type: " + exportMimeType));

		for (String targetLanguageId : targetLanguageIds) {
			zipWriter.addEntry(
				StringBundler.concat(
					StringPool.FORWARD_SLASH, escapedTitle, StringPool.DASH,
					sourceLanguageId, StringPool.DASH, targetLanguageId,
					".xlf"),
				translationInfoItemFieldValuesExporter.
					exportInfoItemFieldValues(
						infoItemFieldValuesProvider.getInfoItemFieldValues(
							object),
						LocaleUtil.fromLanguageId(sourceLanguageId),
						LocaleUtil.fromLanguageId(targetLanguageId)));
		}
	}

	private String _getClassName(
		ResourceRequest resourceRequest, long[] segmentsExperienceIds) {

		if (ArrayUtil.isEmpty(segmentsExperienceIds) ||
			((segmentsExperienceIds.length == 1) &&
			 (segmentsExperienceIds[0] ==
				 SegmentsExperienceConstants.ID_DEFAULT))) {

			return _portal.getClassName(
				ParamUtil.getLong(resourceRequest, "classNameId"));
		}

		return SegmentsExperience.class.getName();
	}

	private long[] _getClassPKs(
		ResourceRequest resourceRequest, long[] segmentsExperienceIds) {

		if (ArrayUtil.isEmpty(segmentsExperienceIds) ||
			((segmentsExperienceIds.length == 1) &&
			 (segmentsExperienceIds[0] ==
				 SegmentsExperienceConstants.ID_DEFAULT))) {

			return new long[] {ParamUtil.getLong(resourceRequest, "classPK")};
		}

		return segmentsExperienceIds;
	}

	private String _getModelClassName(ResourceRequest resourceRequest) {
		return _portal.getClassName(
			ParamUtil.getLong(resourceRequest, "classNameId"));
	}

	private long _getModelClassPK(ResourceRequest resourceRequest) {
		return ParamUtil.getLong(resourceRequest, "classPK");
	}

	private InfoFieldValue<Object> _getTitleInfoFieldValue(
		InfoItemFieldValuesProvider<Object> infoItemFieldValuesProvider,
		Object object) {

		InfoFieldValue<Object> infoFieldValue =
			infoItemFieldValuesProvider.getInfoFieldValue(object, "title");

		if (infoFieldValue != null) {
			return infoFieldValue;
		}

		return infoItemFieldValuesProvider.getInfoFieldValue(object, "name");
	}

	private String _getZipFileName(
			String className, long classPK, String sourceLanguageId,
			Locale locale)
		throws NoSuchInfoItemException {

		InfoItemFieldValuesProvider<Object> infoItemFieldValuesProvider =
			_infoItemServiceTracker.getFirstInfoItemService(
				InfoItemFieldValuesProvider.class, className);

		InfoItemObjectProvider<Object> infoItemObjectProvider =
			_infoItemServiceTracker.getFirstInfoItemService(
				InfoItemObjectProvider.class, className);

		Object object = infoItemObjectProvider.getInfoItem(classPK);

		InfoFieldValue<Object> infoFieldValue = _getTitleInfoFieldValue(
			infoItemFieldValuesProvider, object);

		String escapedTitle = StringUtil.removeSubstrings(
			(String)infoFieldValue.getValue(locale),
			PropsValues.DL_CHAR_BLACKLIST);

		return StringBundler.concat(
			escapedTitle, StringPool.DASH, sourceLanguageId, ".zip");
	}

	@Reference
	private InfoItemServiceTracker _infoItemServiceTracker;

	@Reference
	private Portal _portal;

	@Reference
	private TranslationInfoItemFieldValuesExporterTracker
		_translationInfoItemFieldValuesExporterTracker;

}