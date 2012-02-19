package webEditor.dst.client;

import java.util.ArrayList;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Line;

import webEditor.client.Proxy;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;


public class SearchDisplayManager extends DisplayManager implements IsSerializable
{
	
	private AbsolutePanel panel;
	private DrawingArea canvas;
	private NodeCollection nodeCollection;
	private EdgeCollection edgeCollection;
	private ArrayList<Widget> itemsInPanel;
	private SearchProblem problem;
	private TraversalContainer cont;

	//permanent widgets
	private Button resetButton;
	private Button evaluateButton;
	private TextArea submitText;
	private AbsolutePanel leftButtonPanel;
	private AbsolutePanel middlePanel;
	private AbsolutePanel rightButtonPanel;
	private Button submitOkButton;
	private AbsolutePanel labelPanel;


	public SearchDisplayManager(DrawingArea canvas, AbsolutePanel panel,
			NodeCollection nc, SearchProblem problem)
	{
		System.out.println(problem.getName());
		this.panel = panel;
		this.canvas = canvas;
		this.nodeCollection = nc;
		this.problem = problem;
		this.itemsInPanel = new ArrayList<Widget>();
		drawLines();
	}

	public void displayProblem()
	{
		cont = new TraversalContainer(this);
		addProblemTextArea();
		addCounterPanel();
		addLeftButtonPanel();
		addMiddlePanel();
		addRightButtonPanel();
		addBackButton();
		addResetButton();
		addEvaluateButton();
		addBucketLabels();
				
		insertNodesAndEdges();
	}
	
	private void addProblemTextArea()
	{
		TextArea t = new TextArea();
		t.setStyleName("problem_statement");
		t.setPixelSize(400, 90);   // was 598,90
		t.setReadOnly(true);
		t.setText(problem.getProblemText());
		RootPanel.get().add(t, 2, 5);
	}
	
	private void addCounterPanel(){
		TextArea cp = new TextArea();
		cp.setStyleName("problem_statement");
		cp.setPixelSize(195, 90);
		cp.setReadOnly(true);
		cp.setText("Current Digit: 1");
		RootPanel.get().add(cp, 407, 5);
		
	}
	private void insertNodesAndEdges()
	{
		cont = new TraversalContainer(this); //for reset of traversal problems
		if(problem.getInsertMethod().equals(DSTConstants.INSERT_METHOD_VALUE))
		{
			insertNodesByValue(problem.getNodes());
		}
		else
		{
			insertNodesByValueAndLocation(problem.getNodes(), problem.getXPositions(),
					problem.getYPositions(), problem.getNodesDraggable(), problem.getNodeType());
		}
		
	}
	
	private boolean showingSubMess;

	private void addLeftButtonPanel()
	{
		leftButtonPanel = new AbsolutePanel();
		leftButtonPanel.setPixelSize(130, 30);
		leftButtonPanel.setStyleName("left_panel");
		RootPanel.get().add(leftButtonPanel, 2, 100);
	}

	private void addMiddlePanel()
	{
		middlePanel = new AbsolutePanel();
		middlePanel.setPixelSize(214, 30);
		middlePanel.setStyleName("right_panel");
		RootPanel.get().add(middlePanel, 132, 100);
	}

	private void addRightButtonPanel()
	{
		rightButtonPanel = new AbsolutePanel();
		rightButtonPanel.setPixelSize(382, 30);
		rightButtonPanel.setStyleName("right_panel");
		RootPanel.get().add(rightButtonPanel, 222, 100);
	}
	
	private void addBucketLabels() {
		AbsolutePanel bucketHolder = new AbsolutePanel();
		bucketHolder.setPixelSize(600, 30);
		bucketHolder.setStyleName("bucket_holding_panel");
		
		for (int i = 0; i < 10; i++) {
			labelPanel = new AbsolutePanel();
			Label l = new Label("" + i);
			labelPanel.add(l);
			labelPanel.setPixelSize(60, 30);
			labelPanel.setStyleName("bucket_panel");
			bucketHolder.add(labelPanel, (60 * i) + 1, 0);
		}
		
		RootPanel.get().add(bucketHolder, 3, 174);
	}

