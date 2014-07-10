package webEditor.ProxyFramework;

import webEditor.Notification;
import webEditor.WEStatus;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;

/**
 * @author Dakota Murray
 * 
 * Server File Called: 
 *
 */
public class AssignPasswordCommand extends AbstractServerCall {

	@Override
	protected void handleResponse(Response response) 
	{
		WEStatus status = new WEStatus(response);  
		Notification.notify(status.getStat(), status.getMessage());
	}
	
	public AssignPasswordCommand(String password)
	{
		addArgument("pass", password);
		method = RequestBuilder.GET;
		command = ProxyCommands.AssignPassword;
	}

}
