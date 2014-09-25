package webEditor.views.concrete;

import java.util.List;

import webEditor.Common.Presenter;
import webEditor.presenters.interfaces.DefaultPagePresenter;
import webEditor.views.interfaces.DefaultPageView;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Row;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class generates the landing page for the WAGS web site. The user it taken here
 * if he is logged in and is provided with a list of buttons taking them else-where in
 * the site. 
 */

public class DefaultPage extends Composite implements DefaultPageView {

	private static DefaultPageUiBinder uiBinder = GWT
			.create(DefaultPageUiBinder.class);

	interface DefaultPageUiBinder extends UiBinder<Widget, DefaultPage> {
	}
	
	@UiField ValueBoxBase<String> username;
	@UiField ValueBoxBase<String> password;
	@UiField Button loginButton;
	@UiField Button editorButton;
	@UiField Button logicalButton;
	@UiField Button magnetButton;
	@UiField Button databaseButton;
	@UiField Label welcomeText; 
	@UiField Label loginText;
	@UiField Row loginScreen;
	
	private DefaultPagePresenter presenter;
	
	/** Basic constructor. creates the splash page and will load the admin, magnet
	 *  problem creation, and logical problem creation buttons if the user has admin
	 *  privileges.
	 */
	public DefaultPage() {
		initWidget(uiBinder.createAndBindUi(this));
		//Proxy.isAdmin(adminButton, magnetPCButton, logicalPCButton,databasePCButton);
	}
	
	/** Takes the user to the editor tab */
	@UiHandler("editorButton")
	void onEditorClick(ClickEvent event)
	{
		presenter.onEditorClick();
	}
	
	/** Takes the user to the logical problems page */
	@UiHandler("logicalButton")
	void onLogicalClick(ClickEvent event)
	{
		presenter.onLogicalClick();
	}
	
	/** Takes the user to the magnet problems page */
	@UiHandler("magnetButton")
	void onMagnetClick(ClickEvent event)
	{
		presenter.onMagnetClick();
	}
	
	/** Takes the user to the database problems page */
	@UiHandler("databaseButton")
	void onDatabaseClick(ClickEvent event)
	{
		presenter.onDatabaseClick();
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
		this.presenter = (DefaultPagePresenter) presenter;
	}

	@Override
	public boolean hasPresenter() {
		return this.presenter != null;
	}

	@Override
	public Presenter getPresenter() {
		return this.presenter;
	}
	
	@Override
	public ValueBoxBase<String> getUsernameField() {
		return username;
	}
	
	@Override
	public ValueBoxBase<String> getPasswordField() {
		return password;
	}

	@Override
	public UIObject getLoginButton() {
		return loginButton;
	}
	
	@Override
	public UIObject getWelcomeText() {
		return welcomeText;
	}
	@Override
	public UIObject getLoginText() {
		return loginText;
	}
	
	@Override
	public UIObject getLoginScreen() {
		return loginScreen;
	}
	
	@Override
	public void setData(List<String> data) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public UIObject getEditorButton() {
		return editorButton;
	}

	@Override
	public UIObject getLogicalProblemButton() {
		return logicalButton;
	}

	@Override
	public UIObject getMagnetProblemButton() {
		return magnetButton;
	}

	@Override
	public UIObject getDatabaseProblemButton() {
		return databaseButton;
	}

}
