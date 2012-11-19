package webEditor.dst.client;

import java.util.ArrayList;
import java.util.Stack;

import webEditor.client.Proxy;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.TextArea;

// TODO Get the pivot to show up as immobilized and GET THE FREAKING NODES TO SHOW UP!!!!
public class Evaluation_Quicksort extends Evaluation implements IsSerializable {

	int PASS = 1;
	int PIVOT;
	int[] intArray;
	String[] stringArray;
	ArrayList<int[]> partitionSteps;
	ArrayList<Integer> pivots;
	
	// TODO Fix this for the quicksort partition
	public String evaluate(String problemName, String[] arguments,
			ArrayList<Node> nodes, ArrayList<EdgeParent> edges) {
		intArray = getIntArrayFromString(arguments[0]);
        pivots = new ArrayList<Integer>();
		partitionSteps = quick(intArray, 0, intArray.length-1, pivots);
		PIVOT = pivots.get(PASS-1);
		updateCounterPanel();
		
		String solution = getNodeOrder(nodes);
		String cSolution;
		cSolution = getStringFromIntArray(partitionSteps.get(PASS));
		if (solution.equals(cSolution)) {
			if (solutionInOrder(getIntArrayFromString(solution))) {
//				immobilizeNode(nodes, solution);
				Proxy.submitDST(problemName, 1);
			}
//			immobilizeNode(nodes, solution);
			PIVOT = pivots.get(PASS-1);
			PASS++;
			updateCounterPanel();
			return "Congratulations! Pass " + PASS + " successful!";
		} else {
			String correctSection = getCorrectSection(solution, cSolution);
			Proxy.submitDST(problemName, 0);
			return "Feedback: Incorrect. You were correct for the section: "
					+ correctSection;
		}
	}

	/**
	 * getCorrectSection: returns the part of the student's
	 * solution that is correct thus far
	 * @param solution The student's solution
	 * @param correct The correct solution
	 * @return The section of the student's solution that is correct
	 */
	public String getCorrectSection(String solution, String correct) {
		String cSection = "";
		String[] splitSolution = solution.split(" ");
		String[] splitCorrect = correct.split(" ");
		boolean incorrect = true;
		for (int i = 0; i < splitSolution.length; i++) {
			if (incorrect == true) {
				if (splitSolution[i].equals(splitCorrect[i])) {
					cSection += splitSolution[i] + " ";
				} else {
					incorrect = false;
				}
			}
		}

		return cSection;
	}
	
	/**
	 * getIntArrayFromNodes: returns an array of ints that are the values,
	 * in order, from the list of nodes that the student has created
	 * @param nodes The nodes from the student
	 * @return An array with the int values from the nodes
	 */
	public int[] getIntArrayFromNodes(ArrayList<Node> nodes) {
		int[] intArray = new int[nodes.size() + 1];
		int count = 1;
		for (Node n : nodes) {
			intArray[count] = Integer.parseInt(n.getValue());
			count++;
		}
		return intArray;
	}

	/**
	 * getIntArrayFromString: returns an array of ints that are the values,
	 * in order, from the space separated string of nodes that the student has created
	 * @param nodes The nodes from the student
	 * @return An array with the int values from the nodes
	 */
	public int[] getIntArrayFromString(String nodes) {
		String[] splitNodes = nodes.split(" ");
		int[] intArray = new int[splitNodes.length];
		for (int i = 0; i < splitNodes.length; i++) {
			intArray[i + 1] = Integer.parseInt(splitNodes[i]);
		}
		return intArray;
	}
	
	/**
	 * getStringFromIntArray: returns a " " (space) separated string representing
	 * the nodes from the student's int array
	 * @param intArray The integer array to convert
	 * @return A string representation of the nodes
	 */
	public String getStringFromIntArray(int[] intArray) {
		String solution = "";
		for (int i = 1; i < intArray.length; i++) {
			solution += intArray[i] + " ";
		}
		return solution.trim();
	}
	
	/**
	 * getStringFromNodes: returns a " " (space) separated string representing
	 * the nodes from the student's list of nodes
	 * @param nodes The nodes from the student
	 * @return A string representation of the nodes
	 */
	public String getStringFromNodes(ArrayList<Node> nodes) {
		String solution = "";
		for (Node n : nodes) {
			solution += n.getValue() + " ";
		}
		return solution.trim();
	}
	
