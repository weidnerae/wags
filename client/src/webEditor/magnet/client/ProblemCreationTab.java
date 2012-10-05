package webEditor.magnet.client;

import webEditor.client.Proxy;
import webEditor.client.WEStatus;
import webEditor.client.view.Notification;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class ProblemCreationTab extends Composite {
	@UiField FormPanel problemCreateForm;
	@UiField SubmitButton sbtPCForm;
	
	private static ProblemCreationTabUiBinder uiBinder = GWT
			.create(ProblemCreationTabUiBinder.class);

	interface ProblemCreationTabUiBinder extends
			UiBinder<Widget, ProblemCreationTab> {
	}

	public ProblemCreationTab() {
		initWidget(uiBinder.createAndBindUi(this));
		
		problemCreateForm.setAction(Proxy.getBaseURL() + "?cmd=AddMagnetExercise");
		problemCreateForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		problemCreateForm.setMethod(FormPanel.METHOD_POST);
		problemCreateForm.addSubmitCompleteHandler(new SubmitCompleteHandler(){
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				WEStatus stat = new WEStatus(event.getResults());
				Notification.notify(stat.getStat(), stat.getMessage());
			}
		});
	}

}
