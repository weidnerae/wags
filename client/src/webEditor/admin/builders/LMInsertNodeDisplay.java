package webEditor.admin.builders;

import com.google.gwt.user.client.ui.Button;

public class LMInsertNodeDisplay extends BasicDisplay {
	Button btnAssign;
	
	public void construct(){
		btnCalculate.setVisible(false);
		btnAssign = new Button("Assign");
		btnAssign.addClickHandler(new AssignClickHandler(this));
		
		txtInstructions.setText("Add nodes in the order students would add" +
				" them.  The resulting tree will automatically be built." + 
				" removing a node rebuilds the tree in the same sequence," + 
				" omitting the removed node.  When satisfied with the tree" +
				" click 'Assign' to assign the problem");

		this.canvas.setNodeHandler(new NH_InsertNode(canvas));
		this.baseCol.add(btnAssign);
	}
	

	
	@Override
	public void fillBuilder(ArgHolder child) {
		Node_Basic root = canvas.getRoot();
		String[] args = new String[2];
		args[0] = Traversals.getPreorderTraversal(root);
		args[1] = Traversals.getInorderTraversal(root);
		builder.setArgs(args);
		builder.setTitle(txtTitle.getText());
		builder.setProblemText(txtDesc.getText());
		for(Node_Basic node: canvas.nodes){
			builder.addNode(node.value);
		}
		
		// debugging null positions, edges, etc
		builder.uploadLM();
	}

	@Override
	public void onModify() {
		// Traversal only gets calculated immediately prior to assignment,
		// so onModify is not needed
	}

	@Override
	public void clear() {
		// Nothing is added other than the "Assign" button, so nothing needs
		// to be cleared
	}

	@Override
	public void calculate() {
		// Never gets called as button is hidden
	}

}
