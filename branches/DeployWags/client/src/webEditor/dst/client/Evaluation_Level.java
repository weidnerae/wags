package webEditor.dst.client;
/**
 * This definitely needs to be cleaned up and reworked, I just wanted something that would get the job done.
 * It just checks the inorder traversal of the heap that they made.
 */


import java.util.ArrayList;
import java.util.LinkedList;
import webEditor.client.Proxy; 

import com.google.gwt.user.client.rpc.IsSerializable;

public class Evaluation_Level extends Evaluation  implements IsSerializable
{	
	private ArrayList<EvaluationNode> treeNodes;
	
	public String evaluate(String problemName, String[] arguments, ArrayList<Node> nodes, ArrayList<EdgeParent> edges)
	{	
		errorMessage = "";
		treeNodes = new ArrayList<EvaluationNode>();
		
		for(int i = 0; i < edges.size(); i++)
		{
			EdgeUndirected e = (EdgeUndirected) edges.get(i);
			System.out.println(e.getN1().getValue() + " " + e.getN2().getValue());
		}		
		EvaluationNode rootEvalNode = buildEvaluationTree(nodes, edges);
		String levelTraversalPossibly = "*|*";
		levelTraversalPossibly = getLevelTraversal(rootEvalNode, arguments[0]);
		if(rootEvalNode == null){
			Proxy.submitDST(problemName, 0);
			return "Your tree is incomplete go back and add " +
				   " the necessary edges to complete the tree.";
		}
		if(levelTraversalPossibly.equals(arguments[0])){
			Proxy.submitDST(problemName,1);
			return "Feedback: Congratulations! Your treee is correct.";
		}
		else{
			Proxy.submitDST(problemName,0);
			return errorMessage;
		}
	}
	private EvaluationNode buildEvaluationTree(ArrayList<Node> nodes, ArrayList<EdgeParent> edges)
	{
		@SuppressWarnings("unchecked")
		ArrayList<Node> noParentNodes = (ArrayList<Node>) nodes.clone();
		@SuppressWarnings("unchecked")
		ArrayList<Node> unConnectedNodes = (ArrayList<Node>) nodes.clone();
		for(int i = 0; i < edges.size(); i++)
		{
			EdgeParent edge = edges.get(i);
			//finding the removed node/nodes
			if(unConnectedNodes.contains(edge.getN1()))  
				unConnectedNodes.remove(edge.getN1());
			if(unConnectedNodes.contains(edge.getN2()))
				unConnectedNodes.remove(edge.getN2());
			
			noParentNodes.remove(edge.getN2());
		}	
		
		// taking the removed nodes out of the noParentNodes list.
		for(Node n: unConnectedNodes){
			noParentNodes.remove(n);
		}
		//returns null if more than one node is disconnected from the heap
		if(unConnectedNodes.size()>1){
			return null;
		}
		
		Node rootNode = noParentNodes.get(0);
		EvaluationNode rootEvalNode = null;
		Node currNode = null;
		Node leftNode = null;
		Node parentNode = null;
		Node rightNode = null;

		for(int j = 0; j < nodes.size(); j++)
		{
			currNode = nodes.get(j);
			for(int i = 0; i < edges.size(); i++)
			{
				EdgeParent edge = edges.get(i);
				if(currNode.getValue() == edge.getN1().getValue())
				{
					if(currNode.getLeft() > edge.getN2().getLeft())
					{
						leftNode = edge.getN2();
					}
					else if(currNode.getLeft() < edge.getN2().getLeft())
					{
						rightNode = edge.getN2();
					}
				}
				else if(currNode.getValue() == edge.getN2().getValue())
				{
					parentNode = edge.getN1();
				}
			}

			EvaluationNode evalNode = new EvaluationNode(currNode, parentNode, leftNode, rightNode);
			treeNodes.add(evalNode);
			parentNode = null;
			leftNode = null;
			rightNode = null;

			if(currNode.getValue() == rootNode.getValue())
			{
				rootEvalNode = evalNode;
			}
		}
		
		for(int i = 0; i < treeNodes.size(); i++)
		{
			EvaluationNode n = treeNodes.get(i);
			System.out.print("Val: " + n.node.getValue());
			if(n.parent != null)
				System.out.print(" Par: " + n.parent.getValue());
			if(n.left != null)
				System.out.print(" Left: " + n.left.getValue());
			if(n.right != null)
				System.out.print(" Right: " + n.right.getValue());
			System.out.println("");
		}
		return rootEvalNode;
	}

	private EvaluationNode convertNodeToEvalNode(ArrayList<EvaluationNode> evalNodes, Node node)
	{
		for(int i = 0; i < evalNodes.size(); i++)
		{
			EvaluationNode theNode = evalNodes.get(i);
			if(theNode.node.getValue() == node.getValue())
			{
				return theNode;
			}
		}
		return null;
	}

	private class EvaluationNode implements IsSerializable
	{
		public Node parent;
		public Node node;
		public Node left;
		public Node right;
		public boolean visited;

		public EvaluationNode(Node node, Node parent, Node left, Node right)
		{
			this.node = node;
			this.parent = parent;
			this.left = left;
			this.right = right;
			this.visited = false;
		}
	}
	public String getLevelTraversal(EvaluationNode rootEvalNode, String correctTraversal){
		LinkedList<EvaluationNode> nodeList = new LinkedList<EvaluationNode>();
		String solution ="";
		EvaluationNode currentNode= rootEvalNode;
		nodeList.addLast(currentNode);
     	while(solution.length()<correctTraversal.length()){
     		if(nodeList.size()!=0){
		        currentNode=nodeList.removeFirst();
				solution += currentNode.node.getValue();
		        if(currentNode!=null){
					if(currentNode.left!=null){
						nodeList.addLast(convertNodeToEvalNode(treeNodes,currentNode.left));
					}
					if(currentNode.right!=null){
						nodeList.addLast(convertNodeToEvalNode(treeNodes,currentNode.right));
					}
		        }
     		}
			else{
				solution+=".";
			}
		}
     	if(solution.contains(".")){
     		errorMessage = "FeedBack: Your tree is incomplete, make sure that all " +
     				"nodes are connected with edges.";
     	}
     	else if(!solution.equals(correctTraversal)){
     		String correct="";
     		for(int i=0;i<solution.length();i++){
     			if(solution.charAt(i)==correctTraversal.charAt(i)){
     				correct+=""+solution.charAt(i);
     			}
     		}
     		errorMessage = "Feedback: Incorrect tree. The level traversal of your" +
     				" tree is correct for the segment: "+correct;
     	}
		return solution;
	}
}
