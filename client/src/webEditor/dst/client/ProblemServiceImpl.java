package webEditor.dst.client;

public class ProblemServiceImpl 
{	
	public static Problem getProblem(String problem) {
		AddEdgeRules_TreeMode rules = new AddEdgeRules_TreeMode();
		Evaluation_PostOrderBST eval = new Evaluation_PostOrderBST();
		Evaluation_Preorder preEval = new Evaluation_Preorder();
		
		AddEdgeRules noEdgeAddition = new AddEdgeRules();
		Evaluation_BSTTraversal trav = new Evaluation_BSTTraversal();
		Evaluation_BSTTraversalWithHelp travHelp = new Evaluation_BSTTraversalWithHelp();
		Evaluation_RadixSortWithHelp radix = new Evaluation_RadixSortWithHelp();
		Evaluation_MaxHeap_Preorder preHeap = new Evaluation_MaxHeap_Preorder();
		
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
					new int[][]{{10, 70, 130, 190, 250, 310, 370, 430, 490, 550}, {250, 190, 310, 190, 550, 190, 550, 190, 490, 430},
								{250, 10, 310, 70, 490, 130, 550, 190, 430, 370}, {190, 250, 190, 250, 430, 130, 10, 250, 130, 370},
								{190, 310, 250, 370, 550, 70, 10, 430, 130, 490}, {370, 490, 130, 370, 10, 490, 10, 310, 250, 10}},
					new int[][]{{7, 7, 7, 7, 7, 7, 7, 7, 7, 7}, {80, 80, 80, 120, 80, 160, 120, 200, 80, 80},
								{7, 7, 7, 7, 7, 7, 7, 7, 7, 7}, {80, 80, 120, 120, 80, 80, 80, 160, 120, 80},
								{7, 7, 7, 7, 7, 7, 7, 7, 7, 7}, {80, 120, 80, 120, 160, 80, 80, 80, 80, 120}},
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
					new String[]{"9843127631529","12 29 76 48 98 3 43 15"},
					preHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 20: return new TreeProblem("MaxHeap Insertion",
					"MaxHeap Insertion Skeleton",
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
		
		return 0;
	}
	
}
