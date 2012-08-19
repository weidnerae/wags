package webEditor.magnet.client;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class ModifiedCodePanel extends AbsolutePanel {
	protected StackableContainer mainFunction;
	protected AbsolutePanel mainPanel;
	public String content = "";
	public StringBuffer plainText = new StringBuffer();
	
	public PickupDragController dragController;
	private TrashBin bin = new TrashBin();
	private AbsolutePanel trashPanel = new AbsolutePanel();

	public ModifiedCodePanel(StackableContainer mainFunction,
			StackableContainer[] insideSegments, PickupDragController dragController) {
		setStyleName("code_panel");
		//mainFunction.setStackable(true);
		this.dragController = dragController;
		this.mainFunction = mainFunction;
		mainPanel = new AbsolutePanel();
		mainPanel.add(mainFunction);
		add(mainPanel);
		
		
		this.dragController.unregisterDropControllers();
		BinDropController binController = new BinDropController(bin);
		this.dragController.registerDropController(binController);
		trashPanel.add(bin);
		
		add(trashPanel);
		setWidth("100%");
		setHeight("100%");
		
	}
	
	public void addCreatedContainer(StackableContainer container){
		mainFunction.addInsideContainer(container);
	}
	public String getInsidePremadeMainContent(){
		String result = "";
		for(int i=0;i<mainFunction.getInsidePanel().getWidgetCount();i++){
			result=buildInsidePremadeContent(((StackableContainer)mainFunction.getInsidePanel().getWidget(i)),result);
		}
		return result;
	}
	public String buildInsidePremadeContent(StackableContainer sc, String insidePremadeContent){
		insidePremadeContent+=sc.getContent()+".:|:.";
		for(int i=0;i<sc.getInsidePanel().getWidgetCount();i++){
			insidePremadeContent = buildInsidePremadeContent(((StackableContainer)sc.getInsidePanel().getWidget(i)),insidePremadeContent);
		}
		return insidePremadeContent;
	}
	public String getInsideMainContent(){
		String result = "";
		for(int i=0;i<mainFunction.getInsidePanel().getWidgetCount();i++){
			content="";
			buildContent((StackableContainer)mainFunction.getInsidePanel().getWidget(i));
			result+=content+".:|:.";
		}
		return result;
	}
	public String buildInsideContent(StackableContainer sc, String insideContent){
		if (sc.getTopLabel() != null) {
			insideContent += sc.getTopLabel().getHTML();
		}
		for (int i = 0; i < sc.getInsidePanel().getWidgetCount(); i++) {
			buildInsideContent((StackableContainer) sc.getInsidePanel()
					.getWidget(i),insideContent);
		}
		if (sc.getBottomLabel() != null) {
			insideContent += sc.getBottomLabel().getHTML();
		}
		return insideContent;
	}
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

	public void buildContent(StackableContainer sc) {
		if (sc.getTopLabel() != null) {
			content += sc.getTopLabel().toString();
			plainText.append(sc.getTopLabel().getText() + "\n");

		}
		content += "<span id=\"inside_of_block\">";
		for (int i = 0; i < sc.getInsidePanel().getWidgetCount(); i++) {
			buildContent((StackableContainer) sc.getInsidePanel().getWidget(i));
		}
		content += "</span>";
		if (sc.getBottomLabel() != null) {
			content += sc.getBottomLabel().toString();
			plainText.append(sc.getBottomLabel().getText() + "\n");
		}

	}

}
