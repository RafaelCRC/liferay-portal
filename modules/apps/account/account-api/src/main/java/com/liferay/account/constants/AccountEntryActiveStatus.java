package com.liferay.account.constants;

import com.liferay.portal.kernel.workflow.WorkflowConstants;

/**
 * @author Drew Brokke
 */
public enum AccountEntryActiveStatus {

	ACTIVE(true, WorkflowConstants.STATUS_APPROVED),
	ALL(null, WorkflowConstants.STATUS_ANY),
	INACTIVE(false, WorkflowConstants.STATUS_INACTIVE);

	private AccountEntryActiveStatus(Boolean active, int statusCode) {
		_active = active;
		_statusCode = statusCode;
	}

	public int getStatusCode() {
		return _statusCode;
	}

	private final int _statusCode;

	public Boolean getActive() {
		return _active;
	}

	private final Boolean _active;

	public static final String FIELD_NAME = "active";
	public static final String PARAM_NAME = "activeStatus";

}
