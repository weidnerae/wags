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
 */
public interface Presenter {
	public void go(final HasWidgets container);
	public void bind();
	public boolean bound();
	public void update(List<String> data);
}