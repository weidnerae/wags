package webEditor.client;

import webEditor.client.view.Notification;

import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

/**
 * ParseResponseHandler
 * @author pmeznar
 *
 * This class was to handle all responses from the server
 * which serve only as notification to the user.
 * 
 */
public class ParseResponseHandler implements SubmitCompleteHandler {

	@Override
	public void onSubmitComplete(SubmitCompleteEvent event) {
		WEStatus stat = new WEStatus(event.getResults());
		Notification.notify(stat.getStat(), stat.getMessage());
	}

}
