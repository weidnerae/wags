package webEditor.admin.builders;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class BasicDragController extends PickupDragController {
	BasicBuilder builder;
	
	public BasicDragController(AbsolutePanel boundaryPanel,
			boolean allowDroppingOnBoundaryPanel) {
		super(boundaryPanel, allowDroppingOnBoundaryPanel);
		// TODO Auto-generated constructor stub
	}
	
	public BasicDragController(AbsolutePanel boundaryPanel,
			boolean allowDroppingOnBoundaryPanel, BasicBuilder builder) {
		super(boundaryPanel, allowDroppingOnBoundaryPanel);
		this.builder = builder;
		this.setBehaviorDragStartSensitivity(1);
	}
	
	public void dragEnd(){
		super.dragEnd();
	}

}
