package microlabs.dst.shared;

import java.util.ArrayList;
import com.google.gwt.user.client.rpc.IsSerializable;
import microlabs.dst.client.DSTConstants;
import microlabs.dst.client.EdgeParent;
import microlabs.dst.client.Node;

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
}
