package webEditor.admin;

import webEditor.MagnetProblem;
import webEditor.Notification;
import webEditor.Proxy;
import webEditor.WEStatus;
import webEditor.magnet.view.Consts;
import webEditor.magnet.view.MagnetProblemCreator;
import webEditor.magnet.view.MagnetType;
import webEditor.presenters.interfaces.ProblemCreationPanelPresenter;
import webEditor.views.interfaces.ProblemCreationPanelView;
import webEditor.Common.Presenter;
import webEditor.ProxyFramework.AbstractServerCall;
import webEditor.ProxyFramework.AddMagnetLinkageCommand;
import webEditor.ProxyFramework.GetMagnetGroupsCommand;
import webEditor.ProxyFramework.GetMagnetsByGroupCommand;


import webEditor.ProxyFramework.DeleteMagnetExerciseCommand;
import webEditor.ProxyFramework.GetFileTimeCommand;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

/**
 * This class creates, populates, and adds the functionality to the problem creation page
 * of the admin control board. On this page users are allowed to load, modify, create, and delete
 * exercises using a variety of tools. 
 */

public class ProblemCreationPanel extends Composite implements ProblemCreationPanelView{

	private static ProblemCreationPanelUiBinder uiBinder = GWT
			.create(ProblemCreationPanelUiBinder.class);

	private ProblemCreationPanelPresenter presenter;
	
	interface ProblemCreationPanelUiBinder extends UiBinder<Widget, ProblemCreationPanel> {
	}
	
	final String ADVANCED_PROBLEM = "advanced_problem";
	final String BASIC_PROBLEM = "basic_problem";
	final String PROLOG_BASIC_PROBLEM = "prolog_basic_problem";
	final String C_BASIC_PROBLEM = "c_basic_problem";
	
	@UiField FormPanel problemCreateFormPanel, fileParseFormPanel, downloadMagnetFilesForm;
	@UiField TextBox titleTxtBox, topLabelTxtBox, topRealCodeTxtBox, 
		commentsTxtBox, bottomLabelTxtBox, bottomRealCodeTxtBox, forAllowed, whileAllowed, ifAllowed, 
		elseAllowed, elseIfAllowed, returnAllowed, assignmentAllowed, lastProblemLoadedTxtBox, 
		lastProblemLoadedDownloadTxtBox, prologLabelTxtBox, prologRealTxtBox;
	@UiField TextArea finalTitleTxtBox, descriptionTxtArea, finalDescriptionTxtArea, finalTypeTxtArea,
		classDeclarationTxtArea, innerFunctionsTxtArea, statementsTxtArea, commentsStagingArea,
		hiddenFunctionsArea, forLoop1TextArea, forLoop2TextArea, forLoop3TextArea, whilesTextArea, ifsTextArea,
		returnsTextArea, assignmentsVarTextArea, assignmentsValTextArea;
	@UiField VerticalPanel magnetMakerOptions, magnetReviewPanel, numberAllowedReviewPanel, javaMagnetMaker, 
	prologMagnetMaker;
	@UiField SubmitButton createProblemSubmitButton, fileParseSbt, downloadMagnetFilesButton;
	@UiField Button createCommentsButton, classDeclarationButton, innerFunctionsButton,
		statementsButton, clearDataButton, createHidFunctionButton, btnLoadExercise,
		btnDeleteExercise, btnMoreHelpers, testProblemButton, prologFactBtn, prologRuleBtn, prologTermBtn,
		prologProcedureBtn;
	@UiField RadioButton btnBasicProblem, btnAdvancedProblem, btnPrologBasicProblem, btnCBasicProblem;
	@UiField FileUpload solutionUpload, helperUpload;
	@UiField Label uploadStamp, helperStamp;
	@UiField ListBox lstGroup, lstLoadGroup, lstLoadExercise;
	@UiField Label lblGroup, forLoop1Label, forLoop2Label, forLoop3Label, whileLabel, ifLabel, returnLabel, 
	assignmentVarLabel, assignmentValLabel, testClassLabel, helperClassLabel, classDeclarationTxtAreaLabel,
	innerFunctionsTxtAreaLabel, statementsTxtAreaLabel, hiddenFunctionsLabel;
	@UiField VerticalPanel vtPanelHelper;
	@UiField CheckBox overwrite;
	
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
	private MagnetProblemCreator magnetProblemCreator;

		
	public ProblemCreationPanel(){
		initWidget(uiBinder.createAndBindUi(this));
		
		this.magnetProblemCreator = new MagnetProblemCreator();
		AbstractServerCall magnetCmd = new GetMagnetGroupsCommand(lstLoadGroup, null, null, null, null);
		magnetCmd.sendRequest();
		AbstractServerCall cmd = new GetMagnetsByGroupCommand("Arrays/ArrayLists", null, 
				null, null, lstLoadExercise);
		cmd.sendRequest();
		
		btnBasicProblem.setValue( true );
		//do a proxy call when loading problem to set a bar with times
		//do same proxy call when on submit complete to update bar wtih times
		downloadMagnetFilesForm.setAction(Proxy.getBaseURL() + "?cmd=DownloadMagnetFiles");
		downloadMagnetFilesForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		downloadMagnetFilesForm.setMethod(FormPanel.METHOD_POST);
		
		problemCreateFormPanel.setAction(Proxy.getBaseURL() + "?cmd=AddMagnetExercise");
		problemCreateFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		problemCreateFormPanel.setMethod(FormPanel.METHOD_POST);
		problemCreateFormPanel.addSubmitCompleteHandler(new SubmitCompleteHandler(){
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				// Should have to verify overwrite each time
				overwrite.setValue(false);
				
				WEStatus stat = new WEStatus(event.getResults());
				
				if(stat.getStat() == WEStatus.STATUS_SUCCESS){
					
					AbstractServerCall cmd = new AddMagnetLinkageCommand(stat.getMessage());
					cmd.sendRequest();
					
					//Proxy.addMagnetLinkage(stat.getMessage()); // The title of the problem
					// Remove added Helper Class widgets
					for(int i = 1; i < vtPanelHelper.getWidgetCount(); i++){
						vtPanelHelper.remove(i);
					}
					numHelpers = 1;
				} else if (stat.getStat() == WEStatus.STATUS_WARNING){
					Notification.notify(stat.getStat(), stat.getMessage());
					verifyOverwrite();
				} else {
					Notification.notify(stat.getStat(), stat.getMessage());
				}
			}
		});
		
