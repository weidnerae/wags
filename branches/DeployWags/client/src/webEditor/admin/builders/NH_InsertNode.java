package webEditor.admin.builders;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;

public class NH_InsertNode extends NodeHandler {
	boolean duplicates;
	int curXLevel, curYLevel, listY, nodeNum = 0;
	Node_Basic curParent;
	String nodes = "";
	ArrayList<Node_Basic> listNodes;
	final int MAX_LEVELS = 4;
	final int Y_OFFSET = 50;
	final int HALF_NODE = 20;
	final int LIST_X = 15;
	final int LIST_Y_SPACE = 12;
	
	public NH_InsertNode(BasicCanvas canvas) {
		this.parent = canvas;
		duplicates = false;
		listNodes = new ArrayList<Node_Basic>();
	}

	@Override
	public void addNode(String value) {
		// For now, we'll say no duplicates
		if(!duplicates){
			for(Node_Basic node: parent.nodes){
				if(node.value.equals(value)){
					Window.alert("Duplicate nodes not allowed!");
					return;
				}
			}
		}
		
		nodes += value;
		// Reset positioning for this node
		curXLevel = 0; curYLevel = 0; curParent = null;
		Node_Basic node = new Node_Basic(value, parent);
		Node_Basic root = parent.getRoot();
		// Places node in resulting tree
		if(positionNode(node, root)){
			// Places node on left side of screen to show order of inserts
			listNode(new Node_Basic(value, parent));
		}
	}
	
	private boolean positionNode(Node_Basic node, Node_Basic root){
		if(root != null){
			curYLevel++;
			if(curYLevel >= MAX_LEVELS){
				Window.alert("Tree too deep!");
				return false;
			}
			
			curParent = root;
			if(node.value.compareTo(root.value) < 0){
				curXLevel = curXLevel * 2; // Right child placement
				return positionNode(node, root.leftChild);
			} else {
				curXLevel = curXLevel * 2 + 1;  // Left child placement
				return positionNode(node, root.rightChild);
			}
		} else {
			// Actually calculating position in canvas
			int yPos = parent.canvas.getHeight() / MAX_LEVELS * curYLevel + Y_OFFSET - HALF_NODE;
			int numPartitions = (int) Math.pow(2.0, curYLevel) + 1;
			int xPos = parent.canvas.getWidth() / numPartitions * (curXLevel + 1) - HALF_NODE;
			
			node.setPosition(xPos, yPos);
			parent.canvasPanel.add(node, node.xPos, node.yPos);
			parent.nodes.add(node);
			if(curParent != null) parent.addEdge(node, curParent);
			return true;
		}
	}
	
	private void listNode(Node_Basic node){
		node.setPosition(LIST_X, LIST_Y_SPACE * (nodeNum + 1) + (HALF_NODE * 2) * nodeNum);
		parent.canvasPanel.add(node, node.xPos, node.yPos);
		listNodes.add(node);
		nodeNum++;
	}

	@Override
	public void deleteNode(String value) {
		// remove tree
		parent.clear();  
		
		// remove list
		for(Node_Basic node:listNodes){
			node.delete();
		}
		nodeNum = 0;
		
		String tmpNodes = nodes;
		nodes = "";
		tmpNodes = tmpNodes.replace(value, "");
		for(int i = 0; i < tmpNodes.length(); i++){
			addNode(tmpNodes.charAt(i) + "");
		}
		
		update();
	}

	@Override
	public void clear() {
		for(Node_Basic node:listNodes){
			node.delete();
		}
	}

}
