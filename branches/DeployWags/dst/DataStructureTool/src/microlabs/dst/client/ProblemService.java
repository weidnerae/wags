package microlabs.dst.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("problemServiceServlet")
public interface ProblemService extends RemoteService 
{
	Problem getProblem(int id);
}
