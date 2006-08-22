/**
 * Copyright (c) 2000-2006 Liferay, LLC. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.portlet.alfresco.service.impl;

import com.liferay.portal.PortalException;
import com.liferay.portlet.alfresco.service.spring.AlfrescoContentLocalService;
import com.liferay.portlet.alfrescocontent.util.AuthenticationUtils;
import com.liferay.portlet.alfrescocontent.util.WebServiceFactory;
import com.liferay.util.Validator;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.alfresco.webservice.content.Content;
import org.alfresco.webservice.content.ContentServiceSoapBindingStub;
import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.types.StoreEnum;
import org.alfresco.webservice.util.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="AlfrescoContentLocalServiceImpl.java.html"><b><i>View Source</i></b>
 * </a>
 * 
 * @author Michael Young
 * 
 */
public class AlfrescoContentLocalServiceImpl implements
	AlfrescoContentLocalService {

	public String getContent(String uuid, String path,
		String alfrescoWebClientURL, String userId, String password)
		throws PortalException {

		String content = null;

		try {
			AuthenticationUtils.startSession(alfrescoWebClientURL, userId,
				password);

			ContentServiceSoapBindingStub contentService = WebServiceFactory
				.getContentService(alfrescoWebClientURL);

			Reference reference = new Reference(_SPACES_STORE, uuid, path);

			Predicate predicate = new Predicate(new Reference[] { reference },
				_SPACES_STORE, null);

			Content[] readResult = contentService.read(predicate,
				Constants.PROP_CONTENT);

			content = getContent(readResult[0]);
		}
		catch (Throwable e) {
			throw new PortalException(e);
		}
		finally {
			try {
				AuthenticationUtils.endSession(alfrescoWebClientURL);
			}
			catch (Throwable e) {
				throw new PortalException(e);
			}
		}

		return content;

	}

	public ResultSetRow[] getChildNodes(String uuid, String alfrescoWebClientURL,
		String userId, String password) throws PortalException {

		ResultSetRow[] rows = null;

		try {
			AuthenticationUtils.startSession(alfrescoWebClientURL, userId,
				password);

			RepositoryServiceSoapBindingStub repositoryService = WebServiceFactory
				.getRepositoryService(alfrescoWebClientURL);

			Reference reference = null;

			if (Validator.isNull(uuid)) {
				reference = new Reference(_SPACES_STORE, null,
					_COMPANY_HOME_PATH);
			}
			else {
				reference = new Reference(_SPACES_STORE, uuid, null);
			}

			QueryResult result = repositoryService.queryChildren(reference);

			rows = result.getResultSet().getRows();
		}
		catch (Exception e) {
			throw new PortalException(e);
		}
		finally {
			try {
				AuthenticationUtils.endSession(alfrescoWebClientURL);
			}
			catch (Exception e) {
				throw new PortalException(e);
			}
		}

		return rows;
	}

	public Node getNode(String uuid, String alfrescoWebClientURL,
		String userId, String password) throws PortalException {

		Node[] nodes = null;

		try {
			AuthenticationUtils.startSession(alfrescoWebClientURL, userId,
				password);

			RepositoryServiceSoapBindingStub repositoryService = WebServiceFactory
				.getRepositoryService(alfrescoWebClientURL);

			Reference reference = new Reference(_SPACES_STORE, uuid, null);

			Predicate predicate = new Predicate(new Reference[] { reference },
				_SPACES_STORE, null);
			
			nodes = repositoryService.get(predicate);

		}
		catch (Exception e) {
			throw new PortalException(e);
		}
		finally {
			try {
				AuthenticationUtils.endSession(alfrescoWebClientURL);
			}
			catch (Exception e) {
				throw new PortalException(e);
			}
		}

		return nodes[0];
	}
	
	protected String getContent(Content content) throws Exception {
		StringBuffer sb = new StringBuffer();

		String ticket = AuthenticationUtils.getCurrentTicket();

		URL url = new URL(content.getUrl() + "?ticket=" + ticket);

		URLConnection conn = url.openConnection();

		InputStream is = conn.getInputStream();

		int read = is.read();

		while (read != -1) {
			sb.append((char) read);

			read = is.read();
		}

		return sb.toString();
	}

	private static final Store _SPACES_STORE = new Store(StoreEnum.workspace,
		"SpacesStore");

	private static final String _COMPANY_HOME_PATH = "/app:company_home";

	private static Log _log = LogFactory
		.getLog(AlfrescoContentLocalServiceImpl.class);

}