package webEditor.ProxyFramework;

import java.util.HashMap;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.URL;

public abstract class ProxyStringBuilder {
	
	private static final String baseURL = GWT.getHostPageBaseURL().concat("server.php");
	
	private static final String Prefix = "?cmd=";
	private static final String Delimeter = "&";
	private static final String Assignment = "=";
	
	public static String buildServerString( String command, HashMap<String, String> arguments, RequestBuilder.Method method)
	{
		if (method == RequestBuilder.GET)
		{
			return buildGetCommandString(command, arguments);
		}
		return buildPostCommandString(command);
	}
	
	public static String buildGetCommandString(String command, HashMap<String, String> arguments)
	{
		return URL.encode(baseURL + Prefix + command + buildArgumentString(arguments));
	}
	
	public static String buildPostCommandString(String command)
	{
		return baseURL + Prefix + command;
	}
	
	public static String buildArgumentString(HashMap<String, String> arguments)
	{
		if (arguments == null) {
			return "";
		}
		Set<String> Variables = arguments.keySet();
		String toReturn = "";
		for(String v : Variables) {
		    toReturn += Delimeter + v + Assignment + arguments.get(v);
		}
		return toReturn;
	}
	
	public static String buildPostArgumentString(HashMap<String, String> arguments)
	{
		if (arguments == null) {
			return "";
		}
		Set<String> Variables = arguments.keySet();
		String toReturn = "";
		for(String v : Variables) {
		    toReturn += v + Assignment + arguments.get(v) + Delimeter;
		}
		return toReturn;
	}
	
	public static String encodeString(String str)
	{
		return URL.encode(str);
	}	
}
