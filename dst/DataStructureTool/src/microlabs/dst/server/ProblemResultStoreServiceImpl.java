package microlabs.dst.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import microlabs.dst.client.ProblemResult;
import microlabs.dst.client.ProblemResultStoreService;
import microlabs.dst.shared.SerialEdge;
import microlabs.dst.shared.SerialNode;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ProblemResultStoreServiceImpl extends RemoteServiceServlet implements ProblemResultStoreService
{
	public String saveProblemResults(ProblemResult result) 
	{
		ArrayList<NodeJDO> nodes = new ArrayList<NodeJDO>();
		ArrayList<EdgeJDO> edges = new ArrayList<EdgeJDO>();

		for(int i = 0; i < result.getNodes().size(); i++)
		{
			SerialNode n = result.getNodes().get(i); 
			nodes.add(new NodeJDO(n.getValue(), n.getTop(), n.getLeft()));
		}

		for(int i = 0; i < result.getEdges().size(); i++)
		{
			SerialEdge e = result.getEdges().get(i);
			edges.add(new EdgeJDO(e.getN1(), e.getN2()));
		}

		String email = UserServiceFactory.getUserService().getCurrentUser().getEmail();
		email = email.replace("@", "XATSIGNX");
		ProblemResultJDO resultJDO = new ProblemResultJDO(
				email, result.getAttemptNum(), result.getProblemName(), result.getProblemText(), nodes,
				edges, result.getCurrFeedback());

		PersistenceManager pm = PMF.get().getPersistenceManager();

		try
		{
			pm.makePersistent(resultJDO);

			return "success";
		}
		finally
		{	
			pm.close();
		}
	}
}
