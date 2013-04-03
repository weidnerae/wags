package webEditor.magnet.view;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
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
	private String topJavaCode;
	private String bottomJavaCode;
	
	private final PickupDragController dragController;
	private DropController dropController = new PanelDropController(this);

	private boolean stackable = true;
	private boolean isMain = false;
	private boolean hasCode = false;
	private String containerID;
	
	String content = "";

	/**
	 * A stackable container for the overall code with main and such. This
	 * container is not draggable.
	 * 
	 * @param content
	 *            the HTML formatted string
	 * @param dc
	 *            the drag controller
	 * @param specialCondition
	 *            usually main
	 */
	public StackableContainer(String content, PickupDragController dc,
			int specialCondition) { // For mains, non draggable
		this.content = content;
		
		// pulling out the java code
		if(this.content.contains(Consts.CODE_START)){
			hasCode = true;
			String javaCode = this.content.substring(this.content.indexOf(Consts.CODE_START)+Consts.CODE_START.length(),this.content.indexOf(Consts.CODE_END));
			
			// If this magnet nests
			if(this.content.indexOf(Consts.CODE_SPLIT) != -1){
				topJavaCode = javaCode.substring(0,javaCode.indexOf(Consts.CODE_SPLIT));
				bottomJavaCode = javaCode.substring(javaCode.indexOf(Consts.CODE_SPLIT)+Consts.CODE_SPLIT.length());
			
				String contentBeforeCode = this.content.substring(0,this.content.indexOf(Consts.CODE_START));
				String contentAfterCode = this.content.substring(this.content.indexOf(Consts.CODE_END)+Consts.CODE_END.length());
				this.content = contentBeforeCode+contentAfterCode;
			// If not
			} else {
				topJavaCode = javaCode;
				this.content = this.content.substring(0, this.content.indexOf(Consts.CODE_START));
			}
			
			
		}
		
		add(primaryPanel);  // primaryPanel holds everything else, because the focusPanel can only hold one widget
		setStyleName("stackable_container");
		this.dragController = dc;
		String[] splitContent = new String[0]; // Used to hold comment magnets
		
		boolean containsComment = this.content.contains(".:2:.");
		if(containsComment){
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
				dragController.makeDraggable(this);
				break;
			case Consts.COMMENT:
				stackable = false;
				this.content = "//" + this.content;
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
				addInsideContainer(new StackableContainer(splitContent[i], dc, Consts.COMMENT));
			}
		}
		
		this.content = content.split(".:2:.")[0]; // Reverts back to actual code - hidden stuff and all.
	}

	
	public void setEngaged(boolean engaged) {
		if (engaged) {
			if (isMain) {
				setStyleName("main_code_over");
			} else {
				if(!stackable){
					setStyleName("nonstackable_container_over");
				} else{
					setStyleName("stackable_container_over");
				}
			}
		} else {
			if (isMain) {
				setStyleName("main_code_container");
			} else {
				setStyleName("stackable_container");
			}
		}
	}
	public void addConditionContent(String s) {
		content = content.replaceAll(Consts.CONDITION, s);
		updateContent();
	}
	
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

		} else {
			insidePanel.add(child);
		}
	}

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
	
	public boolean hasChild(String childID){
		for (int i = 0; i < insidePanel.getWidgetCount(); i++) {
			if(insidePanel.getWidget(i) instanceof StackableContainer && ((StackableContainer)insidePanel.getWidget(i)).getID().equals(childID)){
				return true;
			}
		}
		return false;
	}

	public boolean isStackable() {
		return stackable;
	}

	public HTML getTopLabel() {
		if(hasCode){
			return new HTML(topJavaCode);
		}
		else if(topLabel.getHTML().contains(Consts.HIDDEN_CODE)){
			return new HTML(topLabel.getHTML().replace(Consts.HIDDEN_CODE,hiddenCode));
		}
		return topLabel;
	}

	public HTML getBottomLabel() {
		if(hasCode){
			return new HTML(bottomJavaCode); // Possibly not what we want. I think all the code should be contained within javaCode.
		}
		else if(bottomLabel != null){
			if(bottomLabel.getHTML().contains(Consts.HIDDEN_CODE)){
				return new HTML(bottomLabel.getHTML().replace(Consts.HIDDEN_CODE,hiddenCode));
			}
		}
		return bottomLabel;
	}

	public AbsolutePanel getInsidePanel() {
		return insidePanel;
	}
	public String getContent(){
		return content;
	}
	
	public int getLeft() {
		return this.getAbsoluteLeft();
	}
	
	public int getWidth() {
		return this.getOffsetWidth();
	}
	
	public int getTop() {
		return this.getAbsoluteTop();
	}
	
	public int getHeight() {
		return this.getOffsetHeight();
	}
	public String getID(){
		return containerID;
	}
	public void setID(String id){
		containerID = id;
	}
	public void setMain(boolean main){
		this.isMain = main;
	}
	public void setStackable(boolean stack){
		stackable = stack;
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		dragController.registerDropController(dropController);
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		dragController.unregisterDropController(dropController);
	}

}
