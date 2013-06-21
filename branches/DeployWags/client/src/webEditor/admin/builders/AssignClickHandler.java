package webEditor.admin.builders;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;

public class AssignClickHandler implements ClickHandler {
	BasicDisplay display;
	ArgHolder holder;
	
	public AssignClickHandler(BasicDisplay display){
		this.display = display;
		this.holder = null;
	}
	
	public AssignClickHandler(BasicDisplay display, ArgHolder holder){
		this.display = display;
		this.holder = holder;
	}

	@Override
	public void onClick(ClickEvent event) {
		if(holder != null){
			if(holder.getArguments().length == 0){
				Window.alert("No arguments provided!");
				return;
			}
		}
		display.fillBuilder(holder);
	}

}
