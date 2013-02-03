package webEditor.magnet.client;

import webEditor.client.Proxy;
import webEditor.client.WEStatus;
import webEditor.client.view.Notification;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
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
	//	@UiField MagnetCreation magnetCreator;
	@UiField SubmitButton createProblemSubmitButton;
	@UiField Button createCommentsButton, classDeclarationButton, innerFunctionsButton,
		statementsButton, clearDataButton, createHidFunctionButton;
	@UiField FileUpload solutionUpload, helperUpload;
	@UiField ListBox lstGroup;
	@UiField Label lblGroup;
	@UiField CheckBox overwrite;
		
	public ProblemCreationPanel(RefrigeratorMagnet magnet, boolean magnetAdmin){
		initWidget(uiBinder.createAndBindUi(this));
		Proxy.getMagnetGroups(lstGroup, null, null, null, null);
		
		/*if(magnetAdmin){
			lstGroup.setEnabled(true);
			Proxy.getMagnetGroups(lstGroup, null, null, null, null);
		} else {
			lblGroup.setVisible(false);
		}*/
		
		
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
			@Override
			public void onBlur(BlurEvent event) {
				finalTitleTxtBox.setText(titleTxtBox.getText());
			}
		});
		
		descriptionTxtArea.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				finalDescriptionTxtArea.setText(descriptionTxtArea.getText());
			}
		});
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
		hiddenCode = hiddenCode.replaceAll("\t", "");
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
		}
		
		String topRealCode = "";
		if(topRealCodeTxtBox.getText()!=""){
			// If this magnet nests.  Shouldn't be a case with topLabel and topReal
			// but then only bottomLabel, so this should work.
			if(bottomRealCodeTxtBox.getText() != ""){
				topRealCode = Consts.CODE_START+topRealCodeTxtBox.getText()+Consts.CODE_SPLIT;
			} else {
				topRealCode = Consts.CODE_START + topRealCodeTxtBox.getText() + Consts.CODE_END;
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
		}
		
		String bottomRealCode = "";
		if(bottomRealCodeTxtBox.getText()!=""){
			withPanel = true;
			bottomRealCode = bottomRealCodeTxtBox.getText()+Consts.CODE_END;
		}
		
		
		if(withPanel){
			return topLabel+topRealCode+bottomRealCode+"<br/><!-- panel --><br/>"+bottomLabel+comments+Consts.MAGNET_DELIMITER;
		} else {
			return topLabel+topRealCode+bottomRealCode+Consts.MAGNET_DELIMITER;
		}
	
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

