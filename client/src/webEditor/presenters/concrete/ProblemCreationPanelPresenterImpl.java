package webEditor.presenters.concrete;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import webEditor.ProxyFramework.AbstractServerCall;
import webEditor.ProxyFramework.DeleteMagnetExerciseCommand;
import webEditor.ProxyFramework.GetFileTimeCommand;
import webEditor.admin.ProblemCreationPanel;
import webEditor.presenters.interfaces.ProblemCreationPanelPresenter;

public class ProblemCreationPanelPresenterImpl implements ProblemCreationPanelPresenter {
	
	ProblemCreationPanel panel;
	private boolean bound = false; 
	
	//Global variables that are needed so the ChangeHandler can see them
	//or because they must be added conditionally 
	//Looking for a better way to organize it so that global variables are not needed
	ListBox decisionStructures;
	HorizontalPanel input, options, numberAllowedPanel;
	Button addMMOptionButton = new Button("Add");
	Button numberAllowedButton = new Button("Set Number");
	TextBox numberAllowedText = new TextBox();
	TextBox forCond1 	   = new TextBox();  //magnet maker option inputs
	TextBox forCond2 	   = new TextBox();
	TextBox forCond3	   = new TextBox();
	TextBox boolCond 	   = new TextBox();
	TextBox returnVal      = new TextBox();
	TextBox assignVariable = new TextBox();
	TextBox assignValue    = new TextBox();
	
	int numHelpers = 1;

	public ProblemCreationPanelPresenterImpl(ProblemCreationPanel view) {
		panel = view;
	}

	@Override
	public void addHelperUpload() {
		numHelpers++;
		HorizontalPanel newPanel = new HorizontalPanel();
		Label newLabel = new Label("Helper Class " + numHelpers + ":");
		newLabel.setWidth(150 + "px");
		FileUpload newUpload = new FileUpload();
		newUpload.setName("helperClass" + numHelpers);
		
		newPanel.add(newLabel);
		newPanel.add(newUpload);
		
		panel.vtPanelHelper().add(newPanel);
		
		AbstractServerCall timeCmd = new GetFileTimeCommand(panel.lstLoadExercise().getItemText(panel.lstLoadExercise().getSelectedIndex()), panel.uploadStamp(), panel.helperStamp());
	    timeCmd.sendRequest();
		//Proxy.getFileTime(lstLoadExercise.getItemText(lstLoadExercise.getSelectedIndex()), uploadStamp, helperStamp);		
	}

