package webEditor.logical.RadixProblems;

import org.vaadin.gwtgraphics.client.DrawingArea;

import webEditor.Proxy;
import webEditor.logical.AddEdgeRules;
import webEditor.logical.DisplayManager;
import webEditor.logical.Evaluation;
import webEditor.logical.GridNodeDropController;
import webEditor.logical.NodeCollection;
import webEditor.logical.NodeDragController;
import webEditor.logical.Problem;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.AbsolutePanel;

import java.util.HashMap;
import java.util.Map;

public class SearchProblem extends Problem implements IsSerializable {
	private String name;
	private String problemText;
	private String nodes;
	private int[][] xPositions; // must be same size as nodes
	private int[][] yPositions; // must be same size edges
	private String insertMethod;
	private Evaluation_RadixSortWithHelp eval;
	private AddEdgeRules rules;
	private String[] arguments;
	private boolean nodesDraggable;
	private String nodeType;
	private SearchDisplayManager dm;
	private AbsolutePanel gridPanel;
	private Map<String, Integer> indices;

	public SearchProblem(String name, String problemText, String nodes,
			String insertMethod, String[] arguments, Evaluation_RadixSortWithHelp eval,
			boolean nodesDraggable, String nodeType) {
		this.name = name;
		this.problemText = problemText;
		this.nodes = nodes;
		this.insertMethod = insertMethod;
		this.arguments = arguments;
		this.eval = eval;
		this.nodesDraggable = nodesDraggable;
		this.nodeType = nodeType;

		// Creating a map for grabbing the index of a given node (String)
		// used for generating the reset locations (xPositions, yPositions)
		String[] nodeArr = nodes.split(" ");
		indices = new HashMap<String, Integer>();

		for (int i = 0; i < nodeArr.length; i++) {
			indices.put(nodeArr[i], i);
		}

		// Removed passing in of reset positions, generate from these methods
		// instead
		String[] solutions = grabSolutions(arguments);
		this.xPositions = getResetValuesX(solutions);
		this.yPositions = getResetValuesY(solutions);
	}

	public DisplayManager createDisplayManager(AbsolutePanel panel,
			DrawingArea canvas) {
		gridPanel = new AbsolutePanel();
		gridPanel.setPixelSize(590, 543);
		panel.add(gridPanel, 10, 7);

		NodeDragController.setFields(gridPanel, true, null);
		GridNodeDropController.setFields(gridPanel, 60, 30);
		NodeDragController.getInstance().registerDropController(
				GridNodeDropController.getInstance());
		NodeCollection nc = new NodeCollection();

		dm = new SearchDisplayManager(canvas, panel, nc, this);
		eval.setDisplayManager(dm);
		return dm;
	}

	public String evaluate() {
		return getEval().evaluate(getName(), getArguments(), dm.getNodes(),
				null);
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

	public void setEval(Evaluation_RadixSortWithHelp eval) {
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

	public int[][] getXPositions() {
		return xPositions;
	}

	public int[][] getYPositions() {
		return yPositions;
	}

	public String getInsertMethod() {
		return insertMethod;
	}

	public boolean getNodesDraggable() {
		return nodesDraggable;
	}

	public String getNodeType() {
		return nodeType;
	}

	public AbsolutePanel getGridPanel() {
		return gridPanel;
	}

	private int[][] getResetValuesX(String[] solutions) {
		int[][] result = new int[6][solutions[0].split(" ").length];

		for (int i = 0; i < indices.size(); i++) {
			result[0][i] = i * 60;
		}

		for (int i = 0; i < result.length; i++) {
			String[] node = solutions[i / 2].split(" ");
			int[] counters = new int[node.length];
			int digit = i / 2;

			if (i != 0) {
				node = solutions[(i - 1) / 2].split(" ");
				for (int j = 0; j < node.length; j++) {
					result[i][indices.get(node[j])] = j * 60;
				}
			}

			i++;

			for (int j = 0; j < node.length; j++) {
				int nodeVal = 0;

				if (node[j].length() > digit) {
					System.out.println(node[j]);
					System.out.println(digit);
					nodeVal = Character.digit(
							node[j].charAt(node[j].length() - (1 + digit)), 10);
					System.out.println(nodeVal);
				}

				result[i][indices.get(node[j])] = 60 * nodeVal;
				counters[nodeVal]++;
			}
		}

		return result;
	}

	private int[][] getResetValuesY(String[] solutions) {
		int[][] result = new int[6][solutions[0].split(" ").length];

		for (int i = 1; i < result.length; i += 2) {
			String[] node = solutions[i / 2].split(" ");
			int[] counters = new int[node.length];
			int digit = i / 2;

			for (int j = 0; j < node.length; j++) {
				int nodeVal = 0;

				if (node[j].length() > digit) {
					nodeVal = Character.digit(
							node[j].charAt(node[j].length() - (1 + digit)), 10);
				}

				result[i][indices.get(node[j])] = 73 + 40 * counters[nodeVal];
				counters[nodeVal]++;
			}
		}

		return result;
	}
	
	private String[] grabSolutions(String[] arr) {
		String[] result = new String[3];
		
		for (int i = 3; i < arr.length; i++) {
			result[i - 3] = arr[i];
		}
		
		return result;
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
				nodeType + "&group=9" + "&genre=radix";
		
		Proxy.uploadLogicalMicrolab(details);
		return details;
	}
}