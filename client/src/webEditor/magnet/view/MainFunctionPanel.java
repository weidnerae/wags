package webEditor.magnet.view;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

public class MainFunctionPanel extends HorizontalPanel{
	
	private TextBox mainFunctionBox = new TextBox();
	public MainFunctionPanel(){
		add(new HTML("Main Function: "));
		add(mainFunctionBox);
	}
	public String getBoxContent(){
		return mainFunctionBox.getText();
	}

}
