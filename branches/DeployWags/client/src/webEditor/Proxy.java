package webEditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import webEditor.database.DatabasePanel;
import webEditor.database.DatabaseProblem;
import webEditor.logical.DataStructureTool;
import webEditor.magnet.view.MagnetProblemCreator;
import webEditor.magnet.view.RefrigeratorMagnet;
import webEditor.magnet.view.ResultsPanelUi;
import webEditor.magnet.view.StackableContainer;
import webEditor.programming.view.CodeEditor;
import webEditor.programming.view.FileBrowser;
import webEditor.programming.view.OutputReview;
import webEditor.ProxyFramework.AbstractServerCall;
import webEditor.ProxyFramework.GetVisibleExercisesCommand;
import webEditor.views.concrete.DefaultPage;
import webEditor.views.concrete.Login;
import webEditor.views.concrete.Wags;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Proxy
{	

	// Get the URL for the host page
	private static final String baseURL = GWT.getHostPageBaseURL().concat("server.php");
	
	private static final String getFileContents = getBaseURL()+"?cmd=GetFileContents";
	private static final String saveFileContents = getBaseURL()+"?cmd=SaveFileContents";
	private static final String deleteExercise = getBaseURL()+"?cmd=DeleteExercise";
	private static final String deleteUser = getBaseURL()+"?cmd.Deleteuser";
	private static final String getSections = getBaseURL() + "?cmd=GetSections";
	private static final String getFileListing = getBaseURL()+"?cmd=GetFileListing";
	private static final String submitFile = getBaseURL()+"?cmd=Review";
	private static final String logout = getBaseURL()+"?cmd=Logout";
	private static final String login = getBaseURL()+"?cmd=Login";
	private static final String registerURL = Proxy.getBaseURL()+"?cmd=RegisterUser";
	private static DataStructureTool DST;
	private static Wags wags;
		
	private static void holdMessage(String message){
		Notification.cancel(); // Cancel previous clearing schedule if present
		Element parent = DOM.getElementById("notification-area");
		Notification.clear();
		final Label l = new Label(message);
		parent.appendChild(l.getElement());
	}
	
	private static void clearMessage(){
		Notification.clear();
	}
	
	/** Implemented as Command
	//ENCODE FALSE
	public static void addMagnetLinkage2(final String title){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=AddMagnetLinkage&title="+title);
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus stat = new WEStatus(response);
					Notification.notify(stat.getStat(), stat.getMessage());
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Notification.notify(WEStatus.STATUS_ERROR, "Failure: Problem in AddMagnetLinkage");					
				}
			});
		} catch(Exception e){
			Window.alert("Error Occurred.  Please e-mail the following to pmeznar@gmail.com:\n" +
					e.getMessage());
		}
	} */

	/** IMPLEMENTED AS A COMMAND OBJECT
	//ENCODE FALSE
	public static void addSkeletons(String exname){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=AddSkeletons&name=" + exname);
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus stat = new WEStatus(response);
					if( stat.getStat() != WEStatus.STATUS_SUCCESS){
						Notification.notify(stat.getStat(), stat.getMessage());
					}
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("addSkeletons failed!");					
				}
			});
		} catch (Exception e) {
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	} */
	
	//Implemented as a command
	//ENCODE FALSE
	/**
	public static void alterExercise2(String exercise, final String attribute, final ListBox exercises){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=EditExercises" +
				"&title=" + exercise + "&attribute=" + attribute);
		try{
			builder.sendRequest(null, new RequestCallback(){
	
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Error in editExercise request");
				}
	
				@Override
				public void onResponseReceived(Request request,
						Response response) {
					WEStatus status = new WEStatus(response);  
					
					Notification.notify(status.getStat(), status.getMessage());
				}
				
			});
		} catch (RequestException e) {
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	}
*/
	/**

	//Implemented as Command
	//Encode == TRUE
	//Method == POST
	public static void assignPassword(String password){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, Proxy.getBaseURL()+"?cmd=AssignPassword");
		
		try{
			builder.setHeader("Content-Type", "application/x-www-form-urlencoded");
	      @SuppressWarnings("unused")
	      Request req = builder.sendRequest("pass="+password, new RequestCallback() {
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Error in assignPassword request");
				}
	
				@Override
				public void onResponseReceived(Request request,
						Response response) {
					WEStatus status = new WEStatus(response);  	
					Notification.notify(status.getStat(), status.getMessage());
					
				}
				
			});
		} catch (RequestException e) {
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	}
*/
	/** Implemented as COmmand Object
	//Encode == FALSE
	//Method == GET
	public static void buildDST(final Wags wags) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				Proxy.getBaseURL() + "?cmd=GetLogicalExercises");
		try {
			@SuppressWarnings("unused")
			Request req = builder.sendRequest(null, new RequestCallback() {
				public void onResponseReceived(Request request, Response response) {
					WEStatus status = new WEStatus(response);
					String[] problems = status.getMessageArray();
					String[] problemsList = new String[problems.length / 2];
					int[] statusList = new int[problems.length / 2];

					for (int i = 0; i < problems.length - 1; i += 2) {
						int idx = i / 2; 										   // corresponding index for lists
						problemsList[idx] = problems[i]; 						   // title of exercise
						statusList[idx] = Integer.parseInt(problems[i + 1]); 	   // true if corresponding string is '1'
					}

					DataStructureTool DST = new DataStructureTool(problemsList,
							statusList);
					DST.getElement().getStyle().setOverflowY(Overflow.AUTO);
					//wags.replaceCenterContent(DST);
				}
		        
		        public void onError(Request request, Throwable exception) {
		        	Window.alert("error");
		        }
		      });
		    } catch (RequestException e) {
		      Window.alert("Failed to send the request: " + e.getMessage());
		    }
	} */
	
	//Implemented as Command
	//Encode == false
	//Method == get
	/**
	public static void buildMagnets(final WagsPresenterImpl wags) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=GetMagnetExercises");
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus stat = new WEStatus(response);
					
					String[] problems = stat.getMessageArray();
					int[] idList = new int[problems.length / 3];
					String[] problemsList = new String[problems.length / 3];
					int[] statusList = new int[problems.length / 3];
					
					// To understand this, you must understand that problems is an array
					// following a sequence of id, name, success.  Thus, we iterate over it
					// in steps of three, to "group" the entries corresponding to the same exercise
					for(int i = 0; i < problems.length - 2; i += 3){
						final int id = Integer.parseInt(problems[i]);
						
						if (id != 0) {
							int idx = i / 3;
							
							idList[idx] = id;
							problemsList[idx] = problems[i + 1];
							statusList[idx] = Integer.parseInt(problems[i + 2]);
						}
					}
					
					MagnetPage Magnets = new MagnetPage(idList, problemsList, statusList, wags);
					wags.splashPage = Magnets;
					Magnets.getElement().getStyle().setOverflowY(Overflow.AUTO);
					wags.setWidget(Magnets);
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Magnet Exercises error");
				}
			});
		} catch (RequestException e){
			Window.alert("error: " + e.getMessage());
		}
	}
	*/
	/** Implemented as Command Object
	//Method: GET
	//
	public static void buildDatabase(final Wags wags) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=GetDatabaseExercises");
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus stat = new WEStatus(response);
					
					String[] problems = stat.getMessageArray();
					int[] idList = new int[problems.length / 3];
					String[] problemsList = new String[problems.length / 3];
					int[] statusList = new int[problems.length / 3];
					
					// To understand this, you must understand that problems is an array
					// following a sequence of id, name, success.  Thus, we iterate over it
					// in steps of three, to "group" the entries corresponding to the same exercise
					for(int i = 0; i < problems.length - 2; i += 3){
						final int id = Integer.parseInt(problems[i]);
						
						if (id != 0) {
							int idx = i / 3;
							
							idList[idx] = id;
							problemsList[idx] = problems[i + 1];
							statusList[idx] = Integer.parseInt(problems[i + 2]);
						}
					}
					
					DatabasePanel dbPanel = new DatabasePanel(idList, problemsList, statusList);
					dbPanel.getElement().getStyle().setOverflowY(Overflow.AUTO);
					//wags.replaceCenterContent(dbPanel);
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Magnet Exercises error");
				}
			});
		} catch (RequestException e){
			Window.alert("error: " + e.getMessage());
		}
	} */
	
	/** 
	 *  Grabs a database problem
	 * @return 
	 */
	/** ENCODE FALSE
	public static void getDatabaseProblem(int id, final DatabasePanel dbPanel) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=GetDatabaseProblem&id=" + id);
		try{
			builder.sendRequest("", new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus status = new WEStatus(response);
					DatabaseProblem dbProblem = (DatabaseProblem) status.getObject();
					dbPanel.initialize(dbProblem);
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Error getting magnet problem");					
				}
			});
		} catch(Exception e){
			Window.alert(e.getMessage());
		}
	} */
	
	public static void call(String command, HashMap<String, String> request, WagsCallback callback){
		Proxy.call(command, request, callback, RequestBuilder.GET);
	}

	/*
	 * Makes a request to server.
	 * The string command will correspond to a command on the server.
	 * The string[] will contain all other request variables.
	 * The callback will be called when a reponse is received from server.
	 */
	public static void call(String command, HashMap<String, String> request, WagsCallback callback, RequestBuilder.Method method)
	{
		String completeURL = getBaseURL()+"?cmd="+command;
	
		if(request != null){
			/* Loop over request variables appending each to request */
			Set<String> keys = request.keySet();
			Iterator<String> keysItr = keys.iterator();
			while(keysItr.hasNext()){
				String next = keysItr.next();
				completeURL += "&"+next+"="+request.get(next);
			}
		}
		
		RequestBuilder builder = new RequestBuilder(method, completeURL);
		
		/* Make request */
		try {
			builder.sendRequest(null, callback);
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}

	/** Implemented as Command Object
	//Encode == FALSE
	//Method = GET
	public static void checkPassword(final Wags wags){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=CheckPassword");
		try{
			builder.sendRequest(null, new RequestCallback(){
	
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Error in checkPassword request");
				}
	
				@Override
				public void onResponseReceived(Request request,
						Response response) {
					WEStatus status = new WEStatus(response);  
					
					if(status.getStat() == WEStatus.STATUS_ERROR){	
						AssignNewPasswordHandler.handleAssignNewPassword();
					}
				}
				
			});
		} catch (RequestException e) {
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	} */
	
	/** Already Implemented
	public static void cleanOutldCreatedMagnets2(int magnetProblemID){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=CleanOutOldCreatedMagnets&magnetProblemID="+magnetProblemID);
		
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
				}
			});
		} catch (RequestException e){
			Window.alert("Failed");
		}
	} */

	/** Implemented
	public static void deleteExercise2(final String ex, final ListBox exercises){
		String urlCompl = deleteExercise+"&title=" + ex;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, urlCompl);
		try {
			@SuppressWarnings("unused")
			Request r = builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response)
				{
					WEStatus status = new WEStatus(response);
					Notification.notify(status.getStat(), status.getMessage());
					AbstractServerCall visibleCmd = new GetVisibleExercisesCommand(exercises);
				}
				
				@Override
				public void onError(Request request, Throwable exception)
				{
					Window.alert("Error in delete Exercise Request");
				}
			});
		} catch (RequestException e) {
			e.printStackTrace();
		}
	} */
	
	
	/**
	 * Called to set the section of a specific user to 0, thus removing that user from whatever
	 * section he was previously in
	 * 
	 * @param username the username of the user to modify
	 */
	/** Implemented
	public static void RemoveUserFromSection(final String username){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, getBaseURL() + "?cmd=RemoveUserFromSection&name=" + username);
		try {
			@SuppressWarnings("unused")
			Request r = builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response)
				{
					WEStatus status = new WEStatus(response);
					Notification.notify(status.getStat(), status.getMessage());
				}
				
				@Override
				public void onError(Request request, Throwable exception)
				{
					Window.alert("Error in delete Exercise Request");
				}
			});
		} catch (RequestException e) {
			e.printStackTrace();
		}
	} */
	
	/** implemented
	public static void deleteMagnetExercise(String title){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, getBaseURL()+"?cmd=DeleteMagnetExercise&title=" + title);
		try {
			builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response)
				{
					WEStatus status = new WEStatus(response);
					Notification.notify(status.getStat(), status.getMessage());
				}
				@Override
				public void onError(Request request, Throwable exception)
				{
					
				}
			});
		} catch (RequestException e) {
			e.printStackTrace();
		}
	} */

	public static String getBaseURL() {
		return baseURL;
	} 

	/** Implemented
	public static void getDescription(String exercise, final Image descImage){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, getBaseURL()+"?cmd=GetDesc&title=" + exercise);
		try {
			@SuppressWarnings("unused")
			Request r = builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response)
				{
					WEStatus status = new WEStatus(response);
					if(status.getStat() == WEStatus.STATUS_SUCCESS){
						descImage.setUrl(status.getMessage());
					}else{
						Notification.notify(WEStatus.STATUS_ERROR, "Error fetching description url.");
					}
				}
				@Override
				public void onError(Request request, Throwable exception)
				{
					
				}
			});
		} catch (RequestException e) {
			e.printStackTrace();
		}
	} */

	public static DataStructureTool getDST(){
		return DST;
	}
	
	/** Implemented
	public static void reviewExercise2(String title, final String type, final Reviewer pf){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=DSTReview&title="+title+"&type="+type);
		
		try{
			builder.sendRequest(null, new RequestCallback(){
	
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("error");					
				}
	
				@Override
				public void onResponseReceived(Request request,
						Response response) {
					
					WEStatus status = new WEStatus(response);
					
			        String subInfo[] = new String[status.getMessageArray().length];
			        subInfo = status.getMessageArray();
					
			        for (int i = 1; i < subInfo.length; i+=3){
			        	if(subInfo[i] == "1") subInfo[i] = "Yes";
			        	else if (subInfo[i] == "0") subInfo[i] = "No";
			        }
					
			        pf.reviewCallback(subInfo);
				}
			}); 
		} catch (RequestException e){
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	} */
	
	/**
	 * Partner to reviewExercise to review all the exercises that a student has done.
	 * @param student's name
	 * @param Reviewer 
	 */
	/** Implemented
	public static void reviewStudent( String name, final Reviewer pf )
	{
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=StudentReview&name="+name);
		try{
			builder.sendRequest(null, new RequestCallback(){
	
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("error");					
				}
	
				@Override
				public void onResponseReceived(Request request,
						Response response) {
					
					WEStatus status = new WEStatus(response);
			        String subInfo[] = new String[status.getMessageArray().length];
			        subInfo = status.getMessageArray();
			        
			     
			        for (int i = 2; i < subInfo.length; i+=3){
			        	
			        	if(subInfo[i] == "1") subInfo[i] = "Yes";
			        	else if (subInfo[i] == "0") subInfo[i] = "No";
			        }
					
			        pf.reviewCallback(subInfo);
				}
			}); 
		} catch (RequestException e){
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	} */

	
	/**
	 * Simple method to set the uploaded time from a simple file
	 */
	/** Implemented
	public static void getFilTime(String title, final Label uploadStamp, final Label helperStamp)
	{
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=GetFileTime&title=" + title);
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
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
				
				@Override
				public void onError(Request request, Throwable exception) {
					Notification.notify(WEStatus.STATUS_ERROR, "Failure: Problem in GetFileTime");					
				}
			});
		} catch(Exception e){
			Window.alert("Error Occurred.  Please e-mail the following to pmeznar@gmail.com:\n" +
					e.getMessage());
		}
	} */
	
	/** implemented
	public static void getLogicalMicrolab(String title, final DataStructureTool DST){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=GetLogicalMicrolab&title=" + title);
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus status = new WEStatus(response);
					
					if(status.getStat() == WEStatus.STATUS_ERROR){
						Notification.notify(status.getStat(), status.getMessage());
						return;
					}
					
					LogicalMicrolab logMicro = (LogicalMicrolab) status.getObject();
					DST.initialize(logMicro.getProblem());
					//Notification.notify(status.getStat(), "Loaded from server");

				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					 Window.alert("Failed to grab Logical Microlab!");
				}
			});
		} catch (RequestException e){
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	} */
		
	/** implemented
	public static void getLMAssigned(final Receiver pf, final String args){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=GetLMAssigned&args=" + args);
		try {
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus status = new WEStatus(response);
					pf.getCallback(status.getMessageArray(), status, args);
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Logical Exercise Error");
				}
			});
		} catch (RequestException e){
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	} */
	
	/** implemented
	public static void getMMAssigned(final Receiver pf, final String args){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=GetMMAssigned&args=" + args);
		try {
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus status = new WEStatus(response);
					pf.getCallback(status.getMessageArray(), status, args);
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Magnet Exercise Error");
				}
			});
		} catch (RequestException e){
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	} */
	
	/** Implemented 9-26-14 as "GetLMSubjectsCommand"
	public static void getLMSubjects(final ProxyFacilitator pf){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=LogicalExercises&request=subjects");
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus stat = new WEStatus(response);
					
					pf.handleSubjects(stat.getMessageArray());
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					 Window.alert("getLMSubjects error");
				}
			});
		} catch (RequestException e){
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	} */
	
	/** Implemented 9-25-14 as "GetLMGroupsCommand"
	public static void getLMGroups(String subject, final ProxyFacilitator pf){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + 
				"?cmd=LogicalExercises&request=groups&subject=" + subject);
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus stat = new WEStatus(response);
					
					pf.handleGroups(stat.getMessageArray());
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					 Window.alert("getLMGroups error");
				}
			});
		} catch (RequestException e){
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	} */
	
	/** Implemented 9-25-14 as "GetLMExercisesCommand"
	public static void getLMExercises(String group, final ProxyFacilitator pf){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + 
				"?cmd=LogicalExercises&request=exercises&group=" + group);
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus stat = new WEStatus(response);
					
					pf.handleExercises(stat.getMessageArray());
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					 Window.alert("getLMExercises error");
				}
			});
		} catch (RequestException e){
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	} */
	
	/*
	 *  Unfinished/untested method to get the magnet problem groups
	 *  for the admin page.
	 */
	
	/** Implemented 9-26-14 as "GetMMGroupsCommand"
	public static void getMMGroups(final ProxyFacilitator pf){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + 
				"?cmd=GetMagnetGroups");
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus stat = new WEStatus(response);
					
					pf.handleGroups(stat.getMessageArray());
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					 Window.alert("getMMGroups error");
				}
			});
		} catch (RequestException e){
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	} */
	
	/*
	 *  Unfinished/untested method to get the exercises for each magnet problem group
	 *  for the admin page.
	 */
	/** Implemented 9-30-14
	public static void getMMExercises(String group, final ProxyFacilitator pf){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + 
				"?cmd=GetMagnetsByGroup&group=" + group);
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus stat = new WEStatus(response);
					String[] tmp = stat.getMessageArray();
					
					//strip quotations
					for (int i = 0; i < tmp.length; i++) {
						String name = tmp[i].substring(1, tmp[i].length() - 1);
						tmp[i] = name;
					}
					
					pf.handleExercises(tmp);
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					 Window.alert("getMMExercises error");
				}
			});
		} catch (RequestException e){
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	} */
	
	
	/** 
	 * Returns a list of all magnets available for the users section in that group, as well as setting up
	 * the necessary array and HashMap for retaining information about what was selected when navigating
	 * between magnet groups
	 * 
	 * If called from the simplified method handle, only fills a listbox
	 * 
	 * @param groupName:  The name of the group - id is found on server
	 * @param exercisePanel: The vertical panel that will be filled with checkboxes created using
	 * 							the array returned from the server
	 */
	/** Implemented 10-2-14 as "GetMagnetsByGroupCommand"
	public static void getMagnetsByGroup(String groupName, final VerticalPanel exercisePanel, final ArrayList<CheckBox>
			currentMagnets, final HashMap<String, CheckBox> allMagnets, final ListBox lstMagnetExercises){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=GetMagnetsByGroup&group=" + groupName);
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus stat = new WEStatus(response);
					String[] exercises = stat.getMessageArray();
					
					// Only runs when called getMagnetsByGroup(String groupName, final ListBox lstMagnetExercises)
					if(exercisePanel == null){
						lstMagnetExercises.clear();
						for(int i = 0; i < exercises.length; i++){
							String name = exercises[i].substring(1, exercises[i].length() - 1);
							lstMagnetExercises.addItem(name, name);
						}
						return;
					}
					
					// Clear out current magnets
					if(!currentMagnets.isEmpty()){
						for(CheckBox magnet: currentMagnets){
							magnet.setVisible(false);
						}
					}
					
					// Add magnets
					CheckBox chk;
					lstMagnetExercises.clear();
					for(int i = 0; i < exercises.length; i++){
						String name = exercises[i].substring(1, exercises[i].length() - 1);
						lstMagnetExercises.addItem(name, name);
						
						if(allMagnets.containsKey(name)){
							// If we already added this one, grab it again (it may be checked!)
							chk = allMagnets.get(name);
							chk.setVisible(true);
						} else {
							// If we didn't already add this one, add it now! 
							chk = new CheckBox(name);
							allMagnets.put(name, chk); // And we may want it later!
						}
						
						exercisePanel.add(chk);
						currentMagnets.add(chk);
					}
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Magnet group error!");					
				}
			});
		} catch (RequestException e){
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	} */
	
	/** 
	 * Loads the magnetGroups for this section
	 * @param magnetExercises - The listbox to be filled
	 */
	/** Implemented 9-30-14 as "GetMagnetGroupsCommand"
	public static void getMagnetGroups(final ListBox magnetExercises, final VerticalPanel selectionPanel, final ArrayList<CheckBox>
			currentMagnets, final HashMap<String, CheckBox> allMagnets, final ListBox lstMagnetExercises){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=GetMagnetGroups");
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus status = new WEStatus(response);
					String[] problemList = status.getMessageArray();
					magnetExercises.clear(); // To avoid repeat listings
					
					for(int i = 0; i < problemList.length; i++){
						magnetExercises.addItem(problemList[i], problemList[i]);
					}
					
					if(selectionPanel == null){
						return;
					}
					
					// Automatically load problems for initially selected group
					Proxy.getMagnetsByGroup(problemList[0], selectionPanel, currentMagnets, allMagnets, lstMagnetExercises);
										
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Magnet Exercises error");
				}
			});
		} catch (RequestException e){
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	} */

	/** 
	 *  Grabs a magnet problem
	 * @return 
	 */
	/** Implemented 9-30-14 as "GetMagnetProblemCommand"
	public static void getMagnetProblem(final int id, final AcceptsOneWidget page){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=GetMagnetProblem&id=" + id);
		try{
			builder.sendRequest("", new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus status = new WEStatus(response);
					MagnetProblem magProblem = (MagnetProblem) status.getObject();
					MagnetProblemCreator creator = new MagnetProblemCreator();
					RefrigeratorMagnet problem = creator.makeProblem(magProblem);
					page.setWidget(problem);
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Error getting magnet problem");					
				}
			});
		} catch(Exception e){
			Window.alert(e.getMessage());
		}
	} */
	

	
	/* Loads editor page */
	public static void getMagnetProblemForEdit(final TextArea titleArea, final TextArea desc, final TextArea classArea,
			final TextArea functions, final TextArea statements, String title, final TextArea finalTypeTxtArea, final TextArea forLoop1TextArea, final TextArea forLoop2TextArea, 
			final TextArea forLoop3TextArea, final TextArea ifsTextArea, final TextArea whilesTextArea, final TextArea returnsTextArea, final TextArea assignmentsVarTextArea,
			final TextArea assignmentValTextArea, final TextBox ifAllowed, final TextBox elseAllowed, final TextBox elseIfAllowed, final TextBox forAllowed, final TextBox whileAllowed,
			final TextBox returnAllowed, final TextBox assignmentAllowed, final RadioButton btnBasicProblem, final RadioButton btnAdvancedProblem, final RadioButton btnPrologBasicProblem, final RadioButton btnCBasicProblem) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=GetMagnetProblem&title=" + title);
		try{
			builder.sendRequest("", new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus status = new WEStatus(response);
					MagnetProblem magProblem = (MagnetProblem) status.getObject();
					titleArea.setText(magProblem.title);
					desc.setText(magProblem.directions);
					finalTypeTxtArea.setText(magProblem.type);
					classArea.setText(magProblem.solution);
					
					
					if (magProblem.type.equals(webEditor.magnet.view.Consts.ADVANCED_PROBLEM)) {
						//set radio button
						btnAdvancedProblem.setValue( true );
						String fors = "";
						// fors
						for(String s: magProblem.forLeft) {
							if (s != "") {
								fors+=s+".:|:.";
							}
						}
						forLoop1TextArea.setText(fors);
						
						fors = "";
						for(String s: magProblem.forMid) {
							if (s != "") {
								fors+=s+".:|:.";
							}
						}
						forLoop2TextArea.setText(fors);
						
						fors = "";
						for(String s: magProblem.forRight) {
							if (s != "") {
								fors+=s+".:|:.";
							}
						}
						forLoop3TextArea.setText(fors);
						
						// booleans
						fors = "";
						for(String s: magProblem.ifOptions) {
							if (s != "") {
								fors+=s+".:|:.";
							}
						}
						ifsTextArea.setText(fors);
						
						fors = "";
						for(String s: magProblem.whileOptions) {
							if (s != "") { 
								fors+=s+".:|:.";
							}
						}
						whilesTextArea.setText(fors);
						
				        fors = "";
				        for(String s: magProblem.returnOptions) {
				        	if(s != ""){
				        		fors += s + ".:|:.";
				        	}
				        }
				        returnsTextArea.setText(fors);
						
						fors = "";
				        for(String s: magProblem.assignmentVars) {
				        	if(s != "") {
				        		fors += s + ".:|:.";
				        	}
				        }
				        assignmentsVarTextArea.setText(fors);
				        
				        fors = "";
				        for(String s: magProblem.assignmentVals) {
				        	if(s != ""){
				        		fors += s + ".:|:.";
				        	}
				        }
				        assignmentValTextArea.setText(fors);
				        
						// limits
						String[] limits = magProblem.limits.split(",");
						if(limits.length >=5){
							forAllowed.setText(""+Integer.parseInt(limits[0]));
							whileAllowed.setText(""+Integer.parseInt(limits[1]));
							ifAllowed.setText(""+Integer.parseInt(limits[2]));
							elseIfAllowed.setText(""+Integer.parseInt(limits[3]));
							elseAllowed.setText(""+Integer.parseInt(limits[4]));
						}
						if(limits.length >= 7){
							returnAllowed.setText("" + Integer.parseInt(limits[5]));
							assignmentAllowed.setText("" + Integer.parseInt(limits[6]));
						}else{
							returnAllowed.setText("0");
							assignmentAllowed.setText("0");
						}
					} else if(magProblem.type.equals(webEditor.magnet.view.Consts.PROLOG_BASIC_PROBLEM)) {
						btnPrologBasicProblem.setValue(true);
					} else if (magProblem.type.equals(webEditor.magnet.view.Consts.C_BASIC_PROBLEM)) {
						btnCBasicProblem.setValue(true);
					}
					else{
						// This is basic Java problem, it's in the else because some things in the
						// DB have -1 as type or nothing. We should probably go in and fix that.
						btnBasicProblem.setValue( true );	
					}
					
					String innerFunctions = "";
					if(magProblem.innerFunctions != null && magProblem.innerFunctions.length > 0){
						for(int i = 0; i < magProblem.innerFunctions.length; i++){
							innerFunctions += magProblem.innerFunctions[i] + ".:|:.";
						}
					}
					functions.setText(innerFunctions);
					
					String statementList = "";
					if(magProblem.statements != null && magProblem.statements.length > 0){
						for(int i = 0; i < magProblem.statements.length; i++){
							statementList += magProblem.statements[i] + ".:|:.";
						}
					}
					statements.setText(statementList);
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Error getting magnet problem");					
				}
			});
		} catch(Exception e){
			Window.alert(e.getMessage());
		}
	}
	
	
	/** Implemented 9-30-14 as "GetSectionsCommand"
	public static void getSections(final ListBox sections) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, getSections);
		try {
		      @SuppressWarnings("unused")
			Request req = builder.sendRequest(null, new RequestCallback() {
		        public void onResponseReceived(Request request, Response response) {
		        	
		          WEStatus status = new WEStatus(response);
		          
		          if(status.getStat() == WEStatus.STATUS_SUCCESS){
		        	  sections.clear();
		        	  
		        	  if(status.getMessageArray().length > 0){
		        		  String[] message = status.getMessageArray();
		        		  String name;
		        		 
		        		  for(String section: message){
		        			  name = section.substring(1, section.length() - 1); //strip quotes
		        			  // The value passed to the server is the same as the text
		        			  sections.addItem(name, name); 
		           		  }
			        	  
		        	  } else {
		        		  sections.addItem(status.getMessage());
		        	  }
		          }
		        }
		        
		        public void onError(Request request, Throwable exception) {
		        	Window.alert("error");
		        }
		      });
		    } catch (RequestException e) {
		      Window.alert("Failed to send the request: " + e.getMessage());
		    }
	} */

	/** Implemented 9-30-14 as "GetSubmissionInfoCommand"
	public static void getSubmissionInfo(String exercise, final Grid grid){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=AdminReview&title="+exercise);
		final int NUM_COLUMNS = 5;
		try{
			builder.sendRequest(null, new RequestCallback(){
	
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("error");					
				}
	
				@Override
				public void onResponseReceived(Request request,
						Response response) {
					
					WEStatus status = new WEStatus(response);
			        String subInfo[] = new String[status.getMessageArray().length];
			        subInfo = status.getMessageArray();
			        
			        for (int i = 3; i < subInfo.length; i+=NUM_COLUMNS){
			        	if(subInfo[i] == "1") subInfo[i] = "Yes";
			        	else if (subInfo[i] == "0") subInfo[i] = "No";
			        }
			        
			        grid.resize(subInfo.length/NUM_COLUMNS+1, NUM_COLUMNS);
			  		grid.setBorderWidth(1);
			  		
			  		//Sets the headers for the table
			  		grid.setHTML(0, 0, "<b> Username </b>");
			  		grid.setHTML(0, 1, "<b> File </b>");
			  		grid.setHTML(0, 2, "<b> NumAttempts </b>");
			  		grid.setHTML(0, 3, "<b> Correct </b>");
			  		grid.setHTML(0, 4, "<b> Partner </b>");
			  		
			  		int k = 0;
			  		//Fills table with results from AdminReview.php
			  	    for (int row = 1; row < subInfo.length/NUM_COLUMNS+1; ++row) {
			  	      for (int col = 0; col < NUM_COLUMNS; ++col){
			  	    	//numAttempts is 0 based on server, so increment
			  	    	if(col == 2){
			  	    		int numAttempts = Integer.parseInt(subInfo[k++]);
			  	    		numAttempts++;
			  	    		grid.setText(row, col, numAttempts + "");
			  	    	} else {
			  	    		grid.setText(row, col, subInfo[k++]);
			  	    	}
			  	      }
			  	    }
				}
				
			});
		}catch (RequestException e){
			Window.alert("Failed to send the request: " + e.getMessage());
		}
		
	} */
	
	/** Implemented 9-26-14 as "GetUsernamesCommand"
	public static void getUsernames(final ListBox users) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=GetAllUsers");
		try {
		      @SuppressWarnings("unused")
			Request req = builder.sendRequest(null, new RequestCallback() {
		        public void onResponseReceived(Request request, Response response) {
		          WEStatus status = new WEStatus(response);
		          
		          if(status.getStat() == WEStatus.STATUS_SUCCESS){
		        	  if(status.getMessageArray().length > 0){
		        		  users.clear();
		        		  String[] message = status.getMessageArray();
		        		  
			        	  for(int i = 0; i < message.length; i++){
			        		  users.addItem(message[i].substring(1, message[i].length()-1));
			        	  }
			        	  
		        	  } else {
		        		  users.addItem(status.getMessage());
		        	  }
		          }
		        }
		        
		        public void onError(Request request, Throwable exception) {
		        	Window.alert("error");
		        }
		      });
		    } catch (RequestException e) {
		      Window.alert("Failed to send the request: " + e.getMessage());
		    }
	} */
	
	
	/**
	 * Overloaded getUsernames for the Student Review Tab to get Users by Section of logged in Admin
	 * @param studentReviewer
	 */
	/** Implemented 9-26-14 as "GetUsernamesReviewerCommand"
	public static void getUsernames(final Reviewer studentReviewer) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=GetAllUsers");
		try {
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus status = new WEStatus(response);
					String[] message = status.getMessageArray();
					
					for (int i = 0; i < message.length; i++) {
						message[i] = message[i].substring(1, message[i].length()-1);
					}
					studentReviewer.getCallback(status.getMessageArray(), status, request.toString());
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Student Error");
				}
			});
		} catch (RequestException e){
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	} */
	

	/**
	 * Set the greeting label.
	 * If user did not submit a name when they registered just 
	 * use their email address. 
	 * 
	 * Also, hides Anchors for guests
	 */
	
	/** Not being used?
	public static void getUsersName(final Label label, final Anchor Editor, final Anchor DST, 
			final Anchor Magnets, final Anchor Admin, final String startingPlace)
	{
		WagsCallback c = new WagsCallback() {
			@Override
			void warning(WEStatus status) {
				label.setText("Hello, Guest!");
				Editor.setVisible(false);
				// Leave the anchor for the starting place for quicker logical navigation, 
				// any magnet navigation
				Admin.setVisible(false);
				if(!startingPlace.equals("dst")) DST.setVisible(false);
				if(!startingPlace.equals("magnets")) Magnets.setVisible(false);
			}
			
			@Override
			void success(WEStatus status) {
				//HACK to make sure the admin nav link doesn't get taken away for out
				// fake workshop admins...
				if(Integer.parseInt(status.getMessageMapVal("admin")) != 1 &&
						Integer.parseInt(status.getMessageMapVal("section")) != 55){
					Admin.setVisible(false);
				}
				
				String first = status.getMessageMapVal("firstName");
				if(first == null){
					// Use email address in greeting.
					first = status.getMessageMapVal("email");
				}
				label.setText("Hello, "+first+"!");
			}
			
			@Override
			void error(WEStatus status) {
				Notification.notify(WEStatus.STATUS_ERROR, "Error fetching user details.");
			}
		};
		
		Proxy.call("GetUserDetails", null, c);
	} */

	/** Implemented 9-26-14 as "GetVisibleExercisesCommand"
	public static void getVisibleExercises(final ListBox exercises) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=GetVisibleExercises");
		try {
		      @SuppressWarnings("unused")
			Request req = builder.sendRequest(null, new RequestCallback() {
		        public void onResponseReceived(Request request, Response response) {
		          WEStatus status = new WEStatus(response);
		          
		          if(status.getStat() == WEStatus.STATUS_SUCCESS){
		        	  if(status.getMessageArray().length > 0){
		        		  String[] message = status.getMessageArray();
		        		  
		        		  exercises.clear();
		        		  
			        	  for(int i = 0; i < message.length; i++)
			        		  exercises.addItem(message[i]);

		        	  } else {
		        		  exercises.addItem(status.getMessage());
		        	  }
		          }
		        } 
		        
		        public void onError(Request request, Throwable exception) {
		        	Window.alert("error");
		        }
		      });
		    } catch (RequestException e) {
		      Window.alert("Failed to send the request: " + e.getMessage());
		    }
	} */

	/** Implemented 9-26-14 as "IsAdminButtonCommand", possibly already implemented elsewhere
	public static void isAdmin(final Button section_btn){	
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=IsAdmin");
		try {
		      @SuppressWarnings("unused")
			Request req = builder.sendRequest(null, new RequestCallback() {
		        public void onResponseReceived(Request request, Response response) {
		          WEStatus status = new WEStatus(response);
		          boolean root = false;
		          
		          // If not root, no section tab
		          if(status.getStat() == WEStatus.STATUS_SUCCESS){
		         	 section_btn.setVisible( true );
		          }
		        }
		        
		        public void onError(Request request, Throwable exception) {
		        	Window.alert("error");
		        }
		      });
		    } catch (RequestException e) {
		      Window.alert("Failed to send the request: " + e.getMessage());
		    }
	}
	
	/** Implemented 9-26-14 as "IsAdminCommand", possibly already implemented elsewhere
	public static void isAdmin(final Widget magnet, final Widget logical, final Widget admin, final Widget database){	
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=IsAdmin");
		try {
		      @SuppressWarnings("unused")
			Request req = builder.sendRequest(null, new RequestCallback() {
		        public void onResponseReceived(Request request, Response response) {
		          WEStatus status = new WEStatus(response);
		          boolean root = false;
		          
		          // If not root, no section tab
		          if(status.getStat() == WEStatus.STATUS_SUCCESS || status.getStat() == WEStatus.STATUS_WARNING){
		        	  admin.setVisible(true);
		        	
	 *   magnet.setVisible(true);
		        	  logical.setVisible(true);
		        	  database.setVisible(true);
		          }
		        }
		        
		        public void onError(Request request, Throwable exception) {
		        	Window.alert("error");
		        }
		      });
		    } catch (RequestException e) {
		      Window.alert("Failed to send the request: " + e.getMessage());
		    }
	} */
	
	/** Implemented 9-26-14 as "UploadLogicalMicrolabCommand"
	public static void uploadLogicalMicrolab(String details){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, baseURL+"?cmd=UploadLogicalMicrolab" + details);
		
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus status = new WEStatus(response);
					Notification.notify(status.getStat(), status.getMessage());
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("loadLogical error");	
				}
			});
		} catch (RequestException e){
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	} */
	
	/** Implemented 9-26-14 as "LinkNewSectionCommand"
	public static void linkNewSection(String section, String admin, String guest){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, baseURL+"?cmd=LinkNewSection" + 
				"&sect=" + section + "&admin=" + admin + "&guest=" + guest);
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus stat = new WEStatus(response);
					Notification.notify(stat.getStat(), stat.getMessage());
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Error in LinkNewSection request");					
				}
			});
		} catch(Exception e){
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	} */

	/**
	 * Get a file listing from server. Add each file to file browser tree.
	 */
	/** Implemented 10-1-14 as "LoadFileListingCommand"
	public static void loadFileListing(final FileBrowser fileBrowser, final String path)
	{
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, getFileListing);
		try {
			@SuppressWarnings("unused")
			Request r = builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response)
				{
					WEStatus status = new WEStatus(response);
					
					// Create HashMap with link between filenames and their current
					// visibility
					if(status.getStat() == WEStatus.STATUS_SUCCESS){
						fileBrowser.visibilities.clear();
						String[] list = status.getMessageArray();
						String[] fileNames = new String[list.length];
						int index = 0;
						
						// Cycle through, parsing fileNames and visibilities
						for(String entry: list){
							String name = entry.substring(0, entry.length()-1); // Everything but last character
							Integer vis = Integer.parseInt(entry.substring(entry.length() - 1));  // Only last character
							fileNames[index] = name;
				
							fileBrowser.visibilities.put(name, vis);
							index++;
						}
						
						fileBrowser.loadTree(fileNames);
						if(path != "") fileBrowser.openPath(path);
					}else{
						Notification.notify(WEStatus.STATUS_ERROR, "Error fetching file listing.");
					}
				}
				@Override
				public void onError(Request request, Throwable exception)
				{
					
				}
			});
		} catch (RequestException e) {
			e.printStackTrace();
		}
	} */

	/**
	 * Tell server we want to login, and loads the appropriate
	 * widgets
	 */
	
	/** Not being used?
	public static void login(String username, String password)
	{
		String completeURL = login+"&username="+username.trim()+"&password="+password;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(completeURL));
		try {
			@SuppressWarnings("unused")
			Request req = builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus status = new WEStatus(response);
					if(status.getStat() == WEStatus.STATUS_SUCCESS){
						loadDefault();
					}else{
						Notification.notify(WEStatus.STATUS_ERROR, status.getMessage());
					}
				}
				@Override
				public void onError(Request request, Throwable exception) {
					Notification.notify(WEStatus.STATUS_ERROR, "Error occurred while connecting to server.");
				}
			});
		} catch (RequestException e) {
			e.printStackTrace();
		}
	} */
	
	/** Not being used?
	public static void loadDefault() {
		DefaultPage d = new DefaultPage();
		RootPanel root = RootPanel.get();
		root.clear();
		root.add(d);
	} /*

	/**
	 * Tell server we want to logout of WE.
	 */
	
	/** Not being used?
	public static void logout3()
	{
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(logout));
		try{
			@SuppressWarnings("unused")
			Request r = builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response)
				{
					Login l = new Login();
					//l.go();
				}
				
				@Override
				public void onError(Request request, Throwable exception)
				{
					Notification.notify(WEStatus.STATUS_ERROR, exception.getMessage());
				}
			});
		}catch(RequestException e){
			e.printStackTrace();
		}
	} */
	
	/** Implemented 10-1-14 as "SaveCreatedMagnetCommand"
	public static void saveCreatedMagnet(StackableContainer magnet, int magnetProblemId){
		String url = getBaseURL()+"?cmd=SaveCreatedMagnet&magnetContent="+magnet.getContent()+"&magnetID="+magnet.getID()+"&magnetProblemID="+magnetProblemId;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
		try{
			@SuppressWarnings("unused")
			Request r = builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response)
				{
					// Quietly rebuild the file browser on success.
					WEStatus stat = new WEStatus(response);
					if(stat.getStat() == WEStatus.STATUS_SUCCESS){
						
					}else{
						Notification.notify(WEStatus.STATUS_WARNING, "Submission Processed Correctly - Magnet could not be saved");
					}
				}
				@Override
				public void onError(Request request, Throwable exception)
				{}
			});
		}catch(RequestException e){
			e.printStackTrace();
		}
	} */

	/** Implemented 10-1-14 as "MagnetReviewCommand"
	public static void magnetReview(final String saveState, final int id, String code, String title){
		code = URL.encodePathSegment(code);  // Escapes things like "+", etc.
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, Proxy.getBaseURL()+"?cmd=MagnetReview");
		try{
			builder.setHeader("Content-Type", "application/x-www-form-urlencoded");
			builder.sendRequest("code=" + code + "&title=" + title, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus stat = new WEStatus(response);
					String note = "";
					switch (stat.getStat()){
						case WEStatus.STATUS_SUCCESS:
							note = "Success!";
							saveMagnetState(saveState,id,1, true);   // Save Correct Magnet State
							break;
						case WEStatus.STATUS_ERROR:
							note = "Syntax Error - Incorrect";
							break;
						case WEStatus.STATUS_WARNING:
							note = "Logic Error - Incorrect";
							break;
						default:
							break;
					}
					
					Notification.notify(stat.getStat(), note);
					String results = stat.getMessage();
					results = results.replaceAll("<br />", "\n");
					results = results.replaceAll("<tab/>", "\t");
					ResultsPanelUi.setResultsText(results);
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Magnet review error");
				}
			});
		} catch(RequestException e){
			Window.alert(e.getMessage());
		}
		
	} */



	/**
	 * This will be called when the user presses submit, and will send a request to the server to compile,
	 * run, and return the results.  This method then updates the review panel, and notifies the user
	 * of the result.
	 * 
	 * @param code
	 * @param review
	 * @param exerciseId
	 * @param fileName
	 * @param submit
	 */
	public static void review(String code, final OutputReview review, String exercise, String fileName, 
								final Button submit)
	{		
		// Disable (gray-out) the submit button until code is done compiling/running
		//	- This will prevent multiple submissions before the current submission is completed
		submit.setEnabled(false);
		
		holdMessage("Compiling...");
		review.setText("");
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, submitFile);
		try {
		      builder.setHeader("Content-Type", "application/x-www-form-urlencoded");
		      @SuppressWarnings("unused")
		      Request req = builder.sendRequest("code="+code+"&title="+exercise+"&name="+fileName, new RequestCallback() {
		    	  public void onResponseReceived(Request request, Response response) {
			      
			      WEStatus status = new WEStatus(response);
			      clearMessage(); // clear out compilation notification message
			      
			      // Need to correctly format line breaks
			      // - Messy, need to refactor by uncoupling server code
			      // from JSON encoding
			      String msg = status.getMessage();
			      msg = msg.replace("<br />", "\n");
			      msg = msg.replace("<tab/>", "\t");
			      			           
			      review.setText(msg);
			     
			      if(status.getStat() == WEStatus.STATUS_SUCCESS){
			    	  Notification.notify(WEStatus.STATUS_SUCCESS, "Correct!");
			      } else if (status.getStat() == WEStatus.STATUS_WARNING){
			    	  Notification.notify(WEStatus.STATUS_WARNING, "Incorrect, Try Again");
			      } else {
			    	  Notification.notify(WEStatus.STATUS_ERROR, "Failed to Compile");
			      }
			      
			      // enable the submit button again
			      submit.setEnabled(true);
			    }
			    
			    public void onError(Request request, Throwable exception) {
			    	Window.alert("error");
			    	// enable the submit button again
			    	submit.setEnabled(true);
			    }
			  });
			} catch (RequestException e) {
			  Window.alert("Failed to send the request: " + e.getMessage());
		      // enable the submit button again
		      submit.setEnabled(true);
			}
	}
	

	public static boolean saveFile(String fileName, String contents, final FileBrowser browser, final boolean notify)
	{
		holdMessage("Saving...");
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, saveFileContents);
		try {
		      builder.setHeader("Content-Type", "application/x-www-form-urlencoded");
		      @SuppressWarnings("unused")
			Request req = builder.sendRequest("name="+fileName.trim()+"&contents="+contents, new RequestCallback() {
		        @Override
				public void onResponseReceived(Request request, Response response)
				{
					clearMessage();
					WEStatus status = new WEStatus(response);
					if(notify){
						Notification.notify(WEStatus.STATUS_SUCCESS, status.getMessage());
						
						//loadFileListing(browser, "/"); I believe this is no longer needed as they can't add files
					}

				}
				
				@Override
				public void onError(Request request, Throwable exception)
				{
					Notification.notify(WEStatus.STATUS_ERROR, "Error saving file. "+exception.getMessage());
				}
			});
		}catch(RequestException e){
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}
	/** Implemented 10-1-14 as "SaveMagnetStateCommand"
	public static void saveMagnetState(String state, int magnetId, int success, final boolean fromSuccess){
		String url = getBaseURL()+"?cmd=SaveMagnetState&state="+state+"&magnetId="+magnetId+"&success="+success;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
		try{
			@SuppressWarnings("unused")
			Request r = builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response)
				{
					// Quietly rebuild the file browser on success.
					WEStatus stat = new WEStatus(response);
					if(stat.getStat() == WEStatus.STATUS_SUCCESS){
						if(!fromSuccess)
							Notification.notify(WEStatus.STATUS_SUCCESS, stat.getMessage());
					}else{
						Notification.notify(WEStatus.STATUS_WARNING, "Submission Processed Correctly - State not saved");
					}
				}
				@Override
				public void onError(Request request, Throwable exception)
				{}
			});
		}catch(RequestException e){
			e.printStackTrace();
		}
	} */
	
	public static void setDST(DataStructureTool thisDST){
		DST = thisDST;
	}
	
	public static void setWags(Wags thisWags) {
		wags = thisWags;
	}
	
	public static Wags getWags() {
		return wags;
	}
	
	/*
	 *Should merge with SetMMExercises, only difference is the "cmd" and "split" 
	 */
	/** Implemented 10-2-14 as "SetLMExercisesCommand"
	public static void SetLMExercises(String toAssign, final ProxyFacilitator pf) {
		if(toAssign.equals("")) toAssign = "none";
		final String forCallback = toAssign;
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=SetLogicalExercises&list=" + toAssign);
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus stat = new WEStatus(response);
					Notification.notify(stat.getStat(), stat.getMessage());
					pf.setCallback(forCallback.substring(0, forCallback.length()-1).split("\\|"), stat);
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("SetLMExercises error");					
				}
			});
		} catch (RequestException e) {
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	} */
	
	/** Implemented 10-2-14 as "SetMMExercisesCommand"
	public static void SetMMExercises(String assignedMagnets, final ProxyFacilitator pf) {
		if(assignedMagnets.equals("")) assignedMagnets = "none";
		final String forCallback = assignedMagnets;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=SetMagnetExercises&list=" + assignedMagnets);
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus stat = new WEStatus(response);
					Notification.notify(stat.getStat(), stat.getMessage());
					pf.setCallback(forCallback.split(","), stat);
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Set magnet error");					
				}
			});
		} catch (RequestException e) {
			Window.alert("Failed to send the request: " + e.getMessage());
		}
		
	} */

	/** Implemented 10-1-14 as "SubmitDSTCommand"
	public static void submitDST(String title, int success){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=SubmitDST&title="+title+"&success="+success);
		try {
		      @SuppressWarnings("unused")
			Request req = builder.sendRequest(null, new RequestCallback() {
		        public void onResponseReceived(Request request, Response response) {
		          WEStatus status = new WEStatus(response); 
		        }
		        
		        public void onError(Request request, Throwable exception) {
		        	Window.alert("error");
		        }
		      });
		    } catch (RequestException e) {
		      Window.alert("Failed to send the request: " + e.getMessage());	
		    }
	} */	
} 
