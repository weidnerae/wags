package webEditor.dst.client;

import java.util.ArrayList;
import java.util.Stack;

import webEditor.client.Proxy;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;

public class Evaluation_Quicksort extends Evaluation implements IsSerializable {

	int PASS = 1;
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
//		Window.alert(printSteps());
		
		String solution = getNodeOrder(nodes);
		String cSolution = getStringFromIntArray(partitionSteps.get(PASS));
		if (solution.equals(cSolution)) {
			
			if (PASS == partitionSteps.size()-1 && solutionInOrder(getIntArrayFromString(solution))) {
				Proxy.submitDST(problemName, 1);
				return "Congratulations! Exercise completed.";
			}
			PASS++;
			highlightPivotNode(nodes);
			updateCounterPanel();
			return "Congratulations! Pass " + (PASS-1) + " successful!";
		} else {
			String correctSection = getCorrectSection(solution, cSolution);
			Proxy.submitDST(problemName, 0);
			return "Feedback: Incorrect. You were correct for the section: "
					+ correctSection;
			
		}
	}

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
	
	public int[] getIntArrayFromNodes(ArrayList<Node> nodes) {
		int[] intArray = new int[nodes.size() + 1];
		int count = 1;
		for (Node n : nodes) {
			intArray[count] = Integer.parseInt(n.getValue());
			count++;
		}
		return intArray;
	}

	public int[] getIntArrayFromString(String nodes) {
		String[] splitNodes = nodes.split(" ");
		int[] intArray = new int[splitNodes.length];
		for (int i = 0; i < splitNodes.length; i++) {
			intArray[i + 1] = Integer.parseInt(splitNodes[i]);
		}
		return intArray;
	}

	public String getStringFromIntArray(int[] intArray) {
		String solution = "";
		for (int i = 1; i < intArray.length; i++) {
			solution += intArray[i] + " ";
		}
		return solution.trim();
	}
	
	public String getStringFromNodes(ArrayList<Node> nodes) {
		String solution = "";
		for (Node n : nodes) {
			solution += n.getValue();
		}
		return solution.trim();
	}

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

	/**
	 * no idea if this works, but it should highlight the pivot node for the student
	 */
	public void highlightPivotNode(ArrayList<Node> nodes) {
		for (Node x : nodes) {
			x.getLabel().setStyleName("node");
		}
		
		int value = (partitionSteps.get(PASS)[pivots.get(PASS)]);
		int i = 0;
		for (i = 0; i < intArray.length; i++) {
			if (nodes.get(i).getValue().equals(value + ""))
				break;
		}

		nodes.get(i)
			.getLabel().setStyleName("immobilized_node");
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
			((TextArea) Proxy.getDST().getWidget(3)).setText("Current Pass: "
					+ (PASS));
		}
	}
	
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
		String currentString = getStringFromIntArray(intArray);
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
        while (!S.empty())
        {
            ub = (Integer)S.pop();
            lb = (Integer)S.pop();
            if (ub <= lb) continue;
            int i = partition(a, lb, ub);
            pivots.add(i);
            j.add(cloneArray(a));

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
    
    private int[] cloneArray(int[] x) {
    	int[] y = new int[x.length];
    	for (int i = 0; i < x.length; i++) {
    		y[i] = x[i];
    	}
    	return y;
    }
    
    public int getInitialPivot(String arg0) {
    	int x[] = getIntArrayFromString(arg0);
    	int pivot = partition(x, 0, x.length-1);
//    	Window.alert("InitialPivot: " + pivot);

    	return pivot;
    }
    
    private String printSteps() {
    	StringBuffer s = new StringBuffer();
    	for (int i=0; i<partitionSteps.size(); i++) {
    		s.append(getStringFromIntArray(partitionSteps.get(i)) + "\n");
    	}
    	return s.toString();
    }
    
    public int getCurrentStep() {
    	return PASS;
    } 
}
