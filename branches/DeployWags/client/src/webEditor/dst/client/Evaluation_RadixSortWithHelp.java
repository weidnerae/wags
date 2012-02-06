package webEditor.dst.client;

import java.util.ArrayList;

import webEditor.client.Proxy;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Evaluation_RadixSortWithHelp extends Evaluation implements IsSerializable
{
	final int TOP_BORDER = 200;
	String solution="";
	int[] completedTasks = {0,0};
	private final String SECOND_INSTRUCTIONS = "Deque them back to the list in the correct order!";
	public String evaluate(String problemName, String[] arguments, ArrayList<Node> nodes, ArrayList<EdgeParent> edges){
		int[] sortedOrderOfNodes;
		int[] counters = {0,0,0,0,0,0,0,0,0,0};
        if(completedTasks[0]==0){
        	int[][] nodeMatrix = new int[10][nodes.size()];
        	sortedOrderOfNodes = sortNodesByHeight(nodes);
        	for(int i=0;i<sortedOrderOfNodes.length;i++){
				if (nodes.get(i).getTop() < TOP_BORDER) {
					Proxy.submitDST(problemName, 0);
					return "Feedback: You must put all the numbers in a column";
				} else {
					
					int col = nodes.get(i).getLeft() / (nodes.get(i).getLabel().getParent().getOffsetWidth() / 10); //figure out which column the node is in
					nodeMatrix[col][counters[col]] = Integer.parseInt(nodes.get(i).getLabel().getText()); //put num in nodeMatrix
					counters[col]++;
				}
			}
			for(int i=0;i<10;i++){
				if (counters[i] != (int)(arguments[0].charAt(i))) {
					Proxy.submitDST(problemName, 0);
					return "Feedback: You have "+counters[i]+" items in column "+i+" when you should have "+arguments[0].charAt(i);
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
        else{
            return"Feedback: Correct!";
        }

	}
    public void updateProblemText(){
    	if(RootPanel.get().getWidget(1) instanceof TextArea)
    	((TextArea)RootPanel.get().getWidget(1)).setText(SECOND_INSTRUCTIONS);
    }
    public int[] sortNodesByHeight(ArrayList<Node> nodes) {
    		int[] sortedOrderOfNodes = new int[nodes.size()];
    		Node minNode = nodes.get(0);
    		for(int i=0;i<nodes.size();i++){
	    		for(Node n: nodes){
	    			if(n.getTop()>=minNode.getTop())
	    				minNode=n;
	    		}
	    		sortedOrderOfNodes[i]=nodes.indexOf(minNode);
    		}
    	return sortedOrderOfNodes;
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
        public String getNodeOrder(ArrayList<Node> nodes){
        	String solution="";
        	Node[] sortedNodes= new Node[nodes.size()];
        	int min=0;
        	Node minNode=null;
        	int counter=0;
        	while(!nodes.isEmpty()){
	        	for(Node n: nodes){
	        		if(n.getLeft() >min){
	        			minNode=n;
	        			min=n.getLeft();
	        		}
	        	}
	        	sortedNodes[counter]=minNode;
	        	counter++;
	        	nodes.remove(minNode);
        	}
        	for(Node n: sortedNodes){
        		solution+=n.getLabel();
        	}
        	return solution;
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
