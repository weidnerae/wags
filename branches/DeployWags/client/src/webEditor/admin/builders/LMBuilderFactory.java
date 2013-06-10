package webEditor.admin.builders;

import webEditor.logical.DSTConstants;

public class LMBuilderFactory {
	private static final int TRAVERSAL_GID = 1;
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
		
		return builder;
	}
}
