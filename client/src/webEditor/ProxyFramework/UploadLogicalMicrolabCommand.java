package webEditor.ProxyFramework;

import webEditor.WEStatus;
import webEditor.Notification;
import com.google.gwt.http.client.Response;

public class UploadLogicalMicrolabCommand extends AbstractServerCall {

	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
		Notification.notify(status.getStat(), status.getMessage());
	}
	
	public UploadLogicalMicrolabCommand(String details)
	{
		command = ProxyCommands.UploadLogicalMicrolab;
		addArgument("Details", details);
	}
}
