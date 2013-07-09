package webEditor.admin.builders;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TextBox;

public class LMQuickSortDisplay extends BasicDisplay {
	int pivot;
	@Override
	public void construct() {
		canvas.setEdgeHandler(new EH_NoEdges());
		
		ArgPanel pnlPivot = new ArgPanel();
		pnlPivot.setup("Pivot: ", "Set");
		pnlPivot.btnArg.addClickHandler(new SetPivotHandler(this, pnlPivot.txtArg));
		// Override argHolder defaults, as it is being used a different way
		pnlPivot.txtArg.setReadOnly(false);
		pnlPivot.btnArg.setEnabled(true);
		
		basePanel.add(pnlPivot);
		
		ArgPanel argSolution = new ArgPanel();
		argSolution.setup("Solution", "Submit");
		
		basePanel.add(argSolution);
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
			int pivot = -1;
			try{
				pivot = Integer.parseInt(txtPivot.getText());
			} catch (NumberFormatException e){
				txtPivot.setText("");
				Window.alert("Invalid pivot value!");
				return;
			}
			
			display.pivot = pivot;
		}
	}

	@Override
	public void calculate() {
		for(Node_Basic node: canvas.nodes){
			Window.alert(node.value);
		}
	}

	@Override
	public void fillBuilder(ArgHolder child) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onModify() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}
	
	
	/**************************
	 * SIMPLE PARTITION LOGIC *
	 **************************/

}
