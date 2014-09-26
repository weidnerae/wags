package webEditor.ProxyFramework;

import webEditor.WEStatus;
import webEditor.magnet.view.MagnetPageModel;
import webEditor.presenters.concrete.ProblemType;

import com.google.gwt.http.client.Response;

/**
 * 
 * @author Dakota Murray
 * 
 * Server File : GetLogicalExercises.php
 * Arguments : None
 * Method: GET
 * Modifies: Main WAGS object, changes center content panel
 *
 */
public class LoadLogicalCommand extends AbstractServerCall {

	private MagnetPageModel model;
	
	@Override
	protected void handleResponse(Response response) {
		WEStatus status = new WEStatus(response);

		/*
		 * The problems array should have even indices corresponding to 
		 * the problem title, and odd indices with either 0 or 1 for 
		 * not completed and completed.
		 */
		String[] problems = status.getMessageArray();

		for (int i = 0; i < problems.length - 2; i += 3) {
			//Message string is the format of "title","status","id"
			String title = problems[i];
			int stat = Integer.parseInt(problems[i+1]);
			int id = Integer.parseInt(problems[i+2]);

			model.addProblem(id, title, stat, ProblemType.LOGICAL_PROBLEM);
		}
		model.notifyObservers();
	}
	
	public LoadLogicalCommand(MagnetPageModel model)
	{
		this.model = model;
		command = ProxyCommands.GetLogicalExercises;
	}

}
