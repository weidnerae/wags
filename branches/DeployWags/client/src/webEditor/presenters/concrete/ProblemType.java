package webEditor.presenters.concrete;

public enum ProblemType {
	MAGNET_PROBLEM, LOGICAL_PROBLEM, DATABASE_PROBLEM;
	
	public static int TypeToVal(ProblemType type) {
		switch (type) { 
		case MAGNET_PROBLEM:
			return 0;
		case LOGICAL_PROBLEM: 
			return 1;
		case DATABASE_PROBLEM:
			return 2;
		default:
			return -1;
		}
	
	}
}

