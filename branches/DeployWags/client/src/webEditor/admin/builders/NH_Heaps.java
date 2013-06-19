package webEditor.admin.builders;

import com.google.gwt.user.client.Window;

public class NH_Heaps extends NodeHandler{
	int nodeYLevel = 0;
	int nodeXLevel = 0;
	int nodeNum = 0;
	final int MAX_LEVELS = 4;
	final int Y_OFFSET = 50;
	final int HALF_NODE = 20;
	String nodes = "";
	
	public NH_Heaps(BasicCanvas canvas) {
		this.parent = canvas;
	}

	/**
	 * addNode
	 * @param value - The value of the node that is displayed
	 * Adds a node to the BasicCanvas and registers it with the BasicCanvas.
	 * Relies on "positionNode(BasicNode)" to place node
	 * Calls "update()" in case parent LMDisplay must be notified of change
	 */
	public void addNode(String value) {
		// For traversals, we're going to force unique nodes
		for(Node_Basic node: parent.nodes){
			if(node.value.equals(value)){
				Window.alert("Duplicate nodes not allowed!");
				return;
			}
		}
		
		Node_Basic node = new Node_Basic(value, parent);
		parent.dragger.makeDraggable(node);
		parent.nodes.add(node);
		nodes += value;
		
		positionNode(node);
		update();
	}
	
	public void positionNode(Node_Basic node) {
		if(nodeYLevel >= MAX_LEVELS){
			// Logic for when autoplacement no longer works
		}
		
		int yPos = parent.canvas.getHeight() / MAX_LEVELS * nodeYLevel + Y_OFFSET - HALF_NODE;
		int numPartitions = (int) Math.pow(2.0, nodeYLevel) + 1;
		int xPos = parent.canvas.getWidth() / numPartitions * (nodeXLevel + 1) - HALF_NODE;
		
		
		if(nodeXLevel == numPartitions - 2){  // Start a new row. - 2 due to 0 base and 1 extra partition than position
			nodeYLevel++;
			nodeXLevel = 0;
		} else {							  // Next spot in row
			nodeXLevel++;
		}
		
		node.setPosition(xPos, yPos);
		parent.canvasPanel.add(node, node.xPos, node.yPos);
		
		// To draw edge to parent, get node ceiling(node index/2) - 1 (a la heaps)
	}

	@Override
	public void deleteNode(String value) {
		parent.clear();  // remove entire canvas
		String tmpNodes = nodes;
		nodes = "";
		
		// rebuild without removed node
		nodeXLevel = 0;
		nodeYLevel = 0;
		
		tmpNodes = tmpNodes.replace(value, "");
		for(int i = 0; i < tmpNodes.length(); i++){
			addNode(tmpNodes.charAt(i) + "");
		}
		
		update();
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

}
