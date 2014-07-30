package webEditor.admin.builders;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class LMQuickSortDisplay extends BasicDisplay {
	int pivot;
	boolean pivotSet;
	ArgPanel pnlPivot;
	ArgPanel argSolution;
	Label curPivot;
	
	@Override
	public void construct() {
		canvas.setEdgeHandler(new EH_NoEdges());
		canvas.nodeHandler.setUnique(false);
		canvas.nodeHandler.setNumbers(true);
		// As these will be numbers, we need to allow a greater range of digits
		txtAddNode.setMaxLength(3);
		
		txtInstructions.setText("Create a SimplePartition problem by adding" +
		" the nodes to be sorted.  Nodes are evaluated left to right, not" +
		" order of addition.  After setting the pivot, click 'Solve' to" +
		" generate the solution for this particular instance of the problem.");
		txtDesc.setText("Partition the data so that negative values appear" + 
		" on the left and positive values (including 0) appear on the right." + 
		"  The data does not need to be sorted.  Click the the blue arrow to" + 
		" move the blue index to the right and the red arrow to move the red" + 
		" index to the left. When the indices point to two values that" +
		" need to be swapped, click the Swap button. Make sure the pointers" +
		" come together before clicking Finalize.");
		
		/******************
		 * ADDING WIDGETS *
		 ******************/
		HorizontalPanel hpnl = new HorizontalPanel();
		pnlPivot = new ArgPanel();
		pnlPivot.setup("Pivot: ", "Set");
		pnlPivot.btnArg.addClickHandler(new SetPivotHandler(this, pnlPivot.txtArg));
		// Override argHolder defaults, as it is being used a different way
		pnlPivot.txtArg.setReadOnly(false);
		pnlPivot.btnArg.setEnabled(true);
		curPivot = new Label("Current Pivot: 0");
		curPivot.getElement().getStyle().setPaddingTop(5, Unit.PX);
		
		hpnl.add(pnlPivot);
		hpnl.add(curPivot);
		baseCol.add(hpnl);
		
		argSolution = new ArgPanel();
		argSolution.setup("Solution", "Submit");
		
		baseCol.add(argSolution);
		
		argSolution.btnArg.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				fillBuilder(argSolution);
				builder.uploadLM();
			}
		});
	}
	
	private class SetPivotHandler implements ClickHandler{
		LMQuickSortDisplay display;
		TextBox txtPivot;
		
		public SetPivotHandler(LMQuickSortDisplay d, TextBox t){
			display = d;
			txtPivot = t;
		}

		@Override
		public void onClick(ClickEvent event) {
			try{
				pivot = Integer.parseInt(txtPivot.getText());
			} catch (NumberFormatException e){
				txtPivot.setText("");
				Window.alert("Invalid pivot value!");
				return;
			}
			
			pivotSet = true;
			display.pivot = pivot;
			curPivot.setText("Current Pivot: " + pivot);
		}
	}

	@Override
	public void calculate() {	
		argSolution.fillText(runSimplePartition());
	}

	@Override
	public void fillBuilder(ArgHolder child) {
		builder.setTitle(txtTitle.getText());
		builder.setProblemText(txtDesc.getText());
		for(Node_Basic node: canvas.nodes){
			builder.addNode(node.value);
		}
		builder.setArgs(child.getArguments());
	}

	@Override
	public void onModify() {
		argSolution.clear();
	}

	@Override
	public void clear() {
		pivotSet = false;
		pivot = 0;
		pnlPivot.txtArg.setText("");
		curPivot.setText("Current Pivot: 0");
		argSolution.clear();
	}
	
	
	/**************************
	 * SIMPLE PARTITION LOGIC *
	 **************************/
	private String runSimplePartition(){
		int leftIndex = 0;
		int rightIndex = canvas.nodes.size() - 1;
		int[] nodeVals = new int[canvas.nodes.size()];
		int[] nodePos = new int[canvas.nodes.size()];
		
		// Put values into an array
		for(int i = 0; i < canvas.nodes.size(); i++){
			nodeVals[i] = Integer.parseInt(canvas.nodes.get(i).getText());
			nodePos[i] = canvas.nodes.get(i).xPos;
		}
		
		// Sort by xPositions, a selection sort implemented on the fly
		// Basically, we have two arrays - one of positions, one of values
		// We sort the array of positions least to greatest, and make sure 
		// the array of vals undergoes the same transformations
		int selIndex = 0;
		while(selIndex < nodePos.length){
			int min = nodePos[selIndex];
			int minIndex = selIndex;
			
			for(int i = selIndex; i < nodePos.length; i++){
				if(nodePos[i] < min){
					minIndex = i;
					min = nodePos[minIndex];
				}
			}
			
			int tmpPos = nodePos[selIndex];
			int tmpVal = nodeVals[selIndex];
			nodePos[selIndex] = min;
			nodeVals[selIndex] = nodeVals[minIndex];
			nodePos[minIndex] = tmpPos;
			nodeVals[minIndex] = tmpVal;
			
			selIndex++;
		}
		
		// Run the actual algorithm
		while(leftIndex < rightIndex){
			while(nodeVals[leftIndex] < pivot && leftIndex < nodeVals.length - 1){
				leftIndex++;
			}
			while(nodeVals[rightIndex] >= pivot && rightIndex > 0){
				rightIndex--;
			}
			if(leftIndex < rightIndex){
				int tmp = nodeVals[leftIndex];
				nodeVals[leftIndex] = nodeVals[rightIndex];
				nodeVals[rightIndex] = tmp;
			}
		}
		
		// Convert solution to a string
		String ans = "";
		for(int i = 0; i < nodeVals.length; i++){
			ans += nodeVals[i] + " ";
		}
		ans = ans.substring(0, ans.length() - 1);
		
		return ans;	
	}

}
