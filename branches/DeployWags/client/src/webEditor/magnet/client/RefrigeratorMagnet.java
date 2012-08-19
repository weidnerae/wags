package webEditor.magnet.client;



import webEditor.client.Proxy;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;


/**
 * EntryPoint class that defines what happens when the website is opened.
 * @author Daniel Cook, Alex Weidner, Reed Phillips
 * 
 * Refrigerator Magnet Microlab
 * CS 3460 - Kurtz
 * @version r32
 */
public class RefrigeratorMagnet extends AbsolutePanel{
	//screen variables
	static int SCREEN_WIDTH = Window.getClientWidth();
	static int SCREEN_HEIGHT = Window.getClientHeight();
	//the panels
    public TabPanel tabPanel = new TabPanel();
	private EditingPanelUi editingPanel;
	private ResultsPanelUi resultsPanel;
	public PickupDragController dc;
	
	
	public RefrigeratorMagnet(String title, String description, StackableContainer mainFunction, StackableContainer[] insideFunctions, String problemType, StackableContainer[] premadeSegments, String[] structuresList, String[] for1List, String[] for2List, String[] for3List, String[] booleanList, String solution, String[] premadeIDs, PickupDragController newDC) {
		dc=newDC;
		setHeight("99%");
		
		setPremadeIDs(premadeIDs, premadeSegments);
		
		
				add(tabPanel);
			    tabPanel.setSize("100%", "100%");
			    int tabPanelHeight = tabPanel.getOffsetHeight();
			    tabPanelHeight = tabPanel.getOffsetHeight();
			    editingPanel = new EditingPanelUi(this, tabPanelHeight,title,description,mainFunction,insideFunctions,problemType,premadeSegments, structuresList,for1List,for2List,for3List,booleanList, solution, premadeIDs, dc);
				tabPanel.add(editingPanel, "Editing Mode", false);
				tabPanel.selectTab(0);

			    resultsPanel = new ResultsPanelUi(tabPanelHeight);
			    tabPanel.add(resultsPanel, "Results", false);
			    
			    Proxy.addProblemCreation(this);
			    editingPanel.start();

	    
	}
	public void addProblemCreation(){
		tabPanel.add(new ProblemCreationPanel(this), "Problem Creation", false);
	}
	public void setPremadeIDs(String[] ids, StackableContainer[] segments){
		for(int i=0;i<ids.length;i++){
			//segments[i].setID(premadeIDs[i]);
		}
	}

}
