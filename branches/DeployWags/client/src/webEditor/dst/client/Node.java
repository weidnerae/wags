package webEditor.dst.client;

import com.google.gwt.user.client.ui.Label;

public class Node
{
	protected char value;
	protected Label label;
	
	public Node (char value, Label label)
	{
		this.value = value;
		this.label = label;
	}

	public void setValue(char value)
	{
		this.value = value;
	}
	
	public char getValue()
	{
		return value;
	}

	public Label getLabel()
	{
		return label;
	}
	
	public int getTop()
	{
		return label.getAbsoluteTop();
	}
	
	public int getLeft()
	{
		return label.getAbsoluteLeft();
	}
}	

