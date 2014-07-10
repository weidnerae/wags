package webEditor.presenters.concrete;

import java.util.List;

import webEditor.Common.ClientFactory;
import webEditor.Common.Tokens;
import webEditor.ProxyFramework.AbstractServerCall;
import webEditor.ProxyFramework.LogoutCommand;
import webEditor.presenters.interfaces.DefaultPagePresenter;
import webEditor.views.interfaces.DefaultPageView;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

public class DefaultPagePresenterImpl implements DefaultPagePresenter, AcceptsOneWidget {
	
	private static String TRUE = "TRUE";
	private DefaultPageView def;
	private boolean bound = false;
	
	public DefaultPagePresenterImpl(final DefaultPageView view)
	{
		def = view;
		ClientFactory.getAppModel().registerObserver(this);
		bind();
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(def.asWidget());
	}

	@Override
	public void bind() {
		def.setPresenter(this);
		bound = true;
	}

	@Override
	public boolean bound() {
		return bound;
	}

	@Override
	public void update(List<String> data) {
		boolean isAdmin = data.get(1).equals(TRUE);
		def.getLogicalPCButton().setVisible(isAdmin);
		def.getMagnetPCButton().setVisible(isAdmin);
		def.getAdminButton().setVisible(isAdmin);
		def.getDatabasePCButton().setVisible(isAdmin);
	}

	@Override
	public void onEditorClick() {
		History.newItem(Tokens.EDITOR);
	}

	@Override
	public void onLogicalClick() {
		History.newItem(Tokens.DST);
	}

	@Override
	public void onMagnetClick() {
		History.newItem(Tokens.MAGNET);
	}

	@Override
	public void onDatabaseClick() {
		History.newItem(Tokens.DEFAULT);
	}

	@Override
	public void onLogicalPCClick() {
		History.newItem(Tokens.ADMIN + Tokens.DELIM + Tokens.LOGICALCREATION);
	}

	@Override
	public void onMagnetPCClick() {
		History.newItem(Tokens.ADMIN + Tokens.DELIM + Tokens.MAGNETCREATION);	
	}

	@Override
	public void onAdminClick() {
		History.newItem(Tokens.ADMIN);
	}

	@Override
	public void onDatabasePCClick() {
		History.newItem(Tokens.DEFAULT);
	}

	@Override
	public void onLogoutClick() {
		AbstractServerCall cmd = new LogoutCommand();
		cmd.sendRequest();
	}

	@Override
	public void setWidget(IsWidget w) {
		// TODO Auto-generated method stub
		
	}

}
