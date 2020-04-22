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

package com.liferay.dynamic.data.mapping.uad.exporter;

import com.liferay.dynamic.data.mapping.model.DDMContent;
import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.service.DDMContentLocalService;
import com.liferay.dynamic.data.mapping.uad.util.DDMUADUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.user.associated.data.exporter.UADExporter;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Brian Wing Shun Chan
 */
@Component(immediate = true, service = UADExporter.class)
public class DDMFormInstanceRecordUADExporter
	extends BaseDDMFormInstanceRecordUADExporter {

	@Override
	protected String toXmlString(DDMFormInstanceRecord ddmFormInstanceRecord) {
		StringBundler sb = new StringBundler(33);

		String className =
			"com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord";

		sb.append("<model><model-name>");
		sb.append(className);
		sb.append("</model-name>");

		sb.append("<column><column-name>");
		sb.append("formInstanceName</column-name><column-value><![CDATA[");
		sb.append(_getFormInstanceName(ddmFormInstanceRecord));
		sb.append("]]></column-value></column>");
		sb.append("<column><column-name>");
		sb.append("formInstanceRecordId</column-name><column-value><![CDATA[");
		sb.append(ddmFormInstanceRecord.getFormInstanceRecordId());
		sb.append("]]></column-value></column>");
		sb.append("<column><column-name>");
		sb.append("versionUserId</column-name><column-value><![CDATA[");
		sb.append(ddmFormInstanceRecord.getVersionUserId());
		sb.append("]]></column-value></column>");
		sb.append("<column><column-name>");
		sb.append("versionUserName</column-name><column-value><![CDATA[");
		sb.append(ddmFormInstanceRecord.getVersionUserName());
		sb.append("]]></column-value></column>");
		sb.append("<column><column-name>");
		sb.append("userId</column-name><column-value><![CDATA[");
		sb.append(ddmFormInstanceRecord.getUserId());
		sb.append("]]></column-value></column>");
		sb.append("<column><column-name>");
		sb.append("userName</column-name><column-value><![CDATA[");
		sb.append(ddmFormInstanceRecord.getUserName());
		sb.append("]]></column-value></column>");
		sb.append("<column><column-name>");
		sb.append("version</column-name><column-value><![CDATA[");
		sb.append(ddmFormInstanceRecord.getVersion());
		sb.append("]]></column-value></column>");
		sb.append(_getFieldValuesDDMContent(ddmFormInstanceRecord));
		sb.append("</model>");

		return sb.toString();
	}

	private String _getFieldValuesDDMContent(
		DDMFormInstanceRecord ddmFormInstanceRecord) {

		try {
			StringBundler sb = new StringBundler(10);

			sb.append("<column><model><model-name>");
			sb.append("com.liferay.dynamic.data.mapping.model.DDMContent");
			sb.append("</model-name>");

			DDMContent ddmContent = _ddmContentLocalService.getDDMContent(
				ddmFormInstanceRecord.getStorageId());

			JSONObject dataJSONObject = JSONFactoryUtil.createJSONObject(
				ddmContent.getData());

			JSONArray fieldValuesJSONArray = dataJSONObject.getJSONArray(
				"fieldValues");

			fieldValuesJSONArray.forEach(
				fieldValue -> {
					JSONObject fieldValueJSONObject = (JSONObject)fieldValue;

					sb.append("<column><column-name>");
					sb.append(fieldValueJSONObject.get("name"));
					sb.append("</column-name>");
					sb.append("<column-value><![CDATA[");
					sb.append(fieldValueJSONObject.get("value"));
					sb.append("]]></column-value></column>");
				});

			sb.append("</model></column>");

			return sb.toString();
		}
		catch (PortalException portalException) {
			_log.error(
				"Unable to get field values from formInstanceRecord " +
					ddmFormInstanceRecord.getFormInstanceRecordId(),
				portalException);
		}

		return null;
	}

	/**
	 * Extracts DDMFormInstance name from xml structure stored in database
	 * @return DDMFormInstance Name
	 */
	private String _getFormInstanceName(
		DDMFormInstanceRecord ddmFormInstanceRecord) {

		try {
			DDMFormInstance ddmFormInstance =
				ddmFormInstanceRecord.getFormInstance();

			Document document = DDMUADUtil.toDocument(
				ddmFormInstance.getName());

			Node firstChildNode = document.getFirstChild();

			NodeList childNodes = firstChildNode.getChildNodes();

			Node formInstanceNameNode = childNodes.item(0);

			return formInstanceNameNode.getTextContent();
		}
		catch (PortalException portalException) {
			_log.error(
				"Unable to get name from formInstance " +
					ddmFormInstanceRecord.getFormInstanceId(),
				portalException);
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DDMFormInstanceRecordUADExporter.class);

	@Reference
	private DDMContentLocalService _ddmContentLocalService;

}