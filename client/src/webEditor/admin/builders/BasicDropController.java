package webEditor.admin.builders;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class BasicDropController extends AbsolutePositionDropController {
	AbsolutePanel dropTarget;
	
	public BasicDropController(AbsolutePanel dropTarget) {
		super(dropTarget); 
		this.dropTarget = dropTarget;	
	}

	public void onDrop(DragContext context){
		if (context.draggable instanceof Node_Basic){
			Node_Basic node = (Node_Basic) context.draggable;
			node.xPos = context.desiredDraggableX - dropTarget.getAbsoluteLeft();
			node.yPos = context.desiredDraggableY - dropTarget.getAbsoluteTop();
			
			node.reDrawEdges();
		}
		super.onDrop(context);
	}
}
