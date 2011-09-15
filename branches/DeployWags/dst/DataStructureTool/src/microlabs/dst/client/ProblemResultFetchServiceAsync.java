package microlabs.dst.client;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProblemResultFetchServiceAsync 
{
	void getProblemResults(AsyncCallback<List<ProblemResult>> callback);
}
