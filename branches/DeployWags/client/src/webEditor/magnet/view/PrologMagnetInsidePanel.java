package webEditor.magnet.view;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public class PrologMagnetInsidePanel extends AbsolutePanel{

		public PrologMagnetInsidePanel(){
			super();
		}
		
		@Override
		public boolean remove(Widget w){
			boolean result = super.remove(w);
			if(result && getWidgetCount()>0){
				((StackableContainer)getWidget(0)).removeComma();
			}
			return result;
		}
}
