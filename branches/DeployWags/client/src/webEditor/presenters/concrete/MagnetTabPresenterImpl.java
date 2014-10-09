package webEditor.presenters.concrete;

import java.util.HashMap;
import java.util.List;

import com.github.gwtbootstrap.client.ui.CheckBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;

import webEditor.Proxy;
import webEditor.ProxyFacilitator;
import webEditor.Receiver;
import webEditor.WEStatus;
import webEditor.ProxyFramework.AbstractServerCall;
import webEditor.ProxyFramework.GetMMAssignedCommand;
import webEditor.ProxyFramework.GetMMExercisesCommand;
import webEditor.ProxyFramework.GetMMGroupsCommand;
import webEditor.ProxyFramework.SetMMExercisesCommand;
import webEditor.presenters.interfaces.MagnetTabPresenter;
import webEditor.views.concrete.MagnetTab;

public class MagnetTabPresenterImpl implements ProxyFacilitator, Receiver, MagnetTabPresenter {

	private MagnetTab magnetTab;
	private boolean bound;
	
	public MagnetTabPresenterImpl(MagnetTab view) {
		this.magnetTab = view;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(magnetTab.asWidget());
	}

	@Override
	public void bind() {
		magnetTab.setPresenter(this);
		bound = true;
	}

	@Override
	public boolean bound() {
		return bound;
	}

	@Override
	public void update(List<String> data) {

		AbstractServerCall getMMgroups = new GetMMGroupsCommand(this);
		getMMgroups.sendRequest();
		
		AbstractServerCall getMMAssigned = new GetMMAssignedCommand(this, null);
		getMMAssigned.sendRequest();
		
		addGroupClickHandlers();
	}

	@Override
	public void handleGroups(String[] groups) {
		magnetTab.getBtnPanelGroups().addButtons(groups);
		addGroupClickHandlers();
		magnetTab.getBtnPanelGroups().myButtons.get(0).click();
	}
	
	//-------------------------------
	// Group panel click handling
	//-------------------------------
	public void addGroupClickHandlers(){
		for(int i = 0; i < magnetTab.getBtnPanelGroups().myButtons.size(); i++){
			Button tmpBtn = magnetTab.getBtnPanelGroups().myButtons.get(i);
			tmpBtn.addClickHandler(new groupClickHandler(tmpBtn.getText(), (ProxyFacilitator) this));
		}
		magnetTab.getBtnPanelGroups().setClickHandlers();
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
			AbstractServerCall getMMExercises = new GetMMExercisesCommand(title, pf);
			getMMExercises.sendRequest();
		}
		
	}

	@Override
	public void handleExercises(String[] exercises) {
		magnetTab.getChkPanelExercises().addCheckBoxes(exercises);	
	}

	@Override
	public void setExercises(String[] exercises) {
		String exerciseList = "";
		for(int i = 0; i < exercises.length; i++){
			if(exercises.length>1 && !exercises[i].equals("none")){
				exerciseList += exercises[i] + ",";
			}
		}
		
		if(exerciseList.length() > 0)  // a comma was added
			exerciseList = exerciseList.substring(0, exerciseList.length()-1);
		
		AbstractServerCall setMMExercises = new SetMMExercisesCommand(exerciseList, (ProxyFacilitator) this);
		setMMExercises.sendRequest();
	}

	@Override
	public void setCallback(String[] exercises, WEStatus status) {
		if(status.getStat() == WEStatus.STATUS_SUCCESS){
			magnetTab.getAssigned().clear();
			
			for(int i = 0; i < exercises.length; i++){
				magnetTab.getAssigned().add(exercises[i]);
			}
		}		
	}

	@Override
	public void getCallback(String[] exercises, WEStatus status, String args) {
		if(args.equals("")){
			HashMap<String, CheckBox> chkBoxes = magnetTab.getChkPanelExercises().getAssignments();
			for(int i = 0; i < exercises.length; i++){
				magnetTab.getAssigned().add(exercises[i]);
				
				CheckBox tmpCheck = new CheckBox(exercises[i]);
				magnetTab.getChkPanelExercises().addClickHandler(tmpCheck);
				tmpCheck.setValue(true);
				chkBoxes.put(exercises[i], tmpCheck);
			}
		}		
	}

	@Override
	public void handleSubjects(String[] subjects) {
		// TODO Auto-generated method stub
		
	}

}
