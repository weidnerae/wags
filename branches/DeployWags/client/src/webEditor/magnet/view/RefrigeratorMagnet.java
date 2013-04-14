package webEditor.magnet.view;

import java.util.ArrayList;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

/**
 * 
 * Magnet Problem page. We should rename this.
 * 
 */
public class RefrigeratorMagnet extends AbsolutePanel {
	// screen variables
	static int SCREEN_WIDTH = Window.getClientWidth();
	static int SCREEN_HEIGHT = Window.getClientHeight();
	// the panels
	public TabPanel tabPanel = new TabPanel();
	private EditingPanelUi editingPanel;
	private ResultsPanelUi resultsPanel;
	public String problemType;
	public String solution;
	public String state;
	public int id;
	public PickupDragController dc;
	public StackableContainer mainFunction;
	public StackableContainer[] insideFunctions;
	public StackableContainer[] premadeFunctions;
	public int[] limits;
	public int numStatements;

	public RefrigeratorMagnet(int id, String title, String description,
			StackableContainer mainFunction,
			StackableContainer[] insideFunctions, String problemType,
			StackableContainer[] premadeSegments, String[] createdIDs,
			int numStatements, String[][] forLists, String[] booleanList,
			String solution, String[] premadeIDs, String limits,
			PickupDragController newDC, final String state) {
		
		dc = newDC;
		setHeight("99%");
		this.problemType = problemType;
		this.solution = solution;
		this.id = id;
		this.state = state;
		this.mainFunction = mainFunction;
		this.insideFunctions = insideFunctions;
		this.premadeFunctions = premadeSegments;
		this.numStatements = numStatements;
		
		if (problemType.equals(Consts.ADVANCED_PROBLEM)) {
			String[] sLimits = limits.split(",");
			this.limits = new int[sLimits.length];
			int k = 0;
			for (String limit : sLimits) {
				this.limits[k++] = Integer.parseInt(limit);
			}
		}

		add(tabPanel);
		tabPanel.setSize("100%", "100%");
		int tabPanelHeight = tabPanel.getOffsetHeight();
		tabPanelHeight = tabPanel.getOffsetHeight();
		editingPanel = new EditingPanelUi(this, tabPanelHeight, title,
				description, mainFunction, insideFunctions, problemType,
				premadeSegments, createdIDs.length, numStatements,
				forLists, booleanList,
				this.limits, solution, premadeIDs, dc);
		tabPanel.add(editingPanel, "Editing Mode", false);
		tabPanel.selectTab(0);

		resultsPanel = new ResultsPanelUi(tabPanelHeight);
		tabPanel.add(resultsPanel, "Results", false);

		editingPanel.start();

		if (state.length() != 0) {
			Timer timer = new Timer() {
				@Override
				public void run() {
					decode2("0", state);
				}
			};

			timer.schedule(1);
		}
	}

	public String getProblemType() {
		return problemType;
	}

	public String getSolution() {
		return solution;
	}

	public int getID() {
		return id;
	}

	public String getState() {
		return state;
	}

	public void decode(String parentID, String state) {
		int startIndex = state.indexOf("[");
		int endIndex = state.indexOf("]");
		if (state.indexOf("[", startIndex + 1) != -1 && state.indexOf("[", startIndex + 1) < endIndex) {
			/* another open brace before close brace */
			addMagnetsByID(state.substring(startIndex + 1, state.indexOf("[", startIndex + 1)), parentID);
			
			decode2(state.substring(startIndex + 1, 
					state.indexOf("[", startIndex + 1)),
					state.substring(state.indexOf("[", startIndex + 1)));
		} else {
			addMagnetsByID(state.substring(startIndex + 1, endIndex), parentID);
			
			if (endIndex + 1 < state.length() && state.indexOf('[', endIndex) != -1) {
				decode(parentID, state.substring(state.indexOf('[', endIndex)));
			}
		}
	}

	public void decode2(String parentID, String state) {
		int[][] segments = findSegments(state);

		for (int i = 0; i < segments[0].length; i++) {
			decode(parentID, state.substring(segments[0][i], segments[1][i] + 1));
		}
	}

	public int[][] findSegments(String problemState) {
		ArrayList<Integer> startList = new ArrayList<Integer>();
		ArrayList<Integer> endList = new ArrayList<Integer>();

		int bCount = 0;
		for (int i = 0; i < problemState.length(); i++) {
			if (problemState.charAt(i) == '[') {
				if (bCount == 0) {
					startList.add(i);
				}
				
				bCount++;
			} else if (problemState.charAt(i) == ']') {
				bCount--;
				
				if (bCount == 0) {
					endList.add(i);
				}
			}
		}

		int[] startArray = new int[startList.size()];
		int[] endArray = new int[endList.size()];

		for (int i = 0; i < startList.size(); i++) {
			startArray[i] = startList.get(i);
		}
		
		for (int i = 0; i < endList.size(); i++) {
			endArray[i] = endList.get(i);
		}

		return new int[][] { startArray, endArray };
	}

	public void addMagnetsByID(String childID, String parentID) {
		if (isInteger(childID) && isInteger(parentID)) {
			boolean premadeParent = false;
			int parentIndex = -1;
			int childIndex = -1;
			
			for (int i = 0; i < premadeFunctions.length; i++) {
				if (premadeFunctions[i].getID().equals(parentID)) {
					premadeParent = true;
					parentIndex = i;
				}
			}
			
			for (int k = 0; k < insideFunctions.length; k++) {
				if (insideFunctions[k].getID().equals(parentID)) {
					parentIndex = k;
				}
			}
			
			for (int j = 0; j < premadeFunctions.length; j++) {
				if (premadeFunctions[j].getID().equals(childID)) {
					childIndex = j;
				}
			}

			if (parentID.equals(mainFunction.getID()) && childIndex != -1) {
				mainFunction.addInsideContainer(premadeFunctions[childIndex]);
			} else if (childIndex != -1 && parentIndex != -1) {
				if (premadeParent) {
					if (!premadeFunctions[parentIndex].hasChild(premadeFunctions[childIndex].getID())) {
						premadeFunctions[parentIndex].addInsideContainer(premadeFunctions[childIndex]);
					}
				} else {
					if (!insideFunctions[parentIndex].hasChild(premadeFunctions[childIndex].getID())) {
						insideFunctions[parentIndex].addInsideContainer(premadeFunctions[childIndex]);
					}
				}

			}
		}

	}

	public void resetProblem() {
		editingPanel.resetProblem();
	}

	private boolean isInteger(String s) {
	    try {
	        Integer.parseInt(s);
	        return true;
	    } catch(NumberFormatException e) {
	        return false;
	    }
	}
}
