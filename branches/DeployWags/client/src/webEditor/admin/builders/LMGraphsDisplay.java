package webEditor.admin.builders;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;

public class LMGraphsDisplay extends BasicDisplay {
	Button btnAssign;
	ArgPanel pnlSolution;
	boolean kruskal = true;
	
	@Override
	public void construct() {
		canvas.setEdgeHandler(new EH_Graphs(this.canvas));
		
		pnlSolution = new ArgPanel();
		pnlSolution.setup("Order: ", "Assign");
		pnlSolution.btnTraversal.addClickHandler(new AssignClickHandler(this, pnlSolution));
		basePanel.add(pnlSolution);
	}

	@Override
	public void calculate() {
		if(kruskal){
			runKruskals();
		} else {
			runPrims();
		}
		pnlSolution.fillText("yadda");
	}
	
	// This is going to be ugly
	private void runKruskals(){
		// Collect all edges...
		ArrayList<Edge_Graphs> edges = Edge_Graphs.getEdges();
		for(Edge_Graphs edge: edges){
			Window.alert(edge.weight + "");
		}
	}
	
	private void runPrims(){
		
	}

	@Override
	public void fillBuilder(ArgHolder child) {
		// TODO Auto-generated method stub
		Edge_Graphs.reset();
	}

	@Override
	public void onModify() {
		pnlSolution.clear();
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

}
