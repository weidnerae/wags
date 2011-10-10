
package webEditor.client.view;

import java.util.HashMap;

import webEditor.client.Proxy;
import webEditor.client.WEStatus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
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
	@UiField Anchor delete;
	@UiField Anchor submit;
	@UiField ListBox exercises;
	@UiField Button btnGetDesc;
	
	@UiField TextBox fileName;
	@UiField Label hello;
	@UiField CodeEditor editor;
	@UiField FileBrowser browser;
	@UiField Admin admin;
	@UiField OutputReview review;
	@UiField TabLayoutPanel tabPanel;
	
	final static int REVIEWPANEL = 1;
	
	private HashMap<String, String> exerciseMap = new HashMap<String, String>();
	
	public Wags()
	{
		initWidget(uiBinder.createAndBindUi(this));
		
		Proxy.checkTimedExercises();
		Proxy.checkMultiUser(this);
		Proxy.getVisibleExercises(exercises, exerciseMap);
		commandBarVisible(false);

		// Add selection handler to file browser
		browser.getTree().addSelectionHandler(new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event)
			{
				// If clicked item is directory then just open it
				TreeItem i = event.getSelectedItem();
				if(i.getChildCount() > 0)
					return;
				// If clicked item is a leaf TreeItem then open it in editor
				Proxy.getFileContents(browser.getItemPath(i), editor.codeArea);
				
				for(int j = 0; j < exercises.getItemCount(); j++){
					if(exercises.getValue(j).equals(browser.getItemPath(i.getParentItem()).trim().substring(1))){
						exercises.setItemSelected(j, true);
					}
				}
				editor.getTabCheck().setTabCount(3); //reset tabcount for newly
													 //opened file

				// Set filename, save, and delete stuff visible
				commandBarVisible(true);
				fileName.setText(browser.getItemPath(i).toString().substring(1));
			}
		});

		// Show text to rename the file.
		fileName.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event)
			{
				// Add an attribute to the filename textbox that stores the old file name. 
				// Do this onFocus because the user is probably about to edit the file name.
				fileName.getElement().setAttribute("oldName", fileName.getText());
			}
		});
		
		Proxy.isAdmin(tabPanel);
		
		Proxy.getUsersName(hello);
	}
	
	void commandBarVisible(boolean visible){
		save.setVisible(visible);
		delete.setVisible(visible);
		submit.setVisible(visible);
		exercises.setVisible(visible);
		btnGetDesc.setVisible(visible);
	}
	/**
	 * Send contents of text area to server. 
	 */
	@UiHandler("save")
	void onSaveClick(ClickEvent event)
	{
		String text = editor.codeArea.getText();
		
		//URL encoding converts all " " to "+".  Thus, when decoded it was incorrectly
		//converting all "+" to " ", including those actually meant to be +
		text = text.replaceAll("[+]", "%2B");
		if(Proxy.saveFile("/" + fileName.getText().toString(), text, browser));
	}
	
	@UiHandler("fileName")
	void onChange(ChangeEvent event)
	{
		save.setVisible(true);
		delete.setVisible(true);
		submit.setVisible(true);
		exercises.setVisible(true);
	}
	
	/**
	 * Delete file from server.
	 */
	@UiHandler("delete")
	void onDeleteClick(ClickEvent event)
	{
		TreeItem i = browser.getTree().getSelectedItem();
		TreeItem parent = i.getParentItem();
		
		deleteChildren(i);
		Notification.notify(WEStatus.STATUS_SUCCESS, i.getText()+" deleted");
		i.remove();
		
		String reloadPath;
		if(parent.getChildCount() > 0){
			reloadPath = getPath(parent.getChild(0));
		} else {
			reloadPath = getPath(parent);
		}

		editor.setContents("");
		Proxy.loadFileListing(browser, reloadPath);
	}
	
	/**
	 * Logout!	
	 */
	@UiHandler("logout")
	void onLogoutClick(ClickEvent event)
	{
		Proxy.logout();
	}
	
	@UiHandler("submit")
	void onSubmitClick(ClickEvent event)
	{
		/**
		 * From here to the next comment is a workaround dealing with
		 * an anomaly in how RTA's pass spaces as an invalid value.
		 * To fix this, we insert an &nbsp; which will work properly at the
		 * start of the code, and then replace all ' ' with &nbsp; which looks
		 * like we are doing absolutely nothing but replacing ' ' with ' '.
		 * 
		 * As silly as this seems, it works with it and doesn't work without it.
		 * We also then remove the introductory space after using it to cast all
		 * the spaces.
		 */
		String codeHTML, codeText;
		
		codeHTML = editor.codeArea.getHTML();
		editor.codeArea.setHTML("&nbsp;" + codeHTML);
		
		codeText = editor.codeArea.getText();
		codeText = codeText.replace(codeText.charAt(0), ' ');
		codeText = codeText.substring(1);
		
		editor.codeArea.setHTML(codeHTML);
		
		//End of &nbsp; workaround
		
		//URL encode fails to encode "+", this is part of the workaround
		//which is completed on the server side
		codeText = codeText.replaceAll("[+]", "%2B");
		
		String value = exercises.getValue(exercises.getSelectedIndex());
		
		Proxy.review(codeText, review, exerciseMap.get(value), "/"+fileName.getText().toString());
		tabPanel.selectTab(REVIEWPANEL);
		
	}
	
	@UiHandler("btnGetDesc")
	void onDescClick(ClickEvent event){
		String value = exercises.getValue(exercises.getSelectedIndex());
		Proxy.getDesc(exerciseMap.get(value), review);
		tabPanel.selectTab(REVIEWPANEL);
	}
	

	
	@Override
	public WEAnchor getLink()
	{
		return new WEAnchor("Wags", this, "editor");
	}
	
	/**
	 * deleteChildren
	 * Description: recursively remove all children of a deleted directory
	 * @param i The directory
	 * @return none
	 */
	private void deleteChildren(TreeItem i){
		for(int childIndex = 0; childIndex < i.getChildCount(); childIndex++){
			TreeItem child = i.getChild(childIndex);
			
			if(child.getChildCount() > 0)
				deleteChildren(child); //recurses down to leaf
			
			Proxy.deleteFile(getPath(child)); //deletes leaf using path
			child.remove(); //remove from browser
		}
		
		Proxy.deleteFile(getPath(i));
		i.remove();
	}
	
	private String getPath(TreeItem i){
		String path = "";
		while(i != null && i.getParentItem() != null){
			path = "/"+i.getText()+path;
			i = i.getParentItem();
		}
		
		return path;
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
	
}
