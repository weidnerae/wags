package webEditor.views.interfaces;

import java.util.List;

import webEditor.Common.View;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.ValueBoxBase;

public interface LoginView extends View
{
	ValueBoxBase<String> getUsernameField();
	ValueBoxBase<String> getPasswordField();
	HasClickHandlers getLoginButton();
	void setData(List<String> data);
}
