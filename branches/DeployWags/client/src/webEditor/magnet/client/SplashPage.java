package webEditor.magnet.client;

import webEditor.client.MagnetProblem;
import webEditor.client.Proxy;
import webEditor.client.view.View;
import webEditor.client.view.WEAnchor;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class SplashPage extends View {
	protected static PickupDragController dc = new PickupDragController(RootPanel.get(), false);
	static String[] structuresList = {"choose structure...","for","while","if","else if", "else"};
	
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
								//TODO: Had Rootpanel.get().clear() here - it broke things.  Still need
								// to make problem list invisible though
								Proxy.getMagnetProblem();
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
	 * TODO: Rename this method, filter by IDs
	 */
	public static void query(MagnetProblem magnet) {
		new RefrigeratorMagnet(
				magnet.title,
				magnet.directions,
				getMainContainer(magnet.mainFunction),
				buildFunctions(magnet.innerFunctions),
				magnet.type,
				decodePremade(magnet.statements), 
				structuresList,
				magnet.forLeft,
				magnet.forMid,
				magnet.forRight,
				magnet.bools,
				magnet.solution,
				magnet.statements,
				dc
		);
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
	public static StackableContainer[] decodePremade(String[] segments) {
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
	
	public static StackableContainer getMainContainer(String str) {
		return new StackableContainer(str + " {<br /><span id=\"inside_of_block\">"
				+ Consts.TOP + Consts.INSIDE + Consts.BOTTOM + "</span><br />}", dc, Consts.MAIN);
	}
	
	
	public static StackableContainer[] buildFunctions(String[] insideFunctions) {
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
