package webEditor.presenters.concrete;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasWidgets;

import webEditor.ProxyFramework.AbstractServerCall;
import webEditor.ProxyFramework.BuildMagnetsCommand;
import webEditor.magnet.view.MagnetPageModel;
import webEditor.magnet.view.ProblemButton;
import webEditor.presenters.interfaces.MagnetPagePresenter;
import webEditor.views.interfaces.MagnetPageView;


public class MagnetPagePresenterImpl implements MagnetPagePresenter {
	
	public static final String EMPTY_LABEL = "No Magnet Exercises Assigned!";
	public static int INCOMPLETE = 0;
	public static int SUCCESS = 1;
	public static int REVIEW = 2;
	
	private MagnetPageView view;
	
	public MagnetPagePresenterImpl(MagnetPageView view)
	{
		this.view = view;
		MagnetPageModel model = new MagnetPageModel();
		model.registerObserver(this);
		
		AbstractServerCall cmd = new BuildMagnetsCommand(model);
		cmd.sendRequest();
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
		Window.alert(data.get(0));
		String[] ids = data.get(0).split("&");
		String[] titles = data.get(1).split("&");
		String[] statuses = data.get(2).split("&");
		
		ComplexPanel panel = view.getProblemPanel();
		panel.setVisible(false);
		panel.clear();
		
		for(int i = 0; i < ids.length; i++) {
			int id = new Integer(ids[i]);
			String title = titles[i];
			int status = new Integer(statuses[i]);
			panel.add(new ProblemButton(id, title, status));
			
		}
		panel.setVisible(true);
	}

}
