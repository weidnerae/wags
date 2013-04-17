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
	private MagnetMaker magnetMaker;

	public TrashBin(MagnetMaker magnetMaker) {
		this.magnetMaker = magnetMaker;
		HTML text = new HTML("<b>Trash Bin</b>");
		text.setStyleName("trash_label");
		add(text);
		setStyleName(CSS_TRASHBIN);
	}

	public void eatWidget(StackableContainer sc) {
		String content = sc.getContent();
		while (sc.getInsidePanel().getWidgetCount() > 0) {
			eatWidget((StackableContainer)sc.getInsidePanel().getWidget(0));
		}
		if (sc.isCreated()) {
			if (content.startsWith("for")) {
				magnetMaker.incrementLimitCounter(MagnetMaker.FOR);
			} else if (content.startsWith("while")) {
				magnetMaker.incrementLimitCounter(MagnetMaker.WHILE);
			} else if (content.startsWith("if")) {
				magnetMaker.incrementLimitCounter(MagnetMaker.IF);
			} else if (content.startsWith("else if")) {
				magnetMaker.incrementLimitCounter(MagnetMaker.ELSE_IF);
			} else if (content.startsWith("else")) {
				magnetMaker.incrementLimitCounter(MagnetMaker.ELSE);
			}
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
