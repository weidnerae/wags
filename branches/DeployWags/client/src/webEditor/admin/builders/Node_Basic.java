package webEditor.admin.builders;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.ui.Label;

public class Node_Basic extends Label{
	static int idCount = 0;
	final static int CLICKED = 1;
	final static int NOT_CLICKED = 0;
	
	int id, state, xPos, yPos;
	boolean clicked = false;
	String value;
	Node_Basic parent, leftChild, rightChild;
	BasicCanvas parentPanel;
	private ArrayList<Edge_Basic> edges = new ArrayList<Edge_Basic>();
	
	public Node_Basic(String value, BasicCanvas parentPanel){
		this.value = value;
		this.parentPanel = parentPanel;
		this.setText(value);
		this.setStyleName("node");
		this.addDoubleClickHandler(new stateClickHandler(this));
		
		id = idCount;
		idCount++;
	}
	
	public void setPosition(int xPos, int yPos){
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public void addEdge(Edge_Basic edge){
		edges.add(edge);
	}
	
	public void removeEdge(Edge_Basic edge){
		edges.remove(edge);
	}
	
	public boolean equals(Node_Basic node){
		return node.id == this.id;
	}
	
	public void setState(int state){
		this.state = state;
		if(state == NOT_CLICKED){
			this.setStyleName("node");
		} else {
			this.setStyleName("selected_node");
		}
	}
	
	public void deleteEdges(){
		for(Edge_Basic edge: edges){
			edge.delete();
		}
	}
	
	public void reDrawEdges(){
		int numEdges = edges.size();
		
		// "Edges" shrinks when each edge is deleted,
		// which is called from redraw
		for(int i = 0; i < numEdges; i++){
			edges.get(0).redraw(parentPanel);
		}
	}
	
	private class stateClickHandler implements DoubleClickHandler{
		Node_Basic node;
		
		public stateClickHandler(Node_Basic node){
			this.node = node;
		}

		public void onDoubleClick(DoubleClickEvent event) {
			node.state = ++node.state % 2;
			setState(node.state);
			
			parentPanel.wasClicked(node);
		}
	}
	
	public void delete(){
		this.deleteEdges();
		this.removeFromParent();
	}
	
	public String getLeftEdge(){
		if(this.leftChild != null){
			return this.value + " " + leftChild.value;
		}
		
		return "";
	}
	
	public String getRightEdge(){
		if(this.rightChild != null){
			return this.value + " " + rightChild.value;
		}
		
		return "";
	}

}
