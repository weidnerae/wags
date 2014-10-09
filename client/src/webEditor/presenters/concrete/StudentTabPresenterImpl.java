package webEditor.presenters.concrete;

import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;

import webEditor.presenters.interfaces.StudentTabPresenter;
import webEditor.views.concrete.StudentTab;

public class StudentTabPresenterImpl implements StudentTabPresenter {

	private StudentTab studentTab;
	private boolean bound = false;
	
	public StudentTabPresenterImpl(StudentTab view) {
		this.studentTab = view;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(studentTab.asWidget());
	}

	@Override
	public void bind() {
		studentTab.setPresenter(this);
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
