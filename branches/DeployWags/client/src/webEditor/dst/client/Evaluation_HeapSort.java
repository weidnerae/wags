package webEditor.dst.client;

import java.util.ArrayList;

import webEditor.client.Proxy;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

public class Evaluation_HeapSort extends Evaluation implements IsSerializable {
	int CURRENT_STEP = 1;
	int[] intArray;
	String[] stringArray;
	HeapSorter heapSort = new HeapSorter();

	public String evaluate(String problemName, String[] arguments,
			ArrayList<Node> nodes, ArrayList<EdgeParent> edges) {
		String solution = getNodeOrder(nodes);
		intArray = getIntArrayFromString(arguments[0]);
		heapSort.sort(intArray);
		String cSolution = getStringFromIntArray(intArray);
		if (solution.equals(cSolution)) {
			if (CURRENT_STEP == intArray.length - 1) {
				Proxy.submitDST(problemName, 1);
				return "Congratulations! You have completed this exercise.";
			}
			immobilizeNode(nodes, solution);
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
			int s = n - CURRENT_STEP;
			while (n > s) {
				n--;
				exchange(0, n);
				downheap(0);
				for (int n : intArray) {
					System.out.print(n + ", ");
				}
				System.out.println("<- " + n);
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
		int[] intArray = new int[nodes.size()];
		int count = 0;
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
			intArray[i] = Integer.parseInt(splitNodes[i]);
		}
		return intArray;
	}

	public String getStringFromIntArray(int[] intArray) {
		String solution = "";
		for (int i = 0; i < intArray.length; i++) {
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

	//PROBLEM!
	public void immobilizeNode(ArrayList<Node> nodes, String solution) {
		ArrayList<String> duplicateNodes = new ArrayList<String>();
		String[] splitSolution = solution.split(" ");
		String desiredNode = splitSolution[splitSolution.length - CURRENT_STEP];
		int index = 0;
		for (int i = nodes.size() - 1; i >= 0; i--) {
			if (nodes.get(i).getValue().equals(desiredNode) && !duplicateNodes.contains(desiredNode)) { //HERE?
				if (index == 0) {
					index = i;
				} else if (nodes.get(i).getLeft() > nodes.get(index).getLeft()
						&& !(nodes.get(i).getLabel().getStyleName()
								.equals("immobilized_node"))) {
					index = i;
				}
			}
		}
		duplicateNodes.add(nodes.get(index).getValue());
		NodeDragController.getInstance().makeNotDraggable(
				nodes.get(index).getLabel());
		nodes.get(index).getLabel().setStyleName("immobilized_node");

	}

	public void updateCounterPanel() {
		if (RootPanel.get().getWidget(2) instanceof TextArea) {
			((TextArea) RootPanel.get().getWidget(2)).setText("Current Pass: "
					+ CURRENT_STEP);
		}
	}

	public String getCurrentNodeString(String arg0) {
		intArray = getIntArrayFromString(arg0);
		if(CURRENT_STEP==1){
			return arg0;
		}
		CURRENT_STEP--;
		heapSort.sort(intArray);
		String currentString = getStringFromIntArray(intArray);
		CURRENT_STEP++;
		intArray = getIntArrayFromString(arg0);
		return currentString;
	}

	public int getCurrentStep() {
		return CURRENT_STEP;
	}
	

}
