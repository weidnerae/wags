package webEditor.views.interfaces;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;

import webEditor.Common.View;

public interface ProgrammingTabView extends View {
	
	public SubmitButton getAddButton();
	public ListBox getExercises();
	public Button getBtnAdminReview();
	public Button getBtnAddSkeletons();
	public Button getBtnMakeVisible();
	public Grid getGrdAdminReview();
	public FileUpload getTestClass();
	public FileUpload getHelperClass();
	public FileUpload getSolution(); 
	public FileUpload getSkeleton();
	public FormPanel getHelperForm();
	public FormPanel getAdminForm();
	public TextBox getOpenDate();
	public TextBox getCloseDate();
	public TextBox getFileName();

}
