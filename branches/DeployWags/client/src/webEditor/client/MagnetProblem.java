package webEditor.client;

public class MagnetProblem {
	public int id;
	public String title, directions, mainFunction, solution, type;
	public boolean creationStation;
	public String[] innerFunctions, forLeft, forMid, forRight, bools, statements;
	
	public MagnetProblem(int id, String title, String directions, String type, boolean creationStation, String mainFunction,
			String[] innerFunctions, String[] forLeft, String[] forMid, String[] forRight, String[] bools, String[] statements,
			String solution){
		this.id = id;
		this.title = title;
		this.directions = directions;
		this.type = type;
		this.creationStation = creationStation;
		this.mainFunction = mainFunction;
		this.innerFunctions = innerFunctions;
		this.forLeft = forLeft;
		this.forRight = forRight;
		this.forMid = forMid;
		this.bools = bools;
		this.statements = statements;
		this.solution = solution;
	}
}
