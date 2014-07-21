package webEditor.Common;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
/**
 * @author : Dakota Murray
 * @version:  21 July 2014
 * 
 * The Model contains all data and information for each view/presenter in the application as well 
 * as methods and common interfaces to access that information. Each model acts as a Subject in an
 * Observer pattern. When the model is changed all observing presenters will be updated and sent 
 * data to make the appropriate changes to the view.
 * 
 * All Models must have a common interface for accessing data as well as methods used to register and 
 * notify observers. 
 */

public interface Model extends IsWidget
{	
	public List<String> getData();
	public void registerObserver(Presenter presenter);
	public void removeObserver(Presenter presenter);
	public void notifyObservers();
}
