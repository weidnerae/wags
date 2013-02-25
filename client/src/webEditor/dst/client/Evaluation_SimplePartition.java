/*
 * Reed Phillips - 2013
 * 
 * This class is used for the evaluation of a Negative-Positive Partition microlab.
 * This works by swapping negative and positive numbers (with zero as positive) so that
 * all negatives are on the left and all positives are on the right.
 * 
 * This version allows students to only finalize once they have the complete answer 
 * he/she believes is correct, it does not handle force evaluation.
 */
package webEditor.dst.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

import webEditor.client.Proxy;

public class Evaluation_SimplePartition extends Evaluation implements IsSerializable {
	static int lb = 0;
	static int ub = 9;

	public String evaluate(String problemName, String[] arguments,
			ArrayList<Node> nodes, ArrayList<EdgeParent> edges) {
		
		int[] student = getIntArrayFromString(getNodeOrder(nodes));		
		int[] solution = getSolution(getIntArrayFromString(arguments[0].trim()));

		if (equalArrays(student, solution)) {
			// They were right
			Proxy.submitDST(problemName, 1);
			return "Congratulations! You have completed this exercise.";
		} else {
			// They were wrong
			Proxy.submitDST(problemName, 0);
			return "Sorry, try again.";
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
		while (lb <= ub) {
			while (arr[lb] < 0 && lb < ub)
				lb++;
			while (arr[ub] >= 0)
				ub--;
			if (lb < ub) {
                temp=arr[lb]; 
                arr[lb]=arr[ub];
                arr[ub]=temp;
			}
		}
		
		return arr;
	}
	
	private boolean equalArrays(int[] a1, int[] a2) {
		if (a1.length != a2.length)
			return false;
		for (int i = 0; i < a1.length; i++) {
			if (a1[i] != a2[i])
				return false;
		}
		return true;
	}
}
