package webEditor.presenters.interfaces;

import webEditor.Common.Presenter;

public interface ProblemCreationPanelPresenter extends Presenter{

	void addHelperUpload();

	void verifyDelete(String title);

	void verifyOverwrite();

	void clearMagnetMakerOptions();

	void setupMagnetMakerOptions();

}
