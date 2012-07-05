package webEditor.magnet.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class ResultsPanelUi extends Composite {

	private static ResultsPanelUiUiBinder uiBinder = GWT
			.create(ResultsPanelUiUiBinder.class);

	interface ResultsPanelUiUiBinder extends UiBinder<Widget, ResultsPanelUi> {
	}

	@UiField LayoutPanel layout;
	@UiField AbsolutePanel testCode;
	@UiField AbsolutePanel testResults;
	static TextArea codeToTest;
	static TextArea codeTestResults;
	
	/**
	 * Creates results panel where students can see their code that will be tested and 
	 * the results of the test run.
	 * 
	 * @param tabPanelHeight height variable used to ensure panel visibility
	 */
	public ResultsPanelUi(int tabPanelHeight) {
		initWidget(uiBinder.createAndBindUi(this));
		//set size of overall panel
		layout.setSize("100%", tabPanelHeight - 60 + "px");
		//set up left side of panel, the code to test from student
		codeToTest = new TextArea();
		codeToTest.setReadOnly(true);
		codeToTest.setVisibleLines(25);
		codeToTest.setSize("100%", "100%");
		//add it to left side AbsolutePanel
		testCode.add(codeToTest);
	}

	static void setResultsText(String s){
		codeToTest.setText(s);
	}
}
