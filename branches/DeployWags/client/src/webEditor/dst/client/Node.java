package webEditor.dst.client;

import com.google.gwt.user.client.ui.Label;

public class Node {
	protected String value;
	protected Label label;
	protected boolean visited = false;
	
	public Node (String value, Label label) {
		this.value = value;
		this.label = label;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	public Label getLabel() {
		return label;
	}
	
	public int getTop() {
		return label.getAbsoluteTop();
	}
	
	public int getLeft() {
		return label.getAbsoluteLeft();
	}
	
	public void setVisited(boolean v) {
		visited = v;
	}
	public boolean getVisited() {
		return visited;
	}
}	

