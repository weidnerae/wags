package microlabs.dst.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

import microlabs.dst.client.EdgeParent;
import microlabs.dst.client.Node;
import microlabs.dst.client.NodeClickable;

public class Evaluation_BSTTraversal extends Evaluation implements IsSerializable
{
	public String evaluate(String[] arguments, ArrayList<Node> nodes, ArrayList<EdgeParent> edges)
	{	
		NodeClickable n = (NodeClickable) nodes.get(0);
		
		String theTrav = n.getTraversal();
		
		if(theTrav.length() < arguments[0].length())
		{
			return "Feedback: Your traversal is incomplete.  Every node must be clicked once to complete a traversal";
		}
		else if(theTrav.equals(arguments[0]))
			return "Feedback: Your traversal: " + theTrav + "\nCongratulatons, your traversal is correct.";
		else
			return "Feedback: Your traversal: " + theTrav + "\nThe nodes in your traversal are out of order.  Click highlighted" +
					" nodes to remove them from the traversal and try a different ordering.";
	}
}
