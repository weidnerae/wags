package webEditor.ProxyFramework;

import webEditor.WEStatus;
import webEditor.magnet.view.MagnetPageModel;

import com.google.gwt.http.client.Response;

/**
 * @author Dakota Murray
 * 
 * Server File: GetMagnetExercises.php
 * Arguments: None
 * Method: GET
 * Modifies: Main WAGS object, changes the center content panel
 */
public class BuildMagnetsCommand extends AbstractServerCall {

	private MagnetPageModel model;
	
	@Override
	protected void handleResponse(Response response) {
		WEStatus stat = new WEStatus(response);
		
		String[] problems = stat.getMessageArray();
		int[] idList = new int[problems.length / 3];
		String[] problemsList = new String[problems.length / 3];
		int[] statusList = new int[problems.length / 3];
		
		// To understand this, you must understand that problems is an array
		// following a sequence of id, name, success.  Thus, we iterate over it
		// in steps of three, to "group" the entries corresponding to the same exercise
		for(int i = 0; i < problems.length - 2; i += 3){
			final int id = Integer.parseInt(problems[i]);
			
			if (id != 0) {
				int idx = i / 3;
				
				idList[idx] = id;
				problemsList[idx] = problems[i + 1];
				statusList[idx] = Integer.parseInt(problems[i + 2]);
			}
		}
		
		model.setIds(idList, false);
		model.setTitles(problemsList, false);
		model.setStatus(statusList, false);
		model.notifyObservers();
		/**
		Magnets Magnets = new Magnets(idList, problemsList, statusList, (WagsPresenterImpl) page);
		((WagsPresenterImpl) page).splashPage = Magnets;
		Magnets.getElement().getStyle().setOverflowY(Overflow.AUTO);
		page.setWidget(Magnets);
		*/
	}
	
	public BuildMagnetsCommand(MagnetPageModel model)
	{
		this.model = model;
		command = ProxyCommands.GetMagnetExercises;
	}

}
