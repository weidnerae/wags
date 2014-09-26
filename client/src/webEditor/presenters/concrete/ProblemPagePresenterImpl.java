package webEditor.presenters.concrete;
import java.util.List;

import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;

import webEditor.ProxyFramework.AbstractServerCall;
import webEditor.ProxyFramework.LoadAssignedProblemsCommand;
import webEditor.magnet.view.MagnetPageModel;
import webEditor.presenters.interfaces.ProblemPagePresenter;
import webEditor.views.elements.ProblemButton;
import webEditor.views.interfaces.ProblemPageView;

/**
 * @author Dakota Murray
 * 
 * Presenter implementation for the problems page. The problems page is reached through
 * the "Problems" navbar option and displays all problems assigned to a student. 
 *
 */

public class ProblemPagePresenterImpl implements ProblemPagePresenter {
	
	public static final String EMPTY_LABEL = "No Magnet Exercises Assigned!";

	private ProblemPageView view;
	private MagnetPageModel model;

	/**
	 * Basic constructor for the presenter. The presenter initializes a new model, 
	 * and then sends a server request to get the data necessary to populate the
	 * list. The model then updates the presenter with the list of problems.
	 * 
	 * @param view	The ProblemPageView bound to this presenter.
	 */
	public ProblemPagePresenterImpl(ProblemPageView view)
	{
		this.view = view;
		model = new MagnetPageModel();
		model.registerObserver(this, false);
		AbstractServerCall cmd = new LoadAssignedProblemsCommand(model);
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

	/**
	 * Populates the list of logical, database, and magnet problems available for the
	 * student. A title, id, problem type, and status are required to create a problem
	 * button. The steps that must be taken are as follows:
	 * 	
	 * 1. Check to see if the data is already loaded (list.size() > 1), if not, populate the list
	 *  2. Parse data from the data list
	 *  3. Create a new "ProblemButton" object, add it to the appropriate panel depending on type
	 *  4. Set the visual indicators depending on status: assigned or completed
	 *  5. update the page state, ie: what type of problems we are looking at (magnet, logical, database)
	 *  
	 */
	@Override
	public void update(List<String> data) {
		int pageState = new Integer(data.get(0));
		
		// load the panels we need
		ComplexPanel magnets = view.getMagnetPanel();
		ComplexPanel logical = view.getLogicalPanel();
		
		//Only execute if problems have not already been loaded
		if( data.size() > 1) {
			boolean magnetDue = false;
			boolean logicalDue = false;
			
			String[] ids = data.get(1).split("&");
			String[] titles = data.get(2).split("&");
			String[] statuses = data.get(3).split("&");
			String[] types = data.get(4).split("&");
			
			//take this out of the loop for efficiency
			int magnetType = ProblemType.TypeToVal(ProblemType.MAGNET_PROBLEM);
			for(int i = 0; i < ids.length; i++) {
				int id = new Integer(ids[i]);
				String title = titles[i];
				int status = new Integer(statuses[i]);
				int type = new Integer(types[i]);
				
				if (type == magnetType) {
					if ( status == 0) {
						magnetDue = true;
					}
					magnets.add(new ProblemButton(id, title, status));
				} else { 	
					//Is a logical problem
					if (status == 0)  {
						logicalDue = true;
					}
					logical.add(new ProblemButton(id, title, status));
				}
				// Expand these statements for database problems at a later date
			}
			if (magnetDue) {
				view.getMagnetCategory().setIcon(IconType.EXCLAMATION);
				view.getMagnetCategory().addStyleName("problem_due");
			} else {
				view.getMagnetCategory().addStyleName("problem_complete");
			}
			if (logicalDue) {
				view.getLogicalCategory().setIcon(IconType.EXCLAMATION);
				view.getLogicalCategory().addStyleName("problem_due");
			} else {
				view.getLogicalCategory().addStyleName("problem_category");
			}
		}
		
		// Update page state
		setPageState(pageState);
		
	}

	@Override
	public void onMagnetCategoryClick() {
		model.setPageState(MAGNET_STATE, true);
	}

	@Override
	public void onLogicalCategoryClick() {
		model.setPageState(LOGICAL_STATE, true);
	}

	@Override
	public void onDatabaseCategroryClick() {
		model.setPageState(DATABASE_STATE, true);
	}

	/**
	 * Changes the problem state ie: which category of problems is currently
	 * being viewed (magnet, logical, or database).
	 * 
	 * TODO: Currently database problems are not implemented, so if selected will 
	 * 		 default to magnet problems
	 */
	@Override
	public void setPageState(int pageState) {
		ComplexPanel magnets = view.getMagnetPanel();
		ComplexPanel logical = view.getLogicalPanel();
		
		UIObject magnetCategory = view.getMagnetCategory();
		UIObject logicalCategory = view.getLogicalCategory();
		//begin by setting everything to invisible to avoid visual mishaps and make it easy to 
		//update the tab (see end of this function
		magnets.setVisible(false);
		logical.setVisible(false);
		
		magnetCategory.removeStyleName("category_selected");
		logicalCategory.removeStyleName("category_selected");
		switch (pageState) {
		case MAGNET_STATE:
			magnets.setVisible(true);
			magnetCategory.addStyleName("category_selected");
			break;
		case LOGICAL_STATE:
			logical.setVisible(true);
			logicalCategory.addStyleName("category_selected");
			break;
		case DATABASE_STATE: //fall through
		default:
			magnets.setVisible(true);
			magnetCategory.addStyleName("category_selected");
		}
		
	}
}
