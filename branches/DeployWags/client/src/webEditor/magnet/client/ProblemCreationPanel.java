package webEditor.magnet.client;

import webEditor.client.Proxy;
import webEditor.client.WEStatus;
import webEditor.client.view.Notification;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
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


public class ProblemCreationPanel extends Composite{

	private static ProblemCreationPanelUiBinder uiBinder = GWT
			.create(ProblemCreationPanelUiBinder.class);

	interface ProblemCreationPanelUiBinder extends UiBinder<Widget, ProblemCreationPanel> {
	}
	
	@UiField FormPanel problemCreateFormPanel;
	@UiField TextBox titleTxtBox, topLabelTxtBox, topRealCodeTxtBox, 
		commentsTxtBox, bottomLabelTxtBox, bottomRealCodeTxtBox;
	@UiField TextArea finalTitleTxtBox, descriptionTxtArea, finalDescriptionTxtArea,
		classDeclarationTxtArea, innerFunctionsTxtArea, statementsTxtArea, commentsStagingArea,
		hiddenFunctionsArea;
	@UiField VerticalPanel magnetMakerOptions, magnetReviewPanel;
	@UiField SubmitButton createProblemSubmitButton;
	@UiField Button createCommentsButton, classDeclarationButton, innerFunctionsButton,
		statementsButton, clearDataButton, createHidFunctionButton, btnLoadExercise,
		btnDeleteExercise;
	@UiField RadioButton btnBasicProblem, btnAdvancedProblem;
	@UiField FileUpload solutionUpload, helperUpload;
	@UiField ListBox lstGroup, lstLoadGroup, lstLoadExercise;
	@UiField Label lblGroup;
	@UiField CheckBox overwrite;
	
	//Global variables that are needed so the ChangeHandler can see them
	//or because they must be added conditionally 
	//Looking for a better way to organize it so that global variables are not needed
	ListBox decisionStructures;
	HorizontalPanel input;
	Button addMMOptionButton;
		
