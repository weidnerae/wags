package webEditor.magnet.view;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.DropController;

import java.util.ArrayList;

/**
 * Stackable containers are the lifeblood of magnets.  This used to be a comment from r28,
 * but I honestly don't know enough about it in its current form to comment it to my liking currently.
 * 
 * I will be back to comment this, but I highly encourage anyone that has the time when they come accross 
 * this to redocument it.
 */
public class StackableContainer extends FocusPanel {
	private AbsolutePanel primaryPanel = new AbsolutePanel();
	private AbsolutePanel topPanel = new AbsolutePanel();
	private AbsolutePanel insidePanel = new AbsolutePanel();
	private AbsolutePanel bottomPanel = new AbsolutePanel();
	private HTML topLabel;
	private HTML bottomLabel;
	private String hiddenCode;
	private String topCode;
	private String bottomCode;
	private MagnetType magnetType;
	private ProblemType problemType; // Java/Prolog etc.
	
	private DropController dropController = new PanelDropController(this);

	private boolean stackable = true;
	private boolean isMain = false;
	private boolean hasCode = false;
	private boolean isCreated = false;
	private boolean prependedComma = false;
	private int containerID;
	
	String content = "";

	/**
	 * A stackable container for the overall code with main and such. This
	 * container is not draggable.
	 * 
	 * @param content
	 *            the HTML formatted string
	 *            
	 * @param specialCondition
	 *            usually main
	 */
	public StackableContainer(String content, int specialCondition, ProblemType problemType) { // For mains, non draggable
		this.problemType = problemType;
		if(problemType == ProblemType.PROLOG && specialCondition != Consts.MAIN){
			insidePanel = new PrologMagnetInsidePanel();
		}
		this.content = " " + content;
		
		// pulling out the java code
		if(this.content.contains(Consts.CODE_START)){
			hasCode = true;
			String code = this.content.substring(this.content.indexOf(Consts.CODE_START)+Consts.CODE_START.length(),this.content.indexOf(Consts.CODE_END));
			
			// If this magnet nests
			if(this.content.indexOf(Consts.CODE_SPLIT) != -1){
				topCode = code.substring(0,code.indexOf(Consts.CODE_SPLIT));
				bottomCode = code.substring(code.indexOf(Consts.CODE_SPLIT)+Consts.CODE_SPLIT.length());
			
				String contentBeforeCode = this.content.substring(0,this.content.indexOf(Consts.CODE_START));
				String contentAfterCode = this.content.substring(this.content.indexOf(Consts.CODE_END)+Consts.CODE_END.length());
				this.content = contentBeforeCode+contentAfterCode;
			// If not
			} else {
				topCode = code;
				this.content = this.content.substring(0, this.content.indexOf(Consts.CODE_START));
			}
			
			
		}
		
		add(primaryPanel);  // primaryPanel holds everything else, because the focusPanel can only hold one widget
		setStyleName("stackable_container");
		String[] splitContent = new String[0]; // Used to hold comment magnets
		
		boolean containsComment = this.content.contains(".:2:.");
		if (containsComment) {
			splitContent = this.content.split(".:2:.");
			this.content = splitContent[0];
		}
		
		if(this.content.contains(Consts.HIDE_START)){  // Checks to see if the magnet contains hidden code
			hiddenCode = this.content.substring(this.content.indexOf(Consts.HIDE_START)+Consts.HIDE_START.length(),this.content.indexOf(Consts.HIDE_END)); //  Getting the hidden code
			hiddenCode = hiddenCode.replaceAll("<br/>|<br />|<br>", "<br/>"+Consts.HC_DELIMITER);
			// Label doesn't include hidden code, but content will (done near end of constructor)
			String beforeHide = this.content.substring(0, this.content.indexOf(Consts.HIDE_START));
			String afterHide = this.content.substring(this.content.indexOf(Consts.HIDE_END) + Consts.HIDE_END.length());
			this.content = beforeHide + Consts.HIDDEN_CODE + afterHide;
		}		
		switch(specialCondition){
			case Consts.MAIN:
				isMain = true;    // needed for resetting CSS styling
				stackable = true;
				setStyleName("main_code_container");
				break;
			case Consts.INNER:
				if(content.contains(Consts.PANEL_TAG)){
					stackable = true;
				} else {
					stackable = false;
				}
				break;
			case Consts.STATEMENT:
				if(content.contains(Consts.PANEL_TAG)){	
					stackable = true;
				} else {
					stackable = false;
				}
				DragController.INSTANCE.makeDraggable(this);
				break;
			case Consts.COMMENT:
				stackable = false;
				if(problemType == ProblemType.JAVA){
					this.content = "//" + this.content;
				}else if(problemType == ProblemType.PROLOG){
					this.content = "%" + this.content;
				}
				this.getStyleElement().getStyle().setProperty("border","none");
				break;
			default:
				System.err.println("Bad - you shouldn't be here!  Stackable container constructor error.");
				break;
		}	
		
		if(stackable){
			topLabel = new HTML(this.content.substring(0, this.content.indexOf(Consts.PANEL_TAG)
					+ Consts.PANEL_TAG.length()), true);
			topPanel.add(topLabel);
			primaryPanel.add(topPanel);
			primaryPanel.add(insidePanel);
			
			// IF STUFF BREAKS THIS MAY BE THE CULPRIT ADD PANEL_TAG ETC.
			bottomLabel = new HTML(this.content.substring(this.content.indexOf(Consts.PANEL_TAG)
							+ Consts.PANEL_TAG.length(), this.content.length()), true);
			bottomPanel.add(bottomLabel);
			primaryPanel.add(bottomPanel);
		} else {
			// topLabel is actually the only label!
			topLabel = new HTML(this.content);
			// topPanel is actually the only panel!
			topPanel.add(topLabel);
			primaryPanel.add(topPanel);
		}

		// Adds comment magnets to insidePanel
		// NOTE:  Comments can only be added to stackable containers
		if(containsComment){
			for(int i=1; i<splitContent.length;i++){
				addInsideContainer(new StackableContainer(splitContent[i], Consts.COMMENT, this.problemType));
			}
		}
		
		// Determining what type of magnet we're dealing with.
		

		String[] dataTypes = {"int","double","String","boolean", "float", "ArrayList", "byte", "char"};
		String[] accessModifiers = {"public","private","protected"};
		String low = topCode != null ? topCode : topLabel.getText();
		low = low.toLowerCase();
		
		switch(problemType){
			case JAVA:
				if(isMain){
					magnetType = MagnetType.MAIN;
				}else if(low.matches("[ ]??(for|while).*")){
					magnetType = MagnetType.LOOP;
				}else if(low.matches("[ ]??(if|else).*")){
					magnetType = MagnetType.CONDITIONAL;
				}else if(low.matches("[ ]??return.*")){
					magnetType = MagnetType.RETURN;
				}else if(low.matches("[ ]??("+implode("|",dataTypes)+").*")){
					magnetType = MagnetType.DECLARATION;
				}else if(low.matches("[ ]??("+implode("|",accessModifiers)+").*")){
					magnetType = MagnetType.FUNCTION;
				}else{
					magnetType = MagnetType.ASSORTED;
				}
			break;
			case PROLOG:
				String bot = bottomCode != null ? bottomCode : bottomLabel != null ? bottomLabel.getText() : null;
				if(low.contains(":-") && bot != null && bot.endsWith(".")){
					magnetType = MagnetType.RULE;
				}else if (low.endsWith(".")){
					magnetType = MagnetType.FACT;
				} else{
					magnetType = MagnetType.TERM;
				}
				break;
		}
		addStyleName(magnetType.toString());
		
		this.content = content.split(".:2:.")[0]; // Reverts back to actual code - hidden stuff and all.
	}
	
