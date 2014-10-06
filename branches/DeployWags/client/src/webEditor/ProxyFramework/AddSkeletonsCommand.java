package webEditor.ProxyFramework;

import webEditor.Notification;
import webEditor.WEStatus;

import com.google.gwt.http.client.Response;

public class AddSkeletonsCommand extends AbstractServerCall {

	@Override
	protected void handleResponse(Response response) {
		WEStatus stat = new WEStatus(response);
		if( stat.getStat() != WEStatus.STATUS_SUCCESS){
			Notification.notify(stat.getStat(), stat.getMessage());
		}
	}
	
	public AddSkeletonsCommand(String exname)
	{
		addArgument("name", exname);
		command = ProxyCommands.AddSkeleton;
		error = "addSkeletons failed!";
	}

}
