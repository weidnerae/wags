package webEditor.ProxyFramework;

import com.google.gwt.http.client.Response;

import webEditor.magnet.view.StackableContainer;
import webEditor.Notification;
import webEditor.WEStatus;

public class SaveCreatedMagnetCommand extends AbstractServerCall {

	private StackableContainer magnet;
	
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
		if (status.getStat() == WEStatus.STATUS_SUCCESS)
		{
			
		}
		else
		{
			Notification.notify(WEStatus.STATUS_WARNING, "Submission Processed Correctly - Magnet could not be saved.");
		}
	}
	
	public SaveCreatedMagnetCommand(StackableContainer magnet, int magnetProblemID)
	{
		command = ProxyCommands.SaveCreatedMagnet;
		addArgument("magnetcontent", magnet.getContent());
		addArgument("magnetID", "" + magnet.getID());
		addArgument("magnetProblemID", "" + magnetProblemID);
		this.magnet = magnet;
	}
}
