package webEditor.ProxyFramework;

import com.google.gwt.http.client.Response;

import webEditor.ProxyFacilitator;
import webEditor.WEStatus;


public class GetMMGroupsCommand extends AbstractServerCall {

	private ProxyFacilitator pf;
	
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
		pf.handleGroups(status.getMessageArray());
	}
	
	public GetMMGroupsCommand(final ProxyFacilitator pf)
	{
		command = ProxyCommands.GetMagnetGroups;
		this.pf = pf;
	}
}
