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
 */
public interface View extends IsWidget {
	void setPresenter(Presenter presenter);
	boolean hasPresenter();
	Presenter getPresenter();
}
