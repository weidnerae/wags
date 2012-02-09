package webEditor.dst.client;

import java.util.ArrayList;

import webEditor.client.Proxy;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Evaluation_RadixSortWithHelp extends Evaluation implements IsSerializable
{
	final int TOP_BORDER = 200;
	int[] completedTasks = {0,0};
	private final String SECOND_INSTRUCTIONS = "Dequeue them back to the list in the correct order!";
	
	public String evaluate(String problemName, String[] arguments, ArrayList<Node> nodes, ArrayList<EdgeParent> edges) {
		Node[] sortedOrderOfNodes;
		String solution = "";
		int[] counters = {0,0,0,0,0,0,0,0,0,0};
		
        if (completedTasks[0] == 0) {
        	int[][] nodeMatrix = new int[10][nodes.size()];
        	
        	sortedOrderOfNodes = sortNodesByHeight(nodes);
        	
        	/* Check to make sure all the nodes are put into a bucket.
        	 * If they are in a bucket, add them to the nodeMatrix.
        	 */
        	for (int i = 0; i < sortedOrderOfNodes.length; i++) {
				if (sortedOrderOfNodes[i].getTop() < TOP_BORDER) {
					Proxy.submitDST(problemName, 0);
					return "Feedback: You must put all the numbers in a column";
				} else {
					Node n = sortedOrderOfNodes[i];
					int col = n.getLeft() / (n.getLabel().getParent().getOffsetWidth() / 10); //figure out which column the node is in
					nodeMatrix[col][counters[col]] = Integer.parseInt(n.getLabel().getText()); //put num in nodeMatrix
					counters[col]++; 
				}
			}
        	
        	/* Iterate through the buckets */
			for (int i = 0; i < 10; i++) {
				/* If there are the wrong number of nodes in a bucket */
				if (counters[i] != Character.digit(arguments[0].charAt(i), 10)) {
					Proxy.submitDST(problemName, 0);
					return "Counters: "+counters[0]+counters[1]+counters[2]+counters[3]+counters[4]+counters[5]+counters[6]+counters[7]+counters[8]+counters[9]+"Feedback: You have "+counters[i]+" items in column "+i+" when you should have "+Character.digit(arguments[0].charAt(i),10);
				} else {
					/* Iterate through the nodes in the bucket, appending them to solution */
					for (int n : nodeMatrix[i]) {
						if (n != 0)
							solution += n;
					}
				}
			}
			
			if (solution.equals(arguments[1])) { //84364382354363423567428799
				completedTasks[0] = 1;
				updateProblemText();
				return "Feedback: Your buckets are Correct!";
			} else {
				Proxy.submitDST(problemName, 0);
				return solution+"Feedback: Check the order of your buckets.";
			}
        } else if (completedTasks[0] == 1) {
        	solution = "";
        	
        	for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(i).getTop() > TOP_BORDER) {
        			Proxy.submitDST(problemName, 0);
					return "Feedback: Make sure you have dequeued all the buckets completely.";
        		}
        	}
	        
        	solution = getNodeOrder(nodes);
        	
	        if (solution.equals(arguments[1])) {
	        	Proxy.submitDST(problemName, 0);
	        	return "Feedback: You dequeued correctly!";
	        }
        } else {
            return"Feedback: END OF EVAL";
        }
        
        return "uh oh";
	}
    
	public void updateProblemText() {
    	if (RootPanel.get().getWidget(1) instanceof TextArea)
    		((TextArea)RootPanel.get().getWidget(1)).setText(SECOND_INSTRUCTIONS);
    }
    
	/**
	 * Return an array of Nodes, sorted by height
	 * @param nodes ArrayList of Nodes to be sorted
	 * @return sorted array
	 */
	public Node[] sortNodesByHeight(ArrayList<Node> nodes) {
		/* Copy nodes into a copy array so we leave original untouched */
		ArrayList<Node> copy = new ArrayList<Node>();
		
		for (Node n : nodes) {
			copy.add(new Node(n.getValue(), n.getLabel()));
		}
		
		/* Simple selection sort into a new Node array */
    	Node[] sortedNodes = new Node[copy.size()];
    	int i = 0;
    	
    	while (!copy.isEmpty()) {
    		Node minNode = copy.get(0);
    		
    		for (Node n : copy) {
    			if (n.getTop() <= minNode.getTop()) {
    				minNode = n;
    			}
    		}
    		
    		sortedNodes[i++] = minNode;
    		copy.remove(minNode);
    	}
    	
    	return sortedNodes;
    }
    
	/**
	 * Get the order of the nodes in the main queue.
	 * @param nodes ArrayList of nodes.
	 * @return String of concatenated node labels, which will correspond to
	 * the correct solution if they are in the correct order.
	 */
    public String getNodeOrder(ArrayList<Node> nodes) {
		/* Copy nodes into a copy array so we leave original untouched */
		ArrayList<Node> copy = new ArrayList<Node>();
		
		for (Node n : nodes) {
			copy.add(new Node(n.getValue(), n.getLabel()));
		}
		
		/* Simple selection sort, adding label to solution */
    	String solution = "";
    	
    	while (!copy.isEmpty()) {
    		Node minNode = copy.get(0);
    		
    		for (Node n : copy) {
    			if (n.getLeft() <= minNode.getLeft()) {
    				minNode = n;
    			}
    		}
    		
    		solution += minNode.getLabel();
    		copy.remove(minNode);
    	}
    	
    	return solution;
    }
