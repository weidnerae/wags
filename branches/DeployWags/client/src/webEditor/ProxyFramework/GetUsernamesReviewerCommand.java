package webEditor.ProxyFramework;

import webEditor.WEStatus;
import webEditor.Reviewer;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;

public class GetUsernamesReviewerCommand extends AbstractServerCall {
	
	private Reviewer studentReviewer;
	
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
		String[] message = status.getMessageArray();
		
		for (int i = 0; i < message.length; i++)
		{
			message[i] = message[i].substring(1, message[i].length() - 1);
		}
		
		studentReviewer.getCallback(status.getMessageArray(), status, response.toString());
	}
	
	public GetUsernamesReviewerCommand(final Reviewer studentReviewer)
	{
		command = ProxyCommands.GetAllUsers;
		this.studentReviewer = studentReviewer;
	}

}
