package wags.logical;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class NodeDragController extends PickupDragController implements IsSerializable
{
	private static NodeDragController dc;
	private static AbsolutePanel boundaryPanel; 
	private static EdgeCollection ec;
	private static boolean allowDroppingOnBoundaryPanel;
	
	public static NodeDragController getInstance()
	{
		if (dc == null) {
			dc = new NodeDragController(boundaryPanel, allowDroppingOnBoundaryPanel);
		}
		
		dc.setBehaviorDragStartSensitivity(1); // make double clicking work
		
		return dc;
	}
	
	private NodeDragController(AbsolutePanel boundaryPanel, boolean allowDroppingOnBoundaryPanel)
	{
		super(boundaryPanel, allowDroppingOnBoundaryPanel);
	}
	
	public static void setFields(AbsolutePanel theBoundaryPanel,
			boolean allowDrop, EdgeCollection theEc) 
	{
		boundaryPanel = theBoundaryPanel;
		ec = theEc;
		allowDroppingOnBoundaryPanel = allowDrop;
		dc = new NodeDragController(boundaryPanel, allowDroppingOnBoundaryPanel);
	}
	
	public void dragEnd()
	{
		super.dragEnd();
		ec.updateEdgeDrawings();
	}
	
}
