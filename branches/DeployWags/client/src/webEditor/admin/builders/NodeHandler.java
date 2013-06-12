package webEditor.admin.builders;

public abstract class NodeHandler {
	BasicCanvas parent;
	
	public abstract void addNode(String value);
	public abstract void deleteNode(String value);
	public abstract void clear();
	public void update(){
		parent.update();
	}
}
