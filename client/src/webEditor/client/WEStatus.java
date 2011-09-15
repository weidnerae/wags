package webEditor.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
	private Map<String, String> messageMap;
	private boolean bool;

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
			}
			// JSON Boolean
			if(jbool != null){
				bool = jbool.booleanValue();
			}
		}
	}
	
	//So, this will be cleaned up later - No reason
	//WEStatus shouldn't also accept text as a parameter
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
	
	public Map<String, String> getMessageMap(){
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
}
