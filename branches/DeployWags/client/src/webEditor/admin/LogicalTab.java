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

public class LogicalTab extends Composite implements ProxyFacilitator{

	private static LogicalTabUiBinder uiBinder = GWT
			.create(LogicalTabUiBinder.class);

	interface LogicalTabUiBinder extends UiBinder<Widget, LogicalTab> {
	}
	
	@UiField ButtonPanel btnPanelSubjects;
	@UiField ButtonPanel btnPanelGroups;
	@UiField CheckBoxPanel chkPanelExercises;
	@UiField AssignedPanel asPanel, asAlreadyPanel;
	@UiField ReviewPanel rvPanel;

	public LogicalTab() {
		initWidget(uiBinder.createAndBindUi(this));

		// Proxy calls
		Proxy.getLMSubjects(this);
		Proxy.getLMAssigned(this);
		Proxy.getLMAssigned(this, GET_REVIEW);
		
		// Initial set up
		btnPanelSubjects.setTitle("SUBJECTS");
		btnPanelGroups.setTitle("GROUPS");
		chkPanelExercises.setTitle("EXERCISES");
		chkPanelExercises.setAssignedPanel(asPanel);
		
		asPanel.setTitle("SELECTED");
		asAlreadyPanel.setTitle("ASSIGNED");
		asAlreadyPanel.btnAssign.setVisible(false);
		
		addSubjectClickHandlers();
		addGroupClickHandlers();
		asPanel.setParent(this);
		rvPanel.setParent(this);
	}
	
	//-------------------------------
	// Subject panel click handling
	//-------------------------------
	private void addSubjectClickHandlers(){
		for(int i = 0; i < btnPanelSubjects.myButtons.size(); i++){
			Button tmpBtn = btnPanelSubjects.myButtons.get(i);
			tmpBtn.addClickHandler(new subjectClickHandler(tmpBtn.getText(), this));
		}
		
		btnPanelSubjects.setClickHandlers();
	}
	
	private class subjectClickHandler implements ClickHandler{
		private String title;
		private ProxyFacilitator pf;
		
		public subjectClickHandler(String title, ProxyFacilitator pf){
			this.title = title;
			this.pf = pf;
		}

		@Override
		public void onClick(ClickEvent event) {
			Proxy.getLMGroups(title, pf);
		}
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
			Proxy.getLMExercises(title, pf);
		}
		
	}

	//-----------------------------
	// Proxy facilitation
	//-----------------------------
	@Override
	public void handleSubjects(String[] subjects) {
		btnPanelSubjects.addButtons(subjects);
		addSubjectClickHandlers();
		btnPanelSubjects.myButtons.get(0).click();
	}

	@Override
	public void handleGroups(String[] groups) {
		btnPanelGroups.addButtons(groups);
		addGroupClickHandlers();
		// Load exercises for top group
		btnPanelGroups.myButtons.get(0).click();
	}

	@Override
	public void handleExercises(String[] exercises) {
		chkPanelExercises.addCheckBoxes(exercises);
	}
	
	public void setExercises(String[] exercises){
		String toAssign = "";
		for(int i = 0; i < exercises.length; i++){
			toAssign += exercises[i] + "|";
		}
		Proxy.SetLMExercises(toAssign, this);
		
	}

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
	public void getCallback(String[] exercises, WEStatus status, String request) {
		// Currently assigned
		if(request.equals("")){
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
		}
		
		// Review
		if(request.equals(GET_REVIEW)){
			rvPanel.setReview(exercises);
		}
	}

	@Override
	public void reviewExercise(String exercise) {
		Proxy.reviewExercise(exercise, LOGICAL, this);
	}
	
	public void reviewCallback(String[] data){
		rvPanel.fillGrid(data);
	}

}
