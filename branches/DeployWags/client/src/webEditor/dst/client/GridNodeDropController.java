package webEditor.dst.client;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.allen_sauer.gwt.dnd.client.drop.GridConstrainedDropController;
import com.google.gwt.user.client.ui.Widget;


public class GridNodeDropController extends GridConstrainedDropController {
	private static GridNodeDropController dc;
	private static AbsolutePanel boundaryPanel;
	private static int gridX;
	private static int gridY;
	
	public static GridNodeDropController getInstance()
	{
		if(dc == null)
			dc = new GridNodeDropController(boundaryPanel, gridX, gridY);
		return dc;
	}
	
	private GridNodeDropController(AbsolutePanel boundaryPanel, int gridX, int gridY)
	{
		super(boundaryPanel, gridX, gridY);
	}
		
	public static void setFields(AbsolutePanel theBoundaryPanel, int theGridX, int theGridY)
	{
		boundaryPanel = theBoundaryPanel;
		gridX=theGridX;
		gridY=theGridY;
		dc = new GridNodeDropController(boundaryPanel, gridX, gridY);
	}
	
	@Override
	public void drop(Widget widget, int left, int top) {
		left = Math.max(0, Math.min(left, boundaryPanel.getOffsetWidth() - widget.getOffsetWidth()));
	    top = Math.max(0, Math.min(top, boundaryPanel.getOffsetHeight() - widget.getOffsetHeight()));
	    left = (Math.round((float) left / gridX) * gridX);
	    top = (Math.round((float) top / gridY) * gridY);
	    boundaryPanel.add(widget, left, top);
	}
}
