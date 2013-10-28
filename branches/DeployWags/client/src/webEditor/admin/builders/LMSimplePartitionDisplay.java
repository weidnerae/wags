package webEditor.admin.builders;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class LMSimplePartitionDisplay extends BasicDisplay {
	ArgPanel argSolution;
	
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
		// Override argHolder defaults, as it is being used a different way
		basePanel.add(hpnl);
		
		argSolution = new ArgPanel();
		argSolution.setup("Solution", "Submit");
		
		basePanel.add(argSolution);
		
		argSolution.btnArg.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				fillBuilder(argSolution);
				builder.uploadLM();
			}
		});
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
		
      while(leftIndex < rightIndex) {
         while (leftIndex < nodeVals.length-1 && nodeVals[leftIndex] < 0)   
         { 
            leftIndex++;
         }
         while (rightIndex > 0 && nodeVals[rightIndex] >= 0)   
         {
            rightIndex--;
         }
         if (leftIndex < rightIndex)
         {
             int temp = nodeVals[leftIndex];
             nodeVals[leftIndex] = nodeVals[rightIndex];
             nodeVals[rightIndex] = temp;
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