	@Override
	public void verifyDelete(final String title) {
		
		// Construct a dialog box verify the overwrite
		final DialogBox deleteBox = new DialogBox(false);
		Label deleteLbl = new Label("Delete Exercise: " + title);
		Button yes = new Button("YES");
		Button no = new Button("NO");
		
		VerticalPanel base = new VerticalPanel();
		base.setWidth("100%");
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setWidth("100%");
		buttonPanel.setHeight("22px");
		
		buttonPanel.add(yes);
		buttonPanel.add(no);
		yes.setHeight("20px");
		yes.setWidth("100%");
		no.setHeight("20px");
		no.setWidth("100%");
		base.add(deleteLbl);
		base.add(buttonPanel);
		deleteBox.add(base);
		
		yes.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				AbstractServerCall cmd = new DeleteMagnetExerciseCommand(title);
				cmd.sendRequest();
				//Proxy.deleteMagnetExercise(title);
				deleteBox.hide();
			}	
		});
		
		no.addClickHandler(new ClickHandler() {	
			public void onClick(ClickEvent event) {
				deleteBox.hide();
			}
		});
		
		deleteBox.center();		
	}

	@Override
	public void verifyOverwrite() {
		// Construct a dialog box verify the overwrite
		final DialogBox overwriteBox = new DialogBox(false);
		Label overwriteLbl = new Label("Overwrite Current Exercise: " + panel.finalTitleTxtBox().getText());
		Button yes = new Button("YES");
		Button no = new Button("NO");
		
		VerticalPanel base = new VerticalPanel();
		base.setWidth("100%");
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setWidth("100%");
		buttonPanel.setHeight("22px");
		
		buttonPanel.add(yes);
		buttonPanel.add(no);
		yes.setHeight("20px");
		yes.setWidth("100%");
		no.setHeight("20px");
		no.setWidth("100%");
		base.add(overwriteLbl);
		base.add(buttonPanel);
		overwriteBox.add(base);
		
		// If yes, re-submit the form with overwrite flagged
		yes.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				panel.overwrite().setValue(true);
				panel.problemCreateFormPanel().submit();
				overwriteBox.hide();
			}
			
		});
		
		// If no, clear title box and focus it for user
		no.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				panel.titleTxtBox().setText("");
				panel.finalTitleTxtBox().setText("");
				panel.titleTxtBox().setFocus(true);
				overwriteBox.hide();
			}
		});
		
		overwriteBox.center();		
	}

	@Override
	public void clearMagnetMakerOptions() {
		//left side of screen
		panel.magnetMakerOptions().clear();
		panel.magnetMakerOptions().setStyleName("");  //clear CSS
		//right side of screen
		panel.numberAllowedReviewPanel().setVisible(false);
		panel.forLoop1TextArea().setVisible(false);
		panel.forLoop2TextArea().setVisible(false);
		panel.forLoop3TextArea().setVisible(false);
		panel.whilesTextArea().setVisible(false);
		panel.ifsTextArea().setVisible(false);
		panel.returnsTextArea().setVisible(false);
		panel.assignmentsValTextArea().setVisible(false);
		panel.assignmentsVarTextArea().setVisible(false);
		panel.forLoop1Label().setVisible(false);
		panel.forLoop2Label().setVisible(false);
		panel.forLoop3Label().setVisible(false);
		panel.whileLabel().setVisible(false);
		panel.ifLabel().setVisible(false);
		panel.returnLabel().setVisible(false);
		panel.assignmentValLabel().setVisible(false);
		panel.assignmentVarLabel().setVisible(false);
		
	}

	@Override
	public void setupMagnetMakerOptions() {
		panel.magnetMakerOptions().clear();
		//left side of the screen
		//first make title label and add it in
		Label title = new Label("Magnet Maker Options");
		panel.magnetMakerOptions().add(title);
		panel.magnetMakerOptions().setStyleName("problem_creation_magnetmaker");
		
		options = new HorizontalPanel();
		
		//decision structure dropdown for options
		AbsolutePanel dropdown = new AbsolutePanel();
		decisionStructures = new ListBox();
		decisionStructures.addItem("choose type of decision structure...");
		decisionStructures.addItem("if");
		decisionStructures.addItem("for");
		decisionStructures.addItem("while");
		decisionStructures.addItem("else");
		decisionStructures.addItem("else if");
		decisionStructures.addItem("return");
		decisionStructures.addItem("assignment");
		decisionStructures.addChangeHandler(new StructuresHandler());
		dropdown.add(decisionStructures);  //handler will create input panel
		dropdown.setStyleName("problem_creation_mm_dropdown");
		options.add(dropdown);
		
		numberAllowedPanel = new HorizontalPanel();
		options.add(numberAllowedPanel);
		
		panel.magnetMakerOptions().add(options);

		input = new HorizontalPanel();
		input.setStyleName("problem_creation_mm_input");
		
		panel.magnetMakerOptions().add(input);
		
		addMMOptionButton.setStyleName("problem_creation_float_right");
		panel.magnetMakerOptions().add(addMMOptionButton);
		
		//right side of screen (problem review)
		panel.numberAllowedReviewPanel().setVisible(true);
		panel.forLoop1TextArea().setVisible(true);
		panel.forLoop2TextArea().setVisible(true);
		panel.forLoop3TextArea().setVisible(true);
		panel.forLoop1Label().setVisible(true);
		panel.forLoop2Label().setVisible(true);
		panel.forLoop3Label().setVisible(true);
		panel.whileLabel().setVisible(true);
		panel.ifLabel().setVisible(true);
		panel.returnLabel().setVisible(true);
		panel.whilesTextArea().setVisible(true);
		panel.ifsTextArea().setVisible(true);
		panel.returnsTextArea().setVisible(true);
		panel.assignmentVarLabel().setVisible(true);
		panel.assignmentValLabel().setVisible(true);
		panel.assignmentsVarTextArea().setVisible(true);
		panel.assignmentsValTextArea().setVisible(true);		
	}
	
	/** 
	 * THis method does most of the work for implementing the users choice of decision structure
	 * when creating magnets through the advanced magnet creator.
	 */
	private class StructuresHandler implements ChangeHandler{
		@Override
		public void onChange(ChangeEvent event) {
			int index = decisionStructures.getSelectedIndex();
			String value = decisionStructures.getValue(index);
        	if(value.equals("choose type of decision structure..."))  // Default Text
        	{
        		//clear the input panel
        		input.clear();
        		numberAllowedPanel.clear();
        	} else if(value.equals("while"))
        	{
        		//set up input panel for while conditions
       			input.clear();
       			input.add(new Label("while ( "));
       			input.add(boolCond);
       			input.add(new Label(" ) {}"));
        		//set up input panel for number of loops allowed to be created
       			numberAllowedPanel.clear();
        		numberAllowedPanel.add(new Label("Number Allowed: "));
        		numberAllowedPanel.add(numberAllowedText);
        		numberAllowedPanel.add(new Label("  "));
        		numberAllowedPanel.add(numberAllowedButton);
        	} else if(value.equals("for")) {
        		//set up input panel for for loop arguments
        		input.clear();
        		input.add(new Label("for ( "));
        		input.add(forCond1);
        		input.add(new Label(" ; "));
        		input.add(forCond2);
        		input.add(new Label(" ; "));
        		input.add(forCond3);
        		input.add(new Label(" ){} "));
        		//set up input panel for number of loops allowed to be created
        		numberAllowedPanel.clear();
        		numberAllowedPanel.add(new Label("Number Allowed: "));
        		numberAllowedPanel.add(numberAllowedText);
        		numberAllowedPanel.add(new Label("  "));
        		numberAllowedPanel.add(numberAllowedButton);
        	} else if(value.equals("if"))
        	{
        		//set up input panel for if conditions
       			input.clear();
       			input.add(new Label("if ( "));
       			input.add(boolCond);
       			input.add(new Label(" ) {}"));
        		//set up input panel for number of loops allowed to be created
       			numberAllowedPanel.clear();
       			numberAllowedPanel.add(new Label("Number Allowed: "));
       			numberAllowedPanel.add(numberAllowedText);
       			numberAllowedPanel.add(new Label("  "));
       			numberAllowedPanel.add(numberAllowedButton);
        	} else if(value.equals("else"))
        	{
        		//set up input panel for else magnet
       			input.clear();
       			input.add(new Label("else { "));
       			input.add(new Label(" }"));
        		//set up input panel for number of loops allowed to be created
       			numberAllowedPanel.clear();
       			numberAllowedPanel.add(new Label("Number Allowed: "));
       			numberAllowedPanel.add(numberAllowedText);
       			numberAllowedPanel.add(new Label("  "));
       			numberAllowedPanel.add(numberAllowedButton);
        	} else if(value.equals("else if"))
        	{
        		//set up input panel for else if conditions
       			input.clear();
       			input.add(new Label("else if ( "));
       			input.add(boolCond);
       			input.add(new Label(" ) {}"));
        		//set up input panel for number of loops allowed to be created
       			numberAllowedPanel.clear();
       			numberAllowedPanel.add(new Label("Number Allowed: "));
       			numberAllowedPanel.add(numberAllowedText);
       			numberAllowedPanel.add(new Label("  "));
       			numberAllowedPanel.add(numberAllowedButton);
        	} else if(value.equals("return")){
        		// set up input panel for return values
        		input.clear();
        		input.add(new Label("return "));
        		input.add(returnVal);
        		input.add(new Label(";"));
        		//set up input panel for number of return values allowed
        		numberAllowedPanel.clear();
       			numberAllowedPanel.add(new Label("Number Allowed: "));
       			numberAllowedPanel.add(numberAllowedText);
       			numberAllowedPanel.add(new Label("  "));
       			numberAllowedPanel.add(numberAllowedButton);
        	} else if(value.equals("assignment")){
        		input.clear();
        		input.add(assignVariable);
        		input.add(new Label(" = "));
        		input.add(assignValue);
        		input.add(new Label(";"));
        		numberAllowedPanel.clear();
       			numberAllowedPanel.add(new Label("Number Allowed: "));
       			numberAllowedPanel.add(numberAllowedText);
       			numberAllowedPanel.add(new Label("  "));
       			numberAllowedPanel.add(numberAllowedButton);
        	}
		}
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(panel.asWidget());
		
	}

	@Override
	public void bind() {
		panel.setPresenter(this);		
		bound = true;
	}

	@Override
	public boolean bound() {
		return bound;
	}

	@Override
	public void update(List<String> data) {
		// TODO Auto-generated method stub
		
	}
	
	

}
