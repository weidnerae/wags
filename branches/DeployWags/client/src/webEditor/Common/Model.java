package webEditor.Common;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;


public interface Model extends IsWidget
{	
	public List<String> getData();
	public void registerObserver(Presenter presenter);
	public void removeObserver(Presenter presenter);
	public void notifyObservers();
}
