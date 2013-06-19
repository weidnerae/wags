package webEditor.admin.builders;

public class Traversals {
	public static String getPreorderTraversal(Node_Basic root){
		return 	preorder(root).replace("", " ").trim();
	}
	
	public static String getInorderTraversal(Node_Basic root){
		return inorder(root).replace("", " ").trim();
	}
	
	public static String getPostorderTraversal(Node_Basic root){
		return postorder(root).replace(""," ").trim();
	}
	
	private static String preorder(Node_Basic tree){
		String traversal = "";
		if(tree != null){
			traversal += tree.value ;
			traversal += preorder(tree.leftChild);
			traversal += preorder(tree.rightChild);
		}
		
		return traversal;
	}
	private static String inorder(Node_Basic tree){
		String traversal = "";
		if(tree != null){
			traversal += inorder(tree.leftChild);
			traversal += tree.value;
			traversal += inorder(tree.rightChild);
		}
		
		return traversal;
	}	
	private static String postorder(Node_Basic tree){
		String traversal = "";
		if(tree != null){
			traversal += postorder(tree.leftChild);
			traversal += postorder(tree.rightChild);
			traversal += tree.value;
		}
		
		return traversal;
	}

}
