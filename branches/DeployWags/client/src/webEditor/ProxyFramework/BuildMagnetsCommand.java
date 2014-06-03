package webEditor.ProxyFramework;

import webEditor.WEStatus;
import webEditor.Wags;
import webEditor.magnet.view.Magnets;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.http.client.Response;

/**
 * @author Dakota Murray
 * 
 * Server File: GetMagnetExercises.php
 * Arguments: None
 * Method: GET
 * Modifies: Main WAGS object, changes the center content panel
 */
public class BuildMagnetsCommand extends AbstractCommand {

	private Wags wags;
	
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
		
		Magnets Magnets = new Magnets(idList, problemsList, statusList, wags);
		wags.splashPage = Magnets;
		Magnets.getElement().getStyle().setOverflowY(Overflow.AUTO);
		wags.replaceCenterContent(Magnets);

	}
	
	public BuildMagnetsCommand(Wags wags)
	{
		this.wags = wags;
		command = ProxyCommands.GetMagnetExercises;
	}

}
