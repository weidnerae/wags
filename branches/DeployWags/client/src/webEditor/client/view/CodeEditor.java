package webEditor.client.view;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;



public class CodeEditor extends View implements HasHandlers
{
	

	private static CodeEditorUiBinder uiBinder = GWT
			.create(CodeEditorUiBinder.class);

	interface CodeEditorUiBinder extends UiBinder<Widget, CodeEditor>{}
	 
	@UiField public TextArea codeArea;
	public String codeTop;
	public String codeBottom;
	
//	private CompletionCheck colorCheck = new CompletionCheck();
//	private TabCheck tabCheck = new TabCheck(codeArea);
//	private int lastKey;
//	private static final int CCURLS = 221;
//	private static final int CPARENS = 48;

	public CodeEditor()
	{
		initWidget(uiBinder.createAndBindUi(this));

		//I. HATE. CSS.  You can't make anything just freakin' fill the parent element!  IT CANNOT BE THAT HARD.
		codeArea.setHeight("1200px");
		codeArea.setFocus(true);
		codeArea.setEnabled(true);
		
		/*
		 * Handle TAB
		 * Not my code, credit goes to:
		 * alberttattard.blogspot.com
		 */
		codeArea.addKeyDownHandler(new KeyDownHandler(){
			public void onKeyDown(KeyDownEvent event)
			{
				if (event.getNativeKeyCode() == 9){
					event.preventDefault();
					event.stopPropagation();
					final int index = codeArea.getCursorPos();
					final String text = codeArea.getText();
					codeArea.setText(text.substring(0, index) + "\t"
							+ text.substring(index));
					codeArea.setCursorPos(index + 1);
				}
			}
		});
		
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
	
	public void setContents(String contents){
		this.codeArea.setText(contents);
	}
	
	@Override
	public WEAnchor getLink()
	{
		return new WEAnchor("Editor", this, "codeEditor");
	}
	
}
