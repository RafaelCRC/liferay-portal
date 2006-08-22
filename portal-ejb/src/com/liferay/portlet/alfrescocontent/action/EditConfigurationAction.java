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

package com.liferay.portlet.alfrescocontent.action;

import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.util.Constants;
import com.liferay.portlet.PortletPreferencesFactory;
import com.liferay.portlet.alfresco.service.impl.AlfrescoContentLocalServiceImpl;
import com.liferay.util.ParamUtil;
import com.liferay.util.servlet.SessionMessages;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.types.StoreEnum;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="EditConfigurationAction.java.html"><b><i>View Source</i></b></a>
 * 
 * @author Brian Wing Shun Chan
 * @author Michael Young
 * 
 */
public class EditConfigurationAction extends PortletAction {
	protected static final Store STORE = new Store(StoreEnum.workspace,
		"SpacesStore");

	public void processAction(ActionMapping mapping, ActionForm form,
		PortletConfig config, ActionRequest req, ActionResponse res)
		throws Exception {

		String cmd = ParamUtil.getString(req, Constants.CMD);

		if (!cmd.equals(Constants.UPDATE)) {
			return;
		}

		String alfrescoWebClientURL = ParamUtil.getString(req,
			"alfrescoWebClientURL");
		String userId = ParamUtil.getString(req, "userId");
		String nodeUuid = ParamUtil.getString(req, "nodeUuid");
		String password = ParamUtil.getString(req, "password");
		boolean maximizeLinks = ParamUtil.getBoolean(req, "maximizeLinks");

		String portletResource = ParamUtil.getString(req, "portletResource");

		PortletPreferences prefs = PortletPreferencesFactory.getPortletSetup(
			req, portletResource, true, true);

		prefs.setValue("alfresco-web-client-url", alfrescoWebClientURL);
		prefs.setValue("user-id", userId);
		prefs.setValue("node-uuid", nodeUuid);
		prefs.setValue("password", password);
		prefs.setValue("maximize-links", Boolean.toString(maximizeLinks));

		prefs.store();

		SessionMessages.add(req, config.getPortletName() + ".doConfigure");
	}

	public ActionForward render(ActionMapping mapping, ActionForm form,
		PortletConfig config, RenderRequest req, RenderResponse res)
		throws Exception {

		return mapping
			.findForward("portlet.alfresco_content.edit_configuration");
	}

	public static void main(String args[]) throws Exception {

		// Start the session
		AuthenticationUtils.startSession("admin", "admin");
		try {
			RepositoryServiceSoapBindingStub repositoryService = WebServiceFactory
				.getRepositoryService();

			// Get a reference to the space we have named
			Reference reference = new Reference(STORE, null,
				"/app:company_home");
			QueryResult result = repositoryService.queryChildren(reference);

			ResultSetRow[] rows = result.getResultSet().getRows();

			for (int i = 0; i < rows.length; i++) {
				String nodeId = rows[i].getNode().getId();

				System.out.println("Node ID: " + nodeId);

				NamedValue namedValues[] = rows[i].getColumns();

				for (int j = 0; j < namedValues.length; j++) {
					System.out.println(namedValues[j].getName() + ": "
						+ namedValues[j].getValue());
				}
			}

			String content = new AlfrescoContentLocalServiceImpl().getContent(
				null, "/app:company_home/cm:test", "http://localhost", "admin",
				"admin");
			System.out.println(content);
		}
		catch (Throwable e) {
			System.out.println(e.toString());
		}
		finally {
			// End the session
			AuthenticationUtils.endSession();
			System.exit(0);
		}

	}

}