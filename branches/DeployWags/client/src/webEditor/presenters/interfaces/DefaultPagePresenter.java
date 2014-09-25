package webEditor.presenters.interfaces;

import com.google.gwt.event.dom.client.KeyPressEvent;

import webEditor.Common.Presenter;

public interface DefaultPagePresenter extends Presenter {
	public void onEditorClick();
	public void onLogicalClick();
	public void onMagnetClick();
	public void onDatabaseClick();
	public void onLogicalPCClick();
	public void onMagnetPCClick();
	public void onAdminClick();
	public void onDatabasePCClick();
	public void onLogoutClick();
	void onLoginClick();
	void onKeyPressForUsername(KeyPressEvent event);
	void onKeyPressForPassword(KeyPressEvent event);
}
