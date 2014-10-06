package webEditor.logical.TreeProblems;

import java.util.ArrayList;

import webEditor.Proxy;
import webEditor.ProxyFramework.AbstractServerCall;
import webEditor.ProxyFramework.SubmitDSTCommand;
import webEditor.logical.DSTConstants;
import webEditor.logical.EdgeParent;
import webEditor.logical.Evaluation;
import webEditor.logical.Node;
import webEditor.logical.NodeClickable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Evaluation_BSTTraversal extends Evaluation implements IsSerializable
{
	
	public String evaluate(String problemName, String[] arguments, ArrayList<Node> nodes, ArrayList<EdgeParent> edges)
	{	
		NodeClickable n = (NodeClickable) nodes.get(0);
		
		String theTrav = n.getTraversal();
		
		if(theTrav.trim().length() < arguments[0].trim().length())
		{
			AbstractServerCall dstCmd = new SubmitDSTCommand(problemName, 0);
			dstCmd.sendRequest();
			return "Feedback: Your traversal is incomplete.  Every node must be clicked once to complete a traversal";
		}
		else if(theTrav.trim().equals(arguments[0]))
		{
			AbstractServerCall dstCmd = new SubmitDSTCommand(problemName, 1);
			dstCmd.sendRequest();
			return "Feedback: Your traversal: " + theTrav + "\nCongratulatons, your traversal is correct.";
		}
		else{
			
			AbstractServerCall dstCmd = new SubmitDSTCommand(problemName, 0);
			dstCmd.sendRequest();
			int i = 0;
			while(theTrav.charAt(i) == arguments[0].charAt(i)){
				i++;
			}
			
			if(i > 1){
				String correct = theTrav.substring(0, i);
				return "Feedback: Your traversal: " + theTrav + "\nThe nodes in your traversal are out of order.  The portion " +
				correct + " was correct. Retrace your steps to find your mistake.";	
			}
			
			return "Feedback: Your traversal: " + theTrav + "\nYour traversal was incorrect.  For the given traversal, what " +
				"would be the first node visited?";
			
		}
	}

	@Override
	public int returnKeyValue() {
		return DSTConstants.BST_TRAVERSAL_KEY;
	}
}
