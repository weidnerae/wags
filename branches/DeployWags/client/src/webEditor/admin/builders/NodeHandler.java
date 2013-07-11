package webEditor.admin.builders;

public abstract class NodeHandler {
	BasicCanvas parent;
	boolean numbers = false;
	boolean unique = true;
	
	public abstract void addNode(String value);
	public abstract void deleteNode(String value);
	public abstract void clear();
	public void update(){
		parent.update();
	}
	
	public void setNumbers(boolean val){
		numbers = val;
	}
	
	public void setUnique(boolean val){
		unique = val;
	}
}
