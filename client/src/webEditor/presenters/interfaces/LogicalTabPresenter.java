package webEditor.presenters.interfaces;

import webEditor.WEStatus;
import webEditor.Common.Presenter;

public interface LogicalTabPresenter extends Presenter {

	public void setExercises(String[] exercises);
	public void addSubjectClickHandlers();
	public void addGroupClickHandlers();
	public void handleSubjects(String[] subjects);
	public void handleGroups(String[] groups);
	public void handleExercises(String[] exercises);
	public void setCallback(String[] exercises, WEStatus status);
	public void getCallback(String[] exercises, WEStatus status, String request);
}
