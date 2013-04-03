package webEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class DefaultPage extends Composite {

	private static DefaultPageUiBinder uiBinder = GWT
			.create(DefaultPageUiBinder.class);

	interface DefaultPageUiBinder extends UiBinder<Widget, DefaultPage> {
	}

	
	@UiField Button editorButton;
	@UiField Button logicalButton;
	@UiField Button magnetButton;
	@UiField Button adminButton;
	@UiField Button logoutButton;
	
	public DefaultPage() {
		initWidget(uiBinder.createAndBindUi(this));
		Proxy.isAdmin(adminButton);
	}
	
	@UiHandler("editorButton")
	void onEditorClick(ClickEvent event)
	{
		Wags e = new Wags("editor");
		e.go();
	}
	
	@UiHandler("logicalButton")
	void onLogicalClick(ClickEvent event)
	{
		Wags e = new Wags("dst");
		e.go();
	}
	
	@UiHandler("magnetButton")
	void onMagnetClick(ClickEvent event)
	{
		Wags e = new Wags("magnets");
		e.go();
	}
	
	@UiHandler("adminButton")
	void onAdminClick(ClickEvent event)
	{
		Wags e = new Wags("admin");
		e.go();
	}
	
	@UiHandler("logoutButton")
	void onLogoutClick(ClickEvent event)
	{
		Proxy.logout();
	}
}
