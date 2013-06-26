package webEditor.admin.builders;

public interface EdgeHandler {
	/**
	 * addEdge
	 * @param node1 - An edge is drawn between this and node2
	 * @param node2 - An edge is drawn between this and node1
	 * Delegates creation of the edge to the edge class, and then adds the
	 * resulting edge to the canvas.  Calls "update()" in case parent LMDisplay
	 * needs to be aware of the modification.
	 */
	boolean addEdge(Node_Basic node1, Node_Basic node2);
	public void clear();
}
