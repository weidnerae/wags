package microlabs.dst.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("problemsFetchServiceServlet")
public interface ProblemsFetchService extends RemoteService
{
	String[] getProblems();
}
