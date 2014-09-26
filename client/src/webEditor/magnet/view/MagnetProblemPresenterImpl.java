package webEditor.magnet.view;
import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;

import webEditor.presenters.interfaces.ProblemPagePresenter;


public class MagnetProblemPresenterImpl implements MagnetProblemPresenter {

	private MagnetProblemModel model;
	private MagnetProblemView view;
	
	public MagnetProblemPresenterImpl(MagnetProblemView view)
	{
		this.view = view;
		model = new MagnetProblemModel();
		model.registerObserver(this);
	}
	
	@Override
	public void go(HasWidgets container) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bind() {
		view.setPresenter(this);
	}

	@Override
	public boolean bound() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(List<String> data) {
		// TODO Auto-generated method stub

	}

}
