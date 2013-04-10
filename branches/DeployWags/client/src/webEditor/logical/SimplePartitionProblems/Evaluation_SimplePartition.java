/*
 * Reed Phillips - 2013
 * 
 * This class is used for the evaluation of a Negative-Positive Partition microlab.
 * This works by swapping negative and positive numbers (with zero as positive) so that
 * all negatives are on the left and all positives are on the right.
 * 
 * This version allows students to only finalize once they have the complete answer 
 * he/she believes is correct, it does not check the student's answer after each swap.
 */
package webEditor.logical.SimplePartitionProblems;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

import webEditor.Proxy;
import webEditor.logical.EdgeParent;
import webEditor.logical.Evaluation;
import webEditor.logical.Node;

public class Evaluation_SimplePartition extends Evaluation implements IsSerializable {
	private int evalLB;
	private int evalUB;

	public String evaluate(String problemName, String[] arguments,
			ArrayList<Node> nodes, ArrayList<EdgeParent> edges) {
		evalLB = 0;
		evalUB = 9;
		
		int[] student = getIntArrayFromString(getNodeOrder(nodes));		
		int[] solution = getSolution(getIntArrayFromString(arguments[0].trim()));

		if (equalArrays(student, solution)) {
			// They were right
			Proxy.submitDST(problemName, 1);
			return "Congratulations! You have completed this exercise.";
		} else {
			// They were wrong
			Proxy.submitDST(problemName, 0);
			return "Incorrect. Remember that all negatives should be to the left of all positives";
		}
	}

	private int[] getIntArrayFromString(String nodes) {
		String[] splitNodes = nodes.split(" ");
		int[] intArray = new int[splitNodes.length];
		for (int i = 0; i < splitNodes.length; i++) {
			intArray[i] = Integer.parseInt(splitNodes[i]);
		}
		return intArray;
	}
	
	private String getNodeOrder(ArrayList<Node> nodes) {
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
	 * getSolution - Simply switches nodes to the left side if negative and
	 * the right side if positive.
	 * @param arr The array to partition
	 * @return solution
	 */
	private int[] getSolution(int[] arr) {
		int temp;
		while (evalLB <= evalUB) {
			while (arr[evalLB] < 0 && evalLB < evalUB)
				evalLB++;
			while (arr[evalUB] >= 0)
				evalUB--;
			if (evalLB < evalUB) {
                temp=arr[evalLB]; 
                arr[evalLB]=arr[evalUB];
                arr[evalUB]=temp;
			}
		}
		
		return arr;
	}
	
	/**
	 * This just checks to see if two arrays contain all equal elements
	 * @param a1
	 * @param a2
	 * @return
	 */
	private boolean equalArrays(int[] a1, int[] a2) {
		if (a1.length != a2.length)
			return false;
		for (int i = 0; i < a1.length; i++) {
			if (a1[i] != a2[i])
				return false;
		}
		return true;
	}

	@Override
	public int returnKeyValue() {
		// TODO Auto-generated method stub
		return 0;
	}
}
