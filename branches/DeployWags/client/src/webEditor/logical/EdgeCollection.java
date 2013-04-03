package webEditor.logical;

import java.util.ArrayList;

import webEditor.logical.AddEdgeRules;
import webEditor.logical.TreeProblems.RedBlackProblems.TreeTypeDisplayManager;

import org.vaadin.gwtgraphics.client.Line;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;

public class EdgeCollection implements IsSerializable {
	private String[] nodeSelectionInstructions;
	private ArrayList<EdgeParent> edges;
	private int numNodesSelected;
	private Label firstNodeSelected;
	private EdgeClickListener handler;
	private TreeTypeDisplayManager dm;
	private HandlerRegistration[] edgeHandlers;
	private AddEdgeRules rules;
	private boolean removable;
	private NodeCollection graphNodeCollection = new NodeCollection();

	public EdgeCollection(AddEdgeRules rules,
			String[] nodeSelectionInstructions, boolean removable) {
		this.rules = rules;
		this.nodeSelectionInstructions = nodeSelectionInstructions;
		this.removable = removable;
		edges = new ArrayList<EdgeParent>();
		handler = new EdgeClickListener();
		numNodesSelected = 0;
	}

	public void setDisplayManager(DisplayManager dm) {
		this.dm = (TreeTypeDisplayManager) dm;
	}
	
	public DisplayManager getDisplayManager() {
		return this.dm;
	}

