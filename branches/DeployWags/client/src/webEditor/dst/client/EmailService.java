package webEditor.dst.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("emailServiceServlet")
public interface EmailService extends RemoteService 
{
	String email(String problemName);
}