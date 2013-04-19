package webEditor.admin;

import webEditor.Proxy;
import webEditor.ProxyFacilitator;
import webEditor.WEStatus;
import webEditor.admin.builders.LMBuilder;
import webEditor.admin.builders.LMBuilderFactory;
import webEditor.admin.builders.LMTraversalDisplay;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LMEditTab extends Composite implements ProxyFacilitator{

	private static LMEditTabUiBinder uiBinder = GWT
			.create(LMEditTabUiBinder.class);

	interface LMEditTabUiBinder extends UiBinder<Widget, LMEditTab> {
	}
	
	@UiField ButtonPanel btnPanelSubjects, btnPanelGroups;
	@UiField VerticalPanel vtDisplayHolder;

	public LMEditTab() {
		initWidget(uiBinder.createAndBindUi(this));
		btnPanelSubjects.setTitle("SUBJECTS");
		btnPanelGroups.setTitle("GROUPS");
		Proxy.getLMSubjects(this);
	}

	public void handleSubjects(String[] subjects) {
		this.btnPanelSubjects.addButtons(subjects);
		addSubjectClickHandlers();
		btnPanelSubjects.myButtons.get(0).click();
	}

	//Copy & Pasted from logical tab :(
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
		LMDisplay travDisplay = new LMTraversalDisplay();
		btnPanelGroups.myButtons.get(0).addClickHandler(
				new groupClickHandler(LMBuilderFactory.getTraversalBuilder(), travDisplay));
		
		for(int i = 1; i < btnPanelGroups.myButtons.size(); i++){
			Button tmpBtn = btnPanelGroups.myButtons.get(i);
			tmpBtn.addClickHandler(new vanillaHandler());
		}
		
		btnPanelGroups.setClickHandlers();
	}
	
	// Stop gap for groups that don't have LMBuilders/Displays yet
	private class vanillaHandler implements ClickHandler{
		public void onClick(ClickEvent event) {
			Window.alert("Not implemented yet!");
		}
	}
	
	private class groupClickHandler implements ClickHandler{
		LMBuilder builder;
		LMDisplay display;

		public groupClickHandler(LMBuilder builder, LMDisplay display){
			this.builder = builder;
			this.display = display;
			
		}
		@Override
		public void onClick(ClickEvent event) {
			display.setBuilder(builder);
			setCurrentDisplay(display);
		}
	}
	
	public void handleGroups(String[] groups) {
		btnPanelGroups.addButtons(groups);
		addGroupClickHandlers();
		btnPanelGroups.myButtons.get(0).click();
	}
	
	private void setCurrentDisplay(LMDisplay display){
		vtDisplayHolder.clear();
		vtDisplayHolder.add(display);
	}

	public void handleExercises(String[] exercises) {}
	public void setExercises(String[] exercises) {}
	public void setCallback(String[] exercises, WEStatus status) {}
	public void getCallback(String[] exercises, WEStatus status, String request) {}
	public void reviewExercise(String exercise) {}
	public void reviewCallback(String[] data) {}

}
