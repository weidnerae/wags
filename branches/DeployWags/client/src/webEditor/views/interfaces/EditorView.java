package webEditor.views.interfaces;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

import webEditor.Common.View;
import webEditor.programming.view.CodeEditor;
import webEditor.programming.view.FileBrowser;
import webEditor.programming.view.OutputReview;

public interface EditorView extends View {
	
	public TextBox fileName();
	public Anchor getCode();
	public FileBrowser browser();
	public CodeEditor editor();
	public com.google.gwt.user.client.ui.Image description();
	public OutputReview review();
	public Anchor save();
	public Button submit();
	public TabLayoutPanel tabPanel();
	public FormPanel wrapperForm();
	public TextArea lines();
}
