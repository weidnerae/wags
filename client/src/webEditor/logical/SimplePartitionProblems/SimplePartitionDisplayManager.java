package webEditor.logical.SimplePartitionProblems;

import java.util.ArrayList;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.animation.Animate;
import org.vaadin.gwtgraphics.client.shape.Path;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.Text;

import webEditor.Proxy;
import webEditor.logical.DSTConstants;
import webEditor.logical.DisplayManager;
import webEditor.logical.EdgeCollection;
import webEditor.logical.EdgeParent;
import webEditor.logical.Node;
import webEditor.logical.NodeCollection;
import webEditor.logical.NodeDragController;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
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
	
	protected Group swapButton;
	private Path leftUpArrow, rightUpArrow;

	private int lb = 0; // Used for swapping
	private int ub = 9;

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
		drawBoxes();
		addSwapButton();
		drawAllArrows();
		
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
			label.setStyleName("swap_node");
			panel.add(label, xPositions[i], yPositions[i]);

			nodeCollection.addNode(new Node(splitNodes[i], label));
		}
		
		nodeCollection.getNode(lb).getLabel().setStylePrimaryName("selected_swap_node");
		nodeCollection.getNode(ub).getLabel().setStylePrimaryName("selected_swap_node");
		
		makeNodesNotDraggable();
	}
	
	private class PartitionLowNodeClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			Label low = nodeCollection.getNode(lb).getLabel();
			
			if (low.getStylePrimaryName().contains("selected") && (lb + 1) < ub) {
				low.setStylePrimaryName("swap_node");
				lb++;
				low = nodeCollection.getNode(lb).getLabel();
				low.setStylePrimaryName("selected_swap_node");
				new Animate(leftUpArrow, "x", leftUpArrow.getX(), 
						leftUpArrow.getX() + 60, 150).start();
			} else if ((leftUpArrow.getX() + 60 <= rightUpArrow.getX()) && (lb == ub - 1)) {
				// last arrow movement, we want the two arrows to point at the same node
				new Animate(leftUpArrow, "x", leftUpArrow.getX(), leftUpArrow.getX() + 45, 150).start();
				new Animate(rightUpArrow, "x", rightUpArrow.getX(), rightUpArrow.getX() + 15, 150).start();
			}
		}
	}
	
	private class PartitionHighNodeClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			Label high = nodeCollection.getNode(ub).getLabel();
			
			if (high.getStylePrimaryName().contains("selected") && lb < (ub - 1)) {
				high.setStylePrimaryName("swap_node");
				ub--;
				high = nodeCollection.getNode(ub).getLabel();
				high.setStylePrimaryName("selected_swap_node");
				new Animate(rightUpArrow, "x", rightUpArrow.getX(), 
					    rightUpArrow.getX() - 60, 150).start();
			} else if ((leftUpArrow.getX() + 60 <= rightUpArrow.getX()) && (lb == ub - 1)) {
				// last arrow movement, we want the two arrows to point at the same node
				new Animate(rightUpArrow, "x", rightUpArrow.getX(), rightUpArrow.getX() - 45, 150).start();
				new Animate(leftUpArrow, "x", leftUpArrow.getX(), leftUpArrow.getX() - 15, 150).start();
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
				
				leftUpArrow.setX(30);
				rightUpArrow.setX(canvas.getWidth() - 30);

				nodeCollection.emptyNodes();
				insertNodesAndEdges();
				nodeCollection.getNode(lb).getLabel().setStylePrimaryName("selected_swap_node");
				nodeCollection.getNode(ub).getLabel().setStylePrimaryName("selected_swap_node");
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
		int startX = (canvas.getWidth() / 4) + 30;
		int startY = 210;
		Rectangle swapBox = new Rectangle (startX, startY,
										  ((canvas.getWidth() / 4) * 3) - startX - 30, 50);
		swapBox.setRoundedCorners(5);
		swapBox.setFillColor("Yellow");
		Text swapText = new Text(startX + 65, startY + 33, "Swap Nodes");
		swapText.setFillColor("Black");
		Group swapButton = new Group();
		
		swapButton.add(swapBox);
		swapButton.add(swapText);
		swapButton.bringToFront(swapText);
		
		swapButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (leftUpArrow.getX() + 60 <= rightUpArrow.getX()) {
					swapNodes();
				}
			}
		});

		canvas.add(swapButton);
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
	 * drawUpArrow
	 * Description: This method draws an upward facing arrow.
	 * @param xStart The starting x-coordinate for the tip of the arrow
	 */
	private Path drawUpArrow(int xStart) {
		int ARROW_TIP_Y = 85;
		
		Path arrow = new Path(xStart, ARROW_TIP_Y);
		arrow.lineRelativelyTo(15, 22);
		arrow.lineRelativelyTo(-7, 0);
		arrow.lineRelativelyTo(0, 35);
		arrow.lineRelativelyTo(-16, 0);
		arrow.lineRelativelyTo(0, -35);
		arrow.lineRelativelyTo(-7, 0);
		arrow.close();
		
		return arrow;
	}
	
	private Path drawRightArrow() {
		Path arrow = new Path((canvas.getWidth() / 4), 235);
		arrow.lineRelativelyTo(-32, -25);
		arrow.lineRelativelyTo(0, 12);
		arrow.lineRelativelyTo(-100, 0);
		arrow.lineRelativelyTo(0, 26);
		arrow.lineRelativelyTo(100, 0);
		arrow.lineRelativelyTo(0, 12);
		arrow.close();
		
		arrow.addClickHandler(new PartitionLowNodeClickHandler());
		
		return arrow;
	}
	
	private Path drawLeftArrow() {
		Path arrow = new Path(((canvas.getWidth() / 4) * 3), 235);
		arrow.lineRelativelyTo(32, -25);
		arrow.lineRelativelyTo(0, 12);
		arrow.lineRelativelyTo(100, 0);
		arrow.lineRelativelyTo(0, 26);
		arrow.lineRelativelyTo(-100, 0);
		arrow.lineRelativelyTo(0, 12);
		arrow.close();
		
		arrow.addClickHandler(new PartitionHighNodeClickHandler());

		return arrow;
	}
	
	private void drawAllArrows() {
		leftUpArrow = drawUpArrow(30);
		rightUpArrow = drawUpArrow(canvas.getWidth() - 30);
		Path rightFacingArrow = drawRightArrow();
		Path leftFacingArrow = drawLeftArrow();
		
		leftUpArrow.setFillColor("blue");
		rightUpArrow.setFillColor("red");
		rightFacingArrow.setFillColor("blue");
		leftFacingArrow.setFillColor("red");
		
		canvas.add(leftUpArrow);
		canvas.add(rightUpArrow);
		canvas.add(rightFacingArrow);
		canvas.add(leftFacingArrow);
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
		
		// do the actual swap
		getNodes().add(upperIndex, lower);
		getNodes().remove(upperIndex + 1);
		getNodes().add(lowIndex, upper);
		getNodes().remove(lowIndex + 1);
		
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
		
		Label label;
		
		for (int i = 0; i < getNodes().size(); i++) {
			label = getNodes().get(i).getLabel();
			label.setStyleName("swap_node");
			panel.add(label, xPositions[i], yPositions[i]);
		}
		
		// the upper and lower bounds can't move anymore and have already been swapped
		// the exercise if effectively done and the user can't do anything else
		if (lb >= ub) {
			nodeCollection.getNode(lb).getLabel().setStylePrimaryName("swap_node");
			nodeCollection.getNode(ub).getLabel().setStylePrimaryName("swap_node");
		} else {
			nodeCollection.getNode(lb).getLabel().setStylePrimaryName("selected_swap_node");
			nodeCollection.getNode(ub).getLabel().setStylePrimaryName("selected_swap_node");
		}

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
}