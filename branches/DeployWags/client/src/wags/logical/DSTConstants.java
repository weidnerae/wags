package wags.logical;

import wags.logical.HashingProblems.Evaluation_Hashing;
import wags.logical.QuickSortProblems.Evaluation_Quicksort;
import wags.logical.RadixProblems.Evaluation_RadixSortWithHelp;
import wags.logical.SelectionSortProblems.Evaluation_SelectionSort;
import wags.logical.SimplePartitionProblems.Evaluation_SimplePartition;
import wags.logical.TreeProblems.Evaluation_BSTTraversal;
import wags.logical.TreeProblems.Evaluation_BSTTraversalWithHelp;
import wags.logical.TreeProblems.Evaluation_Level;
import wags.logical.TreeProblems.Evaluation_PostOrderBST;
import wags.logical.TreeProblems.Evaluation_Preorder;
import wags.logical.TreeProblems.HeapProblems.Evaluation_HeapSort;
import wags.logical.TreeProblems.HeapProblems.Evaluation_MaxHeap_Level;
import wags.logical.TreeProblems.HeapProblems.Evaluation_MaxHeap_Preorder;
import wags.logical.TreeProblems.HeapProblems.Evaluation_MinHeap_Level;
import wags.logical.TreeProblems.MSTProblems.Evaluation_MSTTraversal;
import wags.logical.TreeProblems.RedBlackProblems.Evaluation_RedBlackTree;

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
	
	// AddEdgeRules
	public final static int TREE_MODE_KEY = 1;
	public final static int NO_EDGES_KEY = 2;
	
	// Evaluations
	public final static int BST_POSTORDER_KEY = 1;
	public final static int BST_PREORDER_KEY = 2;
	public final static int MAXHEAP_LEVEL_KEY = 3;
	public final static int MINHEAP_LEVEL_KEY = 4;
	public final static int HEAPSORT_KEY = 5;
	public final static int BST_LEVEL_KEY = 6;
	public final static int HASHING_KEY = 7;
	public final static int QUICKSORT_KEY = 8;
	public final static int SELECTIONSORT_KEY = 9;
	public final static int BST_REDBLACK_KEY = 10;
	public final static int BST_TRAVERSAL_KEY = 11;
	public final static int BST_TRAVERSAL_HELP_KEY = 12;
	public final static int MST_KEY = 13;
	public final static int RADIX_KEY = 14;
	public final static int MAXHEAP_PREORDER_KEY = 15;
	public final static int SIMPLEPARTITION_KEY = 16;
	
	public static AddEdgeRules getEdgeRules(int edgeRules){
		switch(edgeRules){
		case TREE_MODE_KEY:
			return new AddEdgeRules_TreeMode();
		case NO_EDGES_KEY:
			return new AddEdgeRules();
		default:
			return null;
		}
	}
	
	// Correspond to enum values in LogicalMicrolabs table, evaluation column
	public static Evaluation getEvaluation(int eval){
		switch(eval){
		case BST_POSTORDER_KEY:
			return new Evaluation_PostOrderBST();
		case BST_PREORDER_KEY:
			return new Evaluation_Preorder();
		case MAXHEAP_LEVEL_KEY:
			return new Evaluation_MaxHeap_Level();
		case MINHEAP_LEVEL_KEY:
			return new Evaluation_MinHeap_Level();
		case HEAPSORT_KEY:
			return new Evaluation_HeapSort();
		case BST_LEVEL_KEY:
			return new Evaluation_Level();
		case HASHING_KEY:
			return new Evaluation_Hashing();
		case QUICKSORT_KEY:
			return new Evaluation_Quicksort();
		case SELECTIONSORT_KEY:
			return new Evaluation_SelectionSort();
		case BST_REDBLACK_KEY:
			return new Evaluation_RedBlackTree();
		case BST_TRAVERSAL_KEY:
			return new Evaluation_BSTTraversal();
		case BST_TRAVERSAL_HELP_KEY:
			return new Evaluation_BSTTraversalWithHelp();
		case MST_KEY:
			return new Evaluation_MSTTraversal();
		case RADIX_KEY:
			return new Evaluation_RadixSortWithHelp();
		case MAXHEAP_PREORDER_KEY:
			return new Evaluation_MaxHeap_Preorder();
		case SIMPLEPARTITION_KEY:
			return new Evaluation_SimplePartition();
		default:
			return null;	
		}
	}
}
