package webEditor.ProxyFramework;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;

import webEditor.magnet.view.ResultsPanelUi;
import webEditor.Notification;
import webEditor.ProxyFramework.SaveMagnetStateCommand;
import webEditor.WEStatus;

public class MagnetReviewCommand extends AbstractServerCall {

	private String saveState;
	private int id;
	
	protected void handleResponse(Response response) 
	{
		WEStatus status = new WEStatus(response);
		String note = "";
		
		switch(status.getStat()) {
		case WEStatus.STATUS_SUCCESS:
			note = "Success!";
			AbstractServerCall cmd = new SaveMagnetStateCommand(saveState, id, 1, true);
			cmd.sendRequest();
			break;
		case WEStatus.STATUS_WARNING:
			note = "Syntax Error - Incorrect";
			break;
		case WEStatus.STATUS_ERROR:
			note = "Logic Error - Incorrect";
			break;
		default:
			break;
		}
		
		Notification.notify(status.getStat(), note);
		String results = status.getMessage();
		results = results.replaceAll("<br />", "\n");
		results = results.replaceAll("<tab/>", "\t");
		ResultsPanelUi.setResultsText(results);
	}
	
	public MagnetReviewCommand(String saveState, int id, String code, String title)
	{
		command = ProxyCommands.MagnetReview;
		this.saveState = saveState;
		this.id = id;
		this.method = RequestBuilder.POST;
		addArgument("code", URL.encodePathSegment(code));
		addArgument("title", title);
	}
	
}
