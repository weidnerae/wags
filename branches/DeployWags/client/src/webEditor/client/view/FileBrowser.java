package webEditor.client.view;

import webEditor.client.Proxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class FileBrowser extends View
{

	private static FileBrowserUiBinder uiBinder = GWT
			.create(FileBrowserUiBinder.class);

	interface FileBrowserUiBinder extends UiBinder<Widget, FileBrowser>{}

	@UiField Tree browser;
	@UiField FormPanel form;
	@UiField TextBox curDir;
	@UiField SubmitButton uploadButton;


	/**
	 * FileBrowser is tightly coupled with CodeEditor. Text from items from file
	 * browser are loaded into the CodeEditor.
	 * 
	 * @param editor
	 */
	public FileBrowser()
	{
		initWidget(uiBinder.createAndBindUi(this));
		Proxy.loadFileListing(this, "/");
		
		//Editing out the formpanel until we decide how we want to handle multiple files
		form.setVisible(false);
		//
		
		form.setAction(Proxy.getBaseURL() + "?cmd=UploadFile&dir=" + curDir.getText().toString());
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		final FileBrowser me = this;
		
		
		form.addSubmitCompleteHandler(new SubmitCompleteHandler(){
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				Proxy.loadFileListing(me, "/"+curDir.getText().toString());
			}
			
		});
		
		browser.addSelectionHandler(new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event)
			{
				// If clicked item is directory then just open it
				TreeItem i = event.getSelectedItem();
				if(i.getChildCount() > 0){
					String path = getItemPath(i);
					curDir.setText(path.substring(1)+"/");
					return;
				}
				curDir.setText("");
			}
		});
		

	}
	
	private void formatDirectory(){
		String directory = curDir.getText().toString();
		
		if(directory.startsWith("/", 0))
			directory = directory.substring(1);
		
		if(!directory.endsWith("/") && directory.length() != 0)
			directory = directory + "/";
		
		directory = directory.replaceAll("//+", "/");
		
		curDir.setText(directory);
	}
	
	
	/*
	 * Get the full path of the passed tree item.
	 */
	public String getItemPath(TreeItem i)
	{
		String path = "";
		while(i != null && i.getParentItem() != null){
			path = "/"+i.getText()+path;
			i = i.getParentItem();
		}	
		return path;
	}
	
	/*
	 * Get the full path of the currently selected item.
	 */
	public String getSelectedPath(){
		TreeItem i = getTree().getSelectedItem();
		String path = "";
		while(i != null && i.getParentItem() != null){
			path = "/"+i.getText()+path;
			i = i.getParentItem();
		}
		return path;
	}
	
	/*
	 * Open each item of the tree along the given path. 
	 */
	public void openPath(String path)
	{
		String[] dirs = path.split("/");
		TreeItem t = browser.getItem(0); // Get root.
		// Skip the first index...it's gonna be an empty string.
		for(int i = 1; i < dirs.length && t != null; i++){
			t.setState(true, false);
			t = getItem(t, dirs[i]);
		}
	}

	/**
	 * Parse the string array and add files to tree. The passed string is a
	 * response from the server.
	 */
	public void loadTree(String[] response)
	{
		// Remove any old items from tree.
		browser.clear();
		// Insert root
		TreeItem root = new TreeItem("/");
		TreeItem rootRoot = root;
		browser.addItem(root);
		
		// Loop over each path name
		for(final String s : response){
			final String splitS[] = s.split("/");
			// Loop over directories
			for(int i = 0; i < splitS.length-1; i++){
				if(splitS[i] != ""){
					TreeItem t = FileBrowser.getItem(root, splitS[i]);
					// If t is null then we haven't see this directory before
					// so we should add it to the tree.
					if(t == null){
						t = new TreeItem(splitS[i]);
						root.addItem(t);
					}
					root = t;
				}
			}
			// Add file.
			TreeItem ti = new TreeItem(splitS[splitS.length-1]);
			root.addItem(ti);
			root = rootRoot;
		}
	}
	
	private static TreeItem getItem(TreeItem root, String name){
		for(int i = 0; i < root.getChildCount(); i++){
			TreeItem t = root.getChild(i);
			if(t.getText().equals(name)){
				return t;
			}
		}
		return null;
	}
	
	@Override
	public WEAnchor getLink()
	{
		return new WEAnchor("File Browser", this, "fileBrowser");
	}

	public Tree getTree()
	{
		return this.browser;
	}
	
	@UiHandler("uploadButton")
	void onUploadClick(ClickEvent event){
		formatDirectory();
	}
	
	
	
}
