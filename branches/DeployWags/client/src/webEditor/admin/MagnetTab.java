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
	
	@UiField ManyButtonPanel btnPanelGroups;
	@UiField CheckBoxPanel chkPanelExercises;
	@UiField AssignedPanel asPanel;
	@UiField AssignedPanel asAlreadyPanel;
	@UiField ReviewPanel rvPanel;

	public MagnetTab() {
		initWidget(uiBinder.createAndBindUi(this));
		
		Proxy.getMMGroups(this);
		Proxy.getMMAssigned(this);
		Proxy.getMMAssigned(this, GET_REVIEW);
		asPanel.setParent(this);
		rvPanel.setParent(this);
		
		btnPanelGroups.setTitle("GROUPS"); //groups
		chkPanelExercises.setTitle("EXERCISES"); //exercises in each group
		asPanel.setTitle("SELECTED"); //ones youre picking
		asAlreadyPanel.setTitle("ASSIGNED"); //ones already selected
		asAlreadyPanel.btnAssign.setVisible(false); //hide button for assigned section because it is read only
		chkPanelExercises.setAssignedPanel(asPanel);

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
			asAlreadyPanel.clear();
			
			for(int i = 0; i < exercises.length; i++){
				asAlreadyPanel.add(exercises[i]);
			}
		}
		
		rvPanel.setCurrent(exercises);
	}

	@Override
	public void getCallback(String[] exercises, WEStatus status, String args) {
		if(args.equals("")){
			HashMap<String, CheckBox> chkBoxes = chkPanelExercises.getAssignments();
			for(int i = 0; i < exercises.length; i++){
				asAlreadyPanel.add(exercises[i]);
				asPanel.add(exercises[i]);
				
				// Handles checking assigned exercises
				if(chkBoxes.containsKey(exercises[i])){
					chkBoxes.get(exercises[i]).setValue(true);
				} else {
					CheckBox tmpCheck = new CheckBox(exercises[i]);
					chkPanelExercises.addClickHandler(tmpCheck);
					tmpCheck.setValue(true);
					chkBoxes.put(exercises[i], tmpCheck);
				}
			}
			rvPanel.setCurrent(exercises);
		} else if (args.equals(GET_REVIEW)){
			rvPanel.setReview(exercises);
		}
		
		
	}

	@Override
	public void reviewExercise(String exercise) {
		Proxy.reviewExercise(exercise, MAGNET, this);		
	}

	@Override
	public void reviewCallback(String[] data) {
		rvPanel.fillGrid(data);
	}

	public void update(){
		Proxy.getMMGroups(this);
		Proxy.getMMAssigned(this);
		Proxy.getMMAssigned(this, GET_REVIEW);
		
		addGroupClickHandlers();
	}
	
}
