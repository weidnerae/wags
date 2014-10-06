package webEditor.ProxyFramework;

import webEditor.WEStatus;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Widget;

public class IsAdminCommand extends AbstractServerCall {

	private Widget magnet;
	private Widget logical;
	private Widget admin;
	private Widget database;
	
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
		boolean root = false;
		
		if (status.getStat() == WEStatus.STATUS_SUCCESS || status.getStat() == WEStatus.STATUS_WARNING)
		{
			admin.setVisible(true);
			logical.setVisible(true);
			magnet.setVisible(true);
			database.setVisible(true);
		}
	}
	
	public IsAdminCommand(final Widget magnet, final Widget logical, final Widget admin, final Widget database)
	{
		command = ProxyCommands.IsAdmin;
		this.magnet = magnet;
		this.logical = logical;
		this.admin = admin;
		this.database = database;
	}
}
