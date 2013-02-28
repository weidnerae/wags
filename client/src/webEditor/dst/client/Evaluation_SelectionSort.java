package webEditor.dst.client;

import java.util.ArrayList;

import webEditor.client.Proxy;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Evaluation_SelectionSort extends Evaluation implements IsSerializable {
	
	int step = 0;

	public String evaluate(String problemName, String[] arguments,
			ArrayList<Node> nodes, ArrayList<EdgeParent> edges) {
		String node = arguments[0].trim();
		step = Integer.valueOf(arguments[1]);
		
		int selected = Integer.valueOf(node);
		int smallest = getNextSmallest(nodes);
		
		if (smallest == selected) {
			// They were right
			step++;
			
			if (step == nodes.size() - 1) {
				Proxy.submitDST(problemName, 1);
				return "Congratulations! You have completed this exercise.";
			} else {
				return "";
			}
		} else {
			// They were wrong
			Proxy.submitDST(problemName, 0);
			return "Sorry, try again.";
		}
	}
	
	private int getNextSmallest(ArrayList<Node> nodes) {
		int min = Integer.valueOf(nodes.get(step).getValue());
		
		for (int i = step + 1; i < nodes.size(); i++) {
			int val = Integer.valueOf(nodes.get(i).getValue());
			
			if (val < min) {
				min = val;
			}
		}
		
		return min;
	}

	@Override
	public int returnKeyValue() {
		return DSTConstants.SELECTIONSORT_KEY;
	}
}
