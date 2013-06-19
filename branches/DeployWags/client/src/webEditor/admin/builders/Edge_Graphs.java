package webEditor.admin.builders;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Edge_Graphs extends Edge_Basic {
	DialogBox weightBox;
	Label weightLabel = new Label("");
	final boolean DEBUG = true;
	final int nodeWidth = 40;  // value found via css
	
	public Edge_Graphs(Node_Basic n1, Node_Basic n2, BasicCanvas canvas) {
		super(n1, n2, canvas);
		
		weightBox = constructWeightBox();
		placeLabel(weightLabel);
		weightBox.center();
	}
	
	private void placeLabel(Label weightLabel){
		// get width of edge
		int width = (n1.getAbsoluteLeft() - n2.getAbsoluteLeft() >= 0) ?
				n1.getAbsoluteLeft() - n2.getAbsoluteLeft() :
				n2.getAbsoluteLeft() - n1.getAbsoluteLeft();

		// get height of edge
		int height = (n1.getAbsoluteTop() - n2.getAbsoluteTop() >= 0) ?
				n1.getAbsoluteTop() - n2.getAbsoluteTop() :
				n2.getAbsoluteTop() - n1.getAbsoluteTop();
				
		int left = (n1.getAbsoluteLeft() < n2.getAbsoluteLeft()) ? 
				n1.getAbsoluteLeft() : n2.getAbsoluteLeft();
				
		int top = (n1.getAbsoluteTop() < n2.getAbsoluteTop()) ?
				n1.getAbsoluteTop() : n2.getAbsoluteTop();
		
		left = left - canvas.canvasPanel.getAbsoluteLeft();
		top = top - canvas.canvasPanel.getAbsoluteTop();
		
		int eMidX = (width + nodeWidth) / 2 + left;
		int eMidY = (height + nodeWidth) / 2 + top;
		
		canvas.canvasPanel.add(weightLabel, eMidX, eMidY);
		weightLabel.setStyleName("edge_weight");
	}
	
	private DialogBox constructWeightBox(){
		DialogBox box = new DialogBox();
		HorizontalPanel pnl = new HorizontalPanel();
		VerticalPanel vpnl = new VerticalPanel();
		
		Label lblWeight = new Label("Edge weight: ");
		TextBox txtWeight = new TextBox();
		txtWeight.setMaxLength(2);
		Button btnWeight = new Button("Set");
		btnWeight.addClickHandler(new btnWeightClickHandler(this, txtWeight));
		
		pnl.add(lblWeight);
		pnl.add(txtWeight);
		vpnl.add(pnl);
		vpnl.add(btnWeight);
		box.add(vpnl);
		
		return box;
	}
	
	private class btnWeightClickHandler implements ClickHandler{
		private Edge_Graphs edge;
		private TextBox txtWeight;
		
		public btnWeightClickHandler(Edge_Graphs edge, TextBox txt){
			this.edge = edge;
			this.txtWeight = txt;
		}
		
		@Override
		public void onClick(ClickEvent event) {
			int weight = -1;
			
			try{
				weight = Integer.parseInt(txtWeight.getText());
			} catch (NumberFormatException e){
				txtWeight.setText("");
				Window.alert("Invalid number!");
				return;
			}
			
			edge.weightLabel.setText(weight + "");
			weightBox.hide();
		}
		
	}

	@Override
	protected void onDelete() {
		// TODO Auto-generated method stub

	}

}
