package microlabs.dst.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("problemResultStoreServiceServlet")
public interface ProblemResultStoreService extends RemoteService
{
	String saveProblemResults(ProblemResult result);
}
