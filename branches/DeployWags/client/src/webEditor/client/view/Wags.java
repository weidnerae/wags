
package webEditor.client.view;

import java.util.HashMap;

import webEditor.client.Proxy;
import webEditor.client.WEStatus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class Wags extends View
{

	private static EditorUiBinder uiBinder = GWT.create(EditorUiBinder.class);

	interface EditorUiBinder extends UiBinder<Widget, Wags>{}

	@UiField DockLayoutPanel dock;
	@UiField Anchor logout;
	@UiField Anchor save;
//	@UiField Anchor delete;
	@UiField Button submit;
	@UiField Anchor getCode;
	@UiField Anchor DST;
	@UiField FormPanel wrapperForm;
	@UiField com.google.gwt.user.client.ui.Image description;
	
	@UiField TextBox fileName;
	@UiField Label hello;
	@UiField CodeEditor editor;
	@UiField FileBrowser browser;
	@UiField Admin admin;
	@UiField Students students;
	@UiField OutputReview review;
	@UiField TabLayoutPanel tabPanel;
	
	final static int REVIEWPANEL = 1;
	final static int FILEBROWSER = 0;
	
	private Timer autosaveTimer;
	private final int AUTOSAVETIME = 10000; // autosave time interval in milliseconds
	
	private String currentEditorCode = "";
	
	String currentExerciseId;
	
	private String curPath = "";
	
	private HashMap<String, String> exerciseMap = new HashMap<String, String>();
	
	/**
	 * Constructor
	 * 
	 * -Builds Wags interface once logged in
	 */
	public Wags()
	{
		initWidget(uiBinder.createAndBindUi(this));
		
		// Initialize timer used in code autosaving
		initializeAutosaving();
		
		ListBox filler = new ListBox(); /* not used - except for getVisibleExercises */
		
		Proxy.checkTimedExercises();
		Proxy.checkPassword(this);
		Proxy.checkMultiUser(this);
		Proxy.getVisibleExercises(filler, exerciseMap);
		//Editing out filename changing
		fileName.setEnabled(false);
		//until we decide what to do with multiple files
		commandBarVisible(false);
		
		description.setUrl("");

		//For getting pdf descriptors
		wrapperForm.setAction(Proxy.getBaseURL()+"?cmd=ReturnPDF");		
		wrapperForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		wrapperForm.setMethod(FormPanel.METHOD_POST);
		
		// Add selection handler to file browser
		browser.getTree().addSelectionHandler(new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event)
			{
				TreeItem i = event.getSelectedItem();
				String itemName = browser.getItemPath(i);  // clicked item
				
				// Don't autosave if clicking on the first file of the session, or on a version
				if (currentExerciseId != null && currentExerciseId.compareTo("") != 0 && 
						!itemName.contains("_Versions") && !fileName.getText().contains("_Versions"))
					saveCurrentCode();
				
				// If clicked item is directory then just open it
				if(i.getChildCount() > 0)
					return;
				
				// If clicked item is a leaf TreeItem then open it in editor
				Proxy.getFileContents(itemName, editor);
				currentExerciseId = exerciseMap.get(browser.getItemPath(i.getParentItem()).trim().substring(1)); /* Grab the exercise Id */
				
				/* Update description */
				Proxy.getDescription(currentExerciseId, description);

				// Set filename, save, and delete stuff visible
				commandBarVisible(true);
				fileName.setText(browser.getItemPath(i).toString().substring(1));
			}
		});

// Don't need this right now since filename editing has been disabled for now
		// Show text to rename the file.
//		fileName.addFocusHandler(new FocusHandler() {
//			@Override
//			public void onFocus(FocusEvent event)
//			{
//				// Add an attribute to the filename textbox that stores the old file name. 
//				// Do this onFocus because the user is probably about to edit the file name.
//				fileName.getElement().setAttribute("oldName", fileName.getText());
//			}
//		});
		
		Proxy.isAdmin(tabPanel);
		Proxy.getUsersName(hello);
		
		tabPanel.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
			
			@Override
			public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
				// TODO Auto-generated method stub
				if(tabPanel.getSelectedIndex() == 0){
					curPath = getPath(browser.getTree().getSelectedItem());
				}
			}
		});
		
		tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				Proxy.loadFileListing(browser, curPath);
			}
		});
	} // end constructor
	
// Don't need this right now since filename editing has been disabled for now
//
//	@UiHandler("fileName")
//	void onChange(ChangeEvent event)
//	{
//		save.setVisible(true);
//		delete.setVisible(true);
//		submit.setVisible(true);
//	}


// Editing out delete button, as it currently serves no functionality,
//	and may cause issues, and unpredictability
// -Plan to remove entirely in future revision once it is agreed upon
	/**
	 * Delete file from server.
	 */
