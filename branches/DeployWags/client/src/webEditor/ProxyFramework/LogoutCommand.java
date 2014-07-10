package webEditor.ProxyFramework;

import webEditor.Common.ClientFactory;
import webEditor.Common.Tokens;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.History;

/**
 * @author Dakota Murray
 * 
 * Simple implementation of an object which encapsulates execution of the "logout" server
 * call. The logout command is very simple as it requires no input arguments
 *
 */
public class LogoutCommand extends AbstractServerCall {

	@Override
	protected void handleResponse(Response response) {
		ClientFactory.getAppModel().clear();
		History.newItem(Tokens.LOGIN);
	}
	
	public LogoutCommand()
	{
		command = ProxyCommands.Logout;
	}

}
