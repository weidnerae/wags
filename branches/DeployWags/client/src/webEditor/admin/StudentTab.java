package webEditor.admin;

import webEditor.Notification;
import webEditor.Proxy;
import webEditor.WEStatus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class StudentTab extends Composite {

	private static StudentTabUiBinder uiBinder = GWT
			.create(StudentTabUiBinder.class);

	interface StudentTabUiBinder extends UiBinder<Widget, StudentTab> {
	}
	
	@UiField SubmitButton sbtRegister;
	@UiField Button btnChgPassword;
	@UiField FormPanel registerForm, passwordForm;
	@UiField ListBox users;

	public StudentTab() {
		initWidget(uiBinder.createAndBindUi(this));
		
		Proxy.getUsernames(users);
		
		// Set up password form
		passwordForm.setAction(Proxy.getBaseURL()+"?cmd=ChangePassword");
		passwordForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		passwordForm.setMethod(FormPanel.METHOD_POST);
		passwordForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
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
				
				users.clear();
				Proxy.getUsernames(users);
			}
		});
	}
	
	public void update(){
		Proxy.getUsernames(users);
	}

}
