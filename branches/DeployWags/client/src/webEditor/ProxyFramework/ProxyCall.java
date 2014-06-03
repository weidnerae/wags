package webEditor.ProxyFramework;

import webEditor.Notification;
import webEditor.WEStatus;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

public class ProxyCall
{
	private Response response;
	private String postArgs = null;
	
	public void call(AbstractCommand command)
	{
		String commandURL = ProxyStringBuilder.buildServerString(command.getCommand(), command.getArguments(), command.getMethod());
		
		RequestBuilder builder = new RequestBuilder(command.getMethod(), commandURL);
		final AbstractCommand cmd = command;
		try {
			
			if (command.method == RequestBuilder.POST) {
				 builder.setHeader("Content-Type", "application/x-www-form-urlencoded");
				 postArgs = ProxyStringBuilder.buildArgumentString(command.getArguments());
			}
			
			@SuppressWarnings("unused")
			Request r = builder.sendRequest(postArgs, new RequestCallback() {
				
				
				@Override
				public void onError(Request request, Throwable exception) {
					if (cmd.error == "" || cmd.error == null) {
						Notification.notify(WEStatus.STATUS_ERROR, exception.getMessage());
					}
				}

				@Override
				public void onResponseReceived(Request request,
						Response response) {
					cmd.handleResponse(response);
				}
			});
			
		}
		catch (RequestException e){
			e.printStackTrace();
		}
	}
	
	public void setResponse(Response response)
	{
		this.response = response;
	}
	
	
	public Response getResponse()
	{
		return response;
	}
}

