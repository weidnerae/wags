package webEditor.ProxyFramework;

import com.google.gwt.http.client.Response;

import webEditor.ProxyFacilitator;
import webEditor.WEStatus;

public class GetMMExercisesCommand extends AbstractServerCall {

	private ProxyFacilitator pf;
	
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus (response);
		String[] tmp = status.getMessageArray();
		
		for (int i = 0; i < tmp.length; i++)
		{
			String name = tmp[i].substring(1, tmp[i].length() - 1);
			tmp[i] = name;
		}
		
		pf.handleExercises(tmp);
	}
	
	public GetMMExercisesCommand(String group, final ProxyFacilitator pf)
	{
		command = ProxyCommands.GetMagnetsByGroup;
		addArgument("group", group);
		this.pf = pf;
	}
}
