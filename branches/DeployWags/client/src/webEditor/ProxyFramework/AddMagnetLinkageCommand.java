package webEditor.ProxyFramework;

import com.google.gwt.http.client.Response;

import webEditor.Notification;
import webEditor.WEStatus;

/**
 * @author Dakota Murray
 * 
 * Server File Called: AddMagnetLinkage.php
 * 
 *
 */
public class AddMagnetLinkageCommand extends AbstractServerCall {

	@Override
	protected void handleResponse(Response response)
	{
		WEStatus stat = new WEStatus(response);
		Notification.notify(stat.getStat(), stat.getMessage());
	}
	
	public AddMagnetLinkageCommand(final String title)
	{
		addArgument("title", title);
		command = ProxyCommands.AddMagnetLinkage;
	}

}
