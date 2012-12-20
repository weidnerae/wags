package webEditor.magnet.client;

import webEditor.client.Proxy;
import webEditor.client.WEStatus;
import webEditor.client.view.Notification;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;


public class ProblemCreationPanel extends Composite{

	private static ProblemCreationPanelUiBinder uiBinder = GWT
			.create(ProblemCreationPanelUiBinder.class);

	interface ProblemCreationPanelUiBinder extends UiBinder<Widget, ProblemCreationPanel> {
	}
	
	@UiField FormPanel problemCreateFormPanel;
	@UiField TextBox titleTxtBox;
	@UiField TextArea finalTitleTxtBox; //oooh, I am a bad person
	@UiField TextArea descriptionTxtArea;
	@UiField TextArea finalDescriptionTxtArea;
	
	@UiField TextArea classDeclarationTxtArea;
	@UiField TextArea innerFunctionsTxtArea;
	@UiField TextArea statementsTxtArea;
//	@UiField MagnetCreation magnetCreator;
	@UiField SubmitButton createProblemSubmitButton;
	@UiField TextBox topLabelTxtBox;
	@UiField TextBox topRealCodeTxtBox;
	@UiField TextBox topHiddenCodeTxtBox;
	@UiField TextBox commentsTxtBox;
	@UiField Button createCommentsButton;
	@UiField TextArea commentsStagingArea;
	@UiField TextBox bottomLabelTxtBox;
	@UiField TextBox bottomRealCodeTxtBox;
	@UiField TextBox bottomHiddenCodeTxtBox;
	@UiField Button classDeclarationButton;
	@UiField Button innerFunctionsButton;
	@UiField Button statementsButton;
	@UiField Button clearDataButton;
	@UiField FileUpload solutionUpload;
	@UiField FileUpload helperUpload;
	
	
	public ProblemCreationPanel(RefrigeratorMagnet magnet){
		initWidget(uiBinder.createAndBindUi(this));
		
		problemCreateFormPanel.setAction(Proxy.getBaseURL() + "?cmd=AddMagnetExercise");
		problemCreateFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		problemCreateFormPanel.setMethod(FormPanel.METHOD_POST);
		problemCreateFormPanel.addSubmitCompleteHandler(new SubmitCompleteHandler(){
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				WEStatus stat = new WEStatus(event.getResults());
				Notification.notify(stat.getStat(), stat.getMessage());
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
	
	@UiHandler("createCommentsButton")
	void onCreateCommentClick(ClickEvent event)
	{
		commentsStagingArea.setText(commentsStagingArea.getText()+Consts.COMMENT_DELIMITER + "\\" + commentsTxtBox.getText());
    }
	
	@UiHandler("classDeclarationButton")
	void onClassDeclClick(ClickEvent event)
	{
		String newMagnetString = buildString();
		classDeclarationTxtArea.setText(classDeclarationTxtArea.getText()+newMagnetString);
    }
	
	@UiHandler("innerFunctionsButton")
	void onInnerFunctionslClick(ClickEvent event)
	{
		String newMagnetString = buildString();
		innerFunctionsTxtArea.setText(innerFunctionsTxtArea.getText()+newMagnetString);
	}
	
	@UiHandler("statementsButton")
	void onStatementsClick(ClickEvent event)
	{
		String newMagnetString = buildString();
		statementsTxtArea.setText(statementsTxtArea.getText()+newMagnetString);
	}	
	
	@UiHandler("clearDataButton")
	void onClearDataClick(ClickEvent event)
	{
		topLabelTxtBox.setText("");
		topRealCodeTxtBox.setText("");
		topHiddenCodeTxtBox.setText("");
		bottomLabelTxtBox.setText("");
		bottomRealCodeTxtBox.setText("");
		bottomHiddenCodeTxtBox.setText("");
		commentsTxtBox.setText("");
		
		commentsStagingArea.setText("");
		
    }
	
	private String buildString(){

		String topLabel = "";
		if(topLabelTxtBox.getText()!=""){
			topLabel = topLabelTxtBox.getText();
		}
		
		String topRealCode = "";
		if(topRealCodeTxtBox.getText()!=""){
			topRealCode = Consts.CODE_START+topRealCodeTxtBox.getText()+Consts.CODE_SPLIT;
		}
		
		String topHiddenCode = "";
		if(topHiddenCodeTxtBox.getText()!=""){
			topHiddenCode = Consts.HIDE_START+topHiddenCodeTxtBox.getText()+Consts.HIDE_END;
		}
		

		String comments = "";
		if(commentsStagingArea.getText()!=""){
			comments = commentsStagingArea.getText();
		}
		
		String bottomLabel = "";
		if(bottomLabelTxtBox.getText()!=""){
			bottomLabel = bottomLabelTxtBox.getText();
		}
		
		String bottomRealCode = "";
		if(bottomRealCodeTxtBox.getText()!=""){
			bottomRealCode = bottomRealCodeTxtBox.getText()+Consts.CODE_END;
		}
		
		String bottomHiddenCode = "";
		if(bottomHiddenCodeTxtBox.getText()!=""){
			bottomHiddenCode = Consts.HIDE_START+bottomHiddenCodeTxtBox.getText()+Consts.HIDE_END;
		}
		
		return topLabel+topRealCode+bottomRealCode+topHiddenCode+"<br/><!-- panel --><br/>"+bottomHiddenCode+bottomLabel+comments+Consts.MAGNET_DELIMITER;
	
	}
}

