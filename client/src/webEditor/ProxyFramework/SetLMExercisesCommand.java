package webEditor.ProxyFramework;

import com.google.gwt.http.client.Response;

import webEditor.Notification;
import webEditor.ProxyFacilitator;
import webEditor.WEStatus;

public class SetLMExercisesCommand extends AbstractServerCall {

	private ProxyFacilitator pf;
	private String toAssign;
	
	protected void handleResponse(Response response)
	{
		if(toAssign.equals("")) toAssign = "none";
		final String forCallback = toAssign;
		
		WEStatus status = new WEStatus(response);
		Notification.notify(status.getStat(), status.getMessage());
		pf.setCallback(forCallback.substring(0, forCallback.length() - 1).split("\\|"), status);
	}
	
	public SetLMExercisesCommand(String toAssign, final ProxyFacilitator pf)
	{
		command = ProxyCommands.SetLogicalExercises;
		addArgument("title", toAssign);
		this.pf = pf;
		this.toAssign = toAssign;
	}
}
