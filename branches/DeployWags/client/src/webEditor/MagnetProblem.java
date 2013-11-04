package webEditor;

/**
 * The MagnetProblem class is in essence a wrapper for all of the fields which make up
 * a magnet problem. Each MagnetProblem object holds the information for an entire problem, 
 * including all options for provided magnets, instructor provided descriptions, and current
 * state.  
 */

public class MagnetProblem {
	public int id, numStatements;
	public String title, directions, mainFunction, solution, type, state, limits;
	public boolean creationStation;
	public String[] innerFunctions, forLeft, forMid, forRight, ifOptions, whileOptions, returnOptions, assignmentVars,
					assignmentVals, statements, createdIDs;
	
	public MagnetProblem(int id, String title, String directions, String type, String mainFunction,
			String[] innerFunctions, String[] forLeft, String[] forMid, String[] forRight, String[] ifOptions, String[] whileOptions, String[] returnOptions,
			String[] assignmentVars, String[] assignmentVals, String[] statements, String limits, String[] createdIDs, int numStatements, String solution, String state){
		this.id = id;
		this.title = title;
		this.directions = directions;
		this.type = type;
		this.mainFunction = mainFunction;
		this.innerFunctions = innerFunctions;
		this.forLeft = forLeft;
		this.forRight = forRight;
		this.forMid = forMid;
		this.ifOptions = ifOptions;
		this.whileOptions = whileOptions;
		this.returnOptions = returnOptions;
		this.assignmentVars = assignmentVars;
		this.assignmentVals = assignmentVals;
		if(statements.length == 1 && statements[0].length() == 0) {
		  this.statements = null;
		} else {
		  this.statements = statements;
		}
		this.limits = limits;
		this.solution = solution;
		this.state = state;
		this.numStatements = numStatements;
		this.createdIDs = createdIDs;
	}
}

