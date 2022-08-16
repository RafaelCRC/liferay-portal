package com.liferay.account.constants;

import com.liferay.portal.kernel.workflow.WorkflowConstants;

/**
 * @author Drew Brokke
 */
public enum AccountEntryActiveSearchParam {

	ACTIVE(WorkflowConstants.STATUS_APPROVED),
	ALL(WorkflowConstants.STATUS_ANY),
	INACTIVE(WorkflowConstants.STATUS_INACTIVE);

	private AccountEntryActiveSearchParam(int statusCode) {
		_statusCode = statusCode;
	}

	public int getValue() {
		return _statusCode;
	}

	private final int _statusCode;

}
