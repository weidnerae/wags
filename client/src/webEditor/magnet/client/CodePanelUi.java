package webEditor.magnet.client;

import webEditor.client.Proxy;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class CodePanelUi extends Composite {
	public AbsolutePositionDropController dropControl;
	public String content = "";
	public StringBuffer plainText;
	public StackableContainer mainFunction;
	private String title;
	@UiField ScrollPanel nestPanel;
	@UiField AbsolutePanel mainPanel;
	@UiField Button button;
	@UiField LayoutPanel layoutPanel;
	
	private static CodePanelUiUiBinder uiBinder = GWT
			.create(CodePanelUiUiBinder.class);

	interface CodePanelUiUiBinder extends UiBinder<Widget, CodePanelUi> {
	}

	/**
	 * Creates CodePanel for right side of editing panel.  
	 * @param mainFunction The function that is to be built
	 * @param insideFunctions Possible inner functions nested into the function to be built.
	 */
	public CodePanelUi(StackableContainer main,
			StackableContainer[] insideFunctions, PickupDragController dc, String title) {
		initWidget(uiBinder.createAndBindUi(this));
		this.mainFunction = main;
		this.title = title;
		
		if (insideFunctions != null) {
			addInsideFunctions(insideFunctions);
		}
			
		mainPanel.add(mainFunction);
	}
	
	//finalize button handler
	@UiHandler("button")
	void handleClick(ClickEvent e){
		plainText = new StringBuffer();
		buildContent(mainFunction);
		ResultsPanelUi.setResultsText(getFormattedText());
		Proxy.magnetReview(getFormattedText(), title);
		RefrigeratorMagnet.switchTabs(1);
	}
	
	/**
	 * Places possible inside functions into the main function
	 * @param insideFunctions
	 */
	public void addInsideFunctions(StackableContainer[] insideFunctions) {
		if (insideFunctions == null) {
			return;
		}
		
		int count = 0;
		while (insideFunctions[count] != null) {
			mainFunction.addInsideContainer(insideFunctions[count]);
			count++;
		}
	}
	
	/**
	 * Formats plainText for compilation and testing
	 * @return String the formatted text ready for compilation and testing
	 */
	public String getFormattedText() {
		boolean done = false;
		// make sure a newline gets inserted after the imports
		for (int i = 0; i < plainText.length() && !done; i++) {
			// put a newline after each import statement
			if (plainText.charAt(i) == ';'
					&& plainText.substring(i + 1, i + 7).equals("import")) {
				plainText.insert(i + 1, '\n');
			}
			// get to the last import statement, put a newline and break out
			// of the loop
			else if (plainText.charAt(i) == ';'
					&& plainText.charAt(i + 1) != '\n') {
				plainText.insert(i + 1, '\n');
				done = true;
			}
			// there is already a newline where we need them
			else if (plainText.charAt(i) == ';'
					&& plainText.charAt(i + 1) == '\n') {
				done = true;
			} else
				; // keep going
		}
		return plainText.toString();
	}

	/**
	 * takes the mainFunction stackable container and builds the content so that it can be formatted
	 * @param sc mainFunction
	 */
	public void buildContent(StackableContainer sc) {
		if (sc.getTopLabel() != null) {
			content += sc.getTopLabel().toString();
			plainText.append(sc.getTopLabel().getText() + "\n");

		}
		content += "<span id=\"inside_of_block\">";
		for (int i = 0; i < sc.getInsidePanel().getWidgetCount(); i++) {
			buildContent((StackableContainer) sc.getInsidePanel()
					.getWidget(i));
		}
		content += "</span>";
		if (sc.getBottomLabel() != null) {
			content += sc.getBottomLabel().toString();
			plainText.append(sc.getBottomLabel().getText() + "\n");
		}

	}

}
