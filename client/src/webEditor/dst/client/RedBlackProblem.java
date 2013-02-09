package webEditor.dst.client;

import org.vaadin.gwtgraphics.client.DrawingArea;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class RedBlackProblem extends TreeTypeProblem implements IsSerializable {
	protected String name;
	protected String problemText;
	protected String nodes;
	protected int[] xPositions; // must be same size as nodes
	protected int[] yPositions; // must be same size edges
	protected String insertMethod;
	protected String[] edges; 	// each array element contains two chars (i.e AB),
								// 1st is parent, 2nd is child
	protected Evaluation eval;
	protected AddEdgeRules rules;
	protected String[] arguments;
	protected boolean edgesRemovable;
	protected boolean nodesDraggable;
	protected String nodeType;
	protected DisplayManager dm;

	public RedBlackProblem(String name, String problemText, String nodes,
			String insertMethod, int[] xPositions, int[] yPositions,
			String[] edges, String[] arguments, Evaluation eval,
			AddEdgeRules rules, boolean edgesRemovable, boolean nodesDraggable,
			String nodeType) {
		super(name, problemText, nodes, insertMethod, xPositions, yPositions, edges, arguments, eval, rules, edgesRemovable, nodesDraggable, nodeType);
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
		EdgeCollection ec = new EdgeCollection(getRules(), new String[] {
				"Select first node of edge", "Select second node of edge" },
				getEdgesRemovable());
		NodeDragController.setFields(panel, true, ec);
		NodeDropController.setFields(panel, ec);
		NodeDragController.getInstance().registerDropController(
				NodeDropController.getInstance());
		NodeCollection nc = new NodeCollection();
	
		dm = new RedBlackDisplayManager(canvas, panel, nc, ec, this);
		ec.setDisplayManager(dm);
		return dm;
	}

}