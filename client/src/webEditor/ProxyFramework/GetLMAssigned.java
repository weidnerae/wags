package webEditor.ProxyFramework;

import webEditor.Receiver;
import webEditor.WEStatus;

import com.google.gwt.http.client.Response;

public class GetLMAssigned extends AbstractCommand {

	private Receiver pf;
	private String args;
	
	@Override
	protected void handleResponse(Response response) 
	{
		WEStatus status = new WEStatus(response);
		pf.getCallback(status.getMessageArray(), status, args);

	}
	
	public GetLMAssigned(Receiver pf, final String args)
	{
		this.pf = pf;
		this.args = args;
		addArgument("args", args);
		command = ProxyCommands.GetLMAssigned;
	}

}
