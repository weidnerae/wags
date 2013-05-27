package webEditor.admin.builders;

import webEditor.logical.DSTConstants;

public class LMBuilderFactory {
	private static final int TRAVERSAL_GID = 1;
	
	public static LMBuilder getTraversalBuilder(){
		LMBuilder builder = new LMTraversalBuilder(Genre.TRAVERSAL, 
				InsertMethod.BY_VALUE_AND_LOC, NodeType.CLICKABLE,
				false, false, DSTConstants.NO_EDGES_KEY, TRAVERSAL_GID);
		
		return builder;
	}
}
