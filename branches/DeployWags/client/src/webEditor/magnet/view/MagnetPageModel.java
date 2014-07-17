package webEditor.magnet.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;

import webEditor.Common.Model;
import webEditor.Common.Presenter;

public class MagnetPageModel implements Model {

	private int[] ids;				//array of problem id numbers
	private String[] titles;		//array of problem names
	private int[] statuses;			//array of success values
	
	private ArrayList<Presenter> observers;
	
	public MagnetPageModel()
	{
		observers = new ArrayList<Presenter>();
	}
	
	@Override
	public Widget asWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setIds(int[] ids, boolean toUpdate) {
		this.ids = ids;
		if (toUpdate) {
			notifyObservers();
		}
	}
	
	public void setTitles(String[] titles, boolean toUpdate) {
		this.titles = titles;
		if (toUpdate) {
			notifyObservers();
		}
	}
	
	public void setStatus(int[] statuses, boolean toUpdate) {
		this.statuses = statuses;
		if (toUpdate) {
			notifyObservers();
		}
	}
	
	@Override
	public List<String> getData() {
		String idData = "";
		String titleData = "";
		String statusData = "";
		for(int i = 0; i < ids.length; i++) {
			idData += ids[i] + "&";
			titleData += titles[i] + "&";
			statusData += statuses[i] + "&";
		}
		List<String> list = new ArrayList<String>();
		list.add(idData);
		list.add(titleData);
		list.add(statusData);
		return list;
	}

	@Override
	public void registerObserver(Presenter presenter) {
		observers.add(presenter);
	}

	@Override
	public void removeObserver(Presenter presenter) {
		observers.remove(presenter);
	}

	@Override
	public void notifyObservers() {
		List<String> data = getData();
		for(Presenter pres : observers) {
			pres.update(data);
		}
	}

}
