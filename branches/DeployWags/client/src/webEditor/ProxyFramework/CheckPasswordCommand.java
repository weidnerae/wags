package webEditor.ProxyFramework;

import webEditor.AssignNewPasswordHandler;
import webEditor.WEStatus;

import com.google.gwt.http.client.Response;

/**
 * @author Dakota Murray
 * 
 * Server File: CheckPassword.php
 * Arguments: Wags object, used to replace center content is password is correct
 * 
 *
 */
public class CheckPasswordCommand extends AbstractCommand {

	
	@Override
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
		if(status.getStat() == WEStatus.STATUS_ERROR) {	
			AssignNewPasswordHandler.handleAssignNewPassword();
		}

	}
	
	public CheckPasswordCommand()
	{
		command = ProxyCommands.CheckPassword;		
	}

}
