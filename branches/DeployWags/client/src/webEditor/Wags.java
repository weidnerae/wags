
package webEditor;

import webEditor.admin.AdminPage;
import webEditor.magnet.view.Magnets;
import webEditor.magnet.view.RefrigeratorMagnet;
import webEditor.programming.view.Editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class Wags extends View
{

	private static EditorUiBinder uiBinder = GWT.create(EditorUiBinder.class);

	interface EditorUiBinder extends UiBinder<Widget, Wags>{}
	
	@UiField DockLayoutPanel dock;
	@UiField Anchor Editor;
	@UiField Anchor DST;
	@UiField Anchor Magnets;
	@UiField Anchor AdminPage;
	@UiField Anchor logout;
	@UiField Label hello;

	public Magnets splashPage;
	private Editor editor;
	private AdminPage adminPage;
	
	private String startingPlace;
	
	/**
	 * Constructor
	 * 
	 * -Builds Wags interface once logged in
	 */
	public Wags(String startingPlace)
	{
		initWidget(uiBinder.createAndBindUi(this));
		Proxy.getUsersName(hello, Editor, DST, Magnets, AdminPage, startingPlace);
		Proxy.checkPassword(this);
		Proxy.setWags(this);
		
		if (startingPlace.equals("")) {
			startingPlace = "default";
		} else {
			this.startingPlace = startingPlace;
		}
		editor = new Editor();
		adminPage = new AdminPage();
		
		//Make back/forward buttons work.
		createHistoryHandler();
		
		// Load the correct initial page
		if (startingPlace.equals("magnets")) {
			loadMagnets();
		} else if (startingPlace.equals("dst")) {
			loadDST();
		} else if (startingPlace.equals("editor")){
			loadEditor();
		} else if (startingPlace.equals("admin")){
			loadAdmin();
		} else {
			Proxy.loadDefault();
		}
	}
	
	/**
	 * This creates the ValueChangeHandler that makes clicking back/forward 
	 * in the browser work correctly.
	 */
	private void createHistoryHandler() {
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String url = event.getValue();
				if (url.endsWith("editor")) {
					loadEditor();
				} else if (url.endsWith("dst")) {
					loadDST();
				} else if (url.endsWith("magnets")) {
					loadMagnets();
				} else if (url.endsWith("login")) {
					Proxy.logout();
				} else if (url.endsWith("admin")){
					loadAdmin();
				}
			}
			
		});
	}

	@UiHandler("Editor")
	void onEditorClick(ClickEvent event) {
		loadEditor();
	}
	
	@UiHandler("DST")
	void onDSTClick(ClickEvent event) {
		loadDST();
	}
	
	@UiHandler("Magnets")
	void onMagnetsClick(ClickEvent event) {
		loadMagnets();
	}
	
	@UiHandler("AdminPage")
	void onAdminClick(ClickEvent event) {
		History.newItem("?loc=admin");
	}

	@UiHandler("logout") 
	void onLogoutClick(ClickEvent event) {
		Proxy.logout();
	}
	
	public void loadEditor() {
		replaceCenterContent(editor);
		History.newItem("?loc=editor");
	}
	
	public void loadAdmin() {
		replaceCenterContent(adminPage);
		History.newItem("?loc=admin");
	}
	
	public void loadDST() {
		Proxy.buildDST(this);
		History.newItem("?loc=dst");
	}
	
	public void loadMagnets() {
		Proxy.buildMagnets(this);
		History.newItem("?loc=magnets");
	}

	public void assignPassword(){
		final DialogBox setPassword = new DialogBox(false);
		final PasswordTextBox password = new PasswordTextBox();
		final PasswordTextBox passwordCheck = new PasswordTextBox();
		Label lbl1 = new Label("Enter password: ");
		Label lbl2 = new Label("Re-enter password: ");
		
		Button close = new Button("Set Password");
		
		VerticalPanel base = new VerticalPanel();
		HorizontalPanel line1 = new HorizontalPanel();
		HorizontalPanel line2 = new HorizontalPanel();
		
		setPassword.setText("Please change your password");
				
		close.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(!password.getText().equals(passwordCheck.getText())){
					Notification.notify(WEStatus.STATUS_ERROR, "Passwords don't match");
					return;
				}
				
				if(password.getText().length() < 8){
					Notification.notify(WEStatus.STATUS_ERROR, "Password must be at least 8 characters");
					return;
				}
				
				setPassword.hide();
				Proxy.assignPassword(password.getText());
			}
		});
		
		line1.add(lbl1);
		line1.add(password);
		line2.add(lbl2);
		line2.add(passwordCheck);
		base.add(line1);
		base.add(line2);
		base.add(close);
		setPassword.add(base);
		
		setPassword.center();
		password.setFocus(true);
	}
	
	public void placeProblem(MagnetProblem magnet){
		RefrigeratorMagnet problem = splashPage.makeProblem(magnet);
    	replaceCenterContentMagnet(problem);
	}
	
	public void replaceCenterContentMagnet(RefrigeratorMagnet w){
		for(int i=0;i<dock.getWidgetCount();i++){
			if(dock.getWidgetDirection(dock.getWidget(i))==DockLayoutPanel.Direction.CENTER){
				dock.remove(i);
			}
		}
		
		dock.add(w);
	}
	
	public void replaceCenterContent(Widget w){
		for(int i=0;i<dock.getWidgetCount();i++){
			if(dock.getWidgetDirection(dock.getWidget(i))==DockLayoutPanel.Direction.CENTER){
					dock.remove(i);
			}
		}
		
		dock.add(w);
	}
	
	@Override
	public WEAnchor getLink() {
		return new WEAnchor("Wags", this, startingPlace);
	}
	
	public AdminPage getAdmin() {
		return adminPage;
	}

	public void goToMagnetCreation() {
		this.go();
		TabLayoutPanel t = adminPage.getLayoutPanel();
		t.selectTab(4);
	}

	public void goToLogicalCreation() {
		this.go();
		TabLayoutPanel t = adminPage.getLayoutPanel();
		t.selectTab(5);
	}
}
