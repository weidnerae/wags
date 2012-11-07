package webEditor.client;

import webEditor.client.view.Login;
import webEditor.client.view.Registration;
import webEditor.client.view.Wags;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * WagsEntry
 *
 * This is the entry point for this GWT application.
 * Check if user is currently logged in or not.
 * If they are logged in direct them to the Editor.
 * If they are not logged redirect them based on
 * the loc variable in URL.
 *
 * @author Robert Bost <bostrt@appstate.edu>
 *
 */

public class WagsEntry implements EntryPoint 
{
	public void onModuleLoad() 
	{
		// Check if the user is logged in already.
		String isLoggedInURL = Proxy.getBaseURL()+"?cmd=GetUserDetails";
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(isLoggedInURL));
		try{
			@SuppressWarnings("unused")
			Request req = builder.sendRequest(null, new RequestCallback(){
				@Override
				public void onResponseReceived(Request request,	Response response) 
				{
					WEStatus status = new WEStatus(response);
					
					if(status.getStat() == WEStatus.STATUS_ERROR){
						// No one is logged in. Redirect based on location variable.
						String loc = Location.getParameter("loc");
						if(loc.equals("register")){
							register();
						}
						else if(loc.equals("login")){
							login();
						}
						else{
							login();
						}
					}
					else if(status.getStat() == WEStatus.STATUS_SUCCESS){
						RootLayoutPanel root = RootLayoutPanel.get();
						root.add(new Wags(getParam("loc")));	
					} else if(status.getStat() == WEStatus.STATUS_WARNING){
						String loc = Location.getParameter("loc");
						if(loc.equals("editor")){
							Proxy.logout();
						} else {
							RootLayoutPanel root = RootLayoutPanel.get();
							root.add(new Wags(loc));
						}
					}
				}
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Could not connect to server.");
				}
			});
		}catch(RequestException e){
			Window.alert(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static void register(){
		RootPanel root = RootPanel.get("main-content");
		root.add(new Registration());		
	}
	
	@SuppressWarnings("unused")
	private static void editor(String startingPoint){
		RootLayoutPanel root = RootLayoutPanel.get();
		root.add(new Wags(startingPoint));		
	}
	
	private static void login(){
		// Is user already logged in?
		//RootPanel root = RootPanel.get("main-content");
		RootLayoutPanel root = RootLayoutPanel.get();
		root.add(new Login());		
	}
	
	/**
	 * So, I'd like to use Window.Location.getParameter(), but it doesn't 
	 * work for values after the '#', so I have to roll my own if I want 
	 * that to work. This is probably not the most efficient thing ever, 
	 * but it works.
	 * 
	 * Given a param, it will look through url and find the first occurence of 
	 * '?%param%=', then return whatever follows it (until it finds something 
	 * that is not a letter).
	 * 
	 * 
	 * @param param parameter in the URL that we're looking for
	 * 
	 * @return the value associated with it
	 */
	private String getParam(String param) {
		StringBuilder sb = new StringBuilder();
		String url = Window.Location.getHref();
		char[] urlArr = url.toCharArray();
		
		int start = url.indexOf('?');
		start = url.indexOf(param + '=', start);
		
		if (start == -1) {
			return "undefined"; //param did not exist
		}
		
		for (int i = start + param.length() + 1; i < urlArr.length; i++) {
			if (!Character.isLetter(urlArr[i])) {
				break;
			}
			
			sb.append(urlArr[i]);
 		}
		
		return sb.toString();
	}
}
