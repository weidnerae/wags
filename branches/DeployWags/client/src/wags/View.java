package wags;


import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * View
 * 
 * Abstract class that all Views in WE must implement.
 * Implementations must implement getLink().
 * 
 * @author Robert Bost <bostrt@appstate.edu>
 *
 */
public abstract class View extends Composite implements IsWidget {
	/**
	 * Get a WEAnchor for the View. 
	 * @return WEAnchor
	 */
	public abstract WEAnchor getLink();
	
	public void go(){
		this.getLink().go();
	}
}