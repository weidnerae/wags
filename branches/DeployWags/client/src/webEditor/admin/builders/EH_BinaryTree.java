package webEditor.admin.builders;

public class EH_BinaryTree implements EdgeHandler {
	BasicCanvas parent;

	public EH_BinaryTree(BasicCanvas canvas){
		this.parent = canvas;
	}
	
	@Override
	public boolean addEdge(Node_Basic node1, Node_Basic node2) {
		Edge_BinaryTree edge = new Edge_BinaryTree(node1, node2, parent);
		if(edge.isValid()){
			parent.canvas.add(edge);
			return false;
		}
		parent.update();
		
		return true;
	}

}
