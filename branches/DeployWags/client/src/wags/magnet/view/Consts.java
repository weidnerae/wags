package wags.magnet.view;

/**
 * 
 * These constants define a multitude of things inside the magnets themselves
 * and for peripheral magnet utilities such as the creation station.  
 * 
 * 
 */

public class Consts {
	
	public static String ALGORITHM_PROBLEM = "algorithm_problem";
	public static String BASIC_PROBLEM = "basic_problem";
	public static String ADVANCED_PROBLEM = "advanced_problem";
	
	
	// Position inside block
	public static String CONDITION = "<!-- condition -->";
	public static String PANEL_TAG = "<!-- panel -->";
	
	// Types of Stackable Containers, used in constructor
	public static final int MAIN = 1;
	public static final int INNER = 2;
	public static final int STATEMENT = 3;
	public static final int COMMENT = 4;
	
	public static String TOPANYORDER = "<!-- topanyorder -->";
	public static String BOTTOMANYORDER = "<!-- bottomanyorder -->";
	
	public static String HIDE_START = "<!-- hideStart -->";
	public static String HIDE_END = "<!-- hideEnd -->";
	public static String HIDDEN_CODE = "<!-- hiddenCode -->";
	public static String HC_DELIMITER = "BRIGGADIGGA";
	
	// Used to surround the java code related to that magnet that will be compiled
	public static String CODE_START = "<!-- codeStart -->";
	public static String CODE_SPLIT = "<!-- codeSplit -->";
	public static String CODE_END ="<!-- codeEnd -->";
	
	// Major Delimiters
	public static String COMMENT_DELIMITER = ".:2:.";
	public static String MAGNET_DELIMITER = ".:|:.";

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
	public static String FUNCTION = "{<br /><span id=\"inside_of_block\">" + Consts.PANEL_TAG +  "</span><br />}";
	public static String WHILE = "while (" + Consts.CONDITION
			+ ") {<br /><span id=\"inside_of_block\">" + Consts.PANEL_TAG + "</span><br />}";
	public static String FOR = "for (" + Consts.CONDITION
			+ ") {<br /><span id=\"inside_of_block\">" + Consts.PANEL_TAG + "</span><br />}";
	public static String IF = "if (" + Consts.CONDITION
			+ ") {<br /><span id=\"inside_of_block\">" + Consts.PANEL_TAG + "</span><br />}";

	// ELSE doesn't have conditions associated with it
	public static String ELSE = "else {<br /><span id=\"inside_of_block\">"
			+ Consts.PANEL_TAG+ "</span><br />}";
	public static String ELSEIF = "else if (" + Consts.CONDITION
			+ ") {<br /><span id=\"inside_of_block\">" + Consts.PANEL_TAG + "</span><br />}";
	
	// Box to designate that the containers within will be valid in any order in an Algritm problem
	public static String ANYORDERBOX = "The containers contained within this box can be submitted in any vertical order to be considered correct in an Algoritm Problem."+Consts.TOPANYORDER+"<br /><span id=\"inside_of_block\">"
			+ Consts.PANEL_TAG + "</span>"+Consts.BOTTOMANYORDER+"<br />}";
}
