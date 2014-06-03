package webEditor;

import webEditor.ProxyFramework.AbstractCommand;
import webEditor.ProxyFramework.LogoutCommand;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class generates the landing page for the WAGS web site. The user it taken here
 * if he is logged in and is provided with a list of buttons taking them else-where in
 * the site. 
 */

public class DefaultPage extends Composite {

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
	@UiField Button logoutButton;
	
	/** Basic constructor. creates the splash page and will load the admin, magnet
	 *  problem creation, and logical problem creation buttons if the user has admin
	 *  privileges.
	 */
	public DefaultPage() {
		initWidget(uiBinder.createAndBindUi(this));
		Proxy.isAdmin(adminButton, magnetPCButton, logicalPCButton,databasePCButton);
	}
	
	/** Takes the user to the editor tab */
	@UiHandler("editorButton")
	void onEditorClick(ClickEvent event)
	{
		Wags e = Wags.getWagsInstance(Wags.EditorPageStr);
		e.go();
		
	}
	
	/** Takes te user to the logical problems page */
	@UiHandler("logicalButton")
	void onLogicalClick(ClickEvent event)
	{
		Wags e = Wags.getWagsInstance(Wags.DstPageStr);
		e.go();
	}
	
	/** Takes the user to the magnet problems page */
	@UiHandler("magnetButton")
	void onMagnetClick(ClickEvent event)
	{
		Wags e = Wags.getWagsInstance(Wags.MagnetPageStr);
		e.go();
	}
	
	/** Takes the user to the database problems page */
	@UiHandler("databaseButton")
	void onDatabaseClick(ClickEvent event)
	{
		Wags e = Wags.getWagsInstance(Wags.DatabasePageStr);
		e.go();
	}
	
	/** Takes the user to the admin panel */
	@UiHandler("adminButton")
	void onAdminClick(ClickEvent event)
	{
		Wags e = Wags.getWagsInstance(Wags.AdminPageStr);
		e.go();
	}
	
	/** Takes to user to the magnet problem creation */
	@UiHandler("magnetPCButton")
	void onMagnetPCClick(ClickEvent event)
	{
		Wags e = Wags.getWagsInstance(Wags.MagnetProblemCreationPageStr);
		e.go();
		
	}
	
	/** Takes the user to the logical problem creation */
	@UiHandler("logicalPCButton")
	void onLogicalPCClick(ClickEvent event)
	{
		Wags e = Wags.getWagsInstance(Wags.LogicalProblemCreationPageStr);
		e.go();
	}
	
	/** Logs the user out upon clicking the logout button*/
	@UiHandler("logoutButton")
	void onLogoutClick(ClickEvent event)
	{
		AbstractCommand cmd = new LogoutCommand();
		cmd.sendRequest();
	}
}
