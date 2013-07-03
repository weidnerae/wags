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
		
		txtInstructions.setText("Use this canvas to create a Graph problem.  Add nodes by filling in the appropriate text box " +
				"with the number you'd like on the node and either press 'enter' or press 'Add'.  You can delete nodes in a similar manner " +
				"by holding 'shift' and pressing 'enter' or by pressing the 'Delete' button.  Create edges between nodes by " +
				"double clicking on one node and double clicking on the node you'd like to be the child.  When edges are created " +
				"you will be prompted to determine the weight to be added to the edge.  You can remove an edge by clicking on it. " +
				"Clicking on 'Calculate Results' will determine the answer for the problem you have created and if you are happy with the " +
				"results you can assign the problem to students.  If at any time you'd like to start the process over, press the " +
				"'reset' button in order to return the canvas to it's initial state.");
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
	}
	
	private class KruskalTreeFactory{
		int id = 0;
		
		public KruskalTree getTree(Node_Basic node){
			return new KruskalTree(node, id++);
		}
		
		public void combine(KruskalTree t1, KruskalTree t2, KruskalForest forest){
			ArrayList<Node_Basic> nodes = new ArrayList<Node_Basic>();
			nodes.addAll(t1.nodes);
			nodes.addAll(t2.nodes);
			
			forest.remove(t1);
			forest.remove(t2);
			
			forest.add(new KruskalTree(nodes, id++));
		}
	}
	
	//------------------------//
	//         KRUSKAL        //
	//------------------------//
	
	private class KruskalTree{
		ArrayList<Node_Basic> nodes;
		int id;
		
		public KruskalTree(ArrayList<Node_Basic> nodes, int id){
			this.nodes = nodes;
			this.id = id;
		}
		
		public KruskalTree(Node_Basic node, int id){
			nodes = new ArrayList<Node_Basic>();
			nodes.add(node);
			this.id = id;
		}
		
		public boolean contains(Node_Basic node){
			return nodes.contains(node);
		}
		
	}
	
	private class KruskalForest{
		ArrayList<KruskalTree> trees = new ArrayList<KruskalTree>();
		KruskalTreeFactory factory = new KruskalTreeFactory();
		
		public KruskalForest(ArrayList<Node_Basic> nodes){
			for(Node_Basic node: nodes){
				add(factory.getTree(node));
			}
		}
		
		/**
		 * combine
		 * @param t1 - Tree to be combined with t2
		 * @param t2 - Tree to be combind with t1
		 * t1 and t2 need to be part of the forest for the algorithm, not the
		 * method, to work.  t1 and t2 gets removed, and a new tree containing
		 * all the nodes of both t1 and t2 gets added to the forest.  Actual
		 * executaion delegated to KruskalTreeFactory
		 */
		public void combine(KruskalTree t1, KruskalTree t2){
			factory.combine(t1, t2, this);
		}
		
		public void add(KruskalTree tree){
			trees.add(tree);
		}
		
		public void remove(KruskalTree tree){
			trees.remove(tree);
		}
		
		/**
		 * isSpereate
		 * @param edge - Edge being checked
		 * @return true if nodes belong to separate trees, false otherwise
		 */
		public boolean isSeparate(Edge_Graphs edge){
			int n1_id = getTreeId(edge.n1);
			int n2_id = getTreeId(edge.n2);
			
			return (n1_id != n2_id);
		}
		
		public int getTreeId(Node_Basic node){
			for(KruskalTree tree: trees){
				if(tree.contains(node)){
					return tree.id;
				}
			}
			// Shouldn't be reachable
			Window.alert("ERROR:  Node not found");
			return -1;
		}
		
		public KruskalTree getContainingTree(Node_Basic node){
			for(KruskalTree tree: trees){
				if(tree.contains(node)){
					return tree;
				}
			}
			
			return null;
		}
		
		public int size(){
			return trees.size();
		}
	}
	
	private void runKruskals(){
		Edge_Graphs tmpEdge;
		ArrayList<Edge_Graphs> usedEdges = new ArrayList<Edge_Graphs>();
		
		// Grab sorted list of edges
		ArrayList<Edge_Graphs> edges = Edge_Graphs.getEdges();
		// Create forest of nodes as trees
		KruskalForest forest = new KruskalForest(canvas.nodes);
		
		// Run until all edges checked of complete tree created
		while(edges.size() > 0 && forest.size() > 1){
			if(forest.isSeparate(edges.get(0))){
				tmpEdge = edges.get(0);
				forest.combine(forest.getContainingTree(tmpEdge.n1), 
						forest.getContainingTree(tmpEdge.n2));
				usedEdges.add(tmpEdge);
			}
			
			edges.remove(0);
		}
		
		String solution = "";
		for(Edge_Graphs edge: usedEdges){
			solution += edge.weight + " ";
		}
		solution = solution.substring(0, solution.length() - 1);
		pnlSolution.fillText(solution);
	}
	
	private void runPrims(){
		
	}

	@Override
	public void fillBuilder(ArgHolder child) {
		Edge_Graphs.reset();
	}

	@Override
	public void onModify() {
		pnlSolution.clear();
	}

	@Override
	public void clear() {

	}

}
