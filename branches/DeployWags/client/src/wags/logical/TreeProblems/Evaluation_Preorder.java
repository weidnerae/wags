package wags.logical.TreeProblems;

import java.util.ArrayList;

import java.util.Stack;

import wags.Proxy;
import wags.logical.DSTConstants;
import wags.logical.EdgeParent;
import wags.logical.EdgeUndirected;
import wags.logical.Evaluation;
import wags.logical.Node;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Evaluation_Preorder extends Evaluation  implements IsSerializable
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
		if(testRootNodeForPreOrderTraversal(arguments[0], nodes, edges) == false)
		{
			Proxy.submitDST(problemName, 0);
			return errorMessage;
		}
		
		EvaluationNode rootEvalNode = buildEvaluationTree(nodes, edges);

		if(testInorderTraversal(arguments[0], arguments[1], rootEvalNode, nodes, edges) == false)
		{
			Proxy.submitDST(problemName, 0);
			return errorMessage;
		}
		
		if(testPreOrderTraversal(arguments[0], rootEvalNode, nodes, edges) == false)
		{
			Proxy.submitDST(problemName, 0);
			return errorMessage;
		}
		
		Proxy.submitDST(problemName, 1);
		return "Feedback: Congratulations! Your tree is correct.";
	}

	private boolean testRootNodeForPreOrderTraversal(String postTrav, ArrayList<Node> nodes, ArrayList<EdgeParent> edges)
	{
		@SuppressWarnings("unchecked")
		ArrayList<Node> noParentNodes = (ArrayList<Node>) nodes.clone();

		for(int i = 0; i < edges.size(); i++)
		{
			EdgeParent edge = edges.get(i);
			noParentNodes.remove(edge.getN2());
		}

		if(noParentNodes.size() > 1)
		{
			errorMessage = "Feedback: Your tree is incomplete.  Continue adding edges to complete the tree.";
			return false;
		}
		else if(noParentNodes.size() == 1)
		{
			String[] splitNodes = postTrav.split(" ");
			String rootVal = splitNodes[0];
			Node rootNode = noParentNodes.get(0);
			if(rootVal != rootNode.getValue())
			{
				errorMessage += "Feedback: Your solution is not correct.  Remember that in a \n" +
				"preorder traversal, the first node visited is always the root node. ";
				return false;
			}
		}
		return true;
	}

	private Boolean testInorderTraversal(String postTrav, String correctTrav, EvaluationNode rootEvalNode, ArrayList<Node> nodes, ArrayList<EdgeParent> edges)
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
					inorderTrav += current.node.getValue()+" ";
					current = current.right == null ? null : convertNodeToEvalNode(treeNodes, current.right);
				}
			}
		}

		System.out.println(inorderTrav);

		if(!inorderTrav.trim().equals(correctTrav))
		{
			errorMessage = "Feedback: Incorrect inorder traversal.  Your traversal is " + inorderTrav;
			errorMessage += "\nCorrect inorder traversal is " + correctTrav;
			return false;
		}

		return true;
	}
	
	private boolean testPreOrderTraversal(String preTrav, EvaluationNode rootEvalNode, ArrayList<Node> nodes, ArrayList<EdgeParent> edges)
	{
		EvaluationNode current = rootEvalNode;
		String userPreOrderTrav = "";
		
		userPreOrderTrav += current.node.getValue();
		preTrav = preTrav.replaceAll(" ", "");
		
		while(userPreOrderTrav.length() < preTrav.length())
		{
			if((current.left != null) && (convertNodeToEvalNode(treeNodes, current.left).visited == false))
			{
				userPreOrderTrav += current.left.getValue();
				convertNodeToEvalNode(treeNodes, current.left).visited = true;
				current = convertNodeToEvalNode(treeNodes, current.left);
			}
			else
			{
				if((current.right != null) && (convertNodeToEvalNode(treeNodes, current.right).visited == false))
				{
					userPreOrderTrav += current.right.getValue();
					convertNodeToEvalNode(treeNodes, current.right).visited = true;
					current = convertNodeToEvalNode(treeNodes, current.right);
				}
				else
				{
					convertNodeToEvalNode(treeNodes, current.node).visited = true;
					current = convertNodeToEvalNode(treeNodes, current.parent);
				}
			}	
		}
		
		System.out.println("pre" + userPreOrderTrav);

		if(!userPreOrderTrav.trim().equals(preTrav))
		{
			errorMessage = "Feedback: Incorrect preorder traversal.  Your preorder traversal is " + userPreOrderTrav;
			errorMessage += "\nCorrect preorder traversal is " + preTrav;
			return false;
		}

		return true;
	}
	
	private EvaluationNode buildEvaluationTree(ArrayList<Node> nodes, ArrayList<EdgeParent> edges)
	{
		@SuppressWarnings("unchecked")
		ArrayList<Node> noParentNodes = (ArrayList<Node>) nodes.clone();

		for(int i = 0; i < edges.size(); i++)
		{
			EdgeParent edge = edges.get(i);
			noParentNodes.remove(edge.getN2());
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

	@Override
	public int returnKeyValue() {
		return DSTConstants.BST_PREORDER_KEY;
	}
}
