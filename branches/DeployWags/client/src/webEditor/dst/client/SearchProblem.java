package webEditor.dst.client;

import org.vaadin.gwtgraphics.client.DrawingArea;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class SearchProblem extends Problem implements IsSerializable {
	private String name;
	private String problemText;
	private String nodes;
	private int[] xPositions; //must be same size as nodes
	private int[] yPositions; //must be sames size edges
	private String insertMethod;
	private Evaluation eval;
	private AddEdgeRules rules;
	private String[] arguments;
	private boolean nodesDraggable;
	private String nodeType;
	private SearchDisplayManager dm;

	public SearchProblem(String name, String problemText, String nodes, String insertMethod, int[] xPositions, int[] yPositions, 
			String[] arguments, Evaluation eval, boolean nodesDraggable, String nodeType)
	{
		this.name = name;
		this.problemText = problemText;
		this.nodes = nodes;
		this.insertMethod = insertMethod;
		this.xPositions = xPositions;
		this.yPositions = yPositions;;
		this.arguments = arguments;
		this.eval = eval;
		this.nodesDraggable = nodesDraggable;
		this.nodeType = nodeType;
	}
	
	public DisplayManager createDisplayManager(AbsolutePanel panel, DrawingArea canvas) {
	    NodeDragController.setFields(panel, true, null);
	//	NodeDropController.setFields(panel, ec);
	    GridNodeDropController.setFields(panel,60,30);
		NodeDragController.getInstance().registerDropController(GridNodeDropController.getInstance());		
		NodeCollection nc = new NodeCollection();
		
		dm = new SearchDisplayManager(canvas, panel, nc, this);		
		return dm;
	}
	
	public String evaluate() {
		return getEval().evaluate(getName(), getArguments(), dm.getNodes(), null);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
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

	public String[] getArguments()
	{
		return arguments;
	}

	public void setArguments(String[] arguments)
	{
		this.arguments = arguments;
	}
	
	public int[] getXPositions()
	{
		return xPositions;
	}
	
	public int[] getYPositions()
	{
		return yPositions;
	}
	
	public String getInsertMethod()
	{
		return insertMethod;
	}
		
	public boolean getNodesDraggable()
	{
		return nodesDraggable;
	}
	
	public String getNodeType()
	{
		return nodeType;
	}
}