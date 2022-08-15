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

package com.liferay.account.service.impl;

import com.liferay.account.constants.AccountActionKeys;
import com.liferay.account.model.AccountEntry;
import com.liferay.account.service.base.AccountEntryServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.permission.PortalPermission;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = {
		"json.web.service.context.name=account",
		"json.web.service.context.path=AccountEntry"
	},
	service = AopService.class
)
public class AccountEntryServiceImpl extends AccountEntryServiceBaseImpl {

	@Override
	public void activateAccountEntries(long[] accountEntryIds)
		throws PortalException {

		for (long accountEntryId : accountEntryIds) {
			activateAccountEntry(accountEntryId);
		}
	}

	@Override
	public AccountEntry activateAccountEntry(long accountEntryId)
		throws PortalException {

		_accountEntryModelResourcePermission.check(
			getPermissionChecker(), accountEntryId, ActionKeys.UPDATE);

		return accountEntryLocalService.activateAccountEntry(accountEntryId);
	}

	@Override
	public AccountEntry addAccountEntry(
			long userId, long parentAccountEntryId, String name,
			String description, String[] domains, String email,
			byte[] logoBytes, String taxIdNumber, String type, boolean active,
			ServiceContext serviceContext)
		throws PortalException {

		_portalPermission.check(
			getPermissionChecker(), AccountActionKeys.ADD_ACCOUNT_ENTRY);

		return accountEntryLocalService.addAccountEntry(
			userId, parentAccountEntryId, name, description, domains, email,
			logoBytes, taxIdNumber, type, active, serviceContext);
	}

	/**
	 * @deprecated As of Cavanaugh (7.4.x), replaced by {@link #addAccountEntry(long, long, String, String, String[], String, byte[], String, String, boolean, ServiceContext)}
	 */
	@Deprecated
	@Override
	public AccountEntry addAccountEntry(
			long userId, long parentAccountEntryId, String name,
			String description, String[] domains, String email,
			byte[] logoBytes, String taxIdNumber, String type, int status,
			ServiceContext serviceContext)
		throws PortalException {

		return addAccountEntry(
			userId, parentAccountEntryId, name, description, domains, email,
			logoBytes, taxIdNumber, type, _isActive(status), serviceContext);
	}

	@Override
	public AccountEntry addOrUpdateAccountEntry(
			String externalReferenceCode, long userId,
			long parentAccountEntryId, String name, String description,
			String[] domains, String emailAddress, byte[] logoBytes,
			String taxIdNumber, String type, boolean active,
			ServiceContext serviceContext)
		throws PortalException {

		PermissionChecker permissionChecker = getPermissionChecker();

		AccountEntry accountEntry =
			accountEntryLocalService.fetchAccountEntryByExternalReferenceCode(
				permissionChecker.getCompanyId(), externalReferenceCode);

		if (accountEntry == null) {
			_portalPermission.check(
				permissionChecker, AccountActionKeys.ADD_ACCOUNT_ENTRY);
		}
		else {
			_accountEntryModelResourcePermission.check(
				permissionChecker, permissionChecker.getCompanyId(),
				ActionKeys.UPDATE);
		}

		return accountEntryLocalService.addOrUpdateAccountEntry(
			externalReferenceCode, userId, parentAccountEntryId, name,
			description, domains, emailAddress, logoBytes, taxIdNumber, type,
			active, serviceContext);
	}

	/**
	 * @deprecated As of Cavanaugh (7.4.x), replaced by {@link #addOrUpdateAccountEntry(String, long, long, String, String, String[], String, byte[], String, String, boolean, ServiceContext)}
	 */
	@Deprecated
	@Override
	public AccountEntry addOrUpdateAccountEntry(
			String externalReferenceCode, long userId,
			long parentAccountEntryId, String name, String description,
			String[] domains, String emailAddress, byte[] logoBytes,
			String taxIdNumber, String type, int status,
			ServiceContext serviceContext)
		throws PortalException {

		return addOrUpdateAccountEntry(
			externalReferenceCode, userId, parentAccountEntryId, name,
			description, domains, emailAddress, logoBytes, taxIdNumber, type,
			_isActive(status), serviceContext);
	}

	@Override
	public void deactivateAccountEntries(long[] accountEntryIds)
		throws PortalException {

		for (long accountEntryId : accountEntryIds) {
			deactivateAccountEntry(accountEntryId);
		}
	}

	@Override
	public AccountEntry deactivateAccountEntry(long accountEntryId)
		throws PortalException {

		_accountEntryModelResourcePermission.check(
			getPermissionChecker(), accountEntryId, ActionKeys.DELETE);

		return accountEntryLocalService.deactivateAccountEntry(accountEntryId);
	}

	@Override
	public void deleteAccountEntries(long[] accountEntryIds)
		throws PortalException {

		for (long accountEntryId : accountEntryIds) {
			deleteAccountEntry(accountEntryId);
		}
	}