	private void addBackButton()
	{
		Button backButton = new Button("Back");					
		backButton.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				Proxy.buildDST();
			}
		});
		backButton.setStyleName("control_button");
		leftButtonPanel.add(backButton, 2, 2);
	}

	protected void addResetButton()
	{
		resetButton = new Button("Reset");
		resetButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event)
			{
				removeWidgetsFromPanel();

				for(int i = 0; i < getNodes().size(); i++)
				{
					problem.getGridPanel().remove(getNodes().get(i).getLabel());
				}

				nodeCollection.emptyNodes();
				insertNodesAndEdges();
			}
		});
		resetButton.setStyleName("control_button");
		leftButtonPanel.add(resetButton, 62, 2);
	}

	private void addEvaluateButton()
	{
		evaluateButton = new Button("Evaluate");
		evaluateButton.setWidth("124px");
		evaluateButton.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
			//	setEdgeParentAndChildren();
				String evalResult = problem.getEval().evaluate(problem.getName(), problem.getArguments(), getNodes(), null);

				if(showingSubMess == true)
				{
					RootPanel.get().remove(submitText);
					RootPanel.get().remove(submitOkButton);
				}
				
				if(evalResult.equals("")) return;  //used with the traversal problems with help on, if it was empty string
												   //then the user made a correct click and we don't need to save or display
												   //anything
				submitText.setText(evalResult);				
				addToPanel(submitText, DSTConstants.SUBMIT_X, DSTConstants.SUBMIT_MESS_Y);
				int yOffset = DSTConstants.SUBMIT_MESS_Y + submitText.getOffsetHeight()+2;
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
		submitOkButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				RootPanel.get().remove(submitText);
				RootPanel.get().remove(submitOkButton);
				showingSubMess = false;
			}	
		});
	}
	
	public void drawLines(){
		for(int i=1;i<=9;i++){
			Line line = new Line(i*60,50,i*60,700);
			drawEdge(line);
		}
	}

	public void insertNodesByNumber(int numNodes)
	{
		for(int i = 0; i < numNodes; i++)
		{
			Label label = new Label(((char)('A'+i))+"");
			label.setStyleName("node");
			panel.add(label, 5, 150+(50 *i));
			NodeDragController.getInstance().makeDraggable(label);
			nodeCollection.addNode(new Node(((""+(char)('A'+i))), label));
		}
	}

	public void insertNodesByValue(String nodes)
	{
		String[] splitNodes = nodes.split(" ");
		for(int i = 0; i < nodes.length(); i++)
		{
			Label label = new Label(splitNodes[i]);
			label.setStyleName("node");
			label.getElement().getStyle().setTop(10+(45*i), Style.Unit.PX);
			panel.add(label);
			NodeDragController.getInstance().makeDraggable(label);
			nodeCollection.addNode(new Node(splitNodes[i], label));
		}
	}
	
	public void insertNodesByValueAndLocation(String nodes, int[][] xPositions, int[][] yPositions, boolean draggable,
			String nodeType)
	{
		int spaces = 0;
		int current = ((Evaluation_RadixSortWithHelp)problem.getEval()).getCurrent();
		for (int i = 0; i < nodes.length(); i++) {
			if (nodes.charAt(i) == ' ')
				spaces++;
		}
		
		spaces++;
		String[] splitNodes = nodes.split(" ");
		if (spaces != xPositions[0].length || spaces != yPositions[0].length)
			throw new NullPointerException(); //need to find right exception
		else if(nodeType.equals(DSTConstants.NODE_STRING_DRAGGABLE)) {
			
			for (int i = 0; i < splitNodes.length; i++) {
				Label label = new Label(splitNodes[i]);
				label.setStyleName("string_node");
                problem.getGridPanel().add(label, xPositions[current][i], yPositions[current][i]);
                NodeDragController.getInstance().makeDraggable(label);
                nodeCollection.addNode(new Node(splitNodes[i], label));
			}
		}	
		else
		{
			for(int i = 0; i <splitNodes.length; i++)
			{
				Label label = new Label(splitNodes[i]);
				label.setStyleName("node");
				problem.getGridPanel().add(label, xPositions[current][i], yPositions[current][i]);
				if(draggable) NodeDragController.getInstance().makeDraggable(label);
				
				if(nodeType.equals(DSTConstants.NODE_DRAGGABLE))
					nodeCollection.addNode(new Node(splitNodes[i], label));
				else if(nodeType.equals(DSTConstants.NODE_CLICKABLE_FORCE_EVAL))
					nodeCollection.addNode(new NodeClickable(splitNodes[i], label, cont, true));
				else
					nodeCollection.addNode(new NodeClickable(splitNodes[i], label, cont, false));
			}
		}
	}

	public ArrayList<Node> getNodes()
	{
		return nodeCollection.getNodes();
	}

	public ArrayList<EdgeParent> getEdges()
	{
		return edgeCollection.getEdges();
	}

	public void addToPanel(Widget w, int left, int top)
	{
		itemsInPanel.add(w);
		RootPanel.get().add(w, left, top);
	}

	public void removeWidgetsFromPanel()
	{
		for(int i = 0; i < itemsInPanel.size(); i++)
		{
			RootPanel.get().remove(itemsInPanel.get(i));
		}
	}

	public void drawEdge(Line line)
	{
		canvas.add(line);
	}

	public void removeEdge(Line line)
	{
		canvas.remove(line);
	}

	public void makeNodesDraggable()
	{
		nodeCollection.makeNodesDraggable(NodeDragController.getInstance());
	}

	public void makeNodesNotDraggable()
	{
		nodeCollection.makeNodesNotDraggable(NodeDragController.getInstance());
	}

	public void resetNodeStyles()
	{
		nodeCollection.resetNodeStyles(problem.getNodeType());
	}

	public void resetEdgeStyles()
	{
		edgeCollection.resetEdgeColor();
	}

	public void setEdgeParentAndChildren()
	{
		edgeCollection.setParentAndChildNodes();
	}
	
	public void forceEvaluation()
	{
		evaluateButton.click();
	}
}
