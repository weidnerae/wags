package webEditor.magnet.view;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.LayoutPanel;

/**
 * The editing panel is where the bulk of the magnets UI is housed.
 * It consists of a code panel, to the right, and a construct panel, to the left.
 *
 */

public class EditingPanelUi extends Composite {

	private static EditingPanelUiUiBinder uiBinder = GWT
			.create(EditingPanelUiUiBinder.class);
	private ConstructUi construct;

	interface EditingPanelUiUiBinder extends UiBinder<Widget, EditingPanelUi> {
	}
	
	@UiField LayoutPanel codePanel;  //the right hand side -> build code here
	@UiField LayoutPanel constructPanel;  //the left hand side -> drag code segments from here
	@UiField LayoutPanel layout; //the panel holding it all together

	public EditingPanelUi(RefrigeratorMagnet magnet, int tabPanelHeight,String title, String directions, StackableContainer mainFunction, StackableContainer[] insideSegments, String problemType, StackableContainer[] premadeSegments ,int numCreatedStatements, int numStatements, String[] structuresList, String[] for1List,String[] for2List,String[] for3List, String[] booleanList, int[] limits, String solution, String[] premadeIDs, PickupDragController dc) {
		initWidget(uiBinder.createAndBindUi(this));
		construct = new ConstructUi(problemType, premadeSegments,Integer.parseInt(premadeSegments[premadeSegments.length-1].getID()), title, directions, structuresList, for1List, for2List, for3List, booleanList, limits, dc);
		constructPanel.add(construct);
		codePanel.add(new CodePanelUi(magnet, mainFunction, insideSegments,numStatements, dc, title));
		layout.setSize("100%", tabPanelHeight - 60 + "px");  //stupid magic number to make panel show correctly
	}
	
	// used to get over the delay between instantiating panels and then
	// adding magnets to them based on x,y coordinates
	public void start() {
		construct.start();
	}
	
	public void resetProblem(){
			construct.reset();
	}

}

