package webEditor.logical.TreeProblems.MSTProblems;

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

public class Evaluation_MSTTraversal extends Evaluation implements IsSerializable
{
	
	public String evaluate(String problemName, String[] arguments, ArrayList<Node> nodes, ArrayList<EdgeParent> edges)
	{	
		NodeClickable n = (NodeClickable) nodes.get(0);
		
		String theTrav = n.getTraversal();
		
		if(theTrav.trim().length() < arguments[0].trim().length())
		{
			AbstractServerCall DSTCmd = new SubmitDSTCommand(problemName, 0);
			DSTCmd.sendRequest();
			return "Feedback: Your MST is incomplete.  Make sure that you're reaching every node.";
		}
		else if(theTrav.trim().equals(arguments[0]))
		{
			AbstractServerCall DSTCmd = new SubmitDSTCommand(problemName, 1);
			DSTCmd.sendRequest();
			return "Feedback: Congratulatons, your MST is correct.";
		}
		else{
			
			AbstractServerCall DSTCmd = new SubmitDSTCommand(problemName, 0);
			DSTCmd.sendRequest();
			int i = 0;
			while(theTrav.charAt(i) == arguments[0].charAt(i)){
				i++;
			}
			
			if(i > 1){
				String correct = theTrav.substring(0, i);
				return "Feedback: You selected the egdes: " + theTrav + "\nThe edges were incorrect.  The portion " +
				correct + " was correct. Retrace your steps to find your mistake.";	
			}
			
			return "Feedback: Your selected the edges: " + theTrav + "\nYour selection was incorrect.  No edges were in the correct order.  Be sure you are starting with the "+
			"appropriate edge.";
		
			
		}
	}

	@Override
	public int returnKeyValue() {
		return DSTConstants.MST_KEY;
	}
}
