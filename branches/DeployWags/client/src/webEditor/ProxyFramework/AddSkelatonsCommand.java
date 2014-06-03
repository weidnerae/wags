package webEditor.ProxyFramework;

import webEditor.Notification;
import webEditor.WEStatus;

import com.google.gwt.http.client.Response;

public class AddSkelatonsCommand extends AbstractCommand {

	@Override
	protected void handleResponse(Response response) {
		WEStatus stat = new WEStatus(response);
		if( stat.getStat() != WEStatus.STATUS_SUCCESS){
			Notification.notify(stat.getStat(), stat.getMessage());
		}
	}
	
	public AddSkelatonsCommand(String exname)
	{
		addArgument("name", exname);
		command = ProxyCommands.AddSkelaton;
		error = "addSkealtons failed!";
	}

}
