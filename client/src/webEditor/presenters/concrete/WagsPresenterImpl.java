package webEditor.presenters.concrete;
import java.util.List;

import webEditor.Common.ClientFactory;
import webEditor.Common.Tokens;
import webEditor.ProxyFramework.AbstractServerCall;
import webEditor.ProxyFramework.LogoutCommand;
import webEditor.presenters.interfaces.WagsPresenter;
import webEditor.views.concrete.MagnetPage;
import webEditor.views.interfaces.WagsView;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;

public class WagsPresenterImpl implements WagsPresenter, AcceptsOneWidget
{
	private static final String TRUE = "TRUE";
	
	public MagnetPage splashPage;
	
	private WagsView wags;
	private boolean bound = false;

	public WagsPresenterImpl()
	{
		WagsView view = ClientFactory.getWagsView();
		view.setPresenter(this);
		ClientFactory.getAppModel().registerObserver(this);
	}
	
	public WagsPresenterImpl(final WagsView view)
	{
		this.wags = ClientFactory.getWagsView();
		view.setPresenter(this);
		ClientFactory.getAppModel().registerObserver(this);
	}

	@Override
	public void update(List<String> data)
	{
		boolean isLoggedIn = data.get(0).equals(TRUE);
		boolean isAdmin = data.get(1).equals(TRUE);
		
		wags.getEditorAnchor().setVisible(isLoggedIn);
		wags.getMagnetsAnchor().setVisible(isLoggedIn);
		wags.getLogicalAnchor().setVisible(isLoggedIn);
		wags.getDatabaseAnchor().setVisible(isLoggedIn);
		wags.getLogoutAnchor().setVisible(isLoggedIn);
		wags.getUserAnchor().setVisible(isLoggedIn);
		wags.getAdminAnchor().setVisible(isAdmin);
	}

	@Override
	public void go(final HasWidgets container) {
		container.clear();
		container.add(wags.asWidget());
	}

	@Override
	public void bind() {
		wags.setPresenter(this);
		bound = true;
	}
	
	@Override
	public boolean bound()
	{
		return bound;
	}

	@Override
	public void onHomeClick() {
		History.newItem(Tokens.DEFAULT);
	}

	@Override
	public void onEditorClick() {
		History.newItem(Tokens.EDITOR);
	}

	@Override
	public void onDatabaseClick() {
		History.newItem(Tokens.DATABASE);
	}

	@Override
	public void onDSTClick() {
		History.newItem(Tokens.DST);
	}

	@Override
	public void onMagnetsClick() {
		History.newItem(Tokens.MAGNET);
	}

	@Override
	public void onAdminClick() {
		History.newItem(Tokens.ADMIN);
	}

	@Override
	public void onLogoutClick() {
		AbstractServerCall cmd = new LogoutCommand();
		cmd.sendRequest();
	}
	
	@Override
	public void setWidget(IsWidget w) {
		Panel center = wags.getCenterPanel();
		center.clear();
		center.add(w);
	}

	@Override
	public void onLogicalManagementClick() {
		History.newItem(Tokens.LOGICALMANAGEMENT);
	}

	@Override
	public void onLogicalCreationClick() {
		History.newItem(Tokens.LOGICALCREATION);
	}

	@Override
	public void onMagnetManagementClick() {
		History.newItem(Tokens.MAGNETMANAGEMENT);
	}

	@Override
	public void onMagnetCreationClick() {
		History.newItem(Tokens.MAGNETCREATION);
	}

	@Override
	public void onReviewClick() {
		History.newItem(Tokens.REVIEW);
	}

	@Override
	public void onProgrammingManagementClick() {
		History.newItem(Tokens.PROGRAMMINGMANAGEMENT);
	}

	@Override
	public void onStudentManagementClick() {
		History.newItem(Tokens.MANAGESTUDENT);
	}
}
