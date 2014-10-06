package webEditor.ProxyFramework;

import com.google.gwt.http.client.Response;
import webEditor.WEStatus;
import com.google.gwt.user.client.ui.ListBox;

public class GetUsernamesCommand extends AbstractServerCall {

	private ListBox users;
	
		protected void handleResponse(Response response)
		{
			WEStatus status = new WEStatus(response);
			
			if (status.getStat() == WEStatus.STATUS_SUCCESS) 
			{
				if (status.getMessageArray().length > 0) 
				{
					users.clear();
					String[] message = status.getMessageArray();
					
					for (int i = 0; i < message.length; i++)
					{
						users.addItem(message[i].substring(1, message[i].length() - 1));
					}
				}
				
				else 
				{
					users.addItem(status.getMessage());
				}
			}
		}
		
		public GetUsernamesCommand(final ListBox users)
		{
			command = ProxyCommands.GetAllUsers;
			this.users = users;			
		}
		

}
