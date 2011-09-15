package microlabs.dst.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import microlabs.dst.client.ProblemResult;
import microlabs.dst.client.ProblemResultFetchService;
import microlabs.dst.shared.SerialEdge;
import microlabs.dst.shared.SerialNode;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ProblemResultFetchServiceImpl  extends RemoteServiceServlet implements ProblemResultFetchService
{
	public List<ProblemResult> getProblemResults() 
	{	
		String email = UserServiceFactory.getUserService().getCurrentUser().getEmail();
		email = email.replace("@", "XATSIGNX");
		
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try
		{
			Query query = pm.newQuery(ProblemResultJDO.class);
			query.setFilter("emailAddress == email");
			query.setOrdering("problemName asc, attemptNum asc");
			query.declareParameters("String email");
			List<ProblemResultJDO> results = (List<ProblemResultJDO>) query.execute(email);

			if(results != null)
			{
				List<ProblemResult> probRes = new ArrayList<ProblemResult>();
				
				for(int i = 0; i < results.size(); i++)
				{
					ArrayList<SerialNode> nodes = new ArrayList<SerialNode>();
					for(int j = 0; j < results.get(i).getNodes().size(); j++)
					{
							NodeJDO nJDO = results.get(i).getNodes().get(j);
							SerialNode n = new SerialNode(nJDO.getValue(), nJDO.getTop(), nJDO.getLeft());
							nodes.add(n);
					}
					
					ArrayList<SerialEdge> edges = new ArrayList<SerialEdge>();
					for(int j = 0; j < results.get(i).getEdges().size(); j++)
					{
						EdgeJDO eJDO = results.get(i).getEdges().get(j);
						char n1 = eJDO.getN1();
						char n2 = eJDO.getN2();
						SerialEdge edge = new SerialEdge(n1, n2);
						edges.add(edge);
					}
					
					ProblemResult r = new ProblemResult(results.get(i).getProblemName(), results.get(i).getProblemText(), nodes, edges, 
							results.get(i).getCurrFeedback(), results.get(i).getAttemptNum());
					
					probRes.add(r);
				}
				
				return probRes;
			}
			else
			{
				return null;
			}
		}
		finally
		{	
			pm.close();
		}
	}	
}
