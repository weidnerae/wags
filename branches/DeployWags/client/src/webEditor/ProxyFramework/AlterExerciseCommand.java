package webEditor.ProxyFramework;

import webEditor.Notification;
import webEditor.WEStatus;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.ListBox;

public class AlterExerciseCommand extends AbstractServerCall {

	@Override
	protected void handleResponse(Response response) {
		WEStatus status = new WEStatus(response);  
		
		Notification.notify(status.getStat(), status.getMessage());

	}
	
	public AlterExerciseCommand(String exercise, final String attribute, final ListBox exercises)
	{
		addArgument("title", exercise);
		addArgument("attribute", attribute);
		command = ProxyCommands.EditExercises;
	}

}
