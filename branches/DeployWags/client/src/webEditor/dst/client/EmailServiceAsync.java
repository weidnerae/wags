package webEditor.dst.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EmailServiceAsync {
	
	void email(String problemName, AsyncCallback<String> callback);
}
