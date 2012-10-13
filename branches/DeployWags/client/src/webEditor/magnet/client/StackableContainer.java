package webEditor.magnet.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;

import java.util.ArrayList;

/**
 * Stackable Containers that hold the code and do most of the work as far as
 * dragging and dropping is concerned.
 * 
 * @author Daniel Cook, Alex Weidner, Reed Phillips
 * 
 *         Refrigerator Magnet Microlab CS 3460 - Kurtz
 * @version r28
 */
public class StackableContainer extends FocusPanel {
	private AbsolutePanel innerPanel = new AbsolutePanel();
	private AbsolutePanel topPanel = new AbsolutePanel();
	private AbsolutePanel insidePanel = new AbsolutePanel();
	private AbsolutePanel bottomPanel = new AbsolutePanel();
	private HTML topLabel;
	private HTML bottomLabel;
	private String hiddenCode;
	
	private final PickupDragController dragController;
	private DropController dropController = new PanelDropController(this);

	private boolean stackable = true;
	private boolean isMain = false;
	private String containerID;
	
	String content = "";

	/**
	 * Constructor - Sets up a stackable container by adding HTML formatted
	 * content to the corresponding panel within the container.
	 * 
	 * @param content
	 *            the HTML formatted text to be added to the container.
	 * @param dc
	 *            the drag controller
	 */
	public StackableContainer(String content, PickupDragController dc) {
		add(innerPanel);
		this.dragController = dc;
		boolean hasHidden = content.contains(Consts.HIDE_START);
		if(hasHidden){
			hiddenCode = content.substring(content.indexOf(Consts.HIDE_START)+Consts.HIDE_START.length(),content.indexOf(Consts.HIDE_END)); //  Getting the hidden code
			hiddenCode = hiddenCode.replaceAll("<br/>|<br />|<br>", "<br/>"+Consts.HC_DELIMITER);
			Window.alert(hiddenCode);
			this.content = content.substring(0,content.indexOf(Consts.HIDE_START))+Consts.HIDDEN_CODE+content.substring(content.indexOf(Consts.HIDE_END),content.length()-1);
		}else{
			this.content = content;
		}
		boolean containsComment = this.content.contains(".:2:.");
		if(containsComment){
			String[] splitContent = this.content.split(".:2:.");
			this.content = splitContent[0];
		}
		if (!this.content.contains(Consts.INSIDE)) {
			topLabel = new HTML(this.content);
		} else {
			topLabel = new HTML(this.content.substring(0,
					this.content.indexOf(Consts.INSIDE) + Consts.INSIDE.length()),
					true);
		}
		topPanel.add(topLabel);
		innerPanel.add(topPanel);
		innerPanel.add(insidePanel);
		bottomLabel = new HTML(Consts.BOTTOM
				+ this.content.substring(this.content.indexOf(Consts.INSIDE)
						+ Consts.INSIDE.length(), this.content.length()), true);
		bottomPanel.add(bottomLabel);
		innerPanel.add(bottomPanel);
		setStyleName("stackable_container");
		dragController.makeDraggable(this);
		if(containsComment){
			String[] splitContent = content.split(".:2:.");
			for(int i=1; i<splitContent.length;i++){
				addInsideContainer(new StackableContainer("// "+splitContent[i]+Consts.TOP + Consts.INSIDE + Consts.BOTTOM,dc, Consts.INSIDE_COMMENT));
			}
		}
		this.content = content.split(".:2:.")[0];
	}

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
			String specialCondition) { // For mains, non draggable
		add(innerPanel);
		setStyleName("stackable_container");

		this.dragController = dc;
		boolean hasHidden = content.contains(Consts.HIDE_START);
		if(hasHidden){
			hiddenCode = content.substring(content.indexOf(Consts.HIDE_START)+Consts.HIDE_START.length(),content.indexOf(Consts.HIDE_END)); //  Getting the hidden code
			hiddenCode = hiddenCode.replaceAll("<br/>|<br />|<br>", "<br/>"+Consts.HC_DELIMITER);
			Window.alert(hiddenCode);
			this.content = content.substring(0,content.indexOf(Consts.HIDE_START))+Consts.HIDDEN_CODE+content.substring(content.indexOf(Consts.HIDE_END),content.length());
		}else{
			this.content = content;
		}
		boolean containsComment = this.content.contains(".:2:.");
		if(containsComment){
			String[] splitContent = this.content.split(".:2:.");
			this.content = splitContent[0];
		}
		topLabel = new HTML(this.content.substring(0, this.content.indexOf(Consts.INSIDE)
				+ Consts.INSIDE.length()), true);
		topPanel.add(topLabel);
		innerPanel.add(topPanel);
		innerPanel.add(insidePanel);
		bottomLabel = new HTML(Consts.BOTTOM
				+ this.content.substring(this.content.indexOf(Consts.INSIDE)
						+ Consts.INSIDE.length(), this.content.length()), true);
		bottomPanel.add(bottomLabel);
		innerPanel.add(bottomPanel);
		if (specialCondition.equals(Consts.MAIN)) {	
			isMain = true;
			setStyleName("main_code_container");
		}
		if(containsComment){
			String[] splitContent = content.split(".:2:.");
			for(int i=1; i<splitContent.length;i++){
				addInsideContainer(new StackableContainer("// "+splitContent[i]+Consts.TOP + Consts.INSIDE + Consts.BOTTOM,dc, Consts.INSIDE_COMMENT));
			}
		}
		if(specialCondition.equals(Consts.INSIDE_COMMENT)){
			stackable = false;
			this.getStyleElement().getStyle().setProperty("border","none");
		}
		this.content = content.split(".:2:.")[0];
	}

	/**
	 * Constructor that allows any container to be non stackable by providing a
	 * false as the final argument
	 * 
	 * @param content
	 *            the HTML formatted string
	 * @param dc
	 *            the drag controller
	 * @param s
	 *            true for draggable, false for undraggable
	 */
	public StackableContainer(String content, PickupDragController dc, boolean s) {
		add(innerPanel);

		stackable = s;
		this.dragController = dc;
		boolean hasHidden = content.contains(Consts.HIDE_START);
		if(hasHidden){
			hiddenCode = content.substring(content.indexOf(Consts.HIDE_START)+Consts.HIDE_START.length(),content.indexOf(Consts.HIDE_END)); //  Getting the hidden code
			hiddenCode = hiddenCode.replaceAll("<br/>|<br />|<br>", ("<br/>"+Consts.HC_DELIMITER));
			Window.alert(hiddenCode);
			this.content = content.substring(0,content.indexOf(Consts.HIDE_START))+Consts.HIDDEN_CODE+content.substring(content.indexOf(Consts.HIDE_END)+Consts.HIDE_END.length(),content.length()-1);
		}else{
			this.content = content;
		}
		topLabel = new HTML(this.content);
		innerPanel.add(topLabel);
		boolean containsComment = this.content.contains(".:2:.");
		if(containsComment){
			String[] splitContent = this.content.split(".:2:.");
			this.content = splitContent[0];
		}
		setStyleName("stackable_container");
		dragController.makeDraggable(this);
		if(containsComment){
			String[] splitContent = content.split(".:2:.");
			for(int i=1; i<splitContent.length;i++){
				addInsideContainer(new StackableContainer("// "+splitContent[i]+Consts.TOP + Consts.INSIDE + Consts.BOTTOM,dc, Consts.INSIDE_COMMENT));
			}
		}
		this.content = content.split(".:2:.")[0];
	}

	public void setEngaged(boolean engaged) {
		if (engaged) {
			if (isMain) {
				setStyleName("main_code_over");
			} else {
				setStyleName("stackable_container_over");
			}
		} else {
			if (isMain) {
				setStyleName("main_code_container");
			} else {
				setStyleName("stackable_container");
			}
		}
	}

	public void addVeryTopContent(String s) {
		content = content.replaceAll(Consts.TOP, s);
		updateContent();
	}

	public void addConditionContent(String s) {
		content = content.replaceAll(Consts.CONDITION, s);
		updateContent();
	}

	public void addTopContent(String s) {
		content = content.replaceAll(Consts.TOP, s);
		updateContent();
	}

	public void addInsideContent(String s) {
		insidePanel.add(new StackableContainer(s, dragController));
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
				if (sortedChildren.get(i).getAbsoluteTop() > context.desiredDraggableY
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

	public void addBottomContent(String s) {
		content = content.replaceAll(Consts.BOTTOM, s);
		updateContent();
	}

	public void addContent(String s, String position) {
		if (position.equals("top"))
			addTopContent(s);
		else if (position.equals("middle"))
			addInsideContent(s);
		else if (position.equals("bottom"))
			addBottomContent(s);
		else if (position.equals("verytop"))
			addVeryTopContent(s);
		else if (position.equals("condition"))
			addConditionContent(s);
		else
			addInsideContent(s);
	}

	public void updateContent() {
		topPanel.remove(topLabel);
		bottomPanel.remove(bottomLabel);
		
		if (!content.contains(Consts.INSIDE)) {
			topLabel = new HTML(content);
		} else {
			topLabel = new HTML(content.substring(0,
					content.indexOf(Consts.INSIDE) + Consts.INSIDE.length()));
		}
		bottomLabel = new HTML(Consts.BOTTOM
				+ content.substring(content.indexOf(Consts.INSIDE)
						+ Consts.INSIDE.length(), content.length()));
		topPanel.add(topLabel);
		bottomPanel.add(bottomLabel);
	}

	public boolean isStackable() {
		return stackable;
	}

	public boolean isMain() {
		return isMain;
	}

	public HTML getTopLabel() {
		if(topLabel.getHTML().contains(Consts.HIDDEN_CODE)){
			return new HTML(topLabel.getHTML().replace(Consts.HIDDEN_CODE,hiddenCode));
		}
		return topLabel;
	}

	public HTML getBottomLabel() {
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
