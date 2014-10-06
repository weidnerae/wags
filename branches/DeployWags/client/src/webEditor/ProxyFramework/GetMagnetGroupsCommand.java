package webEditor.ProxyFramework;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.HashMap;

import webEditor.Proxy;
import webEditor.WEStatus;

public class GetMagnetGroupsCommand extends AbstractServerCall {

	private ListBox magnetExercises;
	private VerticalPanel selectionPanel;
	private ArrayList<CheckBox>	currentMagnets;
	private HashMap<String, CheckBox> allMagnets;
	private ListBox lstMagnetExercises;
	
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
		String[] problemList = status.getMessageArray();
		magnetExercises.clear();
		
		for (int i = 0; i < problemList.length; i++)
		{
			magnetExercises.addItem(problemList[i], problemList[i]);
		}
		
		if (selectionPanel == null)
		{
			return;
		}
		
		AbstractServerCall cmd = new GetMagnetsByGroupCommand(problemList[0], selectionPanel, currentMagnets,
				allMagnets, lstMagnetExercises);
		cmd.sendRequest();
	}
	
	public GetMagnetGroupsCommand(final ListBox magnetExercises, final VerticalPanel selectionPanel, 
			final ArrayList<CheckBox> currentMagnets, final HashMap<String, CheckBox> allMagnets, 
			final ListBox lstMagnetExercises)
	{
		command = ProxyCommands.GetMagnetGroups;
		this.magnetExercises = magnetExercises;
		this.selectionPanel = selectionPanel;
		this.currentMagnets = currentMagnets;
		this.allMagnets = allMagnets;
		this.lstMagnetExercises = lstMagnetExercises;
	}
	
}
