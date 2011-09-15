package microlabs.dst.client;

import java.util.ArrayList;

import java.util.List;
import microlabs.dst.shared.AvailableServices;
import microlabs.dst.shared.SerialEdge;
import microlabs.dst.shared.SerialNode;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Line;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;


public class DataStructureTool implements EntryPoint 
{	
	private BasicServicesAsync services;	
	private ProblemsFetchServiceAsync problemsFetchService;
	private final ProblemServiceAsync problemService = GWT //service used to get problem
	.create(ProblemService.class);
	private ProblemResultFetchServiceAsync probFetchService; //service used to get past attempted problems

	private ArrayList<Widget> widgets; //arraylist to hold widgets added to root panel
	private ArrayList<Widget> attempts; //arraylist to hold widgets added for viewing past problem attempts

	private ArrayList<ArrayList<ProblemResult>> problemResults; //ArrayList to contain problem results
	private String[] problemList;	//array of problem names
	private ArrayList<ProblemResultViewHandler> resultViews;

	private int xCoordinate; 	//field to keep track of current x offset
	private int yCoordinate; 	//field to keep track of current y offset
	private int totalHeight; 	//used to place the next button for viewing past problem attmepts
	private String logoutUrl; 	//url for logging out
	private String emailAddr;	//user's email address
	private int lastToAdd = 0;   //for problem result views
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
	public void onModuleLoad() 
	{	
		//initialize services that will be used
		services = GWT.create(BasicServices.class);
		probFetchService = GWT.create(ProblemResultFetchService.class);
		problemsFetchService = GWT.create(ProblemsFetchService.class);
		
		//intialize fields that will be used
		widgets = new ArrayList<Widget>();
		attempts = new ArrayList<Widget>();
		problemResults = new ArrayList<ArrayList<ProblemResult>>();
		resultViews = new ArrayList<ProblemResultViewHandler>();
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
		AsyncCallback<String> cb = new AsyncCallback<String>()
		{
			public void onFailure(Throwable caught) 
			{
				System.out.println(caught.getMessage());
			}

			public void onSuccess(String result)
			{
				emailAddr = result.split(";")[0]; //get user's email address
				logoutUrl = result.split(";")[1]; //get a logout url
				getProblems();					  //perform call to get available problems
			}
		};

		//perform call to get email address and logout URL
		services.doService(AvailableServices.GET_EMAIL_ADDR_AND_LOGOUT_URL, cb);
	}
	
	/**
	 * Method used to perform asynchronous call to get the list of available problems.
	 * Note: String array contains names of problem, where the index of the problem name
	 *		 is the same index used to fetch the problem from the probFetchService.
	 */
	private void getProblems()
	{
		AsyncCallback<String[]> cb = new AsyncCallback<String[]>()
		{
			public void onFailure(Throwable caught) 
			{
				System.out.println(caught.getMessage());
			}

			public void onSuccess(String[] result)
			{
				problemList = result;				
				getProblemResults();
			}
		};
		
		//perform call to get list of problems
		problemsFetchService.getProblems(cb);
	}

	/**
	 * Method used to perform asynchronous call that gets the user's problem results.
	 */
	private void getProblemResults()
	{
		AsyncCallback<List<ProblemResult>> cb = new AsyncCallback<List<ProblemResult>>()
		{
			public void onFailure(Throwable caught) 
			{
				System.out.println(caught.getMessage());
			}

			public void onSuccess(List<ProblemResult> probResAll)
			{
				String currProbName = ""; //name of current problem

				if(probResAll.size() > 0)
				{
					//get the name of the first problem
					currProbName = probResAll.get(0).getProblemName();
				}

				//initialize list to hold current problem results
				ArrayList<ProblemResult> probRes = new ArrayList<ProblemResult>();
				
				//The loop below will exit before the last problem result is added to the list of all results
				//there is a call to add the last set after the loop
				for(int i = 0; i < probResAll.size(); i++)
				{
					if(probResAll.get(i).getProblemName().equals(currProbName))
					{
						probRes.add(probResAll.get(i)); //add to list if still on same problem result
					}
					else //new problem results encountered
					{
						//add current result list to cumulative list
						problemResults.add(probRes);                       
						//clear current result list for next problem's results
						probRes = new ArrayList<ProblemResult>();  		   
						currProbName = probResAll.get(i).getProblemName(); //set current problem name to new problem name
						probRes.add(probResAll.get(i));                    //add new problem result to list
					}
				}

				//add last problem result set to results
				if(probRes.size() > 0)
					problemResults.add(probRes);

				//build the UI
				buildUI();
			}
		};

		//perform call to get the user's problem results
		probFetchService.getProblemResults(cb);
	}

