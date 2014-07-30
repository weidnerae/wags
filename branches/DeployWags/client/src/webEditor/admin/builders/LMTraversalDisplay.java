package webEditor.admin.builders;


import webEditor.logical.DSTConstants;

public class LMTraversalDisplay extends BasicDisplay {
	ArgPanel inorderPanel, preorderPanel, postorderPanel;
	// For when nodes/edges are added/removed
	public void onModify(){
		inorderPanel.clear();
		preorderPanel.clear();
		postorderPanel.clear();
	}
	
	public void calculate(){
		Node_Basic root = canvas.getRoot();
		
		// Gets traversals and formats them for Binary Tree problems
		preorderPanel.fillText(Traversals.getPreorderTraversal(root));
		inorderPanel.fillText(Traversals.getInorderTraversal(root));
		postorderPanel.fillText(Traversals.getPostorderTraversal(root));
	}
	
	public void clear(){
		preorderPanel.fillText("");
		inorderPanel.fillText("");
		postorderPanel.fillText("");
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
		for(Node_Basic node: canvas.nodes){
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
		inorderPanel = new ArgPanel();
		preorderPanel = new ArgPanel();
		postorderPanel = new ArgPanel();
		
		// Set up traversalpanels
		inorderPanel.setup("Inorder: ", "Assign Traversal");
		inorderPanel.btnArg.addClickHandler(new AssignClickHandler(this, inorderPanel));
		preorderPanel.setup("Preorder: ", "Assign Traversal");
		preorderPanel.btnArg.addClickHandler(new AssignClickHandler(this, preorderPanel));
		postorderPanel.setup("Postorder: ","Assign Traversal");
		postorderPanel.btnArg.addClickHandler(new AssignClickHandler(this, postorderPanel));
		
		// Add them
		baseCol.add(inorderPanel);
		baseCol.add(preorderPanel);
		baseCol.add(postorderPanel);
	}
	

}
