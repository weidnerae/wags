package webEditor.client;

import java.util.Stack;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;

public class CompletionCheck {
	private ColorCounter curColor;
	final static String PURPLE = "#B300B3";
	final static String RED = "#CC0000";
	final static String GREEN = "#336633";
	final static String LIGHTBLUE = "#0066FF";
	final static String DARKBLUE = "#0000CC";
	final static String BLACK = "#000000";
	
	//Will probably want to move this to an external class later, for now
	//using in color completion checking.  Suffix 'S' means it needs
	//a shift as well
	private static final int QUOTE = 222;
//	private static final int FSLASH = 191;
//	private static final int SPLATS = 56;
	private static final int OCURLS = 219;
	private static final int CCURLS = 221;
	private static final int OPARENS = 57;
	private static final int CPARENS = 48;
	private static final int BACKSPACE = 8;
	private static final int SHIFT = 16;
	private boolean newSQuote;
	private boolean newDQuote;
	private int lastKeyUp;

	private Stack<ColorCounter> stack = new Stack<ColorCounter>();
	
	public CompletionCheck(){
		//curColor starts black
		curColor = new ColorCounter(BLACK);
		curColor.lastColor = null;
	}
	
	public String pushCheck(KeyDownEvent event){
		int key = event.getNativeKeyCode();
		boolean shift = event.isShiftKeyDown();
		
		//Checks to see if a block/quote was started
		if(key == OPARENS && shift){ 				// (
			stack.push(curColor);
			curColor = new ColorCounter(PURPLE);
		} 
		
		else if (key == OCURLS && shift){ 		// {
			stack.push(curColor);
			curColor = new ColorCounter(GREEN);
		}
		
		else if (key == OCURLS){ 					// [
			stack.push(curColor);
			curColor = new ColorCounter(RED);
		} 
		
		else if (key == QUOTE && shift){ 			// "
			if(curColor.COLOR != LIGHTBLUE){
				stack.push(curColor);
				curColor = new ColorCounter(LIGHTBLUE);
				newDQuote = false;
			}
		} 
		
		else if (key == QUOTE) { 					// '
			if(curColor.COLOR != DARKBLUE && curColor.COLOR != LIGHTBLUE){
				stack.push(curColor);
				curColor = new ColorCounter(DARKBLUE);
				newSQuote = false;
			}
		}
	
		
		//special delete key logic
		if(key == BACKSPACE){
			curColor.count = curColor.count - 1;
			
			//Problem: open parenthesis needs 0
			//closed wants -1
			//If it's black, you can't pop
			if(curColor.count == -1 && curColor.lastColor != null){
				if(curColor.myOpener != null){
					curColor.myOpener.closed = false;
				}
				curColor = stack.pop();
				curColor.count = curColor.count - 1;
				if (curColor.COLOR == LIGHTBLUE) newDQuote = true;
				if (curColor.COLOR == DARKBLUE) newSQuote = true;
			}

			
		} else {
			//Set right before return in case color changes
			if(!skipKey(key)){
				curColor.setCount(curColor.count + 1);
			}
		}
		
		return curColor.COLOR;
	}
	
	public String popCheck(KeyUpEvent event){
		int key = event.getNativeKeyCode();
		boolean shift = event.isShiftKeyDown();
		
		String checkColor = curColor.COLOR;
		
		if(shift || lastKeyUp == SHIFT){
			switch (key){
				case CPARENS: 	// )
					if(checkColor == PURPLE){
						curColor.closed = true;
						stack.push(curColor);
						curColor = new ColorCounter(curColor.giveColor());
					}
					break;
				case CCURLS:  	// }
					if(checkColor == GREEN){
						curColor.closed = true;
						stack.push(curColor);
						curColor = new ColorCounter(curColor.giveColor());
					}
					break;
				case QUOTE:  	// "
					if(checkColor == LIGHTBLUE && newDQuote){
						curColor.closed = true;
						stack.push(curColor);
						curColor = new ColorCounter(curColor.giveColor());
					}
					newDQuote = true;
					break;
				default:
					break;
			}
		} else {
			switch (key){
				case CCURLS:  	// ]
					if(checkColor == RED){
						curColor.closed = true;
						stack.push(curColor);
						curColor = new ColorCounter(curColor.giveColor());
					}
					break;
				case QUOTE: 	// '
					if(checkColor == DARKBLUE && newSQuote){
						curColor.closed = true;
						stack.push(curColor);
						curColor = new ColorCounter(curColor.giveColor());
					}
					newSQuote = true;
					break;
				default:
					break;
			}
		}
		
		lastKeyUp = key;
		
		return curColor.COLOR;
	}
	
	private boolean skipKey(int key){
		if(key == SHIFT || key == 9 || key == 20 || 
				key == 17 || key == 18 || key == 36 || key == 45)
			return true;
		
		return false;
	}

	public void enterIncrement(int inc){
		curColor.count += inc;
	}
	
	private class ColorCounter{
		private String COLOR;
		private int count;
		private ColorCounter lastColor;
		private ColorCounter myOpener;
		boolean closed;
				
		public ColorCounter(String hexColor){
			COLOR = hexColor;
			count = 0;
			if(!stack.empty()){
				lastColor = stack.peek(); //needed to avoid null pointer for first curColor
			}
			closed = false;
			myOpener = null;
		}
		
		private void setCount(int count){
			this.count = count;
		}
		
		private String giveColor(){
			return giveColor(false);
		}
		
		private String giveColor(boolean stop){
			if(closed) return lastColor.giveColor(stop);
			
			if(curColor.closed == true && curColor.COLOR == this.COLOR && stop == false){ 
				this.closed = true;
				curColor.setOpener(this);
				stop = true;
			}
			
			if(closed) return lastColor.giveColor(stop);
			
			return COLOR;
		}
		
		private void setOpener(ColorCounter opener){
			myOpener = opener;
		}
	}
	
}
