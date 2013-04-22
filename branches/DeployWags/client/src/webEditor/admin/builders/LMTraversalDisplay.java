package webEditor.admin.builders;


import webEditor.admin.LMDisplay;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LMTraversalDisplay extends LMDisplay {

	private static LMTraversalDisplayUiBinder uiBinder = GWT
			.create(LMTraversalDisplayUiBinder.class);

	interface LMTraversalDisplayUiBinder extends
			UiBinder<Widget, LMTraversalDisplay> {
	}

	LMBuilder builder;
	@UiField VerticalPanel basePanel;
	@UiField BasicBuilder canvas;
	@UiField Button btnAddNode, btnDeleteNode;
	@UiField TextBox txtAddNode;
	@UiField TraversalPanel inorderPanel, preorderPanel, postorderPanel;
	
	public LMTraversalDisplay() {
		initWidget(uiBinder.createAndBindUi(this));
		addNodeHandling();
		deleteNodeHandling();
		inorderPanel.setup("Inorder: ", "Assign Traversal");
		preorderPanel.setup("Preorder: ", "Assign Traversal");
		postorderPanel.setup("Postorder: ","Assign Traversal");
	}
	
	private void addNodeHandling(){
		// Add nodes
		btnAddNode.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String val = txtAddNode.getText();
				if(val.length() > 0){
					canvas.addNode(txtAddNode.getText());
					txtAddNode.setText("");
				}
			}
		});		
	}
	
	private void deleteNodeHandling(){
		btnDeleteNode.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				canvas.deleteNode(txtAddNode.getText());
			}
		});
	}
	
	public void setBuilder(LMBuilder builder){
		this.builder = builder;
	}
	

}
