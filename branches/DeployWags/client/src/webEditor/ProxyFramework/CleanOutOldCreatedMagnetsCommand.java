package webEditor.ProxyFramework;

import com.google.gwt.http.client.Response;

public class CleanOutOldCreatedMagnetsCommand extends AbstractServerCall {

	@Override
	protected void handleResponse(Response response) {
		//Do Nothing, simply a helper server command

	}

	public CleanOutOldCreatedMagnetsCommand(int magnetProblemId)
	{
		addArgument("magnetProblemId", "" + magnetProblemId);
		command = ProxyCommands.CleanOutOldCreatedMagnets;
		
	}
}
