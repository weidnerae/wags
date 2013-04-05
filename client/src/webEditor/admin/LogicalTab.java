package webEditor.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class LogicalTab extends Composite {

	private static LogicalTabUiBinder uiBinder = GWT
			.create(LogicalTabUiBinder.class);

	interface LogicalTabUiBinder extends UiBinder<Widget, LogicalTab> {
	}
	
	@UiField ButtonPanel btnPanelSubjects;
	@UiField ButtonPanel btnPanelGroups;
	@UiField CheckBoxPanel chkPanelExercises;

	public LogicalTab() {
		initWidget(uiBinder.createAndBindUi(this));
		
		String[] testButtons = {"Test Button 1", "Test Button 2", "Test Button 3",
									"Test Button 4", "Test Button 5"};
		String[] testChecks = {"Checkbox 1", "Checkbox 2", "Checkbox 3", "Checkbox 4"};
		btnPanelSubjects.setTitle("SUBJECTS");
		btnPanelSubjects.addButtons(testButtons);
		
		btnPanelGroups.setTitle("GROUPS");
		btnPanelGroups.addButtons(testButtons);
		
		chkPanelExercises.setTitle("Exercises");
		chkPanelExercises.addCheckBoxes(testChecks);
	}

}
