package webEditor.Common;

import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;

/**
 * @author Dakota Murray
 * @version 21 July 2014
 * 
 * The Presenter is a key component of the MVP design pattern. The Presenter contains all application 
 * logic and can be thought of as the class that does all the work. If anything about the website changes, 
 * updates, or is modified, the presenter is the class handling the work. The presenter contains NO view 
 * elements or application data. The presenter relies on its model and view for such things. 
 *
 * Most of these methods are not being used in any meaningful context. Several will be cut soon. 
 * 
 * Steps to Creating a Presenter:
 * 		- The Presenter will likely be the most complicated component of this whole setup. First create a 
 * 		  specific presenter interface which extends webEditor.Common.Presenter.
 * 		- In this new interface put any methods which must be common to all presenter to this page. This presenter
 * 		  may be empty. That is not a problem as it still provides a place to place methods we need in the future
 * 		- Create a concrete implementation of the presenter and fill in all the methods. Presenters should have a field 
 * 		  for a view which is set in the constructor. The concrete presenter should also have a field for the model. In 
 * 		  the constructor either retrieve or create the model and set the presenter as an observer. 
 * 		- You should have a complete presenter by now. If there are no errorsd go look at webEditor.Commin.AppController 
 * 		  to see how to get the new page working. 
 */
public interface Presenter {
	public void go(final HasWidgets container);
	public void bind();
	public boolean bound();
	public void update(List<String> data);
}