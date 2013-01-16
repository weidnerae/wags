package webEditor.magnet.client;

import java.util.ArrayList;

import webEditor.client.Proxy;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;


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
	public String problemType;
	public String solution;
	public String state;
	public int id;
	public PickupDragController dc;
	public StackableContainer mainFunction;
	public StackableContainer[] insideFunctions;
	public StackableContainer[] premadeFunctions;
	
	
	public RefrigeratorMagnet(int id, String title, String description, StackableContainer mainFunction, StackableContainer[] insideFunctions, String problemType, StackableContainer[] premadeSegments, String[] structuresList, String[] for1List, String[] for2List, String[] for3List, String[] booleanList, String solution, String[] premadeIDs, PickupDragController newDC, final String state) {
		dc=newDC;
		setHeight("99%");
		this.problemType = problemType;
		this.solution = solution;
		this.id = id;
		this.state = state;
		this.mainFunction = mainFunction;
		this.insideFunctions = insideFunctions;
		this.premadeFunctions = premadeSegments;
		
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
	//    Window.alert("after editingpanel starts");
	    if(state!=""){
	        Timer timer = new Timer() {
	            @Override
	            public void run() {
	    	decode2("0",state);
	            }
	        };
	        
	        timer.schedule(1);
	    }
	}
	
	public void addProblemCreation(){
		tabPanel.add(new ProblemCreationPanel(this, false), "Problem Creation", false);
	}
	
	public void addProblemCreation(boolean magnetAdmin){
		tabPanel.add(new ProblemCreationPanel(this, magnetAdmin), "Problem Creation Plus", false);
	}
	
	public String getProblemType(){
		return problemType;
	}
	
	public String getSolution(){
		return solution;
	}
	public int getID(){
		return id;
	}
	public String getState(){
		return state;
	}
	public void decode(String parentID, String state){
//		System.out.println("*"+state);
		int startIndex = state.indexOf("[");
	//	while(braceCount>0){
			int endIndex = state.indexOf("]");
			if(state.indexOf("[",startIndex+1)!=-1 && state.indexOf("[",startIndex+1) < endIndex){   //another open brace before close brace
				addMagnetsByID(state.substring(startIndex+1, state.indexOf("[",startIndex+1)),parentID);
				decode2(state.substring(startIndex+1,state.indexOf("[",startIndex+1)),state.substring(state.indexOf("[",startIndex+1)));
			}
			else{
				addMagnetsByID(state.substring(startIndex+1,endIndex),parentID);
				if(endIndex+1<state.length() && state.indexOf('[',endIndex) != -1)
				decode(parentID,state.substring(state.indexOf('[',endIndex)));
			}
	}
	
	public void decode2(String parentID, String state){
		int[][] segments = findSegments(state);
		
		for(int i=0;i<segments[0].length;i++){
			decode(parentID,state.substring(segments[0][i],segments[1][i]+1));
		}
	}
	
	public int[][] findSegments(String problemState){
		ArrayList<Integer> startList = new ArrayList<Integer>();
		ArrayList<Integer> endList = new ArrayList<Integer>();
		
		int bCount=0;
		for(int i=0;i<problemState.length();i++){
			if(problemState.charAt(i)=='['){
				if(bCount == 0)
					startList.add(i);
				bCount++;
			}
			else if(problemState.charAt(i)==']'){
				bCount--;
				if(bCount == 0)
					endList.add(i);
				
			}
		}
		
			int[] startArray = new int[startList.size()];
			int[] endArray = new int[endList.size()];
			
			for(int i=0;i<startList.size();i++){
				startArray[i] = startList.get(i);
			}
			for(int i=0;i<endList.size();i++){
				endArray[i] = endList.get(i);
			}
			
			return new int[][] {startArray,endArray};
	}
	
	public void addMagnetsByID(String childID, String parentID){
//		Window.alert("adding "+childID + " to "+ parentID);
		boolean isValid = true;
		try{
			int child = Integer.parseInt(childID);
			int parent = Integer.parseInt(parentID);
		} catch(NumberFormatException ex){
			isValid = false;
		}
		if(isValid){
			boolean premadeParent = false;
			int parentIndex = -1;
			int childIndex = -1;
			for(int i=0;i < premadeFunctions.length; i++){
				if(premadeFunctions[i].getID().equals(parentID)){
					premadeParent = true;
					parentIndex = i;
				}
			}
			for(int k = 0; k< insideFunctions.length; k++){
						if(insideFunctions[k].getID().equals(parentID)){
							parentIndex = k;
						}
				}
			for(int j=0;j < premadeFunctions.length; j++){
				if(premadeFunctions[j].getID().equals(childID)){
					childIndex = j;
				}
			}
			
//			Window.alert(childIndex +" : " + parentIndex);
			if(parentID.equals(mainFunction.getID()) && childIndex != -1){
				mainFunction.addInsideContainer(premadeFunctions[childIndex]);
			} else if(childIndex != -1 && parentIndex != -1){
				if(premadeParent){
	//				Window.alert("adding to premade");
					if(!premadeFunctions[parentIndex].hasChild(premadeFunctions[childIndex].getID()))
						premadeFunctions[parentIndex].addInsideContainer(premadeFunctions[childIndex]);
				}else{
	//				Window.alert("adding to inside");
					if(!insideFunctions[parentIndex].hasChild(premadeFunctions[childIndex].getID()))
						insideFunctions[parentIndex].addInsideContainer(premadeFunctions[childIndex]);
				}
				
			}
		}
		
	}

}
