package webEditor.magnet.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


public class MCSOptions extends HorizontalPanel{
	private String[] structuresList;

	private Button createButton = new Button("Create");
	private ListBox structures;
	private TextBox for1Options = new TextBox();
	private TextBox for2Options = new TextBox();
	private TextBox for3Options = new TextBox();
	private TextBox booleanOptions = new TextBox();
	private TextBox statementText = new TextBox();
	
	private VerticalPanel leftPanel = new VerticalPanel();
	private HorizontalPanel forPanel = new HorizontalPanel();
	private HorizontalPanel booleanPanel = new HorizontalPanel();
	private HorizontalPanel topAlignPanel = new HorizontalPanel();
	
	private HorizontalPanel conditionsAreaPanel = new HorizontalPanel();
	private TextArea for1Area = new TextArea();
	private TextArea for2Area = new TextArea();
	private TextArea for3Area = new TextArea();
	private TextArea booleanArea = new TextArea();
	
	public MCSOptions(String[] structuresList){
		
		this.structuresList = structuresList;
		setStyleName("dropdown_panel");

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
				
		booleanPanel.add(new HTML("&nbsp ( &nbsp"));
		booleanPanel.add(booleanOptions);
		booleanPanel.add(new HTML("&nbsp ) &nbsp"));
		
		createButton.addClickHandler(new CreateHandler());	
		createButton.addStyleName("create_button");
		
		leftPanel.add(createButton);
		leftPanel.setCellHorizontalAlignment(createButton, HasHorizontalAlignment.ALIGN_RIGHT);
		
		add(leftPanel);
		
		for1Area.setText("<For-1>");
		for1Area.setCharacterWidth(8);
		conditionsAreaPanel.add(for1Area);
		for2Area.setText("<For-2>");
		for2Area.setCharacterWidth(8);
		conditionsAreaPanel.add(for2Area);
		for3Area.setText("<For-3>");
		for3Area.setCharacterWidth(8);
		conditionsAreaPanel.add(for3Area);
		booleanArea.setText("<boolean>");
		conditionsAreaPanel.add(booleanArea);
		conditionsAreaPanel.setSpacing(10);
		
		add(conditionsAreaPanel);	
	}
	
	public ListBox setupListBox(String[] listOptions){
		ListBox listBox = new ListBox();
		for(String option: listOptions){
			listBox.addItem(option);
		}
		return listBox;
	}

	 public void clearDropdowns() {
		 topAlignPanel.clear();
		 topAlignPanel.add(structures);
	 }
	 
	 public void showForDropdowns(){
		 topAlignPanel.clear();
		 topAlignPanel.add(structures);
		 topAlignPanel.add(forPanel);
	 }
	 public void showBooleanDropdowns(){
		 topAlignPanel.clear();
		 topAlignPanel.add(structures);
		 topAlignPanel.add(booleanPanel);
	 }
	 
	 public boolean isDuplicate(TextArea area, TextBox box){
		 String[] split = area.getText().split(".:colon:.");
		 if(area.getText().contains(box.getText()))
		 	return false;
		 return true;
		 
	 }
	 private class CreateHandler implements ClickHandler {
	        public void onClick(ClickEvent event) {
	        	StackableContainer createdContainer = null;
	        	
	        	// get the item from the listbox and use it to create a new stackable container
	        	String selected = structuresList[structures.getSelectedIndex()];
	        	if(selected.equals("for")){
	        		if(isDuplicate(for1Area,for1Options))
	        			for1Area.setText(for1Area.getText()+".:colon:."+for1Options.getText());
	        		if(isDuplicate(for2Area,for2Options))
	        			for2Area.setText(for2Area.getText()+".:colon:."+for2Options.getText());
	        		if(isDuplicate(for3Area,for3Options))
	        			for3Area.setText(for3Area.getText()+".:colon:."+for3Options.getText());
	        	}
	        	if(selected.equals("boolean")){
	        		booleanArea.setText(booleanArea.getText()+".:colon:."+booleanOptions.getText());
	        	}
	        }
	 }
	 public String[] getAreasText(){
		 return new String[]{for1Area.getText(),for2Area.getText(),for3Area.getText(),booleanArea.getText()};
	 }
	 private class StructuresHandler implements ChangeHandler{

		@Override
		public void onChange(ChangeEvent event) {
			String selected = structuresList[structures.getSelectedIndex()];
        	if(selected.equals("choose structure..."))
        		clearDropdowns();
        	if(selected.equals("for"))
        		showForDropdowns();
        	if(selected.equals("boolean"))
        		showBooleanDropdowns();
			
		}
	 }
}
