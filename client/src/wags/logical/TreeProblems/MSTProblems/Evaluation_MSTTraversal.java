package wags.logical.TreeProblems.MSTProblems;

import java.util.ArrayList;

import wags.Proxy;
import wags.logical.DSTConstants;
import wags.logical.EdgeParent;
import wags.logical.Evaluation;
import wags.logical.Node;
import wags.logical.NodeClickable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Evaluation_MSTTraversal extends Evaluation implements IsSerializable
{
	
	public String evaluate(String problemName, String[] arguments, ArrayList<Node> nodes, ArrayList<EdgeParent> edges)
	{	
		NodeClickable n = (NodeClickable) nodes.get(0);
		
		String theTrav = n.getTraversal();
		
		if(theTrav.trim().length() < arguments[0].trim().length())
		{
			Proxy.submitDST(problemName, 0);
			return "Feedback: Your MST is incomplete.  Make sure that you're reaching every node.";
		}
		else if(theTrav.trim().equals(arguments[0]))
		{
			Proxy.submitDST(problemName, 1);
			return "Feedback: Congratulatons, your MST is correct.";
		}
		else{
			
			Proxy.submitDST(problemName, 0);
			int i = 0;
			while(theTrav.charAt(i) == arguments[0].charAt(i)){
				i++;
			}
			
			if(i > 1){
				String correct = theTrav.substring(0, i);
				return "Feedback: You selected the egdes: " + theTrav + "\nThe edges were incorrectly.  The portion " +
				correct + " was correct. Please try to get the entire MST correct.";	
			}
			
			return "Feedback: Your selected the edges: " + theTrav + "\nYour selection was incorrect.  Be sure you are starting with the "+
			"appropriate edge.";
		
			
		}
	}

	@Override
	public int returnKeyValue() {
		return DSTConstants.MST_KEY;
	}
}
