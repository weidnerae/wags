package webEditor.views.interfaces;

import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

import webEditor.Common.View;

public interface SectionTabView extends View{

	public FormPanel formAddSection();
	public FormPanel formChangeSection();
	public TextBox txtAdminName();
	public TextBox txtGuestName();
	public TextBox txtSectName();
	public PasswordTextBox checkPassword();
	public PasswordTextBox check2Password();
	public ListBox lstCurSections();
	public Label lblCurrentSection();
}
