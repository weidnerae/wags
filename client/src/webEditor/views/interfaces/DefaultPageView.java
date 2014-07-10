package webEditor.views.interfaces;

import webEditor.Common.View;

import com.google.gwt.user.client.ui.UIObject;

public interface DefaultPageView extends View{
	public UIObject getEditorButton();
	public UIObject getLogicalProblemButton();
	public UIObject getMagnetProblemButton();
	public UIObject getDatabaseProblemButton();
	public UIObject getLogicalPCButton();
	public UIObject getMagnetPCButton();
	public UIObject getAdminButton();
	public UIObject getDatabasePCButton();
}
