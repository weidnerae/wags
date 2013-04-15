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
	
	private CreationStation magnetMaker;

	public TrashBin(CreationStation magnetMaker) {
		this.magnetMaker = magnetMaker;
		HTML text = new HTML("<b>Trash Bin</b>");
		text.setStyleName("trash_label");
		add(text);
		setStyleName(CSS_TRASHBIN);
	}

	public void eatWidget(StackableContainer sc) {
		String content = sc.getContent();
		
		/*
		 * I don't think this is correct. It will increase limits in the 
		 * magnet maker even if the magnet wasn't created with the magnet 
		 * maker. Am I wrong?
		 */
		if (content.startsWith("for")) {
			magnetMaker.incrementLimitCounter(CreationStation.FOR);
		} else if (content.startsWith("while")) {
			magnetMaker.incrementLimitCounter(CreationStation.WHILE);
		} else if (content.startsWith("if")) {
			magnetMaker.incrementLimitCounter(CreationStation.IF);
		} else if (content.startsWith("else if")) {
			magnetMaker.incrementLimitCounter(CreationStation.ELSE_IF);
		} else if (content.startsWith("else")) {
			magnetMaker.incrementLimitCounter(CreationStation.ELSE);
		}
		
		sc.removeFromParent();
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
