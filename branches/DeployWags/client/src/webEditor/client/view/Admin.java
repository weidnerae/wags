package webEditor.client.view;

import java.util.ArrayList;
import java.util.Iterator;

import webEditor.client.Proxy;
import webEditor.client.WEStatus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Admin extends Composite{

	@UiField SubmitButton addButton, sbtCompReview;
	@UiField static ListBox exercises;
	@UiField static ListBox logicalExercises, magnetExercises, lstMagnetExercises;
	@UiField ListBox setLogical, lstLSubjects, lstLGroups;
	@UiField Button btnDSTReview, btnAdminReview, btnAddSkeletons, btnMakeVisible, btnMagnetReview, btnMagnet;
	@UiField Grid grdAdminReview, grdDSTReview, grdMagnetReview;
	@UiField FileUpload testClass, helperClass, solution, skeleton;
	@UiField FormPanel helperForm, DSTForm, adminForm, formCompReview;
	@UiField TextBox openDate, closeDate, fileName;
	@UiField CheckBox Traversals, InsertNodes, BuildBST, BuildBT, RadixSort,
		MaxHeapInsert, MaxHeapDelete, MinHeapInsert, MinHeapDelete, MaxHeapBuild,
		MinHeapBuild, HeapSort, Kruskal, Prim;
	@UiField
	static VerticalPanel magnetSelectionPanel, lmPanel;
	
	private static AdminUiBinder uiBinder = GWT.create(AdminUiBinder.class);
	private static ArrayList<CheckBox> currentMagnets = new ArrayList<CheckBox>();
	private static ArrayList<CheckBox> currentLogicals = new ArrayList<CheckBox>();
	private static java.util.HashMap<String, CheckBox> allMagnets = new java.util.HashMap<String, CheckBox>();
	private static java.util.HashMap<String, CheckBox> allLogicals = new java.util.HashMap<String, CheckBox>();

	interface AdminUiBinder extends UiBinder<Widget, Admin> {
	}
	
	public Admin() {
		initWidget(uiBinder.createAndBindUi(this));
        
		//Fill in exercise listboxes
		Proxy.getVisibleExercises(exercises); 
		Proxy.getLogicalExercises(logicalExercises);
		Proxy.getLogicalForAssignment(lstLSubjects, lstLGroups, lmPanel, currentLogicals, allLogicals);
		// I can't believe all the currentMagnets, allMagnets juggling works....
		Proxy.getMagnetGroups(magnetExercises, magnetSelectionPanel, currentMagnets, allMagnets, lstMagnetExercises);
								
		
		//Handle the Add Exercise Form
		adminForm.setAction(Proxy.getBaseURL() + "?cmd=AddExercise");
		adminForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		adminForm.setMethod(FormPanel.METHOD_POST);
			
		adminForm.addSubmitCompleteHandler(new SubmitCompleteHandler(){
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				WEStatus stat = new WEStatus(event.getResults());
				
				Notification.notify(stat.getStat(), stat.getMessage());
				Proxy.getVisibleExercises(exercises); 
				
				if(stat.getStat() == WEStatus.STATUS_SUCCESS){
					// Message is of the form: 'Uploaded Exercise [exercise title]'
					// So, exercise, exercise title begins at index 18
					String exName = stat.getMessage().substring(18);
					Proxy.addSkeletons(exName);
				}
				
			}
		});
		
		//Handle the Actions on Exercises Form
		helperForm.setAction(Proxy.getBaseURL() + "?cmd=AddHelperClass");
		helperForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		helperForm.setMethod(FormPanel.METHOD_POST);
		
		helperForm.addSubmitCompleteHandler(new SubmitCompleteHandler(){
			
			//didn't want to create a whole proxy call, so added this instead
			public void onSubmitComplete(SubmitCompleteEvent event) {
				int status = WEStatus.STATUS_SUCCESS;
				if(event.getResults() != "Upload Successful") status = WEStatus.STATUS_ERROR; 
				Notification.notify(status, event.getResults());
			}
		});
		
		DSTForm.setAction(Proxy.getBaseURL()+"?cmd=SetLogicalExercises");
		DSTForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		DSTForm.setMethod(FormPanel.METHOD_POST);
		
		DSTForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				WEStatus stat = new WEStatus(event.getResults());
				Notification.notify(stat.getStat(), stat.getMessage());
				Proxy.getLogicalExercises(logicalExercises);
			}
		});
		
		// Decides which logical microlabs to display
		setLogical.addChangeHandler(new LogicalMicroHandler());
		lstLSubjects.addChangeHandler(new LogicalSubjectHandler());
		lstLGroups.addChangeHandler(new LogicalGroupHandler());
		// Decides which magnet microlabs to display
		magnetExercises.addChangeHandler(new MagnetMicroHandler());
		btnMagnet.addClickHandler(new MagnetClickHandler());
		
	
		// Set up for CSV review
		formCompReview.setAction(Proxy.getBaseURL()+"?cmd=ComprehensiveReview");
		formCompReview.setEncoding(FormPanel.ENCODING_MULTIPART);
		formCompReview.setMethod(FormPanel.METHOD_POST);
		
		// Initialize microlab choosing (show BST exercises)
		initializeMicros();
		
	}
	
	private class MagnetClickHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			String assignedMagnets = "";
			
			// Construct the string to be passed to the server
			Iterator<String> it = allMagnets.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				if(allMagnets.get(key).getValue() == true){
					assignedMagnets += key + ",";
				}
			}
			
			// Remove last comma
			assignedMagnets = assignedMagnets.substring(0, assignedMagnets.length() - 1);
			Proxy.SetMagnetExercises(assignedMagnets);
		}
		
	}
	
	private class LogicalMicroHandler implements com.google.gwt.event.dom.client.ChangeHandler{
		@Override
		public void onChange(ChangeEvent event) {
			hideAllMicros();
			
			String check = setLogical.getItemText(setLogical.getSelectedIndex());
			
			if(check.equals("BST")){
				Traversals.setVisible(true);
				InsertNodes.setVisible(true);
				BuildBST.setVisible(true);
				BuildBT.setVisible(true);
			}else if(check.equals("Heaps")){
				MaxHeapInsert.setVisible(true);
				MaxHeapDelete.setVisible(true);
				MinHeapInsert.setVisible(true);
				MinHeapDelete.setVisible(true);
				MaxHeapBuild.setVisible(true);
				MinHeapBuild.setVisible(true);
				HeapSort.setVisible(true);
			}else if(check.equals("RadixSort")){
				RadixSort.setVisible(true);				
			}else if(check.equals("MST")){
				Kruskal.setVisible(true);
				Prim.setVisible(true);
			}
			
		}
		
	}
	
	private class LogicalSubjectHandler implements com.google.gwt.event.dom.client.ChangeHandler{
		@Override
		public void onChange(ChangeEvent event) {
			Proxy.getLogicalGroups(lstLSubjects.getItemText(lstLSubjects.getSelectedIndex()), lstLGroups);
		}
	}
	
	private class LogicalGroupHandler implements com.google.gwt.event.dom.client.ChangeHandler{
		@Override
		public void onChange(ChangeEvent event) {
			Proxy.getLogicalExercises(lstLGroups.getItemText(lstLGroups.getSelectedIndex()), lmPanel, currentLogicals, allLogicals);			
		}
		
	}
	
	private class MagnetMicroHandler implements com.google.gwt.event.dom.client.ChangeHandler{

		@Override
		public void onChange(ChangeEvent event) {
			// Passes group name to proxy
			Proxy.getMagnetsByGroup(magnetExercises.getValue(magnetExercises.getSelectedIndex()), magnetSelectionPanel,
						currentMagnets, allMagnets, lstMagnetExercises);
		}
		
	}
	
	// Adds items to setLogical listbox, *will* default to showing BST microlabs
	void initializeMicros(){
		setLogical.clear();
		setLogical.addItem("BST");
		setLogical.addItem("Heaps");
		setLogical.addItem("RadixSort");
		setLogical.addItem("MST");
		
		hideAllMicros();
		
		//Set BST exercises as default visible
		Traversals.setVisible(true);
		InsertNodes.setVisible(true);
		BuildBST.setVisible(true);
		BuildBT.setVisible(true);
		
	}
	
	// Sets all logical microlabs invisible
	void hideAllMicros(){
		// Hmm... shouldn't have those capitalized.
		CheckBox[] groups = {Traversals, InsertNodes, BuildBST, BuildBT, RadixSort,
				MaxHeapInsert, MaxHeapDelete, MinHeapInsert, MinHeapDelete, MaxHeapBuild,
				MinHeapBuild, HeapSort, Kruskal, Prim};
		
		for(int i = 0; i < groups.length; i++){
			groups[i].setVisible(false);
		}
	}
	
	@UiHandler("btnAdminReview")
	void onReviewClick(ClickEvent event)
	{
		grdAdminReview.clear(true);
		Proxy.getSubmissionInfo(exercises.getValue(exercises.getSelectedIndex()), grdAdminReview);		
	}
	
	@UiHandler("btnDSTReview")
	void onDSTReviewClick(ClickEvent event)
	{
		Proxy.getDSTSubmissions(logicalExercises.getValue(logicalExercises.getSelectedIndex()), grdDSTReview, "dst");
	}
	
	@UiHandler("btnMagnetReview")
	void onMagnetReviewClick(ClickEvent event)
	{
		Proxy.getDSTSubmissions(lstMagnetExercises.getValue(lstMagnetExercises.getSelectedIndex()), grdMagnetReview, "magnet");
	}
	
	@UiHandler("btnAddSkeletons")
	void onSkelClick(ClickEvent event){
		Proxy.alterExercise(exercises.getValue(exercises.getSelectedIndex()), "skel", exercises);
	}
	
	@UiHandler("btnMakeVisible")
	void onVisClick(ClickEvent event){
		Proxy.alterExercise(exercises.getValue(exercises.getSelectedIndex()), "vis", exercises);
	}
	
	@UiHandler("btnDeleteExercise")
	void deleteExerciseClick(ClickEvent event){
		final DialogBox deleteExercise = new DialogBox(false);
		Label lbl1 = new Label("ARE YOU SURE?  This deletes ALL associated\n" +
								"files and submissions, and IS NOT RECOVERABLE.");
		
		Button delete = new Button("DELETE");
		Button nvm = new Button("Nevermind");
		
		VerticalPanel base = new VerticalPanel();
		HorizontalPanel line1 = new HorizontalPanel();
		HorizontalPanel line2 = new HorizontalPanel();
				
		deleteExercise.setText("DELETING " + exercises.getValue(exercises.getSelectedIndex()).toUpperCase());
				
		delete.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				deleteExercise.hide();
				Proxy.deleteExercise(exercises.getValue(exercises.getSelectedIndex()), exercises);
			}
		});
		
		nvm.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				deleteExercise.hide();				
			}
		});
		
		line1.add(lbl1);
		line2.add(nvm);
		line2.add(delete);
		base.add(line1);
		base.add(line2);
		deleteExercise.add(base);
		deleteExercise.center();
	}
	
	// Needed for superAdmin changing sections
	public static void updateExercises(){
		Proxy.getVisibleExercises(exercises); 
		Proxy.getLogicalExercises(logicalExercises);
		Proxy.getMagnetGroups(magnetExercises, magnetSelectionPanel, currentMagnets, allMagnets, lstMagnetExercises);
	}

}
