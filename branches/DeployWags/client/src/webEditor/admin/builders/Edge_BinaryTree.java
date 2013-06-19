package webEditor.admin.builders;

import com.google.gwt.user.client.Window;

public class Edge_BinaryTree extends Edge_Basic {
	boolean connectLeftChild;
	boolean validPositions;
	private Node_Basic parent, child;
	private final boolean DEBUG = false;

	public Edge_BinaryTree(Node_Basic n1, Node_Basic n2, BasicCanvas canvas) {
		super(n1, n2, canvas);
		
		validPositions = validPositions();
		if(validPositions){
			assignRelationship();
		}
	}
	
	
	// Used to make sure we can tell which node is parent and which
	// is child (and what child)
	private boolean validPositions(){
		if(n1.yPos == n2.yPos || n1.xPos == n2.xPos){
			Window.alert("Illegal positioning!");
			return false;
		}
		
		return true;
	}
	
	protected void onDelete(){
		if(connectLeftChild){
			parent.leftChild = null;
		} else {
			parent.rightChild = null;
		}
		
		child.parent = null;
	}
	
	
	public boolean isParent(Node_Basic node){
		return node.equals(parent);
	}
	
	public boolean isValid(){
		return validPositions;
	}
	
	// Actually assigns the relationship within the nodes
	private void assignRelationship(){
		// Determine relationships
		if(n1.yPos > n2.yPos){
			parent = n2;
			child = n1;
		} else {
			parent = n1;
			child = n2;
		}
		
		if(child.xPos < parent.xPos){
			connectLeftChild = true;
		} else {
			connectLeftChild = false;
		}
		
		// Check validity of relationship
		if(!validChildToParent()){
			Window.alert("Child can only have one parent!");
			validPositions = false;
			return;
		}
		if(!validParentToChild()){
			Window.alert("Parent cannot have duplicate left/right child!");
			validPositions = false;
			return;
		}
		
		// If valid, establish relationship, assign edges
		child.parent = parent;
		if(connectLeftChild) parent.leftChild = child;
		else parent.rightChild = child;
		child.addEdge(this);
		parent.addEdge(this);
		
		asString = parent.value + " " + child.value;
		
		
		if(DEBUG){
			String tmp = parent.value + " is parent of " + child.value;
			String tmp2;
			if(connectLeftChild){
				tmp2 = "Left child";
			} else {
				tmp2 = "Right child";
			}
			
			Window.alert(tmp + "\n" + tmp2);
		}
	}
	
	private boolean validChildToParent(){
		if(child.parent != null) return false;
		
		return true;
	}
	
	private boolean validParentToChild(){
		if(connectLeftChild){
			if(parent.leftChild != null) return false;
		} else {
			if(parent.rightChild != null) return false;
		}
		
		return true;
	}

}
