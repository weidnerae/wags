package webEditor.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import webEditor.client.view.CodeEditor;
import webEditor.client.view.Exercises;
import webEditor.client.view.FileBrowser;
import webEditor.client.view.Login;
import webEditor.client.view.Notification;
import webEditor.client.view.OutputReview;
import webEditor.client.view.Wags;
import webEditor.dst.client.DataStructureTool;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;


public class Proxy
{	
	// Get the URL for the host page
	//	- Will only return the URL of the host page, ending with a slash, without any of the 
	//	 php arguments
	// 	- Removes need for hardcoded URLs anymore.  Can upload client anywhere without having 
	//	 to change URL
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
					if(attribute.equals("vis"))
						Proxy.getVisibleExercises(exercises);
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

	public static void buildDST(){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=GetLogicalExercises");
		try {
		      @SuppressWarnings("unused")
			Request req = builder.sendRequest(null, new RequestCallback() {
		        public void onResponseReceived(Request request, Response response) {
		        	
		          WEStatus status = new WEStatus(response);
		         
		          String[] problemList = status.getMessageArray();
		          DataStructureTool DST = new DataStructureTool(problemList);
	
		          RootPanel.get().clear();
		          RootPanel.get().add(DST);
		        }
		        
		        public void onError(Request request, Throwable exception) {
		        	Window.alert("error");
		        }
		      });
		    } catch (RequestException e) {
		      Window.alert("Failed to send the request: " + e.getMessage());
		    }
	}
	
	public static void call(String command, HashMap<String, String> request, WagsCallback callback){
		Proxy.call(command, request, callback, RequestBuilder.GET);
	}

	/*
	 * Makes a request to server.
	 * The string command will correspond to a commad on the server.
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

	public static void checkMultiUser(final Wags wags){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=CheckMultiUser");
		try{
			builder.sendRequest(null, new RequestCallback(){
	
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Error in checkMultiUser request");
				}
	
				@Override
				public void onResponseReceived(Request request,
						Response response) {
					WEStatus status = new WEStatus(response);  
					
					if(status.getStat() == WEStatus.STATUS_ERROR){
						String title = status.getMessage();
						
						wags.assignPartner(title);
					}
				}
				
			});
		} catch (RequestException e) {
			Window.alert("Failed to send the request: " + e.getMessage());
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

	public static void getDSTSubmissions(String title, final Grid grid){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=DSTReview&title="+title);
		
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
			  		
			  		int k = 0;
			  		//Fills table with results
			  	    for (int row = 1; row < subInfo.length/3+1; ++row) {
			  	      for (int col = 0; col < 3; ++col)
			  	        grid.setText(row, col, subInfo[k++]);
			  	    }
					
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
	public static void getFileContents(String fileName, final CodeEditor editor){
		String urlCompl = getFileContents+"&name="+fileName.trim().substring(1);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, urlCompl);
		try {
			@SuppressWarnings("unused")
			Request r = builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response)
				{
					//Passing it through JSON kills formatting
					String allText = response.getText();
					
					//Grab status for uneditable codeArea for helper classes
					allText = allText.substring(5);  // WHAT IS THIS FOR???
					allText.trim();
					String status = allText.substring(0, 1); 
					allText = allText.substring(1);
					editor.codeArea.setEnabled(true); /* defaults to enabled */
					
					// Have to take into account comment length when
					//  parsing file
					//	-Now we will stop checking for the initial "//" part,
					//	 as that is a comment style accepted only by some 
					//	 languages (like Java).  Allow the file to have the necessary comment
					//	 marks for that language, and only check for the unique part
					//	 like "<end!TopSection>" instead of "//<end!TopSection>"
					//	-We will still require that two comment marks be used before
					//	 the unique part:
					//		-Ex: "//" for Java, "%%" for Prolog (even though only one '%' is
					//			needed in Prolog for a comment), etc
					String lengthFinder = "<end!TopSection>";
					int len = lengthFinder.length();
					
					// Find the end of top and middle comments
					int endofTop = allText.indexOf("<end!TopSection>");
					int endofMid = allText.indexOf("<end!MidSection>");
					String top = "", mid = allText, bot = "";
							
					//Logic copied from server side
					//	-Except now we want to keep the section comments in the
					//	 top and bottom portions, so that they do not have to be
					//	 added in again later in Wags.java
					if(endofTop != -1){
						top = allText.substring(0, endofTop + len); // keep the comment in top
						mid = allText.substring(endofTop + len); // don't include comment in mid
					}
					
					if(endofMid != -1){
						bot = allText.substring(endofMid - 2); // keep comment in bottom
						mid = allText.substring(endofTop + len, endofMid - 2); // don't leave //, or %%, or etc in mid
					}
					
					editor.codeTop = top;
					editor.codeBottom = bot;
					editor.codeArea.setText(mid);
					
					if(status.equals("0")) 
						editor.codeArea.setEnabled(false); // if status = 0, file is uneditable
				
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
	
	public static void getLogicalExercises(final ListBox logicalExercises){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL() + "?cmd=GetLogicalExercises");
		try {
			builder.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					WEStatus status = new WEStatus(response);
					
					String[] problemList = status.getMessageArray();
					logicalExercises.clear(); //To avoid repeat listings
					for(int i = 0; i < problemList.length - 1; i++){
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

	public static void getSections(final ListBox sections) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, getSections);
		try {
		      @SuppressWarnings("unused")
			Request req = builder.sendRequest(null, new RequestCallback() {
		        public void onResponseReceived(Request request, Response response) {
		        	
		          WEStatus status = new WEStatus(response);
		          
		          if(status.getStat() == WEStatus.STATUS_SUCCESS){
		        	  if(status.getMessageArray().length > 0){
		        		  String[] message = status.getMessageArray();
		        		 
		        		  for(String section: message){
		        			  sections.addItem(section.substring(1, section.length()-1)); //strip quotes
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
	 */
	public static void getUsersName(final Label label)
	{
		WagsCallback c = new WagsCallback() {
			@Override
			void warning(WEStatus status) {}
			
			@Override
			void success(WEStatus status) {
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

	public static void isAdmin(final TabLayoutPanel tabPanel){	
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Proxy.getBaseURL()+"?cmd=IsAdmin");
		try {
		      @SuppressWarnings("unused")
			Request req = builder.sendRequest(null, new RequestCallback() {
		        public void onResponseReceived(Request request, Response response) {
		          WEStatus status = new WEStatus(response);
	
		          if(status.getStat() != WEStatus.STATUS_SUCCESS){
		        	  //Note: Counts reset after each remove, so
		        	  //remove(2) then remove(3) would not work
		        	  //We could do remove(2) then remove(2), but 
		        	  //that's just confusing for no reason
		        	  tabPanel.remove(3);
		        	  tabPanel.remove(2);
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
	 * Tell server we want to login.
	 */
	public static void login(String username, String password, final String location)
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
						if(location.equals("editor")){
							Wags e = new Wags();
							e.go();
						}
						if(location.equals("dst")){
//							DataStructureTool t = new DataStructureTool();
//							t.go();
							Proxy.buildDST();
						}
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
						Proxy.login(username, password, "editor");
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