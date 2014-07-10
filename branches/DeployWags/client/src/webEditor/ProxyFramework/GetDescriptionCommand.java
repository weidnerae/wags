package webEditor.ProxyFramework;

import webEditor.Notification;
import webEditor.WEStatus;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Image;

public class GetDescriptionCommand extends AbstractServerCall {

	private Image descImage;
	
	@Override
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
		if(status.getStat() == WEStatus.STATUS_SUCCESS){
			descImage.setUrl(status.getMessage());
		}else{
			Notification.notify(WEStatus.STATUS_ERROR, "Error fetching description url.");
		}
	}
	
	public GetDescriptionCommand(String exercise, Image descImage)
	{
		addArgument("title", exercise);
		this.descImage = descImage;
		command = ProxyCommands.GetDesc;
	}

}
