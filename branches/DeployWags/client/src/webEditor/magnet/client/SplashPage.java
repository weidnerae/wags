package webEditor.magnet.client;

import webEditor.client.MagnetProblem;
import webEditor.client.Proxy;
import webEditor.client.view.Wags;

import java.util.Random;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SplashPage extends AbsolutePanel {
	public static final String EMPTY_LABEL = "No Magnet Exercises Assigned!";
	static String[] structuresList = {"choose structure...","for","while","if","else if", "else"};
	final VerticalPanel problemPane = new VerticalPanel();
	
	PickupDragController dc = new PickupDragController(RootPanel.get(), false);
	
	String title;
	
	public SplashPage(Wags wags) {
		dc.setBehaviorDragProxy(true);
		add(problemPane);
		problemPane.clear();
		problemPane.setSpacing(5);
		Proxy.getMagnetExercises(wags,problemPane);
	}
	
	/**
	 * Makes the problem
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
		for(int i=0;i<20;i++){
			temp1 = rand.nextInt(arr.length);
			temp2 = rand.nextInt(arr.length);
			
			String sTemp1 = arr[temp1];
			arr[temp1] = arr[temp2];
			arr[temp2] = sTemp1;
		}
		return arr;
	}
	
	public String[] convertArray(JSONArray arr) {
		if (arr.toString().equals("[null]")) {
			return null;
		}
		
		String[] result = new String[arr.size()];
		
		for (int i = 0; i < arr.size(); i++) {
			result[i] = arr.get(i).isString().stringValue();
		}
		
		return result;
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
					preMadeList[counter] = new StackableContainer(segments[counter] + Consts.TOP + Consts.INSIDE + Consts.BOTTOM, dc, false);
				}
				
				counter++;
			}
			
		return preMadeList;
	}
	
	public StackableContainer getMainContainer(String str) {
		return new StackableContainer(str + " {<br /><span id=\"inside_of_block\">"
				+ Consts.TOP + Consts.INSIDE + Consts.BOTTOM + "</span><br />}", dc, Consts.MAIN);
	}
	
	public StackableContainer[] buildFunctions(String[] insideFunctions) {
		if (insideFunctions == null) {
			return null;
		}
		
		StackableContainer[] insideFunctionsList = new StackableContainer[insideFunctions.length]; //should never need this many
		int counter = 0;
		while (counter < insideFunctions.length) {
			insideFunctionsList[counter] = new StackableContainer(insideFunctions[counter], dc, Consts.NONDRAGGABLE);
			counter++;
		}
		
		return insideFunctionsList;
	}
	
	/**
	 * Format the problem buttons to be of style "problem",
	 * height of 50px, and width the same of the max
	 * width of other buttons
	 */
	private void formatButtons() {
		int count = problemPane.getWidgetCount();
		int maxWidth = 0;
		
		// Just return if it is a label saying there are no exercises
		if (count == 1) {
			Label l = ((Label) problemPane.getWidget(0));
			
			if (l.getText().equals(EMPTY_LABEL)) {
				return;
			}
		}
		
		// set style name and figure out max width
		for (int i = 0; i < count; i++) {
			ProblemButton b = ((ProblemButton) problemPane.getWidget(i));
			b.setStyleName("problem");
			int width = b.getOffsetWidth();
			maxWidth = (width > maxWidth) ? width : maxWidth;
		}
		
		// set width and height for everything
		for (int i = 0; i < count; i++) {
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
