package webEditor.Common;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;

public class AppModel implements Model
{
	private boolean isLoggedIn;
	private boolean isAdmin;
	private int id;
	private String username;
	
	private ArrayList<Presenter> observers;
	
	public AppModel()
	{
		isLoggedIn = false;
		isAdmin = false;
		id = 0;
		username = "";
		observers = new ArrayList<Presenter>();
	}
	
	public void setId(int id)
	{
		this.id = id;
		notifyObservers();
	}
	
	public void setId(int id, boolean toNotify)
	{
		this.id = id;
		if (toNotify)
			notifyObservers();
	}
	
	public void setIsLoggedIn(boolean isLoggedIn)
	{
		this.isLoggedIn = isLoggedIn;
		notifyObservers();
	}
	
	public void setIsLoggedIn(boolean isLoggedIn, boolean toNotify)
	{
		this.isLoggedIn = isLoggedIn;
		if (toNotify)
			notifyObservers();
	}
	
	public void setIsAdmin(boolean isAdmin)
	{
		this.isAdmin = isAdmin;
		notifyObservers();
	}
	
	public void setIsAdmin(boolean isAdmin, boolean toNotify)
	{
		this.isAdmin = isAdmin;
		if(toNotify) 
			notifyObservers();
	}
	
	public void setUsername(String username)
	{
		this.username = username;
		notifyObservers();
	}
	
	public void setUsername(String username, boolean toNotify)
	{
		this.username = username;
		if (toNotify)
			notifyObservers();
	}
	public boolean isLoggedIn()
	{
		return isLoggedIn;
	}
	
	public boolean isAdmin()
	{
		return isAdmin;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public int getId()
	{
		return id;
	}

	public void clear()
	{
		isLoggedIn = false;
		isAdmin = false;
		id = 0;
		username = "";
		notifyObservers();
	}
	
	@Override
	public Widget asWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getData() {
		List<String> data = new ArrayList<String>();
		
		if (isLoggedIn) {
			data.add("TRUE");
		} else {
			data.add("FALSE");
		}
		
		if (isAdmin) {
			data.add("TRUE");
		} else {
			data.add("FALSE");
		}
		
		return data;
	}

	@Override
	public void registerObserver(Presenter presenter) {
		if (presenter != null) {
			observers.add(presenter);
			presenter.update(getData());
		}
		
	}

	@Override
	public void removeObserver(Presenter presenter) {
		if (presenter != null) {
			observers.remove(presenter);
		}
	}

	@Override
	public void notifyObservers() {
		List<String> data = getData();
		for (Presenter presenter : observers) {
			presenter.update(data);
		}
	}
}
