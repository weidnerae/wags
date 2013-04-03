package webEditor.programming.view;

import webEditor.Proxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class Editor extends Composite implements IsWidget{
	private static EditorPanelUiBinder uiBinder = GWT.create(EditorPanelUiBinder.class);
	interface EditorPanelUiBinder extends UiBinder<Widget, Editor>{}

	interface EditorUiBinder extends UiBinder<Widget, Editor>{}
	
	@UiField TextBox fileName;
	@UiField Anchor getCode;
	@UiField FileBrowser browser;
	@UiField CodeEditor editor;
	@UiField com.google.gwt.user.client.ui.Image description;
	@UiField OutputReview review;
	@UiField Anchor save;
	@UiField Button submit;
	@UiField TabLayoutPanel tabPanel;
	@UiField FormPanel wrapperForm;
	@UiField TextArea lines;
	
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
	
	// Keep track of the currently selected item
	private TreeItem selectedItem = null;
	public Editor(){
		initWidget(uiBinder.createAndBindUi(this));
		
		SectionTab sections = new SectionTab();
		Students students = new Students();
		Admin admin = new Admin();
				
		initializeAutosaving();				// Initialize timer used in code autosaving
		Proxy.isAdmin(tabPanel, sections, students, admin);
		commandBarVisible(false);
		
		selectedItem = browser.getTree().getItem(0); // initialize selected item to root
		
		lines.setReadOnly(true);
		lines.setVisibleLines(100);
		final Editor thing = this;
		
		description.setUrl(""); // TODO: This needs to be filled with a default

		//For getting pdf descriptors
		wrapperForm.setAction(Proxy.getBaseURL()+"?cmd=ReturnPDF");		
		wrapperForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		wrapperForm.setMethod(FormPanel.METHOD_POST);
		
		// Add selection handler to file browser
		browser.getTree().addSelectionHandler(new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event)
			{
				boolean checkVis = false;
				TreeItem i = event.getSelectedItem();
				selectedItem = i;
				String itemName = browser.getItemPath(i);  // clicked item
				
				// Don't autosave if clicking on the first file of the session, or on a version
				if (currentExercise != null && currentExercise.compareTo("") != 0 && 
						!itemName.contains("_Versions") && !fileName.getText().contains("_Versions"))
					saveCurrentCode();
				
				else
				{
					// If clicked item is a leaf TreeItem then open it in editor
					Proxy.getFileContents(itemName, editor, thing);
					if(itemName.contains("_Versions")){
						currentExercise = browser.getItemPath(i.getParentItem().getParentItem()).trim().substring(1); /* Grab the exercise */
						checkVis = true;
					}
					else {
						currentExercise = browser.getItemPath(i.getParentItem()).trim().substring(1); /* Grab the exercise */
						checkVis = true;
					}
					
					/* Update description */
					Proxy.getDescription(currentExercise, description);
	
					// Set filename, save, and delete stuff visible
					commandBarVisible(true);
					if(checkVis) handleInvisibility(browser.getFileVisibility(itemName));
					fileName.setText(browser.getItemPath(i).toString().substring(1));
				}
			}
		});
		
		// This allows us to select the exercise name when a plus sign is pressed,
		//  so that when the user switches tabs and comes back, the file tree won't
		//  revert back to root
		browser.getTree().addOpenHandler(new OpenHandler<TreeItem>() {
			public void onOpen(OpenEvent<TreeItem> event) 
			{
				TreeItem t = event.getTarget();
				selectedItem = t;
			}
		});
		
		// When the FileBrowser tab is clicked, this will reload the file browser, 
		//	reselect the previously selected item, and open up to the necessary depth
		// -If an exercise name is clicked, it will be selected (due to the OpenHandler
		//	 above), and will be opened up upon reload here.
		// -This will all happen when switching away from the tab (since it is a BeforeSelectionHandler)
		//   so the "flickering" issue will not happen
		tabPanel.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
			@Override
			public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
				Proxy.loadFileListing(browser, getPath(selectedItem));
			}
		});
	}
	/**
	 * Send contents of text area to server. 
	 */
	@UiHandler("save")
	void onSaveClick(ClickEvent event)
	{
		saveCurrentCode();
	}

	@UiHandler("submit")
	void onSubmitClick(ClickEvent event)
	{
		saveCurrentCode();
		
		String codeText = editor.codeTop + "\n" + editor.codeArea.getText() + "\n" + editor.codeBottom;
		
		codeText = URL.encodePathSegment(codeText);
		
		Proxy.review(codeText, review, currentExercise, "/"+fileName.getText().toString(), submit);
		
		tabPanel.selectTab(REVIEWPANEL);
	}
	
	/**
	 * Sets the visibility of the command bar with save, delete, submit, etc.
	 * 
	 * @param visible
	 */
	private void commandBarVisible(boolean visible){
		save.setVisible(visible);
		submit.setVisible(visible);
		getCode.setVisible(visible);
	}
	
	private String getPath(TreeItem i){
		String path = "";
		while(i != null && i.getParentItem() != null){
			path = "/"+i.getText()+path;
			i = i.getParentItem();
		}
		
		return path;
	}
	
	/**
	 * This sets up timer and event handlers for textarea code editor
	 * autosaving feature
	 */
	private void initializeAutosaving()
	{
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
		editor.codeArea.addFocusHandler(new FocusHandler() 
		{
			public void onFocus(FocusEvent event)
			{
				// save every AUTOSAVETIME seconds
				autosaveTimer.scheduleRepeating(AUTOSAVETIME);
			}
		});
		// 	-Blur handler for when user clicks elsewhere
		//		-want to stop autosaving now
		editor.codeArea.addBlurHandler(new BlurHandler() 
		{
			public void onBlur(BlurEvent event)
			{
				autosaveTimer.cancel();
				saveCurrentCode();
			}
		});	
	}

	/**
	 * Save the file before submitting
	 * 
	 * This saves the current code from the textarea editor, as well as
	 * the hidden parts above and below user code
	 */
	private void saveCurrentCode(){
		// First make sure the user has actually changed the code
		//  before sending a request to the server.
		String editorText = editor.codeArea.getText();
		if (!editorText.equals(currentEditorCode))
		{
			// Code is different, so save
			// Update the current editor code text
			currentEditorCode = editorText;
			
			String text = editor.codeTop + editorText + editor.codeBottom;
			
			text = URL.encodePathSegment(text);
			
			if(Proxy.saveFile("/" + fileName.getText(), text, browser, false));
		}
	}
	void handleInvisibility(int vis){
		submit.setEnabled(true);
		
		switch (vis){
			case INVISIBLE:
				submit.setText("Invis");
				break;
			case VISIBLE:
				submit.setText("Submit");
				break;
			case EXPIRED: 
				submit.setText("Expired");
				break;
			case PREOPEN:
				submit.setText("Not Open");
				break;
			default:
				break;
		}
	}
	
	@UiHandler("getCode")
	void onDescClick(ClickEvent event)
	{	
		review.setText(createUserViewableCode());
		
		tabPanel.selectTab(REVIEWPANEL);
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
	public String createUserViewableCode()
	{
		String code = editor.codeTop;
		code +=  editor.codeArea.getText() + editor.codeBottom;
		
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
	 * Adds line numbers next to the code editor.
	 * 
	 * Finds <end!TopSection>, counting up lines, then adds 100 
	 * more lines as a buffer for when students type their own code. 
	 * 100 Should be enough to go off the screen.
	 * 
	 * If you are reading this and feeling clever, figure out a way to 
	 * make it so it can scroll with the editor.
	 */
	public void addLineNumbers() {
		String code = editor.codeTop;
		code +=  editor.codeArea.getText() + editor.codeBottom;
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
		
		lines.setText(sb.toString());
	}
}
