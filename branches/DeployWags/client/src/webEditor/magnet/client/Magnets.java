package webEditor.magnet.client;

import webEditor.client.MagnetProblem;
import webEditor.client.Proxy;
import webEditor.client.view.Wags;

import java.util.ArrayList;

import com.allen_sauer.gwt.dnd.client.PickupDragController;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * This is the "landing page" for magnets problems. It displays a list of 
 * buttons corresponding to the code magnet exercises that have been assigned 
 * to the user. If they have successfully completed the problem, the text on 
 * the button will be green. Otherwise, it will simply be black test displaying 
 * the name of the exercise.
 * 
 *
 */
public class Magnets extends AbsolutePanel {
	
	public static final String EMPTY_LABEL = "No Magnet Exercises Assigned!";
	static String[] structuresList = {"choose structure...","for","while","if","else if", "else"};

	private static int[] idList;			//array of problem id numbers
	private static String[] problemList;	//array of problem names
	private static boolean[] successList;	//array of success values

	final VerticalPanel problemPane = new VerticalPanel();

	private Label banner;
	private Label selectLabel;
	private ArrayList<ProblemButton> attemptButtons;
	
	private Wags wags;
	
	PickupDragController dc;
	
	/**
	 * This will generate a list of ProblemButtons corresponding to the ids and problems 
	 * passed in.
	 * 
	 * @param ids		id numbers for magnet problems
	 * @param problems	title text of magnet problems
	 * @param success	boolean for if magnet problem was completed successfully by the user
	 * @param wags		reference to Wags so that Wags can switch between different pages
	 * 					while retaining the top bar
	 */
	public Magnets(int[] ids, String[] problems, boolean[] success, Wags wags) {
		idList = ids;
		problemList = problems;
		successList = success;
		this.wags = wags;
		
		dc = new PickupDragController(RootPanel.get(), false);
		dc.setBehaviorDragProxy(true);
		
		banner = new Label("Code Magnet Microlabs");
		
		if(problems.length > 0){
			selectLabel = new Label(""); 
		} else {
			selectLabel = new Label(EMPTY_LABEL);
		}
		
		attemptButtons = new ArrayList<ProblemButton>();
		
		this.setStyleName("main_background");
		banner.setStyleName("banner");
		selectLabel.setStyleName("welcome");
		
		buildUI();
	}
	
	/**
	 * Method used to build the user interface.
	 */
	private void buildUI()
	{	
		this.removeAllWidgets();
		
		// create an attempt button for each problem
		// buttons text is green if they have completed the problem successfully
		for (int i = 0; i < problemList.length; i++) {
			int id = idList[i];
			String title = successList[i] ? "<font color=green>" + problemList[i] + "</font>" : problemList[i];
			ProblemButton b = new ProblemButton(title, id);
			attemptButtons.add(b);
		}
		
		add(problemPane);
		problemPane.clear();
		problemPane.setSpacing(5);
		problemPane.add(banner);
		problemPane.add(selectLabel);
		
		/*
		 * Add and format all the ProblemButtons
		 * 
		 * Wrap all this stuff in a timer so that getOffsetWidth() works correctly (I am ashamed)
		 */
		Timer timer = new Timer() {
			public void run() {
				int maxWidth = banner.getOffsetWidth(); //we want the buttons to be at least as wide as the banner

				// Add the style to all the buttons, add the buttons to problemPane,
				// and figure out the maximum width of all the buttons
				for (int i = 0; i < problemList.length; i++) {
					ProblemButton b = attemptButtons.get(i);
					addClickHandling(b, b.getID());
					b.setStyleName("problem");
					problemPane.add(b);

					// find the maximum width of buttons
					int width = attemptButtons.get(i).getOffsetWidth();
					maxWidth = (width > maxWidth) ? width : maxWidth;
				}

				// Go back through and set all the buttons to the max width
				for (int i = 0; i < problemList.length; i++) {
					ProblemButton b = attemptButtons.get(i);
					b.setWidth(maxWidth + "px");
					b.setHeight("50px");
				}
			}
		};
		timer.schedule(1);
	}
	
	/**
	 * Removes all widgets currently on the screen.
	 */
	public void removeAllWidgets() {
		for (int i = 0; i < this.getWidgetCount(); i++) {
			this.remove(this.getWidget(i));
		}
	}
	
	/**
	 * 
	 * Add a click handler to the button so that it calls 
	 * Proxy.getMagnetProblem() when clicked.
	 * 
	 */
	public void addClickHandling(ProblemButton b, final int id) {
		b.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
            	Timer timer = new Timer() {
            		public void run() {
            			Proxy.getMagnetProblem(id, wags);
            			History.newItem("?loc=magnetproblem");
            		}
            	};
            	timer.schedule(1);
            }
		});
	}
	
	/**
	 * @return a RefrigeratorMagnet object, created from the MagnetProblem 
	 * that was grabbed from the server using Proxy.getMagnetProblem()
	 */
	public RefrigeratorMagnet makeProblem(MagnetProblem magnet) {
		return new RefrigeratorMagnet(
				magnet.title,
				magnet.directions,
				getMainContainer(magnet.mainFunction),
				buildFunctions(magnet.innerFunctions),
				magnet.type,
				decodePremade(magnet.statements), 
				structuresList,
				magnet.forLeft,
				magnet.forMid,
				magnet.forRight,
				magnet.bools,
				magnet.solution,
				magnet.statements,
				dc
		);
	}
	
	/**
	 * Creates an array of StackableContainers from an array of 
	 * StackableContainers with the text from an array of Strings.
	 * 
	 * @param segments Array of Strings representing each code segment.
	 * 
	 * @return An array of StackableContainers. Will return null 
	 * if segments is null.
	 */
	private StackableContainer[] decodePremade(String[] segments) {
		if (segments == null) {
			return null;
		}
		
		StackableContainer[] preMadeList = new StackableContainer[segments.length]; //should never need this many
		
		for (int i = 0; i < segments.length; i++) {
			preMadeList[i] = new StackableContainer(segments[i], dc, Consts.STATEMENT);
		}
			
		return preMadeList;
	}
	
	/**
	 * Creates the main StackableContainer that holds all of 
	 * the other StackableContainers.
	 * 
	 * @param str The text for the main StackableContainer
	 * 
	 * @return The main StackableContainer
	 */
	private StackableContainer getMainContainer(String str) {
		// If stuff breaks, this also may be the culprit		
		return new StackableContainer(str,dc,Consts.MAIN);
	}
	
	/**
	 * Creates an array of StackableContainers from a String array
	 * 
	 * @param insideFunctions array of text for inner functions
	 * 
	 * @return An array of StackableContainers. Will return null if 
	 * insideFunctions is null.
	 */
	private StackableContainer[] buildFunctions(String[] insideFunctions) {
		if (insideFunctions == null) {
			return null;
		}
		
		StackableContainer[] insideFunctionsList = new StackableContainer[insideFunctions.length]; //should never need this many
		
		for (int i = 0; i < insideFunctions.length; i++) {
			insideFunctionsList[i] = new StackableContainer(insideFunctions[i], dc, Consts.INNER);
		}
		
		return insideFunctionsList;
	}
}
