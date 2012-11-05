
package webEditor.client.view;

import webEditor.client.MagnetProblem;
import webEditor.client.Proxy;
import webEditor.client.WEStatus;
import webEditor.magnet.client.RefrigeratorMagnet;
import webEditor.magnet.client.SplashPage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
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
	@UiField Anchor logout;
	@UiField Label hello;

	private SplashPage splashPage;
	private Editor editor;
	
	private String startingPlace;
	
	/**
	 * Constructor
	 * 
	 * -Builds Wags interface once logged in
	 */
	public Wags(String startingPlace)
	{
		initWidget(uiBinder.createAndBindUi(this));
		Proxy.getUsersName(hello, Editor, DST, Magnets, startingPlace);
		Proxy.checkPassword(this);
		Proxy.checkMultiUser(this);
		
		this.startingPlace = startingPlace;
		splashPage = new SplashPage(this);
		splashPage.getElement().getStyle().setOverflowY(Overflow.AUTO);
		editor = new Editor();
		
		// Load the correct initial page
		if (startingPlace.equals("magnets")) {
			Magnets.setVisible(true);
			replaceCenterContent(splashPage);
			History.newItem("?loc=magnets");
		} else if (startingPlace.equals("dst")) {
			Proxy.buildDST(this);
			History.newItem("?loc=dst");
		} else {
			replaceCenterContent(editor);
			History.newItem("?loc=editor");
		}
		
		//Make back/forward buttons work.
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String url = event.getValue();
				if (url.endsWith("editor")) {
					Editor.fireEvent(new ClickEvent() { });
				} else if (url.endsWith("dst")) {
					DST.fireEvent(new ClickEvent() { });
				} else if (url.endsWith("magnets")) {
					Magnets.fireEvent(new ClickEvent() { });
				}
			}
			
		});
	} // end constructor

	@UiHandler("Editor")
	void onEditorClick(ClickEvent event)
	{
		replaceCenterContent(editor);
		
		if (!Window.Location.getHref().endsWith("editor")) {
			History.newItem("?loc=editor");
		}
	}
	@UiHandler("DST")
	void onDSTClick(ClickEvent event)
	{
		Proxy.buildDST(this);
		
		if (!Window.Location.getHref().endsWith("dst")) {
			History.newItem("?loc=dst");
		}
	}
	@UiHandler("Magnets")
	void onMagnetsClick(ClickEvent event)
	{
		// Have to construct a new splashpage in case the administrator
		// has changed available exercises
		splashPage = new SplashPage(this);
		splashPage.getElement().getStyle().setOverflowY(Overflow.AUTO);
		replaceCenterContent(splashPage);
		splashPage.getWidget(0).setVisible(true);
		
		if (!Window.Location.getHref().endsWith("magnets")) {
			History.newItem("?loc=magnets");
		}
	}

	/**
	 * Logout!	
	 */
	@UiHandler("logout")
	void onLogoutClick(ClickEvent event)
	{
		Proxy.logout();
	}
	

	public void assignPartner(final String exercise){
		final DialogBox pickPartner = new DialogBox(false);
		final ListBox partners = new ListBox();
		Button close = new Button("Set Partner");
		
		HorizontalPanel DialogBoxContents = new HorizontalPanel();
		pickPartner.setText("Choose a partner for exercise: " + exercise);
		Proxy.getUsernames(partners);
		
		close.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				pickPartner.hide();
				Proxy.assignPartner(exercise, partners.getValue(partners.getSelectedIndex()));
			}
		});
		
		DialogBoxContents.add(partners);
		DialogBoxContents.add(close);
		pickPartner.add(DialogBoxContents);
		
		pickPartner.center();
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
				dock.add(w);
			}
		}
	}
	
	public void replaceCenterContent(Widget w){
		for(int i=0;i<dock.getWidgetCount();i++){
			if(dock.getWidgetDirection(dock.getWidget(i))==DockLayoutPanel.Direction.CENTER){
					dock.remove(i);
			}
			dock.add(w);
		}
	}
	
	public void updateSplashPage(VerticalPanel problemPane){
		
		splashPage.remove(0);  // removes old problemPane ?
		splashPage.add(problemPane);
	}
	
	@Override
	public WEAnchor getLink()
	{
		return new WEAnchor("Wags", this, startingPlace);
	}

}
