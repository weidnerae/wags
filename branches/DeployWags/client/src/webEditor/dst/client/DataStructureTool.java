package webEditor.dst.client;

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

import webEditor.client.Proxy;


public class DataStructureTool  extends AbsolutePanel
{	
	public static int INCOMPLETE = 0;
	public static int SUCCESS = 1;
	public static int REVIEW = 2;

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
	private ArrayList<Button> attemptButtons;
	private ArrayList<Button> reviewButtons;

	/**
	 * This is the entry point method.
	 */
	public DataStructureTool(String[] problems, int[] status) 
	{	
		//initialize fields that will be used
		widgets = new ArrayList<Widget>();
		problemList = problems;
		statusList = status;

		//initialize widgets
		bannerLabel = new Label("Logical Microlabs");
		attemptButtons = new ArrayList<Button>();
		reviewButtons = new ArrayList<Button>();
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
			Button b = new Button(statusList[i] == SUCCESS ? "<font color=green>" + problemList[i] + "</font>" : problemList[i]);

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
	private void buildUI(final ArrayList<Button> buttons)
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
				int maxWidth = bannerLabel.getOffsetWidth();

				for (int i = 0; i < buttons.size(); i++) {
					// add button to attempt problem, Note: handlers are added later
					Button b = buttons.get(i);
					addClickHandling(b, b.getText());
					b.setStyleName("problem");
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
			}
		};
		timer.schedule(1);
	}
	
	private HorizontalPanel buildButtonPanel() {
		assigned = new Button("Switch to Assigned");
		review = new Button("Switch to Review");
		
		assigned.setVisible(false);
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
	
	private void addClickHandling(Button button, final String problem){
		button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				removeAllWidgets();
				Timer timer = new Timer(){
					 public void run() {
						 getProblem(problem);
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
	 * @param probId
	 */
	private void getProblem(String problem)
	{
		/* This is the 'switch' between DB LogicalMicrolabs and
		 * ProblemServiceImpl
		 */
		//Proxy.getLogicalMicrolab(problem, this);
		Problem prob = ProblemServiceImpl.getProblem(problem);

		initialize(prob);
	}

	public void initialize(Problem p)
	{
		Window.alert("ok");
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
