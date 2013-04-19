package webEditor.admin.builders;

import webEditor.Genre;
import webEditor.InsertMethod;
import webEditor.NodeType;
import webEditor.logical.DSTConstants;

public class LMBuilderFactory {
	private static final int TRAVERSAL_GID = 1;
	
	public static LMBuilder getTraversalBuilder(){
		LMBuilder builder = new LMTraversalBuilder(Genre.TRAVERSAL, 
				InsertMethod.BY_VALUE_AND_LOC, NodeType.CLICKABLE,
				false, false, DSTConstants.TREE_MODE_KEY, TRAVERSAL_GID);
		
		return builder;
	}
}
