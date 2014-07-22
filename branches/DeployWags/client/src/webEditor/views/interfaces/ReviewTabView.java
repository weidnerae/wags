package webEditor.views.interfaces;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.SubmitButton;

import webEditor.Reviewer;
import webEditor.Common.View;
import webEditor.admin.ReviewPanel;

public interface ReviewTabView extends View {

	ReviewPanel getRVLogPanel();
	ReviewPanel getRVMagPanel();
	SubmitButton getBtnCompReview();
	FormPanel getFormCompReview();
	Reviewer getLogHandler();
	Reviewer getMagHandler();

}
