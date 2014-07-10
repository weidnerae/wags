package webEditor.ProxyFramework;

import webEditor.Reviewer;
import webEditor.WEStatus;

import com.google.gwt.http.client.Response;

public class ReviewStudentCommand extends AbstractServerCall {

	private Reviewer pf;
	
	@Override
	protected void handleResponse(Response response) 
	{
		WEStatus status = new WEStatus(response);
        String subInfo[] = new String[status.getMessageArray().length];
        subInfo = status.getMessageArray();
        
     
        for (int i = 2; i < subInfo.length; i+=3){
        	
        	if(subInfo[i] == "1") subInfo[i] = "Yes";
        	else if (subInfo[i] == "0") subInfo[i] = "No";
        }
        pf.reviewCallback(subInfo);
	}
	
	public ReviewStudentCommand(String name, Reviewer pf)
	{
		addArgument("name", name);
		this.pf = pf;
		command = ProxyCommands.StudentReview;
	}

}
