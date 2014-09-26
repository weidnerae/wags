package webEditor.presenters.interfaces;

import webEditor.Common.Presenter;

import com.google.gwt.event.dom.client.KeyPressEvent;

public interface LoginPresenter extends Presenter 
{
	void onLoginClick();
	void onKeyPressForUsername(KeyPressEvent event);
	void onKeyPressForPassword(KeyPressEvent event);
}
