package webEditor.admin.builders;

import java.util.ArrayList;

import org.vaadin.gwtgraphics.client.DrawingArea;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class BasicCanvas extends Composite {

	private static BasicBuilderUiBinder uiBinder = GWT
			.create(BasicBuilderUiBinder.class);

	interface BasicBuilderUiBinder extends UiBinder<Widget, BasicCanvas> {
	}
	
	@UiField AbsolutePanel canvasPanel;
	boolean firstClick = true;
	final int NODE_WIDTH = 40;
	BasicNode node1;
	BasicDragController dragger;
	ArrayList<BasicNode> nodes = new ArrayList<BasicNode>();
	int nodeX = 10, nodeY = 10;
	DrawingArea canvas;
	BasicDisplay parent;

	// BasicCanvas Constructor
	// Initializes canvas, adds coloring, and registers needed controllers
	public BasicCanvas() {
		initWidget(uiBinder.createAndBindUi(this));
		canvas = new DrawingArea(600, 450); // match logical programming
		canvas.getElement().getStyle().setBackgroundColor("lightcyan");
		canvasPanel.add(canvas);
		dragger =  new BasicDragController(canvasPanel, false, this);
		dragger.registerDropController(new BasicDropController(canvasPanel));
	}
	
	public void setParent(BasicDisplay parent){
		this.parent = parent;
	}
	
	/**
	 * addNode
	 * @param value - The value of the node that is displayed
	 * Adds a node to the BasicCanvas and registers it with the BasicCanvas.
	 * Relies on "positionNode(BasicNode)" to place node
	 * Calls "update()" in case parent LMDisplay must be notified of change
	 */
	public void addNode(String value){
		// For traversals, we're going to force unique nodes
		for(BasicNode node: nodes){
			if(node.value.equals(value)){
				Window.alert("Duplicate nodes not allowed!");
				return;
			}
		}
		
		BasicNode node = new BasicNode(value, this);
		dragger.makeDraggable(node);
		nodes.add(node);
		
		positionNode(node);
		update();
	}
	
	/**
	 * deleteNode
	 * @param value - The value of the node to be deleted
	 * Removes named node (and corresponding edges) from BasicCanvas, if node 
	 * exists.  Calls "update" in case parent LMDisplay needs to be notified
	 * of change.
	 */
	public void deleteNode(String value){
		for(BasicNode node: nodes){
			if(node.value.equals(value)){
				node.deleteEdges();
				node.setVisible(false);
				nodes.remove(node);
			}
		}
		update();
	}
	
	/**
	 * positionNode
	 * @param node - Node to be added
	 * Determines where the new node will be placed on the canvas. 
	 */
	private void positionNode(BasicNode node){
		// May be modified in future
		// Sets position of current node
		node.setPosition(nodeX, nodeY);
		canvasPanel.add(node, node.xPos, node.yPos);
		
		// Finds position for next node
		nodeX += NODE_WIDTH * 1.5;
		if(nodeX + NODE_WIDTH > canvas.getWidth()){
			nodeX = 10;
		}
	}
	
	/**
	 * wasClicked
	 * @param node - The node that was clicked
	 * Handles clicking/declicking nodes and corresponding edge drawing
	 * if necessary.
	 */
	public void wasClicked(BasicNode node){
		if(firstClick){
			node1 = node;
		} else {
			if (!node1.equals(node)){
				addEdge(node1, node);
			}
			
			unClickAll();
		}
		
		firstClick = !firstClick;
	}
	
	private void unClickAll(){
		for(BasicNode node: nodes){
			node.setState(BasicNode.NOT_CLICKED);
		}
	}
	
	/**
	 * getRoot
	 * @return	- The root node for the tree shown on the BasicCanvas
	 * Returns the root node, determined by the node highest on the canvas
	 * If multiple nodes are at the exact same height, returns the first at
	 * that height.
	 */
	public BasicNode getRoot(){
		BasicNode root;
		if(nodes.size() == 0){
			Window.alert("Empty tree!\nReturning null value");
			return null;
		}
		
		// Start with first node as root
		int topHeight = nodes.get(0).getAbsoluteTop();
		root = nodes.get(0);
		
		for(BasicNode node:nodes){
			if(node.getAbsoluteTop() < topHeight){
				topHeight = node.getAbsoluteTop();
				root = node;
			}
		}
		
		return root;
	}
	
	/**
	 * addEdge
	 * @param node1 - An edge is drawn between this and node2
	 * @param node2 - An edge is drawn between this and node1
	 * Delegates creation of the edge to the edge class, and then adds the
	 * resulting edge to the canvas.  Calls "update()" in case parent LMDisplay
	 * needs to be aware of the modification.
	 */
	public void addEdge(BasicNode node1, BasicNode node2){
		BasicEdge edge = new BasicEdge(node1, node2, this);
		if(edge.isValid()){
			canvas.add(edge);
		}
		update();
	}
	
	/**
	 * update
	 * Calls parent.onModify().  Exists so that edges/nodes only interact with
	 * the canvas, and then the canvas can communicate with the LMDisplay.
	 */
	public void update(){
		parent.onModify();
	}
	
	/**
	 * Clear
	 * Removes all nodes (and thus all edges) from the canvas.  Calls update()
	 */
	public void clear(){
		// Ooh, ran into that "removing from an ArrayList changes indices" issue
		while(nodes.size() > 0){
			nodes.get(0).delete();
			nodes.remove(0);
		}
		update();
	}
	
}
