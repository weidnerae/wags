package webEditor.ProxyFramework;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.HashMap;

import webEditor.WEStatus;

public class GetMagnetsByGroupCommand extends AbstractServerCall {
	
	private VerticalPanel exercisePanel;
	private ArrayList<CheckBox> currentMagnets;
	private HashMap<String, CheckBox> allMagnets;
	private ListBox lstMagnetExercises;
	
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
		String[] exercises = status.getMessageArray();
		
		if (exercisePanel == null)
		{
			lstMagnetExercises.clear();
			for (int i = 0; i < exercises.length; i++)
			{
				String name = exercises[i].substring(1, exercises[i].length() - 1);
				lstMagnetExercises.addItem(name, name);
			}
			return;
		}
		
		if (!currentMagnets.isEmpty())
		{
			for (CheckBox magnet: currentMagnets)
			{
				magnet.setVisible(false);
			}
		}
		
		CheckBox chk;
		lstMagnetExercises.clear();
		for (int i = 0; i < exercises.length; i++)
		{
			String name = exercises[i].substring(1, exercises[i].length() - 1);
			lstMagnetExercises.addItem(name, name);
			
			if (allMagnets.containsKey(name))
			{
				chk = allMagnets.get(name);
				chk.setVisible(true);
			}
			else
			{
				chk = new CheckBox(name);
				allMagnets.put(name, chk);
			}
			
			exercisePanel.add(chk);
			currentMagnets.add(chk);
		}
	}
	
	public GetMagnetsByGroupCommand(String groupName, final VerticalPanel exercisePanel, 
			final ArrayList<CheckBox> currentMagnets, final HashMap<String, CheckBox> allMagnets, 
			final ListBox lstMagnetExercises)
	{
		command = ProxyCommands.GetMagnetsByGroup;
		addArgument("group", groupName);
		this.exercisePanel = exercisePanel;
		this.currentMagnets = currentMagnets;
		this.allMagnets = allMagnets;
		this.lstMagnetExercises = lstMagnetExercises;
	}
}
