package webEditor.dst.client;

import org.vaadin.gwtgraphics.client.DrawingArea;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class HashingProblem extends Problem implements IsSerializable {
	private String name;
	private String problemText;
	private String nodes;
	private int[] xPositions; // must be same size as nodes
	private int[] yPositions; // must be same size edges
	private String insertMethod;
	private String[] edges; // each array element contains two chars (i.e AB),
							// 1st is parent, 2nd is child
	private Evaluation eval;
	private AddEdgeRules rules;
	private String[] arguments;
	private boolean edgesRemovable;
	private boolean nodesDraggable;
	private String nodeType;
	private AbsolutePanel gridPanel;
	private DisplayManager dm;

	public HashingProblem(String name, String problemText, String nodes,
			String insertMethod, int[] xPositions, int[] yPositions,
			String[] edges, String[] arguments, Evaluation eval,
			AddEdgeRules rules, boolean edgesRemovable, boolean nodesDraggable,
			String nodeType) {
		this.name = name;
		this.problemText = problemText;
		this.nodes = nodes;
		this.insertMethod = insertMethod;
		this.xPositions = xPositions;
		this.yPositions = yPositions;
		this.edges = edges;
		this.arguments = arguments;
		this.eval = eval;
		this.rules = rules;
		this.edgesRemovable = edgesRemovable;
		this.nodesDraggable = nodesDraggable;
		this.nodeType = nodeType;
	}

	public DisplayManager createDisplayManager(AbsolutePanel panel,
			DrawingArea canvas) {
		gridPanel = new AbsolutePanel();
		gridPanel.setPixelSize(590, 288);
		panel.add(gridPanel, 10, 10);

		NodeDragController.setFields(gridPanel, true, null);
		GridNodeDropController.setFields(gridPanel, 60, 110);
		NodeDragController.getInstance().registerDropController(
				GridNodeDropController.getInstance());
		NodeCollection nc = new NodeCollection();

		dm = new HashingDisplayManager(canvas, panel, nc, this);
		return dm;
	}

	public String evaluate() {
		return getEval().evaluate(getName(), getArguments(), dm.getNodes(),
				dm.getEdges());
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getProblemText() {
		return problemText;
	}

	public void setProblemText(String problemText) {
		this.problemText = problemText;
	}

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public Evaluation getEval() {
		return eval;
	}

	public void setEval(Evaluation eval) {
		this.eval = eval;
	}

	public AddEdgeRules getRules() {
		return rules;
	}

	public void setRules(AddEdgeRules rules) {
		this.rules = rules;
	}

	public String[] getArguments() {
		return arguments;
	}

	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}

	public int[] getXPositions() {
		return xPositions;
	}

	public int[] getYPositions() {
		return yPositions;
	}

	public String getInsertMethod() {
		return insertMethod;
	}

	public String[] getEdges() {
		return edges;
	}

	public boolean getEdgesRemovable() {
		return edgesRemovable;
	}

	public boolean getNodesDraggable() {
		return nodesDraggable;
	}

	public String getNodeType() {
		return nodeType;
	}
	
	public int getTableSize(){
		return Integer.parseInt(arguments[0]);
	}
	public AbsolutePanel getGridPanel(){
		return gridPanel;
	}
}