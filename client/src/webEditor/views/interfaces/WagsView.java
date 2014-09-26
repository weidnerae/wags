package webEditor.views.interfaces;

import webEditor.Common.View;

import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.UIObject;

public interface WagsView extends View {
	UIObject getHomeOutAnchor();   //Test 9-22-14
	UIObject getHomeAnchor();
	UIObject getProblemsAnchor();
	UIObject getAdminAnchor();
	UIObject getLogoutAnchor();
	UIObject getUserAnchor();
	Panel getCenterPanel();
	DockLayoutPanel getDock();
	FlowPanel getContentPanel();
}
