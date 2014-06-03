package webEditor.ProxyFramework;

import webEditor.Login;

import com.google.gwt.http.client.Response;

/**
 * @author Dakota Murray
 * 
 * Simple implementation of an object which encapsulates execution of the "logout" server
 * call. The logout command is very simple as it requires no input arguments
 *
 */
public class LogoutCommand extends AbstractCommand {

	@Override
	public void sendRequest() {
		command = ProxyCommands.Logout;
		ProxyCall proxy = new ProxyCall();
		proxy.call(this);
	}

	@Override
	protected void handleResponse(Response response) {
		Login loginPage = new Login();
		loginPage.go();
	}
	
	public LogoutCommand()
	{
		
	}

}
