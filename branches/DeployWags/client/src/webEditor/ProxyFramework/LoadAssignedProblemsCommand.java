package webEditor.ProxyFramework;

import webEditor.WEStatus;
import webEditor.magnet.view.ProblemPageModel;
import webEditor.presenters.concrete.ProblemType;

import com.google.gwt.http.client.Response;

/**
 * @author Dakota Murray
 * 
 * Server File: GetMagnetExercises.php
 * Arguments: None
 * Method: GET
 * 
 * Updates the ProblemPageModel with data for all assigned and attempted problems.
 * Once finished will also make the model notify its observers.
 */
public class LoadAssignedProblemsCommand extends AbstractServerCall {

	private ProblemPageModel model;
	
	@Override
	protected void handleResponse(Response response) {
		WEStatus stat = new WEStatus(response);
		
		String[] problems = stat.getMessageArray();
		
		int magnetCount = Integer.parseInt(problems[0]);
		int count = 0;
		
		// Iterate over the problems list in steps of three, using the three
		// data points to add a new problem to the model. 
		for(int i = 1; i < problems.length - 2; i += 3){
			int id = Integer.parseInt(problems[i]);
			String title = problems[i+1];
			int status = Integer.parseInt(problems[i+2]);
			if ( count <= magnetCount) {
				model.addProblem(id, title, status, ProblemType.MAGNET_PROBLEM);
			} else {
				model.addProblem(id, title, status, ProblemType.LOGICAL_PROBLEM);
			}
			count++;
		}
		model.notifyObservers();
	}
	
	public LoadAssignedProblemsCommand(ProblemPageModel model)
	{
		this.model = model;
		command = "GetAssignedExercises";
	}

}
