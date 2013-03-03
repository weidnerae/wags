package wags.logical.TreeProblems.MSTProblems;

import org.vaadin.gwtgraphics.client.DrawingArea;

import wags.logical.AddEdgeRules;
import wags.logical.DisplayManager;
import wags.logical.EdgeCollection;
import wags.logical.Evaluation;
import wags.logical.NodeCollection;
import wags.logical.NodeDragController;
import wags.logical.NodeDropController;
import wags.logical.TreeProblems.TreeProblem;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class MSTProblem extends TreeProblem implements IsSerializable {

	public MSTProblem(String name, String problemText, String nodes,
			String insertMethod, int[] xPositions, int[] yPositions,
			String[] edges, String[] arguments, Evaluation eval,
			AddEdgeRules rules, boolean edgesRemovable, boolean nodesDraggable,
			String nodeType) {
		super(name, problemText, nodes, insertMethod, xPositions, yPositions,
				edges, arguments, eval, rules, edgesRemovable, nodesDraggable,
				nodeType);
	}

	public DisplayManager createDisplayManager(AbsolutePanel panel,
			DrawingArea canvas) {
		EdgeCollection ec = new EdgeCollection(getRules(), new String[] {
				"Select first node of edge", "Select second node of edge" },
				getEdgesRemovable());
		NodeDragController.setFields(panel, true, ec);
		NodeDropController.setFields(panel, ec);
		NodeDragController.getInstance().registerDropController(
				NodeDropController.getInstance());
		NodeCollection nc = new NodeCollection();

		dm = new MSTDisplayManager(canvas, panel, nc, ec, this);
		ec.setDisplayManager(dm);

		return dm;
	}
}