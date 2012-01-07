package webEditor.dst.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

import webEditor.dst.client.DSTConstants;
import webEditor.dst.client.EdgeParent;
import webEditor.dst.client.EdgeUndirected;
import webEditor.dst.client.Node;

public class AddEdgeRules_TreeMode extends AddEdgeRules implements IsSerializable
{	
	public String checkFirstNode(Node node, ArrayList<Node> nodes, ArrayList<EdgeParent> edges)
	{
		String result = checkNumNeighbors(node, edges);
		if(!result.equalsIgnoreCase(DSTConstants.CORRECT))
		{
			return result;
		}
		return DSTConstants.CORRECT;
	}

	public String checkSecondNode(Node firstNode, Node secondNode, ArrayList<Node> nodes, ArrayList<EdgeParent> edges)
	{
		String result = checkForExistingEdge(firstNode, secondNode, edges);
		if(!result.equalsIgnoreCase(DSTConstants.CORRECT))
		{
			return result;
		}

		result = checkForParent(firstNode, secondNode, edges);
		if(!result.equalsIgnoreCase(DSTConstants.CORRECT))
		{
			return result;
		}

		/**result = checkForCycle(firstNode, secondNode, nodes, edges);
		if(!result.equalsIgnoreCase(DSTConstants.CORRECT))
		{
			return result;
		}**/
		return DSTConstants.CORRECT;
	}

	private String checkNumNeighbors(Node node, ArrayList<EdgeParent> edges)
	{
		int numNeighbors = 0;
		for(int i = 0; i < edges.size(); i++)
		{
			EdgeParent edge = edges.get(i);
			if((edge.getN1() == node) || edge.getN2() == node)
			{
				numNeighbors++;
			}

			if(numNeighbors >= 3)
			{
				return "Cannot add another edge to this node. It has reached the maximum " +
						"one parent and two children allowed.  Select another edge or click" +
						"Cancel above.";
			}
		}
		return DSTConstants.CORRECT;
	}

	private String checkForExistingEdge(Node node1, Node node2, ArrayList<EdgeParent> edges)
	{
		for(int i = 0; i < edges.size(); i++)
		{
			EdgeParent edge = edges.get(i);
			if(((edge.getN1() == node1) && (edge.getN2() == node2)) || 
					((edge.getN1() == node2) && (edge.getN2() == node1)))
			{
				return "Cannot add edge, an edge already exists between these nodes.  " +
				"Select another node or click Cancel above.";
			}
		}
		return DSTConstants.CORRECT;
	}

	private String checkForParent(Node node1, Node node2, ArrayList<EdgeParent> edges)
	{
		for(int i = 0; i < edges.size(); i++)
		{
			EdgeParent edge = edges.get(i);
			if(node2 == edge.getN2())
			{
				return "Cannot add edge, this node already has a parent.  " +
				"Select another node or click Cancel above.";
			}
		}
		return DSTConstants.CORRECT;
	}
}