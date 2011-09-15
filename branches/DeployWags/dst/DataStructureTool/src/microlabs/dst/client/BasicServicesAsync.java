package microlabs.dst.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Interface needed for the email and logout Url RPC call */

public interface BasicServicesAsync 
{
	void doService(int requestedService, AsyncCallback<String> callback);
}
