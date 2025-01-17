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

package com.liferay.portal.workflow.kaleo.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.model.ShardedModel;
import com.liferay.portal.kernel.model.change.tracking.CTModel;

import java.util.Date;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model interface for the KaleoTimerInstanceToken service. Represents a row in the &quot;KaleoTimerInstanceToken&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.portal.workflow.kaleo.model.impl.KaleoTimerInstanceTokenModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.portal.workflow.kaleo.model.impl.KaleoTimerInstanceTokenImpl</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see KaleoTimerInstanceToken
 * @generated
 */
@ProviderType
public interface KaleoTimerInstanceTokenModel
	extends BaseModel<KaleoTimerInstanceToken>,
			CTModel<KaleoTimerInstanceToken>, GroupedModel, MVCCModel,
			ShardedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a kaleo timer instance token model instance should use the {@link KaleoTimerInstanceToken} interface instead.
	 */

	/**
	 * Returns the primary key of this kaleo timer instance token.
	 *
	 * @return the primary key of this kaleo timer instance token
	 */
	@Override
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this kaleo timer instance token.
	 *
	 * @param primaryKey the primary key of this kaleo timer instance token
	 */
	@Override
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the mvcc version of this kaleo timer instance token.
	 *
	 * @return the mvcc version of this kaleo timer instance token
	 */
	@Override
	public long getMvccVersion();

	/**
	 * Sets the mvcc version of this kaleo timer instance token.
	 *
	 * @param mvccVersion the mvcc version of this kaleo timer instance token
	 */
	@Override
	public void setMvccVersion(long mvccVersion);

	/**
	 * Returns the ct collection ID of this kaleo timer instance token.
	 *
	 * @return the ct collection ID of this kaleo timer instance token
	 */
	@Override
	public long getCtCollectionId();

	/**
	 * Sets the ct collection ID of this kaleo timer instance token.
	 *
	 * @param ctCollectionId the ct collection ID of this kaleo timer instance token
	 */
	@Override
	public void setCtCollectionId(long ctCollectionId);

	/**
	 * Returns the kaleo timer instance token ID of this kaleo timer instance token.
	 *
	 * @return the kaleo timer instance token ID of this kaleo timer instance token
	 */
	public long getKaleoTimerInstanceTokenId();

	/**
	 * Sets the kaleo timer instance token ID of this kaleo timer instance token.
	 *
	 * @param kaleoTimerInstanceTokenId the kaleo timer instance token ID of this kaleo timer instance token
	 */
	public void setKaleoTimerInstanceTokenId(long kaleoTimerInstanceTokenId);

	/**
	 * Returns the group ID of this kaleo timer instance token.
	 *
	 * @return the group ID of this kaleo timer instance token
	 */
	@Override
	public long getGroupId();

	/**
	 * Sets the group ID of this kaleo timer instance token.
	 *
	 * @param groupId the group ID of this kaleo timer instance token
	 */
	@Override
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this kaleo timer instance token.
	 *
	 * @return the company ID of this kaleo timer instance token
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this kaleo timer instance token.
	 *
	 * @param companyId the company ID of this kaleo timer instance token
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this kaleo timer instance token.
	 *
	 * @return the user ID of this kaleo timer instance token
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this kaleo timer instance token.
	 *
	 * @param userId the user ID of this kaleo timer instance token
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this kaleo timer instance token.
	 *
	 * @return the user uuid of this kaleo timer instance token
	 */
	@Override
	public String getUserUuid();

	/**
	 * Sets the user uuid of this kaleo timer instance token.
	 *
	 * @param userUuid the user uuid of this kaleo timer instance token
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this kaleo timer instance token.
	 *
	 * @return the user name of this kaleo timer instance token
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this kaleo timer instance token.
	 *
	 * @param userName the user name of this kaleo timer instance token
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this kaleo timer instance token.
	 *
	 * @return the create date of this kaleo timer instance token
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this kaleo timer instance token.
	 *
	 * @param createDate the create date of this kaleo timer instance token
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this kaleo timer instance token.
	 *
	 * @return the modified date of this kaleo timer instance token
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this kaleo timer instance token.
	 *
	 * @param modifiedDate the modified date of this kaleo timer instance token
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the kaleo class name of this kaleo timer instance token.
	 *
	 * @return the kaleo class name of this kaleo timer instance token
	 */
	@AutoEscape
	public String getKaleoClassName();

	/**
	 * Sets the kaleo class name of this kaleo timer instance token.
	 *
	 * @param kaleoClassName the kaleo class name of this kaleo timer instance token
	 */
	public void setKaleoClassName(String kaleoClassName);

	/**
	 * Returns the kaleo class pk of this kaleo timer instance token.
	 *
	 * @return the kaleo class pk of this kaleo timer instance token
	 */
	public long getKaleoClassPK();

	/**
	 * Sets the kaleo class pk of this kaleo timer instance token.
	 *
	 * @param kaleoClassPK the kaleo class pk of this kaleo timer instance token
	 */
	public void setKaleoClassPK(long kaleoClassPK);

	/**
	 * Returns the kaleo definition ID of this kaleo timer instance token.
	 *
	 * @return the kaleo definition ID of this kaleo timer instance token
	 */
	public long getKaleoDefinitionId();

	/**
	 * Sets the kaleo definition ID of this kaleo timer instance token.
	 *
	 * @param kaleoDefinitionId the kaleo definition ID of this kaleo timer instance token
	 */
	public void setKaleoDefinitionId(long kaleoDefinitionId);

	/**
	 * Returns the kaleo definition version ID of this kaleo timer instance token.
	 *
	 * @return the kaleo definition version ID of this kaleo timer instance token
	 */
	public long getKaleoDefinitionVersionId();

	/**
	 * Sets the kaleo definition version ID of this kaleo timer instance token.
	 *
	 * @param kaleoDefinitionVersionId the kaleo definition version ID of this kaleo timer instance token
	 */
	public void setKaleoDefinitionVersionId(long kaleoDefinitionVersionId);

	/**
	 * Returns the kaleo instance ID of this kaleo timer instance token.
	 *
	 * @return the kaleo instance ID of this kaleo timer instance token
	 */
	public long getKaleoInstanceId();

	/**
	 * Sets the kaleo instance ID of this kaleo timer instance token.
	 *
	 * @param kaleoInstanceId the kaleo instance ID of this kaleo timer instance token
	 */
	public void setKaleoInstanceId(long kaleoInstanceId);

	/**
	 * Returns the kaleo instance token ID of this kaleo timer instance token.
	 *
	 * @return the kaleo instance token ID of this kaleo timer instance token
	 */
	public long getKaleoInstanceTokenId();

	/**
	 * Sets the kaleo instance token ID of this kaleo timer instance token.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID of this kaleo timer instance token
	 */
	public void setKaleoInstanceTokenId(long kaleoInstanceTokenId);

	/**
	 * Returns the kaleo task instance token ID of this kaleo timer instance token.
	 *
	 * @return the kaleo task instance token ID of this kaleo timer instance token
	 */
	public long getKaleoTaskInstanceTokenId();

	/**
	 * Sets the kaleo task instance token ID of this kaleo timer instance token.
	 *
	 * @param kaleoTaskInstanceTokenId the kaleo task instance token ID of this kaleo timer instance token
	 */
	public void setKaleoTaskInstanceTokenId(long kaleoTaskInstanceTokenId);

	/**
	 * Returns the kaleo timer ID of this kaleo timer instance token.
	 *
	 * @return the kaleo timer ID of this kaleo timer instance token
	 */
	public long getKaleoTimerId();

	/**
	 * Sets the kaleo timer ID of this kaleo timer instance token.
	 *
	 * @param kaleoTimerId the kaleo timer ID of this kaleo timer instance token
	 */
	public void setKaleoTimerId(long kaleoTimerId);

	/**
	 * Returns the kaleo timer name of this kaleo timer instance token.
	 *
	 * @return the kaleo timer name of this kaleo timer instance token
	 */
	@AutoEscape
	public String getKaleoTimerName();

	/**
	 * Sets the kaleo timer name of this kaleo timer instance token.
	 *
	 * @param kaleoTimerName the kaleo timer name of this kaleo timer instance token
	 */
	public void setKaleoTimerName(String kaleoTimerName);

	/**
	 * Returns the blocking of this kaleo timer instance token.
	 *
	 * @return the blocking of this kaleo timer instance token
	 */
	public boolean getBlocking();

	/**
	 * Returns <code>true</code> if this kaleo timer instance token is blocking.
	 *
	 * @return <code>true</code> if this kaleo timer instance token is blocking; <code>false</code> otherwise
	 */
	public boolean isBlocking();

	/**
	 * Sets whether this kaleo timer instance token is blocking.
	 *
	 * @param blocking the blocking of this kaleo timer instance token
	 */
	public void setBlocking(boolean blocking);

	/**
	 * Returns the completion user ID of this kaleo timer instance token.
	 *
	 * @return the completion user ID of this kaleo timer instance token
	 */
	public long getCompletionUserId();

	/**
	 * Sets the completion user ID of this kaleo timer instance token.
	 *
	 * @param completionUserId the completion user ID of this kaleo timer instance token
	 */
	public void setCompletionUserId(long completionUserId);

	/**
	 * Returns the completion user uuid of this kaleo timer instance token.
	 *
	 * @return the completion user uuid of this kaleo timer instance token
	 */
	public String getCompletionUserUuid();

	/**
	 * Sets the completion user uuid of this kaleo timer instance token.
	 *
	 * @param completionUserUuid the completion user uuid of this kaleo timer instance token
	 */
	public void setCompletionUserUuid(String completionUserUuid);

	/**
	 * Returns the completed of this kaleo timer instance token.
	 *
	 * @return the completed of this kaleo timer instance token
	 */
	public boolean getCompleted();

	/**
	 * Returns <code>true</code> if this kaleo timer instance token is completed.
	 *
	 * @return <code>true</code> if this kaleo timer instance token is completed; <code>false</code> otherwise
	 */
	public boolean isCompleted();

	/**
	 * Sets whether this kaleo timer instance token is completed.
	 *
	 * @param completed the completed of this kaleo timer instance token
	 */
	public void setCompleted(boolean completed);

	/**
	 * Returns the completion date of this kaleo timer instance token.
	 *
	 * @return the completion date of this kaleo timer instance token
	 */
	public Date getCompletionDate();

	/**
	 * Sets the completion date of this kaleo timer instance token.
	 *
	 * @param completionDate the completion date of this kaleo timer instance token
	 */
	public void setCompletionDate(Date completionDate);

	/**
	 * Returns the workflow context of this kaleo timer instance token.
	 *
	 * @return the workflow context of this kaleo timer instance token
	 */
	@AutoEscape
	public String getWorkflowContext();

	/**
	 * Sets the workflow context of this kaleo timer instance token.
	 *
	 * @param workflowContext the workflow context of this kaleo timer instance token
	 */
	public void setWorkflowContext(String workflowContext);

	@Override
	public KaleoTimerInstanceToken cloneWithOriginalValues();

}