package webEditor.magnet.client;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
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
	private ConstructUi constructPanel; //the left hand side of the magnets UI, so that the CS can be placed
										//under the directions content
	
	private Button createButton = new Button("Create"); //the button that will be tasked with creating the desired magnet
	//a series of list boxes that will store the lists from above
	private ListBox structures;
	private ListBox forConditions1;
	private ListBox forConditions2;
	private ListBox forConditions3;
	private ListBox booleanConditions;
	
	//different use cases are represented by panels, each for the type of decision structure
	private HorizontalPanel elsePanel = new HorizontalPanel();
	private HorizontalPanel forPanel = new HorizontalPanel();
	private HorizontalPanel booleanPanel = new HorizontalPanel();
	private HorizontalPanel topAlignPanel = new HorizontalPanel();
	
	final PickupDragController dc;
	
	public CreationStation(String[] structuresList, String[] for1List,String[] for2List,String[] for3List, String[] booleanList, ConstructUi constructPanel, PickupDragController dc){		
		setStyleName("dropdown_panel");
		this.dc = dc;
		this.structuresList=structuresList;
		this.for1List = for1List;
		this.for2List = for2List;
		this.for3List = for3List;
		this.booleanList = booleanList;
		this.constructPanel = constructPanel;
		
		//set up main listbox
		structures = setupStructuresBox(structuresList);
		structures.addChangeHandler(new StructuresHandler());
		
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
	
	
	//takes the string array and turns it into a usable listbox for the decision structures
	public ListBox setupStructuresBox(String[] listOptions){
		if (listOptions == null) {
			return null;
		}
		
		ListBox listBox = new ListBox();
		for (String option : listOptions) {
			if(!checkIfEmpty(option))
				listBox.addItem(option);
		}
		return listBox;
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
	 
	 //actually creates the stackable container specified by the selected listbox options
	 private class CreateHandler implements ClickHandler {
	        public void onClick(ClickEvent event) {
	        	//start with a null SC and add the necessary parts to it as we go
	        	StackableContainer createdContainer = null;
	        	//determine what base structure was chosen and branch based off of that
	        	String selected = structuresList[structures.getSelectedIndex()];
	        	//for each case create a different stackable container and 
	        	//depending on the case grab the needed arguments from the remaining listboxes
	        	if(selected.equals("for")){
	        		createdContainer = new StackableContainer(Consts.FOR,dc);
	        		String containerCondition ="";
	        		containerCondition+=forConditions1.getItemText(forConditions1.getSelectedIndex());
	        		containerCondition+="; " + forConditions2.getItemText(forConditions2.getSelectedIndex());
	        		containerCondition+="; " + forConditions3.getItemText(forConditions3.getSelectedIndex());
	        		createdContainer.addContent(containerCondition, "condition");
	        	}
	        	if(selected.equals("while")){
	        		createdContainer = new StackableContainer(Consts.WHILE, dc);
	        		createdContainer.addContent(booleanConditions.getItemText(booleanConditions.getSelectedIndex()) , "condition");	
	        	}
	        	if(selected.equals("if")){
	        		createdContainer = new StackableContainer(Consts.IF, dc);
	        		createdContainer.addContent(booleanConditions.getItemText(booleanConditions.getSelectedIndex()) , "condition");	
	        	}
	        	if(selected.equals("else if")){
	        		createdContainer = new StackableContainer(Consts.ELSEIF, dc);
	        		createdContainer.addContent(booleanConditions.getItemText(booleanConditions.getSelectedIndex()) , "condition");	
	        	}
	        	if(selected.equals("else"))
	        		createdContainer = new StackableContainer(Consts.ELSE,dc);

	        	
	        	// add that sucker to the construct panel
	        	if(structures.getSelectedIndex()!=0);
	        		constructPanel.addSegment(createdContainer);
	        }
	 }
	 
	 //the handler that determines what "panel" the students see
	 //will change which listboxes are available depending on the index of the structures listbox
	 private class StructuresHandler implements ChangeHandler{
		@Override
		public void onChange(ChangeEvent event) {
			String selected = structuresList[structures.getSelectedIndex()];
        	if(selected.equals("choose structure...")) // Default Text
        		clearDropdowns();
       		if(selected.equals("for"))
        		showForDropdowns();
        	if(selected.equals("while"))
        		showWhileDropdowns();
        	if(selected.equals("if"))
        		showIfDropdowns();
        	if(selected.equals("else"))
        		showElseIfDropdowns();
        	if(selected.equals("else"))
        		showElseDropdowns();
			
		}
	 }
	 
}
