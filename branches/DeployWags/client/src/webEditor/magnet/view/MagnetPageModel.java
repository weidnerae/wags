package webEditor.magnet.view;

import java.util.ArrayList;
import java.util.List;

import webEditor.Common.Model;
import webEditor.presenters.concrete.ProblemType;

/**
 * @author dakotamurray
 * 
 * Implementation of the model for use on the problem page
 *
 */
public class MagnetPageModel extends Model {

	public static final int MAGNET_STATE = 0;
	public static final int LOGICAL_STATE = 1;
	public static final int DATABASE_STATE = 2;
	
	public static final int ALREADY_LOADED = 1;
	public static final int NOT_LOADED = 0;
	
	private ArrayList<Integer> ids;				//array of problem id numbers
	private ArrayList<String> titles;		//array of problem names
	private ArrayList<Integer> statuses;
	private ArrayList<Integer> types;      //array of success values
	private int pageState;
	private boolean isLoaded;
	
	public MagnetPageModel()
	{
		super();
		pageState = MAGNET_STATE;
		ids = new ArrayList<Integer>();
		titles = new ArrayList<String>();
		statuses = new ArrayList<Integer>();
		types = new ArrayList<Integer>();
		isLoaded = false;
	}

	public void addProblem(int id, String title, int status, ProblemType type) {
		ids.add(id);
		titles.add(title);
		statuses.add(status);
		types.add(ProblemType.TypeToVal(type));
	}
	
	public void setPageState(int pageState, boolean toUpdate) {
		this.pageState = pageState;
		if (toUpdate) {
			notifyObservers();
		}
	}
	
	@Override
	public List<String> getData() {
		String state = "" + pageState;
		List<String> list = new ArrayList<String>();
		list.add(state);
		if (!isLoaded) {
			String idData = "";
			String titleData = "";
			String statusData = "";
			String typeData = "";
			for(int i = 0; i < ids.size(); i++) {
				idData += ids.get(i) + "&";
				titleData += titles.get(i) + "&";
				statusData += statuses.get(i) + "&";
				typeData += types.get(i) + "&";
			}
			list.add(idData);
			list.add(titleData);
			list.add(statusData);
			list.add(typeData);
			isLoaded = true;
		}
		return list;
	}
}
