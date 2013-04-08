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

	public LogicalTab() {
		initWidget(uiBinder.createAndBindUi(this));

		// Proxy calls
		Proxy.getLMSubjects(this);
		Proxy.getLMGroups("Binary Trees", this);
		Proxy.getLMExercises("Traversals", this);
		Proxy.getLMAssigned(this);
		
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
	}
	
	//-------------------------------
	// Subject panel click handling
	//-------------------------------
	private void addSubjectClickHandlers(){
		for(int i = 0; i < btnPanelSubjects.myButtons.size(); i++){
			Button tmpBtn = btnPanelSubjects.myButtons.get(i);
			tmpBtn.addClickHandler(new subjectClickHandler(tmpBtn.getText(), this));
		}
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
	}

	@Override
	public void handleGroups(String[] groups) {
		btnPanelGroups.addButtons(groups);
		addGroupClickHandlers();
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

	public void setCallback(String[] exercises, int status) {
		if(status == WEStatus.STATUS_SUCCESS){
			asAlreadyPanel.clear();
			
			for(int i = 0; i < exercises.length; i++){
				asAlreadyPanel.add(exercises[i]);
			}
		}
	}

	@Override
	public void getCallback(String[] exercises, int status) {
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
	}

}
