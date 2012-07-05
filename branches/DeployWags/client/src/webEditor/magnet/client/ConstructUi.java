package webEditor.magnet.client;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class ConstructUi extends Composite {
	private TrashBin bin = new TrashBin();
	private StackableContainer[] premade;
	private boolean initial = false;
	private boolean first = true;
	private String problemType;
	private AbsolutePanel contentPanel; //nest panel to hold creation station and segments content
	private AbsolutePanel csContent;  //nest panel to hold creationStation
	private CreationStation creationStation;
	private AbsolutePanel segmentsContent;
	private AbsolutePositionDropController csDropControl;
	private AbsolutePositionDropController segmentDropControl; 
	
	@UiField
	AbsolutePanel directionsContent;
	@UiField
	AbsolutePanel trashbin;
	@UiField
	DockLayoutPanel layout;

	private static ConstructUiUiBinder uiBinder = GWT
			.create(ConstructUiUiBinder.class);

	interface ConstructUiUiBinder extends UiBinder<Widget, ConstructUi> {
	}

	/**
	 * Creates ConstructUi. Adds appropriate content to UiFields
	 * 
	 * @param creationStation
	 *            boolean determines if problem needs creation station (and
	 *            trash bin)
	 * @param premadeSegments
	 *            sc[] lines of code given
	 * @param title
	 *            String
	 * @param description
	 *            String
	 * @param structuresList
	 *            String[] the necessary decision structures needed
	 * @param for1List
	 *            String[] first for condition choices
	 * @param for2List
	 *            String[] second for condition choices
	 * @param for3List
	 *            String[] third for condition choices
	 * @param booleanList
	 *            String[] boolean condition choices
	 * @param dc
	 *            DragController the drag controller passed from rootpanel.
	 */
	public ConstructUi(String problemType,
			StackableContainer[] premadeSegments, String title,
			String description, String[] structuresList, String[] for1List,
			String[] for2List, String[] for3List, String[] booleanList,
			PickupDragController dc) {
		initWidget(uiBinder.createAndBindUi(this));
		// add directions
		directionsContent.add(new HTML("<h4><center>" + title
				+ "</center></h4>" + description + "<br/>"));
		
		this.problemType = problemType;


		if (problemType.equals(Consts.ADVANCED_PROBLEM)) {
			//create the creation station panel, 
			//then create a content panel to nest that and the segments panel.
			//create and reg necesary drop controllers
			//add it to center
			csContent = new AbsolutePanel();
			csDropControl = new AbsolutePositionDropController(csContent);
			dc.registerDropController(csDropControl);
			creationStation = new CreationStation(structuresList, for1List,
					for2List, for3List, booleanList, this, dc);
			csContent.add(creationStation);
			csContent.setStyleName("creation_station");
			
			contentPanel = new AbsolutePanel();
			contentPanel.add(csContent);

			segmentsContent = new AbsolutePanel();
			segmentDropControl = new AbsolutePositionDropController(segmentsContent);
			dc.registerDropController(segmentDropControl);
			contentPanel.add(segmentsContent);
			
			layout.add(contentPanel);
			
			BinDropController binController = new BinDropController(bin);
			dc.registerDropController(binController);
			trashbin.add(bin);
			
			//timer fix: sets the height a milisecond after the panel is created so that it returns correct
			//getOffsetHeight() values
			
			Timer t = new Timer (){
				@Override
				public void run() {
					segmentsContent.setHeight("" + (getOffsetHeight() - (140 + csContent.getOffsetHeight())) + "px");
					contentPanel.setHeight("" + (getOffsetHeight() - 140) + "px");
				}
			};
			t.schedule(1);
		} else {
			//create just segments panel and drop control
			//add it to center
			segmentsContent = new AbsolutePanel();
			segmentsContent.setWidth("100%");
			segmentsContent.setHeight("100%");
			segmentDropControl = new AbsolutePositionDropController(segmentsContent);
			dc.registerDropController(segmentDropControl);
			
			layout.add(segmentsContent);
		}
		
		
		premade = premadeSegments;		
	}
	
	public void start() {
		addSegments(premade);
	}

	public void addSegments(StackableContainer[] segments) {
		if (segments != null) {
			for (StackableContainer segment : segments) {
				addSegment(segment);
			}
		}
		
		initial = true;
	}
	
	/**
	 * BEWARE: THERE BE DRAGONS AHEAD
	 * 
	 * 
	 * @param segment
	 */
	public void addSegment(final StackableContainer segment) {
		Timer timer = new Timer() {
			@Override
			public void run() {
				int baseX = 10;
				int baseY = 10;
				int widgetCount = segmentsContent.getWidgetCount();
				
				if (widgetCount == 0) {
					segmentsContent.add(segment, baseX, baseY);
				} else {
					StackableContainer lastWidget = (StackableContainer) segmentsContent.getWidget(segmentsContent.getWidgetCount() - 1);
					boolean newColumn = lastWidget.getTop() + lastWidget.getHeight() > 
										segmentsContent.getAbsoluteTop() + segmentsContent.getOffsetHeight() - 70 - getAbsoluteTop();
					
					if (newColumn) {		
						for (int i = 0; i < widgetCount; i++) {
							StackableContainer w = (StackableContainer)segmentsContent.getWidget(i);
							
							if (w.getLeft() + w.getWidth() > baseX) {
								baseX = w.getLeft() + w.getWidth();
							}
						}
						
						segmentsContent.add(segment, baseX, baseY);
					} else {	
						
						if (initial && first) {
							int left = lastWidget.getAbsoluteLeft();
							int top = lastWidget.getAbsoluteTop();
						
							lastWidget.removeFromParent();
							
							segmentsContent.add(lastWidget, left - getAbsoluteLeft(), top - segmentsContent.getAbsoluteTop() - 10);
							
							first = false;
						}
						
						baseX = lastWidget.getLeft() - getAbsoluteLeft();
						baseY = lastWidget.getTop() + lastWidget.getHeight() - getAbsoluteTop();
						
						if (initial) {
							baseY -= segmentsContent.getAbsoluteTop() - getAbsoluteTop();
						} else if (problemType.equals(Consts.ADVANCED_PROBLEM)) {				
							baseY -= creationStation.getOffsetHeight();
						}
						
						segmentsContent.add(segment, baseX, baseY);
					}
				}
			}
		};
		
		timer.schedule(1);
	}

}
