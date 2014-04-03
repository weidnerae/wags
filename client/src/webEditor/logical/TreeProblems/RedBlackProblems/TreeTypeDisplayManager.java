package webEditor.logical.TreeProblems.RedBlackProblems;

import java.util.ArrayList;

import org.vaadin.gwtgraphics.client.DrawingArea;

import webEditor.Proxy;
import webEditor.logical.DSTConstants;
import webEditor.logical.DisplayManager;
import webEditor.logical.EdgeCollection;
import webEditor.logical.EdgeParent;
import webEditor.logical.Node;
import webEditor.logical.NodeClickable;
import webEditor.logical.NodeCollection;
import webEditor.logical.NodeDragController;
import webEditor.logical.TraversalContainer;

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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class TreeTypeDisplayManager extends DisplayManager implements
		IsSerializable {
	protected EdgeCollection edgeCollection;
	protected ArrayList<Widget> weightsInPanel;
	protected TreeTypeProblem problem;
	protected boolean addingEdge;
	protected boolean removingEdge;
	protected boolean coloring;

	// permanent widgets
	protected Button addEdgeButton;
	protected Button removeEdgeButton;
	protected Button colorButton;
	protected Label edgeAdditionIns;
	protected AbsolutePanel edgeAdditionInsPanel;

	public TreeTypeDisplayManager(DrawingArea canvas, AbsolutePanel panel,
			NodeCollection nc, EdgeCollection ec, TreeTypeProblem problem) {
		System.out.println(problem.getName());
		this.panel = panel;
		this.canvas = canvas;
		this.nodeCollection = nc;
		this.edgeCollection = ec;
		this.problem = problem;
		super.problem = problem;
		this.addingEdge = false;
		this.removingEdge = false;
		this.itemsInPanel = new ArrayList<Widget>();
		this.weightsInPanel = new ArrayList<Widget>();
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

	protected void insertNodesAndEdges() {
		cont = new TraversalContainer(this); // for reset of traversal problems
		if (problem.getInsertMethod().equals(DSTConstants.INSERT_METHOD_VALUE)) {
			insertNodesByValue(problem.getNodes(), problem.getNodeType());
		}
		else if(problem.getInsertMethod().equals(DSTConstants.INSERT_METHOD_VALUE_LOCATION_COLOR)){
			insertNodesByValueLocationColor(problem.getNodes(),
					problem.getXPositions(), problem.getYPositions(),
					problem.getNodesDraggable(), problem.getNodeType(), problem.getArguments());
		}else {
			insertNodesByValueAndLocation(problem.getNodes(),
					problem.getXPositions(), problem.getYPositions(),
					problem.getNodesDraggable(), problem.getNodeType());
		}

		if (problem.getEdges().length > 0){
			if(isMST()){
				edgeCollection.insertGraphEdges(problem.getEdges(), getNodes());
			}
			else{
				edgeCollection.insertEdges(problem.getEdges(), getNodes());
			}
		}
	}


	// Add Edge Buttons and Color Button
	protected HandlerRegistration edgeEventHandler;
	protected HandlerRegistration edgeCancelEventHandler;
	protected HandlerRegistration removeEdgeEventHandler;
	protected HandlerRegistration removeEdgeCancelEventHandler;
	protected ClickHandler removeEdgeClickHandler;
	protected ClickHandler removeEdgeCancelClickHandler;
	protected ClickHandler edgeClickHandler;
	protected ClickHandler edgeCancelClickHandler;
	protected boolean showingSubMess;

	private class AddEdgeNodeClickHandler implements DoubleClickHandler {
		@Override
		public void onDoubleClick(DoubleClickEvent event) {
			if (edgeCollection.getNumNodesSelected() == 0) {
				removeWidgetsFromPanel();
				resetRemoveEdgeButton();
				if (!problem.getNodeType().equals(DSTConstants.NODE_RED_BLACK)){
					resetNodeStyles();
				}
				resetEdgeStyles();
				makeNodesNotDraggable();
				addEdgeStart();
				edgeCollection.selectFirstNodeOfEdge((Label) event.getSource());
				edgeCollection.addNextEdge();
				setEdgeNodeSelectionInstructions(edgeCollection.getSecondInstructions());
			}
		}
	}

	protected class AddEdgeClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			removeWidgetsFromPanel();
			resetRemoveEdgeButton();
			if (!problem.getNodeType().equals(DSTConstants.NODE_RED_BLACK)){
				resetNodeStyles();
			}
			resetEdgeStyles();
			makeNodesNotDraggable();
			addEdgeStart();
			edgeCollection.addNextEdge();
		}
	}

	// stays
	protected class AddEdgeCancelClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			addEdgeCancel();
			removeEdgeCancelEventHandler.removeHandler();
			removeEdgeButton.addClickHandler(removeEdgeClickHandler);
		}
	}
    
	// stays
	protected class RemoveEdgeClickHandler implements ClickHandler {
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

	protected class RemoveEdgeCancelClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			resetRemoveEdgeButton();
			removeWidgetsFromPanel();
			addEdgeCancel();
		}
	}

	private void resetEdgeButton() {
		addEdgeButton.setText("Add Edge");
		if (!problem.getNodeType().equals(DSTConstants.NODE_RED_BLACK)){
			resetNodeStyles();
		}else{
			removeSelectedState();
		}
		edgeCancelEventHandler.removeHandler();
		edgeEventHandler = addEdgeButton.addClickHandler(edgeClickHandler);
	}

	// stays
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
		Proxy.getDST().add(edgeAdditionInsPanel, 346, 131);
	}
	
	// stays
	public void addEdgeCancel() {
		if (addingEdge) {
			makeNodesDraggable();
			edgeCollection.clearEdgeNodeSelections();
			resetEdgeButton();
			resetEdgeStyles();
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

	// stays
	public void setEdgeNodeSelectionInstructions(String ins) {
		if (ins.equals("")) {
			Proxy.getDST().remove(edgeAdditionInsPanel);
		}
		edgeAdditionIns.setText(ins);
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
				if(isMST()){
					edgeCollection.clearGraphNodeCollection();
				}
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
				setEdgeParentAndChildren();
				String evalResult;
				if(isMST()){
					evalResult = problem.getEval().evaluate(
							problem.getName(), problem.getArguments(), edgeCollection.getGraphNodeCollection().getNodes(),
							getEdges());
				}
				else{
					evalResult = problem.getEval().evaluate(
						problem.getName(), problem.getArguments(), getNodes(),
						getEdges());
				}

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

	protected void addAddEdgeButton() {
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
	
	protected void addRemoveEdgeButton() {
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
				label.setStyleName("node");
				panel.add(label, xPositions[i], yPositions[i]);
				if (draggable){
					NodeDragController.getInstance().makeDraggable(label);
				}
				if (nodeType.equals(DSTConstants.NODE_DRAGGABLE)){
					nodeCollection.addNode(new Node(splitNodes[i], label));
					label.addDoubleClickHandler(new AddEdgeNodeClickHandler());
				}
				else if (nodeType
						.equals(DSTConstants.NODE_CLICKABLE_FORCE_EVAL))
					nodeCollection.addNode(new NodeClickable(splitNodes[i],
							label, cont, true));
				else if (nodeType.equals(DSTConstants.NODE_RED_BLACK)){
					label.setStyleName("red_node");
					nodeCollection.addNode(new NodeRedBlack(splitNodes[i], label, cont, false));
					
				}
				else
					nodeCollection.addNode(new NodeClickable(splitNodes[i],
							label, cont, false));
			}
		}
	}
	
	/**
	 * 
	 * @param nodes	The list of nodes to to be added
	 * @param xPositions
	 * @param yPositions
	 * @param draggable
	 * @param nodeType
	 * @param arguments
	 */
	public void insertNodesByValueLocationColor(String nodes, int[] xPositions,
			int[] yPositions, boolean draggable, String nodeType, String[] arguments) {
		String[] splitNodes = nodes.split(" ");
		if (splitNodes.length != xPositions.length
				|| splitNodes.length != yPositions.length)
			throw new NullPointerException(); // need to find right exception
		
		for (int i = 0; i < splitNodes.length; i++) {
			Label label = new Label(splitNodes[i]);
			label.setStyleName("node");
			panel.add(label, xPositions[i], yPositions[i]);
			if (draggable){
				NodeDragController.getInstance().makeDraggable(label);
			}
			if (nodeType.equals(DSTConstants.NODE_RED_BLACK)){
				if (arguments[3].contains(splitNodes[i])){
					label.setStyleName("black_node");
				}else
					label.setStyleName("red_node");
				
				nodeCollection.addNode(new NodeRedBlack(splitNodes[i], label, cont, false));
				
			}
			else
				nodeCollection.addNode(new NodeClickable(splitNodes[i],
						label, cont, false));
		}
	}

	public ArrayList<Node> getNodes() {
		return nodeCollection.getNodes();
	}

	public ArrayList<EdgeParent> getEdges() {
		return edgeCollection.getEdges();
	}
	
	public void addWeightLabel(Widget w, int left, int top){
		weightsInPanel.add(w);
		Proxy.getDST().add(w, left, top);
	}
	
	public void removeWeightLabelsFromPanel() {
		for (int i = 0; i < weightsInPanel.size(); i++) {
			Proxy.getDST().remove(weightsInPanel.get(i));
		}
	}

	public void makeNodesDraggable() {
		nodeCollection.makeNodesDraggable(NodeDragController.getInstance());
	}

	public void makeNodesNotDraggable() {
		nodeCollection.makeNodesNotDraggable(NodeDragController.getInstance());
	}

	public void resetNodeStyles() {
		if (!problem.getNodeType().equals(DSTConstants.NODE_RED_BLACK)){
			nodeCollection.resetNodeStyles(problem.getNodeType());
		}
	}
	
	private void removeSelectedState() {
		nodeCollection.removeSelectedState();
	}

	// stays
	public void resetEdgeStyles() {
		edgeCollection.resetEdgeColor();
	}

	// stays
	public void setEdgeParentAndChildren() {
		edgeCollection.setParentAndChildNodes();
	}
	

	public void addDiagLabel(String s){
		Proxy.getDST().add(new Label(s), 250,250);
	}
	
    public boolean isMST(){
    	return problem.getProblemText().substring(0,3).equals("MST");
    }
    
    // stays
    public TraversalContainer getTravCont(){
    	return cont;
    }
}