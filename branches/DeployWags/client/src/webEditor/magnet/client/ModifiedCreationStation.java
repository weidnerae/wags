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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


public class ModifiedCreationStation extends VerticalPanel{
	private String[] structuresList;

	private Button createButton = new Button("Create");
	private ListBox structures;
	private TextBox for1Options = new TextBox();
	private TextBox for2Options = new TextBox();
	private TextBox for3Options = new TextBox();
	private TextBox booleanOptions = new TextBox();
	private TextBox statementText = new TextBox();
	private TextBox functionText = new TextBox();
	
	private VerticalPanel leftPanel = new VerticalPanel();
	private HorizontalPanel forPanel = new HorizontalPanel();
	private HorizontalPanel booleanPanel = new HorizontalPanel();
	private HorizontalPanel topAlignPanel = new HorizontalPanel();
	
	private ModifiedCodePanel codePanel;
	//private TextArea textArea = new TextArea();
	public PickupDragController dragControl;
	
public ModifiedCreationStation(String[] structuresList, ModifiedCodePanel codePanel){
		this.codePanel = codePanel;
		this.structuresList = structuresList;
		dragControl = codePanel.dragController;
		setStyleName("dropdown_panel");
	//	booleanConditions.addStyleName("boolean_conditions");

		structures = setupListBox(structuresList);
		structures.addChangeHandler(new StructuresHandler());

		
		topAlignPanel.add(structures);
		leftPanel.add(topAlignPanel);

		forPanel.addStyleName("for_conditions");
		
		forPanel.add(new HTML("&nbsp ( &nbsp"));
		forPanel.add(for1Options);
		forPanel.add(new HTML("&nbsp ; &nbsp"));
		forPanel.add(for2Options);
		forPanel.add(new HTML("&nbsp ; &nbsp"));
		forPanel.add(for3Options);
		forPanel.add(new HTML("&nbsp ) &nbsp"));
		
		booleanPanel.addStyleName("boolean_conditions");
		
		booleanPanel.add(new HTML("&nbsp ( &nbsp"));
		booleanPanel.add(booleanOptions);
		booleanPanel.add(new HTML("&nbsp ) &nbsp"));
		
		createButton.addClickHandler(new CreateHandler());	
		createButton.addStyleName("create_button");
		
		leftPanel.add(createButton);
		leftPanel.setCellHorizontalAlignment(createButton, HasHorizontalAlignment.ALIGN_RIGHT);
		
		add(leftPanel);
		add(codePanel);
		
	}
	
	public ListBox setupListBox(String[] listOptions){
		ListBox listBox = new ListBox();
		for(String option: listOptions){
			listBox.addItem(option);
		}
		return listBox;
	}
	
	 public void clearDropdowns(){
		 topAlignPanel.clear();
		 topAlignPanel.add(structures);
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
	 public void showStatementDropdowns(){
		 topAlignPanel.clear();
		 topAlignPanel.add(structures);
		 statementText.setText("");
		 topAlignPanel.add(statementText);
	 }
	 public void showFunctionDropdowns(){
		 topAlignPanel.clear();
		 topAlignPanel.add(structures);
		 functionText.setText("");
		 topAlignPanel.add(functionText);
		 
	 }
	 public void showAnyOrder(){
		 topAlignPanel.clear();
		 topAlignPanel.add(structures);
		 
	 }
	 private class CreateHandler implements ClickHandler {
	        public void onClick(ClickEvent event) {
	        	StackableContainer createdContainer = null;
	        	
	        	// get the item from the listbox and use it to create a new stackable container
	        	String selected = structuresList[structures.getSelectedIndex()];
	        	if(selected.equals("for")){
	        		createdContainer = new StackableContainer(Consts.FOR, dragControl, Consts.STATEMENT);
	        		String containerCondition ="";
	        		containerCondition+=for1Options.getText();
	        		containerCondition+="; " + for2Options.getText();
	        		containerCondition+="; " + for3Options.getText();
	        		createdContainer.addConditionContent(containerCondition);
	        	}
	        	if(selected.equals("while")){
	        		createdContainer = new StackableContainer(Consts.WHILE, dragControl, Consts.STATEMENT);
	        		createdContainer.addConditionContent(booleanOptions.getText());	
	        	}
	        	if(selected.equals("if")){
	        		createdContainer = new StackableContainer(Consts.IF, dragControl, Consts.STATEMENT);
	        		createdContainer.addConditionContent(booleanOptions.getText());
	        	}
	        	if(selected.equals("else if")){
	        		createdContainer = new StackableContainer(Consts.ELSEIF, dragControl, Consts.STATEMENT);
	        	    createdContainer.addConditionContent(booleanOptions.getText());
	        	}
	        	if(selected.equals("else"))
	        		createdContainer = new StackableContainer(Consts.ELSE, dragControl, Consts.STATEMENT);
	        	if(selected.equals("statement"))
	        		createdContainer = new StackableContainer(statementText.getText(), dragControl, Consts.STATEMENT);
	        	if(selected.equals("function"))
	        		createdContainer = new StackableContainer(functionText.getText()+ Consts.FUNCTION, dragControl, Consts.STATEMENT);  // may need to be Consts.INNER ?
	        	if(selected.equals("ANY ORDER BOX"))
	        		createdContainer = new StackableContainer(Consts.ANYORDERBOX,dragControl, Consts.STATEMENT);
	        	
	        	//putting The text that defines the StackableContainer into the TextArea
	        	codePanel.addCreatedContainer(createdContainer);
	        }
	 }
	 public String getDBString(){
		return codePanel.getInsideMainContent();
	 }
	 public String getPremadeDBString(){
		 return codePanel.getInsidePremadeMainContent();
	 }
	 public StackableContainer getMainFunction(){
		 return codePanel.mainFunction;
	 }
	 private class StructuresHandler implements ChangeHandler{

		@Override
		public void onChange(ChangeEvent event) {
			String selected = structuresList[structures.getSelectedIndex()];
        	if(selected.equals("choose structure..."))
        		clearDropdowns();
        	if(selected.equals("for"))
        		showForDropdowns();
        	if(selected.equals("while"))
        		showWhileDropdowns();
        	if(selected.equals("else if"))
        		showElseIfDropdowns();
        	if(selected.equals("if"))
        		showIfDropdowns();
        	if(selected.equals("else"))
        		clearDropdowns();
        	if(selected.equals("statement"))
        		showStatementDropdowns();
        	if(selected.equals("function"))
        		showFunctionDropdowns();
			
		}
	 }
}
