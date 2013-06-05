package webEditor.logical.SelectionSortProblems;

import org.vaadin.gwtgraphics.client.DrawingArea;

import webEditor.Proxy;
import webEditor.logical.AddEdgeRules;
import webEditor.logical.DisplayManager;
import webEditor.logical.EdgeCollection;
import webEditor.logical.Evaluation;
import webEditor.logical.NodeCollection;
import webEditor.logical.NodeDragController;
import webEditor.logical.NodeDropController;
import webEditor.logical.Problem;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class SelectionSortProblem extends Problem implements IsSerializable {
	protected String name;
	protected String problemText;
	protected String nodes;
	protected int[] xPositions; // must be same size as nodes
	protected int[] yPositions; // must be same size edges
	protected String insertMethod;
	protected Evaluation eval;
	protected AddEdgeRules rules;
	protected String[] arguments;
	protected boolean nodesDraggable;
	protected String nodeType;
	protected DisplayManager dm;

	public SelectionSortProblem(String name, String problemText, String nodes,
			String insertMethod, String[] arguments, Evaluation eval, 
			AddEdgeRules rules, boolean nodesDraggable, String nodeType) {
		this.name = name;
		this.problemText = problemText;
		this.nodes = nodes;
		this.insertMethod = insertMethod;
		this.arguments = arguments;
		this.eval = eval;
		this.rules = rules;
		this.nodesDraggable = nodesDraggable;
		this.nodeType = nodeType;
	}

	public DisplayManager createDisplayManager(AbsolutePanel panel,
			DrawingArea canvas) {
		EdgeCollection ec = new EdgeCollection(getRules(), new String[] {"", "" }, //does nothing
				getEdgesRemovable());
		NodeDragController.setFields(panel, true, ec);
		NodeDropController.setFields(panel, ec);
		NodeDragController.getInstance().registerDropController(
				NodeDropController.getInstance());
		NodeCollection nc = new NodeCollection();

		dm = new SelectionSortDisplayManager(canvas, panel, nc, ec, this);
		
		return dm;
	}

	public String evaluate() {
		return getEval().evaluate(getName(), getArguments(), dm.getNodes(),
				dm.getEdges());
	}

	public String getName() {
		return name;
	}

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

	public boolean getEdgesRemovable() {
		return false;
	}

	public boolean getNodesDraggable() {
		return nodesDraggable;
	}

	public String getNodeType() {
		return nodeType;
	}
	
	public String printDetails(){
		String args = "";
		for(int i = 0; i < arguments.length; i++){
			args += arguments[i] + ",";
			
		}
		args = args.substring(0, args.length()-1);
		
		String details = "";
		details += "&title=" + name + "&problemText=" + problemText +
				"&nodes=" + nodes + "&insertMethod=" + insertMethod +
				"&arguments=" + args + "&evaluation=" + eval.returnKeyValue() +
				"&nodesDraggable=" + nodesDraggable + "&nodeType=" +
				nodeType + "&group=15" + "&genre=selectionsort";
		
		Proxy.uploadLogicalMicrolab(details);
		return details;
	}
}