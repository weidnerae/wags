package webEditor.dst.client;

import java.util.ArrayList;
import java.util.NoSuchElementException;


import com.allen_sauer.gwt.dnd.client.DragController;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;

public class NodeCollection implements IsSerializable
{	
	private ArrayList<Node> nodes;
	
	public NodeCollection()
	{
		nodes = new ArrayList<Node>();
	}
	
	public void addNode(Node n)
	{
		nodes.add(n);
	}
	
	public ArrayList<Node> getNodes()
	{
		return nodes;
	}
	
	public Node getNode(int index)
	{
		if(index >= 0 && index < nodes.size())
		{
			return nodes.get(index);
		}
		else
			throw new IndexOutOfBoundsException();
	}
	
	public Node getNodeByLabel(Label l)
	{
		for(int i = 0; i < nodes.size(); i++)
		{
			if(nodes.get(i).getLabel() == l)
			{
				return nodes.get(i);
			}
		}
		throw new NoSuchElementException();
	}
	
	public void resetNodeStyles(String nodeType)
	{
		for(int i = 0; i < nodes.size(); i++)
		{
			if(nodeType.equals(DSTConstants.NODE_STRING_DRAGGABLE)){
				nodes.get(i).getLabel().setStyleName("string_node");			
			}
			else{
				nodes.get(i).getLabel().setStyleName("node");
			}
		}
	}
			
	public void makeNodesDraggable(DragController dc)
	{
		for(int i = 0; i < nodes.size(); i++)
		{
			dc.makeDraggable(nodes.get(i).getLabel());
		}
	}
	
	public void makeNodesNotDraggable(DragController dc)
	{
		try
		{
			for(int i = 0; i < nodes.size(); i++)
			{
				dc.makeNotDraggable(nodes.get(i).getLabel());
			}
		}
		catch(Exception e)
		{
			System.out.println("Still ok");
		}
	}
	
	public void emptyNodes()
	{
		nodes.clear();
	}
}
