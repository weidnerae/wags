package webEditor.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;

/**
 * Custom callback class.
 * This will take away some repetitive work.
 */
abstract class WagsCallback implements RequestCallback
{
	abstract void success(WEStatus status);
	abstract void error(WEStatus status);
	abstract void warning(WEStatus status);

	@Override
	public void onError(Request request, Throwable exception) { Window.alert("LOL"); }

	@Override
	public void onResponseReceived(Request request, Response response) {
		WEStatus status = new WEStatus(response);
		if(status.getStat() == WEStatus.STATUS_SUCCESS){
			this.success(status);
		}else if(status.getStat() == WEStatus.STATUS_ERROR){
			this.error(status);
		}else if(status.getStat() == WEStatus.STATUS_WARNING){
			this.warning(status);
		}else{
			/* TODO: Do we need a default handler? */
			return;
		}
	}
	
}