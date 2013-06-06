package webEditor.logical;


import org.vaadin.gwtgraphics.client.Line;

import webEditor.logical.TreeProblems.TreeDisplayManager;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;

public class EdgeUndirected extends EdgeParent implements IsSerializable
{
	public void addWeightLabel(){
		// MAGIC, MAGIC EVERYWHERE... MAY GOD HAVE MERCY ON MY SOUL       
		// Why you ask? Because Chrome.
		int width = Math.abs(n1.getLeft()-n2.getLeft());     // getOffset width returns a big fat 0 so we get an offset from the two nodes on the edge.
		int height = Math.abs(n1.getTop()-n2.getTop());      // getOffset height returns a big fat 0 as well so we again get an offset from a two friendly neighborhood nodes.
		int midVert = (((line.getAbsoluteTop()-154)+((line.getAbsoluteTop()-154)+(height-15)))/2+125);              // stuff
		int midHoriz = ((line.getAbsoluteLeft()+(line.getAbsoluteLeft()+width))/2);                                 // moar stuff
		Label l = new Label(weight+"");
		l.setStyleName("edge_weight");
		ec.addWeightLabel(l, midHoriz, midVert, this);
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
		int parentTopOffset = 105; // I do not like these numbers
		int childTopOffset = 115;  // I do not like them.
		int leftOffset = 20;
		int scrollOffset = 0;
		
		/*
		 * This fixes an issue where if the browser is scrolled down at all, 
		 * edges are drawn above where they need to be.
		 */
		if (this.ec.getDisplayManager() instanceof TreeDisplayManager) {
			TreeDisplayManager tdm = (TreeDisplayManager) this.ec.getDisplayManager();
			scrollOffset = tdm.panel.getParent().getElement().getScrollTop();
		}
		
		if(n1.getLabel().getStyleName().equals("mini_node")){
			parentTopOffset = 120;
			leftOffset = 10;
		}
		
		// What is this 30????
		line = new Line(n1.getLeft()+leftOffset, 
						n1.getTop()-parentTopOffset-30+scrollOffset,
						n2.getLeft()+leftOffset, 
						n2.getTop()-childTopOffset-30+scrollOffset);
		if(removable)  
			line.addClickHandler(handler);
		line.setStrokeWidth(3);
		ec.addEdgeToCanvas(line);
	}
}
