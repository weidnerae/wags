package webEditor.magnet.view;

import webEditor.MagnetProblem;

import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * This is effectively the left side of the screen in the editing mode tab.
 *
 */
public class ConstructUi extends Composite {
	private TrashBin bin;
	private StackableContainer[] premade; //field to store premade segments passed in
	private AbsolutePanel contentPanel; //nest panel to hold mgnet maker and segments content
	private AbsolutePanel mmContent;    //nest panel to hold magnet maker
	private MagnetMaker magnetMaker;
	private AbsolutePanel segmentsContent;
	private AbsolutePositionDropController segmentDropControl;
	private int[] segmentTops = new int[50];  //used to keep track of coordinates for added segments
	private int nextID; // used for assigning created magnets ID's
	private String problemType;
	
	@UiField AbsolutePanel directionsContent;  //place for directions
	@UiField AbsolutePanel trashbin;  
	@UiField DockLayoutPanel layout;  //panel that holds entire left hand side of UI

	private static ConstructUiUiBinder uiBinder = GWT.create(ConstructUiUiBinder.class);

	interface ConstructUiUiBinder extends UiBinder<Widget, ConstructUi> {
	}

	/**
	 * Creates ConstructUi. Adds appropriate content to UiFields

	 * @param premadeSegments
	 *            sc[] lines of code given
	 * @param forLists
	 *            String[][] for each for loop dropdown
	 */
	public ConstructUi(RefrigeratorMagnet refrigeratorMagnet, MagnetProblem magnet, StackableContainer[] premadeSegments, String[][] forLists, int numMagnets) {
		initWidget(uiBinder.createAndBindUi(this));
		
		directionsContent.add(
			new HTML(
				"<h4><center>" 
				+ magnet.title
				+ "</center></h4>" 
				+ magnet.directions.replace("\\r\\n", "<br/>").replace("\\\"", "\"") 
				+ "<br/>"
			)	
		);
		
		this.nextID = numMagnets + 1;
		this.problemType = magnet.type;
		this.premade = premadeSegments;
		
		if (problemType.equals(Consts.ADVANCED_PROBLEM)) {
			String[] sLimits = magnet.limits.split(",");
			int[] limits = new int[sLimits.length];
			int k = 0;
			for (String limit : sLimits) {
				limits[k++] = Integer.parseInt(limit);
			}
			
			//create the creation station panel, 
			//then create a content panel to nest that and the segments panel.
			//create and register necessary drop controller
			//add it to center
			mmContent = new AbsolutePanel();
			magnetMaker = new MagnetMaker(forLists, magnet.ifOptions, magnet.whileOptions, limits, this, nextID);
			mmContent.add(magnetMaker);
			mmContent.setStyleName("magnet_maker");
			
			contentPanel = new AbsolutePanel();
			contentPanel.add(mmContent);

			segmentsContent = new AbsolutePanel();
			segmentDropControl = new AbsolutePositionDropController(segmentsContent);
			DragController.INSTANCE.registerDropController(segmentDropControl);
			segmentsContent.getElement().getStyle().setOverflowY(Overflow.AUTO);  //enables scrolling
			contentPanel.add(segmentsContent);
			
			layout.add(contentPanel);
			
			bin = new TrashBin(magnetMaker);
			BinDropController binController = new BinDropController(bin);
			DragController.INSTANCE.registerDropController(binController);
			trashbin.add(bin);
			
			//timer fix: sets the height a millisecond after the panel is created so that it returns correct
			//getOffsetHeight() values
			Timer t = new Timer (){
				@Override
				public void run() {
					segmentsContent.setHeight((getOffsetHeight() - (140 + mmContent.getOffsetHeight())) + "px");
					contentPanel.setHeight((getOffsetHeight() - 140) + "px");
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
			DragController.INSTANCE.registerDropController(segmentDropControl);
			segmentsContent.getElement().getStyle().setOverflowY(Overflow.AUTO);
			layout.add(segmentsContent);
		}
		
		start();
	}
	
	public ConstructUi(String problemType,
			StackableContainer[] premadeSegments, int numMagnets, String title,
			String description, String[][] forLists, String[] booleanList, 
			int[] limits) {	
	}

	//this method is called after the constructor because there is a delay between instantiating the panel
	//and placing all the segments to the segmentsContent panel
	public void start() {
		addSegments(premade);
	}

	public void addSegments(StackableContainer[] segments) {
		if (segments == null) {
			return;
		}
		
		if (problemType.equals(Consts.ADVANCED_PROBLEM)) {
			magnetMaker.resetLimits();
		}
			
		for (StackableContainer segment : segments) {
			if (problemType.equals(Consts.ADVANCED_PROBLEM)) {
				String content = segment.getContent();
				if (content.startsWith("for")) {
					magnetMaker.decrementLimitCounter(MagnetMaker.FOR);
				} else if (content.startsWith("while")) {
					magnetMaker.decrementLimitCounter(MagnetMaker.WHILE);
				} else if (content.startsWith("if")) {
					magnetMaker.decrementLimitCounter(MagnetMaker.IF);
				} else if (content.startsWith("else if")) {
					magnetMaker.decrementLimitCounter(MagnetMaker.ELSE_IF);
				} else if (content.startsWith("else")) {
					magnetMaker.decrementLimitCounter(MagnetMaker.ELSE);
				}
			}
			
			addSegment(segment);
		}
	}
	
	/**
     * Adds a segment to the left side of the screen.
     * 
     * BEWARE: THERE BE DRAGONS AHEAD
     * 
     * @param segment
     */
    public void addSegment(final StackableContainer segment) {
        /*
         * This is ugly, and I don't like it, but after hours and hours of agonizing over the code, this is my only solution.
         * 
         * @author Jon Johnson
         * @version 7/6/2012
         */
        Timer timer = new Timer() {
            @Override
            public void run() {
            	int widgetCount = segmentsContent.getWidgetCount();
            	
            	//Spacing is 10 pixels
                int baseX = 10;
                int baseY = 10;
                                              
                if (widgetCount == 0) {
                    // we add the first widget at an offset of 10, 10
                    segmentsContent.add(segment, baseX, baseY);
                } else {
                    // after the first, we calculate where the next widget should go based on the last widget in the panel
                    StackableContainer lastWidget = (StackableContainer) segmentsContent.getWidget(segmentsContent.getWidgetCount() - 1);
                                      
                    //get raw position information for last widget placed in segmentscontent
                    //subtract it from absolute top of constructUI to account for tabs and anchor
                    baseY = lastWidget.getTop() + lastWidget.getHeight() - getAbsoluteTop();
                    
                    //subtract the length from the top of segments content up to the top of the UI
                    baseY -= segmentsContent.getAbsoluteTop() - getAbsoluteTop();
                    
                    //store the calculated baseY in case it is needed after scrolling
                    segmentTops[widgetCount - 1] = baseY;
                    
                    //check the current baseY against the relative top (baseY) 
                    //of the widget before the last one.  if the current one is smaller
                    //replace it with the one before hand and calculate the correct baseY
                    //by adding the height of the last widget added + 10 to the old baseY
                    if (widgetCount != 1 && baseY < segmentTops[widgetCount - 2]) {
                    	baseY = segmentTops[widgetCount - 2];
                    	baseY += lastWidget.getHeight() + 10;
                    	segmentTops[widgetCount -1] = baseY;
                    }
                }
                
                segmentsContent.add(segment, baseX, baseY);
            }  
        };
        
        timer.schedule(1);
    }
    
	public void reset(){
		segmentsContent.clear();
		start();
	}
}
