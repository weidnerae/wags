package webEditor.logical;

import java.util.ArrayList;
import com.google.gwt.user.client.rpc.IsSerializable;

import webEditor.logical.DSTConstants;
import webEditor.logical.EdgeParent;
import webEditor.logical.Node;

public class AddEdgeRules implements IsSerializable
{
	public String checkFirstNode(Node node, ArrayList<Node> nodes, ArrayList<EdgeParent> edges)
	{
		
		return DSTConstants.CORRECT;
	}
	
	public String checkSecondNode(Node firstNode, Node secondNode, ArrayList<Node> nodes, ArrayList<EdgeParent> edges)
	{
		return DSTConstants.CORRECT;
	}
	
	public int returnKeyValue(){
		return DSTConstants.NO_EDGES_KEY;
	}
}
