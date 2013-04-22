package webEditor.admin.builders;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.ui.Label;

public class BasicNode extends Label{
	static int idCount = 0;
	final static int CLICKED = 1;
	final static int NOT_CLICKED = 0;
	
	int id, state, xPos, yPos;
	boolean clicked = false;
	String value;
	BasicNode parent, leftChild, rightChild;
	BasicBuilder parentPanel;
	private ArrayList<BasicEdge> edges = new ArrayList<BasicEdge>();
	
	public BasicNode(String value, BasicBuilder parentPanel){
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
	
	public void addEdge(BasicEdge edge){
		edges.add(edge);
	}
	
	public boolean equals(BasicNode node){
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
		for(BasicEdge edge: edges){
			edge.delete();
		}
	}
	
	public void reDrawEdges(){
		BasicNode tmpParent, tmpLeft, tmpRight;
		tmpParent = parent;
		tmpLeft = leftChild;
		tmpRight = rightChild;
		
		deleteEdges();
		
		if(tmpParent != null){
			parentPanel.addEdge(this, tmpParent);
		}
		if(tmpLeft != null){
			parentPanel.addEdge(this, tmpLeft);
		}
		if(tmpRight != null){
			parentPanel.addEdge(this, tmpRight);
		}
	}
	
	private class stateClickHandler implements DoubleClickHandler{
		BasicNode node;
		
		public stateClickHandler(BasicNode node){
			this.node = node;
		}

		public void onDoubleClick(DoubleClickEvent event) {
			node.state = ++node.state % 2;
			setState(node.state);
			
			parentPanel.wasClicked(node);
		}
	}

}
