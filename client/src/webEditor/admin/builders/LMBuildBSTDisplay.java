package webEditor.admin.builders;

/**
 * This class will create and add functionality to the basic display used to create logical microlabs
 * of the genre "Build BST". In this type of problem the students are given a postorder traversal and 
 * must construct the origional postorder binary search tree.
 * 
 * This class extends webEditor.admin.builders.BasicDisplay so examine that class for further information 
 * about the nature of the methods used. 
 *  
 * @author Dakota Murray
 *
 */

public class LMBuildBSTDisplay extends BasicDisplay
{
	ArgPanel postorderPanel;
	
	@Override
	public void construct() {
		txtInstructions.setText("add and connect nodes in order to create the tree that you want your students"
				+ " to build. Once complete click the solve button which will automatically generate the correct"
				+ " postorder traversal. Then simply click the create button to submit the problem to the database."
				+ " \nThe postorder traversal will automatically be addedto the description upon clicking create.");
		postorderPanel = new ArgPanel();
		
		// Set up traversal panel
		postorderPanel.setup("Postorder: ","Create");
		postorderPanel.btnArg.addClickHandler(new AssignClickHandler(this, postorderPanel));
		
		// Add them to the screen
		baseCol.add(postorderPanel);
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
		args[0] = Traversals.getPostorderTraversal(root);
		args[1] = Traversals.getInorderTraversal(root);
		builder.setArgs(args);
		builder.setTitle(txtTitle.getText());
		String instructions = txtDesc.getText();
		instructions += "\n Postorder Traversal: " + postorderPanel.getText();
		
		builder.setProblemText(instructions);
		
		
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
