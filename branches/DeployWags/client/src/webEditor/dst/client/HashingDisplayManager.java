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

public class HashingDisplayManager extends DisplayManager implements
		IsSerializable {
	private HashingProblem problem;

	// permanent widgets
	private Button resetButton;

	public HashingDisplayManager(DrawingArea canvas, AbsolutePanel panel,
			NodeCollection nc, HashingProblem problem) {
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
		addLeftButtonPanel();
		addMiddlePanel();
		addRightButtonPanel();
		addBackButton();
		addResetButton();
		addEvaluateButton();

		drawBoxes(problem.getTableSize());
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

	protected void addResetButton() {
		resetButton = new Button("Reset");
		resetButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				removeWidgetsFromPanel();

				for(int i = 0; i < getNodes().size(); i++)
				{
					problem.getGridPanel().remove(getNodes().get(i).getLabel());
				}

				nodeCollection.emptyNodes();
				insertNodesAndEdges();
			}
		});
		resetButton.setStyleName("control_button");
		leftButtonPanel.add(resetButton, 62, 2);
	}

	protected void addEvaluateButton() {
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
		else{
			for (int i = 0; i < splitNodes.length; i++) {
				Label label = new Label(splitNodes[i]);
				label.setStyleName("node");
				problem.getGridPanel().add(label, xPositions[i], yPositions[i]);     // WAS panel.add
				if (draggable){
					NodeDragController.getInstance().makeDraggable(label);
				}
				if (nodeType.equals(DSTConstants.NODE_DRAGGABLE)){
					nodeCollection.addNode(new Node(splitNodes[i], label));
				}
			}
		}
	}

	public void drawBoxes(int tableSize) {
		int YTOP = 115;
		int YBOTTOM = 165;
		int xStart = 5;
		int labelY = 230;
		for (int i = 0; i < tableSize; i++) {
			if(i==10){
				YTOP = 225;
				YBOTTOM = 275;
				xStart = 5;
				labelY = 330;
			}
			Line top = new Line(xStart, YTOP, (xStart + 50), YTOP);
			Line right = new Line((xStart + 50), YTOP, (xStart + 50), YBOTTOM);
			Line bottom = new Line(xStart, YBOTTOM, (xStart + 50), YBOTTOM);
			Line left = new Line(xStart, YTOP, xStart, YBOTTOM);
			Label label = new Label((i) + "");
			Proxy.getDST().add(label, xStart + 22, labelY);
			drawEdge(top);
			drawEdge(right);
			drawEdge(bottom);
			drawEdge(left);
			xStart += 60;
		}
		drawEdge(new Line(0,95,650,95));
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
		int startX = 5;
		String[] splitNodes = nodes.split(" ");
		int[] x = new int[splitNodes.length];
		for (int i = 0; i < splitNodes.length; i++) {
			if(i<10)
				x[i] = (55 * i) + startX;
			else
				x[i]= (55 * (i-10))+startX;
		}
		return x;
	}

	private int[] getSortYLocations(String nodes) {
		String[] splitNodes = nodes.split(" ");
		int[] y = new int[splitNodes.length];
		for (int i = 0; i < splitNodes.length; i++) {
			if(i<10)
				y[i] = 0;
			else
				y[i] = 43;
		}
		return y;
	}
}