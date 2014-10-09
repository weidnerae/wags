package webEditor.presenters.concrete;

import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;

import webEditor.Reviewer;
import webEditor.ProxyFramework.AbstractServerCall;
import webEditor.ProxyFramework.GetLMAssigned;
import webEditor.ProxyFramework.GetMMAssignedCommand;
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

		AbstractServerCall cmd2 = new GetMMAssignedCommand(reviewTab.getLogHandler(), Reviewer.NONE);
		cmd2.sendRequest();

		AbstractServerCall cmd3 = new GetLMAssigned(reviewTab.getLogHandler(),	Reviewer.GET_REVIEW);
		cmd3.sendRequest();

		AbstractServerCall cmd4 = new GetMMAssignedCommand(reviewTab.getLogHandler(),	Reviewer.GET_REVIEW);
		cmd4.sendRequest();
	}

	@Override
	public void update(List<String> data) {
		update();		
	}

	
}
