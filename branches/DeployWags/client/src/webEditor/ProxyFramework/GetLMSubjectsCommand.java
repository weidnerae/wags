package webEditor.ProxyFramework;

import webEditor.ProxyFacilitator;
import webEditor.WEStatus;

import com.google.gwt.http.client.Response;

public class GetLMSubjectsCommand extends AbstractCommand {

	private ProxyFacilitator pf;
	
	@Override
	protected void handleResponse(Response response) 
	{
		WEStatus stat = new WEStatus(response);
		
		pf.handleSubjects(stat.getMessageArray());

	}
	
	public GetLMSubjectsCommand(ProxyFacilitator pf)
	{
		addArgument("request", "subjects");
		this.pf = pf;
		
	}

}
