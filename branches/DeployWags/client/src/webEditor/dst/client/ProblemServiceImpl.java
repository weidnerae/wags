package webEditor.dst.client;

public class ProblemServiceImpl 
{	
	public static Problem getProblem(String problem) {
		AddEdgeRules_TreeMode rules = new AddEdgeRules_TreeMode();
		Evaluation_PostOrderBST eval = new Evaluation_PostOrderBST();
		Evaluation_Preorder preEval = new Evaluation_Preorder();
		Evaluation_MaxHeap_Level levelHeap = new Evaluation_MaxHeap_Level();
		Evaluation_MinHeap_Level minLevelHeap = new Evaluation_MinHeap_Level();
		Evaluation_HeapSort heapSort = new Evaluation_HeapSort();
		Evaluation_Level level = new Evaluation_Level();
		Evaluation_Hashing hashing = new Evaluation_Hashing();
		Evaluation_Quicksort quicksort = new Evaluation_Quicksort();
		Evaluation_SelectionSort select = new Evaluation_SelectionSort();
		
		AddEdgeRules noEdgeAddition = new AddEdgeRules();
		Evaluation_BSTTraversal trav = new Evaluation_BSTTraversal();
		Evaluation_BSTTraversalWithHelp travHelp = new Evaluation_BSTTraversalWithHelp();
		Evaluation_MSTTraversal MSTTrav = new Evaluation_MSTTraversal();
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
					new String[]{"A B D E C F G"},
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
					new String[]{"D B E A F C G"},
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
					new String[]{"D E B F G C A"},
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
					new String[]{"X R D Z H Q J M L"},
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
					new String[]{"P T D C M Q L N S"},
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
					new String[]{"N M L R Q D Z J A"},
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
                    new String[]{"0004110112", "1022301100", "3010112020", "843 643 823 543 634 235 67 428 79 9", 
								 "9 823 428 634 235 843 643 543 67 79", "9 67 79 235 428 543 634 643 823 843"},       
                    radix,
                    true,
                    DSTConstants.NODE_STRING_DRAGGABLE);
			case 19: return new TreeProblem("MaxHeap 1",
					"MaxHeap 1: Move the nodes to the left into a complete binary tree in the given order;" +
				    " you do not have to add edges. Scroll down to make sure you have included all the given data." +
					" Then apply the reheap algorithm to this tree to form a max heap." +
				    " Add the edges to your final answer and then press Evaluate.",
					"76 98 43 12 48 3 15 29",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
				    noEdges,
					new String[]{"98 76 43 29 48 3 15 12"},
					levelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 20: return new TreeProblem("MaxHeap 2",
					"MaxHeap 2: Move the nodes to the left into a complete binary tree in the given order;" +
				    " you do not have to add edges. Scroll down to make sure you have included all the given data." +
					" Then apply the reheap algorithm to this tree to form a max heap." +
				    " Add the edges to your final answer and then press Evaluate.",
					"17 23 7 39 91 78 45 57 82",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
				    noEdges,
					new String[]{"91 82 78 57 23 7 45 17 39"},
					levelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 21: return new TreeProblem("MaxHeap 3",
					"MaxHeap 3: Move the nodes to the left into a complete binary tree in the given order;" +
				    " you do not have to add edges. Scroll down to make sure you have included all the given data." +
					" Then apply the reheap algorithm to this tree to form a max heap." +
				    " Add the edges to your final answer and then press Evaluate.",
					"9 89 17 53 32 47 91 68 30 21",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
				    noEdges,
					new String[]{"91 89 47 68 32 9 17 53 30 21"},
					levelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 22: return new TreeProblem("MaxHeap 4",
					"Move the nodes to the left into a complete binary tree in the given order;" +
				    " you do not have to add edges. Scroll down to make sure you have included all the given data." +
					" Then apply the reheap algorithm to this tree to form a max heap." +
				    " Add the edges to your final answer and then press Evaluate.",
					"7 12 15 29 22 37 41 50 48 73 68 85",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
				    noEdges,
					new String[]{"85 73 41 50 68 37 7 29 48 22 12 15"},
					levelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 23: return new TreeProblem("MaxHeap Insertion 1",
					"MaxHeap Insertion 1: Use the bubble up technique to insert the node 78 into the heap.",
					"98 80 63 48 39 32 22 19 78",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{250,150,350,75,225,275,425,15,20},
					new int[]{75,175,175,275,275,275,275,350,20},
					new String[]{"98 80","98 63","80 48","80 39","63 22","63 32","48 19"},
					new String[]{"98 80 63 78 39 32 22 19 48"},
					levelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 24: return new TreeProblem("MaxHeap Insertion 2",
					"MaxHeap Insertion 2: Use the bubble up technique to insert the node 99 into the heap.",
					"98 82 76 77 12 65 31 52 22 7 1 99",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					getHeapXLocations(true,"98 82 76 77 12 65 31 52 22 7 1 99"),
					getHeapYLocations(true,"98 82 76 77 12 65 31 52 22 7 1 99"),
					new String[]{"98 82","98 76","82 77","82 12","76 65","76 31","77 52","77 22","12 7","12 1"},
					new String[]{"99 82 98 77 12 76 31 52 22 7 1 65"},
					levelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 25: return new TreeProblem("MaxHeap Insertion 3",
					"MaxHeap Insertion 3: Use the bubble up technique to insert the node 79 into the heap.",
					"85 76 68 27 15 39 55 9 79",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					getHeapXLocations(true,"85 76 68 27 15 39 55 9 79"),
					getHeapYLocations(true,"85 76 68 27 15 39 55 9 79"),
					new String[]{"85 76","85 68","76 27","76 15","68 39","68 55","27 9"},
					new String[]{"85 79 68 76 15 39 55 9 27"},
					levelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 26: return new TreeProblem("MaxHeap Insertion 4",
					"MaxHeap Insertion 4: Use the bubble up technique to insert the node 99 into the heap.",
					"98 87 91 76 67 65 59 35 22 37 12 9 47 17 99",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					getHeapXLocations(true,"98 87 91 76 67 65 59 35 22 37 12 9 47 17 99"),
					getHeapYLocations(true,"98 87 91 76 67 65 59 35 22 37 12 9 47 17 99"),
					new String[]{"98 87","98 91","87 76","87 67","91 65","91 59","76 35","76 22","67 37","67 12","65 9","65 47","59 17"},
					new String[]{"99 87 98 76 67 65 91 35 22 37 12 9 47 17 59"},
					levelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 27: return new TreeProblem("MaxHeap Deletion 1",
					"MaxHeap Deletion 1: Use the bubble down technique to remove the node 97 from the heap and then reformulate the heap."+
					" You can simply place 97 out of the way in a corner after removing the connecting edges.",
					"97 80 63 48 39 32 22 19",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{250,150,350,75,225,275,425,15},
					new int[]{75,175,175,275,275,275,275,350},
					new String[]{"97 80","97 63","80 48","80 39","63 22","63 32","48 19"},
					new String[]{"80 48 63 19 39 32 22"},
					levelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 28: return new TreeProblem("MaxHeap Deletion 2",
					"MaxHeap Deletion 2: Use the bubble down technique to remove the node 83 from the heap and then reformulate the heap."+
					" You can simply place 83 out of the way in a corner after removing the connecting edges.",
					"83 45 73 42 28 54 17 33 6",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					getHeapXLocations(false,"83 45 73 42 28 54 17 33 6"),
					getHeapYLocations(false,"83 45 73 42 28 54 17 33 6"),
					new String[]{"83 45","83 73","45 42","45 28","73 54","73 17","42 33","42 6"},
					new String[]{"73 45 54 42 28 6 17 33"},
					levelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 29: return new TreeProblem("MaxHeap Deletion 3",
					"MaxHeap Deletion 3: Use the bubble down technique to remove the node 93 from the heap and then reformulate the heap."+
					" You can simply place 93 out of the way in a corner after removing the connecting edges.",
					"93 87 82 85 60 57 79 36 41 27 51 16",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					getHeapXLocations(false,"93 87 82 85 60 57 79 36 41 27 51 16"),
					getHeapYLocations(false,"93 87 82 85 60 57 79 36 41 27 51 16"),
					new String[]{"93 87","93 82","87 85","87 60","82 57","82 79","85 36","85 41","60 27","60 51","57 16"},
					new String[]{"87 85 82 41 60 57 79 36 16 27 51"},
					levelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 30: return new TreeProblem("MaxHeap Deletion 4",
					"MaxHeap Deletion 4: Use the bubble down technique to remove the node 91 from the heap and then reformulate the heap."+
					" You can simply place 91 out of the way in a corner after removing the connecting edges.",
					"91 89 85 78 67 69 76 54 17 22 37 45 9",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					getHeapXLocations(false,"91 89 85 78 67 69 76 54 17 22 37 45 9"),
					getHeapYLocations(false,"91 89 85 78 67 69 76 54 17 22 37 45 9"),
					new String[]{"91 89","91 85","89 78","89 67","85 69","85 76","78 54","78 17","67 22","67 37","69 45","69 9"},
					new String[]{"89 78 85 54 67 69 76 9 17 22 37 45"},
					levelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 31: return new TreeProblem("BST Level Traversal 1",
					"Perform a level traversal of the binary tree below by clicking" +
					" nodes in the order the traversal would visit them.",
					"A B C D E F G",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{250,150,350,75,225,275,425},
					new int[]{75,175,175,275,275,275,275},
					new String[]{"A B","B D","B E","C F","C G", "A C"},
					new String[]{"A B C D E F G"},
					trav,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_CLICKABLE);
			case 32: return new TreeProblem("BST Level Traversal 2",
					"Perform a Level traversal of the binary tree below by clicking" +
					" nodes in the order the traversal would visit them.",
					"F C M P Q N K S Y",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{250,200,350,50,300,400,100, 450, 150},
					new int[]{75,175,175,275,275,275,375, 375, 475},
					new String[]{"F C","C P","P K","K Y","F M", "M Q", "M N", "N S"},
					new String[]{"F C M P Q N K S Y"},
					trav,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_CLICKABLE);
			case 33: return new TreeProblem( "BST Level Traversal 3",
					"Perform a Level traversal of the binary tree below by clicking" +
					" nodes in the order the traversal would visit them.",
					"V N A L F C G W Q K",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{250,150,350,75,225,425,175,375,475,425},
					new int[]{50,150,150,250,250,250,350,350,350,450},
					new String[]{"V N","V A","N L","N F","F G","A C","C W", "C Q","Q K"},
					new String[]{"V N A L F C G W Q K"},
					trav,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_CLICKABLE);
			case 34: return new TreeProblem("Binary Search Tree from Level Traversal 1",
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
			case 35: return new TreeProblem("Binary Search Tree from Level Traversal 2",
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
			case 36: return new TreeProblem("Binary Search Tree from Level Traversal 3",
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
			case 37: return new TreeProblem("Binary Search Tree from Level Traversal 4",
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
			
			//
			case 38: return new TreeProblem("MinHeap 1",
					"MinHeap 1: Move the nodes to the left into a complete binary tree in the given order;" +
				    " you do not have to add edges. Scroll down to make sure you have included all the given data." +
					" Then apply the reheap algorithm to this tree to form a min heap." +
				    " Add the edges to your final answer and then press Evaluate.",
					"76 98 43 12 48 3 15 29",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
				    noEdges,
					new String[]{"3 12 15 29 48 43 76 98"},
					minLevelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 39: return new TreeProblem("MinHeap 2",
					"Minheap 2: Move the nodes to the left into a complete binary tree in the given order;" +
				    " you do not have to add edges. Scroll down to make sure you have included all the given data." +
					" Then apply the reheap algorithm to this tree to form a min heap." +
				    " Add the edges to your final answer and then press Evaluate.",
					"17 23 91 7 39 78 82 45 57",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
				    noEdges,
					new String[]{"7 17 78 23 39 91 82 45 57"},
					minLevelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 40: return new TreeProblem("MinHeap 3",
					"MinHeap 3: Move the nodes to the left into a complete binary tree in the given order;" +
				    " you do not have to add edges. Scroll down to make sure you have included all the given data." +
					" Then apply the reheap algorithm to this tree to form a min heap." +
				    " Add the edges to your final answer and then press Evaluate.",
					"9 89 17 53 32 47 91 68 30 21",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
				    noEdges,
					new String[]{"9 21 17 30 32 47 91 68 53 89"},
					minLevelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 41: return new TreeProblem("MinHeap 4",
					"MinHeap 4: Move the nodes to the left into a complete binary tree in the given order;" +
				    " you do not have to add edges. Scroll down to make sure you have included all the given data." +
					" Then apply the reheap algorithm to this tree to form a min heap." +
				    " Add the edges to your final answer and then press Evaluate.",
					"85 68 73 48 50 41 37 22 29 15 12 7",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
				    noEdges,
					new String[]{"7 12 37 22 15 41 85 48 29 68 50 73"},
					minLevelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 42: return new TreeProblem("MinHeap Insertion 1",
					"MinHeap Insertion 1: Use the bubble up technique to insert the node 11 into the heap.",
					"19 22 32 39 48 63 80 98 11",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{250,150,350,75,225,275,425,15,20},
					new int[]{75,175,175,275,275,275,275,350,20},
					new String[]{"19 22", "19 32", "22 39", "22 48", "32 63", "32 80", "39 98"},
					new String[]{"11 19 32 22 48 63 80 98 39"},
					minLevelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 43: return new TreeProblem("MinHeap Insertion 2",
					"MinHeap Insertion 2: Use the bubble up technique to insert the node 2 into the heap.",
					//"98 82 76 77 12 65 31 52 22 7 1 99",
					"1 7 22 12 52 31 65 77 76 82 98 2",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					getHeapXLocations(true, "1 7 22 12 52 31 65 77 76 82 98 2"),
					getHeapYLocations(true, "1 7 22 12 52 31 65 77 76 82 98 2"),
					new String[]{"1 7","1 22","7 12","7 52","22 31","22 65","12 77","12 76","52 82","52 98"},
					new String[]{"1 7 2 12 52 22 65 77 76 82 98 31"},
					minLevelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 44: return new TreeProblem("MinHeap Insertion 3",
					"MinHeap Insertion 3: Use the bubble up technique to insert the node 25 into the heap.",
					"9 15 39 27 76 85 68 55 25",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					getHeapXLocations(true, "9 15 39 27 76 85 68 55 25"),
					getHeapYLocations(true, "9 15 39 27 76 85 68 55 25"),
					new String[]{"9 15", "9 39", "15 27", "15 76", "39 85", "39 68", "27 55"},
					new String[]{"9 15 39 25 76 85 68 55 27"},
					minLevelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 45: return new TreeProblem("MinHeap Insertion 4",
					"MinHeap Insertion 4: Use the bubble up technique to insert the node 1 into the heap.",
					"9 12 59 17 22 87 65 35 37 47 76 98 91 67 1",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					getHeapXLocations(true,"9 12 59 17 22 87 65 35 37 47 76 98 91 67 1"),
					getHeapYLocations(true,"9 12 59 17 22 87 65 35 37 47 76 98 91 67 1"),
					new String[]{"9 12","9 59","12 17","12 22","59 87","59 65","17 35","17 37","22 47","22 76","87 98","87 91","65 67"},
					new String[]{"1 12 9 17 22 87 59 35 37 47 76 98 91 67 65"},
					minLevelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			
			//
			case 46: return new TreeProblem("MinHeap Deletion 1",
					"MinHeap Deletion 1: Use the bubble down technique to remove the node 19 from the heap and " +
					"then reformulate the heap. You can simply place 19 out of the way in a corner after " +
					"removing the connecting edges.",
					"19 39 22 80 48 32 63 97",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{250,150,350,75,225,275,425,15},
					new int[]{75,175,175,275,275,275,275,350},
					new String[]{"19 39","19 22","39 80","39 48","22 32","22 63","80 97"},
					new String[]{"22 39 32 80 48 97 63"},
					minLevelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 47: return new TreeProblem("MinHeap Deletion 2",
					"MinHeap Deletion 2: Use the bubble down technique to remove the node 6 from the heap and then reformulate the heap."+
					" You can simply place 6 out of the way in a corner after removing the connecting edges.",
					"6 28 17 42 33 54 83 73 45",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					getHeapXLocations(false,"6 28 17 42 33 54 83 73 45"),
					getHeapYLocations(false,"6 28 17 42 33 54 83 73 45"),
					new String[]{"6 28","6 17","28 42","28 33","17 54","17 83","42 73","42 45"},
					new String[]{"17 28 45 42 33 54 83 73"},
					minLevelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 48: return new TreeProblem("MinHeap Deletion 3",
					"MinHeap Deletion 3: Use the bubble down technique to remove the node 16 from the heap and then reformulate the heap."+
					" You can simply place 16 out of the way in a corner after removing the connecting edges.",
					"16 27 36 41 51 57 79 82 85 87 93 60",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					getHeapXLocations(false,"16 27 36 41 51 57 79 82 85 87 93 60"),
					getHeapYLocations(false,"16 27 36 41 51 57 79 82 85 87 93 60"),
					new String[]{"16 27","16 36","27 41","27 51","36 57","36 79","41 82","41 85","51 87","51 93","57 60"},
					new String[]{"27 41 36 60 51 57 79 82 85 87 93"},
					minLevelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 49: return new TreeProblem("MinHeap Deletion 4",
					"MinHeap Deletion 4: Use the bubble down technique to remove the node 9 from the heap and then reformulate the heap."+
					" You can simply place 9 out of the way in a corner after removing the connecting edges.",
					"9 17 37 22 54 45 76 78 85 89 69 91 67",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					getHeapXLocations(false,"9 17 37 22 54 45 76 78 85 89 69 91 67"),
					getHeapYLocations(false,"9 17 37 22 54 45 76 78 85 89 69 91 67"),
					new String[]{"9 17","9 37","17 22","17 54","37 45","37 76","22 78","22 85","54 89","54 69","45 91","45 67"},
					new String[]{"17 22 37 67 54 45 76 78 85 89 69 91"},
					minLevelHeap,
					rules,
					true,
					true,
					DSTConstants.NODE_STRING_DRAGGABLE);
			case 50: return new SortProblem("HeapSort 1",
					"HeapSort 1: Use the HeapSort algorithm to sort the following array. "+
					"Evaluate after each pass of HeapSort. A pass constitutes removing the element off " +
					"of the top of the heap and rebuilding the heap. If the array does not start "+
					"off as a MaxHeap then you must first heapify the array into a MaxHeap. You will "+
					"evaluate after you have completed the heapification.",
					"9 5 8 2 4 7 6 1",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"9 5 8 2 4 7 6 1"},
					heapSort,
					rules,
					false,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 51: return new SortProblem("HeapSort 2",
					"HeapSort 2: Use the HeapSort algorithm to sort the following array. "+
					"Evaluate after each pass of HeapSort. A pass constitutes removing the element off " +
					"of the top of the heap and rebuilding the heap. If the array does not start "+
					"off as a MaxHeap then you must first heapify the array into a MaxHeap. You will "+
					"evaluate after you have completed the heapification.",
					"9 6 4 5 7 3 2 1",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"9 6 4 5 7 3 2 1"},
					heapSort,
					rules,
					false,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 52: return new SortProblem("HeapSort 3",
					"HeapSort 3: Use the HeapSort algorithm to sort the following array. "+
					"Evaluate after each pass of HeapSort. A pass constitutes removing the element off " +
					"of the top of the heap and rebuilding the heap. If the array does not start "+
					"off as a MaxHeap then you must first heapify the array into a MaxHeap. You will "+
					"evaluate after you have completed the heapification.",
					"8 7 4 6 5 1 2 3",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"8 7 4 6 5 1 2 3"},
					heapSort,
					rules,
					false,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 53: return new SortProblem("HeapSort 4",
					"HeapSort 4: Use the HeapSort algorithm to sort the following array. "+
					"Evaluate after each pass of HeapSort. A pass constitutes removing the element off " +
					"of the top of the heap and rebuilding the heap. If the array does not start "+
					"off as a MaxHeap then you must first heapify the array into a MaxHeap. You will "+
					"evaluate after you have completed the heapification.",
					"9 2 1 9 5 3 6 5",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"9 2 1 9 5 3 6 5"},
					heapSort,
					rules,
					false,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 54: return new SearchProblem("Radix Sort 2",
					"A queue of data values is shown at the top of the display. " +
					"Using the given digit position move each value to the " + 
					"appropriate bucket. Each bucket is a queue structure with " + 
					"the front below the label and the rear at the bottom of the screen.",
                    "371 105 599 21 900 610 5 453 712 99",
                    DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
                    new String[]{"2211020002", "3210010102", "3101111101", "900 610 371 21 712 453 105 5 599 99", 
								 "900 105 5 610 712 21 453 371 599 99", "5 21 99 105 371 453 599 610 712 900"},
                    radix,
                    true,
                    DSTConstants.NODE_STRING_DRAGGABLE);
			case 55: return new MSTProblem("MST Kruskal's 1",
					"MST Kruskal's 1: Use Kruskal's algorithm to find the minimum spanning tree for "+
			        "the given graph.",
					"76 98 43 12 48 3",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{60, 175, 175, 325, 325, 500},
					new int[]{250, 75, 400, 75, 400, 250},
				    new String[]{"76 98 9", "76 43 2", "98 12 11", "98 43 14", "98 48 7", "12 48 17", "12 3 8", "3 48 12", "48 43 5", "98 3 4", "76 48 10"},
					new String[]{"2 4 5 7 8"},
					MSTTrav,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_DRAGGABLE);
			case 56: return new MSTProblem("MST Prim's 1",
					"MST Prim's 1: Use Prim's algorithm to find the minimum spanning tree for "+
			        "the given graph. Start with the node 3.",
					"76 98 43 12 48 3",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{60, 175, 175, 325, 325, 500},
					new int[]{250, 75, 400, 75, 400, 250},
				    new String[]{"76 98 9", "76 43 2", "98 12 11", "98 43 14", "98 48 7", "12 48 17", "12 3 8", "3 48 12", "48 43 5", "98 3 4", "76 48 10"},
					new String[]{"4 7 5 2 8"},
					MSTTrav,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_DRAGGABLE);
			case 57: return new MSTProblem("MST Kruskal's 2",
					"MST Kruskal's 2: Use Kruskal's algorithm to find the minimum spanning tree for "+
			        "the given graph.",
					"87 23 4 29 65 13",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{50, 50, 275, 275, 500, 500},
					new int[]{175, 375, 50, 500, 175, 375},
				    new String[]{"87 4 23", "4 65 7", "65 13 1", "13 29 10", "29 23 3", "23 87 17", "87 65 5", "23 13 11", "87 29 9", "23 4 15", "4 13 6", "29 65 14"},
					new String[]{"1 3 5 6 9"},
					MSTTrav,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_DRAGGABLE);
			case 58: return new MSTProblem("MST Prim's 2",
					"MST Prim's 2: Use Prim's algorithm to find the minimum spanning tree for "+
			        "the given graph. Start with the node 4.",
					"87 23 4 29 65 13",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{50, 50, 275, 275, 500, 500},
					new int[]{175, 375, 50, 500, 175, 375},
				    new String[]{"87 4 23", "4 65 7", "65 13 1", "13 29 10", "29 23 3", "23 87 17", "87 65 5", "23 13 11", "87 29 9", "23 4 15", "4 13 6", "29 65 14"},
					new String[]{"6 1 5 9 3"},
					MSTTrav,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_DRAGGABLE);
			case 59: return new MSTProblem("MST Kruskal's 3",
					"MST Kruskal's 3: Use Kruskal's algorithm to find the minimum spanning tree for "+
			        "the given graph.",
					"15 73 95 2 57 40 23",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{50, 150, 150, 275, 350, 350, 450},
					new int[]{250, 75, 400, 250, 75, 400, 250},
				    new String[]{"15 73 14", "15 95 9", "15 2 12", "73 2 7", "73 57 2", "95 2 17", "95 40 11", "57 40 4", "57 2 5", "57 23 21", "40 23 1", "40 2 13"},
					new String[]{"1 2 4 5 9 11"},
					MSTTrav,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_DRAGGABLE);
			case 60: return new MSTProblem("MST Prim's 3",
					"MST Prim's 3: Use Prim's algorithm to find the minimum spanning tree for "+
			        "the given graph. Start with the node 15.",
					"15 73 95 2 57 40 23",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{50, 150, 150, 275, 350, 350, 450},
					new int[]{250, 75, 400, 250, 75, 400, 250},
					new String[]{"15 73 14", "15 95 9", "15 2 12", "73 2 7", "73 57 2", "95 2 17", "95 40 11", "57 40 4", "57 2 5", "57 23 21", "40 23 1", "40 2 13"},
					new String[]{"9 11 1 4 2 5"},
					MSTTrav,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_DRAGGABLE);
			case 61: return new MSTProblem("MST Kruskal's 4",
					"MST Kruskal's 4: Use Kruskal's algorithm to find the minimum spanning tree for "+
			        "the given graph.",
					"15 73 95 2 57 40 23",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{50, 125, 150, 275, 350, 350, 450},
					new int[]{100, 350, 200, 375, 300, 50, 400},
					new String[]{"15 40 3", "40 23 10", "40 57 7", "23 57 18", "57 2 14", "2 73 16", "73 40 12", "95 15 22", "95 57 5", "2 95 20", "73 15 24"},
					new String[]{"3 5 7 10 12 14"},
					MSTTrav,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_DRAGGABLE);
			case 62: return new MSTProblem("MST Prim's 4",
					"MST Prim's 4: Use Prim's algorithm to find the minimum spanning tree for "+
			        "the given graph. Start with the node 23.",
					"15 73 95 2 57 40 23",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{50, 125, 150, 275, 350, 350, 450},
					new int[]{100, 350, 200, 375, 300, 50, 400},
					new String[]{"15 40 3", "40 23 10", "40 57 7", "23 57 18", "57 2 14", "2 73 16", "73 40 12", "95 15 22", "95 57 5", "2 95 20", "73 15 24"},
					new String[]{"10 3 7 5 12 14"},
					MSTTrav,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_DRAGGABLE);
			case 63: return new MSTProblem("MST Kruskal's DEMO",
					"MST Kruskal's 4: Use Kruskal's algorithm to find the minimum spanning tree for "+
			        "the given graph.",
			        "15 73 95 2 57 40 23",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{450, 150, 250, 350, 50, 275, 75},
					new int[]{300, 200, 400, 50, 250, 150, 50},
					new String[]{"57 23 2","23 2 19", "2 15 7", "15 95 13", "95 57 15", "23 73 5", "57 73 11", "23 40 22", "40 2 12", "95 73 4", "15 40 25","73 40 18", "40 95 30"},
					new String[]{"2 4 5 7 12 13"},
					MSTTrav,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_DRAGGABLE);
			case 64: return new MSTProblem("MST Prim's DEMO",
					"MST Prim's 4: Use Prim's algorithm to find the minimum spanning tree for "+
			        "the given graph. Start with the node 2.",
					"15 73 95 2 57 40 23",
					DSTConstants.INSERT_METHOD_VALUE_AND_LOCATION,
					new int[]{450, 150, 250, 350, 50, 275, 75},
					new int[]{300, 200, 400, 50, 250, 150, 50},
					new String[]{"57 23 2","23 2 19", "2 15 7", "15 95 13", "95 57 15", "23 73 5", "57 73 11", "23 40 22", "40 2 12", "95 73 4", "15 40 25","73 40 18", "40 95 30"},
					new String[]{"7 12 13 4 5 2"},
					MSTTrav,
					noEdgeAddition,
					false,
					false,
					DSTConstants.NODE_DRAGGABLE);
			case 65: return new HashingProblem("Hashing 1",
					"Hashing 1: Working from left to right use the hash function h(x) = x mod 13 to insert the data into the hash table using linear probing.",
					"7 29 39 30 58 11 72 91",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"13","7 7","29 3","39 0","30 4","58 6","11 11","72 8","91 1"},
					hashing,
					rules,
					false,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 66: return new HashingProblem("Hashing 2",
					"Hashing 2: Starting with the first row of data items and working your way from left to right use the hash function h(x) = x mod 17 to insert the data into the hash table using linear probing.",
					"21 32 47 24 83 55 10 75 61 37 42 30 50",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"17","21 4","32 15","47 13","24 7","83 16","55 5","10 10","75 8","61 11","37 3","42 9","30 14","50 0"},					
					hashing,
					rules,
					false,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 67: return new HashingProblem("Hashing 3",
					"Hashing 3: Working from left to right use the hash function h(x) = x mod 11 to insert the data into the hash table using linear probing.",
					"19 83 61 55 27 13 44 21",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"11","19 8","83 6","61 7","55 0","27 5","13 2","44 1","21 10"},
					hashing,
					rules,
					false,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 68: return new HashingProblem("Hashing 4",
					"Hashing 4: Starting with the first row of data items and working your way from left to right use the hash function h(x) = x mod 19 to insert the data into the hash table using linear probing.",
					"7 29 49 30 58 11 75 91 2 37 23 18 40",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"19","7 7","29 10","49 11","30 12","58 1","11 13","75 18","91 15","2 2","37 0","23 4","18 3","40 5"},
					hashing,
					rules,
					false,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 69: return new HashingProblem("Hashing 5",
					"Hashing 5: Starting with the first row of data items and working your way from left to right use the hash function h(x) = x mod 17 to insert the data into the hash table using linear probing.",
					"58 73 20 53 17 24 82 32 62 67 15 48",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"17","58 7","73 5","20 3","53 2","17 0","24 8","82 14","32 15","62 11","67 16","15 1","48 4"},
					hashing,
					rules,
					false,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 70: return new HashingProblem("Hashing 6",
					"Hashing 6: Starting with the first row of data items and working your way from left to right use the hash function h(x) = x mod 19 to insert the data into the hash table using linear probing.",
					"98 43 59 89 84 35 19 27 1 50 38 13 65",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"19","98 3","43 5","59 2","89 13","84 8","35 16","19 0","27 9","1 1","50 12","38 4","13 14","65 10"},
					hashing,
					rules,
					false,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 71: return new HashingProblem("Hashing 1 - Quadratic",
					"Hashing 1 - Quadratic: Working from left to right use the has function h(x) = x mod 13 to insert the data into the hash table using quadratic probing in the form (h(x)+i^2) mod 13.",
					"7 29 39 30 58 11 72 91",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"13","7 7","29 3","39 0","30 4","58 6","11 11","72 8","91 1"},
					hashing,
					rules,
					false,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 72: return new HashingProblem("Hashing 2 - Quadratic",
					"Hashing 2 - Quadratic: Starting with the first row of data items and working your way from left to right use the hash function h(x) = x mod 17 to insert the data into the hash table using quadratic probing in the form (h(x)+i^2) mod 17.",
					"21 32 47 24 83 55 10 75 61 37 42 30 50",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"17","21 4","32 15","47 13","24 7","83 16","55 5","10 10","75 8","61 11","37 3","42 9","30 14","50 0"},					
					hashing,
					rules,
					false,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 73: return new HashingProblem("Hashing 3 - Quadratic",
					"Hashing 3 - Quadratic: Working from left to right use the hash function h(x) = x mod 11 to insert the data into the hash table using quadratic probing in the form (h(x)+i^2) mod 11.",
					"19 83 61 55 27 13 44 21",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"11","19 8","83 6","61 7","55 0","27 5","13 2","44 1","21 10"},
					hashing,
					rules,
					false,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 74: return new HashingProblem("Hashing 4 - Quadratic",
					"Hashing 4 - Quadratic: Starting with the first row of data items and working your way from left to right use the hash function h(x) = x mod 19 to insert the data into the hash table using quadratic probing in the form (h(x)+i^2) mod 19.",
					"7 29 49 30 58 11 75 91 2 37 23 18 40",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"19","7 7","29 10","49 11","30 12","58 1","11 15","75 18","91 16","2 2","37 0","23 4","18 3","40 6"},
					hashing,
					rules,
					false,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 75: return new HashingProblem("Hashing 5 - Quadratic",
					"Hashing 5 - Quadratic: Starting with the first row of data items and working your way from left to right use the hash function h(x) = x mod 17 to insert the data into the hash table using quadratic probing in the form (h(x)+i^2) mod 17.",
					"58 73 20 53 17 24 82 32 62 67 15 48",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"17","58 7","73 5","20 3","53 2","17 0","24 8","82 14","32 15","62 11","67 16","15 6","48 1"},
					hashing,
					rules,
					false,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 76: return new HashingProblem("Hashing 6 - Quadratic",
					"Hashing 6 - Quadratic: Starting with the first row of data items and working your way from left to right use the hash function h(x) = x mod 19 to insert the data into the hash table using quadratic probing in the form (h(x)+i^2) mod 19.",
					"98 43 59 89 84 35 19 27 1 50 38 13 65",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"19","98 3","43 5","59 2","89 13","84 8","35 16","19 0","27 9","1 1","50 12","38 4","13 14","65 17"},
					hashing,
					rules,
					false,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 77: return new SortProblem("Quicksort Partition",
					"Quicksort Partition: For each pass, put the nodes in order according to the pivot",
					"9 2 7 1 0 5 3 6",
					DSTConstants.INSERT_METHOD_VALUE,
					noLocs,
					noLocs,
					noEdges,
					new String[]{"9 2 7 1 0 5 3 6"},
					quicksort,
					rules,
					false,
					true,
					DSTConstants.NODE_DRAGGABLE);
			case 78: return new SelectionSortProblem("Selection Sort Test",
					"Use the selection sort algorithm to sort the array. " +
					"Select the node you would like to swap with the red node and click \"Swap.\"",
					"87 54 56 12 34 23 76 64 8 47",
					DSTConstants.INSERT_METHOD_VALUE,
					new String[]{""},
					select,
					rules,
					false,
					DSTConstants.NODE_CLICKABLE);


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
		if(problem.equals("MaxHeap 1")) return 19;
		if(problem.equals("MaxHeap 2")) return 20;
		if(problem.equals("MaxHeap 3")) return 21;
		if(problem.equals("MaxHeap 4")) return 22;
		if(problem.equals("MaxHeap Insertion 1")) return 23;
		if(problem.equals("MaxHeap Insertion 2")) return 24;
		if(problem.equals("MaxHeap Insertion 3")) return 25;
		if(problem.equals("MaxHeap Insertion 4")) return 26;
		if(problem.equals("MaxHeap Deletion 1")) return 27;
		if(problem.equals("MaxHeap Deletion 2")) return 28;
		if(problem.equals("MaxHeap Deletion 3")) return 29;
		if(problem.equals("MaxHeap Deletion 4")) return 30;
		if(problem.equals("BST Level Traversal 1")) return 31;
		if(problem.equals("BST Level Traversal 2")) return 32;
		if(problem.equals("BST Level Traversal 3")) return 33;
		if(problem.equals("Binary Search Tree from Level Traversal 1")) return 34;
		if(problem.equals("Binary Search Tree from Level Traversal 2")) return 35;
		if(problem.equals("Binary Search Tree from Level Traversal 3")) return 36;
		if(problem.equals("Binary Search Tree from Level Traversal 4")) return 37;
		if(problem.equals("MinHeap 1")) return 38;
		if(problem.equals("MinHeap 2")) return 39;
		if(problem.equals("MinHeap 3")) return 40;
		if(problem.equals("MinHeap 4")) return 41;
		if(problem.equals("MinHeap Insertion 1")) return 42;
		if(problem.equals("MinHeap Insertion 2")) return 43;
		if(problem.equals("MinHeap Insertion 3")) return 44;
		if(problem.equals("MinHeap Insertion 4")) return 45;
		if(problem.equals("MinHeap Deletion 1")) return 46;
		if(problem.equals("MinHeap Deletion 2")) return 47;
		if(problem.equals("MinHeap Deletion 3")) return 48;
		if(problem.equals("MinHeap Deletion 4")) return 49;
		if(problem.equals("HeapSort 1")) return 50;
		if(problem.equals("HeapSort 2")) return 51;
		if(problem.equals("HeapSort 3")) return 52;
		if(problem.equals("HeapSort 4")) return 53;
		if(problem.equals("Radix Sort 2")) return 54;
		if(problem.equals("MST Kruskal's 1")) return 55;
		if(problem.equals("MST Prim's 1")) return 56;
		if(problem.equals("MST Kruskal's 2")) return 57;
		if(problem.equals("MST Prim's 2")) return 58;
		if(problem.equals("MST Kruskal's 3")) return 59;
		if(problem.equals("MST Prim's 3")) return 60;
		if(problem.equals("MST Kruskal's 4")) return 61;
		if(problem.equals("MST Prim's 4")) return 62;
		if(problem.equals("MST Kruskal's DEMO")) return 63;
		if(problem.equals("MST Prim's DEMO")) return 64;
		if(problem.equals("Hashing 1")) return 65;
		if(problem.equals("Hashing 2")) return 66;
		if(problem.equals("Hashing 3")) return 67;
		if(problem.equals("Hashing 4")) return 68;
		if(problem.equals("Hashing 5")) return 69;
		if(problem.equals("Hashing 6")) return 70;
		if(problem.equals("Hashing 1 - Quadratic")) return 71;
		if(problem.equals("Hashing 2 - Quadratic")) return 72;
		if(problem.equals("Hashing 3 - Quadratic")) return 73;
		if(problem.equals("Hashing 4 - Quadratic")) return 74;
		if(problem.equals("Hashing 5 - Quadratic")) return 75;
		if(problem.equals("Hashing 6 - Quadratic")) return 76;
		if(problem.equals("Quicksort Partition")) return 77;
		if(problem.equals("Selection Sort Test")) return 78;
		return 0;
	}
	public static int[] getHeapXLocations(boolean insert, String nodes){
		final int[] xMaster = {275,125,425,55,195,355,495,25,85,165,225,325,385,465,525};
		String[] splitNodes = nodes.split(" ");
		int[] x = new int[splitNodes.length];
		for(int i=0;i<splitNodes.length;i++){
			x[i]=xMaster[i];
		}
		if(insert==true){
			x[x.length-1]=20;
		}
		return x;
	}
	public static int[] getHeapYLocations(boolean insert, String nodes){
		final int[] yMaster = {50,150,150,250,250,250,250,350,350,350,350,350,350,350,350};
		String[] splitNodes = nodes.split(" ");
		int[] y = new int[splitNodes.length];
		for(int i=0;i<splitNodes.length;i++){
			y[i]=yMaster[i];
		}
		if(insert==true){
			y[y.length-1]=20;
		}
		return y;
	}
}
