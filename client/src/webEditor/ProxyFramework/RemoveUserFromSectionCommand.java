package webEditor.ProxyFramework;

import webEditor.Notification;
import webEditor.WEStatus;

import com.google.gwt.http.client.Response;

public class RemoveUserFromSectionCommand extends AbstractCommand {

	@Override
	protected void handleResponse(Response response) 
	{
		WEStatus status = new WEStatus(response);
		Notification.notify(status.getStat(), status.getMessage());
	}
	
	public RemoveUserFromSectionCommand(String username)
	{
		addArgument("name", username);
		command = ProxyCommands.RemoveUserFromSection;
	}

}
