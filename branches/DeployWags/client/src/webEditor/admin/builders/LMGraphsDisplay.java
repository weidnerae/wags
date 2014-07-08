package webEditor.admin.builders;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;

public class LMGraphsDisplay extends BasicDisplay {
	ArgPanel orderPanel;
	public String solution = "";
	boolean kruskal = false;
	boolean prims = false;
	
	//Gets a boolean to determine which algorithm to run
	public LMGraphsDisplay(boolean x)
	{
		if (x)
		{
			prims = true;
		}
		else 
		{
		    kruskal = true;
		}
	}

	@Override
	public void construct() {
		canvas.setEdgeHandler(new EH_Graphs(this.canvas));
		orderPanel = new ArgPanel();
		orderPanel.setup("Order: ", "Assign");
		orderPanel.btnArg.addClickHandler(new AssignClickHandler(this, orderPanel));
		basePanel.add(orderPanel);
		
		if (kruskal)
		{
		txtInstructions.setText("Use this canvas to create a Graph problem.  Add nodes by filling in the appropriate text box " +
				"with the number you'd like on the node and either press 'Enter' or press 'Add'.  You can delete nodes in a similar manner " +
				"by holding 'Shift' and pressing 'Enter' or by pressing the 'Delete' button.  Create edges between nodes by " +
				"double clicking on one node and double clicking on the node you'd like to be the child.  When edges are created " +
				"you will be prompted to determine the weight to be added to the edge.  You can remove an edge by double clicking on it, " +
				"and change the weight by single clicking on it. " +
				"Clicking on 'Calculate Results' will determine the answer for the problem you have created and if you are happy with the " +
				"results you can assign the problem to students.  If at any time you'd like to start the process over, press the " +
				"'reset' button in order to return the canvas to it's initial state.");
		}
		else if (prims)
		{
			txtInstructions.setText("Use this canvas to create a Graph problem.  Add nodes by filling in the appropriate text box " +
					"with the number you'd like on the node and either press 'Enter' or press 'Add'.  You can delete nodes in a similar manner " +
					"by holding 'Shift' and pressing 'Enter' or by pressing the 'Delete' button.  Create edges between nodes by " +
					"double clicking on one node and double clicking on the node you'd like to be the child.  When edges are created " +
					"you will be prompted to determine the weight to be added to the edge.  You can remove an edge by double clicking on it, " +
					"and change the weight by single clicking on it. After entering your nodes and edges enter a starting node into the " +
					"starting node text box, this is needed to calculate the results. " +
					"Clicking on 'Calculate Results' will determine the answer for the problem you have created and if you are happy with the " +
					"results you can assign the problem to students.  If at any time you'd like to start the process over, press the " +
					"'reset' button in order to return the canvas to it's initial state.");
			lblStart.setVisible(true);
			txtStart.setVisible(true);
		}
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
		orderPanel.fillText(solution);
	}
	
