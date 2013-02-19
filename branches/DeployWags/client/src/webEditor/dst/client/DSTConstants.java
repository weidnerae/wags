package webEditor.dst.client;

public class DSTConstants {
	
	public static String CORRECT = "CORRECT";
		
	public static int TEXT_SIZE = 15;
	
	public static String INSERT_GROUP = "insertgroup";
	public static String INSERT_OTHER = "insertgroup";
	public static String CLEAR_GROUPS = "insertgroup";
	
	public static int PROMPT_X = 335;
	public static int PROMPT_Y = 150;
	
	public static int EDGE_PROMPT_X = 335;
	public static int EDGE_PROMPT_Y = 131;
	
	public static int SUBMIT_MESS_Y = 131;
	public static int SUBMIT_X = 335;
	
	public static String NODE = "node";
	public static String NODE_DRAGGABLE = "draggable";
	public static String NODE_CLICKABLE = "clickable";
	public static String NODE_CLICKABLE_FORCE_EVAL = "clickableforceeval";
	public static String NODE_STRING_DRAGGABLE ="stringdraggable";
	public static String NODE_RED_BLACK = "redblack";
	
	public static String INSERT_METHOD_VALUE = "byvalue";
	public static String INSERT_METHOD_VALUE_AND_LOCATION = "byvalueandlocation";
	public static String INSERT_METHOD_VALUE_LOCATION_COLOR = "byvaluelocationcolor";
	
	public static String TREE_MODE = "treemode";
	public static String NO_EDGE = "noedges";
	
	public static AddEdgeRules getEdgeRules(String edgeRules){
		if(edgeRules.equals("treemode")) return new AddEdgeRules_TreeMode();
		if(edgeRules.equals("noedges")) return new AddEdgeRules();
		
		return null;
	}
	
	// Correspond to enum values in LogicalMicrolabs table, evaluation column
	public static Evaluation getEvaluation(String eval){
		if(eval.equals("BST_postorder")) return new Evaluation_PostOrderBST();
		if(eval.equals("BST_preorder")) return new Evaluation_Preorder();
		if(eval.equals("Maxheap_level")) return new Evaluation_MaxHeap_Level();
		if(eval.equals("Minheap_level")) return new Evaluation_MinHeap_Level();
		if(eval.equals("Heapsort")) return new Evaluation_HeapSort();
		if(eval.equals("BST_level")) return new Evaluation_Level();
		if(eval.equals("Hashing")) return new Evaluation_Hashing();
		if(eval.equals("Quicksort")) return new Evaluation_Quicksort();
		if(eval.equals("Selectionsort")) return new Evaluation_SelectionSort();
		if(eval.equals("BST_redblack")) return new Evaluation_RedBlackTree();
		if(eval.equals("BST_traversal")) return new Evaluation_BSTTraversal();
		if(eval.equals("BST_traversal_help")) return new Evaluation_BSTTraversalWithHelp();
		if(eval.equals("MST")) return new Evaluation_MSTTraversal();
		if(eval.equals("Radix")) return new Evaluation_RadixSortWithHelp();
	
		return null;
	}
}
