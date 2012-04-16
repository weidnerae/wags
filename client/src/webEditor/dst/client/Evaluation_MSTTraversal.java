package webEditor.dst.client;

import java.util.ArrayList;

import webEditor.client.Proxy;

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
			return "Feedback: Your traversal is incomplete.  Make sure that you're reaching every node.";
		}
		else if(theTrav.trim().equals(arguments[0]))
		{
			Proxy.submitDST(problemName, 1);
			return "Feedback: Congratulatons, your traversal is correct.";
		}
		else{
			
			Proxy.submitDST(problemName, 0);
			int i = 0;
			while(theTrav.charAt(i) == arguments[0].charAt(i)){
				i++;
			}
			
			if(i > 1){
				String correct = theTrav.substring(0, i);
				return "Feedback: Your traversal was: " + theTrav + "\nThe nodes in your traversal are out of order.  The portion " +
				correct + " was correct. Please try to get the entire traversal correct.";	
			}
			
			return "Feedback: Your traversal: " + theTrav + "\nYour traversal was incorrect.  Be sure you are starting with the "+
			"appropriate edge.";
		
			
		}
	}
}
