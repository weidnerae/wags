package webEditor.dst.client;

import org.vaadin.gwtgraphics.client.DrawingArea;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

public class MSTDisplayManager extends TreeDisplayManager implements
		IsSerializable {

	public MSTDisplayManager(DrawingArea canvas, AbsolutePanel panel,
			NodeCollection nc, EdgeCollection ec, TreeProblem problem) {
		super(canvas, panel, nc, ec, problem);
	}

	private void insertNodesAndEdges() {
		cont = new TraversalContainer(this); // for reset of traversal problems
		if (problem.getInsertMethod().equals(DSTConstants.INSERT_METHOD_VALUE)) {
			insertNodesByValue(problem.getNodes(), problem.getNodeType());
		} else {
			this.insertNodesByValueAndLocation(problem.getNodes(),
					problem.getXPositions(), problem.getYPositions(),
					problem.getNodesDraggable(), problem.getNodeType());
		}

		if (problem.getEdges().length > 0) {
			edgeCollection.insertGraphEdges(problem.getEdges(), getNodes());

		}
	}

	private boolean showingSubMess;

	protected void addResetButton() {
		resetButton = new Button("Reset");
		resetButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				removeWidgetsFromPanel();
				addEdgeCancel();
				resetRemoveEdgeButton();

				for (int i = 0; i < getNodes().size(); i++) {
					panel.remove(getNodes().get(i).getLabel());
				}

				for (int i = 0; i < getEdges().size(); i++) {
					canvas.remove(getEdges().get(i).getLine());
				}

				edgeCollection.emptyEdges();
				nodeCollection.emptyNodes();
				insertNodesAndEdges();
				edgeCollection.clearGraphNodeCollection();
			}
		});
		resetButton.setStyleName("control_button");
		leftButtonPanel.add(resetButton, 62, 2);
	}

	protected void addEvaluateButton() {
		evaluateButton = new Button("Evaluate");
		evaluateButton.setWidth("124px");
		evaluateButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				setEdgeParentAndChildren();

				String evalResult = problem.getEval().evaluate(
						problem.getName(), problem.getArguments(),
						edgeCollection.getGraphNodeCollection().getNodes(),
						getEdges());

				if (showingSubMess == true) {
					RootPanel.get().remove(submitText);
					RootPanel.get().remove(submitOkButton);
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
		rightButtonPanel.add(evaluateButton, 257, 2);

		submitText = new TextArea();
		submitText.setCharacterWidth(30);
		submitText.setReadOnly(true);
		submitText.setVisibleLines(5);
		submitOkButton = new Button("Ok");
		submitOkButton.setStyleName("control_button");
		submitOkButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RootPanel.get().remove(submitText);
				RootPanel.get().remove(submitOkButton);
				showingSubMess = false;
			}
		});
	}

	public void insertNodesByValueAndLocation(String nodes, int[] xPositions,
			int[] yPositions, boolean draggable, String nodeType) {
		String[] splitNodes = nodes.split(" ");
		if (splitNodes.length != xPositions.length
				|| splitNodes.length != yPositions.length) {
			throw new NullPointerException(); // need to find right exception
		} else {
			for (int i = 0; i < splitNodes.length; i++) {
				Label label = new Label(splitNodes[i]);
				label.setStyleName("node");
				panel.add(label, xPositions[i], yPositions[i]);
				nodeCollection.addNode(new Node(splitNodes[i], label));
			}
		}
	}

	public void addDiagLabel(String s) {
		RootPanel.get().add(new Label(s), 250, 250);
	}
}