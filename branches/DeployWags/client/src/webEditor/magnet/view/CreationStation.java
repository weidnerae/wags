package webEditor.magnet.view;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The creation station is a panel that consists of a series of dropdowns, 
 * and from those dropdowns the students can select options to create their own
 * custom magnets.
 *
 */

public class CreationStation extends VerticalPanel{
	//structures list is a list of viable decision structures, whether it be for or boolean
	private String[] structuresList;
	private String[] for1List; //the first argument in a for loop
	@SuppressWarnings("unused") // I assume these will be getting used later, magnet people?
	private String[] for2List; //the second argument in a for loop
	@SuppressWarnings("unused")
	private String[] for3List; //the third argument in a for loop
	private String[] booleanList; //the boolean arguments for whiles, ifs, else-ifs, etc
	private int[] limits;
	private ConstructUi constructPanel; //the left hand side of the magnets UI, so that the CS can be placed
										//under the directions content
	
	private Button createButton = new Button("Create"); //the button that will be tasked with creating the desired magnet
	//a series of list boxes that will store the lists from above
	private MenuBar structures;
	private ListBox forConditions1;
	private ListBox forConditions2;
	private ListBox forConditions3;
	private ListBox booleanConditions;
	
	//different use cases are represented by panels, each for the type of decision structure
	private HorizontalPanel elsePanel = new HorizontalPanel();
	private HorizontalPanel forPanel = new HorizontalPanel();
	private HorizontalPanel booleanPanel = new HorizontalPanel();
	private HorizontalPanel topAlignPanel = new HorizontalPanel();
	
	private MenuBar structureOptions;
	private int selectedStructureIndex = 0;
	private Label selectedStructureCounter;
	
	final PickupDragController dc;
	
	private int nextID;
	
	public CreationStation(String[] structuresList, String[] for1List,String[] for2List,String[] for3List, String[] booleanList, int[] limits, ConstructUi constructPanel, PickupDragController dc, int nextID){		
		setStyleName("dropdown_panel");
		this.dc = dc;
		this.structuresList=structuresList;
		this.for1List = for1List;
		this.for2List = for2List;
		this.for3List = for3List;
		this.booleanList = booleanList;
		this.limits = limits;
		this.constructPanel = constructPanel;
		this.nextID = nextID;

		
		
		//set up Structures MenuBar(used as a ListBox but we can set html for the elements)
		structures = new MenuBar(true);
		setupStructures(structures, structuresList);
		selectedStructureCounter = new Label();
		
		//do the leg work of turning string arrays into list boxes
		forConditions1 = setupListBox(for1List);
		forConditions2 = setupListBox(for2List);
		forConditions3 = setupListBox(for3List);
		booleanConditions = setupListBox(booleanList);
		
		//add the topAlignPanel because it contains the structures listbox
		topAlignPanel.add(structures);
		add(topAlignPanel);
		
		//set up panels for each decision structure so they can be ready to swap to
		forPanel.add(new HTML("&nbsp ( &nbsp"));
		forPanel.add(forConditions1);
		forPanel.add(new HTML("&nbsp ; &nbsp"));
		forPanel.add(forConditions2);
		forPanel.add(new HTML("&nbsp ; &nbsp"));
		forPanel.add(forConditions3);
		forPanel.add(new HTML("&nbsp ) &nbsp"));
		
		booleanPanel.addStyleName("boolean_conditions");
		
		booleanPanel.add(new HTML("&nbsp ( &nbsp"));
		booleanPanel.add(booleanConditions);
		booleanPanel.add(new HTML("&nbsp ) &nbsp"));
		
		//finally make the create button and add it to the bottom of the panel
		createButton.addClickHandler(new CreateHandler());	
		createButton.addStyleName("create_button");
		
		add(createButton);
		setCellHorizontalAlignment(createButton, HasHorizontalAlignment.ALIGN_RIGHT);
	}
	
