package webEditor.dst.client;

import java.util.ArrayList;

import webEditor.client.Proxy;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Evaluation_RadixSortWithHelp extends Evaluation implements IsSerializable
{
	final int TOP_BORDER = 200;
	private final String FIRST_INSTRUCTIONS = "A queue of data values is shown at the top of the display. " +    // Queuing instructions
					"Using the given digit position move each value to the " + 
					"appropriate bucket. Each bucket is a queue structure with " + 
					"the front below the label and the rear at the bottom of the screen.";
	private final String SECOND_INSTRUCTIONS = "Dequeue them back to the list in the correct order!";           // Dequeuing instructions
	int[] completedTasks = {0,0};
	
	int CURRENT_STEP = 0; 		//can be 0-5 representing where we are in the evaluation
	int CURRENT_COUNT = 0;		//represent where we are in the arguments array for counters
	int CURRENT_SOLUTION = 3;	//represent where we are in the arguments array for solutions
	
	public String evaluate(String problemName, String[] arguments, ArrayList<Node> nodes, ArrayList<EdgeParent> edges) {
		Node[] sortedOrderOfNodes;
		String solution = "";
		int[] counters = {0,0,0,0,0,0,0,0,0,0};
		
		if (CURRENT_STEP == 6) {
			return "You have finished!";
		}
		
        if (CURRENT_STEP % 2 == 0) { 		//begin queuing into buckets evaluation (even steps are queuing)
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
				String counts = arguments[CURRENT_COUNT]; //String representing correct number of nodes in each column
				
				if (counters[i] != Character.digit(counts.charAt(i), 10)) {
					Proxy.submitDST(problemName, 0);
					return "Feedback: You have "+counters[i]+" items in column "+i+" when you should have "+Character.digit(counts.charAt(i), 10);
				} else {
					/* Iterate through the nodes in the bucket, appending them to solution */
					for (int n : nodeMatrix[i]) {
						if (n != 0)
							solution += n;
					}
				}
			}
			
			if (solution.equals(arguments[CURRENT_SOLUTION])) {
				CURRENT_STEP++;
				updateProblemText();
				updateCounterPanel();
				return "Feedback: Your buckets are Correct!";
			} else {
				Proxy.submitDST(problemName, 0);
				return solution+"Feedback: Check the order of your buckets.";
			}
        } else if (CURRENT_STEP % 2 == 1) {      // Beginning of Dequeuing evaluation (odd steps are dequeuing)
        	solution = "";
        	for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(i).getTop() > TOP_BORDER) {
        			Proxy.submitDST(problemName, 0);
					return "Feedback: Make sure you have dequeued all the buckets completely.";
        		}
        	}
        	solution = getNodeOrder(nodes);
	        if (solution.equals(arguments[CURRENT_SOLUTION])) { //They dequeued correctly. Go on to next step.
	        	Proxy.submitDST(problemName, 0);
	        	CURRENT_STEP++;
	        	CURRENT_COUNT++;
	        	CURRENT_SOLUTION++;
	        	
	        	if (CURRENT_STEP == 6) { //we are done here
	        		Proxy.submitDST(problemName, 1);
	        		return "Feedback: Congratulations! You have finished!";
	        	}
	        	
				updateProblemText();
				updateCounterPanel();
	        	
	        	return "Feedback: You dequeued correctly!";
	        } else {											//They did not dequeue in the right order
	        	Proxy.submitDST(problemName, 0);
	        	return "Feedback: You have dequeued in the wrong order. Remember to dequeue the buckets from lowest " +
	        			"number to highest number, top to bottom.";
	        }
        } else {
            return "Feedback: We should never get here.";
        }
	}
    
	/**
	 * Update the problem text for queuing into buckets or dequeuing 
	 * from the buckets.
	 */
	public void updateProblemText() {
		String instructions = "";
		
		if (CURRENT_STEP % 2 == 0)
			instructions = FIRST_INSTRUCTIONS;
		else if (CURRENT_STEP % 2 == 1)
			instructions = SECOND_INSTRUCTIONS;
		
	    if (RootPanel.get().getWidget(1) instanceof TextArea)
	    	((TextArea) RootPanel.get().getWidget(1)).setText(instructions);
    }
	
	/**
	 * Update the upper-right hand panel that keeps track of which digit 
	 * we're currently processing.
	 */
	public void updateCounterPanel(){
    	if (RootPanel.get().getWidget(2) instanceof TextArea)
    		((TextArea) RootPanel.get().getWidget(2)).setText("Current Digit: "+(CURRENT_SOLUTION - 2));
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
    		
    		solution += minNode.getLabel().getText();
    		copy.remove(minNode);
    	}
    	
    	return solution;
    }
    
    /**
     * Used with SearchDisplayManager to figure out where to put nodes when calling reset()
     * @return 1 or 0, depending on which set of values are needed from problem
     */
    public int getCurrent(){
    	return CURRENT_STEP;
    }
}
