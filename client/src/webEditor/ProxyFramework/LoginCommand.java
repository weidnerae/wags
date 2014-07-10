package webEditor.ProxyFramework;

import java.util.HashMap;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;

import webEditor.Notification;
import webEditor.WEStatus;
import webEditor.Common.AppController;

public class LoginCommand extends AbstractServerCall {

	@Override
	protected void handleResponse(Response response) 
	{	
		WEStatus status = new WEStatus(response);
		if(status.getStat() == WEStatus.STATUS_SUCCESS){
			Window.alert("Login Command, WEStatus = Success");			
			HashMap<String, String> message = status.getMessageMap();
			Window.alert(message.toString());
			History.newItem("default", false);
			
			AppController.setUserDetails(message);
		} else {
			Notification.notify(WEStatus.STATUS_ERROR, status.getMessage());
		}
		History.fireCurrentHistoryState();
	}
		
	public LoginCommand(String username, String password)
	{	
		addArgument("username", username);
		addArgument("password", password);
		command = ProxyCommands.Login;
	}


}
