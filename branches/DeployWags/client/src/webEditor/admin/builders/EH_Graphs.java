package webEditor.admin.builders;

public class EH_Graphs implements EdgeHandler {
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

}
