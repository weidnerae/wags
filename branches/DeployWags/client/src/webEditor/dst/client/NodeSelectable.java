package webEditor.dst.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

public class NodeSelectable extends Node {
	private boolean isSelected;
	private TraversalContainer traversal;

	public NodeSelectable(String value, Label label, TraversalContainer traversal) {
		super(value, label);
		isSelected = false;
		this.traversal = traversal;
		label.addClickHandler(new NodeClickHandler());
	}

	private class NodeClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			if (label.getStyleName().equals("immobilized_node")) {
				return;
			}
			
			if (!isSelected) {
				traversal.clear();
				
				if (label.getStyleName().equals("red_node")) {
					label.setStyleName("red_selected_node");
				} else {
					label.setStyleName("selected_node");
				}
				
				isSelected = true;
				traversal.addToTraversal(value);
			} else {
				if (label.getStyleName().equals("red_selected_node")) {
					label.setStyleName("red_node");
				} else {
					label.setStyleName("node");
				}
				
				isSelected = false;
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
	
	public void setSelected(boolean b) {
		isSelected = b;
	}
}
