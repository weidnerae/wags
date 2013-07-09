package webEditor.admin.builders;

/**
 * This class does not do anything.
 * This is good, as some displays should not allow edges to be created.
 * @author pmeznar
 *
 */
public class EH_NoEdges implements EdgeHandler {

	@Override
	public boolean addEdge(Node_Basic node1, Node_Basic node2) {
		return true;
	}

	@Override
	public void clear() {
	}

}
