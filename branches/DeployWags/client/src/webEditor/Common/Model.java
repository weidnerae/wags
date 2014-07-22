package webEditor.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Dakota Murray
 * @version:  21 July 2014
 * 
 * The Model contains all data and information for each view/presenter in the application as well 
 * as methods and common interfaces to access that information. Each model acts as a Subject in an
 * Observer pattern. When the model is changed all observing presenters will be updated and sent 
 * data to make the appropriate changes to the view.
 * 
 * Steps for Creating a New Model:
 * 		- The model is the easiest thing to create. First create a class that extends webEditor.Common.Model
 * 		- Put all the fields that you need for the particular model. Add getters and setters to each method,
 * 		  and make sure there is a boolean argument passed to the setter methods to control whether or not 
 * 		  the observers are updated
 * 		- And that is pretty much it, you now have a Model. 
 */

public abstract class Model
{	
	protected ArrayList<Presenter> observers;
	
	public abstract List<String> getData();

	public Model()
	{
		observers = new ArrayList<Presenter>();
	}
	
	public void registerObserver(Presenter presenter) {
		if (presenter != null) {
			observers.add(presenter);
			presenter.update(getData());
		}
		
	}

	public void removeObserver(Presenter presenter) {
		if (presenter != null) {
			observers.remove(presenter);
		}
	}

	public void notifyObservers() {
		List<String> data = getData();
		for (Presenter presenter : observers) {
			presenter.update(data);
		}
	}
}
