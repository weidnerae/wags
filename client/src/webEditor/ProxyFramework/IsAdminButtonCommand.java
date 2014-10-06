package webEditor.ProxyFramework;

import webEditor.WEStatus;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Button;

public class IsAdminButtonCommand extends AbstractServerCall {

	private Button section_btn;
	
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
		boolean root = false;
		
		if (status.getStat() == WEStatus.STATUS_SUCCESS)
		{
			section_btn.setVisible(true);
		}
	}
	
	public IsAdminButtonCommand(final Button section_btn)
	{
		command = ProxyCommands.IsAdmin;
		this.section_btn = section_btn;
	}
	
}
