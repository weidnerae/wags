package microlabs.dst.client;

import java.util.ArrayList;
import microlabs.dst.shared.TraversalContainer;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

public class NodeClickable extends Node
{
	private boolean isSelected;
	private boolean forceEval;
	private TraversalContainer traversal;
	
	public NodeClickable(char value, Label label, TraversalContainer traversal, boolean forceEval)
	{
		super(value, label);
		isSelected = false;
		this.traversal = traversal;
		this.forceEval = forceEval;
		label.addClickHandler(new NodeClickHandler());
	}
	
	private class NodeClickHandler implements ClickHandler
	{
		public void onClick(ClickEvent event)
		{
			if(!isSelected)
			{
				label.setStyleName("selected_node");
				isSelected = true;
				if(forceEval)
					traversal.addToTraversalForceEval(value);
				else
					traversal.addToTraversal(value);
			}
			else
			{
				label.setStyleName("node");
				isSelected = false;
				traversal.removeFromTraversal(value);
			}
		}
	}
	
	public String getTraversal()
	{
		return traversal.getTraversal();
	}
}
