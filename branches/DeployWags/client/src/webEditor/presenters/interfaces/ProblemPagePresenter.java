package webEditor.presenters.interfaces;

import webEditor.Common.Presenter;

/**
 * 
 * @author Dakota Murray
 * 
 * All problem page presenters should be able to do the following:
 * 
 * Handle click events on category buttons (switching currently viewed category)
 * Change the page state to view each category.
 *
 */
public interface ProblemPagePresenter extends Presenter {
	
	public static final int MAGNET_STATE = 0;
	public static final int LOGICAL_STATE = 1;
	public static final int DATABASE_STATE = 2;
	
	public void onMagnetCategoryClick();
	public void onLogicalCategoryClick();
	public void onDatabaseCategroryClick();
	
	public void setPageState(int pageState);
}
