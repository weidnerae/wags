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
		tabPanel.add(w, tabTitle);
		w.setSize("100%", "100%");
	}
}
