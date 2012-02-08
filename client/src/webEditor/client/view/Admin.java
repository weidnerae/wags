package webEditor.client.view;

import java.util.HashMap;

import webEditor.client.Proxy;
import webEditor.client.WEStatus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
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

	private HashMap<String, String> exerciseMap = new HashMap<String, String>();
	
	@UiField FileUpload solution;
	@UiField FileUpload skeleton;
	@UiField SubmitButton addButton;
	@UiField FormPanel adminForm;
	@UiField TextBox fileName;
	@UiField ListBox exercises;
	@UiField ListBox logicalExercises;
	@UiField Button btnDSTReview;
	@UiField Button btnAdminReview;
	//@UiField Button btnAddSkeletons;
	@UiField Button btnMakeVisible;
	//@UiField Button btnEnablePartners;
	@UiField Grid grdAdminReview;
	@UiField Grid grdDSTReview;
	@UiField FileUpload testClass;
	@UiField FileUpload helperClass;
	@UiField FormPanel helperForm;
	@UiField FormPanel DSTForm;
	@UiField TextBox openDate;
	@UiField TextBox closeDate;
	
	private static AdminUiBinder uiBinder = GWT.create(AdminUiBinder.class);

	interface AdminUiBinder extends UiBinder<Widget, Admin> {
	}

	public Admin() {
		initWidget(uiBinder.createAndBindUi(this));
        
		//Fill in exercise listbox
		Proxy.getVisibleExercises(exercises, exerciseMap); 
		Proxy.getLogicalExercises(logicalExercises);
		
		//Handle the Add Exercise Form
		adminForm.setAction(Proxy.getBaseURL() + "?cmd=AddExercise");
		adminForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		adminForm.setMethod(FormPanel.METHOD_POST);
			
		adminForm.addSubmitCompleteHandler(new SubmitCompleteHandler(){
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				WEStatus stat = new WEStatus(event.getResults());
				
				Notification.notify(stat.getStat(), stat.getMessage());
				Proxy.getVisibleExercises(exercises, exerciseMap); 
				
				if(stat.getStat() == WEStatus.STATUS_SUCCESS){
					String exName = stat.getMessage().substring(stat.getMessage().lastIndexOf(" ")+1);
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
				if(event.getResults() != "Class Uploaded") status = WEStatus.STATUS_ERROR; 
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
	}
	
	@UiHandler("btnAdminReview")
	void onReviewClick(ClickEvent event)
	{
		grdAdminReview.clear(true);
		String value = exercises.getValue(exercises.getSelectedIndex());
		Proxy.getSubmissionInfo(Integer.parseInt(exerciseMap.get(value)), grdAdminReview);		
	}
	
	@UiHandler("btnDSTReview")
	void onDSTReviewClick(ClickEvent event)
	{
		Proxy.getDSTSubmissions(logicalExercises.getValue(logicalExercises.getSelectedIndex()), grdDSTReview);
	}
	
//	@UiHandler("btnAddSkeletons")
//	void onSkelClick(ClickEvent event){
//		String value = exercises.getValue(exercises.getSelectedIndex());
//		Proxy.alterExercise(Integer.parseInt(exerciseMap.get(value)), "skel");
//	}
	
	@UiHandler("btnMakeVisible")
	void onVisClick(ClickEvent event){
		String value = exercises.getValue(exercises.getSelectedIndex());
		Proxy.alterExercise(Integer.parseInt(exerciseMap.get(value)), "vis");
		Proxy.getVisibleExercises(exercises, exerciseMap);
	}
	
//	@UiHandler("btnEnablePartners")
//	void onPartnerClick(ClickEvent event){
//		String value = exercises.getValue(exercises.getSelectedIndex());
//		Proxy.alterExercise(Integer.parseInt(exerciseMap.get(value)), "partner");
//	}
	
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
		
		deleteExercise.setText("ARE YOU SURE?");
				
		delete.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				deleteExercise.hide();
				String value = exercises.getValue(exercises.getSelectedIndex());
				Proxy.deleteExercise(exerciseMap.get(value), exercises, exerciseMap);
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

}
