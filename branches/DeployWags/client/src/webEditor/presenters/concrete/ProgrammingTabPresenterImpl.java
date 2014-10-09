package webEditor.presenters.concrete;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import webEditor.ProxyFramework.AbstractServerCall;
import webEditor.ProxyFramework.AlterExerciseCommand;
import webEditor.ProxyFramework.DeleteExerciseCommand;
import webEditor.ProxyFramework.GetSubmissionInfoCommand;
import webEditor.ProxyFramework.GetVisibleExercisesCommand;
import webEditor.presenters.interfaces.ProgrammingTabPresenter;
import webEditor.views.concrete.ProgrammingTab;

public class ProgrammingTabPresenterImpl implements ProgrammingTabPresenter {

	private ProgrammingTab programmingTab;
	private boolean bound = false;

	public ProgrammingTabPresenterImpl(ProgrammingTab view) {
		this.programmingTab = view;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(programmingTab.asWidget());
	}

	@Override
	public void bind() {
		programmingTab.setPresenter(this);
		bound = true;
	}

	@Override
	public boolean bound() {
		return bound;
	}

	@Override
	public void update(List<String> data) {
		AbstractServerCall getVisibleExercises = new GetVisibleExercisesCommand(programmingTab.getExercises());
		getVisibleExercises.sendRequest();
	}

	@Override
	public void deleteExerciseClick(ClickEvent event) {
		final DialogBox deleteExercise = new DialogBox(false);
		Label lbl1 = new Label("ARE YOU SURE?  This deletes ALL associated\n"
				+ "files and submissions, and IS NOT RECOVERABLE.");

		Button delete = new Button("DELETE");
		Button nvm = new Button("Nevermind");

		VerticalPanel base = new VerticalPanel();
		HorizontalPanel line1 = new HorizontalPanel();
		HorizontalPanel line2 = new HorizontalPanel();

		deleteExercise.setText("DELETING "
				+ programmingTab
						.getExercises()
						.getValue(
								programmingTab.getExercises()
										.getSelectedIndex()).toUpperCase());

		delete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				deleteExercise.hide();
				AbstractServerCall cmd = new DeleteExerciseCommand(
						programmingTab.getExercises().getValue(
								programmingTab.getExercises()
										.getSelectedIndex()), programmingTab
								.getExercises());
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
	public void onVisClick(ClickEvent event) {
		AbstractServerCall cmd = new AlterExerciseCommand(programmingTab
				.getExercises().getValue(
						programmingTab.getExercises().getSelectedIndex()),
				"skel", programmingTab.getExercises());
		cmd.sendRequest();
		// Proxy.alterExercise(exercises.getValue(exercises.getSelectedIndex()),
		// "vis", exercises);
	}

	@Override
	public void onReviewClick(ClickEvent event) {
		programmingTab.getGrdAdminReview().clear(true);
		AbstractServerCall getSubmissionInfo = new GetSubmissionInfoCommand(programmingTab.getExercises().getValue(programmingTab.getExercises().getSelectedIndex()),programmingTab.getGrdAdminReview());
		getSubmissionInfo.sendRequest();
	}

	@Override
	public void onSkelClick(ClickEvent event) {
		AbstractServerCall cmd = new AlterExerciseCommand(programmingTab
				.getExercises().getValue(
						programmingTab.getExercises().getSelectedIndex()),
				"skel", programmingTab.getExercises());
		cmd.sendRequest();
		// Proxy.alterExercise(exercises.getValue(exercises.getSelectedIndex()),
		// "skel", exercises);
	}

}