	private void runPrims(){
		ArrayList<String> usedNodes = new ArrayList<String>();
		ArrayList<Edge_Graphs> usedEdges = new ArrayList<Edge_Graphs>();
		ArrayList<Edge_Graphs> edges = Edge_Graphs.getEdges();
		solution = "";
		String tempEdges = "";
		String startNode = txtStart.getText();
		int count = 0;
		
		//Does nothing if start text box is empty
		if (startingNode(txtStart.getText(), edges))
		{
			//Puts the node in txtStart into usedNodes list
			usedNodes.add(startNode);
			while (edges.size() != 0)
			{
				//If the size of the edges list is 1 checks for cycle and adds it to the final
				if (edges.size() == 1)
				{
					count = 0;
					for (String s : usedNodes)
					{
						if (edges.get(0).n1.getText().equals(s) || edges.get(0).n2.getText().equals(s))
						{
							count++;
						}
					}
					if (count < 2)
					{
						tempEdges += edges.get(0).weight;
					}
					break;
				}
				
				//Gathers all edges that are equal to the nodes in usedNodes and puts them in usedEdges
				for (Edge_Graphs e : edges)
				{
					for (String s : usedNodes)
					{
						if (e.n1.getText().equals(s) || e.n2.getText().equals(s))
						{
							usedEdges.add(e);
						}
					}
				}
				
				//Deletes duplicates from usedEdges
				Object[] ue = usedEdges.toArray();
			      for (Object s : ue) 
			      {
				        if (usedEdges.indexOf(s) != usedEdges.lastIndexOf(s))
				        {
				        	usedEdges.remove(usedEdges.lastIndexOf(s));
				        }
			      }
				
				//Gets which edge in usedEdges is the lowest and sets the location to index
				int x = Integer.MAX_VALUE;
				count = 0;
				int index = 0;
				for (Edge_Graphs u : usedEdges)
				{
					if (x > u.weight)
					{
						index = count;
						x = u.weight;
					}
					count++;
				}
				
				//Finds which edge is equal to the one picked in usedEdges and sets its location to indexEdge
				int indexEdge = 0;
				int countTwo = 0;
				for (Edge_Graphs e : edges)
				{
					if (e.weight == usedEdges.get(index).weight)
					{
						indexEdge = countTwo;
					}
					countTwo++;
				}
				
				//Looks at the picked edge and sees if two nodes match any two nodes in usedNodes,
				//to make sure it's not a cycle. If the count is two it has two matching nodes 
				//and it deletes that edge. Otherwise adds the two new nodes.
				count = 0;
				for (String s : usedNodes)
					{
						if (usedEdges.get(index).n1.getText().equals(s) || usedEdges.get(index).n2.getText().equals(s))
						{
							count++;
						}
					}
					if (count >= 2)
					{
						usedEdges.remove(index);
					}
					else
					{
						tempEdges += usedEdges.get(index).weight + " ";
						usedNodes.add(usedEdges.get(index).n1.getText());
						usedNodes.add(usedEdges.get(index).n2.getText());
					}
				
					//Deletes duplicates in usedNodes
					Object[] st = usedNodes.toArray();
				      for (Object s : st) {
				        if (usedNodes.indexOf(s) != usedNodes.lastIndexOf(s)) {
				        	usedNodes.remove(usedNodes.lastIndexOf(s));
				         }
				      }
				//Deletes the picked edge out of edges so it won't repeat
				//and clears usedEdges
				edges.remove(indexEdge);
				usedEdges.clear();
			}
		}
		else if(!startingNode(txtStart.getText(), edges))
		{
			Window.alert("Please enter a valid starting node.");
		}
		solution += tempEdges;
		orderPanel.fillText(solution);
	}

	private boolean startingNode(String start, ArrayList<Edge_Graphs> edges)
	{
		for (Edge_Graphs e : edges)
		{
			if (e.n1.getText().equals(start) || e.n2.getText().equals(start))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void fillBuilder(ArgHolder child) {
		// give the builder the information it needs
		// tell builder to upload problem builder.uploadLM() // uploadLM(True) for debugging
		String[] args = new String[1];
		int[] xPos = new int[10];
		int[] yPos = new int[10];
		args[0] = solution;
		builder.setArgs(args);
		builder.setTitle(txtTitle.getText());
		builder.setProblemText(txtDesc.getText());
		builder.genre = Genre.MST;
		int i = 0;
		for(Node_Basic node: canvas.nodes){
			builder.addNode(node.value);
			xPos[i] = node.xPos;
			yPos[i] = node.yPos;
			i++;
		}
		builder.setPos(xPos, yPos);
		ArrayList<Edge_Graphs> edges = Edge_Graphs.getEdges();
		for(Edge_Graphs e : edges)
		{
			builder.addEdge(e.weight + "");
		}
		builder.uploadLM();
		Window.alert("uploadLM was Called");
	}

	@Override
	public void onModify() {
		orderPanel.clear();
	}

	@Override
	public void clear() {
		orderPanel.fillText("");
	}

}
