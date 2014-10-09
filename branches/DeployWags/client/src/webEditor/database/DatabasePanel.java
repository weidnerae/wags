package webEditor.database;

import java.util.ArrayList;

import org.vaadin.gwtgraphics.client.DrawingArea;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import webEditor.ProxyFramework.AbstractServerCall;
import webEditor.ProxyFramework.GetDatabaseProblemCommand;


public class DatabasePanel  extends AbsolutePanel
{	
	public static int INCOMPLETE = 0;
	public static int SUCCESS = 1;
	public static int REVIEW = 2;
	private int[] idList;				//array of problem id numbers
	private static String[] problemList;	//array of problem names
	private static int[] statusList;	//array of success values
	
	final VerticalPanel problemPane = new VerticalPanel();
	private HorizontalPanel topButtonPanel; // Hold "Switch to X" Buttons

	//widgets
	private ArrayList<Widget> widgets; //arraylist to hold widgets added to root panel
	private Label bannerLabel;
	private Label selectLabel;
	private Button assigned;
	private Button review;
	private ArrayList<DatabaseProblemButton> attemptButtons;
	private ArrayList<DatabaseProblemButton> reviewButtons;
	boolean initialLoad = true;

	/**
	 * This is the entry point method.
	 */
	public DatabasePanel(int[] ids, String[] problems, int[] status) 
	{	
		//initialize fields that will be used
		widgets = new ArrayList<Widget>();
		problemList = problems;
		statusList = status;
		idList = ids;
		//initialize widgets
		bannerLabel = new Label("Database Microlabs");
		attemptButtons = new ArrayList<DatabaseProblemButton>();
		reviewButtons = new ArrayList<DatabaseProblemButton>();
		createButtons();
		topButtonPanel = buildButtonPanel();
		
		buildUI(attemptButtons);
		
		//set styles
		setStyleName("main_background");
		bannerLabel.setStyleName("banner");
		selectLabel.setStyleName("welcome");
	}
	
	private void createButtons() {
		//create an attempt button for each problem
		for (int i = 0; i < problemList.length; i++) {
			//create button that allows a problem to be attempted
			DatabaseProblemButton b = new DatabaseProblemButton(statusList[i] == SUCCESS ? "<font color=green>" + problemList[i] + "</font>" : problemList[i], idList[i]);

			if (statusList[i] == REVIEW) {
				reviewButtons.add(b);
			} else {
				attemptButtons.add(b);
			}
		}
	}

	/**
	 * Method used to build the user interface.
	 */
	private void buildUI(final ArrayList<DatabaseProblemButton> buttons)
	{	
		this.removeAllWidgets();
		add(problemPane);
		problemPane.clear();
		problemPane.setSpacing(5);
		problemPane.add(bannerLabel);
		problemPane.add(topButtonPanel);
		
		if (buttons.size() > 0) {
			selectLabel = new Label(""); 
		} else {
			selectLabel = new Label("No problems assigned!");
		}
		
		problemPane.add(selectLabel);
		
		//Wrap all this stuff in a timer so that
		//getOffsetWidth() works correctly (I am ashamed)
		Timer timer = new Timer() {
			public void run() {
				int maxWidth = 250;

				for (int i = 0; i < buttons.size(); i++) {
					// add button to attempt problem, Note: handlers are added later
					DatabaseProblemButton b = buttons.get(i);
					addClickHandling(b, b.getID());
					b.setStyleName("button");
					problemPane.add(b);

					// find the maximum width of buttons
					int width = buttons.get(i).getOffsetWidth();
					maxWidth = (width > maxWidth) ? width : maxWidth;
				}

				for (int i = 0; i < buttons.size(); i++) {
					Button b = buttons.get(i);
					b.setWidth(maxWidth + "px");
					b.setHeight("50px");
				}
				
				assigned.setWidth(maxWidth + "px");
				review.setWidth(maxWidth + "px");
				if(initialLoad){
					review.setVisible(true);
					initialLoad = false;
				}
			}
		};
		timer.schedule(1);
	}
	
	private HorizontalPanel buildButtonPanel() {
		assigned = new Button("Switch to Assigned");
		review = new Button("Switch to Review");
		
		assigned.setVisible(false);
		review.setVisible(false);
		assigned.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				buildUI(attemptButtons);
				assigned.setVisible(false);
				review.setVisible(true);
			}
		});
		
		review.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				buildUI(reviewButtons);
				review.setVisible(false);
				assigned.setVisible(true);
			}
		});
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(assigned);
		hp.add(review);
		
		return hp;
	}
	
	private void addClickHandling(DatabaseProblemButton button, final int id){
		button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				removeAllWidgets();
				Timer timer = new Timer(){
					 public void run() {
						 getProblem(id);
						 History.newItem("?loc=databaseproblem");
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
	 * @param probId
	 */
	private void getProblem(int id)
	{
		/* This is the 'switch' between DB LogicalMicrolabs and
		 * ProblemServiceImpl
		 */
		AbstractServerCall cmd = new GetDatabaseProblemCommand(id, this);
		cmd.sendRequest();
		//Proxy.getDatabaseProblem(id, this);
		//DatabasePanel.initialize will be called after the problem is retrieved
	}

	public void initialize(DatabaseProblem p)
	{
		// Hide the buttons and stuff
		problemPane.setVisible(false);
		
		//reset possible scrolling on problem selection screen
		Window.scrollTo(0,0);
		
		//initialize the necessary components to display the problem
		AbsolutePanel panel = new AbsolutePanel();
		panel.setStyleName("boundary_panel");
		panel.setPixelSize(600, 550);
		DrawingArea canvas = new DrawingArea(600, 550);
		canvas.setStyleName("canvas");
		panel.add(canvas);
		this.add(panel, 2, 130);
		this.setStyleName("prob_background");
	}
	
}
