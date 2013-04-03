package webEditor.logical.TreeProblems.HeapProblems;

import java.util.ArrayList;

import webEditor.Proxy;
import webEditor.logical.DSTConstants;
import webEditor.logical.EdgeParent;
import webEditor.logical.Evaluation;
import webEditor.logical.Node;
import webEditor.logical.NodeDragController;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.TextArea;

public class Evaluation_HeapSort extends Evaluation implements IsSerializable {
	int CURRENT_STEP = 1;
	int PASS = 1;
	int[] intArray;
	String[] stringArray;
	HeapSorter heapSort = new HeapSorter();

	public String evaluate(String problemName, String[] arguments,
			ArrayList<Node> nodes, ArrayList<EdgeParent> edges) {
		String solution = getNodeOrder(nodes);
		intArray = getIntArrayFromString(arguments[0]);
		String cSolution;
		if (CURRENT_STEP == 1 && !isHeap(intArray)) {
			intArray = reheapStart(intArray);
			cSolution = getStringFromIntArray(intArray);
			if(solution.equals(cSolution)){
				CURRENT_STEP++;
				updateCounterPanel();
				return "Congratulations! You may now start sorting your MaxHeap!";
			}
			else{
				String correctSection = getCorrectSection(solution, cSolution);
				if (correctSection.equals("")) {

					Proxy.submitDST(problemName, 0);
					return "Feedback: Incorrect. Hint: This array is not a MaxHeap, "+
					"you have to heapify it first.";
				}
				return "Feedback: Incorrect. You were correct for the section: "
						+ correctSection;
			}
		} else {
			moveArrayLeft();
			heapSort.sort(intArray);
			moveArrayRight();
			cSolution = getStringFromIntArray(intArray);
			if (solution.equals(cSolution)) {
				if (solutionInOrder(getIntArrayFromString(solution))) {
					immobilizeNode(nodes, solution);
					Proxy.submitDST(problemName, 1);
					return "Congratulations! You have completed this exercise.";
				}
				immobilizeNode(nodes, solution);
				PASS++;
				CURRENT_STEP++;
				updateCounterPanel();
				return "You have successfully completed that pass.";
			} else {
				String correctSection = getCorrectSection(solution, cSolution);
				if (correctSection.equals("")) {
					String[] splitCSolution = cSolution.split(" ");
					Proxy.submitDST(problemName, 0);
					return "Feedback: Incorrect. Hint, the first item is "
							+ splitCSolution[0];
				}
				return "Feedback: Incorrect. You were correct for the section: "
						+ correctSection;
			}
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

	public class HeapSorter {
		private int[] a;
		private int n;

		public void sort(int[] a0) {
			a = a0;
			n = a.length;
			heapsort();
		}

		private void heapsort() {
			buildheap();
			int s = n - PASS;
			while (n > s) {
				n--;
				exchange(0, n);              
				downheap(0);
			}
		}

		private void buildheap() {
			for (int v = n / 2 - 1; v >= 0; v--)
				downheap(v);
		}

		private void downheap(int v) {
			int w = 2 * v + 1; // first descendant of v
			while (w < n) {
				if (w + 1 < n) // is there a second descendant?
					if (a[w + 1] > a[w])
						w++;
				// w is the descendant of v with maximum label

				if (a[v] >= a[w])
					return; // v has heap property
				// otherwise
				exchange(v, w); // exchange labels of v and w
				v = w; // continue
				w = 2 * v + 1;
			}
		}

		private void exchange(int i, int j) {
			int t = a[i];
			a[i] = a[j];
			a[j] = t;
		}

	} // end class HeapSorter

	public int[] getIntArrayFromNodes(ArrayList<Node> nodes) {
		int[] intArray = new int[nodes.size() + 1];
		int count = 1;
		for (Node n : nodes) {
			intArray[count] = Integer.parseInt(n.getValue());
			count++;
		}
		return intArray;
	}

	public String getStringFromNodes(ArrayList<Node> nodes) {
		String solution = "";
		for (Node n : nodes) {
			solution += n.getValue() + " ";
		}
		return solution.trim();
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

	public void updateCounterPanel() {
		if (Proxy.getDST().getWidget(3) instanceof TextArea) {
			((TextArea) Proxy.getDST().getWidget(3)).setText("Current Pass: "
					+ PASS);
		}
	}

	public String getCurrentNodeString(String arg0) {
		intArray = getIntArrayFromString(arg0);
		if (CURRENT_STEP == 1) {
			return arg0;
		}
		else if(CURRENT_STEP == 2 && PASS == 1){
			int[] tempArr = getIntArrayFromString(arg0);
			tempArr =reheapStart(tempArr);
			String currentString = getStringFromIntArray(tempArr);
			return currentString;
		}
		PASS--;
		moveArrayLeft();
		heapSort.sort(intArray);
		moveArrayRight();
		String currentString = getStringFromIntArray(intArray);
		PASS++;
		intArray = getIntArrayFromString(arg0);
		return currentString;
	}

	public int getCurrentStep() {
		return PASS;
	}

	public boolean isHeap(int[] ints) {
		boolean isHeap = true;

		for (int i = 1; (i * 2) < ints.length; i++) {
			if (ints[i] < ints[i * 2]) {
				isHeap = false;
			}
			if ((i * 2) + 1 < ints.length && ints[i] < ints[(i * 2) + 1]) {
				isHeap = false;
			}
		}
		return isHeap;
	}

	public int[] reheapStart(int[] heap) {
		int[] heapC = heap;
		for (int rootIndex = heap.length / 2; rootIndex > 0; rootIndex--) {
			heapC = reheap(heap, rootIndex, heap.length);
		}
		return heapC;

	}

	public int[] reheap(int[] heap, int root, int size) {
		int leftChildIndex = root * 2;
		int rightChildIndex = root * 2 + 1;
		int largest = root;

		if (leftChildIndex < size && heap[leftChildIndex] > heap[root]) {
			largest = leftChildIndex;
		}
		if (rightChildIndex < size && heap[rightChildIndex] > heap[largest]) {
			largest = rightChildIndex;
		}
		if (largest != root) {
			int tmp = heap[largest];
			heap[largest] = heap[root];
			heap[root] = tmp;
			reheap(heap, largest, size);

		}
		return heap;
	}
	public void moveArrayLeft(){
		int[] lInts = new int[intArray.length-1];
	    int count=0;
		for (int i=1;i<intArray.length;i++) {
			lInts[count] = intArray[i];
			count++;
		}
		intArray = lInts;
	}
	public void moveArrayRight(){
		int count = 1;
		int[] lInts = new int[intArray.length+1];
		for (int n : intArray) {
			lInts[count] = n;
			count++;
		}
		intArray = lInts;
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

	@Override
	public int returnKeyValue() {
		return DSTConstants.HEAPSORT_KEY;
	}

}
