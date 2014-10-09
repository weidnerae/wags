package webEditor.presenters.concrete;

import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TreeItem;

import webEditor.Proxy;
import webEditor.presenters.interfaces.EditorPresenter;
import webEditor.views.concrete.Editor;

public class EditorPresenterImpl implements EditorPresenter {
	
	final static int REVIEWPANEL = 1;
	final static int FILEBROWSER = 0;
	final static int VISIBLE = 1;
	final static int INVISIBLE = 0;
	final static int EXPIRED = 2;
	final static int PREOPEN = 3;
	
	private final int AUTOSAVETIME = 10000; // autosave time interval in milliseconds
	private Timer autosaveTimer;
		
	private String currentEditorCode = "";
	
	String currentExercise;
	
	
	private Editor editor;
	private boolean bound = false;

	public EditorPresenterImpl(Editor view) {
		editor = view;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(editor.asWidget());
	}

	@Override
	public void bind() {
		editor.setPresenter(this);
		bound = true;
	}

	@Override
	public boolean bound() {
		return bound;
	}

	@Override
	public void update(List<String> data) {
		// TODO Auto-generated method stub
		Window.alert("inside the editor update method");
	}

	/**
	 * Adds line numbers next to the code editor.
	 * 
	 * Finds <end!TopSection>, counting up lines, then adds 100 
	 * more lines as a buffer for when students type their own code. 
	 * 100 Should be enough to go off the screen.
	 * 
	 * If you are reading this and feeling clever, figure out a way to 
	 * make it so it can scroll with the editor.
	 */
	@Override
	public void addLineNumbers() {
		String code = editor.editor().codeTop;
		code +=  editor.editor().codeArea.getText() + editor.editor().codeBottom;
		String[] lineArr = code.split("\n");
		
		StringBuilder sb = new StringBuilder();
		int line = 0;
		
		
		// Just consume lines until we get to end!TopSection
		while (line < lineArr.length && !lineArr[line].contains("<end!TopSection>")) {
			line++;
		}
		
		// If we never saw <end!TopSection>, start line numbers at 1
		if (line == lineArr.length) {
			line = 1;
		} else {
			line += 2; //add two to make line numbers line up
		}
		
		// Add 100 lines to push it off the end of the screen
		for (int i = 0; i < 100; i++) {
			sb.append((line) + " \n");
			line++;
		}
		
		editor.lines().setText(sb.toString());		
	}

	/**
	 * Use this to create code that the user is allowed to see,
	 *  such as when they click on the "code" button in the editor
	 * 
	 * - We don't want users to see our <end!___Section> tags,
	 *   or any special testing code below the <start!HiddenSection>
	 *   tag needed to make the program run, but not related to the
	 *   user's assignment.  (This code is often under a Test Code
	 *   comment, such as in Prolog and F#)
	 *   
	 * @return scrubbed code
	 */
	@Override
	public String createUserViewableCode() {
		String code = editor.editor().codeTop;
		code +=  editor.editor().codeArea.getText() + editor.editor().codeBottom;
		
		// We need to process the code a bit to hide some of our implementation
		//  -first, replace our <end!___Section> tags, as well as the
		//   two preceding comment tags (//, %%, etc based on the language)
		//		- the '(.)\\1' matches two of the same character, which is
		//        useful because the comment symbols are different in each
		//        language
		code = code.replaceAll("(.)\\1<end!TopSection>", "\n");  // Added so lines numbers still match what is  
		code = code.replaceAll("(.)\\1<end!MidSection>", "\n");  // reported in the runtime/compilation errors
		// 	-then remove any code that needs to be hidden from the user, such
		//   as how we are running the programs and testing the user's rules
		//   in Prolog and F#.  This code will have to be at the end of the file
		//   for now.
		int hidden = code.indexOf("<start!HiddenSection>");
		if (hidden != -1)
		{
			code = code.substring(0, hidden-2); // subtract two for the two comment symbols 
		}
		
		return code;		
	}

	/**
	 * Save the file before submitting
	 * 
	 * This saves the current code from the textarea editor, as well as
	 * the hidden parts above and below user code
	 */
	@Override
	public void saveCurrentCode(){
		// First make sure the user has actually changed the code
		//  before sending a request to the server.
		String editorText = editor.editor().codeArea.getText();
		Window.alert("saveCurrentCode after editorText");
		if (!editorText.equals(currentEditorCode))
		{
			// Code is different, so save
			// Update the current editor code text
			currentEditorCode = editorText;
			
			String text = editor.editor().codeTop + editorText + editor.editor().codeBottom;
			
			text = URL.encodePathSegment(text);
			
			if(Proxy.saveFile("/" + editor.fileName().getText(), text, editor.browser(), false));
		}
	}

	@Override
	public void onSubmitClick() {
		saveCurrentCode();
		
		String codeText = editor.editor().codeTop + "\n" + editor.editor().codeArea.getText() + "\n" + editor.editor().codeBottom;
		
		codeText = URL.encodePathSegment(codeText);
		
		Proxy.review(codeText, editor.review(), currentExercise, "/"+ editor.fileName().getText().toString(), editor.submit());
		
		editor.tabPanel().selectTab(REVIEWPANEL);		
	}

	@Override
	public void commandBarVisible(boolean visible) {
		editor.save().setVisible(visible);
		editor.submit().setVisible(visible);
		editor.getCode().setVisible(visible);		
	}

	@Override
	public String getPath(TreeItem i) {
		String path = "";
		while(i != null && i.getParentItem() != null){
			path = "/"+i.getText()+path;
			i = i.getParentItem();
		}
		
		return path;		
	}

	@Override
	public void initializeAutosaving() {
		autosaveTimer = new Timer()
		{
			public void run() 
			{
				saveCurrentCode();
			}
		};

		// focus and blur handlers to control autosaving
		//	-Focus handler for when user first clicks on textarea editor
		//		-want to begin autosaving now
		
		editor.editor().codeArea.addFocusHandler(new FocusHandler() 
		{
			public void onFocus(FocusEvent event)
			{
				Window.alert("Trying to activate the autosavetime");
				// save every AUTOSAVETIME seconds
				autosaveTimer.scheduleRepeating(AUTOSAVETIME);
			}
		});
				
		// 	-Blur handler for when user clicks elsewhere
		//		-want to stop autosaving now
		editor.editor().codeArea.addBlurHandler(new BlurHandler() 
		{
			public void onBlur(BlurEvent event)
			{
				autosaveTimer.cancel();
				saveCurrentCode();
			}
		});	

	}

	@Override
	public void handleInvisibility(int vis) {
		editor.submit().setEnabled(true);
		
		switch (vis){
			case INVISIBLE:
				editor.submit().setText("Invis");
				break;
			case VISIBLE:
				editor.submit().setText("Submit");
				break;
			case EXPIRED: 
				editor.submit().setText("Expired");
				break;
			case PREOPEN:
				editor.submit().setText("Not Open");
				break;
			default:
				break;
		}		
	}

	@Override
	public void onDescClick(ClickEvent event) {
		editor.review().setText(createUserViewableCode());
		editor.tabPanel().selectTab(REVIEWPANEL);		
	}

}
