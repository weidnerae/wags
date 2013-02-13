package webEditor.dst.client;

import java.util.ArrayList;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Line;

import webEditor.client.Proxy;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class QuickSortDisplayManager extends DisplayManager implements
		IsSerializable {
	private QuickSortProblem problem;

	public QuickSortDisplayManager(DrawingArea canvas, AbsolutePanel panel,
			NodeCollection nc, QuickSortProblem problem) {
		System.out.println(problem.getName());
		this.panel = panel;
		this.canvas = canvas;
		this.nodeCollection = nc;
		this.problem = problem;
		super.problem = problem;
		this.itemsInPanel = new ArrayList<Widget>();
	}

	public void displayProblem() {
		addProblemTextArea();
		addCounterPanel();
		addLeftButtonPanel();
		addMiddlePanel();
		addRightButtonPanel();
		addBackButton();
		addResetButton();
		addEvaluateButton();

		drawBoxes();

		insertNodesAndEdges();
	}

	private void insertNodesAndEdges() {
		if (problem.getInsertMethod().equals(DSTConstants.INSERT_METHOD_VALUE)) {
			insertNodesByValue(problem.getNodes(), problem.getNodeType());
		} else {
			insertNodesByValueAndLocation(problem.getNodes(),
					problem.getXPositions(), problem.getYPositions(),
					problem.getNodesDraggable(), problem.getNodeType());
		}
	}

	private boolean showingSubMess;

	protected void addProblemTextArea() {
		TextArea t = new TextArea();
		t.setStyleName("problem_statement");
		t.setPixelSize(400, 90);
		t.setReadOnly(true);
		t.setText(problem.getProblemText());
		Proxy.getDST().add(t, 2, 5);
	}

	protected void addResetButton() {
		resetButton = new Button("Reset");
		resetButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				removeWidgetsFromPanel();

				for (int i = 0; i < getNodes().size(); i++) {
					panel.remove(getNodes().get(i).getLabel());
				}

				nodeCollection.emptyNodes();
				insertNodesAndEdges();
			}
		});
		resetButton.setStyleName("control_button");
		leftButtonPanel.add(resetButton, 62, 2);
	}

	private void addEvaluateButton() {
		evaluateButton = new Button("Evaluate");
		evaluateButton.setWidth("124px");
		evaluateButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String evalResult = problem.getEval().evaluate(
						problem.getName(), problem.getArguments(), getNodes(),
						getEdges());
				
				if (showingSubMess == true) {
					Proxy.getDST().remove(submitText);
					Proxy.getDST().remove(submitOkButton);
				}

				if (evalResult.equals(""))
					return; // used with the traversal problems with help on, if
							// it was empty string
							// then the user made a correct click and we don't
							// need to save or display
							// anything
				submitText.setText(evalResult);
				addToPanel(submitText, DSTConstants.SUBMIT_X,
						DSTConstants.SUBMIT_MESS_Y);
				int yOffset = DSTConstants.SUBMIT_MESS_Y
						+ submitText.getOffsetHeight() + 2;
				addToPanel(submitOkButton, DSTConstants.SUBMIT_X, yOffset);
				showingSubMess = true;
			}
		});
		showingSubMess = false;
		evaluateButton.setStyleName("control_button");
		rightButtonPanel.add(evaluateButton, 257, 2);

		submitText = new TextArea();
		submitText.setCharacterWidth(30);
		submitText.setReadOnly(true);
		submitText.setVisibleLines(5);
		submitOkButton = new Button("Ok");
		submitOkButton.setStyleName("control_button");
		submitOkButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Proxy.getDST().remove(submitText);
				Proxy.getDST().remove(submitOkButton);
				showingSubMess = false;
			}
		});
	}

	public void insertNodesByNumber(int numNodes) {
		for (int i = 0; i < numNodes; i++) {
			Label label = new Label(((char) ('A' + i)) + "");
			label.setStyleName("node");
			panel.add(label, 5, 150 + (50 * i));
			NodeDragController.getInstance().makeDraggable(label);
			nodeCollection.addNode(new Node(("" + (char) ('A' + i)), label));
		}
	}

	public void insertNodesByValue(String nodes, String nodeType) {
		insertNodesByValueAndLocation(nodes, getSortXLocations(nodes),
				getSortYLocations(nodes), true, nodeType);
	}

	public void insertNodesByValueAndLocation(String nodes, int[] xPositions,
			int[] yPositions, boolean draggable, String nodeType) {
		String[] splitNodes = nodes.split(" ");
		if (splitNodes.length != xPositions.length
				|| splitNodes.length != yPositions.length)
			throw new NullPointerException(); // need to find right exception
		else {
			nodes = problem.getArguments()[0];
			splitNodes = nodes.split(" ");
			xPositions = getSortXLocations(nodes);
			yPositions = getSortYLocations(nodes);
			for (int i = 0; i < splitNodes.length; i++) {
				Label label = new Label(splitNodes[i]);
				label.setStyleName("node");
				panel.add(label, xPositions[i], yPositions[i]);
				if (i <= splitNodes.length
						- ((Evaluation_Quicksort) problem.getEval())
						.getCurrentStep()) {
					NodeDragController.getInstance().makeDraggable(label);
				} 
				if (i == ((Evaluation_Quicksort) problem.getEval()).
						getInitialPivot(nodes)) {
					label.setStyleName("immobilized_node");
				}
				if (nodeType.equals(DSTConstants.NODE_DRAGGABLE))
					nodeCollection.addNode(new Node(splitNodes[i], label));
			}
		}
	}

	public void drawBoxes() {
		final int YTOP = 20;
		final int YBOTTOM = 70;
		int xStart = 10;
		for (int i = 0; i < problem.getNodes().split(" ").length; i++) {
			Line top = new Line(xStart, YTOP, (xStart + 50), YTOP);
			Line right = new Line((xStart + 50), YTOP, (xStart + 50), YBOTTOM);
			Line bottom = new Line(xStart, YBOTTOM, (xStart + 50), YBOTTOM);
			Line left = new Line(xStart, YTOP, xStart, YBOTTOM);
			Label label = new Label((i+1) + "");
			Proxy.getDST().add(label, xStart + 25, 134);
			drawEdge(top);
			drawEdge(right);
			drawEdge(bottom);
			drawEdge(left);
			xStart += 75;
		}
	}

	private void addCounterPanel() {
		TextArea cp = new TextArea();
		cp.setStyleName("problem_statement");
		cp.setPixelSize(195, 90);
		cp.setReadOnly(true);
		cp.setText("Current Pass: 1");
		Proxy.getDST().add(cp, 407, 5);

	}

	public ArrayList<Node> getNodes() {
		return nodeCollection.getNodes();
	}

	public ArrayList<EdgeParent> getEdges() {
		return null;
	}

	public void makeNodesDraggable() {
		nodeCollection.makeNodesDraggable(NodeDragController.getInstance());
	}

	public void makeNodesNotDraggable() {
		nodeCollection.makeNodesNotDraggable(NodeDragController.getInstance());
	}

	public void resetNodeStyles() {
		nodeCollection.resetNodeStyles(problem.getNodeType());
	}

	private int[] getSortXLocations(String nodes) {
		int startX = 15;
		String[] splitNodes = nodes.split(" ");
		int[] x = new int[splitNodes.length];
		for (int i = 0; i < splitNodes.length; i++) {
			x[i] = (75 * i) + startX;
		}
		return x;
	}

	private int[] getSortYLocations(String nodes) {
		String[] splitNodes = nodes.split(" ");
		int[] y = new int[splitNodes.length];
		for (int i = 0; i < splitNodes.length; i++) {
			y[i] = 25;
		}
		return y;
	}
}