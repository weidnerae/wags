package webEditor.dst.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class Evaluation implements IsSerializable
{
	protected String errorMessage;
	
	public Evaluation()
	{
		errorMessage = "";
	}
	
	public abstract String evaluate(String problemName, String[] arguments, ArrayList<Node> nodes, ArrayList<EdgeParent> edges);
	
	// RPC to the email service
	public void emailResult(String problemName)
	{
		System.out.println("Email about to be sent");
		
		
		// Initialize the email service proxy.
		EmailServiceAsync emailService = (EmailServiceAsync) GWT.create(EmailService.class);

	    // Set up the callback object.
	    AsyncCallback<String> callback = new AsyncCallback<String>() {
	      public void onFailure(Throwable caught) {
	        System.out.println("There was an ASYNC CALLBACK error.");
	        caught.printStackTrace();
	      }

	      public void onSuccess(String str) {
	        System.out.println("Email was successful");
	      }
	    };

	    // Make the call to the email service.
	    emailService.email(problemName, callback);
		
	}
	
}
