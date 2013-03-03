package wags.programming.view;

import wags.Proxy;
import wags.View;
import wags.WEAnchor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
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
	@UiField Button magnetLoginButton;

	public Login()
	{
		initWidget(uiBinder.createAndBindUi(this));
		loginButton.setText("Programming");
		dstLoginButton.setText("Logical");
		magnetLoginButton.setText("Magnet");
	}
	
	@UiHandler("username")
	void onKeyPressForUsername(KeyPressEvent event)
	{
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)
		{
			// when enter is pressed, move to password field
			password.setFocus(true);
		}
	}
	
	@UiHandler("password")
	void onKeyPressForPassword(KeyPressEvent event)
	{
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)
		{
			// when enter is pressed, sign into editor
			Proxy.login(username.getText(), password.getText(), "editor");
		}
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
	
	@UiHandler("magnetLoginButton")
	void onMagnetClick(ClickEvent event)
	{
		this.setVisible(false);
		Proxy.login(username.getText(), password.getText(), "magnet");
	}

	@Override
	public WEAnchor getLink()
	{
		return new WEAnchor("Login", this, "login");
	}

}
