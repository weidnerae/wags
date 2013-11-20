package webEditor.admin.builders;

/**
 * This class creates and adds functionality to the interface used to create logical microlabs
 * of the genre "Build BT". In this type of problem students will use a provided inorder and preorder
 * traversal to create a binary tree.
 * 
 * This class extends webEditor.admin.builders.BasicDisplay so examine that class for further information 
 * about the nature of the methods used. 
 *  
 * @author Dakota Murray
 *
 */

public class LMBuildBTDisplay extends BasicDisplay
{
    int traversalType;
	ArgPanel preorderPanel, inorderPanel;
	
	@Override
	public void construct() {
		txtInstructions.setText("Add and connect nodes to form the tree that you would like your"
				+ " students to create. Once completed click the solve button which will automatically"
 				+ " generate the inorder and preorder traversals for the tree. Then simply hit create to"
				+ " submit the problem to the databse. \nThe traversals will automatically be added"
				+ " to the description once you click submit");
		
		preorderPanel = new ArgPanel();
		inorderPanel = new ArgPanel();
		
		// Set up traversalpanels
		preorderPanel.setup("Preorder: ","Create");
		preorderPanel.btnArg.addClickHandler(new AssignClickHandler(this, preorderPanel));
		inorderPanel.setup("Preorder: ", "");
		inorderPanel.setButtonVisible(false);
		
		// Add them to the interface
		basePanel.add(preorderPanel);
		basePanel.add(inorderPanel);
	}

	@Override
	public void calculate() {
		Node_Basic root = canvas.getRoot();
		
		// Gets traversals and formats them for Binary Tree problems
		preorderPanel.fillText(Traversals.getPreorderTraversal(root));
		inorderPanel.fillText(Traversals.getInorderTraversal(root));;
	}

	@Override
	public void fillBuilder(ArgHolder child) {
		Node_Basic root = canvas.getRoot();
		String[] args = new String[2];
		args[0] = Traversals.getPreorderTraversal(root);
		args[1] = Traversals.getInorderTraversal(root);
		builder.setArgs(args);
		
		builder.setTitle(txtTitle.getText());
		String instructions = txtDesc.getText();
		
		//automaticaly add the traversals to the description
		instructions += "\n Preorder Traversal: " + preorderPanel.getText();
		instructions += "\n   Inorder Traversal: " + inorderPanel.getText();	
		builder.setProblemText(instructions);
		
		for(Node_Basic node: canvas.nodes){
			builder.addNode(node.value);
		}
		builder.uploadLM();
	}

	@Override
	public void onModify() {
		preorderPanel.clear();
	}

	@Override
	public void clear() {
		preorderPanel.fillText("");
	}
}
