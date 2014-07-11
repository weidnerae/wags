
package webEditor.views.concrete;

import webEditor.Common.Presenter;
import webEditor.presenters.interfaces.WagsPresenter;
import webEditor.views.interfaces.WagsView;

import com.github.gwtbootstrap.client.ui.Container;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;


public class Wags extends Composite implements WagsView
{

	private static EditorUiBinder uiBinder = GWT.create(EditorUiBinder.class);

	interface EditorUiBinder extends UiBinder<Widget, Wags>{}
	
	@UiField DockLayoutPanel dock;
	//@UiField Panel north;
	@UiField Panel north;
	@UiField Panel center;
	@UiField Label Home;
	@UiField UIObject Editor;
	@UiField UIObject DST;
	@UiField UIObject Magnets;
	@UiField UIObject Database;
	@UiField UIObject AdminPage;
	@UiField UIObject logout;
	@UiField UIObject logicalProblemManagement;
	@UiField UIObject logicalProblemCreation;
	@UiField UIObject magnetProblemManagement;
	@UiField UIObject magnetProblemCreation;
	@UiField UIObject progProblemManagement;
	@UiField UIObject studentManagement;
	@UiField UIObject review;
	@UiField UIObject user;
	
	private WagsPresenter presenter;
	
	/**
	 * Constructor
	 * 
	 * -Builds Wags interface once logged in
	 */
	public Wags()
	{
		initWidget(uiBinder.createAndBindUi(this));
		north.getElement().getParentElement().getStyle().setOverflow(Overflow.VISIBLE);
	}

	@UiHandler("Home")
	void onHomeClick(ClickEvent event) {
		presenter.onHomeClick();
	}
	
	@UiHandler("Editor")
	void onEditorClick(ClickEvent event) {
		presenter.onEditorClick();
	}
	
	@UiHandler("Database")
	void onDatabaseClick(ClickEvent event) {
		presenter.onDatabaseClick();
	}
	
	
	@UiHandler("DST")
	void onDSTClick(ClickEvent event) {
		presenter.onDSTClick();
	}
	
	@UiHandler("Magnets")
	void onMagnetsClick(ClickEvent event) {
		presenter.onMagnetsClick();
	}
	
	@UiHandler("AdminPage")
	void onAdminClick(ClickEvent event) {
		presenter.onAdminClick();
	}

	@UiHandler("logout") 
	void onLogoutClick(ClickEvent event) {
		presenter.onLogoutClick();
	}
	
	@UiHandler("logicalProblemManagement")
	void onLogicalManagementClick(ClickEvent event) {
		presenter.onLogicalManagementClick();
	}
	
	@UiHandler("logicalProblemCreation")
	void onLogicalCreationClick(ClickEvent event) {
		presenter.onLogicalCreationClick();
	}
	
	@UiHandler("magnetProblemManagement")
	void onMagnetManagementClick(ClickEvent event) {
		presenter.onMagnetManagementClick();
	}
	
	@UiHandler("magnetProblemCreation")
	void onMagnetCreationClick(ClickEvent event) {
		presenter.onMagnetCreationClick();
	}
	
	@UiHandler("progProblemManagement")
	void onProgrammingProblemCreation(ClickEvent event) {
		presenter.onProgrammingManagementClick();
	}
	
	@UiHandler("studentManagement")
	void onStudentManagementClick(ClickEvent event) {
		presenter.onStudentManagementClick();
	}
	
	@UiHandler("review")
	void onReviewClick(ClickEvent event) {
		presenter.onReviewClick();
	}
	
	@Override
	public UIObject getHomeAnchor() {
		return Home;
	}

	@Override
	public UIObject getEditorAnchor() {
		return Editor;
	}

	@Override
	public UIObject getMagnetsAnchor() {
		return Magnets;
	}

	@Override
	public UIObject getLogicalAnchor() {
		return DST;
	}

	@Override
	public UIObject getDatabaseAnchor() {
		return Database;
	}

	@Override
	public UIObject getAdminAnchor() {
		return AdminPage;
	}

	@Override
	public UIObject getLogoutAnchor() {
		return logout;
	}

	@Override
	public DockLayoutPanel getDock() {
		return dock;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = (WagsPresenter) presenter;
	}
	
	@Override
	public boolean hasPresenter() {
		return presenter != null;
	}

	@Override
	public Presenter getPresenter() {
		return presenter;
	}

	@Override
	public UIObject getUserAnchor() {
		return user;
	}

	@Override
	public FlowPanel getContentPanel() {
		return null;
	}

	@Override
	public Panel getCenterPanel() {
		return center;
	}
}
