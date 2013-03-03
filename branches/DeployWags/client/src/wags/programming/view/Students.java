package wags.programming.view;

import wags.Notification;
import wags.Proxy;
import wags.WEStatus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;


public class Students extends Composite {
	@UiField PasswordTextBox newPassword;
	@UiField static ListBox users;
	@UiField SubmitButton btnChgPassword;
	@UiField PasswordTextBox checkPassword;
	@UiField FormPanel passwordForm;
	@UiField FormPanel registerForm;
	@UiField FileUpload csvReg;

	private static StudentsUiBinder uiBinder = GWT
			.create(StudentsUiBinder.class);

	interface StudentsUiBinder extends UiBinder<Widget, Students> {
	}

	public Students() {
		initWidget(uiBinder.createAndBindUi(this));
		Proxy.getUsernames(users);
		
		
		//Handle the Password Form
		passwordForm.setAction(Proxy.getBaseURL()+"?cmd=ChangePassword");
		passwordForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		passwordForm.setMethod(FormPanel.METHOD_POST);
		
		passwordForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				WEStatus status = new WEStatus(event.getResults());
				
				Notification.notify(status.getStat(), status.getMessage());
				Proxy.getUsernames(users);
			}
		});
		
		//Handle the registration form
		registerForm.setAction(Proxy.getBaseURL()+"?cmd=RegisterStudents");
		registerForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		registerForm.setMethod(FormPanel.METHOD_POST);
		
		registerForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				WEStatus status = new WEStatus(event.getResults());
				
				Notification.notify(status.getStat(), status.getMessage());
			}
		});
		
	}
	
	public static void updateStudents(){
		Proxy.getUsernames(users);
	}

}
