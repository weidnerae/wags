package webEditor.views.interfaces;

import webEditor.Common.View;
import webEditor.admin.AssignedPanel;
import webEditor.admin.ButtonPanel;
import webEditor.admin.CheckBoxPanel;

public interface MagnetTabView extends View {

	public ButtonPanel getBtnPanelGroups();
	public CheckBoxPanel getChkPanelExercises();
	public AssignedPanel getSelected();
	public AssignedPanel getAssigned();
}
