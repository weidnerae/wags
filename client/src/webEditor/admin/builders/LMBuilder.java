package webEditor.admin.builders;

import java.util.ArrayList;

import webEditor.Proxy;
import webEditor.logical.DSTConstants;

import com.google.gwt.user.client.Window;


public class LMBuilder {
	private static final int XPOS = 0;
	private static final int YPOS = 1;
	
	private String title, problemText, arguments = "";
	private boolean edgesRemovable, nodesDraggable;
	private int evaluation, edgeRules, groupId;
	private ArrayList<String> nodes, edges;
	private String[][] positions;
	
	private InsertMethod insertMethod;
	private NodeType nodeType;
	Genre genre;
	
	
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
		this.evaluation = -1;
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
	
	private String listToString(ArrayList<String> aList){
		String str = "";
		
		for(int i = 0; i < aList.size(); i++){
			str += aList.get(i) + ","; 
		}
		
		// remove final ","
		return str.substring(0, str.length()-1);
	}
	
	private String listToString(String[] aList){
		String str = "";
		
		for(int i = 0; i < aList.length; i++){
			str += aList[i] + ",";
		}
		
		// Again, remove final ","
		return str.substring(0, str.length()-1);
	}
	
	private boolean validateMe(){
		if(this.title == ""){
			Window.alert("Empty title!");
			return false;
		}
		
		if(this.problemText == ""){
			Window.alert("Empty description!");
			return false;
		}
		
		if(this.nodes.size() == 0){
			Window.alert("No nodes!");
			return false;
		}
		//THIS PROBABLY NEEDS TO BE MODIFIED FOR MST PROBLEMS
		Window.alert("About to check pos");
		if(!this.genre.equals(Genre.MST) && this.positions[XPOS].length != this.nodes.size() && 
				this.insertMethod != InsertMethod.BY_VALUE){
			Window.alert("Positioning bug discovered!" + this.insertMethod + InsertMethod.BY_VALUE);
			return false;
		}
		Window.alert("About to check edges");
		// NO_EDGES_KEY actually means "no edge addition"
		if(this.edges.size() == 0 && this.edgeRules == DSTConstants.NO_EDGES_KEY){
			Window.alert("No edges!");
			return false;
		}
		Window.alert("About to check length");
		if(this.arguments.length() == 0){
			Window.alert("No arguments given to check solution!");
			return false;
		}
		Window.alert("About to check eval");
		if(this.evaluation == -1){
			Window.alert("No evaluation defined!");
			return false;
		}
		
		return true;
	}
	
	private void reset(){
		nodes.clear();
		edges.clear();
		arguments = "";
	}
	
	public void uploadLM(boolean debug){
		Window.alert("Inside uploadLM in LMBuilder class");
		if(!validateMe()){
			reset();
			Window.alert("We're in validate");
			return;
		}
		// Get title, description, nodes
		String str = "";
		Window.alert("We're in");
		// Convert positions to strings
		String nodes = listToString(this.nodes);
		Window.alert("nodes: " + nodes);
		nodes = nodes.replace(',', ' ');
		
			String xPos = listToString(positions[XPOS]);
			String yPos = listToString(positions[YPOS]);
			Window.alert("xPos/yPos = " + xPos + " " + yPos);
			
			String edgStr = listToString(edges);
			Window.alert("edgStr " + edgStr);
			String args = this.arguments;
			Window.alert("args: " + args);
			int edgeRem = (this.edgesRemovable) ? 1 : 0;
			Window.alert("edgeRem " + edgeRem);
			
			int nodesDrag = (this.nodesDraggable) ? 1 : 0;
			Window.alert("nodesDrag " + nodesDrag);
			str = "&title=" + this.title + "&problemText=" + this.problemText 
					+ "&nodes=" + nodes + "&edges=" + edgStr 
					+ "&xPositions=" + xPos + "&yPositions=" + yPos + "&insertMethod="  
					+ this.insertMethod + "&evaluation=" + this.evaluation
					+ "&edgeRules=" + this.edgeRules + "&arguments=" 
					+ args + "&edgesRemovable=" + edgeRem
					+ "&nodesDraggable=" + nodesDrag + "&nodeType=" + this.nodeType
					+ "&genre=" + this.genre + "&group=" + this.groupId;
		
		if(debug) Window.alert(str);
		else {
			Window.alert("Server being called");
			reset();  // All info is stored in string, so can reset before Proxy call
			Proxy.uploadLogicalMicrolab(str);
		}
	}
	
	public void uploadLM(){
		uploadLM(false);
	}
}
