package webEditor.client;

import webEditor.client.view.Login;
import webEditor.client.view.Registration;
import webEditor.client.view.Wags;
import webEditor.dst.client.DataStructureTool;

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
						if(Location.getParameter("loc").equals("editor")){
							editor();
						}
						else if(Location.getParameter("loc").equals("DST")){
							DST();
						}
						else if(true){
							editor(); //Default to editor
						}
					}
				}
				@Override
				public void onError(Request request, Throwable exception) {
					// TODO: Show this message in a notification area.
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
	
	private static void editor(){
		// TODO: Is user logged in?
		RootLayoutPanel root = RootLayoutPanel.get();
		root.add(new Wags());		
	}
	
	private static void login(){
		// Is user already logged in?
		//RootPanel root = RootPanel.get("main-content");
		RootLayoutPanel root = RootLayoutPanel.get();
		root.add(new Login());		
	}
	
	private static void DST(){
		Proxy.buildDST();
	}
}
