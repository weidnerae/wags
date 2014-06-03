package webEditor.ProxyFramework;

import webEditor.LogicalMicrolab;
import webEditor.Notification;
import webEditor.WEStatus;
import webEditor.logical.DataStructureTool;

import com.google.gwt.http.client.Response;

public class GetLogicalMicrolabCommand extends AbstractCommand {

	DataStructureTool DST;
	@Override
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
		
		if(status.getStat() == WEStatus.STATUS_ERROR){
			Notification.notify(status.getStat(), status.getMessage());
			return;
		}
		
		LogicalMicrolab logMicro = (LogicalMicrolab) status.getObject();
		DST.initialize(logMicro.getProblem());
		/*Notification.notify(status.getStat(), "Loaded from server");*/
	}

	public GetLogicalMicrolabCommand(String title, DataStructureTool DST)
	{
		addArgument("title", title);
		this.DST = DST;
		command = ProxyCommands.GetLogicalMicrolab;
		
	}
}
