package webEditor.magnet.client;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;


/**
 * Stackable Container drop controller that calls method within the stackableContainer class
 * to achieve the stacking functionality.  This is mostly glue code that connects 
 * stackableContainer's stacking methods with the drag and drop events that occur as the user drags magnets.
 *
 */
public class PanelDropController extends SimpleDropController {

	private final StackableContainer dropTarget;

	public PanelDropController(StackableContainer dropTarget) {
		super(dropTarget);
		this.dropTarget = dropTarget;
	}

	@Override
	public void onDrop(DragContext context) {
		if (dropTarget.isStackable()) {
			dropTarget.addInsideContainer(
					(StackableContainer) context.selectedWidgets.get(0),
					context);
		}
	}

	@Override
	public void onEnter(DragContext context) {
		super.onEnter(context);
		if (dropTarget.getWidget() != null) {
			dropTarget.setEngaged(true);
		}
	}

	@Override
	public void onLeave(DragContext context) {
		dropTarget.setEngaged(false);
		super.onLeave(context);
	}

	@Override
	public void onPreviewDrop(DragContext context) throws VetoDragException {
		if (dropTarget.getWidget() == null || !dropTarget.isStackable()) {
			throw new VetoDragException();
		}
		super.onPreviewDrop(context);
	}

}
