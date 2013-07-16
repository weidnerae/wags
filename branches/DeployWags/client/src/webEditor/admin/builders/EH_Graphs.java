package webEditor.admin.builders;

public class EH_Graphs implements EH_Weighted {
	BasicCanvas parent;

	public EH_Graphs(BasicCanvas canvas){
		this.parent = canvas;
	}
	
	@Override
	public boolean addEdge(Node_Basic node1, Node_Basic node2) {
		Edge_Graphs edge = new Edge_Graphs(node1, node2, parent);
		parent.canvas.add(edge);
		parent.update();
		
		return true;
	}
	
	public void clear(){
		Edge_Graphs.reset();
	}

	@Override
	public boolean addEdge(Node_Basic node1, Node_Basic node2, int weight) {
		Edge_Graphs edge = new Edge_Graphs(node1, node2, parent, weight);
		node1.addEdge(edge);
		node2.addEdge(edge);
		parent.canvas.add(edge);
		parent.update();
		
		return true;
	}

}
