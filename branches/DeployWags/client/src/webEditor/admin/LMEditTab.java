package webEditor.admin;

import webEditor.Proxy;
import webEditor.ProxyFacilitator;
import webEditor.WEStatus;
import webEditor.admin.builders.BasicDisplay;
import webEditor.admin.builders.LMBuildBSTDisplay;
import webEditor.admin.builders.LMBuilder;
import webEditor.admin.builders.LMBuilderFactory;
import webEditor.admin.builders.LMGraphsDisplay;
import webEditor.admin.builders.LMInsertNodeDisplay;
import webEditor.admin.builders.LMQuickSortDisplay;
import webEditor.admin.builders.LMSimplePartitionDisplay;
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
	// Group panel click handling (WRONG WRONG WRONG)
	//-------------------------------
	private void addGroupClickHandlers(){
		// Have to link to name in button
		for(Button button:btnPanelGroups.myButtons){
			assignGrpBtnClickHandler(button);
		}
		
		btnPanelGroups.setClickHandlers();
	}
	
	private void assignGrpBtnClickHandler(Button button){
		if(button.getText().equals("Traversals")){
			button.addClickHandler(new checkClickHandler(
			new LMTraversalDisplay(), LMBuilderFactory.getTraversalBuilder()));
		} else if (button.getText().equals("Insert Node")){
			button.addClickHandler(new checkClickHandler(
			new LMInsertNodeDisplay(), LMBuilderFactory.getInsertNodeBuilder()));
		} else if (button.getText().equals("Kruskal")){
			button.addClickHandler(new checkClickHandler(
			new LMGraphsDisplay(), LMBuilderFactory.getGraphsBuilder()));
		} else if (button.getText().equals("Simple Partition")){
			button.addClickHandler(new checkClickHandler(
			new LMSimplePartitionDisplay(), LMBuilderFactory.getSimplePartitionBuilder()));
		} else if (button.getText().equals("Build BST")){
			button.addClickHandler(new checkClickHandler(
			new LMBuildBSTDisplay(), LMBuilderFactory.getBuildBSTBuilder()));
		} else {
			button.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Window.alert("Not implemented yet!");
				}
			});
		}
	}
	
	private class checkClickHandler implements ClickHandler{
		BasicDisplay display;
		LMBuilder builder;
		
		public checkClickHandler(BasicDisplay display, LMBuilder builder){
			this.display = display;
			this.builder = builder;
		}
		
		public void onClick(ClickEvent event){
			display.load(vtDisplayHolder, builder);
		}
	}
	
	public void handleGroups(String[] groups) {
		btnPanelGroups.addButtons(groups);
		addGroupClickHandlers();
		btnPanelGroups.myButtons.get(0).click();
	}

	public void handleExercises(String[] exercises) {}
	public void setExercises(String[] exercises) {}
	public void setCallback(String[] exercises, WEStatus status) {}
	public void getCallback(String[] exercises, WEStatus status, String request) {}
	public void reviewExercise(String exercise) {}
	public void reviewCallback(String[] data) {}

}
