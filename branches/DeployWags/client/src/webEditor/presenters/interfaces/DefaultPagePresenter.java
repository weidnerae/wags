package webEditor.presenters.interfaces;

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
}
