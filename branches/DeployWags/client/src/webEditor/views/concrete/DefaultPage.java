package webEditor.views.concrete;

import webEditor.Common.Presenter;
import webEditor.presenters.interfaces.DefaultPagePresenter;
import webEditor.views.interfaces.DefaultPageView;

import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.UIObject;
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

	
	@UiField Button editorButton;
	@UiField Button logicalButton;
	@UiField Button magnetButton;
	@UiField Button adminButton;
	@UiField Button databaseButton;
	@UiField Button magnetPCButton;
	@UiField Button logicalPCButton;
	@UiField Button databasePCButton;
	
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
	
	/** Takes the user to the admin panel */
	@UiHandler("adminButton")
	void onAdminClick(ClickEvent event)
	{
		presenter.onAdminClick();
	}
	
	/** Takes to user to the magnet problem creation */
	@UiHandler("magnetPCButton")
	void onMagnetPCClick(ClickEvent event)
	{
		presenter.onMagnetPCClick();
	}
	
	/** Takes the user to the logical problem creation */
	@UiHandler("logicalPCButton")
	void onLogicalPCClick(ClickEvent event)
	{
		presenter.onLogicalPCClick();
	}
	
	@UiHandler("databasePCButton")
	void onDatabasePCClick(ClickEvent event)
	{
		presenter.onDatabasePCClick();
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

	@Override
	public UIObject getLogicalPCButton() {
		return logicalPCButton;
	}

	@Override
	public UIObject getMagnetPCButton() {
		return magnetPCButton;
	}

	@Override
	public UIObject getAdminButton() {
		return adminButton;
	}

	@Override
	public UIObject getDatabasePCButton() {
		return databasePCButton;
	}
}