//	@UiHandler("delete")
//	void onDeleteClick(ClickEvent event)
//	{
//		TreeItem i = browser.getTree().getSelectedItem();
//		TreeItem parent = i.getParentItem();
//		
//		deleteChildren(i);
//		Notification.notify(WEStatus.STATUS_SUCCESS, i.getText()+" deleted");
//		i.remove();
//		
//		String reloadPath;
//		if(parent.getChildCount() > 0){
//			reloadPath = getPath(parent.getChild(0));
//		} else {
//			reloadPath = getPath(parent);
//		}
//	
//		editor.setContents("");
//		Proxy.loadFileListing(browser, reloadPath);
//		curPath = reloadPath;
//	}

	@UiHandler("getCode")
	void onDescClick(ClickEvent event){
		String wholeText = editor.codeTop;
		wholeText +=  editor.codeArea.getText() + editor.codeBottom;
		//review.setHTML("<pre>" + wholeText + "</pre>");
		review.setText(wholeText);
		tabPanel.selectTab(1);
		
		/*
		String value = exercises.getValue(exercises.getSelectedIndex());
		Proxy.getDesc(exerciseMap.get(value), review);
		tabPanel.selectTab(REVIEWPANEL);
		*/
	}

	@UiHandler("DST")
	void onDSTClick(ClickEvent event){
		this.setVisible(false);
		Proxy.buildDST();
	}

	/**
	 * Logout!	
	 */
	@UiHandler("logout")
	void onLogoutClick(ClickEvent event)
	{
		Proxy.logout();
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
		
		String codeText = editor.codeTop;
		codeText += "//<end!TopSection>" + editor.codeArea.getText();
		codeText += "//<end!MidSection>" + editor.codeBottom;
		
		//URL encode fails to encode "+", this is part of the workaround
		//which is completed on the server side
		codeText = codeText.replaceAll("[+]", "%2B");
		
		Proxy.review(codeText, review, currentExerciseId, "/"+fileName.getText().toString(), submit);
		
		tabPanel.selectTab(REVIEWPANEL);
	}
	
	/**
	 * Sets the visibility of the command bar with save, delete, submit, etc.
	 * 
	 * @param visible
	 */
	private void commandBarVisible(boolean visible){
		save.setVisible(visible);
// Delete is being removed, as it offers no functionality currently
//		delete.setVisible(visible);
		submit.setVisible(visible);
		//Exercises is being removed, currently just a placeholder
		//to retain some of its functionality (name id map)
		//exercises.setVisible(visible);
		getCode.setVisible(visible);
		//btnGetPDF.setVisible(visible);
	}

// Removing the delete button, and associated functions, as they provide
//  no functionality currently
	/**
	 * deleteChildren
	 * Description: recursively remove all children of a deleted directory
	 * @param i The directory
	 * @return none
	 */
//	private void deleteChildren(TreeItem i){
//		for(int childIndex = 0; childIndex < i.getChildCount(); childIndex++){
//			TreeItem child = i.getChild(childIndex);
//			
//			if(child.getChildCount() > 0)
//				deleteChildren(child); //recurses down to leaf
//			
//			Proxy.deleteFile(getPath(child)); //deletes leaf using path
//			child.remove(); //remove from browser
//		}
//		
//		Proxy.deleteFile(getPath(i));
//		i.remove();
//	}
	
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
			
			String text = editor.codeTop;
			if(text != "") text += "//<end!TopSection>";
			text += editorText;
			if(editor.codeBottom != "") text += "//<end!MidSection>";
			text += editor.codeBottom;
			
			//URL encoding converts all " " to "+".  Thus, when decoded it was incorrectly
			//converting all "+" to " ", including those actually meant to be +
			text = text.replaceAll("[+]", "%2B");
			text = text.replaceAll("[&&]", "%!`");
			
			if(Proxy.saveFile("/" + fileName.getText(), text, browser, false));
		}
	}

	public void assignPartner(final String exercise){
		final DialogBox pickPartner = new DialogBox(false);
		final ListBox partners = new ListBox();
		Button close = new Button("Close");
		
		HorizontalPanel DialogBoxContents = new HorizontalPanel();
		pickPartner.setText("Choose a partner for exercise: " + exercise);
		Proxy.getUsernames(partners);
		
		close.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				pickPartner.hide();
				Proxy.assignPartner(exercise, partners.getValue(partners.getSelectedIndex()));
			}
		});
		
		DialogBoxContents.add(partners);
		DialogBoxContents.add(close);
		pickPartner.add(DialogBoxContents);
		
		pickPartner.center();
	}

	public void assignPassword(){
		final DialogBox setPassword = new DialogBox(false);
		final PasswordTextBox password = new PasswordTextBox();
		final PasswordTextBox passwordCheck = new PasswordTextBox();
		Label lbl1 = new Label("Enter password: ");
		Label lbl2 = new Label("Re-enter password: ");
		
		Button close = new Button("Close");
		
		VerticalPanel base = new VerticalPanel();
		HorizontalPanel line1 = new HorizontalPanel();
		HorizontalPanel line2 = new HorizontalPanel();
		
		setPassword.setText("Please change your password");
				
		close.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(!password.getText().equals(passwordCheck.getText())){
					Notification.notify(WEStatus.STATUS_ERROR, "Passwords don't match");
					return;
				}
				
				setPassword.hide();
				Proxy.assignPassword(password.getText());
			}
		});
		
		line1.add(lbl1);
		line1.add(password);
		line2.add(lbl2);
		line2.add(passwordCheck);
		base.add(line1);
		base.add(line2);
		base.add(close);
		setPassword.add(base);
		
		setPassword.center();
	}

	@Override
	public WEAnchor getLink()
	{
		return new WEAnchor("Wags", this, "editor");
	}
	
}
