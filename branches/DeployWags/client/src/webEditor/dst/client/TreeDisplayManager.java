package webEditor.dst.client;

import java.util.ArrayList;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Line;

import webEditor.client.Proxy;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class TreeDisplayManager extends DisplayManager implements
		IsSerializable {
	private AbsolutePanel panel;
	private DrawingArea canvas;
	private NodeCollection nodeCollection;
	private EdgeCollection edgeCollection;
	private ArrayList<Widget> itemsInPanel;
	private TreeProblem problem;
	private boolean addingEdge;
	private boolean removingEdge;
	private TraversalContainer cont;

	// permanent widgets
	private Button resetButton;
	private Button addEdgeButton;
	private Button removeEdgeButton;
	private Label edgeAdditionIns;
	private AbsolutePanel edgeAdditionInsPanel;
	private Button evaluateButton;
	private TextArea submitText;
	private AbsolutePanel leftButtonPanel;
	private AbsolutePanel middlePanel;
	private AbsolutePanel rightButtonPanel;
	private Button submitOkButton;

	public TreeDisplayManager(DrawingArea canvas, AbsolutePanel panel,
			NodeCollection nc, EdgeCollection ec, TreeProblem problem) {
		System.out.println(problem.getName());
		this.panel = panel;
		this.canvas = canvas;
		this.nodeCollection = nc;
		this.edgeCollection = ec;
		this.problem = problem;
		this.addingEdge = false;
		this.removingEdge = false;
		this.itemsInPanel = new ArrayList<Widget>();
	}

	public void displayProblem() {
		cont = new TraversalContainer(this);
		addProblemTextArea();
		addLeftButtonPanel();
		addMiddlePanel();
		addRightButtonPanel();
		addBackButton();
		addResetButton();
		addEvaluateButton();

		if (problem.getEdgesRemovable()) {
			addAddEdgeButton();
			addRemoveEdgeButton();
		}

		insertNodesAndEdges();
	}

	private void insertNodesAndEdges() {
		cont = new TraversalContainer(this); // for reset of traversal problems
		if (problem.getInsertMethod().equals(DSTConstants.INSERT_METHOD_VALUE)) {
			insertNodesByValue(problem.getNodes(), problem.getNodeType());
		} else {
			insertNodesByValueAndLocation(problem.getNodes(),
					problem.getXPositions(), problem.getYPositions(),
					problem.getNodesDraggable(), problem.getNodeType());
		}

		if (problem.getEdges().length > 0)
			edgeCollection.insertEdges(problem.getEdges(), getNodes());
	}

	// Add Edge Button
	private HandlerRegistration edgeEventHandler;
	private HandlerRegistration edgeCancelEventHandler;
	private HandlerRegistration removeEdgeEventHandler;
	private HandlerRegistration removeEdgeCancelEventHandler;
	private ClickHandler removeEdgeClickHandler;
	private ClickHandler removeEdgeCancelClickHandler;
	private ClickHandler edgeClickHandler;
	private ClickHandler edgeCancelClickHandler;
	private boolean showingSubMess;

	private class AddEdgeNodeClickHandler implements DoubleClickHandler {
		public void onDoubleClick(DoubleClickEvent event) {
			if (edgeCollection.getNumNodesSelected() == 0) {
				removeWidgetsFromPanel();
				resetRemoveEdgeButton();
				resetNodeStyles();
				resetEdgeStyles();
				makeNodesNotDraggable();
				addEdgeStart();
				edgeCollection.selectFirstNodeOfEdge((Label) event.getSource());
				edgeCollection.addNextEdge();
				setEdgeNodeSelectionInstructions(edgeCollection.getSecondInstructions()); 

			}
		}
	}

	private class AddEdgeClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			removeWidgetsFromPanel();
			resetRemoveEdgeButton();
			resetNodeStyles();
			resetEdgeStyles();
			makeNodesNotDraggable();
			addEdgeStart();
			edgeCollection.addNextEdge();
		}
	}

	private class AddEdgeCancelClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			addEdgeCancel();
			removeEdgeCancelEventHandler.removeHandler();
			removeEdgeButton.addClickHandler(removeEdgeClickHandler);
		}
	}

	private class RemoveEdgeClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			if (getEdges().size() > 0) {
				addEdgeCancel();
				removeWidgetsFromPanel();

				removingEdge = true;
				removeEdgeButton.setText("Cancel");
				removeEdgeEventHandler.removeHandler();
				removeEdgeCancelEventHandler = removeEdgeButton
						.addClickHandler(removeEdgeCancelClickHandler);

				Label l = new Label("Click Edge to Remove");
				l.setStyleName("edge_remove");
				addToPanel(l, 445, DSTConstants.EDGE_PROMPT_Y);
			}
		}
	}

	private class RemoveEdgeCancelClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			resetRemoveEdgeButton();
			removeWidgetsFromPanel();
			addEdgeCancel();
		}
	}

	private void resetEdgeButton() {
		addEdgeButton.setText("Add Edge");
		edgeCancelEventHandler.removeHandler();
		edgeEventHandler = addEdgeButton.addClickHandler(edgeClickHandler);
	}

	public void resetRemoveEdgeButton() {
		if (removingEdge) {
			removeEdgeButton.setText("Remove Edge");
			removeEdgeCancelEventHandler.removeHandler();
			removeEdgeEventHandler = removeEdgeButton
					.addClickHandler(removeEdgeClickHandler);
			removingEdge = false;
		}
	}

	public void addEdgeStart() {
		addEdgeButton.setText("Cancel");
		edgeEventHandler.removeHandler();
		edgeCancelEventHandler = addEdgeButton
				.addClickHandler(edgeCancelClickHandler);
		addingEdge = true;
		RootPanel.get().add(edgeAdditionInsPanel, 346, 131);
	}

	public void addEdgeCancel() {
		if (addingEdge) {
			makeNodesDraggable();
			edgeCollection.clearEdgeNodeSelections();
			resetEdgeButton();
			resetEdgeStyles();
			resetNodeStyles();
			setEdgeNodeSelectionInstructions("");
			addingEdge = false;
		}
	}

	public void removeEdgeCancel() {
		if (removingEdge) {
			resetRemoveEdgeButton();
			removingEdge = false;
		}
	}

	public void setEdgeNodeSelectionInstructions(String ins) {
		if (ins.equals("")) {
			RootPanel.get().remove(edgeAdditionInsPanel);
		}
		edgeAdditionIns.setText(ins);
	}

	// End Add Edge Button

	private void addProblemTextArea() {
		TextArea t = new TextArea();
		t.setStyleName("problem_statement");
		t.setPixelSize(598, 90);
		t.setReadOnly(true);
		t.setText(problem.getProblemText());
		RootPanel.get().add(t, 2, 5);
	}

	private void addLeftButtonPanel() {
		leftButtonPanel = new AbsolutePanel();
		leftButtonPanel.setPixelSize(130, 30);
		leftButtonPanel.setStyleName("left_panel");
		RootPanel.get().add(leftButtonPanel, 2, 100);
	}

	private void addMiddlePanel() {
		middlePanel = new AbsolutePanel();
		middlePanel.setPixelSize(214, 30);
		middlePanel.setStyleName("middle_panel");
		RootPanel.get().add(middlePanel, 132, 100);
	}

	private void addRightButtonPanel() {
		rightButtonPanel = new AbsolutePanel();
		rightButtonPanel.setPixelSize(383, 30);           
		rightButtonPanel.setStyleName("right_panel");      
		RootPanel.get().add(rightButtonPanel, 221, 100);   
	}

	private void addBackButton() {
		Button backButton = new Button("Back");
		backButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Proxy.buildDST();
			}
		});
		backButton.setStyleName("control_button");
		leftButtonPanel.add(backButton, 2, 2);
	}

	protected void addResetButton() {
		resetButton = new Button("Reset");
		resetButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				removeWidgetsFromPanel();
				addEdgeCancel();
				resetRemoveEdgeButton();

				for (int i = 0; i < getNodes().size(); i++) {
					panel.remove(getNodes().get(i).getLabel());
				}

				for (int i = 0; i < getEdges().size(); i++) {
					canvas.remove(getEdges().get(i).getLine());
				}

				edgeCollection.emptyEdges();
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
				setEdgeParentAndChildren();
				String evalResult = problem.getEval().evaluate(
						problem.getName(), problem.getArguments(), getNodes(),
						getEdges());

				if (showingSubMess == true) {
					RootPanel.get().remove(submitText);
					RootPanel.get().remove(submitOkButton);
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
				RootPanel.get().remove(submitText);
				RootPanel.get().remove(submitOkButton);
				showingSubMess = false;
			}
		});
	}

	private void addAddEdgeButton() {
		addEdgeButton = new Button("Add Edge");
		addEdgeButton.setWidth("124px");
		edgeClickHandler = new AddEdgeClickHandler();
		edgeCancelClickHandler = new AddEdgeCancelClickHandler();
		edgeEventHandler = addEdgeButton.addClickHandler(edgeClickHandler);
		edgeAdditionIns = new Label("");
		edgeAdditionIns.setStyleName("edge_instructions");
		edgeAdditionInsPanel = new AbsolutePanel();
		edgeAdditionInsPanel.setPixelSize(256, 20);
		edgeAdditionInsPanel.setStyleName("edge_addition_panel");
		edgeAdditionInsPanel.add(edgeAdditionIns);
		addEdgeButton.setStyleName("control_button");
		rightButtonPanel.add(addEdgeButton, 3, 2);
	}

	private void addRemoveEdgeButton() {
		removeEdgeButton = new Button("Remove Edge");
		removeEdgeButton.setWidth("130px");
		removeEdgeButton.setStyleName("control_button");
		removeEdgeClickHandler = new RemoveEdgeClickHandler();
		removeEdgeCancelClickHandler = new RemoveEdgeCancelClickHandler();
		removeEdgeEventHandler = removeEdgeButton
				.addClickHandler(removeEdgeClickHandler);
		rightButtonPanel.add(removeEdgeButton, 128, 2);
	}

	public void insertNodesByNumber(int numNodes) {
		for (int i = 0; i < numNodes; i++) {
			Label label = new Label(((char) ('A' + i)) + "");
			label.addDoubleClickHandler(new AddEdgeNodeClickHandler());
			label.setStyleName("node");
			panel.add(label, 5, 150 + (50 * i));
			NodeDragController.getInstance().makeDraggable(label);
			nodeCollection.addNode(new Node(("" + (char) ('A' + i)), label));
		}
	}

	public void insertNodesByValue(String nodes, String nodeType) {
		String[] splitNodes = nodes.split(" ");

		if (nodeType.equals(DSTConstants.NODE_STRING_DRAGGABLE)) {

			for (int i = 0; i < splitNodes.length; i++) {
				Label label = new Label(splitNodes[i]);
				label.addDoubleClickHandler(new AddEdgeNodeClickHandler());
				label.setStyleName("string_node");
				label.getElement().getStyle()
						.setTop(10 + (45 * i), Style.Unit.PX);
				label.addDoubleClickHandler(new AddEdgeNodeClickHandler());
				panel.add(label);
				NodeDragController.getInstance().makeDraggable(label);
				nodeCollection.addNode(new Node(splitNodes[i], label));
			}
		} else {
			for (int i = 0; i < splitNodes.length; i++) {
				Label label = new Label(splitNodes[i]);
				label.setStyleName("node");
				label.getElement().getStyle()
						.setTop(10 + (45 * i), Style.Unit.PX);
				label.addDoubleClickHandler(new AddEdgeNodeClickHandler());
				panel.add(label);
				NodeDragController.getInstance().makeDraggable(label);
				nodeCollection.addNode(new Node(splitNodes[i], label));
			}
		}
	}

	public void insertNodesByValueAndLocation(String nodes, int[] xPositions,
			int[] yPositions, boolean draggable, String nodeType) {
		String[] splitNodes = nodes.split(" ");
		if (splitNodes.length != xPositions.length
				|| splitNodes.length != yPositions.length)
			throw new NullPointerException(); // need to find right exception
		else if (nodeType.equals(DSTConstants.NODE_STRING_DRAGGABLE)) {

			for (int i = 0; i < splitNodes.length; i++) {
				Label label = new Label(splitNodes[i]);
				label.addDoubleClickHandler(new AddEdgeNodeClickHandler());
				label.setStyleName("string_node");
				panel.add(label, xPositions[i], yPositions[i]);
				NodeDragController.getInstance().makeDraggable(label);
				nodeCollection.addNode(new Node(splitNodes[i], label));
			}
		} else {
			for (int i = 0; i < splitNodes.length; i++) {
				Label label = new Label(splitNodes[i]);
				label.addDoubleClickHandler(new AddEdgeNodeClickHandler());
				label.setStyleName("node");
				panel.add(label, xPositions[i], yPositions[i]);
				if (draggable)
					NodeDragController.getInstance().makeDraggable(label);
				if (nodeType.equals(DSTConstants.NODE_DRAGGABLE))
					nodeCollection.addNode(new Node(splitNodes[i], label));
				else if (nodeType
						.equals(DSTConstants.NODE_CLICKABLE_FORCE_EVAL))
					nodeCollection.addNode(new NodeClickable(splitNodes[i],
							label, cont, true));
				else
					nodeCollection.addNode(new NodeClickable(splitNodes[i],
							label, cont, false));
			}
		}
	}

	public ArrayList<Node> getNodes() {
		return nodeCollection.getNodes();
	}

	public ArrayList<EdgeParent> getEdges() {
		return edgeCollection.getEdges();
	}

	public void addToPanel(Widget w, int left, int top) {
		itemsInPanel.add(w);
		RootPanel.get().add(w, left, top);
	}

	public void removeWidgetsFromPanel() {
		for (int i = 0; i < itemsInPanel.size(); i++) {
			RootPanel.get().remove(itemsInPanel.get(i));
		}
	}

	public void drawEdge(Line line) {
		canvas.add(line);
	}

	public void removeEdge(Line line) {
		canvas.remove(line);
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

	public void resetEdgeStyles() {
		edgeCollection.resetEdgeColor();
	}

	public void setEdgeParentAndChildren() {
		edgeCollection.setParentAndChildNodes();
	}

	public void forceEvaluation() {
		evaluateButton.click();
	}
	public void addDiagLabel(String s){
		RootPanel.get().add(new Label(s), 250,250);
	}
}