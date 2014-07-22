package webEditor.presenters.concrete;

import java.util.List;

import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasWidgets;

import webEditor.Proxy;
import webEditor.Reviewer;
import webEditor.WEStatus;
import webEditor.ProxyFramework.AbstractServerCall;
import webEditor.ProxyFramework.GetLMAssigned;
import webEditor.ProxyFramework.GetMMAssigned;
import webEditor.ProxyFramework.ReviewExerciseCommand;
import webEditor.presenters.interfaces.ReviewTabPresenter;
import webEditor.views.concrete.ReviewTab;
import webEditor.views.interfaces.ReviewTabView;

public class ReviewTabPresenterImpl implements ReviewTabPresenter {

	private ReviewTabView reviewTab;
	private boolean bound = false;

	public ReviewTabPresenterImpl(ReviewTab view) {
		this.reviewTab = view;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(reviewTab.asWidget());
	}

	@Override
	public void bind() {
		reviewTab.setPresenter(this);
		bound = true;
	}

	@Override
	public boolean bound() {
		return bound;
	}

	public void update() {

		AbstractServerCall cmd1 = new GetLMAssigned(reviewTab.getLogHandler(), Reviewer.NONE);
		cmd1.sendRequest();

		AbstractServerCall cmd2 = new GetMMAssigned(reviewTab.getLogHandler(), Reviewer.NONE);
		cmd2.sendRequest();

		AbstractServerCall cmd3 = new GetLMAssigned(reviewTab.getLogHandler(),	Reviewer.GET_REVIEW);
		cmd3.sendRequest();

		AbstractServerCall cmd4 = new GetMMAssigned(reviewTab.getLogHandler(),	Reviewer.GET_REVIEW);
		cmd4.sendRequest();

		// Leftover from the old code
		// Proxy.getLMAssigned(logHandler, Reviewer.NONE);
		// Proxy.getMMAssigned(magHandler, Reviewer.NONE);

		// Proxy.getLMAssigned(logHandler, Reviewer.GET_REVIEW);
		// Proxy.getMMAssigned(magHandler, Reviewer.GET_REVIEW);
	}

	@Override
	public void update(List<String> data) {
		update();		
	}

	
}
