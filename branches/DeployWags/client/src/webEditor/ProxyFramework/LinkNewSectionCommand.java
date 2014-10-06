package webEditor.ProxyFramework;

import webEditor.Notification;
import webEditor.WEStatus;
import com.google.gwt.http.client.Response;

public class LinkNewSectionCommand extends AbstractServerCall {
	
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
		Notification.notify(status.getStat(), status.getMessage());
	}
	
	public LinkNewSectionCommand(String section, String admin, String guest)
	{
		command = ProxyCommands.LinkNewSection;
		addArgument("sect", section);
		addArgument("admin", admin);
		addArgument("guest", guest);
	}
}