	public void setEngaged(boolean engaged) {
		if (engaged) {
			if(!stackable){
				setStyleName("nonstackable_container_over");
			}else {
				this.removeStyleName(magnetType.toString());
				this.addStyleName(magnetType.toString()+"_over");
			}
		} else {
			if(stackable){
				this.removeStyleName(magnetType.toString()+"_over");
			}
			this.addStyleName(magnetType.toString());
		}
	}
	public void addConditionContent(String s) {
		content = content.replaceAll(Consts.CONDITION, s);
		updateContent();
	}
	
	/**
	 * Currently used with return statements to replace the VALUE tag with
	 * a provided return value
	 * 
	 * @param s the value to return
	 */
	public void addReturnContent(String s) {
		content = content.replaceAll(Consts.VALUE, s);
		updateStatementContent();
	}
		
	/**
	 * Used for creating assignment statements by replacing the VARIABLE and VALUE tags
	 * in the string with provided values
	 * 
	 * @param var The name of the variable
	 * @param val the name of the value to assign the var parameter to 
	 */
	public void addVariableContent(String var, String val) {
		content = content.replaceAll(Consts.VARIABLE, var);
		content = content.replaceAll(Consts.VALUE, val);
		updateStatementContent();
	}
	
	/**
	 * Places another stackable container inside of this one
	 * 
	 * @param sc The stackable container to place 
	 */
	public void addInsideContainer(StackableContainer sc) {
		insidePanel.add(sc);
	}

