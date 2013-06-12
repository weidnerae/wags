package webEditor.admin.builders;

import java.util.ArrayList;

import org.vaadin.gwtgraphics.client.DrawingArea;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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

	BasicNode node1;
	BasicDragController dragger;
	ArrayList<BasicNode> nodes = new ArrayList<BasicNode>();
	DrawingArea canvas;
	BasicDisplay parent;
	NodeHandler nodeHandler;
	boolean autoPos;

	// BasicCanvas Constructor
	// Initializes canvas, adds coloring, and registers needed controllers
	public BasicCanvas() {
		initWidget(uiBinder.createAndBindUi(this));
		canvas = new DrawingArea(600, 450); // match logical programming
		canvas.getElement().getStyle().setBackgroundColor("lightcyan");
		canvasPanel.add(canvas);
		dragger =  new BasicDragController(canvasPanel, false, this);
		dragger.registerDropController(new BasicDropController(canvasPanel));
		nodeHandler = new NH_Traversal(this);
	}
	
	public void setParent(BasicDisplay parent){
		this.parent = parent;
	}
	
	public void setNodeHandler(NodeHandler nh){
		this.nodeHandler = nh;
	}
	
	public void addNode(String value){
		nodeHandler.addNode(value);
	}
	
	public void deleteNode(String value){
		nodeHandler.deleteNode(value);
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
		
		nodeHandler.clear();
		update();
	}
	
}
