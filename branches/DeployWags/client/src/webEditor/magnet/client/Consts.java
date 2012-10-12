package webEditor.magnet.client;

/**
 * Constants that are used to build the code snippets.
 * 
 * @author Daniel Cook, Alex Weidner, Reed Phillips
 * 
 *         Refrigerator Magnet Microlab CS 3460 - Kurtz
 * @version r28
 */
public interface Consts {
	
	public static String ALGORITHM_PROBLEM = "algorithm_problem";
	public static String BASIC_PROBLEM = "basic_problem";
	public static String ADVANCED_PROBLEM = "advanced_problem";
	// Special conditions for StackableContainer constructor
	public static String MAIN = "main";
	public static String NONDRAGGABLE = "nondraggable";
	public static String INSIDE_COMMENT = "inside_comment";

	// Position inside block
	public static String TOP = "<!-- top -->";
	public static String INSIDE = "<!-- inside -->";
	public static String BOTTOM = "<!-- bottom -->";
	public static String CONDITION = "<!-- condition -->";
	
	public static String TOPANYORDER = "<!-- topanyorder -->";
	public static String BOTTOMANYORDER = "<!-- bottomanyorder -->";

	// *** STANDARDS ***//
	//default structures list
	public static String[] STRUCTURES_LIST = {"choose structure...","for","while","if","else if", "else"};
	//default modified structures list used for inner Functions
	public static String[] INNER_FUNCTIONS_STRUCTURES_LIST = {"choose structure...","for","while","if","else if", "else","statement","function"};
	//default modified structures list used for premade segments
	public static String[] PREMADE_SEGMENTS_STRUCTURES_LIST = {"choose structure...","for","while","if","else if", "else","statement"};
	public static String[] ALGORITHM_SOLUTIONS_STRUCTURES_LIST = {"choose structure...","for","while","if","else if", "else","statement", "ANY ORDER BOX"};
	
	//for choosing condition type
	public static String[] CONDITION_TYPES = {"choose condition type...","for","boolean"};

	// Blocks of Code
	public static String FUNCTION = "{<br /><span id=\"inside_of_block\">" + Consts.TOP
			+ Consts.INSIDE + Consts.BOTTOM + "</span><br />}";
	public static String WHILE = "while (" + Consts.CONDITION
			+ ") {<br /><span id=\"inside_of_block\">" + Consts.TOP
			+ Consts.INSIDE + Consts.BOTTOM + "</span><br />}";
	public static String FOR = "for (" + Consts.CONDITION
			+ ") {<br /><span id=\"inside_of_block\">" + Consts.TOP
			+ Consts.INSIDE + Consts.BOTTOM + "</span><br />}";
	public static String IF = "if (" + Consts.CONDITION
			+ ") {<br /><span id=\"inside_of_block\">" + Consts.TOP
			+ Consts.INSIDE + Consts.BOTTOM + "</span><br />}";

	// ELSE doesn't have conditions associated with it
	public static String ELSE = "else {<br /><span id=\"inside_of_block\">"
			+ Consts.TOP + Consts.INSIDE + Consts.BOTTOM + "</span><br />}";
	public static String ELSEIF = "else if (" + Consts.CONDITION
			+ ") {<br /><span id=\"inside_of_block\">" + Consts.TOP
			+ Consts.INSIDE + Consts.BOTTOM + "</span><br />}";
	
	// Box to designate that the containers within will be valid in any order in an Algritm problem
	public static String ANYORDERBOX = "The containers contained within this box can be submitted in any vertical order to be considered correct in an Algoritm Problem."+Consts.TOPANYORDER+"<br /><span id=\"inside_of_block\">"
			+ Consts.TOP + Consts.INSIDE + Consts.BOTTOM + "</span>"+Consts.BOTTOMANYORDER+"<br />}";
}
