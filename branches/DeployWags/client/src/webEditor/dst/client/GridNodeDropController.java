package webEditor.dst.client;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.allen_sauer.gwt.dnd.client.drop.GridConstrainedDropController;

public class GridNodeDropController extends GridConstrainedDropController {
	private static GridNodeDropController dc;
	//private static NodeCollection nc;
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
}
