package webEditor.client.view;

import webEditor.client.WEStatus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;



public class Notification extends View {

	private static NotificationUiBinder uiBinder = GWT.create(NotificationUiBinder.class);
	interface NotificationUiBinder extends UiBinder<Widget, Notification> {
	}
	
	// Style for notification. Associated with notification.css
	interface NotificationStyle extends CssResource {
		String error();
		String success();
		String warning();
	}
	
	// Timer is static so only one instance
	// 	- Allows it to be cancelled if needed
	private final static Timer t = new Timer() {
		@Override
		public void run()
		{
			Notification.clear();
		}
	};

	@UiField Label notificationArea;
	@UiField PopupPanel panel;
	@UiField NotificationStyle style;

	public Notification(int status, String msg) 
	{
		initWidget(uiBinder.createAndBindUi(this));
		
		// Panel should gain a little attention.
		panel.setAnimationEnabled(true);
		//panel.setAutoHideEnabled(true);
		Element pEl = panel.getElement();

		// Add class to panel so styling comes out right.
		switch(status){
			case WEStatus.STATUS_ERROR:
				pEl.addClassName(style.error());
				break;
			case WEStatus.STATUS_SUCCESS:
				pEl.addClassName(style.success());
				break;
			case WEStatus.STATUS_WARNING:
				pEl.addClassName(style.warning());
		}

		notificationArea.setText(msg);
	}

	@Override
	public WEAnchor getLink() {
		return null;
	}
	
	public static void notify(int status, String msg)
	{
		Notification.cancel();
		Element na = DOM.getElementById("notification-area");
		Notification.clear();
		Notification n = new Notification(status, msg);
		int cw = Window.getClientWidth(); // width of the window
		int sl = Window.getScrollLeft();  // distance from left side of window to left side of viewport
		
		// Width of the notification is 40% of the width of the window (from CSS)
		int nWidth = (int)(cw*.4);
		
		// Getting the viewport size, Super gross.
		int oldLeft = Window.getScrollLeft();
		int oldTop = Window.getScrollTop();
		Window.scrollTo(Window.getClientWidth(), 0);
		int viewportWidth = Window.getClientWidth()-Window.getScrollLeft();		
		Window.scrollTo(oldLeft,oldTop);
			
		int xPos = (sl+(((int)viewportWidth/2)-(nWidth)/2));
		
		RootPanel.get().add(n,xPos,Window.getScrollTop());		
		// Automatically clear notification in 5 seconds.
		t.schedule(5000);
	}
	
	public static void clear()
	{
		for(int i = 0; i < RootPanel.get().getWidgetCount(); i++){
			if(RootPanel.get().getWidget(i) instanceof Notification)
				RootPanel.get().remove(i);
		}
	}
	
	// Cancel timer's current schedule
	public static void cancel()
	{
		t.cancel();
	}
	
	@UiHandler("notificationArea")
	void onPanelClick(ClickEvent event){
		//Notification.clear();
	}
	
}
