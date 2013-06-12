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
	
	public static LMBuilder getInsertNodeBuilder(){
		LMBuilder builder = new LMBuilder(Genre.TRAVERSAL, 
				InsertMethod.BY_VALUE, NodeType.DRAGGABLE,
				true, true, DSTConstants.TREE_MODE_KEY, 
				NOT_IMPORTANT);
		builder.setEval(DSTConstants.BST_PREORDER_KEY);
		builder.addEdge("");
		builder.setPos(new int[0], new int[0]);
		
		return builder;
	}
}