		fileParseFormPanel.setAction(Proxy.getBaseURL() + "?cmd=JavaToMagnets");
		fileParseFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		fileParseFormPanel.setMethod(FormPanel.METHOD_POST);
		fileParseFormPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String[] magnets = event.getResults().split("\n");
				classDeclarationTxtArea.setText(magnets[0]);
				innerFunctionsTxtArea.setText(magnets[2]);
				statementsTxtArea.setText(magnets[4]);
			}
		});
		
		/** 
		 * Makes it so that when focused is shifted from the title input text area (on the left) the 
		 * user input will automatically be placed in the final title text area (on the right)
		 */
		titleTxtBox.addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				finalTitleTxtBox.setText(titleTxtBox.getText());
			}
		});
		
		/** 
		 * Makes it so that when focused is shifted from the description input text area (on the left) the 
		 * user input will automatically be placed in the final description text area (on the right)
		 */
		descriptionTxtArea.addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				finalDescriptionTxtArea.setText(descriptionTxtArea.getText());
			}
		});
		
		lstLoadGroup.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				AbstractServerCall cmd = new GetMagnetsByGroupCommand(lstLoadGroup.getItemText(lstLoadGroup.getSelectedIndex()),
						null, null, null, lstLoadExercise);
				cmd.sendRequest();
			}
		});
		
		/** 
		 * Creates a button which when clicked will load the details of a selected magnet problem from the
		 * database and into the problem creation menu. 
		 */
		btnLoadExercise.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String lastProblemLoaded = lstLoadExercise.getItemText(lstLoadExercise.getSelectedIndex());
				lastProblemLoadedTxtBox.setText(lastProblemLoaded);
				lastProblemLoadedDownloadTxtBox.setText(lastProblemLoaded);
				Proxy.getMagnetProblemForEdit(finalTitleTxtBox, finalDescriptionTxtArea, classDeclarationTxtArea, 
						innerFunctionsTxtArea, statementsTxtArea, lstLoadExercise.getItemText(lstLoadExercise.getSelectedIndex()),
						finalTypeTxtArea,forLoop1TextArea, forLoop2TextArea, forLoop3TextArea, ifsTextArea, whilesTextArea, returnsTextArea,
						assignmentsVarTextArea, assignmentsValTextArea, ifAllowed, elseAllowed, elseIfAllowed, forAllowed, whileAllowed, 
						returnAllowed, assignmentAllowed, btnBasicProblem, btnAdvancedProblem, btnPrologBasicProblem, btnCBasicProblem);
				AbstractServerCall timeCmd = new GetFileTimeCommand(lstLoadExercise.getItemText(lstLoadExercise.getSelectedIndex()), uploadStamp, helperStamp);
			    timeCmd.sendRequest();
				//Proxy.getFileTime(lstLoadExercise.getItemText(lstLoadExercise.getSelectedIndex()), uploadStamp, helperStamp);
				// dear god help me please
				Timer t = new Timer() {
			      @Override
			      public void run() {
						if (btnBasicProblem.getValue() == true) {
							btnBasicProblem.setValue(true);
							clearMagnetMakerOptions();
							setupJavaOptions();
							finalTypeTxtArea.setText( BASIC_PROBLEM );
						} else if (btnAdvancedProblem.getValue() == true) {
							btnAdvancedProblem.setValue(true);
							setupMagnetMakerOptions();
							setupJavaOptions();
							finalTypeTxtArea.setText( ADVANCED_PROBLEM );
						}else if (btnPrologBasicProblem.getValue() == true){
							btnPrologBasicProblem.setValue(true);
							clearMagnetMakerOptions();
							setupPrologOptions();
							finalTypeTxtArea.setText( PROLOG_BASIC_PROBLEM );
						} else if (btnCBasicProblem.getValue() == true) {
							btnCBasicProblem.setValue(true);
							clearMagnetMakerOptions();
							setupCOptions();
							finalTypeTxtArea.setText( C_BASIC_PROBLEM );
						}
						AbstractServerCall timeCmd = new GetFileTimeCommand(lstLoadExercise.getItemText(lstLoadExercise.getSelectedIndex()), uploadStamp, helperStamp);
					    timeCmd.sendRequest();
						//Proxy.getFileTime(lstLoadExercise.getItemText(lstLoadExercise.getSelectedIndex()), uploadStamp, helperStamp);
			      }
			    };
			    t.schedule(160);
			}
		});
		
		/** Creates a button that when clicked will clear all exercise details */
		btnDeleteExercise.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String title = lstLoadExercise.getItemText(lstLoadExercise.getSelectedIndex());
				verifyDelete(title);
			}
		});
		
		/** 
		 * Creates a button which when pushed will take the user input from the number allowed
		 * text area of the advanced magnet creation drop down box and set the maximum number
		 * of that type of magnet. 
		 */
		numberAllowedButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String value = numberAllowedText.getText();  //get value from text box
				
				int index = decisionStructures.getSelectedIndex();
				String indexValue = decisionStructures.getValue(index);  //find out the selected decision structure
	            		
				//update the selected decision structures corresponding textbox on the right side
				//of the screen with the current value
				if(indexValue.equals("while")) {
					whileAllowed.setText(value);
	        	} else if(indexValue.equals("for")) {
	        		forAllowed.setText(value);
	        	} else if(indexValue.equals("if")) {
	        		ifAllowed.setText(value);
	        	} else if(indexValue.equals("else")) {
	        		elseAllowed.setText(value);
	        	} else if(indexValue.equals("else if")) {
	        		elseIfAllowed.setText(value);
	        	} else if(indexValue.equals("return")) {
	        		returnAllowed.setText(value);
	        	} else if(indexValue.equals("assignment")) {
	        		assignmentAllowed.setText(value);
	        	}
			}
		});
		
		/**
		 * Adds a button which when clicked will add a user created decision structure to the appropriate 
		 * textbox on the right hand side of the screen.
		 */
		addMMOptionButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//get which type of magnet maker you'll be inputting
				int index = decisionStructures.getSelectedIndex();
				String selected = decisionStructures.getValue(index);
				
				if (selected.equals("for"))
				{
					if (forLoop1TextArea.getText().equals("")) {
						forLoop1TextArea.setText(forCond1.getText());
					} else forLoop1TextArea.setText(forLoop1TextArea.getText() + ".:|:." + forCond1.getText());
					if (forLoop2TextArea.getText().equals("")) {
						forLoop2TextArea.setText(forCond2.getText());
					} else forLoop2TextArea.setText(forLoop2TextArea.getText() + ".:|:." + forCond2.getText());
					if (forLoop3TextArea.getText().equals("")) {
						forLoop3TextArea.setText(forCond3.getText());
					} else forLoop3TextArea.setText(forLoop3TextArea.getText() + ".:|:." + forCond3.getText());	
				} else if (selected.equals("if")) {
					//extra code because both "booleans" general field exists as well as 
					//separated fields for if booleans and while booleans
					if (ifsTextArea.getText().equals("")) {
						ifsTextArea.setText(boolCond.getText());
					} else ifsTextArea.setText(ifsTextArea.getText() + ".:|:." + boolCond.getText());	
				} else if (selected.equals("while")) {
					//extra code because both "booleans" general field exists as well as 
					//separated fields for if booleans and while booleans
					if (whilesTextArea.getText().equals("")) {
						whilesTextArea.setText(boolCond.getText());
					} else whilesTextArea.setText(whilesTextArea.getText() + ".:|:." + boolCond.getText());
				} else if (selected.equals("else")) {
				} else if (selected.equals("else if")) {
					//extra code because both "booleans" general field exists as well as 
					//separated fields for if booleans and while booleans
					if (ifsTextArea.getText().equals("")) {
						ifsTextArea.setText(boolCond.getText());
					} else ifsTextArea.setText(ifsTextArea.getText() + ".:|:." + boolCond.getText());	
				} else if(selected.equals("return")) {
					if(returnsTextArea.getText().equals("")){
						returnsTextArea.setText(returnVal.getText());
					} else 
						returnsTextArea.setText(returnsTextArea.getText() + ".:|:." + returnVal.getText());
				} else if(selected.equals("assignment")) {
					if(assignmentsVarTextArea.getText().equals("")){
						assignmentsVarTextArea.setText(assignVariable.getText());
					} else
						assignmentsVarTextArea.setText(assignmentsVarTextArea.getText() + ".:|:." + assignVariable.getText());
				    if(assignmentsValTextArea.getText().equals("")) {
				    	assignmentsValTextArea.setText(assignValue.getText());
				    } else 
				    	assignmentsValTextArea.setText(assignmentsValTextArea.getText() + ".:|:." + assignValue.getText());
				}
			}
		});
		
		btnMoreHelpers.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				addHelperUpload();				
			}
		});
		
		numberAllowedButton.setTitle( "Adjusts the number of the selected decision structure that the student will be" +
												" allowed to use at one time.");
		addMMOptionButton.setTitle( "Adds the input conditions to the problem so that students can select from them" +
											 " when creating the selected decision structure");
	}	

	
	private void addHelperUpload(){
		numHelpers++;
		HorizontalPanel newPanel = new HorizontalPanel();
		Label newLabel = new Label("Helper Class " + numHelpers + ":");
		newLabel.setWidth(150 + "px");
		FileUpload newUpload = new FileUpload();
		newUpload.setName("helperClass" + numHelpers);
		
		newPanel.add(newLabel);
		newPanel.add(newUpload);
		
		vtPanelHelper.add(newPanel);
		
		AbstractServerCall timeCmd = new GetFileTimeCommand(lstLoadExercise.getItemText(lstLoadExercise.getSelectedIndex()), uploadStamp, helperStamp);
	    timeCmd.sendRequest();
		//Proxy.getFileTime(lstLoadExercise.getItemText(lstLoadExercise.getSelectedIndex()), uploadStamp, helperStamp);
	}
	
	/**
	 * creates a pop-up window which will open up once the user presses the delete button
	 * and will verify with the user that they really want to delete the magnet exercise.
	 *
	 * @param title the title of the current exercise being created
	 */
	void verifyDelete(final String title){
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
	
	/**
	 * Creates a pop-up window when the user wishes to overwrite their current exercise which will
	 * verify that the user really wants to overwrite. 
	 */
	void verifyOverwrite(){
		// Construct a dialog box verify the overwrite
		final DialogBox overwriteBox = new DialogBox(false);
		Label overwriteLbl = new Label("Overwrite Current Exercise: " + finalTitleTxtBox.getText());
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
				overwrite.setValue(true);
				problemCreateFormPanel.submit();
				overwriteBox.hide();
			}
			
		});
		
		// If no, clear title box and focus it for user
		no.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				titleTxtBox.setText("");
				finalTitleTxtBox.setText("");
				titleTxtBox.setFocus(true);
				overwriteBox.hide();
			}
		});
		
		overwriteBox.center();
	}
	
	
	/** Removes the Magnet Maker Options panel from Problem Creation */
	void clearMagnetMakerOptions()
	{
		//left side of screen
		magnetMakerOptions.clear();
		magnetMakerOptions.setStyleName("");  //clear CSS
		//right side of screen
		numberAllowedReviewPanel.setVisible(false);
		forLoop1TextArea.setVisible(false);
		forLoop2TextArea.setVisible(false);
		forLoop3TextArea.setVisible(false);
		whilesTextArea.setVisible(false);
		ifsTextArea.setVisible(false);
		returnsTextArea.setVisible(false);
		assignmentsValTextArea.setVisible(false);
		assignmentsVarTextArea.setVisible(false);
		forLoop1Label.setVisible(false);
		forLoop2Label.setVisible(false);
		forLoop3Label.setVisible(false);
		whileLabel.setVisible(false);
		ifLabel.setVisible(false);
		returnLabel.setVisible(false);
		assignmentValLabel.setVisible(false);
		assignmentVarLabel.setVisible(false);
	}
	
	/** Sets up the Magnet Maker Options panel and makes it visible in Problem Creation 
	 * 
	 * 	Adds decisionStructures ListBox to magnet maker options panel, adds appropriate listeners to "Set Number" and "Add" buttons*/
	void setupMagnetMakerOptions()
	{
		magnetMakerOptions.clear();
		//left side of the screen
		//first make title label and add it in
		Label title = new Label("Magnet Maker Options");
		magnetMakerOptions.add(title);
		magnetMakerOptions.setStyleName("problem_creation_magnetmaker");
		
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
		
		magnetMakerOptions.add(options);

		input = new HorizontalPanel();
		input.setStyleName("problem_creation_mm_input");
		
		magnetMakerOptions.add(input);
		
		addMMOptionButton.setStyleName("problem_creation_float_right");
		magnetMakerOptions.add(addMMOptionButton);
		
		//right side of screen (problem review)
		numberAllowedReviewPanel.setVisible(true);
		forLoop1TextArea.setVisible(true);
		forLoop2TextArea.setVisible(true);
		forLoop3TextArea.setVisible(true);
		forLoop1Label.setVisible(true);
		forLoop2Label.setVisible(true);
		forLoop3Label.setVisible(true);
		whileLabel.setVisible(true);
		ifLabel.setVisible(true);
		returnLabel.setVisible(true);
		whilesTextArea.setVisible(true);
		ifsTextArea.setVisible(true);
		returnsTextArea.setVisible(true);
		assignmentVarLabel.setVisible(true);
		assignmentValLabel.setVisible(true);
		assignmentsVarTextArea.setVisible(true);
		assignmentsValTextArea.setVisible(true);
	}
	
	private void setupPrologOptions()
	{
		helperClassLabel.setText("Prolog Query File: ");
		testClassLabel.setText("Prolog Solution File: ");
		prologMagnetMaker.setVisible(true);
		javaMagnetMaker.setVisible(false);
		fileParseFormPanel.setVisible(false);
		statementsTxtAreaLabel.setText("Facts/Rules/Terms:");
		innerFunctionsTxtAreaLabel.setText("Starting Comment:");
		hiddenFunctionsArea.setVisible(false);
		hiddenFunctionsLabel.setVisible(false);
		classDeclarationTxtArea.setVisible(false);
		classDeclarationTxtAreaLabel.setVisible(false);
		createHidFunctionButton.setVisible(false);
	}
	
	private void setupJavaOptions()
	{
		javaMagnetMaker.setVisible(true);
		prologMagnetMaker.setVisible(false);
		classDeclarationTxtArea.setVisible(true);
		classDeclarationTxtAreaLabel.setVisible(true);
		helperClassLabel.setText("Helper Class: ");
		testClassLabel.setText("Testing Class: ");
		fileParseFormPanel.setVisible(true);
		classDeclarationTxtArea.setVisible(false);
		classDeclarationTxtAreaLabel.setVisible(false);
		statementsTxtAreaLabel.setText("Statements:");
		innerFunctionsTxtAreaLabel.setText("Functions: ");
		hiddenFunctionsArea.setVisible(true);
		hiddenFunctionsLabel.setVisible(true);
		createHidFunctionButton.setVisible(true);
	}
	
	private void setupCOptions()
	{
		javaMagnetMaker.setVisible(true);
		prologMagnetMaker.setVisible(false);
		helperClassLabel.setText("Helper Class: ");
		testClassLabel.setText("Testing Class: ");
		fileParseFormPanel.setVisible(true);
		statementsTxtAreaLabel.setText("Statements:");
		innerFunctionsTxtAreaLabel.setText("Functions: ");
		hiddenFunctionsArea.setVisible(true);
		hiddenFunctionsLabel.setVisible(true);
		createHidFunctionButton.setVisible(true);
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

	/**
	 * Makes it so when the creation mode is changes to basic that the text in the box
	 * labeled "Problem Type" on the right hand side of the screen is changes to say "basic problem"
	 *
	 * @param event the event caused when the user changes to the basic problem mode
	 */
	@UiHandler("btnBasicProblem")
	void onBasicProblemClick(ValueChangeEvent<Boolean> event)
	{
		clearMagnetMakerOptions();
		setupJavaOptions();
		finalTypeTxtArea.setText(BASIC_PROBLEM);
	}
	
	/**
	 * Makes it so when the creation mode is changes to advanced that the text in the box
	 * labeled "Problem Type" on the right hand side of the screen is changes to say "advanced problem"
	 *
	 * @param event the event caused when the user changes to the advanced problem mode
	 */
	@UiHandler("btnAdvancedProblem")
	void onAdvancedProblemClick(ValueChangeEvent<Boolean> event)
	{
		setupMagnetMakerOptions();
		setupJavaOptions();
		finalTypeTxtArea.setText(ADVANCED_PROBLEM);
		
	}
	
	@UiHandler("btnPrologBasicProblem")
	void onPrologBasicProblemClick(ValueChangeEvent<Boolean> event)
	{
		clearMagnetMakerOptions();
		setupPrologOptions();
		finalTypeTxtArea.setText(PROLOG_BASIC_PROBLEM);
	}
	
	@UiHandler("btnCBasicProblem")
	void onCBasicProblemClick(ValueChangeEvent<Boolean> event)
	{
		clearMagnetMakerOptions();
		setupCOptions();
		finalTypeTxtArea.setText(C_BASIC_PROBLEM);
	}
	
	/**
	 * Adds functionality to the comment button so that when clicked, it will add a comment to a magnet created
	 * via the magnet creater in problem creation
	 * 
	 * @param event the click event
	 */
	@UiHandler("createCommentsButton")
	void onCreateCommentClick(ClickEvent event)
	{
		commentsStagingArea.setText(commentsStagingArea.getText()+Consts.COMMENT_DELIMITER + commentsTxtBox.getText());
    }
	
	@UiHandler("createHidFunctionButton")
	void onCreateHidFunctionClick(ClickEvent event)
	{
		if(innerFunctionsTxtArea.getText() == ""){
			String warning;
			if(btnPrologBasicProblem.getValue()){
				warning = "Test Code can only be added after a starting comment has been added";
			}else{
				warning = "Hidden Functions must come after a visible function";
			}
			Notification.notify(WEStatus.STATUS_WARNING, warning);
			return;
		}
		
		// Convert hidden code to a better format for magnet problems
		String hiddenCode = hiddenFunctionsArea.getText();
		//hiddenCode = hiddenCode.replaceAll("(\r\n|\n)", "<br/>");
		hiddenCode = encodeString(hiddenCode);
		hiddenCode = Consts.HIDE_START+"<br/>"+hiddenCode+Consts.HIDE_END;
		
		// Remove the previous magnet delimiter
		String currentCode = innerFunctionsTxtArea.getText();
		currentCode = currentCode.substring(0, currentCode.length()-Consts.MAGNET_DELIMITER.length());
		
		// If the last thing in the current code is now "<!-- hideEnd -->"
		if(currentCode.substring(currentCode.length()-Consts.HIDE_END.length()).equals(Consts.HIDE_END)){
			// Removes middle <!-- hideEnd --> and <!-- hideStart -->
			currentCode = currentCode.substring(0, currentCode.length() - Consts.HIDE_END.length());
			hiddenCode = hiddenCode.substring(Consts.HIDE_START.length());
		}
		
		currentCode = currentCode+"<br>";
		
		innerFunctionsTxtArea.setText(currentCode+hiddenCode+Consts.MAGNET_DELIMITER);
		
		hiddenFunctionsArea.setText("");
	}
	
	@UiHandler("classDeclarationButton")
	void onClassDeclClick(ClickEvent event)
	{
		String newText = buildJavaString();
		// The main class should only have one magnet, and no delimiter
		String realText = newText.substring(0, newText.length()-Consts.MAGNET_DELIMITER.length());
		classDeclarationTxtArea.setText(realText);
		clearJavaLabels();
    }
	
	@UiHandler("innerFunctionsButton")
	void onInnerFunctionslClick(ClickEvent event)
	{
		String newMagnetString = buildJavaString();
		innerFunctionsTxtArea.setText(innerFunctionsTxtArea.getText()+newMagnetString);
		clearJavaLabels();
	}
	
	@UiHandler("statementsButton")
	void onStatementsClick(ClickEvent event)
	{
		String newMagnetString = buildJavaString();
		statementsTxtArea.setText(statementsTxtArea.getText()+newMagnetString);
		clearJavaLabels();
	}	
	
	@UiHandler("clearDataButton")
	void onClearDataClick(ClickEvent event)
	{
		clearJavaLabels();
	}
	
	@UiHandler("prologProcedureBtn")
	void onProcedureClick(ClickEvent event){
		String newMagnetString = buildPrologString(MagnetType.MAIN);
		innerFunctionsTxtArea.setText(innerFunctionsTxtArea.getText()+newMagnetString);
		clearPrologLabels();
	}

	@UiHandler("prologFactBtn")
	void onFactClick(ClickEvent event){
		String newMagnetString = buildPrologString(MagnetType.FACT);
		statementsTxtArea.setText(statementsTxtArea.getText()+newMagnetString);
		clearPrologLabels();
	}

	@UiHandler("prologRuleBtn")
	void onRuleClick(ClickEvent event){
		String newMagnetString = buildPrologString(MagnetType.RULE);
		statementsTxtArea.setText(statementsTxtArea.getText()+newMagnetString);
		clearPrologLabels();
	}
	
	@UiHandler("prologTermBtn")
	void onTermClick(ClickEvent event){
		String newMagnetString = buildPrologString(MagnetType.TERM);
		statementsTxtArea.setText(statementsTxtArea.getText()+newMagnetString);
		clearPrologLabels();
	}
	
	/**
	 * Builds a string from the user entered code and psuedocode from the automatic statement/function/class
	 * creator on the lower left side of the screen. 
	 * 
	 * @return a String built from the user entered label and real code text.
	 */
	private String buildJavaString(){
		boolean withPanel = false;
		
		String topLabel = "";
		if(topLabelTxtBox.getText()!=""){
			topLabel = topLabelTxtBox.getText();
			topLabel = encodeString(topLabel);
		}
		
		String topRealCode = "";
		if(topRealCodeTxtBox.getText()!=""){
			topRealCode = topRealCodeTxtBox.getText();	
			topRealCode = encodeString(topRealCode);
			// If this magnet nests.  Shouldn't be a case with topLabel and topReal
			// but then only bottomLabel, so this should work.
			if(bottomRealCodeTxtBox.getText() != ""){
				topRealCode = Consts.CODE_START+topRealCode+Consts.CODE_SPLIT;
			} else {
				topRealCode = Consts.CODE_START + topRealCode + Consts.CODE_END;
			}
		}		

		String comments = "";
		if(commentsStagingArea.getText()!=""){
			comments = commentsStagingArea.getText();
			withPanel = true;
		}
		
		String bottomLabel = "";
		if(bottomLabelTxtBox.getText()!=""){
			withPanel = true;
			bottomLabel = bottomLabelTxtBox.getText();
			bottomLabel = encodeString(bottomLabel);
		}
		
		String bottomRealCode = "";
		if(bottomRealCodeTxtBox.getText()!=""){
			withPanel = true;
			bottomRealCode = bottomRealCodeTxtBox.getText();
			bottomRealCode = encodeString(bottomRealCode);
			bottomRealCode = bottomRealCode+Consts.CODE_END;
		}
		
		if(withPanel){
			return topLabel+topRealCode+bottomRealCode+"<br/><!-- panel --><br/>"+bottomLabel+comments+Consts.MAGNET_DELIMITER;
		} else {
			return topLabel+topRealCode+bottomRealCode+Consts.MAGNET_DELIMITER;
		}
	
	}
	
	private String buildPrologString(MagnetType type) {
		String label = "";
		if(prologLabelTxtBox.getText()!=""){
			label = prologLabelTxtBox.getText();
			label = encodeString(label);
		}
		
		String realCode = "";
		if(prologRealTxtBox.getText()!=""){
			realCode = prologRealTxtBox.getText();	
			realCode = encodeString(realCode);
			realCode = Consts.CODE_START + realCode + Consts.CODE_END;
		}	
		
		switch(type){
			case FACT: return label+"."+realCode+Consts.MAGNET_DELIMITER;
			case RULE: return label+realCode+" :- <br/><!-- panel --><br/>."+Consts.MAGNET_DELIMITER;
			case TERM: return label+realCode+Consts.MAGNET_DELIMITER;
			case MAIN: return "% "+label+realCode+"<br/><!-- panel --><br/>"+Consts.MAGNET_DELIMITER;
			default: return label+realCode+Consts.MAGNET_DELIMITER;
		}
		
	}

	
	@UiHandler("testProblemButton")
	void onTestProblemClick(ClickEvent event){
		MagnetProblem problem;
		if(finalTypeTxtArea.getText().equals(ADVANCED_PROBLEM)){	
			
			String[] limits = new String[7];
			limits[0] = forAllowed.getText();
			limits[1] = whileAllowed.getText();
			limits[2] = ifAllowed.getText();
			limits[3] = elseIfAllowed.getText();
			limits[4] = elseAllowed.getText();
			limits[5] = returnAllowed.getText();
			limits[6] = assignmentAllowed.getText();
			
			for(int i= 0; i < limits.length; i++){
				if(limits[i] == null || limits[i].equals("")){
					limits[i] = "0";
				}
			}
			
			StringBuilder limit = new StringBuilder();
			
			for(int i=0; i < limits.length; i++){
				limit.append(limits[i]);
				if(i < limits.length -1){
					limit.append(",");
				}
			}
			
		  // Advanced problem. Load all the fields;
	      problem = new MagnetProblem(-1, finalTitleTxtBox.getText(), finalDescriptionTxtArea.getText(), 
				finalTypeTxtArea.getText(), classDeclarationTxtArea.getText(), innerFunctionsTxtArea.getText().split(".:\\|:."), forLoop1TextArea.getText().split(".:\\|:."), forLoop2TextArea.getText().split(".:\\|:."), forLoop3TextArea.getText().split(".:\\|:."), ifsTextArea.getText().split(".:\\|:."), whilesTextArea.getText().split(".:\\|:."),
				returnsTextArea.getText().split(".:\\|:."), assignmentsVarTextArea.getText().split(".:\\|:."), assignmentsValTextArea.getText().split(".:\\|:."), statementsTxtArea.getText().split(".:\\|:."), limit.toString(), new String[0], statementsTxtArea.getText().split(".:\\|:.").length+innerFunctionsTxtArea.getText().split(".:\\|:.").length, "", "");
		} else {
			
			// Basic Problem. Some fields don't exist so only grab what we need.
			String emptyString = "";
			String[] emptyArr = new String[0];
			problem = new MagnetProblem(-1, finalTitleTxtBox.getText(), finalDescriptionTxtArea.getText(), 
					finalTypeTxtArea.getText(), classDeclarationTxtArea.getText(), innerFunctionsTxtArea.getText().split(".:\\|:."), emptyArr,emptyArr, emptyArr, emptyArr, emptyArr, emptyArr, emptyArr, emptyArr,
					statementsTxtArea.getText().split(".:\\|:."), emptyString, emptyArr, statementsTxtArea.getText().split(".:\\|:.").length+innerFunctionsTxtArea.getText().split(".:\\|:.").length, "", "");
		}
		
		//TODO FIX SETTING UP TEST PROBLEMS
		//adminPage.setUpTestProblem( this.magnetProblemCreator.makeProblem( problem ));
	}
		
	/**
	 * Encodes a string into escaped HTML
	 * 
	 * @author Dakota Murray
	 * 
	 * @param text the string to encode
	 * @return a string with all necessary characters encoded
	 */
	private String encodeString(String text){
		return SafeHtmlUtils.htmlEscape(text);
	}
	
	/**
	 * Clears the contents of all of the label boxes
	 */
	public void clearJavaLabels(){
		topLabelTxtBox.setText("");
		topRealCodeTxtBox.setText("");
		bottomLabelTxtBox.setText("");
		bottomRealCodeTxtBox.setText("");
		commentsStagingArea.setText("");
		commentsTxtBox.setText("");
	}
	
	public void clearPrologLabels(){
		prologRealTxtBox.setText("");
		prologLabelTxtBox.setText("");
	}
	
	public void update(){
		AbstractServerCall magnetCmd = new GetMagnetGroupsCommand(lstLoadGroup, null, null, null, null);
		magnetCmd.sendRequest();
		AbstractServerCall cmd = new GetMagnetsByGroupCommand("Arrays/ArrayLists", null, null, null, lstLoadExercise);
		cmd.sendRequest();
	}


	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = (ProblemCreationPanelPresenter) presenter;
	}


	@Override
	public boolean hasPresenter() {
		return presenter != null;
	}


	@Override
	public Presenter getPresenter() {
		return presenter;
	}


	@Override
	public FormPanel problemCreateFormPanel() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public FormPanel fileParseFormPanel() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public FormPanel downloadMagnetFilesForm() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextBox titleTxtBox() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextBox topLabelTxtBox() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextBox topRealCodeTxtBox() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextBox commentsTxtBox() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextBox bottomLabelTxtBox() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextBox bottomRealCodeTxtBox() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextBox forAllowed() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextBox whileAllowed() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextBox ifAllowed() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextBox elseAllowed() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextBox elseIfAllowed() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextBox returnAllowed() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextBox assignmentAllowed() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextBox lastProblemLoadedTxtBox() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextBox lastProblemLoadedDownloadTxtBox() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextBox prologLabelTxtBox() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextBox prologRealTxtBox() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextArea finalTitleTxtBox() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextArea descriptionTxtArea() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextArea finalDescriptionTxtArea() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextArea finalTypeTxtArea() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextArea classDeclarationTxtArea() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextArea innerFunctionsTxtArea() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextArea statementsTxtArea() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextArea commentsStagingArea() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextArea hiddenFunctionsArea() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextArea forLoop1TextArea() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextArea forLoop2TextArea() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextArea forLoop3TextArea() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextArea whilesTextArea() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextArea ifsTextArea() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextArea returnsTextArea() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextArea assignmentsVarTextArea() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TextArea assignmentsValTextArea() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public VerticalPanel magnetMakerOptions() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public VerticalPanel magnetReviewPanel() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public VerticalPanel numberAllowedReviewPanel() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public VerticalPanel javaMagnetMaker() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public VerticalPanel prologMagnetMaker() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public SubmitButton createProblemSubmitButton() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public SubmitButton fileParseSbt() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public SubmitButton downloadMagnetFilesButton() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Button createCommentsButton() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Button classDeclarationButton() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Button innerFunctionsButton() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Button statementsButton() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Button clearDataButton() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Button createHidFunctionButton() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Button btnLoadExercise() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Button btnDeleteExercise() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Button btnMoreHelpers() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Button testProblemButton() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Button prologFactBtn() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Button prologRuleBtn() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Button prologTermBtn() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Button prologProcedureBtn() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public RadioButton btnBasicProblem() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public RadioButton btnAdvancedProblem() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public RadioButton btnPrologBasicProblem() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public RadioButton btnCBasicProblem() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public FileUpload solutionUpload() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public FileUpload helperUpload() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Label uploadStamp() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Label helperStamp() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ListBox lstGroup() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ListBox lstLoadGroup() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ListBox lstLoadExercise() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Label lblGroup() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Label forLoop1Label() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Label forLoop2Label() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Label forLoop3Label() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Label whileLabel() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Label ifLabel() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Label returnLabel() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Label assignmentVarLabel() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Label assignmentValLabel() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Label testClassLabel() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Label helperClassLabel() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Label classDeclarationTxtAreaLabel() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Label innerFunctionsTxtAreaLabel() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Label statementsTxtAreaLabel() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Label hiddenFunctionsLabel() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public VerticalPanel vtPanelHelper() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public CheckBox overwrite() {
		// TODO Auto-generated method stub
		return null;
	}
}