	public ProblemCreationPanel(RefrigeratorMagnet magnet, boolean magnetAdmin){
		initWidget(uiBinder.createAndBindUi(this));
		Proxy.getMagnetGroups(lstLoadGroup);
		Proxy.getMagnetsByGroup("Arrays/ArrayLists", lstLoadExercise);		
		
		
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
					Proxy.addMagnetLinkage(stat.getMessage()); // The title of the problem
				} else if (stat.getStat() == WEStatus.STATUS_WARNING){
					Notification.notify(stat.getStat(), stat.getMessage());
					verifyOverwrite();
				} else {
					Notification.notify(stat.getStat(), stat.getMessage());
				}
			}
		});
		
		titleTxtBox.addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				finalTitleTxtBox.setText(titleTxtBox.getText());
			}
		});
		
		descriptionTxtArea.addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				finalDescriptionTxtArea.setText(descriptionTxtArea.getText());
			}
		});
		
		lstLoadGroup.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				Proxy.getMagnetsByGroup(lstLoadGroup.getItemText(lstLoadGroup.getSelectedIndex()), lstLoadExercise);
			}
		});
		
		btnLoadExercise.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Proxy.getMagnetProblemForEdit(finalTitleTxtBox, finalDescriptionTxtArea, classDeclarationTxtArea, 
						innerFunctionsTxtArea, statementsTxtArea, lstLoadExercise.getItemText(lstLoadExercise.getSelectedIndex()));
			}
		});
		
		btnDeleteExercise.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String title = lstLoadExercise.getItemText(lstLoadExercise.getSelectedIndex());
				verifyDelete(title);
			}
		});
		
	}	
	
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
				Proxy.deleteMagnetExercise(title);
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
	
	void clearMagnetMakerOptions()
	{
		magnetMakerOptions.clear();
		magnetMakerOptions.setStyleName("");  //clear CSS
	}
	
	void setupMagnetMakerOptions()
	{
		//first make title label and add it in
		Label title = new Label("Magnet Maker Options");
		magnetMakerOptions.add(title);
		magnetMakerOptions.setStyleName("problem_creation_magnetmaker");
		
		HorizontalPanel options = new HorizontalPanel();
		
		//decision structure dropdown for options
		AbsolutePanel dropdown = new AbsolutePanel();
		decisionStructures = new ListBox();
		decisionStructures.addItem("choose type of decision structure...");
		decisionStructures.addItem("if");
		decisionStructures.addItem("for");
		decisionStructures.addItem("while");
		decisionStructures.addItem("else");
		decisionStructures.addItem("else if");
		decisionStructures.addChangeHandler(new StructuresHandler());
		dropdown.add(decisionStructures);  //handler will create input panel
		dropdown.setStyleName("problem_creation_mm_dropdown");
		options.add(dropdown);
		
		input = new HorizontalPanel();
		input.setStyleName("problem_creation_mm_input");
		
		options.add(input);
		
		magnetMakerOptions.add(options);
		
		addMMOptionButton = new Button("Add");
		addMMOptionButton.setStyleName("problem_creation_float_right");
		magnetMakerOptions.add(addMMOptionButton);
		
	}
	
	private class StructuresHandler implements ChangeHandler{
		@Override
		public void onChange(ChangeEvent event) {
			int index = decisionStructures.getSelectedIndex();
			String value = decisionStructures.getValue(index);
        	if(value.equals("choose type of decision structure..."))  // Default Text
        	{
        		//clear the input panel
        		input.clear();
        	} else if(value.equals("while"))
        	{
        		//set up input panel for while conditions
       			input.clear();
       			input.add(new Label("while ( "));
       			input.add(new TextBox());
       			input.add(new Label(" ) {}"));
        	} else if(value.equals("for")) {
        		//set up input panel for for loop arguments
        		input.clear();
        		input.add(new Label("for ("));
        		input.add(new TextBox());
        		input.add(new Label(" ; "));
        		input.add(new TextBox());
        		input.add(new Label(" ; "));
        		input.add(new TextBox());
        		input.add(new Label(" ) "));
        	} else if(value.equals("if"))
        	{
        		//set up input panel for if conditions
       			input.clear();
       			input.add(new Label("if ( "));
       			input.add(new TextBox());
       			input.add(new Label(" ) {}"));
        	} else if(value.equals("else"))
        	{
        		//set up input panel for else magnet
       			input.clear();
       			input.add(new Label("else { "));
       			input.add(new TextBox());
       			input.add(new Label(" }"));
        	} else if(value.equals("else if"))
        	{
        		//set up input panel for else if conditions
       			input.clear();
       			input.add(new Label("else if ( "));
       			input.add(new TextBox());
       			input.add(new Label(" ) {}"));
        	}
		}
	}

	
	@UiHandler("btnBasicProblem")
	void onBasicProblemClick(ValueChangeEvent<Boolean> event)
	{
		clearMagnetMakerOptions();
	}
	
	@UiHandler("btnAdvancedProblem")
	void onAdvancedProblemClick(ValueChangeEvent<Boolean> event)
	{
		setupMagnetMakerOptions();
	}
	
	@UiHandler("createCommentsButton")
	void onCreateCommentClick(ClickEvent event)
	{
		commentsStagingArea.setText(commentsStagingArea.getText()+Consts.COMMENT_DELIMITER + "\\\\" + commentsTxtBox.getText());
    }
	
	@UiHandler("createHidFunctionButton")
	void onCreateHidFunctionClick(ClickEvent event)
	{
		if(innerFunctionsTxtArea.getText() == ""){
			Notification.notify(WEStatus.STATUS_WARNING, "Hidden Functions must come after a visible function");
			return;
		}
		
		// Convert hidden code to a better format for magnet problems
		String hiddenCode = hiddenFunctionsArea.getText();
		hiddenCode = hiddenCode.replaceAll("(\r\n|\n)", "<br>");
		hiddenCode = removeAngleBrackets(hiddenCode);
		hiddenCode = Consts.HIDE_START+"<br>"+hiddenCode+Consts.HIDE_END;
		
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
		String newText = buildString();
		// The main class should only have one magnet, and no delimiter
		String realText = newText.substring(0, newText.length()-Consts.MAGNET_DELIMITER.length());
		classDeclarationTxtArea.setText(realText);
		clearLabels();
    }
	
	@UiHandler("innerFunctionsButton")
	void onInnerFunctionslClick(ClickEvent event)
	{
		String newMagnetString = buildString();
		innerFunctionsTxtArea.setText(innerFunctionsTxtArea.getText()+newMagnetString);
		clearLabels();
	}
	
	@UiHandler("statementsButton")
	void onStatementsClick(ClickEvent event)
	{
		String newMagnetString = buildString();
		statementsTxtArea.setText(statementsTxtArea.getText()+newMagnetString);
		clearLabels();
	}	
	
	@UiHandler("clearDataButton")
	void onClearDataClick(ClickEvent event)
	{
		clearLabels();
    }
	
	private String buildString(){
		boolean withPanel = false;
		
		String topLabel = "";
		if(topLabelTxtBox.getText()!=""){
			topLabel = topLabelTxtBox.getText();
			topLabel = removeAngleBrackets(topLabel);
		}
		
		String topRealCode = "";
		if(topRealCodeTxtBox.getText()!=""){
			topRealCode = topRealCodeTxtBox.getText();
			topRealCode = removeAngleBrackets(topRealCode);
			
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
			bottomLabel = removeAngleBrackets(bottomLabel);
		}
		
		String bottomRealCode = "";
		if(bottomRealCodeTxtBox.getText()!=""){
			withPanel = true;
			bottomRealCode = bottomRealCodeTxtBox.getText();
			bottomRealCode = removeAngleBrackets(bottomRealCode);
			bottomRealCode = bottomRealCode+Consts.CODE_END;
		}
		
		
		if(withPanel){
			return topLabel+topRealCode+bottomRealCode+"<br/><!-- panel --><br/>"+bottomLabel+comments+Consts.MAGNET_DELIMITER;
		} else {
			return topLabel+topRealCode+bottomRealCode+Consts.MAGNET_DELIMITER;
		}
	
	}
	
	private String removeAngleBrackets(String text){
		text = text.replaceAll("<", "&lt;");
		text = text.replaceAll(">", "&gt;");
		return text;
	}
	
	public void clearLabels(){
		topLabelTxtBox.setText("");
		topRealCodeTxtBox.setText("");
		
		bottomLabelTxtBox.setText("");
		bottomRealCodeTxtBox.setText("");
		commentsStagingArea.setText("");
		commentsTxtBox.setText("");
	}
}

