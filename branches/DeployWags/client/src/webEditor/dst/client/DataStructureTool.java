package webEditor.dst.client;

import java.util.ArrayList;

import org.vaadin.gwtgraphics.client.DrawingArea;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import webEditor.client.view.View;
import webEditor.client.view.WEAnchor;
import webEditor.client.Proxy;


public class DataStructureTool  extends View
{	
	private ArrayList<Widget> widgets; //arraylist to hold widgets added to root panel
	private ArrayList<Widget> attempts; //arraylist to hold widgets added for viewing past problem attempts

	private String[] problemList;	//array of problem names

	private int xCoordinate; 	//field to keep track of current x offset
	private int yCoordinate; 	//field to keep track of current y offset
	private String emailAddr;	//user's email address
	private ArrayList<Integer> originalYCoordinates;

	//widgets
	private Label bannerLabel;
	private Label selectLabel;
	private Label welcomeLabel;
	private Button logoutButton;
	private ArrayList<Label>  problemLabels;
	private ArrayList<Button> attemptButtons;
	private ArrayList<Button> viewResultButtons;

	/**
	 * This is the entry point method.
	 */
	public DataStructureTool() 
	{	
		//intialize fields that will be used
		widgets = new ArrayList<Widget>();
		attempts = new ArrayList<Widget>();
		originalYCoordinates = new ArrayList<Integer>();

		//initialize widgets
		bannerLabel = new Label("Data Structure Tool");
		selectLabel = new Label("Please select a problem below.");
		logoutButton = new Button("Logout");
		problemLabels = new ArrayList<Label>();
		attemptButtons = new ArrayList<Button>();
		viewResultButtons = new ArrayList<Button>();

		//set styles
		RootPanel.get().setStyleName("main_background");
		bannerLabel.setStyleName("banner");
		selectLabel.setStyleName("welcome");

		//Note: method calls to initialize and build the app have to be buried in the onSuccess methods
		//of the asynchronous calls to ensure that everything is initialized in order
		//start asynchronous calls
		getEmailAddressAndLogoutURL();
	}

	/**
	 * Method used to perform asynchronous call that gets the user's email address
	 * and a logout URL.
	 */
	private void getEmailAddressAndLogoutURL()
	{
		Proxy.getUsersName(welcomeLabel);
		
		getProblems();
	}
	
	/**
	 * Method used to perform asynchronous call to get the list of available problems.
	 * Note: String array contains names of problem, where the index of the problem name
	 *		 is the same index used to fetch the problem from the probFetchService.
	 */
	private void getProblems()
	{
		//This is temporary, as problems reside fully on client at the moment
		//Will have to add dynamic changing of "problemList"
		problemList =  new String[] {"BST Preorder  Traversal (Help on)",
	 			 "BST Inorder   Traversal (Help on)",
	 			 "BST Postorder Traversal (Help on)",
	 			 "BST Preorder  Traversal (Help off)",
	 			 "BST Inorder   Traversal (Help off)",
	 			 "BST Postorder Traversal (Help off)",
				 "Insert Nodes into a BST 1",
	 			 "Insert Nodes into a BST 2",
	 			 "Insert Nodes into a BST 3",
	 			 "Insert Nodes into a BST 4",
				 "Binary Search Tree from Postorder Traversal 1",
				 "Binary Search Tree from Postorder Traversal 2",
				 "Binary Search Tree from Postorder Traversal 3",
				 "Binary Search Tree from Postorder Traversal 4",
				 "Binary Tree from Pre/Inorder Traversals 1",
				 "Binary Tree from Pre/Inorder Traversals 2",
				 "Binary Tree from Pre/Inorder Traversals 3",
				 "Binary Tree from Pre/Inorder Traversals 4",};
		
		buildUI();
	}

