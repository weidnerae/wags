package webEditor.views.concrete;


import java.util.List;

import webEditor.Common.Presenter;
import webEditor.presenters.interfaces.LoginPresenter;
import webEditor.views.interfaces.LoginView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
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
public class Login extends Composite implements LoginView
{

	private static LoginUiBinder uiBinder = GWT.create(LoginUiBinder.class);

	interface LoginUiBinder extends UiBinder<Widget, Login>{}

	@UiField TextBox username;
	@UiField PasswordTextBox password;
	@UiField Button loginButton;
	
	private LoginPresenter presenter;
	
	public Login()
	{
		initWidget(uiBinder.createAndBindUi(this));
		loginButton.setText("Log in");
	}
	
	/**
	 * Changes the focus to the password field the the user currently
	 * has the username field selected and presses enter. 
	 * 
	 * @param event an event caused when the user presses enter
	 */
	@UiHandler("username")
	void onKeyPressForUsername(KeyPressEvent event)
	{
		presenter.onKeyPressForUsername(event);
	}
	
	/**
	 * Attempts to log the user in with provided username and password. This method
	 * is called when the user has the password field selected and then presses enter
	 * 
	 * @param event the event caused by the user pressing enter
	 */
	@UiHandler("password")
	void onKeyPressForPassword(KeyPressEvent event)
	{
		presenter.onKeyPressForPassword(event);
	}

	/** 
	 * Attempts to log the user in with provided username and password. THis method
	 * is called whenever the user clicks on the login button
	 * 
	 * @param event an event caused by the user clicking the login button
	 */
	@UiHandler("loginButton")
	void onClick(ClickEvent event)
	{
		presenter.onLoginClick();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = (LoginPresenter) presenter;
	}

	@Override
	public HasClickHandlers getUsernameField() {
		return username;
	}

	@Override
	public HasClickHandlers getPasswordField() {
		return password;
	}

	@Override
	public HasClickHandlers getLoginButton() {
		return loginButton;
	}

	@Override
	public void setData(List<String> data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasPresenter() {
		return presenter != null;
	}

	@Override
	public Presenter getPresenter() {
		return presenter;
	}
	


}