	/**
	 * getNodeOrder: gets the order of nodes in an ArrayList
	 * @param nodes The nodes to get the order of
	 * @return A space separated string of the nodes in order
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

			solution += minNode.getValue() + " ";
			copy.remove(minNode);
		}

		return solution.trim();
	}
	
	// TODO Get this to work with the pivot for quicksort... currently still
	// implemented for heap sort
	public void immobilizeNode(ArrayList<Node> nodes, String solution) {
		String[] splitSolution = solution.split(" ");
		String desiredNode = splitSolution[splitSolution.length - PASS];
		int index = -1;
		for (int i = nodes.size() - 1; i >= 0; i--) {

			if (nodes.get(i).getValue().equals(desiredNode)) {
				if (index == -1) {
					if (!(nodes.get(i).getLabel().getStylePrimaryName()
							.equals("immobilized_node")))
						index = i;
				} else {
					if (!(nodes.get(i).getLabel().getStylePrimaryName()
							.equals("immobilized_node"))) {
						if (nodes.get(i).getLeft() >= nodes.get(index)
								.getLeft()) {
							index = i;
						}
					}
				}
			}
		}
		NodeDragController.getInstance().makeNotDraggable(
				nodes.get(index).getLabel());
		nodes.get(index).getLabel().setStyleName("immobilized_node");
		if (PASS == nodes.size() - 1) {
			for (int i = 0; i < nodes.size(); i++) {
				if (!(nodes.get(i).getLabel().getStylePrimaryName()
						.equals("immobilized_node"))) {
					NodeDragController.getInstance().makeNotDraggable(
							nodes.get(i).getLabel());
					nodes.get(i).getLabel().setStyleName("immobilized_node");
				}
			}
		}

	}
	
	public boolean solutionInOrder(int[] solution){
		boolean inOrder=true;
		for(int i=1;i<solution.length-1;i++){
			if(solution[i]>solution[i+1]){
				inOrder=false;
			}
		}
		return inOrder;
	}
	
	public void updateCounterPanel() {
		if (Proxy.getDST().getWidget(3) instanceof TextArea) {
			((TextArea) Proxy.getDST().getWidget(3)).setText("Pivot: " + PIVOT + "\nCurrent Pass: "
					+ PASS);
		}
	}
	
	// TODO Implement this for quicksort partition.. actually don't think this
	// is even used
	public String getCurrentNodeString(String arg0, int left, int right) {
		intArray = getIntArrayFromString(arg0);
		if (PASS == 1) {
			return arg0;
		}
		else if(PASS >= 1){
			int[] tempArr = getIntArrayFromString(arg0);
			left = partition(tempArr, left, right);
			String currentString = getStringFromIntArray(tempArr);
			return currentString;
		}
		PASS--;
		String currentString = getStringFromIntArray(intArray);
		PASS++;
		intArray = getIntArrayFromString(arg0);
		return currentString;
	}

	/**
	 * partition method that splits an array to be sorted and swaps values
	 * to be on the proper side of the pivot, where the pivot is always
	 * the lower bound parameter.
	 * @param x
	 * @param lb
	 * @param ub
	 * @return The pivot
	 */
    public int partition(int x[], int lb, int ub)
    {
        int a, down, temp, up,pj;
        a=x[lb];
        up=ub;
        down=lb;
        while(down<up)
        {
            while(x[down]<=a && down<up)
                down=down+1;       
            while(x[up]>a)
                up=up-1;         
            if(down<up)
            {
                temp=x[down]; 
                x[down]=x[up];
                x[up]=temp;
            }
        }

        x[lb]=x[up];
        x[up]=a;

        pj=up;
        return (pj);
    }

    /**
     * iterative quicksort method that returns an array list that contains
     * all of the steps that resulted from the partitioning method
     * @param a The array to sort
     * @param lb Lower bound of the array
     * @param ub Upper bound of the array
     * @return An ArrayList of steps
     */
    public ArrayList<int[]> quick(int[] a, int lb, int ub, ArrayList<Integer> pivots)
    {
    	ArrayList<int[]> j = new ArrayList<int[]>();
        Stack<Integer> S = new Stack<Integer>();
        S.push(lb);
        S.push(ub);
        j.add(clone(a));
        while (!S.empty())
        {
            ub = (Integer)S.pop();
            lb = (Integer)S.pop();
            if (ub <= lb) continue;
            int i = partition(a, lb, ub);
            pivots.add(i);
            j.add(clone(a));

            if (i-lb > ub-i)
            {
                S.push(lb);
                S.push(i-1);
            }
            S.push(i+1);
            S.push(ub);
            if (ub-i >= i-lb)
            {
                S.push(lb);
                S.push(i-1);
            }
        }
        return j;
    }
    
    /**
     * Clones an array of ints, element by element
     * @param x
     * @return
     */
    private int[] clone(int[] x) {
    	int[] y = new int[x.length];
    	for (int i = 0; i < x.length; i++) {
    		y[i] = x[i];
    	}
    	return y;
    }
}
