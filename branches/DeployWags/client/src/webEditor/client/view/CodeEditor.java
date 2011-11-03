package webEditor.client.view;


import webEditor.client.TabCheck;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;



public class CodeEditor extends View implements HasHandlers
{
	

	private static CodeEditorUiBinder uiBinder = GWT
			.create(CodeEditorUiBinder.class);

	interface CodeEditorUiBinder extends UiBinder<Widget, CodeEditor>{}
	 
	@UiField
	public RichTextArea codeArea;
	@UiField public RichTextArea codeTop;
	@UiField public RichTextArea codeBottom;
	
//	private CompletionCheck colorCheck = new CompletionCheck();
	private TabCheck tabCheck = new TabCheck(codeArea);
//	private int lastKey;
//	private static final int CCURLS = 221;
//	private static final int CPARENS = 48;

	public CodeEditor()
	{
		initWidget(uiBinder.createAndBindUi(this));

		codeArea.setFocus(true);
		codeArea.setEnabled(true);
		codeArea.getFormatter().setFontName("monospace");
		
		codeTop.setEnabled(false);
		codeBottom.setEnabled(false);
		
		
		/*
		 * Commented to remove unsatisfactory coloring logic
		 * Currently relies on key presses, no functionality for 
		 * navigation around the text using a mouse
		 */
//		codeArea.addKeyDownHandler(new KeyDownHandler(){
//			public void onKeyDown(KeyDownEvent event)
//			{
//				//codeArea.getFormatter().setForeColor(colorCheck.pushCheck(event));
//				int ENTER = 13;
//				int key = event.getNativeKeyCode();
//						
//				if (lastKey == ENTER){
//					if (key == CCURLS || (key == CPARENS && event.isShiftKeyDown())){
//						indent(tabCheck.getTabCount()-1);
//					} else if (key != 16){
//						indent(tabCheck.getTabCount());
//					}
//				}
//				
//				tabCheck.pushCheck(event);
//				
//				if(event.getNativeKeyCode() != 16) lastKey = event.getNativeKeyCode();
//			}
//		});
		
//		codeArea.addKeyUpHandler(new KeyUpHandler(){
//			public void onKeyUp(KeyUpEvent event){
//				
//				if(event.getNativeKeyCode() == 13){ //Enter key
//					for(int i = 0; i < tabCheck.getTabCount(); i++)
//						codeArea.getFormatter().insertHTML("&nbsp; &nbsp; &nbsp;");
//					tabCheck.enterIncrement(tabCheck.getTabCount() * 5);
//				}
//				
//				//codeArea.getFormatter().setForeColor(colorCheck.popCheck(event));
//				//colorCheck.popCheck(event);
//			}
//		});
		
	}
	
	private void indent(int tabCount){
		for(int i = 0; i < tabCount; i++){
			codeArea.getFormatter().insertHTML("&nbsp;&nbsp;&nbsp;&nbsp;");
			tabCheck.enterIncrement(4);
		}
	}
	
	public void setContents(String contents){
		this.codeArea.setHTML(contents);
	}
	
	@Override
	public WEAnchor getLink()
	{
		return new WEAnchor("Editor", this, "codeEditor");
	}
	
	public TabCheck getTabCheck(){
		return this.tabCheck;
	}
	
}