	/**
	 * Method used to build the user interface.
	 */
	private void buildUI()
	{	
		//set up the welcome label
		welcomeLabel = new Label("Welcome, " + emailAddr);
		//add click handler to logout button
		logoutButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event)
			{
				Window.Location.replace(logoutUrl);
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
		addToPanel(welcomeLabel, 40+bannerLabel.getOffsetWidth(), yCoordinate+8);
		addToPanel(logoutButton, 40+bannerLabel.getOffsetWidth()+welcomeLabel.getOffsetWidth()+2, yCoordinate+3);
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
			//loop through problem results, if result problem name matches current problem name,
			//add a button to view the results, Note: handlers are added later
			for(int j = 0; j < problemResults.size(); j++)
			{
				if(getProbNameFromResultsAtIndex(j).equals(problemList[i]))
				{
					addToPanel(viewResultButtons.get(i), 375, yCoordinate);
				}
			}
			
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
					getProblem(emailAddr, id);
				}
			});
			//add click handler for problem result selection
			for(int j = 0; j < problemResults.size(); j++)
			{
				if(getProbNameFromResultsAtIndex(j).equals(problemList[i]))
				{
					//create a new ProblemResultViewHandler and add to list
					//the ProblemResultViewHandler handles viewing past problem attempts
					resultViews.add(new ProblemResultViewHandler(originalYCoordinates.get(i), 
							512, problemResults.get(j), 
							viewResultButtons.get(i), i));
					//add click handler to button
					viewResultButtons.get(i).addClickHandler(resultViews.get(resultViews.size()-1));
				}
			}
		}
	}
	
	/**
	 * Gets the name of the problem at the index in the problem result arraylist
	 * 
	 * @param index the index of the result list
	 * @return name of the problem in the list of problem results at index
	 */
	private String getProbNameFromResultsAtIndex(int index)
	{
		if(index >= 0 && index < problemResults.size() && problemResults.get(index).size() > 0)
		{
			return problemResults.get(index).get(0).getProblemName();
		}
		else
			return "Error getting problem name from Result";
	}

	/**
	 * Adds buttons to view attempts from past problems.  Called from ProblemResultVeiwHandler 
	 * instances.
	 * 
	 * @param start start attempt number
	 * @param end end attempt number (should be 5 less than start)
	 * @param probRes list of problem results
	 * @param xOffset x coordinate
	 * @param yOffset y coordinate
	 * @param probId  id of problem
	 */
	private void addAttemptsInRange(int start, int end, final List<ProblemResult> probRes, 
			int xOffset, int yOffset, int probId)
	{
		for(int i = start; i >= end; i--)
		{
			Label l = new Label("Attempt: " + 
					probRes.get(i).getAttemptNum());
			l.setStyleName("problem");
			l.setWidth("79px");

			Button b = new Button("View");
			final int index = i;
			b.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event)
				{
					//clear the screen
					removeAllWidgets();
					//view for problem attempt
					createAndInsertProbResultPanel(probRes.get(index));
				}
			});

			//add buttons and labels to panel
			addAttemptToPanel(l, xOffset, yOffset, probId);
			xOffset += (8+l.getOffsetWidth());
			addAttemptToPanel(b, xOffset, yOffset, probId);
			yOffset += 7 + l.getOffsetHeight();
			xOffset -= (8+l.getOffsetWidth());
		}
		totalHeight = yOffset; //used in ProblemResultViewHandler
	}

	/**
	 * Method used to display attempted problem result/
	 * @param result encapsulation of a previous attempt
	 */
	private void createAndInsertProbResultPanel(ProblemResult result)
	{
		//needs comments and could be cleaned up but functions pretty well
		AbsolutePanel panel = new AbsolutePanel();
		panel.setStyleName("boundary_panel");
		panel.setPixelSize(600, 550);
		DrawingArea canvas = new DrawingArea(600, 550);
		canvas.setStyleName("canvas");
		panel.add(canvas);

		for(int i = 0; i < result.getNodes().size(); i++)
		{
			SerialNode n = result.getNodes().get(i);
			Label label = new Label(n.getValue()+"");
			label.setStyleName("node");
			panel.add(label, n.getLeft(), n.getTop()-130);
		}

		for(int i = 0; i < result.getEdges().size(); i++)
		{
			SerialEdge e = result.getEdges().get(i);
			int n1Left = 0, n1Top = 0, n2Left = 0, n2Top = 0;

			for(int j = 0; j < result.getNodes().size(); j++)
			{
				SerialNode n = result.getNodes().get(j);
				if(e.getN1() == n.getValue())
				{
					n1Left = n.getLeft();
					n1Top = n.getTop()-130;
				}
				else if(e.getN2() == n.getValue())
				{
					n2Left = n.getLeft();
					n2Top = n.getTop()-130;
				}
			}

			Line line = new Line(n1Left+20, n1Top+20, 
					n2Left+20, n2Top+20);
			line.setStrokeWidth(3);
			canvas.add(line);		
		}

		TextArea t = new TextArea();
		t.setStyleName("problem_statement");
		t.setPixelSize(598, 90);
		t.setReadOnly(true);
		t.setText(result.getProblemText());
		RootPanel.get().add(t, 2, 5);

		TextArea submitText = new TextArea();
		submitText.setCharacterWidth(30);
		submitText.setReadOnly(true);
		submitText.setVisibleLines(5);
		submitText.setText(result.getCurrFeedback());
		addToPanel(submitText, DSTConstants.SUBMIT_X, DSTConstants.SUBMIT_MESS_Y);
		RootPanel.get().add(panel, 2, 130);

		AbsolutePanel leftButtonPanel = new AbsolutePanel();
		leftButtonPanel.setPixelSize(602, 30);
		leftButtonPanel.setStyleName("left_panel");
		RootPanel.get().add(leftButtonPanel, 2, 100);

		Button backButton = new Button("Back");					
		backButton.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				Window.Location.reload();
			}
		});
		backButton.setStyleName("control_button");
		leftButtonPanel.add(backButton, 2, 2);
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
	 * Inserts a widget associated with past problem attempts at coordinates 
	 * top and let on the screen
	 * @param widget
	 * @param left x coordinate
	 * @param top y coordinate
	 * @param num index of problem result
	 */
	public void addAttemptToPanel(Widget widget, int left, int top, int num)
	{		
		if(num != lastToAdd)
		{
			removeAllAttempts();
			for(int i = 0; i < resultViews.size(); i++)
			{
				if(i != num && resultViews.get(i) != null)
					resultViews.get(i).resetButtonText();
			}
		}
		//lastToAdd is used to keep track of when to hide and reset button texts
		//after attempts are viewed
		lastToAdd = num;
		attempts.add(widget);
		RootPanel.get().add(widget, left, top);
	}

	/**
	 * Gets the problem associated probId and initializes it to be attempted
	 * @param userEmail
	 * @param probId
	 */
	private void getProblem(final String userEmail, final int probId)
	{
		problemService.getProblem(probId, new AsyncCallback<Problem>() {
			public void onFailure(Throwable caught) 
			{
				System.out.println(caught.getMessage());
			}

			public void onSuccess(Problem prob) {

				initialize(userEmail, prob, probId);
			}
		});
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

		//determine current attempt number from past attempts
		int attemptNumber = 0;
		for(int i = 0; i < problemResults.size(); i++)
		{
			if(getProbNameFromResultsAtIndex(i).equals(problemList[probNum]))
			{
				attemptNumber = problemResults.get(i).size();
			}
		}
		attemptNumber++;
		DisplayManager dm = new DisplayManager(canvas, panel, nc, ec, p, attemptNumber);
		ec.setDisplayManager(dm);
		dm.displayProblem();
	}	
	
	/**
	 * This is a private class used to handle the viewing of past problem attempts.  It encapsulates the behavior
	 * seen when cycling through past problem attempts.  It needs comments and may need some refactoring but hasn't
	 * broken as of yet.
	 */
	private class ProblemResultViewHandler implements ClickHandler
	{
		private boolean showing = false;
		private int start;
		private int end;
		private int viewProbYOffset;
		private int viewProbButtonXOffset;
		private ArrayList<ProblemResult> probRes;
		private Button viewProb;
		private int probId;

		public ProblemResultViewHandler(int viewProbYOffset, int viewProbButtonXOffset, ArrayList<ProblemResult> probRes,
				Button viewProb, int probId)
		{
			this.viewProbYOffset = viewProbYOffset;
			this.viewProbButtonXOffset = viewProbButtonXOffset;
			this.probRes = probRes;
			this.viewProb = viewProb;
			this.probId = probId;
		}

		public void resetButtonText()
		{
			viewProb.setText("View Attempts");
			showing = false;
		}

		public void onClick(ClickEvent event)
		{
			if(!showing)
			{
				start = probRes.size()-1;
				end = probRes.size() >= 5 ? probRes.size()-5 : 0;

				addAttemptsInRange(start, end, probRes, viewProbButtonXOffset, viewProbYOffset, probId);
				showing = true;
				viewProb.setText("Hide Attempts");

				if(probRes.size() > 5)
				{
					final Button nextButton = new Button("Next");
					addAttemptToPanel(nextButton, viewProbButtonXOffset+(8+79), totalHeight+5, probId);
					final Button prevButton = new Button("Prev");

					nextButton.addClickHandler(new ClickHandler(){
						public void onClick(ClickEvent event)
						{
							removeAllAttempts();
							if(start > 4) start--;
							if(end > 0) end--;
							addAttemptsInRange(start, end, probRes, viewProbButtonXOffset, viewProbYOffset, probId);
							if(start < probRes.size()-1)
								addAttemptToPanel(prevButton, viewProbButtonXOffset+(8+79), viewProbYOffset-32, probId);
							if(end > 0)
								addAttemptToPanel(nextButton, viewProbButtonXOffset+(8+79), totalHeight+5, probId);
						}
					});

					prevButton.addClickHandler(new ClickHandler(){
						public void onClick(ClickEvent event)
						{
							removeAllAttempts();
							if(start < probRes.size()-1) start++;
							if(end < probRes.size()-5) end++;
							addAttemptsInRange(start, end, probRes, viewProbButtonXOffset, viewProbYOffset, probId);
							if(start < probRes.size()-1)
								addAttemptToPanel(prevButton, viewProbButtonXOffset+(8+79), viewProbYOffset-32, probId);
							if(end > 0)
								addAttemptToPanel(nextButton, viewProbButtonXOffset+(8+79), totalHeight+5, probId);
						}
					});
				}
			}
			else
			{
				removeAllAttempts();
				resetButtonText();
			}
		}
	}
}
