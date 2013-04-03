package webEditor.logical.SelectionSortProblems;

import java.util.ArrayList;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Line;

import webEditor.Proxy;
import webEditor.logical.DSTConstants;
import webEditor.logical.DisplayManager;
import webEditor.logical.EdgeCollection;
import webEditor.logical.EdgeParent;
import webEditor.logical.Node;
import webEditor.logical.NodeCollection;
import webEditor.logical.NodeDragController;
import webEditor.logical.NodeSelectable;
import webEditor.logical.TraversalContainer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class SelectionSortDisplayManager extends DisplayManager implements
		IsSerializable {
	private SelectionSortProblem problem;
	private EdgeCollection edgeCollection;

	int step = 0; // Keep track of which node is the red node

	public SelectionSortDisplayManager(DrawingArea canvas, AbsolutePanel panel,
			NodeCollection nc, EdgeCollection ec, SelectionSortProblem problem) {
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
		cont = new TraversalContainer(this);
		cont.setHidden(true);
		
		addProblemTextArea();
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
		}
	}
	
	public void insertNodesByValue(String nodes, String nodeType) {
		insertNodesByValueAndLocation(nodes, getSortXLocations(nodes),
				getSortYLocations(nodes), true, nodeType);
	}

	public void insertNodesByValueAndLocation(String nodes, int[] xPositions,
			int[] yPositions, boolean draggable, String nodeType) {
		String[] splitNodes = nodes.split(" ");
		
		for (int i = 0; i < splitNodes.length; i++) {
			Label label = new Label(splitNodes[i]);
			if (i < step) {
				label.setStyleName("immobilized_node");
			} else {
				label.setStyleName("node");
			}
			panel.add(label, xPositions[i], yPositions[i]);
			nodeCollection.addNode(new NodeSelectable(splitNodes[i], label, cont));
		}
		
		nodeCollection.getNode(step).getLabel().setStyleName("red_node");
		
		makeNodesNotDraggable();
	}

	private boolean showingSubMess;

	protected void addResetButton() {
		resetButton = new Button("Reset");
		resetButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				step = 0;
				removeWidgetsFromPanel();

				for (int i = 0; i < getNodes().size(); i++) {
					panel.remove(getNodes().get(i).getLabel());
				}

				nodeCollection.emptyNodes();
				insertNodesAndEdges();
				resetNodeStyles();
			}
		});
		resetButton.setStyleName("control_button");
		leftButtonPanel.add(resetButton, 62, 2);
	}

	private void addEvaluateButton() {
		evaluateButton = new Button("Swap");
		evaluateButton.setWidth("124px");
		evaluateButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String evalResult = "Please select a node.";
				
				// Make sure they have something selected, otherwise they would
				// lose points for double clicking accidently
				if (getSelectedNode() != null) {
					evalResult = problem.getEval().evaluate(
							problem.getName(), new String[]{cont.getTraversal(), "" + step}, getNodes(),
							getEdges());
				}
				
				
				// Only shuffle nodes if they were right
				if (!evalResult.contains("Sorry")) {
					shuffleNodes(evalResult);
				}
				
				if (showingSubMess) {
					Proxy.getDST().remove(submitText);
					Proxy.getDST().remove(submitOkButton);
				}
				
				// If they got it right, we just return an empty string
				// and we don't want to display that
				if (!evalResult.equals("")) {
					submitText.setText(evalResult);
					addToPanel(submitText, DSTConstants.SUBMIT_X,
							DSTConstants.SUBMIT_MESS_Y);
					int yOffset = DSTConstants.SUBMIT_MESS_Y
							+ submitText.getOffsetHeight() + 2;
					addToPanel(submitOkButton, DSTConstants.SUBMIT_X, yOffset);
					showingSubMess = true;
				}
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
	 * Swap the selected node with the red node.
	 * 
	 * @param eval String returned by Evaluation_SelectionSort.java
	 */
	private void shuffleNodes(String eval) {
		Node current = getNodes().get(step);
		Node smallest = getSelectedNode();
		int insertionIdx = getNodes().indexOf(smallest);
		getNodes().add(insertionIdx, current);
		getNodes().remove(insertionIdx + 1);
		getNodes().add(step, smallest);
		getNodes().remove(step + 1);
		
		step++;
		
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
			
			if (i < step) {
				label.setStyleName("immobilized_node");
			} else {
				label.setStyleName("node");
			}
			
			panel.add(label, xPositions[i], yPositions[i]);
		}
		
		nodeCollection.getNode(step).getLabel().setStyleName("red_node");
		makeNodesNotDraggable();
	}
	
	/**
	 * Finds the selected node.
	 * 
	 * @return null if there is no selected node
	 */
	private Node getSelectedNode() {
		for (Node n : getNodes()) {
			if (n.getLabel().getStylePrimaryName().contains("selected")) {
				return n;
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

	public void resetNodeStyles() {
		nodeCollection.resetNodeStyles(problem.getNodeType());
		nodeCollection.getNode(step).getLabel().setStyleName("red_node");
	}
}