package webEditor.ProxyFramework;

import webEditor.Receiver;
import webEditor.WEStatus;

import com.google.gwt.http.client.Response;

public class GetMMAssigned extends AbstractCommand {

	private Receiver pf;
	private String args;
	
	@Override
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
		pf.getCallback(status.getMessageArray(), status, args);
	}
	
	public GetMMAssigned(Receiver pf, String args)
	{
		addArgument("args", args);
		this.pf = pf;
		this.args = args;
		command = ProxyCommands.GetMMAssigned;
	}

}
