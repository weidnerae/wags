package microlabs.dst.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProblemServiceAsync {
	
	void getProblem(int id, AsyncCallback<Problem> callback);
}
