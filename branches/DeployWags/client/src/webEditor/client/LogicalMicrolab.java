/* LogicalMicrolab
 * 
 * Philip Meznar '12
 * 
 * This class is used to create the Problems which are used in the 
 * DataStructureTool.  The constructor is given information in a very
 * raw form from the Database.  
 * 
 * When a problem is requested, the LogicalMicrolab checks it's "genre"
 * and uses this to parse parameters appropriately and return the
 * expected problem.
 * 
 * In short, this class and WEStatus are the new classes responsible for
 * what ProblemServiceImpl.java used to do
 */

package webEditor.client;

import com.google.gwt.user.client.Window;

import webEditor.dst.client.AddEdgeRules;
import webEditor.dst.client.DSTConstants;
import webEditor.dst.client.Evaluation;
import webEditor.dst.client.Problem;
import webEditor.dst.client.TreeProblem;

public class LogicalMicrolab {
	private String title, problemText, nodes, xPositions, yPositions,
		insertMethod, edges, nodeType, genre, arguments;
	private Evaluation evaluation;
	private AddEdgeRules edgeRules;
	private boolean edgesRemovable, nodesDraggable;
	private int group;
	
	public LogicalMicrolab(String title, String problemText, String nodes,
			String xPositions, String yPositions, String insertMethod,
			String edges, String evaluation, String edgeRules, String arguments,
			int edgesRemovable, int nodesDraggable, String nodeType, int group, String genre){
		
		this.title = title;
		this.problemText = problemText;
		this.nodes = nodes;
		this.xPositions = xPositions;
		this.yPositions = yPositions;
		this.insertMethod = insertMethod;
		this.edges = edges;
		this.evaluation = DSTConstants.getEvaluation(evaluation);
		this.edgeRules = DSTConstants.getEdgeRules(edgeRules);
		this.arguments = arguments;
		this.edgesRemovable = (edgesRemovable == 1)? true : false;
		this.nodesDraggable = (nodesDraggable == 1)? true : false;
		this.nodeType = nodeType;
		this.group = group;
		this.genre = genre;
	}
	
	public Problem getProblem(){
		if(genre.equals("traversal")){
			
			String[] args = new String[1];
			args[0] = arguments;
			
			return new TreeProblem(title, problemText, nodes,
					insertMethod, convertToIntArray(xPositions, ","),
					convertToIntArray(yPositions, ","), edges.split(","),
					args, evaluation, edgeRules, edgesRemovable,
					nodesDraggable, nodeType);
		}
		
		return null;
	}
	
	private int[] convertToIntArray(String list, String delim){
		String[] tmp = list.split(delim);
		int[] intArray = new int[tmp.length];
		
		for(int i = 0; i < tmp.length; i++){
			intArray[i] = Integer.parseInt(tmp[i]);
		}
		
		return intArray;
	}
	
}
