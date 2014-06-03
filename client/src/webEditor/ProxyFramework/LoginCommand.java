package webEditor.ProxyFramework;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.RootPanel;

import webEditor.DefaultPage;
import webEditor.Notification;
import webEditor.WEStatus;

public class LoginCommand extends AbstractCommand {

	@Override
	protected void handleResponse(Response response) 
	{
		WEStatus status = new WEStatus(response);
		if(status.getStat() == WEStatus.STATUS_SUCCESS){
			DefaultPage d = new DefaultPage();
			RootPanel root = RootPanel.get();
			root.clear();
			root.add(d);
		}else{
			Notification.notify(WEStatus.STATUS_ERROR, status.getMessage());
		}
	}
	
	public LoginCommand(String username, String password)
	{	
		addArgument("username", username);
		addArgument("password", password);
		command = ProxyCommands.Login;
	}


}
