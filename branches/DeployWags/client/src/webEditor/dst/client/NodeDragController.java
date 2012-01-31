package webEditor.dst.client;

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
		if(dc == null)
			dc = new NodeDragController(boundaryPanel, allowDroppingOnBoundaryPanel);
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
	}
	
	public void dragEnd()
	{
		super.dragEnd();
		ec.updateEdgeDrawings();
	}
	
	public static void reset()
	{
		dc = null;
	}
}
