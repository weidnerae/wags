package webEditor.magnet.view;

/**
 * Standard trash bin behavior, eats any stackablecontainer that is dropped into its drop area
 */

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.AbsolutePanel;

/**
 * Panel that removes StackableContainers that are dragged onto it.
 */
final class TrashBin extends AbsolutePanel {
	private static final String CSS_TRASHBIN = "trash_bin";
	private static final String CSS_TRASHBIN_ENGAGE = "trash_bin-engage";
    private ConstructUi construct;
    
	public TrashBin(ConstructUi construct) {
		this.construct = construct;
		HTML text = new HTML("<b>Trash Bin</b>");
		text.setStyleName("trash_label");
		add(text);
		setStyleName(CSS_TRASHBIN);
	}
	
	/**
	 * resets any magnet fed to the bin. 
	 * 
	 * If the magnet being fed to the trash bin is premade then that magnet 
	 * will be reset to its origional spot and all magnets stacked inside of 
	 * it will also be reset.
	 * 
	 * If the magnet fed to the trash bin is one created by the magnet maker 
	 * then it will be removed from display entirely and the limits for that
	 * magnet type will be incremented. Any magnets stacked inside of the created
	 * one will also be reset. 
	 * 
	 * @param sc the magnet being fed to the trash bin
	 */
	public void eatWidget(StackableContainer sc) {
		String content = sc.getContent();
		int pos = 0;
		int widgetCount = sc.getInsidePanel().getWidgetCount();
		for(int i = 0; i < widgetCount; i++){
			eatWidget((StackableContainer)sc.getInsidePanel().getWidget(pos));
			if(sc.getInsidePanel().getWidgetCount() == widgetCount) {
				pos++;
			}
		}
		String type = sc.getMagnetType().toString();
		if(type.equals("function") || type.equals("main")) {
		    //do nothing
		}
		else if (sc.isCreated()) {
			construct.manageLimits(content);
			sc.removeFromParent();
		}else{
			sc.removeFromParent();
			this.construct.addSegment(sc);
		}
	}

	
	public boolean isWidgetEater() {
		return true;
	}

	public void setEngaged(boolean engaged) {
		if (engaged) {
			setStyleName(CSS_TRASHBIN_ENGAGE);
		} else {
			setStyleName(CSS_TRASHBIN);
		}
	}

}
