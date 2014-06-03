package webEditor;

import webEditor.ProxyFramework.AbstractCommand;
import webEditor.ProxyFramework.LoginCommand;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ReLoginPopupHandler {


	public static void handleLogin()
	{
		
		final DialogBox setPassword = new DialogBox(false);
		final TextBox username= new TextBox();
		final PasswordTextBox password = new PasswordTextBox();
		Label lbl1 = new Label("Enter Username: ");
		Label lbl2 = new Label("Enter Password: ");
		
		Button close = new Button("Login");
		
		VerticalPanel base = new VerticalPanel();
		HorizontalPanel line1 = new HorizontalPanel();
		HorizontalPanel line2 = new HorizontalPanel();
		
		setPassword.setText("Session Timed Out: Please Login Again");
		
		close.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (username.getText().length() < 8) {
					Notification.notify(WEStatus.STATUS_ERROR, "Username must be at least 8 characters");
					return;
				}
				if(password.getText().length() < 8){
					Notification.notify(WEStatus.STATUS_ERROR, "Password must be at least 8 characters");
					return;
				}
				
				setPassword.hide();
				AbstractCommand cmd = new LoginCommand(username.getText(), password.getText());
				cmd.sendRequest();
			}
		});
		
		line1.add(lbl1);
		line1.add(username);
		line2.add(lbl2);
		line2.add(password);
		base.add(line1);
		base.add(line2);
		base.add(close);
		setPassword.add(base);
		setPassword.center();
		password.setFocus(true);
		
	}
}
