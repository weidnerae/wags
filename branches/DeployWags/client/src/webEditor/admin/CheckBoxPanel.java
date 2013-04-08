package webEditor.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CheckBoxPanel extends Composite {

	private static CheckBoxPanelUiBinder uiBinder = GWT
			.create(CheckBoxPanelUiBinder.class);

	interface CheckBoxPanelUiBinder extends UiBinder<Widget, CheckBoxPanel> {
	}
	
	@UiField Label title;
	@UiField VerticalPanel chkBoxHolder;
	@UiField Button btnUnsetAll;
	private HashMap<String, CheckBox> allExercises = new HashMap<String, CheckBox>();
	private ArrayList<CheckBox> currentExercises = new ArrayList<CheckBox>();
	private AssignedPanel assignedPanel;

	public CheckBoxPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		this.getElement().getStyle().setWidth(95, Unit.PCT); // needs to go away via CSS
		btnUnsetAll.addClickHandler(new unsetExercisesClick());
		btnUnsetAll.setText("Unset All");
	}
	
	public void setTitle(String title){
		this.title.setText(title);
	}
	
	public void setAssignedPanel(AssignedPanel panel){
		this.assignedPanel = panel;
	}
	
	public void clearCheckBoxes(){
		chkBoxHolder.clear();
	}
	
	public void addCheckBoxes(String[] chkBoxes){
		clearCheckBoxes();
		
		for(String chkBox: chkBoxes){
			// If the chkBox has been created already
			if(allExercises.containsKey(chkBox)){
				chkBoxHolder.add(allExercises.get(chkBox));
			} else {
				CheckBox tmpChk = new CheckBox(chkBox);	// create
				tmpChk.addClickHandler(new checkBoxClick(tmpChk));	// Bind to assigned panel
				allExercises.put(chkBox, tmpChk);		// record
				chkBoxHolder.add(tmpChk);				// place
			}
			
			currentExercises.add(allExercises.get(chkBox));	// in use
		}
	}
	
	// Needed for unassinging currently assigned exercises
	public HashMap<String, CheckBox> getAssignments(){
		return allExercises;
	}
	
	// Uncheck all chkboxes
	public void unsetAll(){
		CheckBox tmpChk;
		Set<String> keys = allExercises.keySet();
		Iterator<String> keysItr = keys.iterator();
		
		while(keysItr.hasNext()){
			tmpChk = allExercises.get(keysItr.next());
			tmpChk.setValue(false);
		}
	}
	
	private class unsetExercisesClick implements ClickHandler{

		// Clears assigned panel, iterates through checkboxes,
		// adds any checked ones back
		public void onClick(ClickEvent event) {
			assignedPanel.clear();
			unsetAll();
		}
		
	}
	
	// Adds or removes entry from assignedPanel when value of
	// checkbox changes
	private class checkBoxClick implements ClickHandler{
		private CheckBox myBox;
		
		public checkBoxClick(CheckBox chkBox){
			myBox = chkBox;
		}
		@Override
		public void onClick(ClickEvent event) {
			if(myBox.getValue()){
				assignedPanel.add(myBox.getText());
			} else {
				assignedPanel.remove(myBox.getText());
			}
		}
		
	}
	
	public void addClickHandler(CheckBox box){
		box.addClickHandler(new checkBoxClick(box));
	}

}
