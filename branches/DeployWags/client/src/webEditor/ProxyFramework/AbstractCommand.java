package webEditor.ProxyFramework;

import java.util.HashMap;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;

/**
 * @author: Dakota Murray
 * 
 * An abstract class which all server commands will extend. Object of type ABstractCommand must implement
 * the underlying logic of the sendRequest and handleResponse methods. sendRequest puts together the 
 * appropriate information and sends a request to the server using the ProxyCall class. Once a response is 
 * received the response if passed to the handleResponse method of the appropriate object. the handleResponse has all
 * of the underlying logic of how to deal with the server response. 
 * 
 * Subclasses must also set the appropriate values for the command string (@see ProxyCommands.java), the 
 * arguments HashMap (key = name of variable, value = value for that variable), the method of server call
 * (the only ones currently present in WAGS are GET and POST) as well as error and success messages, if they
 * are needed. 
 * @see ProxyCall.java
 *
 */
public abstract class AbstractCommand 
{

	protected String success = "";
	protected String error = "";
	protected HashMap<String, String> arguments;
	protected String command;
	
	//default server call type = GET
	protected RequestBuilder.Method method = RequestBuilder.GET;
	
	public void sendRequest()
	{
		ProxyCall proxy = new ProxyCall();
		proxy.call(this);
	}

	protected abstract void handleResponse(Response response);
	
	protected void addArgument(String argName, String argVal)
	{
		if (arguments == null) {
			arguments = new HashMap<String, String>();
		}
		arguments.put(argName,  argVal);
	}
	
	public String getCommand()
	{
		return command;
	}
	
	public String getSuccessMessage()
	{
		return success;
	}
	
	public String getErrorMessage()
	{
		return error;
	}
	
	public HashMap<String, String> getArguments()
	{
		return arguments;
	}
	
	public RequestBuilder.Method getMethod()
	{
		return method;
	}
}
