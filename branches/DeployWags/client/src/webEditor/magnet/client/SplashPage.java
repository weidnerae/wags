package webEditor.magnet.client;

import webEditor.client.MagnetProblem;
import webEditor.client.Proxy;
import webEditor.client.view.Wags;

import java.util.Random;

import com.allen_sauer.gwt.dnd.client.PickupDragController;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

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
public class SplashPage extends AbsolutePanel {
	
	public static final String EMPTY_LABEL = "No Magnet Exercises Assigned!";
	static String[] structuresList = {"choose structure...","for","while","if","else if", "else"};
	VerticalPanel problemPane;
	Label banner;
	
	PickupDragController dc;
	
	/**
	 * Create a VerticalPanel to hold all the magnet problem buttons 
	 * and add a Title to it.
	 * 
	 * @param wags
	 */
	public SplashPage(Wags wags) {
		dc = new PickupDragController(RootPanel.get(), false);
		dc.setBehaviorDragProxy(true);
		
		problemPane = new VerticalPanel();
		problemPane.clear();
		problemPane.setSpacing(5);
		
		banner = new Label("Code Magnet Microlabs");
		banner.setStyleName("banner");
		problemPane.add(banner);
		
		add(problemPane);
		
		Proxy.getMagnetExercises(wags,problemPane);
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
				randomizeArray(magnet.forLeft),
				randomizeArray(magnet.forMid),
				randomizeArray(magnet.forRight),
				randomizeArray(magnet.bools),
				magnet.solution,
				magnet.statements,
				dc
		);
	}
	
	public static String[] randomizeArray(String[] arr){
		Random rand = new Random();
		int temp1;
		int temp2;
		
		for (int i = 0; i < 20; i++) {
			temp1 = rand.nextInt(arr.length);
			temp2 = rand.nextInt(arr.length);
			
			String sTemp1 = arr[temp1];
			arr[temp1] = arr[temp2];
			arr[temp2] = sTemp1;
		}
		
		return arr;
	}
	
	public StackableContainer[] decodePremade(String[] segments) {
		if (segments == null) {
			return null;
		}
		
		StackableContainer[] preMadeList = new StackableContainer[segments.length]; //should never need this many
		int counter = 0;
		
		while (counter < segments.length) {
			if (segments[counter].contains(Consts.TOP)) {
				preMadeList[counter] = new StackableContainer(segments[counter], dc);
			} else {
				preMadeList[counter] = new StackableContainer(
						segments[counter] + Consts.TOP 
										  + Consts.INSIDE 
										  + Consts.BOTTOM,
						dc,
						false
				);
			}
			
			counter++;
		}
			
		return preMadeList;
	}
	
	public StackableContainer getMainContainer(String str) {
		return new StackableContainer(
			str + " {<br /><span id=\"inside_of_block\">"
				+ Consts.TOP + Consts.INSIDE + Consts.BOTTOM
				+ "</span><br />}",
			dc,
			Consts.MAIN
		);
	}
	
	public StackableContainer[] buildFunctions(String[] insideFunctions) {
		if (insideFunctions == null) {
			return null;
		}
		
		StackableContainer[] insideFunctionsList = new StackableContainer[insideFunctions.length]; //should never need this many
		
		for (int i = 0; i < insideFunctions.length; i++) {
			insideFunctionsList[i] = new StackableContainer(insideFunctions[i], dc, Consts.NONDRAGGABLE);
		}
		
		return insideFunctionsList;
	}
	
	/**
	 * Format the problem buttons to be of style "problem",
	 * height of 50px, and width the same of the max
	 * width of other buttons
	 * 
	 * Start at index 2 for the loops because we have the banner 
	 * Label and can have a label saying that there are no exercises.
	 * If there are exercises, the second label is blank.
	 */
	private void formatButtons() {
		int count = problemPane.getWidgetCount();
		
		if (count <= 2) {
			return; // there are no problem buttons
		}
		
		int maxWidth = problemPane.getWidget(0).getOffsetWidth();
		
		// set style name and figure out max width
		for (int i = 2; i < count; i++) {
			ProblemButton b = ((ProblemButton) problemPane.getWidget(i));
			b.setStyleName("problem");
			int width = b.getOffsetWidth();
			maxWidth = (width > maxWidth) ? width : maxWidth;
		}
		
		// set width and height for everything
		for (int i = 2; i < count; i++) {
			ProblemButton b = ((ProblemButton) problemPane.getWidget(i));
			b.setWidth(maxWidth + "px");
			b.setHeight("50px");
		}
	}
	
	/**
	 * Whenever a widget is added to SplashPage (should 
	 * only ever be problemPane, which is a VerticalPanel),
	 * call formatButtons() in order to make sure the buttons
	 * look correct.
	 * 
	 * Need to do this because Wags.java's updateSplashPage() 
	 * removes the panel and then re-adds it.
	 */
	@Override
	public void add(Widget w) {
		super.add(w);
		formatButtons();
	}
}
