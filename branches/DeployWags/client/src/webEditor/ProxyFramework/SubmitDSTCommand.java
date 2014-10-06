package webEditor.ProxyFramework;

import com.google.gwt.http.client.Response;

import webEditor.WEStatus;

public class SubmitDSTCommand extends AbstractServerCall{
	
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
	}
	
	public SubmitDSTCommand(String title, int success)
	{
		command = ProxyCommands.SubmitDST;
		addArgument("title", title);
		addArgument("success", "" + success);
	}
}
