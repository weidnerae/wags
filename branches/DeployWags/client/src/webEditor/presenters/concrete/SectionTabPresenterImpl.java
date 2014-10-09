package webEditor.presenters.concrete;

import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;

import webEditor.presenters.interfaces.SectionTabPresenter;
import webEditor.views.concrete.SectionTab;

public class SectionTabPresenterImpl implements SectionTabPresenter {
	

	
	private SectionTab sectionTab;
	private boolean bound = false;

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(sectionTab.asWidget());
	}

	@Override
	public void bind() {
		sectionTab.setPresenter(this);
		bound = true;
	}

	@Override
	public boolean bound() {
		return bound;
	}

	@Override
	public void update(List<String> data) {
		// TODO Auto-generated method stub
		
	}

}
