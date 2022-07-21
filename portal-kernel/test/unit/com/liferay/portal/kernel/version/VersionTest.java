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

package com.liferay.portal.kernel.version;

import com.liferay.petra.string.StringPool;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Luis Ortiz
 */
public class VersionTest {

	@Test
	public void testCompareEqualVersions() {
		Version version1 = Version.parseVersion("1.2.3");

		Version version2 = Version.parseVersion("1.2.3.");

		Assert.assertEquals(0, version1.compareTo(version2));
	}

	@Test
	public void testCompareStepVersions() {
		Version version1 = Version.parseVersion("1.2.3.Step-1");

		Version version2 = Version.parseVersion("1.2.3.Step-2");

		Assert.assertTrue(version1.compareTo(version2) < 0);
	}

	@Test
	public void testCompareStepWithUpdatedVersions() {
		Version version1 = Version.parseVersion("1.2.3");

		Version version2 = Version.parseVersion("1.2.3.Step-2");

		Assert.assertTrue(version1.compareTo(version2) > 0);
	}

	@Test
	public void testEqualVersions() {
		Version version1 = Version.parseVersion("1.2.3");

		Version version2 = Version.parseVersion("1.2.3.");

		Assert.assertTrue(version1.equals(version2));
	}

	@Test
	public void testNotEqualVersions() {
		Version version1 = Version.parseVersion("1.2.3");

		Version version2 = Version.parseVersion("1.2.3.Step-4");

		Assert.assertTrue(!version1.equals(version2));
	}

	@Test
	public void testParseEmptyVersion() {
		Version version = Version.parseVersion("");

		Assert.assertEquals(0, version.getMajor());
		Assert.assertEquals(0, version.getMinor());
		Assert.assertEquals(0, version.getMicro());
		Assert.assertEquals(StringPool.BLANK, version.getQualifier());
	}

	@Test
	public void testParseNullVersion() {
		Version version = Version.parseVersion(null);

		Assert.assertEquals(0, version.getMajor());
		Assert.assertEquals(0, version.getMinor());
		Assert.assertEquals(0, version.getMicro());
		Assert.assertEquals(StringPool.BLANK, version.getQualifier());
	}

	@Test
	public void testParseStepVersion() {
		Version version = Version.parseVersion("1.2.3.Step-4");

		Assert.assertEquals(1, version.getMajor());
		Assert.assertEquals(2, version.getMinor());
		Assert.assertEquals(3, version.getMicro());
		Assert.assertEquals("Step-4", version.getQualifier());
	}

	@Test
	public void testParseVersion() {
		Version version = Version.parseVersion("1.2.3");

		Assert.assertEquals(1, version.getMajor());
		Assert.assertEquals(2, version.getMinor());
		Assert.assertEquals(3, version.getMicro());
		Assert.assertEquals(StringPool.BLANK, version.getQualifier());
	}

	@Test
	public void testParseWrongFormatVersion() {
		Version version = Version.parseVersion("V1.4.3");

		Assert.assertEquals(0, version.getMajor());
		Assert.assertEquals(0, version.getMinor());
		Assert.assertEquals(0, version.getMicro());
		Assert.assertEquals(StringPool.BLANK, version.getQualifier());
	}

	@Test
	public void testToStringStepVersion() {
		Version version = Version.parseVersion("1.2.3.Step-4");

		Assert.assertEquals("1.2.3.Step-4", version.toString());
	}

	@Test
	public void testToStringUpdatedVersion() {
		Version version = Version.parseVersion("1.2.3.");

		Assert.assertEquals("1.2.3", version.toString());
	}

}