	public void addNextEdge() {
		dm.setEdgeNodeSelectionInstructions(nodeSelectionInstructions[0]);
		class EdgeNodeSelectionHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				Label selectedNode = (Label) event.getSource();
				if (numNodesSelected == 0) {
					Node n = getNodeByLabel(selectedNode);
					selectFirstNodeOfEdge(n.getLabel());
				} else if (selectedNode != firstNodeSelected) {
					Node n1 = getNodeByLabel(firstNodeSelected);
					Node n2 = getNodeByLabel(selectedNode);

					if (n2.getTop() < n1.getTop()) {
						Node temp = n2;
						n2 = n1;
						n1 = temp;
					}
					dm.setEdgeParentAndChildren();
					String check = rules.checkSecondNode(n1, n2, dm.getNodes(),
							dm.getEdges());

					if (check.equalsIgnoreCase(DSTConstants.CORRECT)) {
						EdgeUndirected eu = new EdgeUndirected(n1, n2,
								getInstance(), handler, removable);
						eu.drawEdge();
						edges.add(eu);
						dm.addEdgeCancel();
						dm.resetRemoveEdgeButton();
					} else {
						showEdgeAdditionError(check);
					}
				}
			}
		}

		EdgeNodeSelectionHandler eh = new EdgeNodeSelectionHandler();

		ArrayList<Node> nodes = dm.getNodes();
		edgeHandlers = new HandlerRegistration[nodes.size()];

		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			Label l = n.getLabel();
			edgeHandlers[i] = l.addClickHandler(eh);
		}
	}

	private EdgeCollection getInstance() {
		return this;
	}

	private void showEdgeAdditionError(String error) {
		TextArea t = new TextArea();
		t.setCharacterWidth(30);
		t.setReadOnly(true);
		t.setVisibleLines(5);
		t.setText(error);
		dm.addToPanel(t, DSTConstants.PROMPT_X, DSTConstants.PROMPT_Y);

		Button ok = new Button("Ok");
		ok.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dm.resetEdgeStyles();
				dm.removeWidgetsFromPanel();
			}
		});
		int yOffset = DSTConstants.PROMPT_Y + t.getOffsetHeight() + 1;
		dm.addToPanel(ok, DSTConstants.PROMPT_X, yOffset);
	}

	public void selectFirstNodeOfEdge(Label node) {
		dm.setEdgeParentAndChildren();
		String check = rules.checkFirstNode(getNodeByLabel(node),
				dm.getNodes(), dm.getEdges());
		if (check.equals(DSTConstants.CORRECT)) {
			firstNodeSelected = node;
			numNodesSelected++;
			dm.setEdgeNodeSelectionInstructions(nodeSelectionInstructions[1]);
			if (node.getOffsetHeight() < 38) { // Check to see if regular node
												// or string node
				node.setStyleName("selected_string_node");
			} else {
				node.setStyleName("selected_node");
			}
		} else {
			showEdgeAdditionError(check);
		}
	}

	public void insertEdges(String[] edgePairs, ArrayList<Node> nodes) {
		// finding which nodes are used
		ArrayList<String> usedNodes = new ArrayList<String>();
		ArrayList<String> duplicates = new ArrayList<String>();
		for (String s : edgePairs) {
			String[] splitEdgePairs = s.split(" ");
			for (int i=0;i<2;i++) {
				if (!usedNodes.contains(splitEdgePairs[i])) {
						usedNodes.add(splitEdgePairs[i]);
				}
				else if(i==1){
					duplicates.add(splitEdgePairs[i]);
				}
			}
		}
		// making an arraylist of nodes with only the nodes that are used
		ArrayList<Node> nodesToUse = new ArrayList<Node>();
		int offset = 0;
		for (int i = 0; i < nodes.size(); i++) {
			if (usedNodes.contains(nodes.get(i).getValue())) {
				nodesToUse.add(i + offset, nodes.get(i));
			} else {
				offset -= 1;
			}
		}
		for(String ep: edgePairs){
			String[] splitEdges = ep.split(" ");
			String parent = splitEdges[0];
			String child = splitEdges[1];
			Node parentNode = null;
			Node childNode = null;
			if(!duplicates.contains(parent) && !duplicates.contains(child)){              // If parent and child is unique
				for(Node n: nodesToUse){
					if(n.getValue().equals(parent)){
						parentNode = n;
					}
					if(n.getValue().equals(child)){
						childNode = n;
					}
				}
			}else{          
				int distance = 1000;    // Magical, just needed to be big.
				for(Node n1: nodes){
					if(n1.getValue().equals(parent)){
						for(Node n2: nodes){
							if(n2.getValue().equals(child)){
								if(n2.getTop()>n1.getTop() && Math.abs(n1.getLeft()-n2.getLeft()) < distance){
									distance = (int) Math.sqrt((Math.pow(Math.abs(n1.getLeft()-n2.getLeft()),2)+Math.pow((n2.getTop()-n1.getTop()),2)));
									parentNode = n1;
									childNode = n2;
								}
							}
						}
					}
				}
			}
            EdgeUndirected eu = new EdgeUndirected(parentNode, childNode, getInstance(), handler, removable);
            eu.drawEdge();
            edges.add(eu);
		}
		
	}
	
	public void insertGraphEdges(String[] edgePairs, ArrayList<Node> nodes){
		for(String edgePair: edgePairs){
			String[]splitEdges = edgePair.split(" ");
			Node n1 = null;
			Node n2 = null;
			int weight = Integer.parseInt(splitEdges[2]);
			for(int i=0;i<nodes.size();i++){
				if(nodes.get(i).getValue().equals(splitEdges[0])){
					n1 = nodes.get(i); 
				}
				if(nodes.get(i).getValue().equals(splitEdges[1])){
					n2 = nodes.get(i);
				}
			}
			EdgeUndirected eu = new EdgeUndirected(n1, n2, getInstance(), handler, removable, weight);
			eu.drawEdge();	
			
			edges.add(eu);
		}
		for(EdgeParent eu: edges){
			((EdgeUndirected)eu).addWeightLabel();
		}
	}

	public void clearEdgeNodeSelections() {
		numNodesSelected = 0;
		for (int i = 0; i < edgeHandlers.length; i++) {
			edgeHandlers[i].removeHandler();
		}
	}

	public void addEdgeToCanvas(Line line) {
		line.setStrokeWidth(10);
		dm.drawEdge(line);
	}

	public void removeEdgeFromCanvas(Line line) {
		dm.removeEdge(line);
	}

	public void updateEdgeDrawings() {
		for (int i = 0; i < edges.size(); i++) {
			edges.get(i).redraw();
		}
	}

	private Node getNodeByLabel(Label l) {
		ArrayList<Node> nodes = dm.getNodes();
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).getLabel() == l) {
				return nodes.get(i);
			}
		}
		return null;
	}

	private EdgeParent getEdgeByLine(Line line) {
		for (int i = 0; i < edges.size(); i++) {
			if (edges.get(i).getLine() == line) {
				return edges.get(i);
			}
		}
		return null;
	}

	public void resetEdgeColor() {
		for (int i = 0; i < edges.size(); i++) {
			edges.get(i).getLine().setStrokeColor("black");
		}
	}

	public ArrayList<EdgeParent> getEdges() {
		return edges;
	}

	/**
	 * Method: setParentAndChildNodes()
	 * 
	 * Goes through the current set of edges and moves the parent node of the
	 * edge to N1 and the child to N2. This simplifies evaluation and edge
	 * addition.
	 */
	public void setParentAndChildNodes() {
		for (int i = 0; i < edges.size(); i++) {
			EdgeParent edge = edges.get(i);
			if (edge.getN2().getTop() < edge.getN1().getTop()) {
				Node temp = edge.getN2();
				edge.setN2(edge.getN1());
				edge.setN1(temp);
			}
		}
	}

	public void emptyEdges() {
		edges.clear();
	}

	class EdgeClickListener implements ClickHandler {
		private Line currElement = null;

		public void onClick(ClickEvent event) {
			dm.removeWidgetsFromPanel();
			dm.addEdgeCancel();
			dm.resetRemoveEdgeButton();

			if (currElement != null)
				currElement.setStrokeColor("black");

			currElement = (Line) event.getSource();
			currElement.setStrokeColor("yellow");

			Label l = new Label("Do you want to delete this edge?");
			l.setStyleName("edge_remove");
			dm.addToPanel(l, DSTConstants.EDGE_PROMPT_X,
					DSTConstants.EDGE_PROMPT_Y);

			Button yes = createYesButton(currElement);
			int yOffset = DSTConstants.EDGE_PROMPT_Y + l.getOffsetHeight();
			dm.addToPanel(yes, DSTConstants.EDGE_PROMPT_X, yOffset);

			Button no = createNoButton();
			dm.addToPanel(no,
					DSTConstants.EDGE_PROMPT_X + 3 + yes.getOffsetWidth(),
					yOffset);
		}

		private Button createYesButton(final Line line) {
			final Button b = new Button("Yes");

			b.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					edges.remove(getEdgeByLine(line));
					for (int i = 0; i < edges.size(); i++) {
						EdgeUndirected e = (EdgeUndirected) edges.get(i);
						System.out.println(e.getN1().getValue() + " "
								+ e.getN2().getValue());
					}
					dm.removeEdge(line);
					dm.resetEdgeStyles();
					dm.removeWidgetsFromPanel();
				}
			});
			return b;
		}

		private Button createNoButton() {
			final Button b = new Button("No");

			b.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					dm.resetEdgeStyles();
					dm.removeWidgetsFromPanel();
				}
			});
			return b;
		}
	}

	public int getNumNodesSelected() {
		return numNodesSelected;
	}

	public String getSecondInstructions() {
		return nodeSelectionInstructions[1];
	}
	public void addWeightLabel(Label l, int x, int y, EdgeUndirected edge){
		dm.addWeightLabel(l, x, y);
		graphNodeCollection.addNode(new NodeClickable(l.getText(),l, dm.getTravCont(), false, edge, this));
	}
	public NodeCollection getGraphNodeCollection(){
		return graphNodeCollection;
	}
	public void clearGraphNodeCollection(){
		graphNodeCollection.emptyNodes();
		for(EdgeParent ep: edges){
			((EdgeUndirected)ep).addWeightLabel();
		}
	}
}
