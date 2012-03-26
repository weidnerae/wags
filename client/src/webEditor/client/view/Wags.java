
package webEditor.client.view;

import webEditor.client.Proxy;
import webEditor.client.WEStatus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.http.client.URL;
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

	@UiField Admin admin;
	@UiField FileBrowser browser;
	@UiField com.google.gwt.user.client.ui.Image description;
	@UiField DockLayoutPanel dock;
	@UiField Anchor DST;
	@UiField CodeEditor editor;
	@UiField TextBox fileName;
	@UiField Anchor getCode;
	@UiField Label hello;
	@UiField Anchor logout;
	@UiField OutputReview review;
	@UiField Anchor save;
	@UiField Students students;
	@UiField Button submit;
	@UiField TabLayoutPanel tabPanel;
	@UiField FormPanel wrapperForm;
	
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
	
	private String curPath = "";
	
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
		
		Proxy.isAdmin(tabPanel);
		Proxy.checkPassword(this);
		Proxy.checkMultiUser(this);
		Proxy.getUsersName(hello);
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
				boolean checkVis = false;
				TreeItem i = event.getSelectedItem();
				String itemName = browser.getItemPath(i);  // clicked item
				
				// Don't autosave if clicking on the first file of the session, or on a version
				if (currentExercise != null && currentExercise.compareTo("") != 0 && 
						!itemName.contains("_Versions") && !fileName.getText().contains("_Versions"))
					saveCurrentCode();
				
				// If clicked item is directory then just open it
				if(i.getChildCount() > 0)
					return;
				
				// If clicked item is a leaf TreeItem then open it in editor
				Proxy.getFileContents(itemName, editor);
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
		});
		
		tabPanel.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
			
			@Override
			public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
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
				submit.setEnabled(false);
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
		String wholeText = editor.codeTop;
		wholeText +=  editor.codeArea.getText() + editor.codeBottom;
		review.setText(wholeText);
		
		tabPanel.selectTab(REVIEWPANEL);
	}

	@UiHandler("DST")
	void onDSTClick(ClickEvent event)
	{
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
		
		String codeText = editor.codeTop + editor.codeArea.getText() + editor.codeBottom;
		
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
		password.setFocus(true);
	}

	@Override
	public WEAnchor getLink()
	{
		return new WEAnchor("Wags", this, "editor");
	}
	
}