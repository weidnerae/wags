package webEditor.presenters.concrete;

import java.util.List;

import webEditor.Common.ClientFactory;
import webEditor.Common.Tokens;
import webEditor.ProxyFramework.AbstractServerCall;
import webEditor.ProxyFramework.LoginCommand;
import webEditor.ProxyFramework.LogoutCommand;
import webEditor.presenters.interfaces.DefaultPagePresenter;
import webEditor.views.interfaces.DefaultPageView;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PasswordTextBox;

public class DefaultPagePresenterImpl implements DefaultPagePresenter, AcceptsOneWidget {
	
	private static String TRUE = "TRUE";
	private DefaultPageView def;
	private boolean bound = false;
	
	public DefaultPagePresenterImpl(final DefaultPageView view)
	{
		def = view;
		ClientFactory.getAppModel().registerObserver(this);
		bind();
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(def.asWidget());
	}

	@Override
	public void bind() {
		def.setPresenter(this);
		bound = true;
	}

	@Override
	public boolean bound() {
		return bound;
	}

	@Override
	public void update(List<String> data) {
		boolean isAdmin = data.get(1).equals(TRUE);
		boolean isLoggedIn = data.get(0).equals(TRUE);
		def.getEditorButton().setVisible(false);
		def.getDatabaseProblemButton().setVisible(isLoggedIn);
		def.getLogicalProblemButton().setVisible(isLoggedIn);
		def.getMagnetProblemButton().setVisible(isLoggedIn);
		def.getUsernameField().setVisible(!isLoggedIn);
		def.getPasswordField().setVisible(!isLoggedIn);
		def.getLoginButton().setVisible(!isLoggedIn);
		def.getWelcomeText().setVisible(isLoggedIn);
		def.getLoginText().setVisible(!isLoggedIn);
		def.getLoginScreen().setVisible(!isLoggedIn);
	}
	
	@Override
	public void onLoginClick() {
		String username = def.getUsernameField().getText();
		String password = def.getPasswordField().getText();
		Window.alert("attempting login");
		AbstractServerCall cmd = new LoginCommand(username, password);
		cmd.sendRequest();
	}
	
	@Override
	public void onKeyPressForUsername(KeyPressEvent event) {
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)
		{
			// when enter is pressed, move to password field
			((PasswordTextBox) def.getPasswordField()).setFocus(true);
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
	public void onEditorClick() {
		History.newItem(Tokens.EDITOR);
	}

	@Override
	public void onLogicalClick() {
		History.newItem(Tokens.DST);
	}

	@Override
	public void onMagnetClick() {
		History.newItem(Tokens.MAGNET);
	}

	@Override
	public void onDatabaseClick() {
		History.newItem(Tokens.DATABASE);
	}

	@Override
	public void onLogicalPCClick() {
		History.newItem(Tokens.ADMIN + Tokens.DELIM + Tokens.LOGICALCREATION);
	}

	@Override
	public void onMagnetPCClick() {
		History.newItem(Tokens.ADMIN + Tokens.DELIM + Tokens.MAGNETCREATION);	
	}

	@Override
	public void onAdminClick() {
		History.newItem(Tokens.ADMIN);
	}

	@Override
	public void onDatabasePCClick() {
		History.newItem(Tokens.DATABASE);
	}

	@Override
	public void onLogoutClick() {
		AbstractServerCall cmd = new LogoutCommand();
		cmd.sendRequest();
	}

	@Override
	public void setWidget(IsWidget w) {
		// TODO Auto-generated method stub
		
	}

}
