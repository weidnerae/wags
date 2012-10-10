package webEditor.dst.client;

import java.util.ArrayList;
import com.google.gwt.user.client.ui.Label;

public class TraversalContainer {
	
	private ArrayList<String> traversal;
	private DisplayManager dm;
	
	public TraversalContainer(DisplayManager dm)
	{
		this.dm = dm;
		traversal = new ArrayList<String>();
	}
	
	public void addToTraversal(String s)
	{
		traversal.add(s);	
		updateUI();
	}
	
	public void addToTraversalForceEval(String s)
	{
		addToTraversal(s);
		dm.forceEvaluation();
	}
	
	public void removeFromTraversal(String s)
	{
		if(traversal.contains(s))
			traversal.remove(s);
		updateUI();
	}
	
	private void updateUI()
	{
		Label l = new Label("Current traversal: " + getTraversal());
		l.setStyleName("edge_instructions");
		dm.removeWidgetsFromPanel();                         
		dm.addToPanel(l, 2, DSTConstants.EDGE_PROMPT_Y);
	}
	
	public String getTraversal()
	{
		String theTrav = "";
		for(int i = 0; i < traversal.size(); i++)
		{
			theTrav += traversal.get(i).toString()+" ";
		}
		
		return theTrav;
	}
}
