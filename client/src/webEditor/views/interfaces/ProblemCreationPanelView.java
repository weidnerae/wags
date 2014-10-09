package webEditor.views.interfaces;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import webEditor.Common.View;

public interface ProblemCreationPanelView extends View{

 public FormPanel problemCreateFormPanel(); 
 public FormPanel fileParseFormPanel();
 public FormPanel downloadMagnetFilesForm();
 
 public TextBox titleTxtBox(); 
 public TextBox topLabelTxtBox(); 
 public TextBox topRealCodeTxtBox(); 
 public TextBox commentsTxtBox(); 
 public TextBox bottomLabelTxtBox(); 
 public TextBox bottomRealCodeTxtBox(); 
 public TextBox forAllowed(); 
 public TextBox whileAllowed(); 
 public TextBox ifAllowed(); 
 public TextBox elseAllowed(); 
 public TextBox elseIfAllowed(); 
 public TextBox returnAllowed(); 
 public TextBox assignmentAllowed(); 
 public TextBox lastProblemLoadedTxtBox(); 
 public TextBox lastProblemLoadedDownloadTxtBox(); 
 public TextBox prologLabelTxtBox(); 
 public TextBox prologRealTxtBox();
 
 public TextArea finalTitleTxtBox(); 
 public TextArea descriptionTxtArea(); 
 public TextArea finalDescriptionTxtArea(); 
 public TextArea finalTypeTxtArea(); 
 public TextArea classDeclarationTxtArea(); 
 public TextArea innerFunctionsTxtArea(); 
 public TextArea statementsTxtArea(); 
 public TextArea commentsStagingArea(); 
 public TextArea hiddenFunctionsArea(); 
 public TextArea forLoop1TextArea(); 
 public TextArea forLoop2TextArea(); 
 public TextArea forLoop3TextArea(); 
 public TextArea whilesTextArea(); 
 public TextArea ifsTextArea(); 
 public TextArea returnsTextArea(); 
 public TextArea assignmentsVarTextArea(); 
 public TextArea assignmentsValTextArea();
 
 public VerticalPanel magnetMakerOptions(); 
 public VerticalPanel magnetReviewPanel();
 public VerticalPanel numberAllowedReviewPanel(); 
 public VerticalPanel javaMagnetMaker(); 
 public VerticalPanel prologMagnetMaker();
 
 public SubmitButton createProblemSubmitButton(); 
 public SubmitButton fileParseSbt(); 
 public SubmitButton downloadMagnetFilesButton();
 
 public Button createCommentsButton(); 
 public Button classDeclarationButton(); 
 public Button innerFunctionsButton(); 
 public Button statementsButton(); 
 public Button clearDataButton(); 
 public Button createHidFunctionButton(); 
 public Button btnLoadExercise(); 
 public Button btnDeleteExercise(); 
 public Button btnMoreHelpers();
 public Button testProblemButton(); 
 public Button prologFactBtn(); 
 public Button prologRuleBtn(); 
 public Button prologTermBtn(); 
 public Button prologProcedureBtn();
 
 public RadioButton btnBasicProblem(); 
 public RadioButton btnAdvancedProblem(); 
 public RadioButton btnPrologBasicProblem(); 
 public RadioButton btnCBasicProblem();
 
 public FileUpload solutionUpload(); 
 public FileUpload helperUpload();
 
 public Label uploadStamp();
 public Label helperStamp();
 
 public ListBox lstGroup();
 public ListBox lstLoadGroup(); 
 public ListBox lstLoadExercise();
 
 public Label lblGroup(); 
 public Label forLoop1Label(); 
 public Label forLoop2Label(); 
 public Label forLoop3Label(); 
 public Label whileLabel(); 
 public Label ifLabel(); 
 public Label returnLabel(); 
 public Label assignmentVarLabel(); 
 public Label assignmentValLabel(); 
 public Label testClassLabel(); 
 public Label helperClassLabel(); 
 public Label classDeclarationTxtAreaLabel(); 
 public Label innerFunctionsTxtAreaLabel(); 
 public Label statementsTxtAreaLabel(); 
 public Label hiddenFunctionsLabel();
 
 public VerticalPanel vtPanelHelper();
 
 public CheckBox overwrite();
 
}
