package webEditor.magnet.client;



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
public class RefrigeratorMagnet{
	//screen variables
	static int SCREEN_WIDTH = Window.getClientWidth();
	static int SCREEN_HEIGHT = Window.getClientHeight();
	//the panels
    static TabPanel tabPanel = new TabPanel();
	private EditingPanelUi editingPanel;
	private ResultsPanelUi resultsPanel;
	static PickupDragController dc;
	
	
	public RefrigeratorMagnet(String title, String description, StackableContainer mainFunction, StackableContainer[] insideFunctions, String problemType, StackableContainer[] premadeSegments, String[] structuresList, String[] for1List, String[] for2List, String[] for3List, String[] booleanList, String solution, String[] premadeIDs, PickupDragController dc) {
		this.dc=dc;
		
		setPremadeIDs(premadeIDs, premadeSegments);
		RootPanel rootPanel = RootPanel.get();
		rootPanel.setHeight("99%");
		rootPanel.setWidth("99%");
		
	    rootPanel.add(tabPanel);
	    tabPanel.setSize("98%", "96%");
	    int tabPanelHeight = tabPanel.getOffsetHeight();
	    editingPanel = new EditingPanelUi(tabPanelHeight,title,description,mainFunction,insideFunctions,problemType,premadeSegments, structuresList,for1List,for2List,for3List,booleanList, solution, premadeIDs, dc);
		tabPanel.add(editingPanel, "Editing Mode", false);
		tabPanel.selectTab(0);

	    resultsPanel = new ResultsPanelUi(tabPanelHeight);
	    tabPanel.add(resultsPanel, "Results", false);
	    
	    tabPanel.add(new ProblemCreationPanel(tabPanel, this.dc), "Problem Creation", false);
	    
	    editingPanel.start();
	}
	public void setPremadeIDs(String[] ids, StackableContainer[] segments){
		for(int i=0;i<ids.length;i++){
			//segments[i].setID(premadeIDs[i]);
		}
	}

	 static void switchTabs(int tab){
		 tabPanel.selectTab(tab);
	 }

	 
	 public static PickupDragController getDragController(){
		 return dc;
	 }


}