	Command forCommand = new Command() {
	      public void execute() {
	        showForDropdowns();
	        selectedStructureIndex = 1;
			updateStructure();
	      }
	    };
    Command whileCommand = new Command() {
	      public void execute() {
	        showWhileDropdowns();
	        selectedStructureIndex = 2;
			updateStructure();
	      }
	    };
    Command ifCommand = new Command() {
	      public void execute() {
	        showIfDropdowns();
	        selectedStructureIndex = 3;
			updateStructure();
	      }
	    };
    Command elseIfCommand = new Command() {
	      public void execute() {
	        showElseIfDropdowns();
	        selectedStructureIndex = 4;
			updateStructure();
	      }
	    };
    Command elseCommand = new Command() {
	      public void execute() {
	        showElseDropdowns();
	        selectedStructureIndex = 5;
			updateStructure();
	      }
	    };
	    
	public void updateStructureOptions(){
		structureOptions.clearItems();
		String menuItemHTML;
		for(int i=1; i< structuresList.length; i++){
			if(limits[i-1]>0){
				menuItemHTML = "<div><p style = \"margin:0px\">"+structuresList[i]+"<span style = \"float:right;\" class = \"structureLimitAvailable\">"+limits[i-1]+"</span></div>";
			} else{
				menuItemHTML = "<div><p style = \"margin:0px\">"+structuresList[i]+"<span style = \"float:right;\" class = \"structureLimitUnvailable\">"+limits[i-1]+"</span></div>";
			}
			
		    if(structuresList[i].equals("for")){
				structureOptions.addItem(menuItemHTML,true,forCommand);
			} else if(structuresList[i].equals("while")){
				structureOptions.addItem(menuItemHTML,true,whileCommand);
			} else if(structuresList[i].equals("if")){
				structureOptions.addItem(menuItemHTML,true,ifCommand);
			} else if(structuresList[i].equals("else if")){
				structureOptions.addItem(menuItemHTML,true,elseIfCommand);
			} else if(structuresList[i].equals("else")){
				structureOptions.addItem(menuItemHTML,true,elseCommand);
			}
			if(i!=structuresList.length-1){
				structureOptions.addSeparator();
			}
			
			updateStructure();
		}
	}
	
	public void updateStructure(){
		structures.clearItems();
		if(selectedStructureIndex == 0){
			structures.addItem("<div style=\"display:inline-block;min-width:110px;\" >"+structuresList[selectedStructureIndex]+"</div>",true, structureOptions);
			return;
		}else if(limits[selectedStructureIndex-1]>0){
			structures.addItem("<div style=\"display:inline-block;min-width:110px;\" >"+structuresList[selectedStructureIndex]+"<span style = \"float:right;\" class = \"structureLimitAvailable\">"+limits[selectedStructureIndex-1]+"</span></div>",true, structureOptions);
		} else if(limits[selectedStructureIndex-1] <= 0){
			structures.addItem("<div style=\"display:inline-block;min-width:110px;\" >"+structuresList[selectedStructureIndex]+"<span style = \"float:right;\" class = \"structureLimitUnvailable\">"+limits[selectedStructureIndex-1]+"</span></div>",true, structureOptions);	
		}
		
		createButton.setHTML("Create: "+limits[selectedStructureIndex-1] + " left");
	}
	//takes the string array and turns it into a usable listbox for the decision structures
	public void setupStructures(MenuBar structures, String[] structuresList){		
		structureOptions = new MenuBar(true);
		structures.setAnimationEnabled(true);
		
		updateStructureOptions();
	}

	
	//helper method for setupStructuresBox that determines if the list is empty
	public boolean checkIfEmpty(String option){
		if(option.equals("for") && for1List!=null && for1List.length>0)
			return false;
		else if(!option.equals("choose structure...") && booleanList!=null && booleanList.length>0)
			return false;
		else if(option.equals("choose structure..."))
			return false;
		return true;
			
	}
	
	//takes the string array and turns it into a usable listbox for the arguments used in decision structures
	public ListBox setupListBox(String[] listOptions){
		if (listOptions == null) {
			return null;
		}
		
		ListBox listBox = new ListBox();
		for (String option : listOptions) {
			listBox.addItem(option);
		}
		return listBox;
	}

