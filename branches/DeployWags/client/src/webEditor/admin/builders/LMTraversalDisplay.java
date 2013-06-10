package webEditor.admin.builders;


import webEditor.logical.DSTConstants;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;


public class LMTraversalDisplay extends BasicDisplay {
	TraversalPanel inorderPanel, preorderPanel, postorderPanel;
	// For when nodes/edges are added/removed
	public void onModify(){
		inorderPanel.clear();
		preorderPanel.clear();
		postorderPanel.clear();
	}
	
	public void calculate(){
		BasicNode root = canvas.getRoot();
		
		// Gets traversals and formats them for Binary Tree problems
		preorderPanel.fillText(preorder(root).replace("", " ").trim());
		inorderPanel.fillText(inorder(root).replace("", " ").trim());
		postorderPanel.fillText(postorder(root).replace("", " ").trim());
	}
	
	public void clear(){
		preorderPanel.fillText("");
		inorderPanel.fillText("");
		postorderPanel.fillText("");
	}

	private String preorder(BasicNode tree){
		String traversal = "";
		if(tree != null){
			traversal += tree.value ;
			traversal += preorder(tree.leftChild);
			traversal += preorder(tree.rightChild);
		}
		
		return traversal;
	}
	private String inorder(BasicNode tree){
		String traversal = "";
		if(tree != null){
			traversal += inorder(tree.leftChild);
			traversal += tree.value;
			traversal += inorder(tree.rightChild);
		}
		
		return traversal;
	}	
	private String postorder(BasicNode tree){
		String traversal = "";
		if(tree != null){
			traversal += postorder(tree.leftChild);
			traversal += postorder(tree.rightChild);
			traversal += tree.value;
		}
		
		return traversal;
	}
	
	private class assignClickHandler implements ClickHandler{
		BasicDisplay parent;
		TraversalPanel child;
		
		public assignClickHandler(BasicDisplay parent, TraversalPanel child){
			this.parent = parent;
			this.child = child;
		}
		@Override
		public void onClick(ClickEvent event) {
			if(child.getArguments()[0].length() == 0){
				Window.alert("No traversal provided");
				return;
			}
			parent.fillBuilder(child);
		}
		
	}

	@Override
	public void fillBuilder(ArgHolder child) {
		// Weird casting because ___.getAbsolute___ doesn't actually
		// seem to be returning an int, although that is what it claims.
		int canvasLeft = (int) Math.floor((double)canvas.getAbsoluteLeft());
		int canvasTop = (int) Math.floor((double)canvas.getAbsoluteTop());
		// Ehh... how to handle unattached nodes
		int[] xPos = new int[canvas.nodes.size()];
		int[] yPos = new int[canvas.nodes.size()];
				
		builder.setTitle(txtTitle.getText());
		builder.setProblemText(txtDesc.getText());
		builder.setArgs(child.getArguments());
		builder.setEval(DSTConstants.BST_TRAVERSAL_KEY);
		
		// Travel through nodes, adding child edges
		int nodeCount = 0;
		for(BasicNode node: canvas.nodes){
			builder.addNode(node.value);
			builder.addEdge(node.getLeftEdge());
			builder.addEdge(node.getRightEdge());
			
			// We're not really interested in absolute values, but rather the
			// position within the canvas.
			xPos[nodeCount] = (int) Math.floor((double)node.getAbsoluteLeft())
					- canvasLeft;
			yPos[nodeCount] = (int) Math.floor((double)node.getAbsoluteTop())
					- canvasTop;
			
			
			nodeCount++;
		}
		
		builder.setPos(xPos, yPos);
		builder.uploadLM();
	}

	@Override
	public void construct() {
		inorderPanel = new TraversalPanel();
		preorderPanel = new TraversalPanel();
		postorderPanel = new TraversalPanel();
		
		// Set up traversalpanels
		inorderPanel.setup("Inorder: ", "Assign Traversal", this);
		inorderPanel.btnTraversal.addClickHandler(new assignClickHandler(inorderPanel.parent, inorderPanel));
		preorderPanel.setup("Preorder: ", "Assign Traversal", this);
		preorderPanel.btnTraversal.addClickHandler(new assignClickHandler(preorderPanel.parent, preorderPanel));
		postorderPanel.setup("Postorder: ","Assign Traversal", this);
		postorderPanel.btnTraversal.addClickHandler(new assignClickHandler(postorderPanel.parent, postorderPanel));
		
		// Add them
		basePanel.add(inorderPanel);
		basePanel.add(preorderPanel);
		basePanel.add(postorderPanel);
	}
	

}
