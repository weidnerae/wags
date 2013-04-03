package webEditor;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ScrollEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;



public class Notification extends View {

        private static NotificationUiBinder uiBinder = GWT.create(NotificationUiBinder.class);
        private static PopupPanel popup;
        interface NotificationUiBinder extends UiBinder<Widget, Notification> {
        }
        
        // Style for notification. Associated with notification.css
        interface NotificationStyle extends CssResource {
                String error();
                String success();
                String warning();
                String popuppanel();
        }
        
        // Timer is static so only one instance
        //      - Allows it to be cancelled if needed
        private final static Timer t = new Timer() {
                @Override
                public void run()
                {
                        Notification.clear();
                }
        };

        @UiField Label notificationArea;
        @UiField static PopupPanel panel;
        @UiField static NotificationStyle style;

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
                Notification.clear();
                Notification n = new Notification(status, msg);
                
                popup = new PopupPanel();
                popup.getElement().addClassName(style.popuppanel());
                popup.setWidth(Window.getClientWidth() - 8 +"px");
                popup.add(n);
                Notification.NotificationScroll nscroll = new Notification.NotificationScroll();
                Window.addWindowScrollHandler(nscroll);
                
                RootPanel.get().add(popup,0,Window.getScrollTop());
                // Automatically clear notification in 5 seconds.
                t.schedule(3000);
        }
        
        public static void clear()
        {
        	Element parent = DOM.getElementById("notification-area");
        	for(int i = 0; i < parent.getChildCount(); i++){
        		parent.removeChild(parent.getChild(i));
        	}
        	
            for(int i = 0; i < RootPanel.get().getWidgetCount(); i++){
                if(RootPanel.get().getWidget(i) instanceof PopupPanel){
                	// In order to not also remove the password popup, we check to make sure this widget
                	// is super long...
                	if(RootPanel.get().getWidget(i).getOffsetWidth() >= Window.getClientWidth() - 8)
                		RootPanel.get().remove(i);
                }
            }
        }
        
        // Cancel timer's current schedule
        public static void cancel()
        {
                t.cancel();
        }
        
        private static class NotificationScroll implements Window.ScrollHandler{
			@Override
			public void onWindowScroll(ScrollEvent event) {
				popup.setPopupPosition(0, Window.getScrollTop());				
			}
        	
        }
        
}