	//utility methods used to change what dropdowns are available to the student using the creation station
	 public void clearDropdowns() {
		 topAlignPanel.clear();
		 topAlignPanel.add(structures);
	 }
	 
	 public void showElseDropdowns() {
		 topAlignPanel.clear();
		 topAlignPanel.add(structures);
		 topAlignPanel.add(elsePanel);

	 }
	 public void showForDropdowns(){
		 topAlignPanel.clear();
		 topAlignPanel.add(structures);
		 topAlignPanel.add(forPanel);
	 }
	 public void showWhileDropdowns(){
		 topAlignPanel.clear();
		 topAlignPanel.add(structures);
		 topAlignPanel.add(booleanPanel);
	 }
	 public void showIfDropdowns(){
		 topAlignPanel.clear();
		 topAlignPanel.add(structures);
		 topAlignPanel.add(booleanPanel);
	 }
	 public void showElseIfDropdowns(){
		 topAlignPanel.clear();
		 topAlignPanel.add(structures);
		 topAlignPanel.add(booleanPanel);
	 }
	 
	 public void incrementLimitCounter(int i){
		 limits[i]++;
	     updateStructureOptions();
	 }
	 
	 //actually creates the stackable container specified by the selected listbox options
	 private class CreateHandler implements ClickHandler {
	        public void onClick(ClickEvent event) {
	        	//start with a null SC and add the necessary parts to it as we go
	        	StackableContainer createdContainer = null;
	        	//determine what base structure was chosen and branch based off of that
	        	String selected = structuresList[selectedStructureIndex];
	        	//for each case create a different stackable container and 
	        	//depending on the case grab the needed arguments from the remaining listboxes
	        	if(selected.equals("for")){
	        		createdContainer = new StackableContainer(Consts.FOR,dc, Consts.STATEMENT);
	        		String containerCondition ="";
	        		containerCondition+=forConditions1.getItemText(forConditions1.getSelectedIndex());
	        		containerCondition+="; " + forConditions2.getItemText(forConditions2.getSelectedIndex());
	        		containerCondition+="; " + forConditions3.getItemText(forConditions3.getSelectedIndex());
	        		createdContainer.addConditionContent(containerCondition);
	        	} else if(limits[0]<=0){
	        		// Have already reach the creation limit for this item
	        	}
	        	if(selected.equals("while")){
	        		createdContainer = new StackableContainer(Consts.WHILE, dc, Consts.STATEMENT);
	        		createdContainer.addConditionContent((booleanConditions.getItemText(booleanConditions.getSelectedIndex())));	
	        	} else if(selected.equals("if")){
	        		createdContainer = new StackableContainer(Consts.IF, dc, Consts.STATEMENT);
	        		createdContainer.addConditionContent(booleanConditions.getItemText(booleanConditions.getSelectedIndex()));	
	        	} else if(selected.equals("else if")){
	        		createdContainer = new StackableContainer(Consts.ELSEIF, dc, Consts.STATEMENT);
	        		createdContainer.addConditionContent(booleanConditions.getItemText(booleanConditions.getSelectedIndex()));	
	        	} else if(selected.equals("else")){
	        		createdContainer = new StackableContainer(Consts.ELSE,dc, Consts.STATEMENT);
	        	}
	        	
		        updateStructureOptions();
	        	
	        	// Set/ increment the ID for the container
	        	createdContainer.setID(""+nextID++);
	        	// add that sucker to the construct panel
	        	if(selectedStructureIndex != 0 && limits[selectedStructureIndex-1]>0){
	        		constructPanel.addSegment(createdContainer);
	        		limits[selectedStructureIndex-1]--;
	        		updateStructureOptions();
	        	} else if(selectedStructureIndex != 0) {
	        		Window.alert("Error: You have reached your limit of "+structuresList[selectedStructureIndex]+"'s ");
	        	}
	        }
	 }
	 
}