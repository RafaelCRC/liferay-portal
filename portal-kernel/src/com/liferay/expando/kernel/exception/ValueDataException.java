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

package com.liferay.expando.kernel.exception;

import com.liferay.portal.kernel.exception.PortalException;

import java.util.Locale;

/**
 * @author Brian Wing Shun Chan
 */
public class ValueDataException extends PortalException {

	public static class MismatchColumnTypeException extends ValueDataException {

		public MismatchColumnTypeException(String msg) {
			super(msg);
		}

	}

	public static class MustInformDefaultLocale extends ValueDataException {

		public MustInformDefaultLocale(Locale locale) {
			super(
				"A value for the default locale (" + locale.getLanguage() +
					") must be defined");
		}

	}

	public static class UnsupportedColumnTypeException
		extends ValueDataException {

		public UnsupportedColumnTypeException(String msg) {
			super(msg);
		}

	}

	private ValueDataException(String msg) {
		super(msg);
	}

}