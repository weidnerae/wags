package wags.logical;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

public class NodeClickable extends Node {
	private boolean isSelected;
	private boolean forceEval;
	private TraversalContainer traversal;
	private EdgeUndirected edge; // For the weight nodes for Graph Problems
	private EdgeCollection ec;   // For the weight nodes for Graph Problems

	public NodeClickable(String value, Label label,
			TraversalContainer traversal, boolean forceEval) {
		super(value, label);
		isSelected = false;
		this.traversal = traversal;
		this.forceEval = forceEval;
		label.addClickHandler(new NodeClickHandler());
	}

	public NodeClickable(String value, Label label,
			TraversalContainer traversal, boolean forceEval, EdgeUndirected edge, EdgeCollection ec) {
		super(value, label);
		isSelected = false;
		this.traversal = traversal;
		this.forceEval = forceEval;
		this.edge = edge;
		this.ec = ec;
		label.addClickHandler(new WeightClickHandler());
	}

	private class NodeClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			if (!isSelected) {
				label.setStyleName("selected_node");
				isSelected = true;
				if (forceEval)
					traversal.addToTraversalForceEval(value);
				else
					traversal.addToTraversal(value);
			} else {
				label.setStyleName("node");
				isSelected = false;
				traversal.removeFromTraversal(value);
			}

		}
	}

	private class WeightClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			if (!isSelected) {
				label.setStyleName("selected_edge_weight");
				edge.line.setStrokeColor("#27F500");
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
					if(((NodeClickable)n).isSelected){
						((NodeClickable)n).edge.getN1().getLabel().setStyleName("immobilized_node");
						((NodeClickable)n).edge.getN2().getLabel().setStyleName("immobilized_node");
					}

				}
				traversal.removeFromTraversal(value);
			}

		}
	}
    public Node getThis(){
    	return this;
    }
	public String getTraversal() {
		return traversal.getTraversal();
	}
}
