
package webEditor;

import webEditor.ProxyFramework.AbstractCommand;
import webEditor.ProxyFramework.BuildDSTCommand;
import webEditor.ProxyFramework.BuildDatabaseCommand;
import webEditor.ProxyFramework.BuildMagnetsCommand;
import webEditor.ProxyFramework.CheckPasswordCommand;
import webEditor.ProxyFramework.LogoutCommand;
import webEditor.admin.AdminPage;
import webEditor.magnet.view.Magnets;
import webEditor.magnet.view.RefrigeratorMagnet;
import webEditor.programming.view.Editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;


public class Wags extends View
{

	private static EditorUiBinder uiBinder = GWT.create(EditorUiBinder.class);

	interface EditorUiBinder extends UiBinder<Widget, Wags>{}
	
	public static final String EditorPageStr = "editor";
	public static final String MagnetPageStr = "magnets";
	public static final String DstPageStr = "dst";
	public static final String AdminPageStr = "admin";
	public static final String DatabasePageStr = "database";
	public static final String MagnetProblemCreationPageStr = "magnetpc";
	public static final String LogicalProblemCreationPageStr = "logicalpc";
	public static final String LogoutStr = "logout";
	
	@UiField DockLayoutPanel dock;
	@UiField SimplePanel north;
	@UiField Anchor Home;
	@UiField Anchor Editor;
	@UiField Anchor DST;
	@UiField Anchor Magnets;
	@UiField Anchor Database;
	@UiField Anchor AdminPage;
	@UiField Anchor logout;
	@UiField Anchor logManage;
	@UiField Anchor logCreation;
	@UiField Anchor magManage;
	@UiField Anchor magCreation;
	@UiField Anchor proManage;
	@UiField Anchor stuManage;
	@UiField Anchor review;
	//@UiField ListBox Management;
	Label hello;
	
	public Magnets splashPage;
	private Editor editor;
	private AdminPage adminPage;
	private String startingPlace;
	private static Wags wags;
	
	
	public static Wags getWagsInstance()
	{
		if (wags == null) {
			wags = new Wags("");
		}
		return wags;
	}
	
	public static Wags getWagsInstance(String startingPlace)
	{
		if (wags == null) {
			wags = new Wags(startingPlace);
		} 
		else {
			wags.setPage(startingPlace);
		}
		return wags;
	}
	/**
	 * Constructor
	 * 
	 * -Builds Wags interface once logged in
	 */
	private Wags(String startingPlace)
	{

		initWidget(uiBinder.createAndBindUi(this));
		north.getElement().getParentElement().getStyle().setOverflow(Overflow.VISIBLE);
		Proxy.getUsersName(hello, Editor, DST, Magnets, AdminPage, startingPlace);
		AbstractCommand cmd2 = new CheckPasswordCommand();
		cmd2.sendRequest();
		
		//Proxy.checkPassword(this);
		Home.setHref("");
		
		if (startingPlace.equals("")) {
			startingPlace = "default";
		} else {
			this.startingPlace = startingPlace;
		}
		editor = new Editor();
		adminPage = new AdminPage();
		
		//Make back/forward buttons work.
		createHistoryHandler();
				
		//Add items to the management listbox
		//Management.addItem("Logical Problem Management");
		//Management.addItem("Logical Problem Creation");
		//Management.addItem("Magnet Problem Management");
		//Management.addItem("Magnet Problem Creation");
		//Management.addItem("Programming Problem Management");
		//Management.addItem("Student Management");
		
		// Load the correct initial page
		setPage(startingPlace);
	}
		
	public void setPage(String page)
	{
		switch (page)
		{
			case Wags.MagnetPageStr:
				loadMagnets();
				break;
			case Wags.DstPageStr:
				loadDST();
				break;
			case Wags.EditorPageStr:
				loadEditor();
				break;
			case Wags.AdminPageStr:
				loadAdmin();
				break;
			case Wags.DatabasePageStr:
				loadDatabasePanel();
				break;
			case Wags.MagnetProblemCreationPageStr:
				loadMagnetProblemCreation();
				break;
			case Wags.LogicalProblemCreationPageStr:
				loadLogicalProblemCreation();
				break;
			case Wags.LogoutStr:
				AbstractCommand cmd = new LogoutCommand();
				cmd.sendRequest();
				break;
			default:
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
				} else if (url.endsWith(Wags.MagnetPageStr)) {
					loadMagnets();
				} else if (url.endsWith("login")) {
					Proxy.logout();
				} else if (url.endsWith("admin")) {
					loadAdmin();
				} else if (url.endsWith("magnetpc")) {
					loadMagnetProblemCreation();
				} else if (url.endsWith("logicalpc")) {
					loadLogicalProblemCreation();
				}
			}
			
		});
	}
	
	/**@UiHandler("Management")
	void onBrowserEvent(ClickEvent event) {
		//History.newItem("?loc=admin");
		loadAdmin();
		adminPage.add(Management.getItemText(Management.getSelectedIndex()));
	}*/
	
	@UiHandler("magCreation")
	void onBrowserEvent(ClickEvent event) {
		loadAdmin();
		adminPage.add("Magnet Problem Creation");
	}

	@UiHandler("Editor")
	void onEditorClick(ClickEvent event) {
		loadEditor();
	}
	
	@UiHandler("Database")
	void onDatabaseClick(ClickEvent event) {
		loadDatabasePanel();
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
		AbstractCommand cmd = new BuildDSTCommand(this);
		cmd.sendRequest();
		//Proxy.buildDST(this);
		History.newItem("?loc=dst");
	}
	
	public void loadMagnets() {
		AbstractCommand cmd = new BuildMagnetsCommand(this);
		cmd.sendRequest();
		//Proxy.buildMagnets(this);
		History.newItem("?loc=magnets");
	}
	
	public void loadDatabasePanel() {
		AbstractCommand cmd = new BuildDatabaseCommand(this);
		cmd.sendRequest();
		//Proxy.buildDatabase(this);
		History.newItem("?loc=database");
	}
	
	private void loadLogicalProblemCreation()
	{
		replaceCenterContent(adminPage);
		adminPage.openLogicalPC();
		History.newItem("?loc=logicalpc");
	}

	private void loadMagnetProblemCreation()
	{
		replaceCenterContent(adminPage);
		adminPage.openMagnetPC();
		History.newItem("?loc=magnetpc");
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
}
