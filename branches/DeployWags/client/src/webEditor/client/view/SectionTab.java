package webEditor.client.view;

import webEditor.client.Proxy;
import webEditor.client.WEStatus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class SectionTab extends Composite implements HasText {
	
	@UiField FormPanel formAddSection, formChangeSection;
	@UiField TextBox txtAdminName, txtGuestName, txtSectName;
	@UiField PasswordTextBox checkPassword, check2Password;
	@UiField ListBox lstCurSections;
	
	private static SectionTabUiBinder uiBinder = GWT
			.create(SectionTabUiBinder.class);

	interface SectionTabUiBinder extends UiBinder<Widget, SectionTab> {
	}

	public SectionTab() {
		initWidget(uiBinder.createAndBindUi(this));
		
		// Fill listbox with sections that currently exist
		Proxy.getSections(lstCurSections);
		
		// Bind "addSection" form to AddSection.php
		formAddSection.setAction(Proxy.getBaseURL() + "?cmd=AddSection");
		formAddSection.setEncoding(FormPanel.ENCODING_MULTIPART);
		formAddSection.setMethod(FormPanel.METHOD_POST);
		formAddSection.addSubmitCompleteHandler(new AddCompleteHandler());
		
		// Bind "changeSection" form to ChangeSection.php
		formChangeSection.setAction(Proxy.getBaseURL() + "?cmd=ChangeSection");
		formChangeSection.setEncoding(FormPanel.ENCODING_MULTIPART);
		formChangeSection.setMethod(FormPanel.METHOD_POST);
		formChangeSection.addSubmitCompleteHandler(new ChangeCompleteHandler());
		
		
	}
	
	// Handler for adding a new section
	// If server is successful in adding section, handler links the section and accounts
	//		together.  If unsuccessful, the handler displays a notification
	private class AddCompleteHandler implements SubmitCompleteHandler{
		@Override
		public void onSubmitComplete(SubmitCompleteEvent event) {
			// The accounts have been created, but not properly linked together at this point
			WEStatus stat = new WEStatus(event.getResults());
			
			
			if(stat.getStat() == WEStatus.STATUS_SUCCESS){
				// This call links the section and accounts together
				// linkNewSections provides its own notification of success/failure
				Proxy.linkNewSection(txtSectName.getText(), txtAdminName.getText(), txtGuestName.getText());
				
				Proxy.getSections(lstCurSections);
				
				// Clear field after successful submission
				txtAdminName.setText("");
				txtSectName.setText("");
				txtGuestName.setText("");	
				checkPassword.setText("");
				check2Password.setText("");
				
			}else{
				Notification.notify(stat.getStat(), stat.getMessage());
			}
		}
		
	}
	
	// Handler for changing a section
	// If server is successful in changing the section, it will also
	// ask the Admin page to update the exercise lists
	private class ChangeCompleteHandler implements SubmitCompleteHandler{
		public void onSubmitComplete(SubmitCompleteEvent event) {
			WEStatus stat = new WEStatus(event.getResults());
			Notification.notify(stat.getStat(), stat.getMessage());
			if(stat.getStat() == WEStatus.STATUS_SUCCESS){
				Admin.updateExercises();
			}
			
		}
		
	}
	
	@Override
	public String getText() {
		return null;
	}

	@Override
	public void setText(String text) {	
	}

}
