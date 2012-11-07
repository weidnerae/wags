package webEditor.dst.client;

import java.util.ArrayList;

import org.vaadin.gwtgraphics.client.DrawingArea;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import webEditor.client.Proxy;


public class DataStructureTool  extends AbsolutePanel
{	
	private ArrayList<Widget> widgets; //arraylist to hold widgets added to root panel

	private static String[] problemList;	//array of problem names
	private static boolean[] successList;	//array of success values

	private String emailAddr;	//user's email address
	final VerticalPanel problemPane = new VerticalPanel();

	//widgets
	private Label bannerLabel;
	private Label selectLabel;
	private ArrayList<Button> attemptButtons;

	/**
	 * This is the entry point method.
	 */
	public DataStructureTool(String[] problems, boolean[] success) 
	{	
		//initialize fields that will be used
		widgets = new ArrayList<Widget>();
		problemList = problems;
		successList = success;

		//initialize widgets
		bannerLabel = new Label("Logical Microlabs");
		if(problems.length > 0){
			selectLabel = new Label(""); 
		} else {
			selectLabel = new Label("No problems assigned!");
		}
		//problemLabels = new ArrayList<Label>();
		attemptButtons = new ArrayList<Button>();


		//set styles
		this.setStyleName("main_background");
		bannerLabel.setStyleName("banner");
		selectLabel.setStyleName("welcome");

		//Note: method calls to initialize and build the app have to be buried in the onSuccess methods
		//of the asynchronous calls to ensure that everything is initialized in order
		//start asynchronous calls
		buildUI();
	}

	/**
	 * Method used to build the user interface.
	 */
	private void buildUI()
	{	
		this.removeAllWidgets();
		
		//create an attempt button for each problem
		for(int i = 0; i < problemList.length; i++)  //The explode php function results in one extra, empty index
		{
			//create button that allows a problem to be attempted
			attemptButtons.add(new Button(successList[i] ? "<font color=green>" + problemList[i] + "</font>" : problemList[i]));
			//create button that allows past attempts to be viewed if present
			//viewResultButtons.add(new Button("View Attempts"));
		}
		
		add(problemPane);
		problemPane.clear();
		problemPane.setSpacing(5);
		problemPane.add(bannerLabel);
		problemPane.add(selectLabel);
		
		//Wrap all this stuff in a timer so that
		//getOffsetWidth() works correctly (I am ashamed)
		Timer timer = new Timer() {
			public void run() {
				int maxWidth = bannerLabel.getOffsetWidth();

				for (int i = 0; i < problemList.length; i++) {
					// add button to attempt problem, Note: handlers are added later
					Button b = attemptButtons.get(i);
					addClickHandling(b, b.getText());
					b.setStyleName("problem");
					problemPane.add(b);

					// find the maximum width of buttons
					int width = attemptButtons.get(i).getOffsetWidth();
					maxWidth = (width > maxWidth) ? width : maxWidth;
				}

				for (int i = 0; i < problemList.length; i++) {
					Button b = attemptButtons.get(i);
					b.setWidth(maxWidth + "px");
					b.setHeight("50px");
				}
			}
		};
		timer.schedule(1);
	}
	
	private void addClickHandling(Button button, final String problem){
		button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				removeAllWidgets();
				emailAddr = "TestUser";
				Timer timer = new Timer(){
					 public void run() {
						 getProblem(emailAddr, problem);
						 History.newItem("?loc=dstproblem");
					 }
				};
				timer.schedule(1);
				
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
		// Hide the buttons and stuff
		problemPane.setVisible(false);
		
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
