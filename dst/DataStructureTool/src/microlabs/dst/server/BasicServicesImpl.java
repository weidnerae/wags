package microlabs.dst.server;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import microlabs.dst.client.BasicServices;
import microlabs.dst.shared.AvailableServices;

@SuppressWarnings("serial")
public class BasicServicesImpl extends RemoteServiceServlet implements BasicServices
{	
	public String doService(int requestedService)
	{
		if(requestedService == AvailableServices.GET_EMAIL_ADDR_AND_LOGOUT_URL)
			return UserServiceFactory.getUserService().getCurrentUser().getEmail() + 
			";" + UserServiceFactory.getUserService().createLogoutURL("http://www.google.com");
		else if(requestedService == AvailableServices.GET_REPORTS)
			return "Unimplmented";
		else 
			return "ERROR";
	}

}
