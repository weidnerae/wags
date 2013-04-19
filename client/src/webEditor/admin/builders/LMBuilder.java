package webEditor.admin.builders;

import java.util.ArrayList;

import webEditor.Genre;
import webEditor.InsertMethod;
import webEditor.NodeType;

public abstract class LMBuilder {
	private static final int XPOS = 0;
	private static final int YPOS = 1;
	
	private String title, problemText, arguments;
	private boolean edgesRemovable, nodesDraggable;
	private int evaluation, edgeRules, groupId;
	private ArrayList<String> nodes, edges;
	private String[][] positions;
	
	private InsertMethod insertMethod;
	private NodeType nodeType;
	private Genre genre;
	
	
	public LMBuilder(Genre genre, InsertMethod insertMethod, NodeType nodeType,
			boolean edgesRemovable, boolean nodesDraggable, int edgeRules,
			int groupId){	
		this.genre = genre;
		this.insertMethod = insertMethod;
		this.nodeType = nodeType;
		this.edgesRemovable = edgesRemovable;
		this.nodesDraggable = nodesDraggable;
		this.edgeRules = edgeRules;
		this.groupId = groupId;
		nodes = new ArrayList<String>();
		edges = new ArrayList<String>();
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setProblemText(String problemText){
		this.problemText = problemText;
	}
	
	public void addNode(String node){
		nodes.add(node);
	}
	
	public void addEdge(String edge){
		edges.add(edge);
	}
	
	public void setEval(int eval){
		this.evaluation = eval;
	}
	
	public abstract void buildArgs();
	public abstract void buildPos();
	public abstract String uploadString();
}
