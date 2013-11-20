package webEditor.admin.builders;

import webEditor.logical.DSTConstants;

public class LMBuilderFactory {
	// Group currently overriden by admin's group
	private static final int NOT_IMPORTANT = 0;
	
	public static LMBuilder getTraversalBuilder(){
		return new LMBuilder(Genre.TRAVERSAL, 
				InsertMethod.BY_VALUE_AND_LOC, NodeType.CLICKABLE,
				false, false, DSTConstants.NO_EDGES_KEY, NOT_IMPORTANT);
		
	}
	
	public static LMBuilder getBuildBSTBuilder(){
		LMBuilder builder = new LMBuilder(Genre.TRAVERSAL, 
				InsertMethod.BY_VALUE, NodeType.DRAGGABLE, true, true, DSTConstants.TREE_MODE_KEY, NOT_IMPORTANT);
		builder.setEval(DSTConstants.BST_PREORDER_KEY);
		builder.addEdge("");
		builder.setPos(new int[0], new int[0]);
		return builder;
	}
	
	public static LMBuilder getInsertNodeBuilder(){
		LMBuilder builder = new LMBuilder(Genre.TRAVERSAL, 
				InsertMethod.BY_VALUE, NodeType.DRAGGABLE,
				true, true, DSTConstants.TREE_MODE_KEY, 
				NOT_IMPORTANT);
		builder.addEdge("");
		builder.setPos(new int[0], new int[0]);
		
		return builder;
	}
	
	public static LMBuilder getGraphsBuilder(){
		LMBuilder builder = new LMBuilder(Genre.MST,
				InsertMethod.BY_VALUE_AND_LOC, NodeType.DRAGGABLE,
				false, false, DSTConstants.NO_EDGES_KEY,
				NOT_IMPORTANT);
		builder.setEval(DSTConstants.MST_KEY);
		
		return builder;
	}
	
	public static LMBuilder getSimplePartitionBuilder(){
		LMBuilder builder = new LMBuilder(Genre.SIMPLE_PARTITION,
				InsertMethod.BY_VALUE, NodeType.NODE, false, false,
				DSTConstants.TREE_MODE_KEY, NOT_IMPORTANT);
		builder.addEdge("");
		builder.setPos(new int[0], new int[0]);
		builder.setEval(DSTConstants.SIMPLEPARTITION_KEY);
		
		return builder;
	}
}
