package webEditor.ProxyFramework;

import webEditor.Receiver;
import webEditor.WEStatus;

import com.google.gwt.http.client.Response;

public class GetMMAssignedCommand extends AbstractServerCall {

	private Receiver pf;
	private String args;
	
	@Override
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
		pf.getCallback(status.getMessageArray(), status, args);
	}
	
	public GetMMAssignedCommand(Receiver pf, String args)
	{
		command = ProxyCommands.GetMMAssigned;
		addArgument("args", args);
		this.pf = pf;
		this.args = args;
	}

}
