package wags.logical.SimplePartitionProblems;

import java.util.ArrayList;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Line;

import wags.Proxy;
import wags.logical.DSTConstants;
import wags.logical.DisplayManager;
import wags.logical.EdgeCollection;
import wags.logical.EdgeParent;
import wags.logical.Node;
import wags.logical.NodeCollection;
import wags.logical.NodeDragController;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class SimplePartitionDisplayManager extends DisplayManager implements
		IsSerializable {
	private SimplePartitionProblem problem;
	private EdgeCollection edgeCollection;
	
	protected Button swapButton;

	private int lb = 0; // Used for swapping
	private int ub = 9;
	private HandlerRegistration lowReg, highReg;

	public SimplePartitionDisplayManager(DrawingArea canvas, AbsolutePanel panel,
			NodeCollection nc, EdgeCollection ec, SimplePartitionProblem problem) {
		System.out.println(problem.getName());
		this.panel = panel;
		this.canvas = canvas;
		this.nodeCollection = nc;
		this.edgeCollection = ec;
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
		addSwapButton();
		drawBoxes();
		
		insertNodesAndEdges();
	}

	private void insertNodesAndEdges() {
		if (problem.getInsertMethod().equals(DSTConstants.INSERT_METHOD_VALUE)) {
			insertNodesByValue(problem.getNodes(), problem.getNodeType());
		}
	}
	
	public void insertNodesByValue(String nodes, String nodeType) {
		insertNodesByValueAndLocation(nodes, getSortXLocations(nodes),
				getSortYLocations(nodes), true, nodeType);
	}

	public void insertNodesByValueAndLocation(String nodes, int[] xPositions,
			int[] yPositions, boolean draggable, String nodeType) {
		String[] splitNodes = nodes.split(" ");
		Label label;

		for (int i = 0; i < splitNodes.length; i++) {
			label = new Label(splitNodes[i]);
			if (i < lb || i > ub) {
				label.setStyleName("immobilized_node");
			} else {
				label.setStyleName("node");
			}
			label.setStyleName("node");
			panel.add(label, xPositions[i], yPositions[i]);

			nodeCollection.addNode(new Node(splitNodes[i], label));
		}
		
		nodeCollection.getNode(lb).getLabel().setStylePrimaryName("selected_red_node");
		lowReg = nodeCollection.getNode(lb).getLabel().
				addClickHandler(new PartitionLowNodeClickHandler());
		nodeCollection.getNode(ub).getLabel().setStylePrimaryName("selected_red_node");
		highReg = nodeCollection.getNode(ub).getLabel().
				addClickHandler(new PartitionHighNodeClickHandler());
		
		makeNodesNotDraggable();
	}
	
	private class PartitionLowNodeClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			Label low = nodeCollection.getNode(lb).getLabel();
			if (low.getStylePrimaryName().contains("selected") && (lb + 1) < ub) {
				lowReg.removeHandler();
				low.setStylePrimaryName("immobilized_node");
				lb++;
				low = nodeCollection.getNode(lb).getLabel();
				low.setStylePrimaryName("selected_red_node");
				lowReg = low.addClickHandler(new PartitionLowNodeClickHandler());
			}
		}
	}
	
	private class PartitionHighNodeClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			Label high = nodeCollection.getNode(ub).getLabel();
			if (high.getStylePrimaryName().contains("selected") && lb < (ub - 1)) {
				highReg.removeHandler();
				high.setStylePrimaryName("immobilized_node");
				ub--;
				high = nodeCollection.getNode(ub).getLabel();
				high.setStylePrimaryName("selected_red_node");
				highReg = high.addClickHandler(new PartitionHighNodeClickHandler());
			}
		}
		
	}

	private boolean showingSubMess;

	protected void addResetButton() {
		resetButton = new Button("Reset");
		resetButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				lb = 0;
				ub = 9;
				removeWidgetsFromPanel();

				for (int i = 0; i < getNodes().size(); i++) {
					panel.remove(getNodes().get(i).getLabel());
				}

				nodeCollection.emptyNodes();
				insertNodesAndEdges();
				resetNodeStylesAndHandlers();
			}
		});
		resetButton.setStyleName("control_button");
		leftButtonPanel.add(resetButton, 62, 2);
	}

	private void addEvaluateButton() {
		evaluateButton = new Button("Finalize");
		evaluateButton.setWidth("100px");
		evaluateButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String evalResult = "Please select a node.";
				
				// Make sure they have something selected
				if (getLowerSelectedNode() != null && getUpperSelectedNode() != null) {
					evalResult = problem.getEval().evaluate(
							problem.getName(), problem.getArguments(), getNodes(),
							getEdges());
				}
				
				if (showingSubMess) {
					Proxy.getDST().remove(submitText);
					Proxy.getDST().remove(submitOkButton);
				}
				
				// If they got it right, we just return an empty string
				// and we don't want to display that
				if (evalResult.equals(""))
					return;
				
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
		rightButtonPanel.add(evaluateButton, 281, 2);

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
	
	private void addSwapButton() {
		swapButton = new Button("Swap highlighted nodes");
		swapButton.setWidth("175px");
		
		swapButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				swapNodes();
			}
		});
		
		swapButton.setStyleName("control_button");
		rightButtonPanel.add(swapButton, 56, 2);
	}
	
	/**
	 * I stole this from Daniel.
	 */
	private int[] getSortXLocations(String nodes) {
		int startX = 10;
		String[] splitNodes = nodes.split(" ");
		int[] x = new int[splitNodes.length];
		for (int i = 0; i < splitNodes.length; i++) {
			if (i % 10 == 0) {
				startX = 10;
			}
			
			x[i] = startX;
			startX += 60;
		}
		return x;
	}

	/**
	 * I stole this from Daniel.
	 */
	private int[] getSortYLocations(String nodes) {
		int startY = 30;
		String[] splitNodes = nodes.split(" ");
		int[] y = new int[splitNodes.length];
		for (int i = 0; i < splitNodes.length; i++) {
			if (i > 0 && i % 10 == 0) {
				startY += 100;
			}
			y[i] = startY;
		}
		return y;
	}
	
	/**
	 * I stole this from Daniel.
	 */
	public void drawBoxes() {
		int YTOP = 25;
		int YBOTTOM = 75;
		int xStart = 5;
		int labelY = 140;
		for (int i = 0; i < problem.getNodes().split(" ").length; i++) {
			if (xStart + 50 >= 600) {
				YTOP += 100;
				YBOTTOM += 100;
				xStart = 5;
				labelY += 100;
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
	}
	
	/**
	 * Swap the left selected node with the right selected node.
	 * 
	 * @param eval String returned by Evaluation_SelectionSort.java
	 */
	private void swapNodes() {
		Node lower = getLowerSelectedNode();
		Node upper = getUpperSelectedNode();
		int lowIndex = getNodes().indexOf(lower);
		int upperIndex = getNodes().indexOf(upper);
		getNodes().add(upperIndex, lower);
		getNodes().remove(upperIndex + 1);
		highReg.removeHandler();
		highReg = getNodes().get(upperIndex-1).getLabel().
				addClickHandler(new PartitionHighNodeClickHandler());
		getNodes().add(lowIndex, upper);
		getNodes().remove(lowIndex + 1);
		lowReg.removeHandler();
		lowReg = getNodes().get(lowIndex+1).getLabel().
				addClickHandler(new PartitionLowNodeClickHandler());
		
		// we just swapped the nodes, so the upper and lower bounds
		// should be updated for the next swap
		lb++;
		ub--;
		
		// Redraw the nodes in the correct order
		removeWidgetsFromPanel();
		reinsertNodes();
	}
	
	/**
	 * Insert all the nodes, setting the correct style so that they 
	 * are green or red based on the current step.
	 * 
	 */
	public void reinsertNodes() {
		int[] xPositions = getSortXLocations(problem.getNodes());
		int[] yPositions = getSortYLocations(problem.getNodes());
		
		for (int i = 0; i < getNodes().size(); i++) {
			Label label = getNodes().get(i).getLabel();
			
			if (i < lb || i > ub) {
				label.setStyleName("immobilized_node");
			} else {
				label.setStyleName("node");
			}
			
			panel.add(label, xPositions[i], yPositions[i]);
		}
		
		nodeCollection.getNode(lb).getLabel().setStylePrimaryName("selected_red_node");
		nodeCollection.getNode(ub).getLabel().setStylePrimaryName("selected_red_node");

		makeNodesNotDraggable();
	}
	
	/**
	 * Finds the selected node coming from the lower bound.
	 * 
	 * @return null if there is no selected node
	 */
	private Node getLowerSelectedNode() {
		for (int i = lb; i < ub; i++) {
			Node n = getNodes().get(i);
			if (n.getLabel().getStylePrimaryName().contains("selected")) {
				return n;
			}
		}
		
		return null;
	}
	
	private Node getUpperSelectedNode() {
		for (int i = ub; i > lb; i--) {
			if (getNodes().get(i).getLabel().getStylePrimaryName().contains("selected")) {
				return getNodes().get(i);
			}
		}
		
		return null;
	}

	public ArrayList<Node> getNodes() {
		return nodeCollection.getNodes();
	}

	public ArrayList<EdgeParent> getEdges() {
		return edgeCollection.getEdges();
	}

	public void makeNodesDraggable() {
		nodeCollection.makeNodesDraggable(NodeDragController.getInstance());
	}

	public void makeNodesNotDraggable() {
		nodeCollection.makeNodesNotDraggable(NodeDragController.getInstance());
	}

	public void resetNodeStylesAndHandlers() {
		nodeCollection.getNode(lb).getLabel().setStylePrimaryName("selected_red_node");
		lowReg.removeHandler();
		lowReg = nodeCollection.getNode(lb).getLabel().
				addClickHandler(new PartitionLowNodeClickHandler());
		nodeCollection.getNode(ub).getLabel().setStylePrimaryName("selected_red_node");
		highReg.removeHandler();
		highReg = nodeCollection.getNode(ub).getLabel().
				addClickHandler(new PartitionHighNodeClickHandler());
	}
}