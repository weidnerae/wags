package webEditor.magnet.view;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MagnetTypePanel extends HorizontalPanel{
	private VerticalPanel leftPanel, rightPanel;
	private int longestLength = 0;
	private boolean onePanelMode = false;
	
	public MagnetTypePanel(){
		leftPanel = new VerticalPanel();
		rightPanel = new VerticalPanel();
		super.add(leftPanel);
		super.add(rightPanel);
	}

	public void onResize(int newWidth) {
		updateLength();
		if(newWidth < 1.75*longestLength && !onePanelMode){
			onePanelMode = true;
			while(rightPanel.getWidgetCount() > 0){
				leftPanel.add(rightPanel.getWidget(0));
			}
		}else if(newWidth >= 1.75*longestLength && onePanelMode){
			onePanelMode = false;
			int numLeftWidgets = leftPanel.getWidgetCount()+1;
			int count = 0;
			while(count < (numLeftWidgets/2)){
				rightPanel.add(leftPanel.getWidget((numLeftWidgets/2)));
				count++;
			}
		}
		
	}
	
	@Override
	public void add(Widget w){
		if(onePanelMode){
			leftPanel.add(w);
		}else{
			if(leftPanel.getWidgetCount() > rightPanel.getWidgetCount()){
				rightPanel.add(w);
			}else{
				leftPanel.add(w);
			}
		}
		
		if(w.getOffsetWidth() > longestLength){	
			longestLength = w.getOffsetWidth();
		}
	}
	
	public void updateLength(){
		for(int i=0; i < leftPanel.getWidgetCount(); i++){
			if(leftPanel.getWidget(i).getOffsetWidth() > longestLength){
				longestLength = leftPanel.getWidget(i).getOffsetWidth();
			}
		}
		for(int i=0; i < rightPanel.getWidgetCount(); i++){
			if(rightPanel.getWidget(i).getOffsetWidth() > longestLength){
				longestLength = rightPanel.getWidget(i).getOffsetWidth();
			}
		}
	}

}
