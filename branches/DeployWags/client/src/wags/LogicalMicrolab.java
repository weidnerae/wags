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

package wags;

import wags.logical.AddEdgeRules;
import wags.logical.DSTConstants;
import wags.logical.Evaluation;
import wags.logical.Problem;
import wags.logical.ProblemServiceImpl;
import wags.logical.HashingProblems.HashingProblem;
import wags.logical.QuickSortProblems.QuickSortProblem;
import wags.logical.RadixProblems.Evaluation_RadixSortWithHelp;
import wags.logical.RadixProblems.SearchProblem;
import wags.logical.SelectionSortProblems.SelectionSortProblem;
import wags.logical.SimplePartitionProblems.SimplePartitionProblem;
import wags.logical.TreeProblems.TreeProblem;
import wags.logical.TreeProblems.MSTProblems.MSTProblem;
import wags.logical.TreeProblems.RedBlackProblems.RedBlackProblem;

public class LogicalMicrolab {
	private String title, problemText, nodes, xPositions, yPositions,
		insertMethod, edges, nodeType, genre, arguments;
	private Evaluation evaluation;
	private AddEdgeRules edgeRules;
	private boolean edgesRemovable, nodesDraggable;
	private int group;
	
	public LogicalMicrolab(String title, String problemText, String nodes,
			String xPositions, String yPositions, String insertMethod,
			String edges, int evaluation, int edgeRules, String arguments,
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
		// Everything requires "args"
		String[] args = getArguments(arguments);
		
		if(genre.equals("radix")){
			return new SearchProblem(title, problemText, nodes, insertMethod,
					args, new Evaluation_RadixSortWithHelp(), nodesDraggable, nodeType);
		}
		if(genre.equals("selectionsort")){
			return new SelectionSortProblem(title, problemText, nodes, insertMethod,
					args, evaluation, edgeRules, nodesDraggable, nodeType);
		}
		if(genre.equals("simplepartition")){
			return new SimplePartitionProblem(title, problemText, nodes, insertMethod,
					args, evaluation, edgeRules, nodesDraggable, nodeType);
		}
		
		// Everything from here on requires edgeList
		String[] edgeList = getEdges(edges);
		
		// Only difference from heapDelete is status of boolean getting passed
		// to "getHeap_Location" methods
		if(genre.equals("heapInsert")){
			return new TreeProblem(title, problemText, nodes,
					insertMethod, ProblemServiceImpl.getHeapXLocations(true, nodes),
					ProblemServiceImpl.getHeapYLocations(true, nodes), edgeList, args,
					evaluation, edgeRules, edgesRemovable, nodesDraggable, nodeType);
		}
		if(genre.equals("heapDelete")){
			return new TreeProblem(title, problemText, nodes,
					insertMethod, ProblemServiceImpl.getHeapXLocations(false, nodes),
					ProblemServiceImpl.getHeapYLocations(false, nodes), edgeList, args,
					evaluation, edgeRules, edgesRemovable, nodesDraggable, nodeType);
		}
		
		// Everything form here on requires positions
		int[] xPos = getLocations(xPositions);
		int[] yPos = getLocations(yPositions);
		
		if(genre.equals("traversal")){	
			return new TreeProblem(title, problemText, nodes,
					insertMethod, xPos,
					yPos, edgeList,
					args, evaluation, edgeRules, edgesRemovable,
					nodesDraggable, nodeType);
		}
				
		if(genre.equals("mst")){
			return new MSTProblem(title, problemText, nodes, insertMethod,
					xPos, yPos, edgeList, args, evaluation, 
					edgeRules, edgesRemovable, nodesDraggable, nodeType);
		}
		
		if(genre.equals("hashing")){
			return new HashingProblem(title, problemText, nodes, insertMethod,
					xPos, yPos, edgeList, args, evaluation, edgeRules,
					edgesRemovable, nodesDraggable, nodeType);
		}
		
		if(genre.equals("qsort")){
			String[] qEdges = new String[0];
			return new QuickSortProblem(title, problemText, nodes, insertMethod,
					xPos, yPos, qEdges, args, evaluation, edgeRules,
					edgesRemovable, nodesDraggable, nodeType);
		}
		
		if(genre.equals("redblack")){
			return new RedBlackProblem(title, problemText, nodes, insertMethod,
					xPos, yPos, edgeList, args, evaluation, edgeRules, 
					edgesRemovable, nodesDraggable, nodeType);
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
	
	private String[] getEdges(String edges){
		String[] edgeList;
		if(edges.length() == 0){
			edgeList = new String[0];
		} else {
			edgeList = edges.split(",");
		}
		
		return edgeList;
	}
	
	private int[] getLocations(String pos){
		if(pos.length() > 0){
			return convertToIntArray(pos, ",");
		} else {
			return new int[0];
		}
	}
	
	private String[] getArguments(String arguments){
		String[] args;
		if(!arguments.contains(",")){
			args = new String[1];
			args[0] = arguments;
		} else {
			args = arguments.split(",");
		}
		
		return args;
	}
	
}
