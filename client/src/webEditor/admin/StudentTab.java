package webEditor.admin;

import webEditor.Notification;
import webEditor.Proxy;
import webEditor.Reviewer;
import webEditor.WEStatus;
import webEditor.ProxyFramework.AbstractCommand;
import webEditor.ProxyFramework.ReviewStudentCommand;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
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
	@UiField ReviewPanel studentReviewPnl;
	
	Reviewer studentReviewer;
	
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
		
		//the student review panel
		studentReviewer = new StudentReviewHandler();
		studentReviewPnl.setParent(studentReviewer);
		studentReviewPnl.setTitle( "Student Review" );
		Proxy.getUsernames(studentReviewer);
		
	}
	
	public void update(){
		Proxy.getUsernames(users);
	}


	private class StudentReviewHandler implements Reviewer {
		@Override
		public void getCallback( String[] exercises, WEStatus status, String request )
		{
			//"" is success
			if (exercises != null) { 
				studentReviewPnl.setStudents(exercises);
			} else {
				Window.alert( "exercises is null" );
			}
		}

		@Override
		public void review( String name )
		{
			AbstractCommand cmd = new ReviewStudentCommand(name, this);
			cmd.sendRequest();
			//Proxy.reviewStudent(name, this);
		}

		@Override
		public void reviewCallback( String[] list )
		{
			studentReviewPnl.fillGrid(list, true); //true because it is student review
		}
	}
	
	 /** Used by MyUiBinder to instantiate ReviewPanels */
	  @UiFactory ReviewPanel makeCricketStores() { // method name is insignificant
	    return new ReviewPanel(true);
	  }
}
