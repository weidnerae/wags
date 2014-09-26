package webEditor.views.interfaces;

import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;

import webEditor.Common.View;

public interface ProblemPageView extends View {

	public ComplexPanel getMagnetPanel();
	public ComplexPanel getLogicalPanel();
	public ComplexPanel getDatabasePanel();
	
	public Button getMagnetCategory();
	public Button getLogicalCategory();
	public Button getDatabaseCategory();
}