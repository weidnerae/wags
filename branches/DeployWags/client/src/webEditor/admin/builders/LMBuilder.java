package webEditor.admin.builders;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;


public abstract class LMBuilder {
	private static final int XPOS = 0;
	private static final int YPOS = 1;
	
	private String title, problemText, arguments = "";
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
		if(!edge.equals("")){
			edges.add(edge);
		}
	}
	
	public void setEval(int eval){
		this.evaluation = eval;
	}
	
	public void setArgs(String[] args){
		for(String arg: args){
			arguments += arg + ",";
		}
		
		// Remove final comma
		arguments = arguments.substring(0, arguments.length() - 1);
	}
	
	public void setPos(int[] xPos, int[] yPos){
		if(xPos.length != yPos.length){
			Window.alert("Position arrays are of different length.  Error!");
			return;
		}
		
		// Adds all positions as strings to positions[][]
		positions = new String[2][xPos.length];
		for(int i = 0; i < xPos.length; i++){
			positions[XPOS][i] = xPos[i] + "";
			positions[YPOS][i] = yPos[i] + ""; 
		}
	}
	
	public abstract void buildArgs();
	public abstract void buildPos();
	public abstract String uploadString();
}
