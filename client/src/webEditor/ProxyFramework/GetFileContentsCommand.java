package webEditor.ProxyFramework;

import webEditor.programming.view.CodeEditor;
import webEditor.views.concrete.Editor;

import com.google.gwt.http.client.Response;

public class GetFileContentsCommand extends AbstractServerCall {

	private CodeEditor editor;
	private Editor codeArea;

	@Override
	protected void handleResponse(Response response)
	{
		// Passing it through JSON kills formatting
		// For some unknown reason, the text from the server gets 
		// prepended with all sorts of spaces....
		String allText = response.getText().trim();
		
		//Grab status for uneditable codeArea for helper classes
		String status = allText.substring(0, 1); 
		allText = allText.substring(1);
		editor.codeArea.setEnabled(true); /* defaults to enabled */
		
		// Have to take into account comment length
		//	-We will still require that two comment marks be used before tag
		String lengthFinder = "<end!TopSection>";
		int len = lengthFinder.length();
		
		// Find the end of top and middle comments
		int endofTop = allText.indexOf("<end!TopSection>");
		int endofMid = allText.indexOf("<end!MidSection>");
		String top = "", mid = allText, bot = "";
				
		//Logic copied from server side
		if(endofTop != -1){
			top = allText.substring(0, endofTop + len); // keep the comment in top
			mid = allText.substring(endofTop + len);    // don't include comment in mid
		}
		
		if(endofMid != -1){
			bot = allText.substring(endofMid - 2); // keep comment in bottom
			mid = allText.substring(endofTop + len, endofMid - 2); // don't leave //, or %%, or etc in mid
		}
		
		editor.codeTop = top;
		editor.codeBottom = bot;
		// I'll look at this more - but I believe this is causing no indentation on the first line, 
		// which isn't what we want. - Philip
		//editor.codeArea.setText(mid.replaceAll("^\\s+", "")); // get rid of all leading whitespace
		editor.codeArea.setText(mid);
		
		if(status.equals("0")) {
			editor.codeArea.setEnabled(false); // if status = 0, file is uneditable
		}
		
		codeArea.addLineNumbers();

	}

	public GetFileContentsCommand(String fileName, final CodeEditor editor, final Editor codeArea)
	{
		addArgument("name", "fileName");
		this.editor = editor;
		this.codeArea = codeArea;
		command = ProxyCommands.GetFileContents;
	}
}
