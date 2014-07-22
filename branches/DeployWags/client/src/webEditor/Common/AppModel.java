package webEditor.Common;

import java.util.ArrayList;
import java.util.List;


/**
 * @author  : Dakota Murray
 * @version : 21 July 2014
 *
 * Contains all application wide data and information (username, admin status, etc). Look to the Mode.java
 * interface for more info.
 */
public class AppModel extends Model
{
	private boolean isLoggedIn;
	private boolean isAdmin;
	private int id;
	private String username;
	
	public AppModel()
	{
		super();
		isLoggedIn = false;
		isAdmin = false;
		id = 0;
		username = "";
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

	
}