	public void addInsideContainer(StackableContainer child, DragContext context) {
		if (insidePanel.getWidgetCount() > 0) {
			ArrayList<StackableContainer> children = new ArrayList<StackableContainer>();
			ArrayList<StackableContainer> sortedChildren = new ArrayList<StackableContainer>();
			for (int i = 0; i < insidePanel.getWidgetCount(); i++) {
				if(!child.equals(insidePanel.getWidget(i)))
					children.add((StackableContainer) insidePanel.getWidget(i));
			}
			while (children.size() > 0) {
				int maxHeight = children.get(0).getAbsoluteTop();
				int maxHeightIndex = 0;
				for (int i = 0; i < children.size(); i++) {
					if (children.get(i).getAbsoluteTop() < maxHeight) {
						maxHeight = children.get(i).getAbsoluteTop();
						maxHeightIndex = i;
					}
				}
				sortedChildren.add(children.get(maxHeightIndex));
				children.remove(maxHeightIndex);
			}
			boolean done = false;
			for (int i = 0; i < sortedChildren.size(); i++) {
				if (sortedChildren.get(i).getAbsoluteTop() > context.mouseY
						&& !done) {
					sortedChildren.add(i, child);
					done = true;
				}
			}
			for (StackableContainer sc : sortedChildren) {
				insidePanel.add(sc);
			}
			if (!done) { // If panel is being added to bottom.
				insidePanel.add(child);
			}
			if(problemType == ProblemType.PROLOG){
				updateCommas();
			}
		} else {
			insidePanel.add(child);
			child.removeComma(); // May drag a magnet that has a comma into a magnet in ConstructUi
		}
	}

	/**
	 * Updates the contents of magnets containing statements, that is magnets
	 * which do not a panel to stack more magnets in (returns, assigns, etc)
	 */
	public void updateStatementContent() {
		topPanel.remove(topLabel);
		topLabel = new HTML(content);
		topPanel.add(topLabel);
	}
	
	/**
	 * Is used to update the contents of magnets which can have further magnets
	 * stacked inside of them such as for loops, while loops, if statements, etc...
	 */
	public void updateContent() {		
		topPanel.remove(topLabel);
		bottomPanel.remove(bottomLabel);
		if (!content.contains(Consts.PANEL_TAG)) {
			topLabel = new HTML(content);
		} else {
			topLabel = new HTML(content.substring(0,
					content.indexOf(Consts.PANEL_TAG) + Consts.PANEL_TAG.length()));
		}
		bottomLabel = new HTML(content.substring(content.indexOf(Consts.PANEL_TAG)
						+ Consts.PANEL_TAG.length(), content.length()));
		topPanel.add(topLabel);
		bottomPanel.add(bottomLabel);
	}
	
	public boolean hasChild(int childID){
		for (int i = 0; i < insidePanel.getWidgetCount(); i++) {
			if (insidePanel.getWidget(i) instanceof StackableContainer 
					&& ((StackableContainer) insidePanel.getWidget(i)).getID() == childID) {
				return true;
			}
		}
		return false;
	}

	public HTML getTopLabel() {
		if(hasCode){
			return new HTML(topCode);
		}
		else if(topLabel.getHTML().contains(Consts.HIDDEN_CODE)){
			return new HTML(topLabel.getHTML().replace(Consts.HIDDEN_CODE,hiddenCode));
		}
		return topLabel;
	}

	public HTML getBottomLabel() {
		if(hasCode){
			return new HTML(bottomCode); // Possibly not what we want. I think all the code should be contained within code.
		}
		else if(bottomLabel != null){
			if(bottomLabel.getHTML().contains(Consts.HIDDEN_CODE)){
				return new HTML(bottomLabel.getHTML().replace(Consts.HIDDEN_CODE,hiddenCode));
			}
		}
		return bottomLabel;
	}
	
	public void prependComma(){
		if(problemType == ProblemType.PROLOG && magnetType == MagnetType.TERM && !prependedComma){
			topLabel.setText(", "+topLabel.getText());
			prependedComma = true;
		}
	}
	
	public void removeComma(){
		if(problemType == ProblemType.PROLOG && prependedComma){
			topLabel.setText(topLabel.getText().substring(2));
			prependedComma = false;
		}
	}
	
	public void updateCommas(){
		for(int i = 1; i < insidePanel.getWidgetCount(); i++){
			((StackableContainer)insidePanel.getWidget(i)).prependComma();
		}
	}
	
	public AbsolutePanel getInsidePanel() { return insidePanel; }
	public String getContent() { return content; }
	public int getID() { return containerID; }
	public boolean isStackable() { return stackable; }
	public boolean isCreated() { return isCreated; }
	public int getLeft() { return this.getAbsoluteLeft(); }
	public int getWidth() { return this.getOffsetWidth(); }
	public int getTop() { return this.getAbsoluteTop(); }
	public int getHeight() { return this.getOffsetHeight(); }
	public MagnetType getMagnetType() { return this.magnetType; }
	
	public void setID(int id) { containerID = id; }
	public void setMain(boolean main) { this.isMain = main; }
	public void setStackable(boolean stack) { stackable = stack; }
	public void setCreated(boolean created) { this.isCreated = created; }
	public void setMagnetType(MagnetType type){ this.magnetType = type;	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		DragController.INSTANCE.registerDropController(dropController);
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		DragController.INSTANCE.unregisterDropController(dropController);
	}
	
	public String implode(String glue, String[] strArray)
	{
	    String ret = "";
	    for(int i=0;i<strArray.length;i++)
	    {
	        ret += (i == strArray.length - 1) ? strArray[i] : strArray[i] + glue;
	    }
	    return ret;
	}
}
