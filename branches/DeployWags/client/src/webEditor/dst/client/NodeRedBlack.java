package webEditor.dst.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

public class NodeRedBlack extends Node {
	private boolean isSelected;
	private boolean beingColored;
	private boolean forceEval;
	private boolean color;
	private boolean addingEdges;
	private TraversalContainer traversal;
	private EdgeUndirected edge; // For the weight nodes for Graph Problems
	private EdgeCollection ec;   // For the weight nodes for Graph Problems

	public NodeRedBlack(String value, Label label,
			TraversalContainer traversal, boolean forceEval) {
		super(value, label);
		//label.setStylePrimaryName("red_node");
		isSelected = false;
		beingColored = false;
		this.traversal = traversal;
		this.forceEval = forceEval;
		label.addClickHandler(new NodeClickHandler());
		
	}

	public NodeRedBlack(String value, Label label,
			TraversalContainer traversal, boolean forceEval, EdgeUndirected edge, EdgeCollection ec) {
		super(value, label);
		isSelected = false;
		this.traversal = traversal;
		this.forceEval = forceEval;
		this.edge = edge;
		this.ec = ec;
		addingEdges=false;
		//label.setStylePrimaryName("red_node");
		//label.addClickHandler(new WeightClickHandler());
	}

	private class NodeClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			color = (label.getStyleName().equals(("black_node")));  //color is TRUE if black
			if (!isSelected) { 
				if (addingEdges){
					//Do not recolor and do add nodes to traversal
				}
				else if (beingColored && !color){ //if node is red
					label.setStyleName("black_node"); //change to black
					color=true;
				}
				else if (beingColored && color){ //if node is black
					label.setStyleName("red_node"); //change to red
					color=false;
				}
				else{
					traversal.addToTraversal(value); //if not coloring, add to traversal
					isSelected = true;	//make node selected.
				}
			} else {
				if (addingEdges){
					//Do not recolor and do add nodes to traversal
				}
				else if (beingColored && color){ //if node is black
					label.setStyleName("red_node"); //change to red
					color=false;
				}
				else if (beingColored && !color){ //if node is red
					label.setStyleName("black_node");//change to black
					color = true;
				}
				else{
					isSelected = false; //if not coloring, unselect node
					traversal.removeFromTraversal(value); //remove from traversal list
				}
			}
		}
	}

	/*	 This is not needed for Red-Black Trees
	private class WeightClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			if (!isSelected) {
				label.setStyleName("selected_edge_weight");
				edge.line.setStrokeColor("#FF0000");
				edge.getN1().getLabel().setStyleName("immobilized_node");
				edge.getN2().getLabel().setStyleName("immobilized_node");
				isSelected = true;
				if (forceEval)
					traversal.addToTraversalForceEval(value);
				else
					traversal.addToTraversal(value);
			} else {
				label.setStyleName("edge_weight");
				edge.line.setStrokeColor("#000000");
				edge.getN1().getLabel().setStyleName("node");
				edge.getN2().getLabel().setStyleName("node");

				isSelected = false;
				for(Node n: ec.getGraphNodeCollection().getNodes()){
					if(((NodeRedBlack)n).isSelected){
						((NodeRedBlack)n).edge.getN1().getLabel().setStyleName("immobilized_node");
						((NodeRedBlack)n).edge.getN2().getLabel().setStyleName("immobilized_node");
					}

				}
				traversal.removeFromTraversal(value);
			}

		}
	}
	*/
	
    public Node getThis(){
    	return this;
    }
	public String getTraversal() {
		return traversal.getTraversal();
	}
	
	public boolean getSelected(){
		return isSelected;
	}
	
	public boolean getBeingColored(){
		return beingColored;
	}
	
	public void setAddingEdgesTrue(){
		addingEdges = true;
	}
	public void setAddingEdgesFalse(){
		addingEdges = false;
	}
	
	
	public void setBeingColoredTrue(){
		beingColored = true;
	}
	
	public void setBeingColoredFalse(){
		beingColored = false;
	}
	
	public boolean getAddingEdges(){
		return addingEdges;
	}
}

