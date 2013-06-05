package webEditor.logical.TreeProblems.RedBlackProblems;

import org.vaadin.gwtgraphics.client.DrawingArea;

import webEditor.Proxy;
import webEditor.logical.AddEdgeRules;
import webEditor.logical.DisplayManager;
import webEditor.logical.EdgeCollection;
import webEditor.logical.Evaluation;
import webEditor.logical.NodeCollection;
import webEditor.logical.NodeDragController;
import webEditor.logical.NodeDropController;

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
	
	public String printDetails(){
		String str = "";
		str = "&title=" + this.name + "&problemText=" + this.problemText + "&nodes=" + this.nodes;
		String xPos = "";
		String yPos = "";
		for(int i = 0; i < xPositions.length; i++){
			xPos += xPositions[i] + ",";
			yPos += yPositions[i] + ",";
		}
		xPos = xPos.substring(0, xPos.length()-1);
		yPos = yPos.substring(0, yPos.length()-1);
		
		str += "&xPositions=" + xPos + "&yPositions=" + yPos + "&insertMethod=" + this.insertMethod;
		
		String edgStr = "";
		for(int i = 0; i < edges.length; i++){
			edgStr += edges[i] + ",";
		}
		edgStr = edgStr.substring(0, edgStr.length() - 1);
		
		String args = "";
		for(int i = 0; i < arguments.length; i++){
			args += arguments[i] + ",";
		}
		args = args.substring(0, args.length() - 1);
		
		int edgeRem = 0, nodesDrag = 0;
		if(this.edgesRemovable) edgeRem = 1;
		if(this.nodesDraggable) nodesDrag = 1;
		str += "&edges=" + edgStr + "&evaluation=" + this.eval.returnKeyValue() + "&edgeRules=" + this.rules.returnKeyValue() 
				+ "&arguments=" + args + "&edgesRemovable=" + edgeRem
				+ "&nodesDraggable=" + nodesDrag + "&nodeType=" + this.nodeType + "&genre=redblack" + 
				"&group=15";
		
		Proxy.uploadLogicalMicrolab(str);
		return str;
		
	}

}