package microlabs.dst.shared;

import java.util.ArrayList;
import com.google.gwt.user.client.ui.Label;
import microlabs.dst.client.DSTConstants;
import microlabs.dst.client.DisplayManager;

public class TraversalContainer {
	
	private ArrayList<Character> traversal;
	private DisplayManager dm;
	
	public TraversalContainer(DisplayManager dm)
	{
		this.dm = dm;
		traversal = new ArrayList<Character>();
	}
	
	public void addToTraversal(Character c)
	{
		traversal.add(c);	
		updateUI();
	}
	
	public void addToTraversalForceEval(Character c)
	{
		addToTraversal(c);
		dm.forceEvaluation();
	}
	
	public void removeFromTraversal(Character c)
	{
		if(traversal.contains(c))
			traversal.remove(c);
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
			theTrav += traversal.get(i).toString();
		}
		
		return theTrav;
	}
}
