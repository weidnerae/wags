package webEditor.admin;

import webEditor.Proxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
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
	
	@UiField LogicalTab logical;
	@UiField MagnetTab magnet;
	@UiField ProblemCreationPanel magnetPC;
	@UiField ProgrammingTab programming;
	@UiField ReviewTab review;
	@UiField StudentTab students;
	@UiField TabLayoutPanel tabPanel;
	@UiField LMEditTab lmEditTab;
	//@UiField HorizontalPanel testPanel;

	public AdminPage() {
		
		initWidget(uiBinder.createAndBindUi(this));	
		
		
		magnetPC.setAdminPage(this);
		SectionTab sections = new SectionTab();
		sections.setAdmin(this);
		Proxy.isAdmin(tabPanel, sections);		
	}
	
	
	public void update(){
		logical.update();
		magnet.update();
		programming.update();
		students.update();
		review.update();
		magnetPC.update();
	}

	/** 
	 * Changes the tab currently being viewed in the admin control panel
	 * 
	 * @param tab an integer representing one of the possible tab selections. 
	 */
	public void setSelectedTab(int tab) {
		tabPanel.selectTab( tab );
	}
	
	public void addWidgetInNewTab(Widget w, String tabTitle){
		int currentTabIndex = tabPanel.getSelectedIndex();
		
		HorizontalPanel tabWidget = new HorizontalPanel();
		
		tabWidget.add(new InlineLabel(tabTitle + " "));
		InlineLabel tabCloseButton = new InlineLabel("X");
		
		tabCloseButton.addClickHandler(new CloseTabClickHandler(w, currentTabIndex, tabPanel));
		tabCloseButton.addMouseOverHandler(new MouseOverCursorHandler(tabCloseButton));
		tabCloseButton.addStyleName("problem_tab_close_button");
		tabWidget.add(tabCloseButton);
		
		tabPanel.add(w, tabWidget);
		tabWidget.getParent().addStyleName("format_problem_tab");
		tabPanel.selectTab(w);
	}
	
	public class CloseTabClickHandler implements ClickHandler{
		TabLayoutPanel tabPanel;
		Widget widgetToClose;
		int indexToOpen;
		public CloseTabClickHandler(Widget widgetToClose,int indexToOpen, TabLayoutPanel tabPanel){
			this.widgetToClose = widgetToClose;
			this.indexToOpen = indexToOpen;
			this.tabPanel = tabPanel;
		}
		@Override
		public void onClick(ClickEvent event) {
			tabPanel.selectTab(indexToOpen);
			tabPanel.remove(widgetToClose);			
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

