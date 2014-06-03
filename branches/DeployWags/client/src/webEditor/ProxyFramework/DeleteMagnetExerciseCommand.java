package webEditor.ProxyFramework;

import webEditor.Notification;
import webEditor.WEStatus;

import com.google.gwt.http.client.Response;

public class DeleteMagnetExerciseCommand extends AbstractCommand {

	@Override
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
		Notification.notify(status.getStat(), status.getMessage());
	}
	
	public DeleteMagnetExerciseCommand(String title)
	{
		addArgument("title", title);
		command = ProxyCommands.DeleteMagnetExercise;
	}

}
