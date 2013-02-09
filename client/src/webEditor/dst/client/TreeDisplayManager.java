package webEditor.dst.client;

import java.util.ArrayList;

import org.vaadin.gwtgraphics.client.DrawingArea;

import webEditor.client.Proxy;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class TreeDisplayManager extends TreeTypeDisplayManager implements
		IsSerializable {
	protected EdgeCollection edgeCollection;
	protected ArrayList<Widget> weightsInPanel;
	protected TreeProblem problem;
	protected boolean addingEdge;
	protected boolean removingEdge;
	protected boolean coloring;

	// permanent widgets
	protected Button addEdgeButton;
	protected Button removeEdgeButton;
	protected Button colorButton;
	protected Label edgeAdditionIns;
	protected AbsolutePanel edgeAdditionInsPanel;

	public TreeDisplayManager(DrawingArea canvas, AbsolutePanel panel,
			NodeCollection nc, EdgeCollection ec, TreeProblem problem) {
		super(canvas, panel, nc, ec, problem);
		System.out.println(problem.getName());
		this.panel = panel;
		this.canvas = canvas;
		this.nodeCollection = nc;
		this.edgeCollection = ec;
		this.problem = problem;
		super.problem = problem;
		this.addingEdge = false;
		this.removingEdge = false;
		this.itemsInPanel = new ArrayList<Widget>();
		this.weightsInPanel = new ArrayList<Widget>();
	}

}