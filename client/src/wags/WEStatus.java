package wags;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;


public class WEStatus 
{
	public static final int STATUS_ERROR   = 0;
	public static final int STATUS_SUCCESS = 1;
	public static final int STATUS_WARNING = 2;
	
	private int stat;
	private String message = "";
	private String[] messageArray;
	private HashMap<String, String> messageMap;
	private boolean bool;
	private Object myObject;

	/**
	 * Takes a Response as parameter. Parses the response and builds
	 * a new WEStatus.
	 * @param response
	 */
	public WEStatus(Response response)
	{
		JSONValue vals = JSONParser.parseStrict(response.getText());
		JSONObject obj = vals.isObject();
		if(obj != null){
			JSONNumber stat   = obj.get("stat").isNumber();
			JSONString string = obj.get("msg").isString();
			JSONArray  array  = obj.get("msg").isArray();
			JSONObject object = obj.get("msg").isObject();
			JSONBoolean jbool = obj.get("msg").isBoolean();
			// JSON Number
			if (stat != null)
				this.stat = Integer.parseInt(stat.toString());
			// JSON String
			if (string != null) {
				String msg = string.toString();
				msg = msg.trim();
				// Remove the quotations at beginning and end of string.
				this.message = msg.substring(1, msg.length() - 1);
			}
			// JSON Array
			if (array != null) {
				messageArray = new String[array.size()];
				for (int i = 0; i < array.size(); i++) {
					String msg = ((JSONValue) array.get(i)).toString();
					messageArray[i] = msg.substring(1, msg.length() - 1);// Remove quotes
				}
				
				//Set message to concatenated contents of array
				for (String msg:messageArray){
					if(msg.length() > 0)
						this.message += msg + " | ";
				}
				
				this.message = this.message.substring(0, message.length()-3); //remove last " | "
			}
			// JSON Object
			if(object != null){
				Set<String> keys = object.keySet();
				messageMap = new HashMap<String, String>();
				Iterator<String> itr = keys.iterator();
				String key;
				String val;
				while(itr.hasNext()){
					key = itr.next();
					val = object.get(key).toString();
					if(val.equals("null")){
						val = null;
					}else{
						val = val.substring(1, val.length()-1);// Remove quotes
					}
					messageMap.put(key, val);
				}
				
				// Since we have a limited number of "objects" being passed from the
				// server, we'll go ahead and construct similar objects on the client
				if(messageMap.containsKey("Object")){
					createObject(messageMap);
				}
			}
			// JSON Boolean
			if(jbool != null){
				bool = jbool.booleanValue();
			}
		}
	}
	
	public WEStatus(String JSONtext)
	{
		JSONValue vals = JSONParser.parseStrict(JSONtext);
		JSONObject obj = vals.isObject();
		if(obj != null){
			JSONNumber stat   = obj.get("stat").isNumber();
			JSONString string = obj.get("msg").isString();
			JSONArray  array  = obj.get("msg").isArray();
			JSONObject object = obj.get("msg").isObject();
			JSONBoolean jbool = obj.get("msg").isBoolean();
			// JSON Number
			if (stat != null)
				this.stat = Integer.parseInt(stat.toString());
			// JSON String
			if (string != null) {
				String msg = string.toString();
				msg = msg.trim();
				// Remove the quotations at beginning and end of string.
				this.message = msg.substring(1, msg.length() - 1);
			}
			// JSON Array
			if (array != null) {
				messageArray = new String[array.size()];
				for (int i = 0; i < array.size(); i++) {
					String msg = ((JSONValue) array.get(i)).toString();
					messageArray[i] = msg.substring(1, msg.length() - 1);// Remove quotes
				}
				
				//Set message to concatenated contents of array
				for (String msg:messageArray){
					if(msg.length() > 0)
						this.message += msg + " | ";
				}
				
				this.message = this.message.substring(0, message.length()-3); //remove last " | "
			}
			// JSON Object
			if(object != null){
				Set<String> keys = object.keySet();
				messageMap = new HashMap<String, String>();
				Iterator<String> itr = keys.iterator();
				String key;
				String val;
				while(itr.hasNext()){
					key = itr.next();
					val = object.get(key).toString();
					if(val.equals("null")){
						val = null;
					}else{
						val = val.substring(1, val.length()-1);// Remove quotes
					}
					messageMap.put(key, val);
				}
			}
			// JSON Boolean
			if(jbool != null){
				bool = jbool.booleanValue();
			}
		}
	}
	
	public int getStat()
	{
		return this.stat;
	}
	
