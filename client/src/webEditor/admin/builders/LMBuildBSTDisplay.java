package webEditor.admin.builders;


import webEditor.logical.DSTConstants;

public class LMBuildBSTDisplay extends BasicDisplay
{
    int traversalType;
	ArgPanel postorderPanel;
	
	@Override
	public void construct() {
		txtInstructions.setText("add and connect nodes in order to create the tree that you want your students"
				+ "to build. Once complete click the solve button which will automatically generate the correct"
				+ "postorder traversal. Then simply click the create button beside the generated traversal to"
				+ "submit the problem to the database");
		postorderPanel = new ArgPanel();
		
		// Set up traversalpanels
		postorderPanel.setup("Postorder: ","Create");
		postorderPanel.btnArg.addClickHandler(new AssignClickHandler(this, postorderPanel));
		
		// Add them
		basePanel.add(postorderPanel);
	}

	@Override
	public void calculate() {
		Node_Basic root = canvas.getRoot();
		
		// Gets traversals and formats them for Binary Tree problems
		postorderPanel.fillText(Traversals.getPostorderTraversal(root));
	}

	@Override
	public void fillBuilder(ArgHolder child) {
		Node_Basic root = canvas.getRoot();
		String[] args = new String[2];
		args[1] = Traversals.getInorderTraversal(root);
		
		builder.setTitle(txtTitle.getText());
		String instructions = txtDesc.getText();
		instructions += "\n Postorder Traversal: " + postorderPanel.getText();
		builder.setEval(DSTConstants.BST_POSTORDER_KEY);	
		args[0] = Traversals.getPostorderTraversal(root);
		builder.setProblemText(instructions);
		builder.setArgs(args);
		
		for(Node_Basic node: canvas.nodes){
			builder.addNode(node.value);
		}
		builder.uploadLM();
	}

	@Override
	public void onModify() {
		postorderPanel.clear();
	}

	@Override
	public void clear() {
		postorderPanel.fillText("");
	}
}
