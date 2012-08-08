package webEditor.dst.client;
/**
 * This definitely needs to be cleaned up and reworked, I just wanted something that would get the job done.
 * It just checks the inorder traversal of the heap that they made.
 */


import java.util.ArrayList;
import java.util.Stack;
import webEditor.client.Proxy; 

import com.google.gwt.user.client.rpc.IsSerializable;

public class Evaluation_MaxHeap_Preorder extends Evaluation  implements IsSerializable
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
		if(rootEvalNode == null){
			Proxy.submitDST(problemName, 0);
			return "You have removed too many nodes from the heap, make sure " +
					"you have gone back and added the necessary edges " +
					"to complete the new heap.";
		}

		if(testInorderTraversal(arguments[0], rootEvalNode, nodes, edges) == false)
		{
			Proxy.submitDST(problemName, 0);
			return errorMessage;
		}
				
		Proxy.submitDST(problemName, 1);
		return "Feedback: Congratulations! Your heap is correct.";
	}

	private Boolean testInorderTraversal(String correctTrav, EvaluationNode rootEvalNode, ArrayList<Node> nodes, ArrayList<EdgeParent> edges)
	{
		EvaluationNode current = rootEvalNode;

		String inorderTrav = "";

		while(!(current == null))
		{
			Stack<EvaluationNode> travNodes = new Stack<EvaluationNode>();
			while((travNodes.size() > 0) || (!(current == null)))
			{
				if(!(current == null))
				{
					travNodes.push(current);
					current = current.left == null ? null : convertNodeToEvalNode(treeNodes, current.left);
				}
				else
				{
					current = travNodes.pop();
					inorderTrav += current.node.getLabel().getText();
							current = current.right == null ? null : convertNodeToEvalNode(treeNodes, current.right);
				}
			}
		}

		System.out.println(inorderTrav);

		if(!inorderTrav.trim().equals(correctTrav))
		{
			errorMessage = "Feedback: Incorrect Heap.  The inorder traversal of your heap is " + inorderTrav;
			errorMessage += "\n while the inorder traversal of the correct heap should be " + correctTrav;
			return false;
		}

		return true;
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
		@SuppressWarnings("unused")
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
}
