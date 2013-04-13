package webEditor;

public class MagnetProblem {
	public int id, numStatements;
	public String title, directions, mainFunction, solution, type, state, limits;
	public boolean creationStation;
	public String[] innerFunctions, forLeft, forMid, forRight, bools, statements, createdIDs;
	
	public MagnetProblem(int id, String title, String directions, String type, String mainFunction,
			String[] innerFunctions, String[] forLeft, String[] forMid, String[] forRight, String[] bools, String[] statements, 
			String limits, String[] createdIDs, int numStatements, String solution, String state){
		this.id = id;
		this.title = title;
		this.directions = directions;
		this.type = type;
		this.mainFunction = mainFunction;
		this.innerFunctions = innerFunctions;
		this.forLeft = forLeft;
		this.forRight = forRight;
		this.forMid = forMid;
		this.bools = bools;
		this.statements = statements;
		this.limits = limits;
		this.solution = solution;
		this.state = state;
		this.numStatements = numStatements;
		this.createdIDs = createdIDs;
	}
}

