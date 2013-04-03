package webEditor.programming.view;


import webEditor.View;
import webEditor.WEAnchor;

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

	public CodeEditor()
	{
		initWidget(uiBinder.createAndBindUi(this));

		codeArea.setEnabled(false); // shouldn't be editable when first logging in
		
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
