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
	public StringBuilder plainText;
	public StackableContainer mainFunction;
	private String title;
	private int tabNumber = -1; // So the initial increment will give 0 tabs
	private RefrigeratorMagnet magnet;
	
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
	 * 
	 * @param mainFunction The function that is to be built
	 * @param insideFunctions Possible inner functions nested into the function to be built.
	 */
	public CodePanelUi(RefrigeratorMagnet magnet, StackableContainer main,
			StackableContainer[] insideFunctions, PickupDragController dc, String title) {
		initWidget(uiBinder.createAndBindUi(this));
		this.magnet = magnet;
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
		if (magnet.getProblemType().equals(Consts.ALGORITHM_PROBLEM)) {
			evaluateAlgorithmProblem();
		}
		
		plainText = new StringBuilder();
		
		buildContent(mainFunction);
		ResultsPanelUi.setCodeText(getFormattedText());
		Proxy.magnetReview(getFormattedText(), title);
		magnet.tabPanel.selectTab(1);
	}
	
	/**
	 * Places possible inside functions into the main function
	 * 
	 * @param insideFunctions
	 */
	public void addInsideFunctions(StackableContainer[] insideFunctions) {
		if (insideFunctions == null) {
			return;
		}
		
		for (int i = 0; i < insideFunctions.length && insideFunctions[i] != null; i++) {
			mainFunction.addInsideContainer(insideFunctions[i]);
		}
	}
	
	/**
	 * Formats plainText for compilation and testing.
	 * 
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
			}
		}
		
		return plainText.toString();
	}

	/**
	 * This executes when the finalize button is pressed.
	 * 
	 * It takes the mainFunction stackable container and builds the content 
	 * so that it can be formatted.
	 * 
	 * After this is run, plainText will contain the text of all the stackable 
	 * containers, but stripped of all the embedded HTML shenanigans, ready to 
	 * manipulated by getFormattedText(). 
	 * 
	 * @param sc 
	 * 		The mainFunction StackableContainer. All of the other 
	 * 		StackableContainers are grabbed recursively.
	 */
	public void buildContent(StackableContainer sc) {
		tabNumber++;
		String tabs = "";
		
		// For proper indentation
		for (int i = 0; i < tabNumber; i++) {
			tabs += "\t";
		}
		
		if (sc.getTopLabel() != null) {
			content += sc.getTopLabel().toString();
			plainText.append(tabs + sc.getTopLabel().getText() + "\n");
			
		}
		
		content += "<span id=\"inside_of_block\">";
		
		for (int i = 0; i < sc.getInsidePanel().getWidgetCount(); i++) {
			buildContent((StackableContainer) sc.getInsidePanel().getWidget(i));
		}
		
		content += "</span>";
		
		if (sc.getBottomLabel() != null) {
			content += sc.getBottomLabel().toString();
			plainText.append(tabs + sc.getBottomLabel().getText() + "\n");
		}
		
		tabNumber--;
	}
	
	
	
	/****************************************************
	 * None of this code is ever called.                *
	 * We won't use it unless we do Algorithm problems. *
	 ****************************************************/
	
	
	
	public void evaluateAlgorithmProblem(){
		boolean done = false;
		String userSolution = getAlgoSolution();
		String givenSolution = magnet.getSolution();
		for(int i=0;i<givenSolution.length();i++){
	//		if()
			
		}
		
	}
	
	public String getAlgoSolution(){
		String idChain = "";
		for(int i=0;i<mainFunction.getInsidePanel().getWidgetCount();i++){
			idChain = buildIDString((StackableContainer)mainFunction.getInsidePanel().getWidget(i),idChain);
		}
		return idChain;
	}
	
	public String buildIDString(StackableContainer sc, String idChain) {
		if(sc.getContent().contains(Consts.TOPANYORDER))
			idChain+="[";
		else
			idChain+="{";
		//idChain+=sc.getID();
		for (int i = 0; i < sc.getInsidePanel().getWidgetCount(); i++) {
			idChain=buildIDString((StackableContainer) sc.getInsidePanel().getWidget(i),idChain);
		}
		if(sc.getContent().contains(Consts.TOPANYORDER))
			idChain+="]";
		else
			idChain+="}";
		return idChain;
	}

}