	/**
	 * Method used to build the user interface.
	 */
	private void buildUI()
	{	
		RootPanel.get().clear();
		//add click handler to logout button
		logoutButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event)
			{
				Proxy.logout();
			}
		});
		
		//create a label and attempt button for each problem
		for(int i = 0; i < problemList.length; i++)
		{
			//add the label with the problem's name
			problemLabels.add(new Label(problemList[i]));
			//set the label's style
			problemLabels.get(i).setStyleName("problem");
			//create button that allows a problem to be attempted
			attemptButtons.add(new Button("Attempt"));
			//create button that allows past attempts to be viewed if present
			viewResultButtons.add(new Button("View Attempts"));
		}

		//lots of magic numbers here, needs to be cleaned up at some point
		//most magic numbers represent pixel offsets between various widgets
		//add static widgets: welcome message, logout button, etc..
		yCoordinate = 5;
		addToPanel(bannerLabel, 4, yCoordinate);
		//addToPanel(welcomeLabel, 40+bannerLabel.getOffsetWidth(), yCoordinate+8); //<- Problem may be here....
		//addToPanel(logoutButton, 40+bannerLabel.getOffsetWidth()+welcomeLabel.getOffsetWidth()+2, yCoordinate+3);
		yCoordinate += 60+bannerLabel.getOffsetHeight();
		addToPanel(selectLabel, 4, yCoordinate);
		yCoordinate += 25 + selectLabel.getOffsetHeight();
		
		for(int i = 0; i < problemList.length; i++)
		{
			xCoordinate = 4;
			//add problem name label
			addToPanel(problemLabels.get(i), xCoordinate, yCoordinate);
			xCoordinate += (8+problemLabels.get(i).getOffsetWidth());
			//add button to attempt problem, Note: handlers are added later
			addToPanel(attemptButtons.get(i), 300, yCoordinate);
			xCoordinate += (12+attemptButtons.get(i).getOffsetWidth());
			
			//set original Y coordinate for later use with problem result viewing
			originalYCoordinates.add(yCoordinate);
			yCoordinate += 31;
		}
		
		//call method to add click handlers to buttons
		addClickHandlers();
	}
	
	/**
	 * Method to add click handlers to the buttons.
	 */
	private void addClickHandlers()
	{
		for(int i = 0; i < problemList.length; i++)
		{
			final int id = i;
			//add click handler for problem selection
			attemptButtons.get(i).addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event)
				{
					removeAllWidgets();
					emailAddr = "TestUser";
					getProblem(emailAddr, id);
				}
			});
		}
	}


	/**
	 * Removes all widgets currently on the screen.
	 */
	public void removeAllWidgets()
	{
		for(int i = 0; i < widgets.size(); i++)
		{
			RootPanel.get().remove(widgets.get(i));
		}
		removeAllAttempts();
	}

	/**
	 * Removes all widgets associated with previous problem attempts from the screen.
	 */
	public void removeAllAttempts()
	{
		for(int i = 0; i < attempts.size(); i++)
		{
			RootPanel.get().remove(attempts.get(i));
		}
	}

	/**
	 * Inserts a widget at coordinates top and left on the screen
	 * 
	 * @param widget
	 * @param left x coordinate
	 * @param top  y coordinate
	 */
	public void addToPanel(Widget widget, int left, int top)
	{
		widgets.add(widget);
		RootPanel.get().add(widget, left, top);
	}

	/**
	 * Gets the problem associated probId and initializes it to be attempted
	 * @param userEmail
	 * @param probId
	 */
	private void getProblem(final String userEmail, final int probId)
	{
		Problem prob = ProblemServiceImpl.getProblem(probId);

		initialize(userEmail, prob, probId);
	}

	private void initialize(String userEmail, Problem p, int probNum)
	{
		//initialize the necessary components to display the problem
		AbsolutePanel panel = new AbsolutePanel();
		panel.setStyleName("boundary_panel");
		panel.setPixelSize(600, 550);
		DrawingArea canvas = new DrawingArea(600, 550);
		canvas.setStyleName("canvas");
		panel.add(canvas);
		RootPanel.get().add(panel, 2, 130);
		RootPanel.get().setStyleName("prob_background");
		EdgeCollection ec = new EdgeCollection(p.getRules(), 
				new String[]{"Select first node of edge","Select second node of edge"}, p.getEdgesRemovable());

		NodeDragController.setFields(panel, true, ec);
		NodeDropController.setFields(panel, ec);
		NodeDragController.getInstance().registerDropController(NodeDropController.getInstance());

		NodeCollection nc = new NodeCollection();

		DisplayManager dm = new DisplayManager(canvas, panel, nc, ec, p);
		ec.setDisplayManager(dm);
		dm.displayProblem();
	}

	/* (non-Javadoc)
	 * @see webEditor.client.view.View#getLink()
	 */
	@Override
	public WEAnchor getLink() {
		return new WEAnchor("DST", this, "DST");
	}	
	
}
