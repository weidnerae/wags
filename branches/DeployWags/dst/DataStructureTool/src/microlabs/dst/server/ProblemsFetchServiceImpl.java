package microlabs.dst.server;

import microlabs.dst.client.ProblemsFetchService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ProblemsFetchServiceImpl extends RemoteServiceServlet implements ProblemsFetchService
{
	//NOTE: THE INDEX IN THE ARRAY HERE REPRESENTS THE ID NUMBER OF THE PROBLEM
	//THE ID IS USED TO FETCH THE PROBLEM CONTENTS FROM THE PROBLEM SERVICE
	//THIS NEEDS TO BE CLEANED UP
	public String[] getProblems() {
		return new String[] {"Binary Search Tree from Postorder Traversal 1",
							 "Binary Search Tree from Postorder Traversal 2",
							 "Binary Search Tree from Postorder Traversal 3",
							 "Binary Search Tree from Postorder Traversal 4",
							 "BST Preorder Traversal (Help on) 1",
							 "BST Inorder Traversal (Help on) 1",
							 "BST Postorder Traversal (Help on) 1",
							 "BST Preorder Traversal (Help off) 1",
							 "BST Inorder Traversal (Help off) 1",
							 "BST Postorder Traversal (Help off) 1"};
	}
}
