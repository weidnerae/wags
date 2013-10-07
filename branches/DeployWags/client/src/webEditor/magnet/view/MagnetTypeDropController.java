package webEditor.magnet.view;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public class MagnetTypeDropController extends AbsolutePositionDropController{
	private ConstructUi construct;

	public MagnetTypeDropController(AbsolutePanel dropPanel, ConstructUi construct){
		super(dropPanel);
		this.construct = construct;
	}
	
	@Override
	public void drop(Widget widget, int x, int y){
		construct.addSegment((StackableContainer) widget);
	}
	
	@Override
	public void onDrop(DragContext context) {
		context.draggable.removeFromParent();
		construct.addSegment((StackableContainer)context.draggable);
		
		super.onDrop(context);
	}
}
