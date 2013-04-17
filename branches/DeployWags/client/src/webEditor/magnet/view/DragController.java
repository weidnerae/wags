package webEditor.magnet.view;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public enum DragController {
	INSTANCE;
	
	private final PickupDragController dc;
	
	private DragController() {
		dc = new PickupDragController(RootPanel.get(), false);
		dc.setBehaviorDragProxy(true);
	}

	public void registerDropController(DropController dropController) {
		dc.registerDropController(dropController);
	}

	public void makeDraggable(Widget draggable) {
		dc.makeDraggable(draggable);
	}

	public void unregisterDropController(DropController dropController) {
		dc.unregisterDropController(dropController);
	}

	public void unregisterDropControllers() {
		dc.unregisterDropControllers();
	}
}
