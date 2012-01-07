package webEditor.client.view;

import webEditor.client.Proxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


/**
 * LoginView
 *
 * Show user the login form.
 * They will see user name text box, password text box, a login button,
 * and a link to the registration form. 
 *
 * @author Robert Bost <bostrt@appstate.edu>
 *
 */
public class Login extends View
{

	private static LoginUiBinder uiBinder = GWT.create(LoginUiBinder.class);

	interface LoginUiBinder extends UiBinder<Widget, Login>{}

	@UiField TextBox username;
	@UiField PasswordTextBox password;
	@UiField Button loginButton;
	@UiField Button dstLoginButton;

	public Login()
	{
		initWidget(uiBinder.createAndBindUi(this));
		loginButton.setText("Programming");
		dstLoginButton.setText("Logical");
	}

	@UiHandler("loginButton")
	void onClick(ClickEvent event)
	{
		Proxy.login(username.getText(), password.getText(), "editor");
	}
	
	@UiHandler("dstLoginButton")
	void onDSTClick(ClickEvent event)
	{
		this.setVisible(false);
		Proxy.login(username.getText(), password.getText(), "dst");
	}

	@Override
	public WEAnchor getLink()
	{
		return new WEAnchor("Login", this, "login");
	}

}
