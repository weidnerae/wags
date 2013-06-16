package webEditor.admin.builders;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class BasicDragController extends PickupDragController {
	BasicCanvas builder;
	
	public BasicDragController(AbsolutePanel boundaryPanel,
			boolean allowDroppingOnBoundaryPanel) {
		super(boundaryPanel, allowDroppingOnBoundaryPanel);
	}
	
	public BasicDragController(AbsolutePanel boundaryPanel,
			boolean allowDroppingOnBoundaryPanel, BasicCanvas builder) {
		super(boundaryPanel, allowDroppingOnBoundaryPanel);
		this.builder = builder;
		this.setBehaviorDragStartSensitivity(1);
	}
	
	public void dragEnd(){
		super.dragEnd();
	}

}
