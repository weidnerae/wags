package wags.magnet.view;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
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
	private boolean initial = false; //a dragon's boolean
	private String problemType; //differentiates between algo, basic, and advanced
	private AbsolutePanel contentPanel; //nest panel to hold mgnet maker and segments content
	private AbsolutePanel mmContent;    //nest panel to hold magnet maker
	private CreationStation magnetMaker;
	private AbsolutePanel segmentsContent;
	private AbsolutePositionDropController segmentDropControl; 
	private int nextID; // used for assigning created magnets ID's
	
	@UiField
	AbsolutePanel directionsContent;  //place for directions
	@UiField
	AbsolutePanel trashbin;  
	@UiField
	DockLayoutPanel layout;  //panel that holds entire left hand side of UI

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
	 *            String[] first 'for' condition choices
	 * @param for2List
	 *            String[] second 'for' condition choices
	 * @param for3List
	 *            String[] third 'for' condition choices
	 * @param booleanList
	 *            String[] boolean condition choices
	 * @param dc
	 *            DragController the drag controller passed from rootpanel.
	 */
	public ConstructUi(String problemType,
			StackableContainer[] premadeSegments, int numMagnets,String title,
			String description, String[] structuresList, String[] for1List,
			String[] for2List, String[] for3List, String[] booleanList,
			PickupDragController dc) {
		initWidget(uiBinder.createAndBindUi(this));
		
		directionsContent.add(
			new HTML(
				"<h4><center>" 
				+ title
				+ "</center></h4>" 
				+ description.replace("\\r\\n", "<br/>").replace("\\\"", "\"") 
				+ "<br/>"
			)	
		);
		
		this.problemType = problemType;
		this.nextID = numMagnets+1;

		if (problemType.equals(Consts.ADVANCED_PROBLEM)) {
			//create the creation station panel, 
			//then create a content panel to nest that and the segments panel.
			//create and register necessary drop controller
			//add it to center
			mmContent = new AbsolutePanel();
			magnetMaker = new CreationStation(structuresList, for1List,
					for2List, for3List, booleanList, this, dc, nextID);
			mmContent.add(magnetMaker);
			mmContent.setStyleName("creation_station");
			
			contentPanel = new AbsolutePanel();
			contentPanel.add(mmContent);

			segmentsContent = new AbsolutePanel();
			segmentDropControl = new AbsolutePositionDropController(segmentsContent);
			dc.registerDropController(segmentDropControl);
			segmentsContent.getElement().getStyle().setOverflowY(Overflow.AUTO);  //enables scrolling
			contentPanel.add(segmentsContent);
			
			layout.add(contentPanel);
			
			bin = new TrashBin();
			BinDropController binController = new BinDropController(bin);
			dc.registerDropController(binController);
			trashbin.add(bin);
			
			//timer fix: sets the height a milisecond after the panel is created so that it returns correct
			//getOffsetHeight() values
			
			Timer t = new Timer (){
				@Override
				public void run() {
					segmentsContent.setHeight("" + (getOffsetHeight() - (140 + mmContent.getOffsetHeight())) + "px");
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
			segmentsContent.getElement().getStyle().setOverflowY(Overflow.AUTO);
			layout.add(segmentsContent);
		}

		premade = premadeSegments;		
	}
	
	//this method is called after the constructor because there is a delay between instantiating the panel
	//and placing all the segments to the segmentsContent panel
	public void start() {
		initial = false;            // Just added this to check 2/26/13
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
     * Adds a segment to the left side of the screen.
     * 
     * BEWARE: THERE BE DRAGONS AHEAD
     * 
     * @param segment
     */
    public void addSegment(final StackableContainer segment) {
        /*
         * In order for getOffsetWidth(), getAbsoluteTop(), etc. to return the correct values, we have to wrap the calls in a Timer object.
         * I don't fully understand why this works, but it results in functional code. Current theory is that adding it to the timer allows 
         * everything to be added to the DOM before we try to call the methods.
         * 
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
                if(problemType.equals(Consts.ADVANCED_PROBLEM)) 
                	baseY = 10 + magnetMaker.getOffsetHeight();
      
                                            
                if (widgetCount == 0) {
                        // we add the first widget at an offset of 10, 10"
                        segmentsContent.add(segment, baseX, baseY);
                } else {
                        // after the first, we calculate where the next widget should go based on the last widget in the panel
                        StackableContainer lastWidget = (StackableContainer) segmentsContent.getWidget(segmentsContent.getWidgetCount() - 1);
                             
                        baseY = lastWidget.getTop() + lastWidget.getHeight() - getAbsoluteTop();
                        
  //    }   ADD THIS BACK.
//                                  // for the first set of magnets (i.e. before the user adds any they created), we have to have a special
                        // flag and treat them differently
                        if (initial) {
                                baseY -= segmentsContent.getAbsoluteTop() - getAbsoluteTop();
                        } else if (problemType.equals(Consts.ADVANCED_PROBLEM)) { 
                   //             baseY -= magnetMaker.getOffsetHeight();
                        }
                } // get rid of this.
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
