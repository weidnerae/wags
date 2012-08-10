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


public class CreationStation extends VerticalPanel{
	private String[] structuresList;
	private String[] for1List;
	@SuppressWarnings("unused") // I assume these will be getting used later, magnet people?
	private String[] for2List;
	@SuppressWarnings("unused")
	private String[] for3List;
	private String[] booleanList;
	private ConstructUi constructPanel;
	
	private Button createButton = new Button("Create");
	private ListBox structures;
	private ListBox forConditions1;
	private ListBox forConditions2;
	private ListBox forConditions3;
	private ListBox booleanConditions;
	
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
		
		structures = setupStructuresBox(structuresList);
		structures.addChangeHandler(new StructuresHandler());
		
		forConditions1 = setupListBox(for1List);
		forConditions2 = setupListBox(for2List);
		forConditions3 = setupListBox(for3List);
		booleanConditions = setupListBox(booleanList);
		
		topAlignPanel.add(structures);
		add(topAlignPanel);
		
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
		
		createButton.addClickHandler(new CreateHandler());	
		createButton.addStyleName("create_button");
		
		add(createButton);
		setCellHorizontalAlignment(createButton, HasHorizontalAlignment.ALIGN_RIGHT);
	}
	public boolean checkIfEmpty(String option){
		if(option.equals("for") && for1List!=null && for1List.length>0)
			return false;
		else if(!option.equals("choose structure...") && booleanList!=null && booleanList.length>0)
			return false;
		else if(option.equals("choose structure..."))
			return false;
		return true;
			
	}
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
	 
	 private class CreateHandler implements ClickHandler {
	        public void onClick(ClickEvent event) {
	        	StackableContainer createdContainer = null;
	        	String selected = structuresList[structures.getSelectedIndex()];
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
