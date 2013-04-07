package webEditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import webEditor.logical.DataStructureTool;
import webEditor.magnet.view.Magnets;
import webEditor.magnet.view.RefrigeratorMagnet;
import webEditor.magnet.view.ResultsPanelUi;
import webEditor.magnet.view.StackableContainer;
import webEditor.programming.view.Admin;
import webEditor.programming.view.CodeEditor;
import webEditor.programming.view.Editor;
import webEditor.programming.view.Exercises;
import webEditor.programming.view.FileBrowser;
import webEditor.programming.view.OutputReview;

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
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class Proxy
{	

	// Get the URL for the host page
	private static final String baseURL = GWT.getHostPageBaseURL().concat("server.php");
	
	private static final String getFileContents = getBaseURL()+"?cmd=GetFileContents";
	private static final String saveFileContents = getBaseURL()+"?cmd=SaveFileContents";
	private static final String deleteExercise = getBaseURL()+"?cmd=DeleteExercise";
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
	
	public static void addMagnetLinkage(final String title){
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
	}
	
	public static void addProblemCreation(final RefrigeratorMagnet magnet){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=IsAdmin");
		try {
		      @SuppressWarnings("unused")
			Request req = builder.sendRequest(null, new RequestCallback() {
		        public void onResponseReceived(Request request, Response response) {
		          WEStatus status = new WEStatus(response);
		          boolean root = false;
		          
		          if(status.getStat() == WEStatus.STATUS_SUCCESS){
		        	  root = true;
		          }
		          
		          // If administrator or root
		          if(status.getStat() == WEStatus.STATUS_WARNING || root){
		        	  if(status.getMessage().equals("magnet")){
		        		  magnet.addProblemCreation(true);
		        	  } else {
		        		  magnet.addProblemCreation(false);
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
		
	}

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
	}
	
	public static void alterExercise(String exercise, final String attribute, final ListBox exercises){
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

	public static void assignPartner(String exercise, String partner){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=SetPartner" +
				"&ex=" + exercise + "&partner=" + partner);
		try{
			builder.sendRequest(null, new RequestCallback(){
	
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Error in assignPartner request");
				}
	
				@Override
				public void onResponseReceived(Request request,
						Response response) {
					WEStatus status = new WEStatus(response);  
					
					//SetPartner will handle the correct messages
					Notification.notify(status.getStat(), status.getMessage());
				}
				
			});
		} catch (RequestException e) {
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	}

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

	public static void buildDST(final Wags wags) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				Proxy.getBaseURL() + "?cmd=GetLogicalExercises");
		try {
			@SuppressWarnings("unused")
			Request req = builder.sendRequest(null, new RequestCallback() {
				public void onResponseReceived(Request request, Response response) {
					WEStatus status = new WEStatus(response);
					
					/*
					 * The problems array should have even indices corresponding to 
					 * the problem title, and odd indices with either 0 or 1 for 
					 * not completed and completed.
					 */
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
					wags.replaceCenterContent(DST);
				}
		        
		        public void onError(Request request, Throwable exception) {
		        	Window.alert("error");
		        }
		      });
		    } catch (RequestException e) {
		      Window.alert("Failed to send the request: " + e.getMessage());
		    }
	}
	
	public static void buildMagnets(final Wags wags) {
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
					
					Magnets Magnets = new Magnets(idList, problemsList, statusList, wags);
					wags.splashPage = Magnets;
					Magnets.getElement().getStyle().setOverflowY(Overflow.AUTO);
					wags.replaceCenterContent(Magnets);
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
						wags.assignPassword();
					}
				}
				
			});
		} catch (RequestException e) {
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	}

	public static void checkTimedExercises(){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=CheckOpenExercises");
		
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
	}
	
	public static void cleanOutOldCreatedMagnets(int magnetProblemID){
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
	}

	public static void deleteExercise(final String ex, final ListBox exercises){
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
					Proxy.getVisibleExercises(exercises);
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
	}
	
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
	}

	public static String getBaseURL() {
		return baseURL;
	}

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
	}

	public static DataStructureTool getDST(){
		return DST;
	}

	public static void getDSTSubmissions(String title, final Grid grid, String type){
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
			        
			        grid.resize(subInfo.length/3+1, 3);
			  		grid.setBorderWidth(1);
			  		
			  		//Sets the headers for the table
			  		grid.setHTML(0, 0, "<b> Username </b>");
			  		grid.setHTML(0, 1, "<b> Correct </b>");
			  		grid.setHTML(0, 2, "<b> NumAttempts </b>");
			  		
			  		int k = 0, row = 1;
			  		//Fills table with results - stops before last row (the summary)
			  	    for (row = 1; row < subInfo.length/3; ++row) {
			  	      for (int col = 0; col < 3; ++col)
			  	        grid.setText(row, col, subInfo[k++]);
			  	    }
			  	    // Last row
			  	    grid.setHTML(row, 0, "<b> " + subInfo[subInfo.length - 3] + " </b>");
			  	    grid.setHTML(row, 1, "<b> " + subInfo[subInfo.length - 2] + " </b>");
			  	    grid.setHTML(row, 2, "<b> " + subInfo[subInfo.length - 1] + " </b>");
				}
				
			});
		}catch (RequestException e){
			Window.alert("Failed to send the request: " + e.getMessage());
		}
		
	}

	/**
	 * Get a list of exercises from the server and put them
	 * in the FlexTable.
	 * 
	 */
	public static void getExercises(Exercises ex){
		
		WagsCallback c = new WagsCallback() {
			@Override
			void warning(WEStatus status) {}
			
			@Override
			void success(WEStatus status) {
				Window.alert(status.getMessageArray().toString());
			}
			
			@Override
			void error(WEStatus status) {
				Window.alert(status.getMessage());
			}
		};
		HashMap<String, String> vars = new HashMap<String, String>();
		Proxy.call("GetExercises", vars, c);
	}

	/**
	 * Get the contents of a file with the given name from server.
	 * Put those contents in the passed CodeEditor.
	 */
	public static void getFileContents(String fileName, final CodeEditor editor, final Editor codeArea){
		String urlCompl = getFileContents+"&name="+fileName.trim().substring(1);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, urlCompl);
		try {
			@SuppressWarnings("unused")
			Request r = builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response)
				{
					// Passing it through JSON kills formatting
					// For some unknown reason, the text from the server gets 
					// prepended with all sorts of spaces....
					String allText = response.getText().trim();
					
					//Grab status for uneditable codeArea for helper classes
					String status = allText.substring(0, 1); 
					allText = allText.substring(1);
					editor.codeArea.setEnabled(true); /* defaults to enabled */
					
					// Have to take into account comment length
					//	-We will still require that two comment marks be used before tag
					String lengthFinder = "<end!TopSection>";
					int len = lengthFinder.length();
					
					// Find the end of top and middle comments
					int endofTop = allText.indexOf("<end!TopSection>");
					int endofMid = allText.indexOf("<end!MidSection>");
					String top = "", mid = allText, bot = "";
							
					//Logic copied from server side
					if(endofTop != -1){
						top = allText.substring(0, endofTop + len); // keep the comment in top
						mid = allText.substring(endofTop + len);    // don't include comment in mid
					}
					
					if(endofMid != -1){
						bot = allText.substring(endofMid - 2); // keep comment in bottom
						mid = allText.substring(endofTop + len, endofMid - 2); // don't leave //, or %%, or etc in mid
					}
					
					editor.codeTop = top;
					editor.codeBottom = bot;
					// I'll look at this more - but I believe this is causing no indentation on the first line, 
					// which isn't what we want. - Philip
					//editor.codeArea.setText(mid.replaceAll("^\\s+", "")); // get rid of all leading whitespace
					editor.codeArea.setText(mid);
					
					if(status.equals("0")) {
						editor.codeArea.setEnabled(false); // if status = 0, file is uneditable
					}
					
					codeArea.addLineNumbers();
				}
				
				@Override
				public void onError(Request request, Throwable exception)
				{
					
				}
			});
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}
	/**
     * getLogicalForAssignment
     * 
     * Handles the initial loading of the Subject and Group listboxes on the admin page for logical exercises, as well as
     * initially loading the checkboxes for the default selected group
     * @param subjects      The listbox the subjects are loaded into
     * @param groups        The listbox the groups are loaded into
     * @param chkBoxArea    The vertical panel the checkboxes for exercises are loaded into
     */
    public static void getLogicalForAssignment(final ListBox subjects, final ListBox groups, final VerticalPanel chkBoxArea,
                    final ArrayList<CheckBox> currentLogicals, final HashMap<String, CheckBox> allLogicals){
            RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, Proxy.getBaseURL() + "?cmd=LogicalExercises&request=initial");
            try {
                    builder.sendRequest(null, new RequestCallback() {
                            
                            @Override
                            public void onResponseReceived(Request request, Response response) {
                                    WEStatus status = new WEStatus(response);
                                    int level = 0; // 0 = subject, 1 = group, 2 = exercise
                                    
                                    // Empty to guarantee no duplicates
                                    subjects.clear();
                                    groups.clear();
                                    chkBoxArea.clear();
                                    for(CheckBox box: currentLogicals){
                                            box.setVisible(false);
                                    }
                                    
                                    String[] problemList = status.getMessageArray();
                                    for(String entry: problemList){
                                            // If we're transitioning into a new level, transition and grab next string
                                            if(entry.equals("GROUPS")){
                                                    level = 1;
                                                    continue;
                                            } else if(entry.equals("EXERCISES")){
                                                    level = 2;
                                                    continue;
                                            } else {
                                                    if(level == 0){
                                                            subjects.addItem(entry, entry);
                                                    } else if (level == 1){
                                                            groups.addItem(entry, entry);
                                                    } else {
                                                            if (!allLogicals.containsKey(entry)){
                                                            	// For exercises, if it hasn't been loaded, load it
                                                                CheckBox ex = new CheckBox(entry);
                                                                chkBoxArea.add(ex);
                                                                currentLogicals.add(ex);
                                                                allLogicals.put(entry, ex);
                                                            
                                                            } else {
                                                            	// If it was loaded earlier, grab that one    
                                                            	CheckBox ex = allLogicals.get(entry);
                                                                currentLogicals.add(ex);
                                                                ex.setVisible(true);
                                                                chkBoxArea.add(ex);
                                                            }
                                                    }
                                            }
                                    }
                                    
                            }
                            
                            @Override
                            public void onError(Request request, Throwable exception) {
                                    Window.alert("Logical Exercise Error");
                            }
                    });
            } catch (RequestException e){
                    Window.alert("Failed to send the request: " + e.getMessage());
            }
    }
    
    /**
     * getLogicalGroups
     * 
     * Called off of a changehandler attached to the subject listbox for logical microlabs in the admin tab
     * Repopulates the groups listbox with the appropriate groups for each new subject
     * @param subject       The subject that has just been chosen
     * @param groups        The listbox to populate with the correct groups
     */
    public static void getLogicalGroups(String subject, final ListBox groups, 
    			final VerticalPanel passPanel, final ArrayList<CheckBox> passArray, final HashMap<String, CheckBox> passMap){
    	
            RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=LogicalExercises&request=groups&subject=" + subject);
            try {
                    builder.sendRequest(null, new RequestCallback() {
                            
                            @Override
                            public void onResponseReceived(Request request, Response response) {
                                    WEStatus status = new WEStatus(response);
                                    
                                    String[] groupList = status.getMessageArray();
                                    groups.clear();
                                    for(String s: groupList){
                                            groups.addItem(s,s);
                                    }
        
                                    // So we don't have weird looking group/exercise combos
                                    Proxy.getLogicalExercises(groupList[0], passPanel, passArray, passMap);
                                    
                            }
                            
                            @Override
                            public void onError(Request request, Throwable exception) {
                                    Window.alert("Error grabbing groups");
                            }
                    });
            } catch (RequestException e){
                    Window.alert("Failed to send the request: " + e.getMessage());
            }
    }
    
	public static void getLogicalExercises(String group, final VerticalPanel panel,final ArrayList<CheckBox> currentLogicals,
			final HashMap<String, CheckBox> allLogicals) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=LogicalExercises&request=exercises&group=" + group);
		try {
			builder.sendRequest(null, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus status = new WEStatus(response);

					String[] exList = status.getMessageArray();
					// Hide
					for (CheckBox chk : currentLogicals) {
						chk.setVisible(false);
					}

					CheckBox ex;
					for (String entry : exList) {
						if (allLogicals.containsKey(entry)) {
							// If it was loaded earlier, grab that one
							ex = allLogicals.get(entry);
							ex.setVisible(true);
						} else {
							// Add it as a new exercise
							ex = new CheckBox(entry);
							allLogicals.put(entry, ex);
						}
						
						panel.add(ex);
						currentLogicals.add(ex);
					}
				}

				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Error grabbing groups");
				}

			});
		} catch (RequestException e) {
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	}
    
	public static void getLogicalExercises(final ListBox logicalExercises){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=GetLogicalExercises");
		try {
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus status = new WEStatus(response);
					
					String[] problemList = status.getMessageArray();
					logicalExercises.clear(); //To avoid repeat listings
					
					for(int i = 0; i < problemList.length; i+=2){ 
		        		  logicalExercises.addItem(problemList[i]); 
		        	}
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Logical Exercise Error");
				}
			});
		} catch (RequestException e){
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	}
	
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
					Notification.notify(WEStatus.STATUS_SUCCESS, "Loaded from server");
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					 Window.alert("Failed to grab Logical Microlab!");
				}
			});
		} catch (RequestException e){
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	}
	
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
	}
	
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
	}
	
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
	}
	
	/*
	 *  Unfinished/untested method to get the magnet problem groups
	 *  for the admin page.
	 */
	
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
	}
	
	/*
	 *  Unfinished/untested method to get the exercises for each magnet problem group
	 *  for the admin page.
	 */
	
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
	}
	
	
	/**
	 * This will check to see which exercises are curently assigned, then call 
	 * Admin.checkCurrentLogicalExercises() to make them checked by default.
	 * 
	 * @param result ArrayList for storing the result.
	 */
	public static void getAssignedLogicalExercises(final ArrayList<String> result) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=GetLogicalExercises");
		try {
			builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus status = new WEStatus(response);
					String[] problemList = status.getMessageArray();
					
					for (int i = 0; i < problemList.length; i+=2) { 
						result.add(problemList[i]);
					}
					
					Admin.checkCurrentLogicalExercises();
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Logical Exercise Error");
				}
			});
		} catch (RequestException e) {
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	}
	
	/**
	 * This will check to see which magnets are currently assigned, then call 
	 * Admin.checkCurrentMagnetExercises() to make them checked by default.
	 * 
	 * @param result ArrayList for storing the result.
	 */
	public static void getAssignedMagnetExercises(final ArrayList<String> result) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=GetMagnetExercises");
		try {
			builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus status = new WEStatus(response);
					String[] problemList = status.getMessageArray();

					for (int i = 1; i < problemList.length; i+=3) {
						// Anything with the next element being "2" is a "Review" exercise,
						// not a currently assigned one
						if(!problemList[i+1].equals("2")){
							result.add(problemList[i]);
						}
					}
					
					Admin.checkCurrentMagnetExercises();
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Magnet Exercise Error");
				}
			});
		} catch (RequestException e) {
			Window.alert("Failed to send the request: " + e.getMessage());
		}
	}
	
	/* Simplified getMagnetsByGroup, just fills a listbox */
	public static void getMagnetsByGroup(String groupName, final ListBox lstMagnetExercises){
		getMagnetsByGroup(groupName, null, null, null, lstMagnetExercises);
	}
	
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
	public static void getMagnetsByGroup(String groupName, final VerticalPanel exercisePanel, final ArrayList<CheckBox>
			currentMagnets, final HashMap<String, CheckBox> allMagnets, final ListBox lstMagnetExercises){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=GetMagnetsByGroup&group=" + groupName);
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus stat = new WEStatus(response);
					String[] exercises = stat.getMessageArray();
					
					/* Only runs when called getMagnetsByGroup(String groupName, final ListBox lstMagnetExercises) */
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
	}

	/* For simply populating a listbox, wraps more complicated method */
	public static void getMagnetGroups(final ListBox magnetExercises){
		Proxy.getMagnetGroups(magnetExercises, null, null, null, null);
	}
	
	/** 
	 * Loads the magnetGroups for this section
	 * @param magnetExercises - The listbox to be filled
	 */
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
	}

	/** 
	 *  Grabs a magnet problem
	 * @return 
	 */
	public static void getMagnetProblem(final int id, final Wags wags){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=GetMagnetProblem&id=" + id);
		try{
			builder.sendRequest("", new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus status = new WEStatus(response);
					MagnetProblem magProblem = (MagnetProblem) status.getObject();
					wags.placeProblem(magProblem);
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
	
	/* Loads editor page */
	public static void getMagnetProblemForEdit(final TextArea titleArea, final TextArea desc, final TextArea classArea,
			final TextArea functions, final TextArea statements, String title){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=GetMagnetProblem&title=" + title);
		try{
			builder.sendRequest("", new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus status = new WEStatus(response);
					MagnetProblem magProblem = (MagnetProblem) status.getObject();
					titleArea.setText(magProblem.title);
					desc.setText(magProblem.directions);
					classArea.setText(magProblem.solution);
					
					String innerFunctions = "";
					if(magProblem.innerFunctions.length > 0){
						for(int i = 0; i < magProblem.innerFunctions.length; i++){
							innerFunctions += magProblem.innerFunctions[i] + ".:|:.";
						}
					}
					functions.setText(innerFunctions);
					
					String statementList = "";
					if(magProblem.statements.length > 0){
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
	}

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
		
	}
	
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
	}

	/**
	 * Set the greeting label.
	 * If user did not submit a name when they registered just 
	 * use their email address. 
	 * 
	 * Also, hides Anchors for guests
	 */
	public static void getUsersName(final Label label, final Anchor Editor, final Anchor DST, final Anchor Magnets, final Anchor Admin, final String startingPlace)
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
				if(Integer.parseInt(status.getMessageMapVal("admin")) != 1){
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
	}

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
	}

	public static void isAdmin(final TabLayoutPanel tabPanel, final Widget sections, final Widget students, final Widget admin){	
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=IsAdmin");
		try {
		      @SuppressWarnings("unused")
			Request req = builder.sendRequest(null, new RequestCallback() {
		        public void onResponseReceived(Request request, Response response) {
		          WEStatus status = new WEStatus(response);
		          boolean root = false;
		          
		          // If not root, no section tab
		          if(status.getStat() == WEStatus.STATUS_SUCCESS){
		        	  root = true;
		          }
		          
		          // If Admin or Root
		          if(status.getStat() == WEStatus.STATUS_WARNING || root){
		        	  ScrollPanel scroll = new ScrollPanel();
		        	  scroll.add(admin);
		        	  scroll.addStyleName("administration");
		        	  
		        	  ScrollPanel scroll2 = new ScrollPanel();
		        	  scroll2.add(students);
		        	  scroll2.addStyleName("administration");
		        	  
		        	  ScrollPanel scroll3 = new ScrollPanel();
		        	  scroll3.add(sections);
		        	  scroll3.addStyleName("administration");
		        	  
		        	  tabPanel.add(scroll, "Exercises");
		        	  tabPanel.add(scroll2, "Students");
		        	  if(root) tabPanel.add(scroll3, "Sections");
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
	
	public static void isAdmin(final Widget admin){	
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
	
	public static void loadLogicalMicrolab(String details){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, baseURL+"?cmd=LoadLogicalMicrolab" + details);
		
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
	}
	
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
	}

	/**
	 * Get a file listing from server. Add each file to file browser tree.
	 */
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
	}

	/**
	 * Tell server we want to login, and loads the appropriate
	 * widgets
	 */
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
	}
	
	public static void loadDefault() {
		DefaultPage d = new DefaultPage();
		RootPanel root = RootPanel.get();
		root.clear();
		root.add(d);
	}

	/**
	 * Tell server we want to logout of WE.
	 */
	public static void logout()
	{
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(logout));
		try{
			@SuppressWarnings("unused")
			Request r = builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response)
				{
					Login l = new Login();
					l.go();
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
	}
	
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
	}

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
		
	}

	/**
	 * Register a user.
	 */
	public static void register(String email, final String username, final String password, String firstName, String lastName, String section)
	{
		String completeURL = Proxy.registerURL+"&email="+email+
		 "&username="+username+
		 "&password="+password+
		 "&firstName="+firstName+
		 "&lastName="+lastName+
		 "&section="+section;
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, completeURL);
		try{
			@SuppressWarnings("unused")
			Request req = builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					RootPanel root = RootPanel.get();
					WEStatus status = new WEStatus(response);
					if(status.getStat() == WEStatus.STATUS_SUCCESS){
						// Log the user in automatically.
						Proxy.login(username, password);
						Notification.notify(status.getStat(), status.getMessage());
					}else{
						Notification.notify(status.getStat(), status.getMessage());
					}
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
				}
			});
		} catch (RequestException e){
			e.printStackTrace();
		}
	}

	/**
	 * Rename a file. Update TreeItem. 
	 */
	public static void renameFile(String oldName, final String newName, final FileBrowser browser)
	{
		String url = getBaseURL()+"?cmd=RenameFile&old="+oldName+"&new="+newName;
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
						// Rebuild browser
						Proxy.loadFileListing(browser, newName);
						
					}else{
						Notification.notify(stat.getStat(), stat.getMessage());
					}
				}
				@Override
				public void onError(Request request, Throwable exception)
				{}
			});
		}catch(RequestException e){
			e.printStackTrace();
		}
	}

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
	}
	
	public static void setDST(DataStructureTool thisDST){
		DST = thisDST;
	}
	
	public static void setWags(Wags thisWags) {
		wags = thisWags;
	}
	
	public static Wags getWags() {
		return wags;
	}
	
	public static void SetLogicalExercises(String assignedExercises, final ListBox logicalExercises) {
		if(assignedExercises.equals("")) assignedExercises = "none";
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=SetLogicalExercises&list=" + assignedExercises);
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus stat = new WEStatus(response);
					Notification.notify(stat.getStat(), stat.getMessage());
					Proxy.getLogicalExercises(logicalExercises);
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Set magnet error");					
				}
			});
		} catch (RequestException e) {
			Window.alert("Failed to send the request: " + e.getMessage());
		}
		
	}

	public static void SetMagnetExercises(String assignedMagnets) {
		if(assignedMagnets.equals("")) assignedMagnets = "none";
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=SetMagnetExercises&list=" + assignedMagnets);
		try{
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus stat = new WEStatus(response);
					Notification.notify(stat.getStat(), stat.getMessage());
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Set magnet error");					
				}
			});
		} catch (RequestException e) {
			Window.alert("Failed to send the request: " + e.getMessage());
		}
		
	}

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
	}
	
}
