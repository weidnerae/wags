package webEditor.admin;

import webEditor.Proxy;
import webEditor.ProxyFacilitator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MagnetTab extends Composite implements ProxyFacilitator {

	private static MagnetTabUiBinder uiBinder = GWT
			.create(MagnetTabUiBinder.class);

	interface MagnetTabUiBinder extends UiBinder<Widget, MagnetTab> {
	}
	
	@UiField ButtonPanel btnPanelGroups;
	@UiField CheckBoxPanel chkPanelExercises;
	@UiField AssignedPanel asPanel;

	public MagnetTab() {
		initWidget(uiBinder.createAndBindUi(this));
		
		Proxy.getMMGroups(this);
		Proxy.getMMExercises("Default", this);
		
		btnPanelGroups.setTitle("GROUPS");
		chkPanelExercises.setTitle("EXERCISES");
		chkPanelExercises.setAssignedPanel(asPanel);

		addGroupClickHandlers();
	}

	@Override
	public void handleSubjects(String[] subjects) {
		// TODO Auto-generated method stub
		
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
			Proxy.getMMExercises(title, pf);
		}
		
	}

	@Override
	public void setCallback(String[] exercises, int status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getCallback(String[] exercises, int status) {
		// TODO Auto-generated method stub
		
	}
	
}
