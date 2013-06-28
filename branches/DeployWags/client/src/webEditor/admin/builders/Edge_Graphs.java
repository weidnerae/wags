package webEditor.admin.builders;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
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
	int weight;
	final static int NO_EDGE = 100;  // as weights are limited to 2 digits, this = max
	private static int[][] adjMatrix;
	private static ArrayList<Edge_Graphs> edges = new ArrayList<Edge_Graphs>();
	
	public Edge_Graphs(Node_Basic n1, Node_Basic n2, BasicCanvas canvas) {
		super(n1, n2, canvas);
		
		weightBox = constructWeightBox();
		placeLabel(weightLabel);
	}
	
	private void placeLabel(Label weightLabel){
		/*  Series of workarounds for getOffSetWidth/Height not working */
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
	}
	
	private DialogBox constructWeightBox(){
		DialogBox box = new DialogBox();
		HorizontalPanel pnl = new HorizontalPanel();
		VerticalPanel vpnl = new VerticalPanel();
		
		Label lblWeight = new Label("Edge weight: ");
		TextBox txtWeight = new TextBox();
		
		
		txtWeight.setMaxLength(2);
		final Button btnWeight = new Button("Set");
		btnWeight.addClickHandler(new btnWeightClickHandler(this, txtWeight));
		
		pnl.add(lblWeight);
		pnl.add(txtWeight);
		vpnl.add(pnl);
		vpnl.add(btnWeight);
		box.add(vpnl);
		
		txtWeight.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)
				{
					btnWeight.click();
				}
			}
		});
		
		box.center();
		txtWeight.setFocus(true);
		
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
			

			edge.weight = weight;
			edge.weightLabel.setText(weight + "");
			weightLabel.setStyleName("edge_weight");
			weightBox.hide();

			// Update static fields for later use
			Edge_Graphs.updateMatrix(edge);
			// Insert sorted
			int i = 0;
			while(i < Edge_Graphs.edges.size() 
					&& Edge_Graphs.edges.get(i).weight < edge.weight){
				i++;
			}
			Edge_Graphs.edges.add(i, edge);
			
			// Add edge to node list so edges can be redrawn correctly
			n1.addEdge(edge);
			n2.addEdge(edge);
		}
		
	}
	
	// Not subclassing canvas forces us to save the adjacency matrix
	// statically in Edge_Graphs, which is kind of annoying....
	public static void updateMatrix(Edge_Graphs edge){

		int numNodes = edge.canvas.nodes.size();
		if(adjMatrix == null || numNodes > adjMatrix.length){
			copyMatrix(numNodes);
		}
		
		adjMatrix[edge.n1.id][edge.n2.id] = edge.weight;
		adjMatrix[edge.n2.id][edge.n1.id] = edge.weight;
		
		edge.canvas.update();
	}
	
	private static void copyMatrix(int numNodes){
		int curLength;
		if(adjMatrix != null) curLength = adjMatrix.length;   // sq matrix means row len works
		else curLength = 0;
		
		int[][] newMatrix = new int[numNodes][numNodes];
		
		// Fill with invalids
		for(int i = 0; i < numNodes; i++){
			for(int j = 0; j < numNodes; j++){
				newMatrix[i][j] = NO_EDGE;
			}
		}
		
		// Copy over correct weights
		for(int i = 0; i < curLength; i++){
			for(int j = 0; j < curLength; j++){
				newMatrix[i][j] = adjMatrix[i][j];
			}
		}
		
		// Copy back into adjMatrix
		adjMatrix = newMatrix;
	}
	
	public static int[][] getMatrix(){
		return adjMatrix;
	}
	
	/**
	 * getEdges
	 * @return A sorted list by weight of all edges in graph
	 */
	public static ArrayList<Edge_Graphs> getEdges(){
		return edges;
	}

	@Override
	protected void onDelete() {
		adjMatrix[n1.id][n2.id] = NO_EDGE;
		adjMatrix[n2.id][n1.id] = NO_EDGE;
		
		this.weightLabel.setVisible(false);
		this.weightLabel.removeFromParent();
		edges.remove(this);
		
		canvas.update();
	}
	
	public static void reset(){
		adjMatrix = null;
		edges.clear();
	}

}
