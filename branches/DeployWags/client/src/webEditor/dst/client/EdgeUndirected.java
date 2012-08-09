package webEditor.dst.client;


import org.vaadin.gwtgraphics.client.Line;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;

public class EdgeUndirected extends EdgeParent implements IsSerializable
{
	public void addWeightLabel(){
		int midVert = (line.getY1() + line.getY2())/2+120;
		int midHoriz = (line.getX1() + line.getX2())/2;
		Label l = new Label(weight+"");
		l.setStyleName("edge_weight");
		ec.addLabel(l, midHoriz, midVert, this);
	}
	public EdgeUndirected(){super(null,null,null,null,false);}
	
	public EdgeUndirected(Node n1, Node n2, EdgeCollection ec, ClickHandler ch, boolean removable)
	{
		super(n1, n2, ec, ch, removable);
	}
	
	public EdgeUndirected(Node n1, Node n2)
	{
		super(n1, n2, null, null, false);
	}
	public EdgeUndirected(Node n1, Node n2, EdgeCollection ec, ClickHandler ch, boolean removable, int weight)
	{
		super(n1, n2, ec, ch, removable, weight);
	}
	
	public void drawEdge()
	{
		int parentTopOffset = 105;
		int childTopOffset = 120;
		int leftOffset = 20;
		if(n1.getLabel().getStyleName().equals("mini_node")){
			parentTopOffset = 120;
			leftOffset = 10;
		}
		line = new Line(n1.getLeft()+leftOffset, n1.getTop()-parentTopOffset-30,   //the n1.getTop()-105 used to be n1.getTop()-100   added -70
				n2.getLeft()+leftOffset, n2.getTop()-childTopOffset-30);           //n2.getTop()-120 used to be n2.getTop()-100    added -70
		if(removable)  
			line.addClickHandler(handler);
		line.setStrokeWidth(3);
		ec.addEdgeToCanvas(line);
	}
}
