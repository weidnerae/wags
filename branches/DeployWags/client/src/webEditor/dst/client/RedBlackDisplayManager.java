package webEditor.dst.client;

import java.util.ArrayList;

import org.vaadin.gwtgraphics.client.DrawingArea;

import webEditor.client.Proxy;



import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class RedBlackDisplayManager extends TreeTypeDisplayManager implements
		IsSerializable {
	protected EdgeCollection edgeCollection;
	protected ArrayList<Widget> weightsInPanel;
	protected RedBlackProblem problem;
	protected boolean addingEdge;
	protected boolean removingEdge;
	protected boolean coloring;

	// permanent widgets
	protected Button addEdgeButton;
	protected Button removeEdgeButton;
	protected Button colorButton;
	protected Label edgeAdditionIns;
	protected AbsolutePanel edgeAdditionInsPanel;

	public RedBlackDisplayManager(DrawingArea canvas, AbsolutePanel panel,
			NodeCollection nc, EdgeCollection ec, RedBlackProblem problem) {
		super(canvas, panel, nc, ec, problem);
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

	public void displayProblem() {
		cont = new TraversalContainer(this);
		addProblemTextArea();
		addLeftButtonPanel();
		addMiddlePanel();
		addRightButtonPanel();
		addBackButton();
		addResetButton();
		addEvaluateButton();
		
		
		if (problem.getEdgesRemovable()) {
			addAddEdgeButton();
			addRemoveEdgeButton();
		}
		addColorButton();

		insertNodesAndEdges();
	}
	
	protected void addRightButtonPanel() {
		rightButtonPanel = new AbsolutePanel();
		rightButtonPanel.setPixelSize(460, 30);
		rightButtonPanel.setStyleName("right_panel");
		Proxy.getDST().add(rightButtonPanel, 144, 100);
	}

	// Add Edge Buttons and Color Button
	private HandlerRegistration edgeEventHandler;
	private HandlerRegistration edgeCancelEventHandler;
	private HandlerRegistration removeEdgeEventHandler;
	private HandlerRegistration removeEdgeCancelEventHandler;
	private HandlerRegistration colorButtonEventHandler;
	private HandlerRegistration colorButtonCancelEventHandler;
	private ClickHandler removeEdgeClickHandler;
	private ClickHandler removeEdgeCancelClickHandler;
	private ClickHandler edgeClickHandler;
	private ClickHandler edgeCancelClickHandler;
	private ClickHandler colorButtonClickHandler;
	private ClickHandler colorButtonCancelClickHandler;
	private boolean showingSubMess;

	private class ColorButtonClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			//removeWidgetsFromPanel();
			//resetColorButton();
			makeNodesNotDraggable();
			colorButtonStart();
			
		}
	}
	private class ColorButtonCancelClickHander implements ClickHandler {
		public void onClick(ClickEvent event){
			colorButtonCancel();
			colorButtonCancelEventHandler.removeHandler();
			colorButton.addClickHandler(colorButtonClickHandler);
		}
		
	}


	private void resetEdgeButton() {
		addEdgeButton.setText("Add Edge");
		edgeCancelEventHandler.removeHandler();
		edgeEventHandler = addEdgeButton.addClickHandler(edgeClickHandler);
		changeRBNodeEdgeStatus(false);
	}
	
	public void resetColorButton(){
		if (coloring){	
			colorButton.setText("Recolor");
			colorButtonCancelEventHandler.removeHandler();
			colorButtonEventHandler = colorButton.addClickHandler(colorButtonClickHandler);
			coloring = false;
		}
	}
	
	public void addEdgeStart() {
		addEdgeButton.setText("Cancel");
		edgeEventHandler.removeHandler();
		edgeCancelEventHandler = addEdgeButton
				.addClickHandler(edgeCancelClickHandler);
		addingEdge = true;	
		changeRBNodeEdgeStatus(true);
		Proxy.getDST().add(edgeAdditionInsPanel, 346, 131);
	}
	
	public void colorButtonStart(){		
		colorButton.setText(" Done ");
		colorButtonEventHandler.removeHandler();
		colorButtonCancelEventHandler = colorButton.addClickHandler(colorButtonCancelClickHandler);
		changeRBNodeStatus(true);
		coloring = true;
	}
	
	public void colorButtonCancel(){
		if (coloring){
			makeNodesDraggable();
			changeRBNodeStatus(false);
			colorButton.setText("Recolor");
			coloring = false;
		}
	}
	
	protected void addMiddlePanel() {
		middlePanel = new AbsolutePanel();
		middlePanel.setPixelSize(12, 30);
		middlePanel.setStyleName("middle_panel");
		Proxy.getDST().add(middlePanel, 132, 100);
	}

	
	private void addColorButton(){
		colorButton = new Button("Recolor");
		colorButton.setStyleName("control_button");
		colorButtonClickHandler = new ColorButtonClickHandler();
		colorButtonCancelClickHandler = new ColorButtonCancelClickHander();
		colorButtonEventHandler = colorButton.addClickHandler(colorButtonClickHandler);
		rightButtonPanel.add(colorButton, 0, 2);
		
	}
	
	protected void addRemoveEdgeButton() {
		removeEdgeButton = new Button("Remove Edge");
		removeEdgeButton.setWidth("130px");
		removeEdgeButton.setStyleName("control_button");
		removeEdgeClickHandler = new RemoveEdgeClickHandler();
		removeEdgeCancelClickHandler = new RemoveEdgeCancelClickHandler();
		removeEdgeEventHandler = removeEdgeButton
				.addClickHandler(removeEdgeClickHandler);
		rightButtonPanel.add(removeEdgeButton, 204, 2);
	}
	
	protected void addAddEdgeButton() {
		addEdgeButton = new Button("Add Edge");
		addEdgeButton.setWidth("124px");
		edgeClickHandler = new AddEdgeClickHandler();
		edgeCancelClickHandler = new AddEdgeCancelClickHandler();
		edgeEventHandler = addEdgeButton.addClickHandler(edgeClickHandler);
		edgeAdditionIns = new Label("");
		edgeAdditionIns.setStyleName("edge_instructions");
		edgeAdditionInsPanel = new AbsolutePanel();
		edgeAdditionInsPanel.setPixelSize(256, 20);
		edgeAdditionInsPanel.setStyleName("edge_addition_panel");
		edgeAdditionInsPanel.add(edgeAdditionIns);
		addEdgeButton.setStyleName("control_button");
		rightButtonPanel.add(addEdgeButton, 80, 2);
	}
	
	protected void addEvaluateButton() {
		evaluateButton = new Button("Evaluate");
		evaluateButton.setWidth("124px");
		evaluateButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				setEdgeParentAndChildren();
				String evalResult;
				if(isMST()){
					evalResult = problem.getEval().evaluate(
							problem.getName(), problem.getArguments(), edgeCollection.getGraphNodeCollection().getNodes(),
							getEdges());
				}
				else{
					evalResult = problem.getEval().evaluate(
						problem.getName(), problem.getArguments(), getNodes(),
						getEdges());
				}

				if (showingSubMess == true) {
					Proxy.getDST().remove(submitText);
					Proxy.getDST().remove(submitOkButton);
				}

				if (evalResult.equals(""))
					return; // used with the traversal problems with help on, if
							// it was empty string
							// then the user made a correct click and we don't
							// need to save or display
							// anything
				submitText.setText(evalResult);
				addToPanel(submitText, DSTConstants.SUBMIT_X,
						DSTConstants.SUBMIT_MESS_Y);
				int yOffset = DSTConstants.SUBMIT_MESS_Y
						+ submitText.getOffsetHeight() + 2;
				addToPanel(submitOkButton, DSTConstants.SUBMIT_X, yOffset);
				showingSubMess = true;

			}
		});
		showingSubMess = false;
		evaluateButton.setStyleName("control_button");
		rightButtonPanel.add(evaluateButton, 335, 2);

		submitText = new TextArea();
		submitText.setCharacterWidth(30);
		submitText.setReadOnly(true);
		submitText.setVisibleLines(5);
		submitOkButton = new Button("Ok");
		submitOkButton.setStyleName("control_button");
		submitOkButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Proxy.getDST().remove(submitText);
				Proxy.getDST().remove(submitOkButton);
				showingSubMess = false;
			}
		});
	}
	
	public void changeRBNodeStatus(boolean color){
		for (Node node : nodeCollection.getNodes()){
			NodeRedBlack rbNode = (NodeRedBlack)node;
			if (color)
				rbNode.setBeingColoredTrue();
			else
				rbNode.setBeingColoredFalse();
		}
	}
	
	public void changeRBNodeEdgeStatus(boolean status){
		for (Node node : nodeCollection.getNodes()){
			NodeRedBlack rbNode = (NodeRedBlack)node;
			if (status)
				rbNode.setAddingEdgesTrue();
			else
				rbNode.setAddingEdgesFalse();
		}
	}

}