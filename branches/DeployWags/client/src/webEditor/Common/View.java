package webEditor.Common;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author Dakota Murray
 * 
 * The view is a key part of the MVP design pattern. The view instantiates and contains instances of
 * all UI elements. Basically the view is what the user sees. The view contains no application logic 
 * or data. All events which require specific logic are delegated to the view's presenter. 
 * 
 * All views have a presenter attached to them. The presenter provides logic and updates to the 
 * view. 
 * 
 * Steps to Create a new View:
 * 		- Create a new interface following the naming convention yourClassNameView.java and have it extend
 * 		  webEditor.Common.View.java. 
 * 		- In this newly created interface create signatures for all methods a view of this type must implement. 
 * 		  For example, in the login page you could have methods to get the username adn password textboxes, etc. 
 * 		- Create a concrete View implementation which extends the GWT class Composite and implements your
 * 		  view interface. Also create .ui.xml file to go along with the new concrete view
 * 		- Now simply create the view as necessary and implement all methods from the interface. The view should have 
 * 		  a field for the type of presenter it needs
 * 		- Look at other views in webEditor.views.* to see examples of views. Ensure that the .ui.xml and the
 * 		  view are linked properly. 
 */
public interface View extends IsWidget {
	void setPresenter(Presenter presenter);
	boolean hasPresenter();
	Presenter getPresenter();
}
