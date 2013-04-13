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
		int index = content.indexOf(' ');
		String firstWord = content.substring(0, index);
		
		if(firstWord.equals("for")){
			magnetMaker.incrementLimitCounter(0);
		} else if(firstWord.equals("while")){
			magnetMaker.incrementLimitCounter(1);
		} else if(firstWord.equals("if")){
			magnetMaker.incrementLimitCounter(2);
		} else if(firstWord.equals("else")){
			if(content.substring(index+1,content.indexOf(' ',index+1)).equals("if")){ // "else if"
				magnetMaker.incrementLimitCounter(3);
			} else{
				magnetMaker.incrementLimitCounter(4);
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
