package webEditor.admin;

import webEditor.Proxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
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
		// This is making me upset. If I use the FlowPanel then every tab except the Problem Demo tab is
		// bumped by 4-5 pixels.  If I use the HorizontalPanel then only the Problem Demo tab is bumped up.
		// All of this has something to do with the 2.0 panels being created to work only in standards mode.
		// this link has some info: http://www.gwtproject.org/doc/latest/DevGuideUiPanels.html
		// I could probably find a happy medium with the right concotion of panels but I need to take a break
		// from this and move onto something else for a bit.
		int currentTabIndex = tabPanel.getSelectedIndex();
		// With this only the new tab with be messed up
		HorizontalPanel tabWidget = new HorizontalPanel();
		// With this every tab except the new tab will be messed up.
		//FlowPanel tabWidget = new FlowPanel();
		tabWidget.add(new InlineLabel(tabTitle+" "));
		Button tabCloseButton = new Button("[X]", new CloseTabClickHandler(w, currentTabIndex, tabPanel));
		tabWidget.add(tabCloseButton);
		tabPanel.add(w, tabWidget);
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
}
