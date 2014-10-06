package webEditor.ProxyFramework;

import webEditor.WEStatus;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.http.client.Response;

public class GetVisibleExercisesCommand extends AbstractServerCall {

	private ListBox exercises;
	
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
		if (status.getStat() == WEStatus.STATUS_SUCCESS)
		{
			if (status.getMessageArray().length > 0)
			{
				String[] message = status.getMessageArray();
				
				exercises.clear();
				
				for (int i = 0; i < message.length; i++)
				{
					exercises.addItem(message[i]);
				}
			}
			else 
			{
				exercises.addItem(status.getMessage());
			}
		}
	}
	
	public GetVisibleExercisesCommand(final ListBox exercises)
	{
		command = ProxyCommands.GetVisibleExercises;
		this.exercises = exercises;
	}
}
