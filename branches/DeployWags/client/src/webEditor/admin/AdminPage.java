package webEditor.admin;

import webEditor.Proxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

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
	
	public void setSelectedTab(int tab) {
		tabPanel.selectTab( tab );
	}
}
