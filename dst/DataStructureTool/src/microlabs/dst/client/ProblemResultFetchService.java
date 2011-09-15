package microlabs.dst.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("problemResultFetchServiceServlet")
public interface ProblemResultFetchService extends RemoteService
{
	List<ProblemResult> getProblemResults();
}
