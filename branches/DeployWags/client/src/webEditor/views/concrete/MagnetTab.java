package webEditor.views.concrete;

import java.util.HashMap;

import webEditor.ProxyFacilitator;
import webEditor.WEStatus;
import webEditor.Common.Presenter;
import webEditor.ProxyFramework.AbstractServerCall;
import webEditor.ProxyFramework.GetMMAssignedCommand;
import webEditor.ProxyFramework.GetMMExercisesCommand;
import webEditor.ProxyFramework.GetMMGroupsCommand;
import webEditor.ProxyFramework.SetMMExercisesCommand;
import webEditor.admin.AssignedPanel;
import webEditor.admin.ButtonPanel;
import webEditor.admin.CheckBoxPanel;
import webEditor.presenters.interfaces.MagnetTabPresenter;
import webEditor.views.interfaces.MagnetTabView;

import com.github.gwtbootstrap.client.ui.CheckBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class MagnetTab extends Composite implements ProxyFacilitator, MagnetTabView {

	private static MagnetTabUiBinder uiBinder = GWT
			.create(MagnetTabUiBinder.class);

	interface MagnetTabUiBinder extends UiBinder<Widget, MagnetTab> {
	}
	
	@UiField ButtonPanel btnPanelGroups;
	@UiField CheckBoxPanel chkPanelExercises;
	@UiField AssignedPanel selected;
	@UiField AssignedPanel assigned;
	@UiField ListBox listBox;
	
	private MagnetTabPresenter presenter;

	public MagnetTab() {
		initWidget(uiBinder.createAndBindUi(this));
		
		AbstractServerCall groupsCmd = new GetMMGroupsCommand(this);
		groupsCmd.sendRequest();
		AbstractServerCall assignedCmd = new GetMMAssignedCommand(this, "");
		assignedCmd.sendRequest();
		//set up button panel
		 //groups
		//set up checkbox panel
		 //exercises in each group
		chkPanelExercises.setAssignedPanel(selected);
		//set up assigned panels
		 //ones youre picking
		selected.setAssigned(false);
		selected.setPartner(assigned);
		selected.setExercises( chkPanelExercises );
		selected.setParent(this);
		 //ones already selected
		assigned.setAssigned(true);
		assigned.setPartner(selected);
		assigned.setParent(this);
		addGroupClickHandlers();
		
		 listBox.addChangeHandler(new ChangeHandler()
		 {
		  public void onChange(ChangeEvent event)
		  {
			  int selectedIndex = listBox.getSelectedIndex();
			  if (selectedIndex > -1) 
			  {
				  btnPanelGroups.myButtons.get(selectedIndex).click();
		      }
		  }
		 });
	}
	
	@Override
	public void handleSubjects(String[] subjects) {	}

	@Override
	public void handleGroups(String[] groups) {
		btnPanelGroups.addButtons(groups);
		
			for (int i = 0; i < groups.length; i++)
			{
				listBox.addItem("" + groups[i]);
			}
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
			if(exercises.length>1 && !exercises[i].equals("none")){
				exerciseList += exercises[i] + ",";
			}
		}
		
		if(exerciseList.length() > 0)  // a comma was added
			exerciseList = exerciseList.substring(0, exerciseList.length()-1);
		
		AbstractServerCall cmd = new SetMMExercisesCommand(exerciseList, this);
		cmd.sendRequest();
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
			AbstractServerCall exerciseCmd = new GetMMExercisesCommand(title, pf);
			exerciseCmd.sendRequest();
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
		AbstractServerCall groupsCmd = new GetMMGroupsCommand(this);
		groupsCmd.sendRequest();
		AbstractServerCall assignedCmd = new GetMMAssignedCommand(this, "");
		assignedCmd.sendRequest();
		addGroupClickHandlers();
	}


	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = (MagnetTabPresenter) presenter;
	}

	@Override
	public boolean hasPresenter() {
		return presenter != null;
	}

	@Override
	public Presenter getPresenter() {
		return presenter;
	}

	@Override
	public ButtonPanel getBtnPanelGroups() {
		return btnPanelGroups;
	}

	@Override
	public CheckBoxPanel getChkPanelExercises() {
		return chkPanelExercises;
	}

	@Override
	public AssignedPanel getSelected() {
		return selected;
	}

	@Override
	public AssignedPanel getAssigned() {
		return assigned;
	}

	
}
