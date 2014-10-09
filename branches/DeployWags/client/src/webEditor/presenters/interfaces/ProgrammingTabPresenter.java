package webEditor.presenters.interfaces;

import com.google.gwt.event.dom.client.ClickEvent;

import webEditor.Common.Presenter;

public interface ProgrammingTabPresenter extends Presenter {

	void deleteExerciseClick(ClickEvent event);

	void onVisClick(ClickEvent event);

	void onReviewClick(ClickEvent event);

	void onSkelClick(ClickEvent event);

}
