package webEditor.admin.builders;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;

public class NH_AlongTop extends NodeHandler {
	int nodeX = 10, nodeY = 10;
	final int NODE_WIDTH = 40;
	private ArrayList<Node_Basic> listNodes;
	
	public NH_AlongTop(BasicCanvas canvas) {
		this.parent = canvas;
		listNodes = new ArrayList<Node_Basic>();
	}

	@Override
	/**
	 * addNode
	 * @param value - The value of the node that is displayed
	 * Adds a node to the BasicCanvas and registers it with the BasicCanvas.
	 * Relies on "positionNode(BasicNode)" to place node
	 * Calls "update()" in case parent LMDisplay must be notified of change
	 */
	public void addNode(String value) {
		// For traversals, we're going to force unique nodes
		if(unique){
			for(Node_Basic node: parent.nodes){
				if(node.value.equals(value)){
					Window.alert("Duplicate nodes not allowed!");
					return;
				}
			}
		}
		
		if(numbers){
			try{
				Integer.parseInt(value);
			} catch (NumberFormatException e){
				Window.alert("Illegal number format!");
				return;
			}
		}
		
		Node_Basic node = new Node_Basic(value, parent);
		parent.dragger.makeDraggable(node);
		parent.nodes.add(node);
		listNodes.add(node);
		positionNode(node);
		update();
	}

	public void addNode(Node_Basic node) {
		parent.nodes.add(node);
		positionNode(node);
		update();
	}
	/**
	 * positionNode
	 * @param node - Node to be added
	 * Determines where the new node will be placed on the canvas. 
	 */
	private void positionNode(Node_Basic node){
		// May be modified in future
		// Sets position of current node
		node.setPosition(nodeX, nodeY);
		parent.canvasPanel.add(node, node.xPos, node.yPos);
		
		// Finds position for next node
		nodeX += NODE_WIDTH * 1.5;
		if(nodeX + NODE_WIDTH > parent.canvas.getWidth()){
			nodeX = 10;
		}
	}
	
	/**
	 * deleteNode
	 * @param value - The value of the node to be deleted
	 * Removes named node (and corresponding edges) from BasicCanvas, if node 
	 * exists.  Calls "update" in case parent LMDisplay needs to be notified
	 * of change.
	 */
	public void deleteNode(String value){
		// remove tree
				parent.clear();  
				// remove list
				for(int i = 0; i < listNodes.size(); i++){
					Node_Basic node = listNodes.get(i);
					node.delete();
					if(value.equals(node.value)) {
						listNodes.remove(i);
					}	
				}
				for(int i = 0; i < listNodes.size(); i++){
					addNode(listNodes.get(i));
				}
				
				update();
	}

	@Override
	public void clear() {
		// Reset node positioning
		nodeX = 10; nodeY = 10;	
	}
}
