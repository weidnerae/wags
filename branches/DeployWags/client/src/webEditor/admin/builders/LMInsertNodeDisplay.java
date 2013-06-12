package webEditor.admin.builders;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

public class LMInsertNodeDisplay extends BasicDisplay {
	Button btnAssign;
	
	public void construct(){
		btnCalculate.setVisible(false);
		btnAssign = new Button("Assign");
		btnAssign.addClickHandler(new AssignClickHandler(this));

		this.canvas.setNodeHandler(new NH_InsertNode(canvas));
		this.basePanel.add(btnAssign);
	}
	
	private class AssignClickHandler implements ClickHandler{
		BasicDisplay display;
		
		public AssignClickHandler(BasicDisplay display){
			this.display = display;
		}

		@Override
		public void onClick(ClickEvent event) {
			display.fillBuilder(null);
		}
	}
	
	@Override
	public void fillBuilder(ArgHolder child) {
		BasicNode root = canvas.getRoot();
		String[] args = new String[2];
		args[0] = Traversals.getPreorderTraversal(root);
		args[1] = Traversals.getInorderTraversal(root);
		builder.setArgs(args);
		builder.setTitle(txtTitle.getText());
		builder.setProblemText(txtDesc.getText());
		for(BasicNode node: canvas.nodes){
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
