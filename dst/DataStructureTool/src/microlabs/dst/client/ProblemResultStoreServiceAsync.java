package microlabs.dst.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProblemResultStoreServiceAsync {
	void saveProblemResults(ProblemResult result, AsyncCallback<String> callback);
}
