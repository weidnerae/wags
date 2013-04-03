package webEditor.logical;

import java.util.ArrayList;


import com.google.gwt.user.client.ui.Label;

public class TraversalContainer {
	
	private ArrayList<String> traversal;
	private DisplayManager dm;
	private boolean hidden = false;
	
	public TraversalContainer(DisplayManager dm) {
		this.dm = dm;
		traversal = new ArrayList<String>();
	}
	
	public void addToTraversal(String s) {
		traversal.add(s);	
		updateUI();
	}
	
	public void addToTraversalForceEval(String s) {
		addToTraversal(s);
		dm.forceEvaluation();
	}
	
	public void removeFromTraversal(String s) {
		if(traversal.contains(s))
			traversal.remove(s);
		updateUI();
	}
	
	private void updateUI() {
		if (!hidden) {
			Label l = new Label("Current traversal: " + getTraversal());
			l.setStyleName("edge_instructions");
			dm.removeWidgetsFromPanel();                         
			dm.addToPanel(l, 2, DSTConstants.EDGE_PROMPT_Y);
		}
	}
	
	public String getTraversal() {
		String theTrav = "";
		for (int i = 0; i < traversal.size(); i++) {
			theTrav += traversal.get(i).toString()+" ";
		}
		
		return theTrav;
	}
	
	/**
	 * 
	 * Currently using this with Selection Sort so that I can have only 
	 * one Node selected at a time.
	 * 
	 */
	public void clear() {
		traversal.clear();
		for (Node n : dm.getNodes()) {
			if (n instanceof NodeSelectable) {
				((NodeSelectable) n).setSelected(false);
				
				if (n.getLabel().getStyleName().equals("selected_node")) {
					n.getLabel().setStyleName("node");
				} else if (n.getLabel().getStyleName().equals("red_selected_node")) {
					n.getLabel().setStyleName("red_node");
				}
			}
		}
	}
	
	public void setHidden(boolean b) {
		hidden = b;
	}
}
