package webEditor.views.interfaces;

import webEditor.Common.View;
import webEditor.admin.AssignedPanel;
import webEditor.admin.ButtonPanel;
import webEditor.admin.CheckBoxPanel;

public interface LogicalTabView extends View  {

	ButtonPanel btnPanelSubjects();
	ButtonPanel btnPanelGroups();
	CheckBoxPanel chkPanelExercises();
	AssignedPanel assigned(); 
	AssignedPanel selected();
}
