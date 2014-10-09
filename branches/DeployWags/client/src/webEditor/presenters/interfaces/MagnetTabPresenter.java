package webEditor.presenters.interfaces;

import webEditor.WEStatus;
import webEditor.Common.Presenter;

public interface MagnetTabPresenter extends Presenter {

	void handleGroups(String[] groups);
	void handleExercises(String[] exercises);
	void setExercises(String[] exercises);
	void setCallback(String[] exercises, WEStatus status);
	void getCallback(String[] exercises, WEStatus status, String args);
	void addGroupClickHandlers();

}
