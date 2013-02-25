package webEditor.dst.client;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;

import webEditor.client.Proxy;

public class Evaluation_SimplePartition extends Evaluation implements IsSerializable {
	int PASS = 0;
	static int lb = 0;
	static int ub = 9;

	public String evaluate(String problemName, String[] arguments,
			ArrayList<Node> nodes, ArrayList<EdgeParent> edges) {
		
		int[] student = getIntArrayFromString(getNodeOrder(nodes));		
		int[] solution = getSolution(getIntArrayFromString(arguments[0].trim()));
		
		Window.alert("Student:" + printArray(student));
		Window.alert("Solution:" + printArray(solution));
		if (equalArrays(student, solution)) {
			// They were right
			PASS++;
			
				Proxy.submitDST(problemName, 1);
				return "Congratulations! You have completed this exercise.";
		} else {
			// They were wrong
			Proxy.submitDST(problemName, 0);
			return "Sorry, try again.";
		}
	}

	/**
	 * Simply switches two nodes if the one on the left is positive and the one
	 * on the right is negative
	 * @param arr the argument array
	 * @return
	 */
	private int[] getCurrentStep(int[] arr) {
		int temp;
		for (int i = 0; i < PASS; i++) {
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
	
	private int[] getSolution(int[] arr) {
		int temp, lb = 0, ub = 9;
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
	
	private String printArray(int[] x){
		String arr = "";
		for (int y : x) {
			arr += " " + y;
		}
		return arr;
	}
	
}
