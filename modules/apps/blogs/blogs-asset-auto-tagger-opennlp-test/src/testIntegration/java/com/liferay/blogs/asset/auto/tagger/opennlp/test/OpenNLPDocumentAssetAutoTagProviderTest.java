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

package com.liferay.blogs.asset.auto.tagger.opennlp.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.asset.auto.tagger.AssetAutoTagProvider;
import com.liferay.blogs.model.BlogsEntry;
import com.liferay.blogs.service.BlogsEntryLocalService;
import com.liferay.petra.function.UnsafeRunnable;
import com.liferay.portal.configuration.test.util.ConfigurationTemporarySwapper;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Cristina González
 * @author Alicia García González
 */
@RunWith(Arquillian.class)
public class OpenNLPDocumentAssetAutoTagProviderTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_userId = getUserId();
		_serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _userId);
	}

	@Test
	public void testGetTagNamesWithTextFile() throws Exception {
		String fileName = _FILE_NAME + ".txt";

		BlogsEntry blogsEntry = _blogsEntryLocalService.addEntry(_userId,_FILE_NAME, new String(
			FileUtil.getBytes(getClass(), "dependencies/" + fileName)),
			_serviceContext);

		_testWithOpenNLPAutoTagProviderEnabled(
			() -> {
				Collection<String> expectedTagNames = Arrays.asList(
					"AT ALL.", "Adventures", "Alice", "Australia", "Bill",
					"General Information About Project", "IRS",
					"Internal Revenue Service", "Lewis Carroll", "Mary Ann",
					"Michael Hart", "Michael S. Hart", "Mississippi", "NOT",
					"Paris", "Pat", "Pepper", "Queens", "Rabbit", "Rome",
					"Salt Lake City", "THERE", "United States", "White Rabbit",
					"William", "YOUR");


				Collection<String> actualTagNames =
					_assetAutoTagProvider.getTagNames(blogsEntry);

				Assert.assertEquals( "Different number of tags", expectedTagNames.size(),
					actualTagNames.size());
				Assert.assertTrue(actualTagNames.toString(),
					actualTagNames.containsAll(expectedTagNames));
			});
	}

	@Test
	public void testGetTagNamesWithTextFileAndDisabledConfiguration()
		throws Exception {

		String fileName = _FILE_NAME + ".txt";

		BlogsEntry blogsEntry = _blogsEntryLocalService.addEntry(_userId,_FILE_NAME, new String(
				FileUtil.getBytes(getClass(), "dependencies/" + fileName)),
			_serviceContext);

		_testWithOpenNLPAutoTagProviderDisabled(
			() -> {
				Collection<String> tagNames = _assetAutoTagProvider.getTagNames(
					blogsEntry);

				Assert.assertEquals(tagNames.toString(), 0, tagNames.size());
			});
	}

	@Test
	public void testGetTagNamesWithTextInNoEnglishLanguage() throws Exception {
		String fileName = _FILE_NAME_NO_ENGLISH + ".txt";

		BlogsEntry blogsEntry = _blogsEntryLocalService.addEntry(_userId,
			_FILE_NAME, new String(
				FileUtil.getBytes(getClass(), "dependencies/" + fileName)),
			_serviceContext);


		_testWithOpenNLPAutoTagProviderEnabled(
			() -> {
				Collection<String> tagNames = _assetAutoTagProvider.getTagNames(
					blogsEntry);

				Assert.assertEquals(tagNames.toString(), 0, tagNames.size());
			});
	}

	private void _testWithOpenNLPAutoTagProviderDisabled(
			UnsafeRunnable<Exception> unsafeRunnable)
		throws Exception {

		try (ConfigurationTemporarySwapper
				autoTaggerConfigurationTemporarySwapper =
					new ConfigurationTemporarySwapper(
						_OPENNLP_AUTO_TAGGER_CONFIGURATION_CLASS_NAME,
						new HashMapDictionary<String, Object>() {
							{
								put("enabled", false);
							}
						})) {

			try (ConfigurationTemporarySwapper
					blogsAutoTagProviderConfigurationTemporarySwapper =
						new ConfigurationTemporarySwapper(
							_OPENNLP_AUTO_TAG_PROVIDER_CONFIGURATION_CLASS_NAME,
							new HashMapDictionary<String, Object>() {
								{
									put("enabled", true);
								}
							})) {

				unsafeRunnable.run();
			}
		}
	}

	private void _testWithOpenNLPAutoTagProviderEnabled(
			UnsafeRunnable<Exception> unsafeRunnable)
		throws Exception {

		try (ConfigurationTemporarySwapper
				autoTaggerConfigurationTemporarySwapper =
					new ConfigurationTemporarySwapper(
						_OPENNLP_AUTO_TAGGER_CONFIGURATION_CLASS_NAME,
						new HashMapDictionary<String, Object>() {
							{
								put("enabled", true);
								put("confidenceThreshold", 0.9);
							}
						})) {

			try (ConfigurationTemporarySwapper
					blogsAutoTagProviderConfigurationTemporarySwapper =
						new ConfigurationTemporarySwapper(
							_OPENNLP_AUTO_TAG_PROVIDER_CONFIGURATION_CLASS_NAME,
							new HashMapDictionary<String, Object>() {
								{
									put("enabled", true);
								}
							})) {

				unsafeRunnable.run();
			}
		}
	}

	private static final String _FILE_NAME = "Alice's Adventures in Wonderland, by Lewis Carroll";
	private static final String _FILE_NAME_NO_ENGLISH = "25328-0";

	private static final String
		_OPENNLP_AUTO_TAG_PROVIDER_CONFIGURATION_CLASS_NAME =
			"com.liferay.blogs.asset.auto.tagger.opennlp.internal." +
				"configuration." +
					"OpenNLPDocumentAssetAutoTagProviderCompanyConfiguration";

	private static final String _OPENNLP_AUTO_TAGGER_CONFIGURATION_CLASS_NAME =
		"com.liferay.asset.auto.tagger.opennlp.internal.configuration." +
			"OpenNLPDocumentAssetAutoTaggerCompanyConfiguration";

	@Inject(
		filter = "component.name=com.liferay.blogs.asset.auto.tagger.opennlp.internal.OpenNLPDocumentAssetAutoTagProvider"
	)
	private AssetAutoTagProvider _assetAutoTagProvider;

	@Inject
	private BlogsEntryLocalService _blogsEntryLocalService;

	@DeleteAfterTestRun
	private Group _group;

	private long _userId;

	private ServiceContext _serviceContext;

	protected long getUserId() throws Exception {
		return TestPropsValues.getUserId();
	}
}