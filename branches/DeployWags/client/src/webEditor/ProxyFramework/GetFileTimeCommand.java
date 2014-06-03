package webEditor.ProxyFramework;

import webEditor.WEStatus;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Label;

public class GetFileTimeCommand extends AbstractCommand {

	private Label uploadStamp;
	private Label helperStamp;
	
	@Override
	protected void handleResponse(Response response)
	{
		WEStatus stat = new WEStatus(response);
		//Notification.notify(stat.getStat(), stat.getMessage());
		
		String[] msgArray = stat.getMessageArray();
		String fileTime = msgArray[0];
		
		if(fileTime.equals(0))
		{
			uploadStamp.setText("No test class received");
		}
		else
		{
			uploadStamp.setText("Last test class received: " + fileTime);
		}
		
		String helperTime = "";
		if(msgArray.length == 1)
		{
			helperTime = "0";
		}
		for(int i = 1; i < msgArray.length; i++)
		{
			String current = msgArray[i];
			if(current.compareTo(helperTime) > 0)
			{
				helperTime = current;
			}
		}
		
		if(helperTime.equals("0"))
		{
			helperStamp.setText("No helper class received");
		}
		else
		{
			helperStamp.setText("Last helper class received: " + helperTime);
		}

	}
	
	public GetFileTimeCommand(String title, final Label uploadStamp, final Label helperStamp)
	{
		addArgument("title", title);
		this.uploadStamp = uploadStamp;
		this.helperStamp = helperStamp;
		command = ProxyCommands.GetFileTime;
	}

}
