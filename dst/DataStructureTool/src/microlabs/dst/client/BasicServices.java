package microlabs.dst.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Interface needed for the email and logout Url RPC call */

@RemoteServiceRelativePath("basicServicesServlet")
public interface BasicServices extends RemoteService 
{
	public String doService(int requestedService);
}
