package webEditor.admin.builders;

import org.vaadin.gwtgraphics.client.Line;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;

public class BasicEdge extends Line {
	private final static int NODE_HALF = 20;
	private final int LINE_WIDTH = 10;
	private final boolean DEBUG = true;
	
	private BasicNode parent, child;
	private BasicNode n1, n2;
	boolean connectLeftChild;
	boolean validPositions;

	public BasicEdge(int x1, int y1, int x2, int y2) {
		super(x1, y1, x2, y2);
	}
	
	// Takes two nodes, creates a line to connect them,
	// and assigns the relationship between them
	public BasicEdge(BasicNode n1, BasicNode n2){
		super(n1.xPos + NODE_HALF, n1.yPos + NODE_HALF, 
				n2.xPos + NODE_HALF, n2.yPos + NODE_HALF);
		
		this.n1 = n1;
		this.n2 = n2;
		this.setStrokeWidth(LINE_WIDTH);
		validPositions = validPositions();
		if(validPositions){
			assignRelationship();
		}
		
		this.addClickHandler(new edgeRemoveClick(this));
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
	
	public void delete(){
		this.removeFromParent();
		this.setVisible(false);
		if(connectLeftChild){
			parent.leftChild = null;
		} else {
			parent.rightChild = null;
		}
		
		child.parent = null;
	}
	
	// When an edge gets clicked, it gets removed
	private class edgeRemoveClick implements ClickHandler{
		BasicEdge edge;
		
		public edgeRemoveClick(BasicEdge edge){
			this.edge = edge;
		}
		
		// Edges get removed whenever they are clicked
		public void onClick(ClickEvent event) {
			if(DEBUG) Window.alert("Was clicked");
			
			edge.delete();
		}
		
	}

}