	public String getMessage()
	{
		return this.message;
	}
	
	public String[] getMessageArray()
	{
		return this.messageArray;
	}
	
	public HashMap<String, String> getMessageMap(){
		return this.messageMap;
	}
	
	public String getMessageMapVal(String key){
		if(messageMap == null)
			return null;

		return messageMap.get(key);
	}
	
	public boolean getBool(){
		return bool;
	}
	
	private void createObject(HashMap<String, String> messageMap){
		String objType = messageMap.get("Object");
		if(objType == "MagnetProblem"){		
			int id = Integer.parseInt(messageMap.get("id"));
			
			// This is a stopgap to see if we can get it working correctly - Jon
			String mainFunction = messageMap.get("solution");
						
			// Get arrays
			String[] innerFunctions, forLeft, forMid, forRight, bools, statements, allStatements, oldStatements, newStatements, createdIDs;
			int numStatements;
			innerFunctions = parseArray(messageMap.get("innerFunctions"));
			forLeft = parseArray(messageMap.get("forLeft"));
			forMid = parseArray(messageMap.get("forMid"));
			forRight = parseArray(messageMap.get("forRight"));
			bools = parseArray(messageMap.get("bools"));
			allStatements = messageMap.get("statements").split(".:3:.");
			oldStatements = parseArray(allStatements[0]);
			numStatements = oldStatements.length+(innerFunctions.length);
			if(allStatements.length >1){
				String[][] createdStatementsAndIDs = parseCreated(allStatements);
				newStatements = createdStatementsAndIDs[1];
				createdIDs = createdStatementsAndIDs[0];
				statements = concatenateArrays(oldStatements,newStatements);
			} else{
				createdIDs = new String[0];
				statements = oldStatements;
				newStatements = new String[0];
			}
			// Create the object
			myObject = new MagnetProblem(id, messageMap.get("title"), messageMap.get("directions"), 
					messageMap.get("type"), mainFunction, innerFunctions, forLeft, forMid, forRight, bools,
					statements, createdIDs, numStatements, messageMap.get("solution"), messageMap.get("state"));
		} else if (objType == "LogicalMicrolab"){
			// Pretty much just passes the database information into the LogicalMicrolab constructor.
			// The real "parsing" of information happens in LogicalMicrolab.getProblem, which uses
			// the 'genre' of the LogicalMicrolab to determine what sort of problem should be returned
			
			int edgeRul = handleInt(messageMap, "edgeRul");
			int edgeRem = handleInt(messageMap, "edgesRem");
			
			myObject = new LogicalMicrolab(messageMap.get("title"), 
					messageMap.get("problemText"),
					messageMap.get("nodes"), 
					messageMap.get("xPositions"), 
					messageMap.get("yPositions"),
					messageMap.get("insertMethod"), 
					messageMap.get("edges"), 
					Integer.parseInt(messageMap.get("evaluation")), 
					edgeRul,
					messageMap.get("arguments"),
					edgeRem,
					Integer.parseInt(messageMap.get("nodesDraggable")), 
					messageMap.get("nodeType"), 
					Integer.parseInt(messageMap.get("group")), 
					messageMap.get("genre"));
		}
	}
	
	private int handleInt(HashMap<String, String> map, String key){
		if(map.containsKey(key)){
			return Integer.parseInt(map.get(key));
		}
		
		return 0;
	}
	
	// If an object was created, return it.  Otherwise, return a String saying
	// there is no object
	public Object getObject(){
		if (!messageMap.isEmpty() && messageMap.containsKey("Object")){
			return myObject;
		} else {
			String oops = "No object!";
			return oops;
		}
			
	}
	
	private String[] parseArray(String parseText){
		
		return parseText.split(".:\\|:.");
		
	}
	
	private String[] concatenateArrays(String[] arr1, String[] arr2){
		String[] arr = new String[arr1.length+arr2.length-1];
		
		for(int i=0;i < arr1.length;i++){
			arr[i] = arr1[i];
		}
		for(int i=0;i < arr2.length;i++){
			arr[(arr1.length)+i] = arr2[i];
		}
		
		return arr;
	
	}
	
	private String[][] parseCreated(String[] allStatements){
		String[][] result = new String[2][allStatements.length-1];
		for(int i=1;i<allStatements.length;i++){
			String[] splitCreated = allStatements[i].split(".:\\|:.");
			result[1][i-1] = splitCreated[1];
			result[0][i-1] = splitCreated[0];
		}
		return result;
	}
}
