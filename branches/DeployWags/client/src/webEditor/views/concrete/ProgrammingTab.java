package webEditor.views.concrete;

import webEditor.Notification;
import webEditor.Proxy;
import webEditor.WEStatus;
import webEditor.Common.Presenter;
import webEditor.ProxyFramework.AbstractServerCall;
import webEditor.ProxyFramework.AddSkeletonsCommand;
import webEditor.ProxyFramework.AlterExerciseCommand;
import webEditor.ProxyFramework.DeleteExerciseCommand;
import webEditor.ProxyFramework.GetSubmissionInfoCommand;
import webEditor.ProxyFramework.GetVisibleExercisesCommand;
import webEditor.presenters.interfaces.ProgrammingTabPresenter;
import webEditor.views.interfaces.ProgrammingTabView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class ProgrammingTab extends Composite implements ProgrammingTabView {

	private static ProgrammingTabUiBinder uiBinder = GWT
			.create(ProgrammingTabUiBinder.class);

	interface ProgrammingTabUiBinder extends UiBinder<Widget, ProgrammingTab> {
	}

	@UiField
	SubmitButton addButton;
	@UiField
	ListBox exercises;
	@UiField
	Button btnAdminReview, btnAddSkeletons, btnMakeVisible;
	@UiField
	Grid grdAdminReview;
	@UiField
	FileUpload testClass, helperClass, solution, skeleton;
	@UiField
	FormPanel helperForm, adminForm;
	@UiField
	TextBox openDate, closeDate, fileName;
	private ProgrammingTabPresenter presenter;

	public ProgrammingTab() {
		initWidget(uiBinder.createAndBindUi(this));
		AbstractServerCall visibleCmd = new GetVisibleExercisesCommand(
				exercises);
		visibleCmd.sendRequest();

		// Handle the Add Exercise Form
		adminForm.setAction(Proxy.getBaseURL() + "?cmd=AddExercise");
		adminForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		adminForm.setMethod(FormPanel.METHOD_POST);

		adminForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				WEStatus stat = new WEStatus(event.getResults());

				Notification.notify(stat.getStat(), stat.getMessage());
				AbstractServerCall visibleCmd1 = new GetVisibleExercisesCommand(
						exercises);
				visibleCmd1.sendRequest();

				if (stat.getStat() == WEStatus.STATUS_SUCCESS) {
					// Message is of the form: 'Uploaded Exercise [exercise
					// title]'
					// So, exercise, exercise title begins at index 18
					String exName = stat.getMessage().substring(18);
					AbstractServerCall abstractCmd = new AddSkeletonsCommand(
							exName);
					abstractCmd.sendRequest();
					// Proxy.addSkeletons(exName);
				}

			}
		});

		// Handle the Actions on Exercises Form
		helperForm.setAction(Proxy.getBaseURL() + "?cmd=AddHelperClass");
		helperForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		helperForm.setMethod(FormPanel.METHOD_POST);

		helperForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			// didn't want to create a whole proxy call, so added this instead
			public void onSubmitComplete(SubmitCompleteEvent event) {
				int status = WEStatus.STATUS_SUCCESS;
				if (event.getResults() != "Upload Successful")
					status = WEStatus.STATUS_ERROR;
				Notification.notify(status, event.getResults());
			}
		});
	}

	public void update() {
		AbstractServerCall visibleCmd = new GetVisibleExercisesCommand(
				exercises);
		visibleCmd.sendRequest();
	}

	@UiHandler("btnAdminReview")
	void onReviewClick(ClickEvent event) {
		grdAdminReview.clear(true);
		AbstractServerCall subCmd = new GetSubmissionInfoCommand(
				exercises.getValue(exercises.getSelectedIndex()),
				grdAdminReview);
		subCmd.sendRequest();
	}

	@UiHandler("btnAddSkeletons")
	void onSkelClick(ClickEvent event) {
		AbstractServerCall cmd = new AlterExerciseCommand(
				exercises.getValue(exercises.getSelectedIndex()), "skel",
				exercises);
		cmd.sendRequest();
		// Proxy.alterExercise(exercises.getValue(exercises.getSelectedIndex()),
		// "skel", exercises);
	}

	@UiHandler("btnMakeVisible")
	void onVisClick(ClickEvent event) {
		AbstractServerCall cmd = new AlterExerciseCommand(
				exercises.getValue(exercises.getSelectedIndex()), "skel",
				exercises);
		cmd.sendRequest();
		// Proxy.alterExercise(exercises.getValue(exercises.getSelectedIndex()),
		// "vis", exercises);
	}

	@UiHandler("btnDeleteExercise")
	void deleteExerciseClick(ClickEvent event) {
		final DialogBox deleteExercise = new DialogBox(false);
		Label lbl1 = new Label("ARE YOU SURE?  This deletes ALL associated\n"
				+ "files and submissions, and IS NOT RECOVERABLE.");

		Button delete = new Button("DELETE");
		Button nvm = new Button("Nevermind");

		VerticalPanel base = new VerticalPanel();
		HorizontalPanel line1 = new HorizontalPanel();
		HorizontalPanel line2 = new HorizontalPanel();

		deleteExercise.setText("DELETING "
				+ exercises.getValue(exercises.getSelectedIndex())
						.toUpperCase());

		delete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				deleteExercise.hide();
				AbstractServerCall cmd = new DeleteExerciseCommand(exercises
						.getValue(exercises.getSelectedIndex()), exercises);
				cmd.sendRequest();
				// Proxy.deleteExercise(exercises.getValue(exercises.getSelectedIndex()),
				// exercises);
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

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = (ProgrammingTabPresenter) presenter;

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
	public SubmitButton getAddButton() {
		return addButton;
	}

	@Override
	public ListBox getExercises() {
		return exercises;
	}

	@Override
	public Button getBtnAdminReview() {
		return btnAdminReview;
	}

	@Override
	public Button getBtnAddSkeletons() {
		return btnAddSkeletons;
	}

	@Override
	public Button getBtnMakeVisible() {
		return btnMakeVisible;
	}

	@Override
	public Grid getGrdAdminReview() {
		return grdAdminReview;
	}

	@Override
	public FileUpload getTestClass() {
		return testClass;
	}

	@Override
	public FileUpload getHelperClass() {
		return helperClass;
	}

	@Override
	public FileUpload getSolution() {
		return solution;
	}

	@Override
	public FileUpload getSkeleton() {
		return skeleton;
	}

	@Override
	public FormPanel getHelperForm() {
		return helperForm;
	}

	@Override
	public FormPanel getAdminForm() {
		return adminForm;
	}

	@Override
	public TextBox getOpenDate() {
		return openDate;
	}

	@Override
	public TextBox getCloseDate() {
		return closeDate;
	}

	@Override
	public TextBox getFileName() {
		return fileName;
	}

}
