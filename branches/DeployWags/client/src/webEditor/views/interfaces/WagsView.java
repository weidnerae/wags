package webEditor.views.interfaces;

import webEditor.Common.View;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.UIObject;

public interface WagsView extends View {
	UIObject getHomeAnchor();
	UIObject getEditorAnchor();
	UIObject getMagnetsAnchor();
	UIObject getLogicalAnchor();
	UIObject getDatabaseAnchor();
	UIObject getAdminAnchor();
	UIObject getLogoutAnchor();
	UIObject getUserAnchor();
	Panel getCenterPanel();
	DockLayoutPanel  getDock();
	FlowPanel getContentPanel();
}
