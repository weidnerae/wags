package webEditor.views.interfaces;

import java.util.List;

import webEditor.Common.View;

import com.google.gwt.event.dom.client.HasClickHandlers;

public interface LoginView extends View
{
	HasClickHandlers getUsernameField();
	HasClickHandlers getPasswordField();
	HasClickHandlers getLoginButton();
	void setData(List<String> data);
}
