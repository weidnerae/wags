package webEditor.admin.builders;


import webEditor.admin.LMDisplay;
import webEditor.logical.DSTConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
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
	@UiField BasicCanvas canvas;
	@UiField Button btnAddNode, btnDeleteNode;
	@UiField TextBox txtAddNode, txtTitle;
	@UiField TextArea txtDesc;
	@UiField TraversalPanel inorderPanel, preorderPanel, postorderPanel;
	
	public LMTraversalDisplay() {
		initWidget(uiBinder.createAndBindUi(this));
		addNodeHandling();
		deleteNodeHandling();
		
		// Set up traversalpanels
		inorderPanel.setup("Inorder: ", "Assign Traversal", this);
		inorderPanel.btnTraversal.addClickHandler(new assignClickHandler(inorderPanel.parent, inorderPanel));
		preorderPanel.setup("Preorder: ", "Assign Traversal", this);
		preorderPanel.btnTraversal.addClickHandler(new assignClickHandler(preorderPanel.parent, preorderPanel));
		postorderPanel.setup("Postorder: ","Assign Traversal", this);
		postorderPanel.btnTraversal.addClickHandler(new assignClickHandler(postorderPanel.parent, postorderPanel));
	}
	
	@UiHandler("txtAddNode")
	void onEnterPress(KeyPressEvent event){
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)
		{
			btnAddNode.click();
		}
	}
	
	@UiHandler("btnReset")
	void onResetClick(ClickEvent event){
		canvas.clear();
		preorderPanel.fillText("");
		inorderPanel.fillText("");
		postorderPanel.fillText("");
		txtTitle.setText("");
		txtDesc.setText("");
	}
	
	@UiHandler("btnGetTraversals")
	void onTraversalClick(ClickEvent event){
		BasicNode root = canvas.getRoot();
		
		preorderPanel.fillText(preorder(root));
		inorderPanel.fillText(inorder(root));
		postorderPanel.fillText(postorder(root));
	}
	
	private String preorder(BasicNode tree){
		String traversal = "";
		if(tree != null){
			traversal += tree.value;
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
	
	private class assignClickHandler implements ClickHandler{
		LMDisplay parent;
		TraversalPanel child;
		
		public assignClickHandler(LMDisplay parent, TraversalPanel child){
			this.parent = parent;
			this.child = child;
		}
		@Override
		public void onClick(ClickEvent event) {
			parent.fillBuilder(child);
		}
		
	}

	@Override
	public void fillBuilder(ArgHolder child) {
		// Ehh... how to handle unattached nodes
		int[] xPos = new int[canvas.nodes.size()];
		int[] yPos = new int[canvas.nodes.size()];
		String edgeList = "";
		
		// Has to validate fields...
		
		builder.setTitle(txtTitle.getText());
		builder.setProblemText(txtDesc.getText());
		builder.setArgs(child.getArguments());
		builder.setEval(DSTConstants.BST_TRAVERSAL_KEY);
		
		// Travel through nodes, adding child edges
		int nodeCount = 0;
		for(BasicNode node: canvas.nodes){
			builder.addNode(node.value);
			builder.addEdge(node.getLeftEdge());
			edgeList += node.getLeftEdge() + ",";
			builder.addEdge(node.getRightEdge());
			edgeList += node.getRightEdge() + ",";
			xPos[nodeCount] = node.getAbsoluteLeft();
			yPos[nodeCount] = node.getAbsoluteTop();
		}
		
		// Debugging
		Window.alert(edgeList);
	}
	

}
