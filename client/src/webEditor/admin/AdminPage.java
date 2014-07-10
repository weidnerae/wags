package webEditor.admin;

import webEditor.Notification;
import webEditor.Proxy;
import webEditor.WEStatus;
import webEditor.magnet.view.RefrigeratorMagnet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Generates the admin control page which allows user with admin rights
 * to traverse between various management and problem creation tools.
 */

public class AdminPage extends Composite {

	private static AdminPageUiBinder uiBinder = GWT
			.create(AdminPageUiBinder.class);

	interface AdminPageUiBinder extends UiBinder<Widget, AdminPage> {
	}
	
	//menu items
	@UiField
	Button logic_m_btn;	//logical problem management
	@UiField
	Button logic_c_btn;	//logical problem creation
	@UiField
	Button magnet_m_btn;	//magnet problem management
	@UiField
	Button magnet_c_btn;	//magnet problem creation
	@UiField
	Button prog_btn;		//programming management
	@UiField
	Button student_btn;	//student management
	@UiField
	Button review_btn;	//review
	
	@UiField
	Button section_btn;	//section management

	//content panels
	@UiField
	LayoutPanel layout;
	@UiField
	AbsolutePanel content;
	@UiField
	VerticalPanel menu;	//holder for menu items
	
	//global variables
	Button[] items;
	ProblemCreationPanel magnet_c;
	LogicalTab logic_m = new LogicalTab();
	MagnetTab magnet_m = new MagnetTab();
	SectionTab sections;

	public AdminPage() {
		initWidget(uiBinder.createAndBindUi(this));	
		
		//initialize global variables
		items = new Button[]{logic_m_btn, logic_c_btn, magnet_m_btn, magnet_c_btn, prog_btn, student_btn, review_btn, section_btn};
		magnet_c = new ProblemCreationPanel();
		//magnet_c.setAdminPage(this);
		//sections = new SectionTab();
		//sections.setAdmin(this);
		menu.setSpacing(0);
		//check if admin is root to make section_btn visible
		//Proxy.isAdmin(section_btn);		
	}
	
	public void add(String selection) {
		clearContent();
		switch (selection) {
		case "Logical Problem Management": {
			content.add(new LogicalTab());
			break;
		}
		case "Logical Problem Creation": {
			content.add(new LMEditTab());
			break;
		}
		case "Magnet Problem Management": {
			content.add(new MagnetTab());
			break;
		}
		case "Magnet Problem Creation": {
			content.add(magnet_c);
			break;
		}
		case "Programming Problem Management": {
			content.add(new ProgrammingTab());
			break;
		}
		case "Student Management": {
			content.add(new StudentTab());
			break;
		}
		default:
			break;
		}
	}
	
	/**
	 * MENU ITEM BUTTON HANDLERS
	 */
	/*
	@UiHandler("section_btn")
	void handleSection(ClickEvent e) {
			clearContent();
			content.add(sections);
			section_btn.setStyleName( "admin_menu_item_selected" );
	}
	
	@UiHandler("logic_m_btn")
	void handleLogicM(ClickEvent e) {
			clearContent();
			content.add(new LogicalTab());
			logic_m_btn.setStyleName( "admin_menu_item_selected" );
	}
	/*
	@UiHandler("logic_c_btn")
	void handleLogicC(ClickEvent e) {
			clearContent();
			content.add(new LMEditTab());
			logic_c_btn.setStyleName( "admin_menu_item_selected" );
	}
	
	@UiHandler("magnet_m_btn")
	void handleMagnetP(ClickEvent e) {
			clearContent();
			content.add(new MagnetTab());
			magnet_m_btn.setStyleName( "admin_menu_item_selected" );
	}
	
	@UiHandler("magnet_c_btn")
	void handleMagnetC(ClickEvent e) {
			clearContent();
			content.add(magnet_c);
			magnet_c_btn.setStyleName( "admin_menu_item_selected" );
	}
	
	@UiHandler("prog_btn")
	void handleProg(ClickEvent e) {
			clearContent();
			content.add(new ProgrammingTab());
			prog_btn.setStyleName( "admin_menu_item_selected" );
	}
	
	@UiHandler("student_btn")
	void handleStudent(ClickEvent e) {
			clearContent();
			content.add(new StudentTab());
			student_btn.setStyleName( "admin_menu_item_selected" );
	}
	
	@UiHandler("review_btn")
	void handleReview(ClickEvent e) {
			clearContent();
			content.add(new ReviewTab());
			review_btn.setStyleName( "admin_menu_item_selected" );
	}
	*/
	
	public void clearContent() {
			content.clear();
			
			for (Button btn: items) {
				btn.setStyleName("admin_menu_item");
			}
	}
	
	public void update(){
		magnet_c.update();
	}

	/**
	 * For default page/creating new WAGS objects.
	 * Used in starting place methods
	 */
	public void openLogicalPC()
	{
		clearContent();
		content.add(new LMEditTab());
		//logic_c_btn.setStyleName( "admin_menu_item_selected" );
	}

	public void openMagnetPC()
	{
		clearContent();
		content.add(magnet_c);
		//magnet_c_btn.setStyleName( "admin_menu_item_selected" );
	}
	

	/**
	 * Handling test problem stuff for Magnet Problem Creation
	 */
	public void setUpTestProblem( RefrigeratorMagnet magnet )
	{
		magnet_c.setVisible( false );
		content.add( magnet );
		
		HorizontalPanel tabWidget = new HorizontalPanel();
		InlineLabel tabCloseButton = new InlineLabel("X");
		
		tabCloseButton.addClickHandler(new CloseTabClickHandler(magnet));
		tabCloseButton.addMouseOverHandler(new MouseOverCursorHandler(tabCloseButton));
		tabCloseButton.addStyleName("problem_tab_close_button");
		tabWidget.add(tabCloseButton);
		
		tabWidget.addStyleName("format_problem_tab");
		
		magnet.tabPanel.add(new AbsolutePanel(), tabWidget);
	}
	
	public class CloseTabClickHandler implements ClickHandler{
		Widget testProblem;
		public CloseTabClickHandler(Widget testProblem){
			this.testProblem = testProblem;
		}
		@Override
		public void onClick(ClickEvent event) {
			//get rid of test problem
			content.remove(testProblem);
			magnet_c.setVisible(true);
		}
	}
	
	public class MouseOverCursorHandler implements MouseOverHandler{
		InlineLabel closeButton;
		
		public MouseOverCursorHandler(InlineLabel closeButton) {
			this.closeButton = closeButton;
		}
		
		@Override
		public void onMouseOver(MouseOverEvent event) {
			closeButton.addStyleName("cursor_to_pointer");
		}
	}
}

