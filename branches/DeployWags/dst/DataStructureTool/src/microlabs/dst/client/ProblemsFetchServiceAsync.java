package microlabs.dst.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProblemsFetchServiceAsync 
{
	void getProblems(AsyncCallback<String[]> callback);
}
