package microlabs.dst.client;


import org.vaadin.gwtgraphics.client.Line;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;

public class EdgeUndirected extends EdgeParent implements IsSerializable
{
	public EdgeUndirected(){super(null,null,null,null,false);}
	
	public EdgeUndirected(Node n1, Node n2, EdgeCollection ec, ClickHandler ch, boolean removable)
	{
		super(n1, n2, ec, ch, removable);
	}
	
	public EdgeUndirected(Node n1, Node n2)
	{
		super(n1, n2, null, null, false);
	}
	
	public void drawEdge()
	{
		line = new Line(n1.getLeft()+20, n1.getTop()-100, 
				n2.getLeft()+20, n2.getTop()-100);
		if(removable)
			line.addClickHandler(handler);
		line.setStrokeWidth(3);
		ec.addEdgeToCanvas(line);
	}
}
