package webEditor.dst.client;

public class ProblemServiceImpl 
{	
	public static Problem getProblem(String problem) {
		AddEdgeRules_TreeMode rules = new AddEdgeRules_TreeMode();
		Evaluation_PostOrderBST eval = new Evaluation_PostOrderBST();
		Evaluation_Preorder preEval = new Evaluation_Preorder();
		Evaluation_MaxHeap_Preorder preHeap = new Evaluation_MaxHeap_Preorder();
		Evaluation_Level level = new Evaluation_Level();
		
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
					"Perform an inorder traversal of the binary tree below by clicking" +
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
					new String[]{"Q H B M P V T W", "B H M P Q T V W"}, //pre, in
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
					new String[]{"O I F D N V R P Z", "D F I N O P R V Z"}, //pre, in
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
					new String[]{"P H G N K O Q R S", "G H K N O P Q R S"}, //pre, in
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
					new String[]{"P H G K O N Q S R", "G H K N O P Q R S"}, //pre, in
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
					new String[]{"D B H J F P T U S M", "B D F H J M P S T U"},
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
					new String[]{"D A J E L M O N K", "A D E J K L M N O"},
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
					new String[]{"B A O M F R X Z U P", "A B F M O P R U X Z"},
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
					new String[]{"B E A K J H T R Q", "A B E H J K Q R T"},
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
					new String[]{"X D M L T K J", "M D L X K T J"}, //post MLDKJTX
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
					new String[]{"E X P O R A L S", "O P X E L A R S"},  //post OPXLASRE
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
					new String[]{"P D L S O Q N T B", "S L O D Q P N T B"}, //post SOLQDBTNP
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
					new String[]{"D L G N R A O P E T M", "G N R L A D P E O T M"}, //post RNGALEPMTOD
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
					"Insert the nodes into a MaxHeap. Hint: you may have to use the" +
					" bubble up technique.",
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
					"MaxHeap Insertion. Use the bubble up technique to insert the node 78 into the heap.",
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
					"MaxHeap Deletion. Use the bubble down technique to remove the node 97 from the heap.",
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
			case 22: return new TreeProblem("BST Level Traversal 1",
					"Perform a level traversal of the binary tree below by clicking" +
					" nodes in the order the traversal would visit them.",
					"A B C D E F G",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{250,150,350,75,225,275,425},
					new int[]{75,175,175,275,275,275,275},
					new String[]{"A B","B D","B E","C F","C G", "A C"},
					new String[]{"ABCDEFG"},
					trav,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_CLICKABLE);
			case 23: return new TreeProblem("BST Level Traversal 2",
					"Perform a Level traversal of the binary tree below by clicking" +
					" nodes in the order the traversal would visit them.",
					"F C M P Q N K S Y",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{250,200,350,50,300,400,100, 450, 150},
					new int[]{75,175,175,275,275,275,375, 375, 475},
					new String[]{"F C","C P","P K","K Y","F M", "M Q", "M N", "N S"},
					new String[]{"FCMPQNKSY"},
					trav,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_CLICKABLE);
			case 24: return new TreeProblem( "BST Level Traversal 3",
					"Perform a Level traversal of the binary tree below by clicking" +
					" nodes in the order the traversal would visit them.",
					"V N A L F C G W Q K",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{250,150,350,75,225,425,175,375,475,425},
					new int[]{50,150,150,250,250,250,350,350,350,450},
					new String[]{"V N","V A","N L","N F","F G","A C","C W", "C Q","Q K"},
					new String[]{"VNALFCGWQK"},
					trav,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_CLICKABLE);
			case 25: return new TreeProblem("Binary Search Tree from Level Traversal 1",
					"Construct a binary search tree that would have" +
					" a Level traversal of QWERTYU. There are several correct solutions.",
					"W E U Q T R Y",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"QWERTYU"},
					level,
					rules,
					true,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 26: return new TreeProblem("Binary Search Tree from Level Traversal 2",
					"Construct a binary search tree that would have" +
					" a Level traversal of YHAOLKD. There are several correct solutions.",
					"O H A D Y K L",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"YHAOLKD"},
					level,
					rules,
					true,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 27: return new TreeProblem("Binary Search Tree from Level Traversal 3",
					"Construct a binary search tree that would have" +
					" a Level traversal of HEZXPOISD. There are several correct solutions.",
					"O E D X H S I Z P",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"HEZXPOISD"},
					level,
					rules,
					true,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 28: return new TreeProblem("Binary Search Tree from Level Traversal 4",
					"Construct a binary search tree that would have" +
					" a Level traversal of KLAVJISOBE. There are several correct solutions.",
					"K E L B V J S I A O",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"KLAVJISOBE"},
					level,
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
		if(problem.equals("BST Level Traversal 1")) return 22;
		if(problem.equals("BST Level Traversal 2")) return 23;
		if(problem.equals("BST Level Traversal 3")) return 24;
		if(problem.equals("Binary Search Tree from Level Traversal 1")) return 25;
		if(problem.equals("Binary Search Tree from Level Traversal 2")) return 26;
		if(problem.equals("Binary Search Tree from Level Traversal 3")) return 27;
		if(problem.equals("Binary Search Tree from Level Traversal 4")) return 28;
		
		return 0;
	}
	
}
