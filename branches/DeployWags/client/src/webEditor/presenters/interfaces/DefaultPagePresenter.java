package webEditor.presenters.interfaces;

import com.google.gwt.event.dom.client.KeyPressEvent;

import webEditor.Common.Presenter;

public interface DefaultPagePresenter extends Presenter {
	public void onProblemsClick();
	void onLoginClick();
	void onKeyPressForUsername(KeyPressEvent event);
	void onKeyPressForPassword(KeyPressEvent event);
}