/**
 * 		private final String SECOND_INSTRUCTIONS = "Deque them back to the list in the correct order!";
		int TOP_BORDER = 20;

        public String evaluate(String problemName, String[] arguments, ArrayList<Node> nodes, ArrayList<EdgeParent> edges)
        {
        if(completedTasks[0]==0){
        	nodes = sortNodesByHeight(nodes);
			int[][] nodeMatrix = new int[10][nodes.size()];
			int[] counters = {0,0,0,0,0,0,0,0,0,0};
			String solution="";
			
			for(Node n:nodes){
				if (n.getTop() < TOP_BORDER) {
					Proxy.submitDST(problemName, 0);
					return "Feedback: You must put all the numbers in a column";
				} else {
					
					int col = n.getLeft() / (n.getLabel().getParent().getOffsetWidth() / 10); //figure out which column the node is in
					nodeMatrix[col][counters[col]] = Integer.parseInt(n.getLabel().getText()); //put num in nodeMatrix
					counters[col]++;
				}
			}
			
			for(int i=0;i<10;i++){
				if (counters[i] != arguments[0].charAt(i)) {
					Proxy.submitDST(problemName, 0);
					return "Feedback: Check to see if you are missing any items or have too many items in column "+i;
				}
				else {
					for (int n : nodeMatrix[i]) {
						solution+=n;
					}
				}
			}
			
			if (solution.equals(arguments[1])) {
				completedTasks[0] = 1;
				updateProblemText();
				return "Feedback: Your buckets are Correct!";
			} else {
				Proxy.submitDST(problemName, 0);
				return "Feedback: Check the order of your buckets.";
			}
		}
        else if (completedTasks[0]==1) {
        	for (Node n:nodes) {
        		if (n.getTop() > TOP_BORDER) {
        			Proxy.submitDST(problemName, 0);
					return "Feedback: Make sure you have dequed all the buckets completely.";
        		}
        	}
	        String solution = getNodeOrder(nodes);
	        if(solution.equals(arguments[1])){
	        	Proxy.submitDST(problemName, 0);
	        	return "Feedback: You dequed correctly!";
	        }
        }
	       return ""; 
       }

        
        public ArrayList<Node> sortNodesByHeight(ArrayList<Node> nodes) {
        	ArrayList<Node> sortedNodes = new ArrayList<Node>();
        	Node minNode = nodes.get(0);
        	
        	while (!nodes.isEmpty()) {
        		for (Node n : nodes) {
        			if (n.getTop() <= minNode.getTop()) {
        				minNode = n;
        			}
        		}
        		sortedNodes.add(minNode);
        		nodes.remove(minNode);
        	}
        	return sortedNodes;
        }
        public void updateProblemText(){
        	((TextArea)RootPanel.get().getWidget(0)).setText(SECOND_INSTRUCTIONS);
        }
 */
}
