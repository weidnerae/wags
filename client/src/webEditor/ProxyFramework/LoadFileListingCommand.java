package webEditor.ProxyFramework;

import com.google.gwt.http.client.Response;

import webEditor.Notification;
import webEditor.programming.view.FileBrowser;
import webEditor.WEStatus;

public class LoadFileListingCommand extends AbstractServerCall {

	private FileBrowser fileBrowser;
	private String path;
	
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
		
		if (status.getStat() == WEStatus.STATUS_SUCCESS)
		{
			fileBrowser.visibilities.clear();
			String[] list = status.getMessageArray();
			String[] fileNames = new String[list.length];
			int index = 0;
			
			for (String entry: list)
			{
				String name = entry.substring(0, entry.length() - 1);
				Integer vis = Integer.parseInt(entry.substring(entry.length() - 1));
				fileNames[index] = name;
				
				fileBrowser.visibilities.put(name, vis);
				index++;
			}
			
			fileBrowser.loadTree(fileNames);
			if (path != "") fileBrowser.openPath(path);
			else Notification.notify(WEStatus.STATUS_ERROR, "Error fetching file listing.");
		}
		
	}
	
	public LoadFileListingCommand(final FileBrowser fileBrowser, final String path)
	{
		this.path = path;
		this.fileBrowser = fileBrowser;
	}
	
}
