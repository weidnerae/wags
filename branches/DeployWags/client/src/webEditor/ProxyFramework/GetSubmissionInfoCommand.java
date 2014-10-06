package webEditor.ProxyFramework;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Grid;

import webEditor.WEStatus;

public class GetSubmissionInfoCommand extends AbstractServerCall {

	private Grid grid;
	final int NUM_COLUMNS = 5;
	
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
		String[] subInfo = new String[status.getMessageArray().length];
		subInfo = status.getMessageArray();
		
		for (int i = 3; i < subInfo.length; i += NUM_COLUMNS)
		{
			if (subInfo[i] == "1") subInfo[i] = "Yes";
			else if (subInfo[i] == "0") subInfo[i] = "No";
		}
		
		grid.resize(subInfo.length/NUM_COLUMNS + 1, NUM_COLUMNS);
		grid.setBorderWidth(1);
		
		//Sets the headers for the table
  		grid.setHTML(0, 0, "<b> Username </b>");
  		grid.setHTML(0, 1, "<b> File </b>");
  		grid.setHTML(0, 2, "<b> NumAttempts </b>");
  		grid.setHTML(0, 3, "<b> Correct </b>");
  		grid.setHTML(0, 4, "<b> Partner </b>");
  		
  		int k = 0;
  		//Fills table with results from AdminReview.php
  	    for (int row = 1; row < subInfo.length/NUM_COLUMNS+1; ++row) 
  	    {
  	    	
  	      for (int col = 0; col < NUM_COLUMNS; ++col)
  	      {
  	    	
  	    	//numAttempts is 0 based on server, so increment
  	    	if(col == 2){
  	    		int numAttempts = Integer.parseInt(subInfo[k++]);
  	    		numAttempts++;
  	    		grid.setText(row, col, numAttempts + "");
  	    	} 
  	    	
  	    	else 
  	    	{
  	    		grid.setText(row, col, subInfo[k++]);
  	    	}
  	      }
  	    }
	}
	
	public GetSubmissionInfoCommand(String exercise, final Grid grid)
	{
		command = ProxyCommands.AdminReview;
		addArgument("Title", exercise);
		this.grid = grid;
	}
}
