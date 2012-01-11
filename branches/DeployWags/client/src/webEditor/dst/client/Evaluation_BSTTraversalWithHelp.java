package webEditor.dst.client;

import java.util.ArrayList;

import webEditor.client.Proxy;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Evaluation_BSTTraversalWithHelp extends Evaluation implements IsSerializable
{
	public String evaluate(String problemName, String[] arguments, ArrayList<Node> nodes, ArrayList<EdgeParent> edges)
	{	
		NodeClickable n = (NodeClickable) nodes.get(0);
		
		String theTrav = n.getTraversal();
		
		if(theTrav.length() < arguments[0].length())
		{
			boolean foundIncorrect = false;
			String incorrectNodes = "";
			for(int i = 0; i < theTrav.length(); i++)
			{
				if(foundIncorrect)
					incorrectNodes += theTrav.charAt(i) + "";
				else if((theTrav.charAt(i) != arguments[0].charAt(i)) && !foundIncorrect)
				{
					foundIncorrect = true;
					incorrectNodes += theTrav.charAt(i) + "";
				}
			}
			
			if(incorrectNodes.length() > 0){
				Proxy.submitDST(problemName, 0);
				return "Feedback: Your traversal: " + theTrav + "\nNode(s): " + incorrectNodes + " have been clicked out of order in relation to the " +
						"correct traversal.  Deselect the node(s) and try another ordering.";
			}
			else
				return "";
		}
		else if(theTrav.equals(arguments[0]))
		{
			Proxy.submitDST(problemName, 1);
			return "Feedback: Your traversal: " + theTrav + "\nCongratulatons, your traversal is correct.";
		}
		else
			Proxy.submitDST(problemName, 0);
			return "Feedback: Your traversal: " + theTrav + "\nThe nodes in your traversal are out of order.  Click highlighted" +
					" nodes to remove them from the traversal and try a different ordering.";
	}
}
