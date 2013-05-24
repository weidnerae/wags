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

	public BasicCanvas() {
		initWidget(uiBinder.createAndBindUi(this));
		canvas = new DrawingArea(600, 450); // match logical programming
		canvas.getElement().getStyle().setBackgroundColor("lightcyan");
		canvasPanel.add(canvas);
		dragger =  new BasicDragController(canvasPanel, false, this);
		dragger.registerDropController(new BasicDropController(canvasPanel));
	}
	
	public void addNode(String value){
		BasicNode node = new BasicNode(value, this);
		dragger.makeDraggable(node);
		nodes.add(node);
		
		positionNode(node);
	}
	
	public void deleteNode(String value){
		for(BasicNode node: nodes){
			if(node.value.equals(value)){
				node.deleteEdges();
				node.setVisible(false);
				nodes.remove(node);
			}
		}
	}
	
	public void positionNode(BasicNode node){
		// Have to make this real at some point
		node.setPosition(nodeX, nodeY);
		canvasPanel.add(node, node.xPos, node.yPos);
		
		// Find position for next node
		nodeX += NODE_WIDTH * 1.5;
		if(nodeX + NODE_WIDTH> canvas.getWidth()){
			nodeX = 10;
		}
	}
	
	
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
	
	public void addEdge(BasicNode node1, BasicNode node2){
		BasicEdge edge = new BasicEdge(node1, node2);
		if(edge.isValid()){
			canvas.add(edge);
		}
	}
	
	public void clear(){
		// Ooh, ran into that "removing from an ArrayList changes indices" issue
		while(nodes.size() > 0){
			nodes.get(0).delete();
			nodes.remove(0);
		}
	}
	
}
