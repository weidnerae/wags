package webEditor.ProxyFramework;

import webEditor.ProxyFacilitator;
import webEditor.WEStatus;

import com.google.gwt.http.client.Response;

public class GetLMGroupsCommand extends AbstractServerCall {
	
	private ProxyFacilitator pf;
	
	@Override
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
		pf.handleGroups(status.getMessageArray());
	}
	
	public GetLMGroupsCommand(String subject, ProxyFacilitator pf)
	{
		command = ProxyCommands.LogicalExercises;
		addArgument("request", "groups");
		addArgument("subject", subject);
		this.pf = pf;
	}
	
}