package webEditor.views.concrete;


import webEditor.Notification;
import webEditor.Proxy;
import webEditor.Common.Presenter;
import webEditor.ProxyFramework.AbstractServerCall;
import webEditor.ProxyFramework.GetSectionsCommand;
import webEditor.ProxyFramework.LinkNewSectionCommand;
import webEditor.presenters.interfaces.SectionTabPresenter;
import webEditor.views.interfaces.SectionTabView;
import webEditor.WEStatus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class SectionTab extends Composite implements HasText, SectionTabView {
	
	@UiField FormPanel formAddSection, formChangeSection;
	@UiField TextBox txtAdminName, txtGuestName, txtSectName;
	@UiField PasswordTextBox checkPassword, check2Password;
	@UiField ListBox lstCurSections;
	@UiField Label lblCurrentSection;
	
	private SectionTabPresenter presenter;
	
	private static SectionTabUiBinder uiBinder = GWT
			.create(SectionTabUiBinder.class);

	interface SectionTabUiBinder extends UiBinder<Widget, SectionTab> {
	}

	public SectionTab() {
		initWidget(uiBinder.createAndBindUi(this));
		
		// Fill listbox with sections that currently exist
		AbstractServerCall sectionCmd = new GetSectionsCommand(lstCurSections);
		sectionCmd.sendRequest();
		
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
				String[] data = stat.getMessage().split(" ");
				// This call links the section and accounts together
				// linkNewSections provides its own notification of success/failure
				AbstractServerCall cmd = new LinkNewSectionCommand(data[0], data[1], data[2]);
				cmd.sendRequest();
				
				AbstractServerCall sectionsCmd = new GetSectionsCommand(lstCurSections);
				sectionsCmd.sendRequest();
				
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
				// Change current section label
				lblCurrentSection.setText(lstCurSections.getItemText(lstCurSections.getSelectedIndex()));
				
				//TODO WHAT IS THIS AND FIX IT!
				//admin.update();
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

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = (SectionTabPresenter) presenter;
	}

	@Override
	public boolean hasPresenter() {
		return presenter != null;
	}

	@Override
	public Presenter getPresenter() {
		return presenter;
	}

	@Override
	public FormPanel formAddSection() {
		return formAddSection;
	}

	@Override
	public FormPanel formChangeSection() {
		return formChangeSection;
	}

	@Override
	public TextBox txtAdminName() {
		return txtAdminName;
	}

	@Override
	public TextBox txtGuestName() {
		return txtGuestName;
	}

	@Override
	public TextBox txtSectName() {
		return txtSectName;
	}

	@Override
	public PasswordTextBox checkPassword() {
		return checkPassword;
	}

	@Override
	public PasswordTextBox check2Password() {
		return check2Password;
	}

	@Override
	public ListBox lstCurSections() {
		return lstCurSections;
	}

	@Override
	public Label lblCurrentSection() {
		return lblCurrentSection;
	}

}
