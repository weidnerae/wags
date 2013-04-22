package webEditor.admin.builders;

import java.util.ArrayList;

import org.vaadin.gwtgraphics.client.DrawingArea;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class BasicBuilder extends Composite {

	private static BasicBuilderUiBinder uiBinder = GWT
			.create(BasicBuilderUiBinder.class);

	interface BasicBuilderUiBinder extends UiBinder<Widget, BasicBuilder> {
	}
	
	@UiField AbsolutePanel canvasPanel;
	boolean firstClick = true;
	final int NODE_WIDTH = 40;
	BasicNode node1;
	BasicDragController dragger;
	ArrayList<BasicNode> nodes = new ArrayList<BasicNode>();
	int nodeX = 10, nodeY = 10;
	DrawingArea canvas;

	public BasicBuilder() {
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
	
	public void addEdge(BasicNode node1, BasicNode node2){
		BasicEdge edge = new BasicEdge(node1, node2);
		if(edge.isValid()){
			canvas.add(edge);
		}
		
		/*String y = "Node 2 is higher!";
		String x = "Node 2 is to the left!";
		
		if(node1.yPos < node2.yPos){
			y = "Node 1 is higher!";
		}
		
		if(node1.xPos < node2.xPos){
			x = "Node 1 is to the left!";
		}
		
		Line line = new Line(node1.xPos, node1.yPos, node2.xPos, node2.yPos);
		
		canvas.add(line);
		Window.alert(y + "\n" + x);*/
	}
	
}
