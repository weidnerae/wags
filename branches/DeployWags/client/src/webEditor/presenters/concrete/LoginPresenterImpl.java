package webEditor.presenters.concrete;

import java.util.List;

import webEditor.ProxyFramework.AbstractServerCall;
import webEditor.ProxyFramework.LoginCommand;
import webEditor.presenters.interfaces.LoginPresenter;
import webEditor.views.interfaces.LoginView;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.user.client.ui.HasWidgets;

public class LoginPresenterImpl implements LoginPresenter
{

	private LoginView loginPage;
	private boolean bound = false;
	
	public LoginPresenterImpl(LoginView loginPage)
	{
		this.loginPage = loginPage;
		bind();
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(loginPage.asWidget());
		
	}

	@Override
	public void bind() 
	{
		loginPage.setPresenter(this);
		bound = true;
	}

	@Override
	public void onLoginClick() {
		String username = loginPage.getUsernameField().getText();
		String password = loginPage.getPasswordField().getText();
		Window.alert("attempting login");
		AbstractServerCall cmd = new LoginCommand(username, password);
		cmd.sendRequest();
	}

	@Override
	public void onKeyPressForUsername(KeyPressEvent event) {
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)
		{
			// when enter is pressed, move to password field
			((PasswordTextBox) loginPage.getPasswordField()).setFocus(true);
		}
		
	}

	@Override
	public void onKeyPressForPassword(KeyPressEvent event) {
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)
		{	
			onLoginClick();
		}
	}

	@Override
	public boolean bound() {
		return bound;
	}

	@Override
	public void update(List<String> data) {
		// TODO Auto-generated method stub
		
	}


}
