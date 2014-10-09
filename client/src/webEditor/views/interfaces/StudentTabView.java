package webEditor.views.interfaces;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SubmitButton;

import webEditor.Common.View;
import webEditor.admin.ReviewPanel;

public interface StudentTabView extends View{
	
	
	public SubmitButton getSbtRegister();
	public Button getBtnChgPassword();
	public FormPanel getRegisterForm();
	public FormPanel getPasswordForm();
	public ListBox getUsers();
	public ReviewPanel getStudentReviewPnl();

}
