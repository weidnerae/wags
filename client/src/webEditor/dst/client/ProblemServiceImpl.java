package webEditor.dst.client;

public class ProblemServiceImpl 
{	
	public static Problem getProblem(String problem) {
		AddEdgeRules_TreeMode rules = new AddEdgeRules_TreeMode();
		Evaluation_PostOrderBST eval = new Evaluation_PostOrderBST();
		Evaluation_Preorder preEval = new Evaluation_Preorder();
		Evaluation_MaxHeap_Preorder preHeap = new Evaluation_MaxHeap_Preorder();
		
		AddEdgeRules noEdgeAddition = new AddEdgeRules();
		Evaluation_BSTTraversal trav = new Evaluation_BSTTraversal();
		Evaluation_BSTTraversalWithHelp travHelp = new Evaluation_BSTTraversalWithHelp();
		Evaluation_RadixSortWithHelp radix = new Evaluation_RadixSortWithHelp();

		
		int[] noLocs = new int[0]; //used as a placeholder for problems with no preset locations
		String[] noEdges = new String[0]; //used as a placeholder for problems with no preset edges
		
		
		int id = getProblemId(problem);
 		
		switch(id)
		{
			case 0: return new TreeProblem("BST Preorder Traversal (Help on)",
					"Perform a preorder traversal of the binary tree below by clicking" +
					" nodes in the order the traversal would visit them.",
					"A B C D E F G",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{250,150,350,75,225,275,425},
					new int[]{75,175,175,275,275,275,275},
					new String[]{"A B","B D","B E","C F","C G", "A C"},
					new String[]{"ABDECFG"},
					travHelp,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_CLICKABLE_FORCE_EVAL);
			case 1: return new TreeProblem("BST Inorder Traversal (Help on)",
					"Perform an inorder traversal of the binary tree below by clicking" +
					" nodes in the order the traversal would visit them.",
					"A B C D E F G",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{250,150,350,75,225,275,425},
					new int[]{75,175,175,275,275,275,275},
					new String[]{"A B","B D","B E","C F","C G", "A C"},
					new String[]{"DBEAFCG"},
					travHelp,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_CLICKABLE_FORCE_EVAL);
			case 2: return new TreeProblem("BST Postorder Traversal (Help on)",
					"Perform a postorder traversal of the binary tree below by clicking" +
					" nodes in the order the traversal would visit them.",
					"A B C D E F G",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{250,150,350,75,225,275,425},
					new int[]{75,175,175,275,275,275,275},
					new String[]{"A B","B D","B E","C F","C G", "A C"},
					new String[]{"DEBFGCA"},
					travHelp,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_CLICKABLE_FORCE_EVAL);
			case 3: return new TreeProblem("BST Preorder Traversal (Help off)",
					"Perform a preorder traversal of the binary tree below by clicking" +
					" nodes in the order the traversal would visit them.",
					"X R Q D H J M Z L",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{250,150,350,100,200,300,450,50,400},
					new int[]{75,175,175,275,275,275,275,375,375},
					new String[]{"X R","R D","D Z","R H","X Q", "Q J", "Q M", "M L"},
					new String[]{"XRDZHQJML"},
					trav,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_CLICKABLE);
			case 4: return new TreeProblem( "BST Inorder Traversal (Help off)",
					"Perform an inorer traversal of the binary tree below by clicking" +
					" nodes in the order the traversal would visit them.",
					"M C L P Q N T S D",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{250,200,350,50,300,400,100, 450, 150},
					new int[]{75,175,175,275,275,275,375, 375, 475},
					new String[]{"M C","C P","P T","T D","M L", "L Q", "L N", "N S"},
					new String[]{"PTDCMQLNS"},
					trav,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_CLICKABLE);
			case 5: return new TreeProblem("BST Postorder Traversal (Help off)",
					"Perform a postorder traversal of the binarytree below by clicking" +
					" nodes in the order the traversal would visit them.",
					"A R J M L Q Z N D",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{250,150,350,100,200,300,400,50,450},
					new int[]{75,175,175,275,275,275,275,375,375},
					new String[]{"A R","R M","M N","A J","R L","J Q","J Z","Z D"},
					new String[]{"NMLRQDZJA"},
					trav,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_CLICKABLE);
			case 6: return new TreeProblem("Insert Nodes into a BST 1",
					"Given the following BST, insert the following " +
					"nodes in order to retain a BST structure." +
					"\nHint: Add the top node first, and work your way down",
					"Q H V M T B P W",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"QHBMPVTW", "BHMPQTVW"}, //pre, in
					preEval,
					rules,
					true,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 7: return new TreeProblem("Insert Nodes into a BST 2",
					"Given the following BST, insert the following " +
					"nodes in order to retain a BST structure." +
					"\nHint: Add the top node first, and work your way down",
					"O I V N R Z F D P",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"OIFDNVRPZ", "DFINOPRVZ"}, //pre, in
					preEval,
					rules,
					true,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 8: return new TreeProblem("Insert Nodes into a BST 3",
					"Given the following BST, insert the following " +
					"nodes in order to retain a BST structure." +
					"\nHint: Add the top node first, and work your way down",
					"P H Q G N K O R S",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"PHGNKOQRS", "GHKNOPQRS"}, //pre, in
					preEval,
					rules,
					true,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 9: return new TreeProblem("Insert Nodes into a BST 4",
					"Given the following BST, insert the following " +
					"nodes in order to retain a BST structure." +
					"\nHint: Add the top node first, and work your way down",
					"P H Q G S R K O N",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"PHGKONQSR", "GHKNOPQRS"}, //pre, in
					preEval,
					rules,
					true,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 10: return new TreeProblem("Binary Search Tree from Postorder Traversal 1",
					"Given the postorder traversal DBHJFPTUSM, " +
					"construct the original binary search tree.  " +
					"Hint: The binary search tree is unique.",
					"D B H J F P T U S M",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"DBHJFPTUSM", "BDFHJMPSTU"},
					eval,
					rules,
					true,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 11: return new TreeProblem("Binary Search Tree from Postorder Traversal 2",
					"Given the postorder traversal, DAJELMONK, " +
					"construct the original binary search tree.  " +
					"Hint: The binary search tree is unique.",
					"D A J E L M O N K",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"DAJELMONK", "ADEJKLMNO"},
					eval,
					rules,
					true,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 12: return new TreeProblem("Binary Search Tree from Postorder Traversal 3",
					"Given the postorder traversal BAOMFRXZUP, " +
					"construct the original binary search tree.  " +
					"Hint: The binary search tree is unique.",
					"B A O M F R X Z U P",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"BAOMFRXZUP", "ABFMOPRUXZ"},
					eval,
					rules,
					true,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 13: return new TreeProblem("Binary Search Tree from Postorder Traversal 4",
					"Given the postorder traversal BEAKJHTRQ, " +
					"construct the original binary search tree.  " +
					"Hint: The binary search tree is unique.",
					"B E A K J H T R Q",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"BEAKJHTRQ", "ABEHJKQRT"},
					eval,
					rules,
					true,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 14: return new TreeProblem("Binary Tree from Pre/Inorder Traversals 1",
					"Given the preorder traversal XDMLTKJ, and " +
					"the inorder traversal MDLXKTJ " +
					"construct the original binary tree.  " +
					"Hint: The binary tree is unique.",
					"D J K L M T X",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"XDMLTKJ", "MDLXKTJ"}, //post MLDKJTX
					preEval,
					rules,
					true,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 15: return new TreeProblem("Binary Tree from Pre/Inorder Traversals 2",
					"Given the preorder traversal EXPORALS, and " +
					"the inorder traversal OPXELARS " +
					"construct the original binary tree.  " +
					"Hint: The binary tree is unique.",
					"A E L O P R S X",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"EXPORALS", "OPXELARS"},  //post OPXLASRE
					preEval,
					rules,
					true,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 16: return new TreeProblem("Binary Tree from Pre/Inorder Traversals 3",
					"Given the preorder traversal PDLSOQNTB, and " +
					"the inorder traversal SLODQPNTB " +
					"construct the original binary tree.  " +
					"Hint: The binary tree is unique.",
					"B D L N O P Q S T",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"PDLSOQNTB", "SLODQPNTB"}, //post SOLQDBTNP
					preEval,
					rules,
					true,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 17: return new TreeProblem("Binary Tree from Pre/Inorder Traversals 4",
					"Given the preorder traversal DLGNRAOPETM, and " +
					"the inorder traversal GNRLADPEOTM " +
					"construct the original binary tree.  " +
					"Hint: The binary tree is unique.",
					"A D E G L M N O P R T",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"DLGNRAOPETM", "GNRLADPEOTM"}, //post RNGALEPMTOD
					preEval,
					rules,
					true,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 18: return new SearchProblem("RADIX Sort",
					"A queue of data values is shown at the top of the display. " +
					"Using the given digit position move each value to the " + 
					"appropriate bucket. Each bucket is a queue structure with " + 
					"the front below the label and the rear at the bottom of the screen.",
                    "634 843 235 643 79 823 9 543 428 67",
                    DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
                    new int[][]{{0, 60, 120, 180, 240, 300, 360, 420, 480, 540}, {240, 180, 300, 180, 540, 180, 540, 180, 480, 420}, 
								{240, 0, 300, 60, 480, 120, 540, 180, 420, 360}, {180, 240, 180, 240, 420, 120, 0, 240, 120, 360}, 
								{180, 300, 240, 360, 540, 60, 0, 420, 120, 480}, {360, 480, 120, 360, 0, 480, 0, 300, 240, 0}},
					new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {73, 73, 73, 113, 73, 153, 113, 193, 73, 73}, 
								{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {73, 73, 113, 113, 73, 73, 73, 153, 113, 73}, 
								{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {73, 113, 73, 113, 153, 73, 73, 73, 73, 113}},
                    new String[]{"0004110112", "1022301100", "3010112020", "84364382354363423567428799", 
								 "98234286342358436435436779", "96779235428543634643823843"},       
                    radix,
                    true,
                    DSTConstants.NODE_STRING_DRAGGABLE);
			case 19: return new TreeProblem("MaxHeap",
					"MaxHeap Skeleton",
					"76 98 43 12 48 3 15 29",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
				    noEdges,
					new String[]{"122976489834315"},
					preHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 20: return new TreeProblem("MaxHeap Insertion",
					"MaxHeap Insertion Skeleton, Use the bubble up technique to insert the node 57 into the heap.",
					"98 80 63 48 39 32 22 19 78",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{250,150,350,75,225,275,425,15,20},
					new int[]{75,175,175,275,275,275,275,350,20},
					new String[]{"98 80","98 63","80 48","80 39","63 22","63 32","48 19"},
					new String[]{"197848803998326322"},
					preHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 21: return new TreeProblem("MaxHeap Deletion",
					"MaxHeap Deletion Skeleton, Use the bubble down technique to remove the node 97 from the heap.",
					"97 80 63 48 39 32 22 19",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{250,150,350,75,225,275,425,15},
					new int[]{75,175,175,275,275,275,275,350},
					new String[]{"97 80","97 63","80 48","80 39","63 22","63 32","48 19"},
					new String[]{"48803963193222"},
					preHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_DRAGGABLE);
			
		default:
			return new TreeProblem("Binary Search Tree from Postorder Traversal 1",
					"Given the postorder traversal DBHJFPTUSM, " +
					"construct the original binary search tree.  " +
					"Hint: The binary search tree is unique.",
					"DBHJFPTUSM",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"DBHJFPTUSM", "BDFHJMPSTU"},
					eval,
					rules,
					true,
					true,
					DSTConstants.NODE_DRAGGABLE);
		}
	}	
	
	private static int getProblemId(String problem){
		if(problem.equals("BST Preorder Traversal (Help on)")) return 0;
		if(problem.equals("BST Inorder Traversal (Help on)")) return 1;
		if(problem.equals("BST Postorder Traversal (Help on)")) return 2;
		if(problem.equals("BST Preorder Traversal (Help off)")) return 3;
		if(problem.equals("BST Inorder Traversal (Help off)")) return 4;
		if(problem.equals("BST Postorder Traversal (Help off)")) return 5;
		if(problem.equals("Insert Nodes into a BST 1")) return 6;
		if(problem.equals("Insert Nodes into a BST 2")) return 7;
		if(problem.equals("Insert Nodes into a BST 3")) return 8;
		if(problem.equals("Insert Nodes into a BST 4")) return 9;
		if(problem.equals("Binary Search Tree from Postorder Traversal 1")) return 10;
		if(problem.equals("Binary Search Tree from Postorder Traversal 2")) return 11;
		if(problem.equals("Binary Search Tree from Postorder Traversal 3")) return 12;
		if(problem.equals("Binary Search Tree from Postorder Traversal 4")) return 13;
		if(problem.equals("Binary Tree from Pre/Inorder Traversals 1")) return 14;
		if(problem.equals("Binary Tree from Pre/Inorder Traversals 2")) return 15;
		if(problem.equals("Binary Tree from Pre/Inorder Traversals 3")) return 16;
		if(problem.equals("Binary Tree from Pre/Inorder Traversals 4")) return 17;
		if(problem.equals("RADIX Sort")) return 18;
		if(problem.equals("MaxHeap")) return 19;
		if(problem.equals("MaxHeap Insertion")) return 20;
		if(problem.equals("MaxHeap Deletion")) return 21;
		
		return 0;
	}
	
}
