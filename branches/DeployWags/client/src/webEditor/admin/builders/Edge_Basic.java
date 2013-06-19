package webEditor.admin.builders;

import org.vaadin.gwtgraphics.client.Line;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public abstract class Edge_Basic extends Line {
	private final static int NODE_HALF = 20;
	private final int LINE_WIDTH = 10;

	protected Node_Basic n1, n2;
	protected BasicCanvas canvas;
	protected String asString;

	public Edge_Basic(int x1, int y1, int x2, int y2) {
		super(x1, y1, x2, y2);
	}
	
	// Takes two nodes, creates a line to connect them,
	// and assigns the relationship between them
	public Edge_Basic(Node_Basic n1, Node_Basic n2, BasicCanvas canvas){
		super(n1.xPos + NODE_HALF, n1.yPos + NODE_HALF, 
				n2.xPos + NODE_HALF, n2.yPos + NODE_HALF);
		
		this.canvas = canvas;
		
		this.n1 = n1;
		this.n2 = n2;
		this.setStrokeWidth(LINE_WIDTH);
		this.addClickHandler(new edgeRemoveClick(this));
	}

	
	public void delete(){
		this.removeFromParent();
		this.setVisible(false);
		this.onDelete();
		
		canvas.update();
	}
	
	protected abstract void onDelete();
	
	// When an edge gets clicked, it gets removed
	private class edgeRemoveClick implements ClickHandler{
		Edge_Basic edge;
		
		public edgeRemoveClick(Edge_Basic edge){
			this.edge = edge;
		}
		
		// Edges get removed whenever they are clicked
		public void onClick(ClickEvent event) {
			edge.delete();
		}
		
	}

	
	public String toString(){
		return asString;
	}

}
