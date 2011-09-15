package webEditor.client;

import java.util.Stack;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.user.client.ui.RichTextArea;

public class TabCheck {
	private Stack<TabCounter> tabStack = new Stack<TabCounter>();
	private TabCounter curTab;
	private RichTextArea editor;
	private int lastKey;
	private int tabCount = 3; //<--- scary workaround - usually, will be writing at the method
	//level.  Thus, it is set to default.
	
	private static final int OCURLS = 219;
	private static final int CCURLS = 221;
	private static final int OPARENS = 57;
	private static final int CPARENS = 48;
	private static final int SHIFT = 16;
	private static final int BACKSPACE = 8;
	private static final int ENTER = 13;
	
	public TabCheck(RichTextArea editor){
		this.editor = editor;
		curTab = new TabCounter(true);
		curTab.count = 1000;
	}
	
	public void pushCheck(KeyDownEvent event){
		int key = event.getNativeKeyCode();
		boolean shift = event.isShiftKeyDown();
		
		switch(key){
			case OPARENS:
				if (shift) tabCount++;
				tabStack.push(curTab);
				curTab = new TabCounter(true);
				break;
			case OCURLS:
				tabCount++;
				tabStack.push(curTab);
				curTab = new TabCounter(true);
				break;
			case CPARENS:
				if (shift){
					tabCount--;
					if(lastKey == ENTER) removeTab();
					tabStack.push(curTab);
					curTab = new TabCounter(false);
				}
				
				break;
			case CCURLS:
				tabCount--;
				if (lastKey == ENTER) removeTab();
				tabStack.push(curTab);
				curTab = new TabCounter(false);
				break;
		}
		
		if (key == BACKSPACE){
			curTab.count--;
		}
		else if (!skipKey(key)){
			curTab.count++;
		}
		
		if(curTab.count == 0){
			if(curTab.open){
				tabCount--;
			} else {
				tabCount ++;
			}
			
			curTab = tabStack.pop();
			curTab.count = curTab.count - 1;
		}
				
		lastKey = key;
	}
	
	public void enterIncrement(int inc){
		curTab.count += inc;
	}
	
	private void removeTab(){
		String text = editor.getHTML();
		text = text.substring(0, text.length()-5);
		editor.setHTML(text);
	}
	
	
	private boolean skipKey(int key){
		if(key == SHIFT || key == 9 || key == 20 || 
				key == 17 || key == 18 || key == 36 || key == 45)
			return true;
		
		return false;
	}
	
	public int getTabCount(){
		return tabCount;
	}
	
	public void setTabCount(int count){
		tabCount = count;
	}
	
	private class TabCounter{
		final boolean open;
		int count;
		
		public TabCounter(boolean open){
			this.open = open;
			count = 0;
		}
	}
}
