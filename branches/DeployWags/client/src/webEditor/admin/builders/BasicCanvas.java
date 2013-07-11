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

	Node_Basic node1;
	BasicDragController dragger;
	ArrayList<Node_Basic> nodes = new ArrayList<Node_Basic>();
	ArrayList<PseudoEdge> edges = new ArrayList<PseudoEdge>();
	DrawingArea canvas;
	BasicDisplay parent;
	NodeHandler nodeHandler;
	EdgeHandler edgeHandler;
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
		
		nodeHandler = new NH_AlongTop(this);
		edgeHandler = new EH_BinaryTree(this);
	}
	
	public void setParent(BasicDisplay parent){
		this.parent = parent;
	}
	
	public void setNodeHandler(NodeHandler nh){
		this.nodeHandler = nh;
	}
	
	public void setEdgeHandler(EdgeHandler eh){
		this.edgeHandler = eh;
	}
	
	public void addNode(String value){
		nodeHandler.addNode(value);
	}
	
	public void deleteNode(String value){
		nodeHandler.deleteNode(value);
	}
	
	public void deleteEdge(Node_Basic n1, Node_Basic n2){
		for(PseudoEdge edge: edges){
			if (edge.matches(n1, n2)){
				edges.remove(edge);
			}
		}
	}

	/**
	 * wasClicked
	 * @param node - The node that was clicked
	 * Handles clicking/declicking nodes and corresponding edge drawing
	 * if necessary.
	 */
	public void wasClicked(Node_Basic node){
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
		for(Node_Basic node: nodes){
			node.setState(Node_Basic.NOT_CLICKED);
		}
	}
	
	/**
	 * getRoot
	 * @return	- The root node for the tree shown on the BasicCanvas
	 * Returns the root node, determined by the node highest on the canvas
	 * If multiple nodes are at the exact same height, returns the first at
	 * that height.
	 */
	public Node_Basic getRoot(){
		Node_Basic root;
		if(nodes.size() == 0){
			return null;
		}
		
		// Start with first node as root
		int topHeight = nodes.get(0).getAbsoluteTop();
		root = nodes.get(0);
		
		for(Node_Basic node:nodes){
			if(node.getAbsoluteTop() < topHeight){
				topHeight = node.getAbsoluteTop();
				root = node;
			}
		}
		
		return root;
	}
	
	/**
	 * addEdge
	 * @param node1 - Node edge is drawn between
	 * @param node2 - Other node edge is drawn between
	 * @return Whether or not the edge was added successfully or not
	 * Like addNode and deleteNode, this method delegates the actual
	 * adding of the edge to another class.  This is a workaround for
	 * the limitations of UiBinder (as I understand it).
	 */
	public boolean addEdge(Node_Basic node1, Node_Basic node2){
		// For now, you can't have multiple edges between nodes
		PseudoEdge newEdge = new PseudoEdge(node1, node2);
		for(PseudoEdge edge: edges){
			if(edge.matches(newEdge)){
				Window.alert("Cannot have multiple edges between paired nodes");
				return false;
			}
		}
		
		if(!(edgeHandler instanceof EH_NoEdges)) edges.add(newEdge);
		
		return edgeHandler.addEdge(node1, node2);
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
		edgeHandler.clear();
		
		update();
	}
	
	private class PseudoEdge{
		private Node_Basic n1, n2;
		
		public PseudoEdge(Node_Basic n1, Node_Basic n2){
			this.n1 = n1;
			this.n2 = n2;
		}
		
		public boolean matches(PseudoEdge edge){
			return ((n1 == edge.n1 && n2 == edge.n2) ||
					(n1 == edge.n2 && n2 == edge.n1));
		}
		
		public boolean matches(Node_Basic node1, Node_Basic node2){
			return ((n1 == node1 && n2 == node2) || (n1 == node2 && n2 == node1));
		}
	}
	
}