	@Override
	public void deleteAccountEntry(long accountEntryId) throws PortalException {
		_accountEntryModelResourcePermission.check(
			getPermissionChecker(), accountEntryId, ActionKeys.DELETE);

		accountEntryLocalService.deleteAccountEntry(accountEntryId);
	}

	@Override
	public AccountEntry fetchAccountEntry(long accountEntryId)
		throws PortalException {

		_accountEntryModelResourcePermission.check(
			getPermissionChecker(), accountEntryId, ActionKeys.VIEW);

		return accountEntryLocalService.fetchAccountEntry(accountEntryId);
	}

	@Override
	public List<AccountEntry> getAccountEntries(
			long companyId, boolean active, int start, int end,
			OrderByComparator<AccountEntry> orderByComparator)
		throws PortalException {

		PermissionChecker permissionChecker = getPermissionChecker();

		if (!permissionChecker.hasPermission(
				null, AccountEntry.class.getName(), companyId,
				ActionKeys.VIEW)) {

			throw new PrincipalException.MustHavePermission(
				permissionChecker, AccountEntry.class.getName(), 0,
				ActionKeys.VIEW);
		}

		return accountEntryLocalService.getAccountEntries(
			companyId, active, start, end, orderByComparator);
	}

	/**
	 * @deprecated As of Cavanaugh (7.4.x), replaced by {@link #getAccountEntries(long, boolean, int, int, OrderByComparator)}
	 */
	@Deprecated
	@Override
	public List<AccountEntry> getAccountEntries(
			long companyId, int status, int start, int end,
			OrderByComparator<AccountEntry> orderByComparator)
		throws PortalException {

		return getAccountEntries(
			companyId, _isActive(status), start, end, orderByComparator);
	}

	// @Deprecated

	@Override
	public AccountEntry getAccountEntry(long accountEntryId)
		throws PortalException {

		AccountEntry accountEntry = accountEntryLocalService.getAccountEntry(
			accountEntryId);

		_accountEntryModelResourcePermission.check(
			getPermissionChecker(), accountEntryId, ActionKeys.VIEW);

		return accountEntry;
	}

	@Override
	public BaseModelSearchResult<AccountEntry> searchAccountEntries(
			String keywords, LinkedHashMap<String, Object> params, int cur,
			int delta, String orderByField, boolean reverse)
		throws PortalException {

		PermissionChecker permissionChecker = getPermissionChecker();

		if (params == null) {
			params = new LinkedHashMap<>();
		}

		params.put("permissionUserId", permissionChecker.getUserId());

		return accountEntryLocalService.searchAccountEntries(
			permissionChecker.getCompanyId(), keywords, params, cur, delta,
			orderByField, reverse);
	}

	@Override
	public AccountEntry updateAccountEntry(AccountEntry accountEntry)
		throws PortalException {

		_accountEntryModelResourcePermission.check(
			getPermissionChecker(), accountEntry, ActionKeys.UPDATE);

		return accountEntryLocalService.updateAccountEntry(accountEntry);
	}

	public AccountEntry updateAccountEntry(
			long accountEntryId, long parentAccountEntryId, String name,
			String description, boolean deleteLogo, String[] domains,
			String emailAddress, byte[] logoBytes, String taxIdNumber,
			boolean active, ServiceContext serviceContext)
		throws PortalException {

		_accountEntryModelResourcePermission.check(
			getPermissionChecker(), accountEntryId, ActionKeys.UPDATE);

		return accountEntryLocalService.updateAccountEntry(
			accountEntryId, parentAccountEntryId, name, description, deleteLogo,
			domains, emailAddress, logoBytes, taxIdNumber, active,
			serviceContext);
	}

	/**
	 * @deprecated As of Cavanaugh (7.4.x), replaced by {@link #updateAccountEntry(long, long, String, String, boolean, String[], String, byte[], String, boolean, ServiceContext)}
	 */
	@Deprecated
	@Override
	public AccountEntry updateAccountEntry(
			long accountEntryId, long parentAccountEntryId, String name,
			String description, boolean deleteLogo, String[] domains,
			String emailAddress, byte[] logoBytes, String taxIdNumber,
			int status, ServiceContext serviceContext)
		throws PortalException {

		return updateAccountEntry(
			accountEntryId, parentAccountEntryId, name, description, deleteLogo,
			domains, emailAddress, logoBytes, taxIdNumber, _isActive(status),
			serviceContext);
	}

	@Override
	public AccountEntry updateExternalReferenceCode(
			long accountEntryId, String externalReferenceCode)
		throws PortalException {

		_accountEntryModelResourcePermission.check(
			getPermissionChecker(), accountEntryId, ActionKeys.UPDATE);

		return accountEntryLocalService.updateExternalReferenceCode(
			accountEntryId, externalReferenceCode);
	}

	private boolean _isActive(int status) {
		return Objects.equals(WorkflowConstants.STATUS_APPROVED, status);
	}

	@Reference(
		target = "(model.class.name=com.liferay.account.model.AccountEntry)"
	)
	private ModelResourcePermission<AccountEntry>
		_accountEntryModelResourcePermission;

	@Reference
	private PortalPermission _portalPermission;

}