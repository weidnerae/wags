package webEditor.magnet.client;

import com.allen_sauer.gwt.dnd.client.PickupDragController;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.LayoutPanel;

public class EditingPanelUi extends Composite {

	private static EditingPanelUiUiBinder uiBinder = GWT
			.create(EditingPanelUiUiBinder.class);
	private ConstructUi construct;

	interface EditingPanelUiUiBinder extends UiBinder<Widget, EditingPanelUi> {
	}
	
	@UiField LayoutPanel codePanel;
	@UiField LayoutPanel constructPanel;
	@UiField LayoutPanel layout;

	public EditingPanelUi(RefrigeratorMagnet magnet, int tabPanelHeight,String title, String directions, StackableContainer mainFunction, StackableContainer[] insideSegments, String problemType, StackableContainer[] premadeSegments, String[] structuresList, String[] for1List,String[] for2List,String[] for3List, String[] booleanList, String solution, String[] premadeIDs, PickupDragController dc) {
		initWidget(uiBinder.createAndBindUi(this));
		codePanel.add(new CodePanelUi(magnet, mainFunction, insideSegments, dc, title));
		construct = new ConstructUi(problemType, premadeSegments, title, directions, structuresList, for1List, for2List, for3List, booleanList, dc);
		constructPanel.add(construct);
		layout.setSize("100%", tabPanelHeight - 60 + "px");  //stupid magic number to make panel show correctly
	}
	
	public void start() {
		construct.start();
	}

}
