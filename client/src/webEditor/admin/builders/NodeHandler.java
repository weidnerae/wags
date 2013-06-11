package webEditor.admin.builders;

public abstract class NodeHandler {
	BasicCanvas parent;
	
	public abstract void addNode(String node);
	public abstract void deleteNode(String node);
	public void update(){
		parent.update();
	}
}
