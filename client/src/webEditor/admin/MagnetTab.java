package webEditor.admin;

import java.util.HashMap;

import webEditor.Proxy;
import webEditor.ProxyFacilitator;
import webEditor.WEStatus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MagnetTab extends Composite implements ProxyFacilitator {

	private static MagnetTabUiBinder uiBinder = GWT
			.create(MagnetTabUiBinder.class);

	interface MagnetTabUiBinder extends UiBinder<Widget, MagnetTab> {
	}
	
	@UiField ButtonPanel btnPanelGroups;
	@UiField CheckBoxPanel chkPanelExercises;
	@UiField AssignedPanel selected;
	@UiField AssignedPanel assigned;

	public MagnetTab() {
		initWidget(uiBinder.createAndBindUi(this));
		
		Proxy.getMMGroups(this);
		Proxy.getMMAssigned(this);
		
		//set up button panel
		btnPanelGroups.setTitle("GROUPS"); //groups
		//set up checkbox panel
		chkPanelExercises.setTitle("EXERCISES"); //exercises in each group
		chkPanelExercises.setAssignedPanel(selected);
		//set up assigned panels
		selected.setTitle("SELECTED"); //ones youre picking
		selected.setAssigned(false);
		selected.setPartner(assigned);
		selected.setExercises( chkPanelExercises );
		selected.setParent(this);
		assigned.setTitle("ASSIGNED"); //ones already selected
		assigned.setAssigned(true);
		assigned.setPartner(selected);
		assigned.setParent(this);
		
		addGroupClickHandlers();
	}
	
	@Override
	public void handleSubjects(String[] subjects) {	}

	@Override
	public void handleGroups(String[] groups) {
		btnPanelGroups.addButtons(groups);
		addGroupClickHandlers();
		btnPanelGroups.myButtons.get(0).click();
	}

	@Override
	public void handleExercises(String[] exercises) {
		chkPanelExercises.addCheckBoxes(exercises);
	}
	
	public void setExercises(String[] exercises){
		String exerciseList = "";
		for(int i = 0; i < exercises.length; i++){
			exerciseList += exercises[i] + ",";
		}
		
		if(exerciseList.length() > 0)  // a comma was added
			exerciseList = exerciseList.substring(0, exerciseList.length()-1);
		
		Proxy.SetMMExercises(exerciseList, this);
	}

	//-------------------------------
	// Group panel click handling
	//-------------------------------
	private void addGroupClickHandlers(){
		for(int i = 0; i < btnPanelGroups.myButtons.size(); i++){
			Button tmpBtn = btnPanelGroups.myButtons.get(i);
			tmpBtn.addClickHandler(new groupClickHandler(tmpBtn.getText(), this));
		}
		btnPanelGroups.setClickHandlers();
	}
	
	private class groupClickHandler implements ClickHandler{
		String title;
		ProxyFacilitator pf;

		public groupClickHandler(String title, ProxyFacilitator pf){
			this.title = title;
			this.pf = pf;
		}
		@Override
		public void onClick(ClickEvent event) {
			Proxy.getMMExercises(title, pf);
		}
		
	}

	@Override
	public void setCallback(String[] exercises, WEStatus status) {
		if(status.getStat() == WEStatus.STATUS_SUCCESS){
			assigned.clear();
			
			for(int i = 0; i < exercises.length; i++){
				assigned.add(exercises[i]);
			}
		}
	}

	@Override
	public void getCallback(String[] exercises, WEStatus status, String args) {
		if(args.equals("")){
			HashMap<String, CheckBox> chkBoxes = chkPanelExercises.getAssignments();
			for(int i = 0; i < exercises.length; i++){
				assigned.add(exercises[i]);
				
				CheckBox tmpCheck = new CheckBox(exercises[i]);
				chkPanelExercises.addClickHandler(tmpCheck);
				tmpCheck.setValue(true);
				chkBoxes.put(exercises[i], tmpCheck);
			}
		}
	}

	public void update(){
		Proxy.getMMGroups(this);
		Proxy.getMMAssigned(this);
		
		addGroupClickHandlers();
	}

	
}
