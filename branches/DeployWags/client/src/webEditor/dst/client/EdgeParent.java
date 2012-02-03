package webEditor.dst.client;


import org.vaadin.gwtgraphics.client.Line;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class EdgeParent implements IsSerializable
{
	protected EdgeCollection ec;
	protected ClickHandler handler;
	protected Line line;
	protected Node n1;
	protected Node n2;
	protected boolean removable;
	
	public EdgeParent(Node n1, Node n2, EdgeCollection ec, ClickHandler handler, boolean removable)
	{
		this.n1 = n1;
		this.n2 = n2;
		this.ec = ec;
		this.handler = handler;
		this.removable = removable;
	}
	
	public Node getN1()
	{
		return n1;
	}
	
	public Node getN2()
	{
		return n2;
	}
	
	public void setN1(Node node)
	{
		this.n1 = node;
	}
	
	public void setN2(Node node)
	{
		this.n2 = node;
	}
	
	public Line getLine()
	{
		return line;
	}
	
	public void redraw()
	{
		ec.removeEdgeFromCanvas(line);
		drawEdge();
	}
	
	public abstract void drawEdge();
	
	
}
