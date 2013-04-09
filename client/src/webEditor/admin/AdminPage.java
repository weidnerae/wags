package webEditor.admin;

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
	@UiField TabLayoutPanel tabPanel;

	public AdminPage() {
		initWidget(uiBinder.createAndBindUi(this));		
	}
	
	public LogicalTab getLogical(){
		return logical;
	}
	
	public MagnetTab getMagnet() {
		return magnet;
	}
	
	public ProblemCreationPanel getMagnetPC() {
		return magnetPC;
	}
	
	public TabLayoutPanel getLayoutPanel() {
		return tabPanel;
	}
}
