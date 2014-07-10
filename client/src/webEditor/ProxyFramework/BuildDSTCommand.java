package webEditor.ProxyFramework;

import webEditor.WEStatus;
import webEditor.logical.DataStructureTool;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

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
public class BuildDSTCommand extends AbstractServerCall {

	private AcceptsOneWidget presenter;
	
	@Override
	protected void handleResponse(Response response) {
		WEStatus status = new WEStatus(response);
		
		/*
		 * The problems array should have even indices corresponding to 
		 * the problem title, and odd indices with either 0 or 1 for 
		 * not completed and completed.
		 */
		String[] problems = status.getMessageArray();
		String[] problemsList = new String[problems.length / 2];
		int[] statusList = new int[problems.length / 2];

		for (int i = 0; i < problems.length - 1; i += 2) {
			int idx = i / 2; 										   // corresponding index for lists
			problemsList[idx] = problems[i]; 						   // title of exercise
			statusList[idx] = Integer.parseInt(problems[i + 1]); 	   // true if corresponding string is '1'
		}

		DataStructureTool DST = new DataStructureTool(problemsList,
				statusList);
		DST.getElement().getStyle().setOverflowY(Overflow.AUTO);
		presenter.setWidget(DST);

	}
	
	public BuildDSTCommand(AcceptsOneWidget presenter)
	{
		this.presenter= presenter;
		command = ProxyCommands.GetLogicalExercises;
		
	}

}
