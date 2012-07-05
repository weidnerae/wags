package webEditor.magnet.client;

import webEditor.client.view.View;
import webEditor.client.view.WEAnchor;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class SplashPage extends View {
	protected PickupDragController dc = new PickupDragController(RootPanel.get(), false);
	String[] structuresList = {"choose structure...","for","while","if","else if", "else"};
	
	String title;
	
	public SplashPage() {
		String completeURL = "http://cs.appstate.edu/wags/Test_Version/GetProblems.php";
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, completeURL);
		try {
			builder.setHeader("Content-Type", "application/x-www-form-urlencoded");
			
			@SuppressWarnings("unused")
			Request req = builder.sendRequest("", new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					JSONValue vals = JSONParser.parseStrict(response.getText());
					JSONArray arr = vals.isArray();
					
					for(int i=0;i<arr.size();i++){
						String sId = arr.get(i).isObject().get("id").isString().stringValue();
						int id = Integer.parseInt(sId);
						title = arr.get(i).isObject().get("title").isString().stringValue();
						RootPanel.get().add(new ProblemButton(title,id, new ClickHandler(){
							public void onClick(ClickEvent event) {
								RootPanel.get().clear();
								query(((ProblemButton)event.getSource()).getID());
							}
							public void onError(Request request,
									Throwable exception) {
							}
						}));
					}
						
					
				}

				public void onError(Request request, Throwable exception) {
				}
			});
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get Stuff Off of Server
	 */
	public void query(int id) {
		String completeURL = "http://cs.appstate.edu/wags/Test_Version/Query.php";
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, completeURL);
		try {
			builder.setHeader("Content-Type", "application/x-www-form-urlencoded");
			
			@SuppressWarnings("unused")
			Request req = builder.sendRequest("", new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					JSONValue vals = JSONParser.parseStrict(response.getText());
					JSONArray arr = vals.isArray();
					JSONObject obj = arr.get(0).isObject();
					
					if (obj != null) {
						JSONString title = obj.get("title").isString();
						JSONString desc = obj.get("directions").isString();
						JSONString main = obj.get("mainFunction").isString();
						JSONArray innerFunctions = obj.get("innerFunctions").isArray();
						JSONString problemType = obj.get("type").isString();
						JSONArray forLeft = obj.get("forLeft").isArray();
						JSONArray forMid = obj.get("forMid").isArray();
						JSONArray forRight = obj.get("forRight").isArray();
						JSONArray booleans = obj.get("booleans").isArray();
						JSONArray statements = obj.get("statements").isArray();
						JSONString premadeIDs = obj.get("numbers").isString();
						JSONString solution = obj.get("solution").isString();

						RefrigeratorMagnet rm = new RefrigeratorMagnet(
								title.stringValue(),
								desc.stringValue(),
								getMainContainer(main.stringValue()),
								buildFunctions(convertArray(innerFunctions)),
								problemType.stringValue(),
								decodePremade(convertArray(statements)), 
								structuresList,
								convertArray(forLeft),
								convertArray(forMid),
								convertArray(forRight),
								convertArray(booleans),
								solution.stringValue(),
								premadeIDs.stringValue(),
								dc
						);
					}else{
						Window.alert("NULL");
					}
				}


				public void onError(Request request, Throwable exception) {
				}
			});
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}
	
	public String[] convertArray(JSONArray arr) {
		if (arr.toString().equals("[null]")) {
			return null;
		}
		
		String[] result = new String[arr.size()];
		
		for (int i = 0; i < arr.size(); i++) {
			result[i] = arr.get(i).isString().stringValue();
		}
		
		return result;
	}
	public StackableContainer[] decodePremade(String[] segments) {
		if (segments == null) {
			return null;
		}
		
		StackableContainer[] preMadeList = new StackableContainer[segments.length]; //should never need this many
		int counter = 0;
			while (counter < segments.length) {
				if (segments[counter].contains(Consts.TOP)) {
					preMadeList[counter] = new StackableContainer(segments[counter], dc);
				} else {
					preMadeList[counter] = new StackableContainer(segments[counter] + Consts.TOP + Consts.INSIDE + Consts.BOTTOM, dc, false);
				}
				
				counter++;
			}
			
		return preMadeList;
	}
	
	public StackableContainer getMainContainer(String str) {
		return new StackableContainer(str + " {<br /><span id=\"inside_of_block\">"
				+ Consts.TOP + Consts.INSIDE + Consts.BOTTOM + "</span><br />}", dc, Consts.MAIN);
	}
	
	
	public StackableContainer[] buildFunctions(String[] insideFunctions) {
		if (insideFunctions == null) {
			return null;
		}
		
		StackableContainer[] insideFunctionsList = new StackableContainer[insideFunctions.length]; //should never need this many
		int counter = 0;
		while (counter < insideFunctions.length) {
			insideFunctionsList[counter] = new StackableContainer(insideFunctions[counter], dc, Consts.NONDRAGGABLE);
			counter++;
		}
		
		return insideFunctionsList;
	}

	@Override
	public WEAnchor getLink() {
		// TODO Auto-generated method stub
		return new WEAnchor("SP", this, "SP");
	}
}
