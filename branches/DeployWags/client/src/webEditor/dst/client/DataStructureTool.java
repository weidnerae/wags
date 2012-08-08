package webEditor.dst.client;

import java.util.ArrayList;

import org.vaadin.gwtgraphics.client.DrawingArea;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import webEditor.client.Proxy;


public class DataStructureTool  extends AbsolutePanel
{	
	private ArrayList<Widget> widgets; //arraylist to hold widgets added to root panel

	private static String[] problemList;	//array of problem names
	private static boolean[] successList;	//array of success values

	private int xCoordinate; 	//field to keep track of current x offset
	private int yCoordinate; 	//field to keep track of current y offset
	private String emailAddr;	//user's email address
	private ArrayList<Integer> originalYCoordinates;

	//widgets
	private Label bannerLabel;
	private Label selectLabel;
	private Label welcomeLabel;
	private ArrayList<Label>  problemLabels;
	private ArrayList<Button> attemptButtons;

	/**
	 * This is the entry point method.
	 */
	public DataStructureTool(String[] problems, boolean[] success) 
	{	
		//initialize fields that will be used
		widgets = new ArrayList<Widget>();
		originalYCoordinates = new ArrayList<Integer>();
		problemList = problems;
		successList = success;

		//initialize widgets
		bannerLabel = new Label("Data Structure Tool");
		selectLabel = new Label("Please select a problem below.");
		problemLabels = new ArrayList<Label>();
		attemptButtons = new ArrayList<Button>();


		//set styles
		this.setStyleName("main_background");
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
		
		//Redirects to getProblems
		buildUI();
	}

	/**
	 * Method used to build the user interface.
	 */
	private void buildUI()
	{	
		this.removeAllWidgets();
		
		welcomeLabel = new Label();
		
		Proxy.getUsersName(welcomeLabel);
		
		//create a label and attempt button for each problem
		for(int i = 0; i < problemList.length; i++)  //The explode php function results in one extra, empty index
		{
			//add the label with the problem's name
			problemLabels.add(new Label(problemList[i]));
			//set the label's style
			problemLabels.get(i).setStyleName("problem");
			//create button that allows a problem to be attempted
			attemptButtons.add(new Button(successList[i] ? "<font color=green>Completed</font>" : "Attempt"));
			//create button that allows past attempts to be viewed if present
			//viewResultButtons.add(new Button("View Attempts"));
		}

		//lots of magic numbers here, needs to be cleaned up at some point
		//most magic numbers represent pixel offsets between various widgets
		//add static widgets: welcome message, logout button, etc..
		yCoordinate = 5;
		addToPanel(bannerLabel, 4, yCoordinate);
		yCoordinate = 65+bannerLabel.getOffsetHeight();
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
			addClickHandling(attemptButtons.get(i), problemLabels.get(i).getText());
			xCoordinate += (12+attemptButtons.get(i).getOffsetWidth());
			
			//set original Y coordinate for later use with problem result viewing
			originalYCoordinates.add(yCoordinate);
			yCoordinate += 31;
		}
		
		
		
		//call method to add click handlers to buttons
		//addClickHandlers();
	}
	
	private void addClickHandling(Button button, final String problem){
		button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				removeAllWidgets();
				emailAddr = "TestUser";
				getProblem(emailAddr, problem);
			}
		});
	}

	/**
	 * Removes all widgets currently on the screen.
	 */
	public void removeAllWidgets()
	{
		for(int i = 0; i < widgets.size(); i++)
		{
			this.remove(widgets.get(i));
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
		this.add(widget, left, top);
	}

	/**
	 * Gets the problem associated probId and initializes it to be attempted
	 * @param userEmail
	 * @param probId
	 */
	private void getProblem(final String userEmail, String problem)
	{
		Problem prob = ProblemServiceImpl.getProblem(problem);

		initialize(userEmail, prob);
	}

	private void initialize(String userEmail, Problem p)
	{
		
		//initialize the necessary components to display the problem
		AbsolutePanel panel = new AbsolutePanel();
		panel.setStyleName("boundary_panel");
		panel.setPixelSize(600, 550);
		DrawingArea canvas = new DrawingArea(600, 550);
		canvas.setStyleName("canvas");
		panel.add(canvas);
		this.add(panel, 2, 130);
		this.setStyleName("prob_background");
				
		Proxy.setDST(this);
		DisplayManager dm = p.createDisplayManager(panel, canvas);
		dm.displayProblem();
	}

	
}
