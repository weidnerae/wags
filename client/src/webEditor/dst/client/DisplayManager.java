package webEditor.dst.client;

import java.util.ArrayList;

import webEditor.client.Proxy;
import webEditor.client.view.Wags;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * - Extend this class to create a Display manager specific to each type of problem.
 * - Initially, this was designed for problems based on trees, so it was difficult to 
 * add any other type of problem. Everything that was in here originally is now in 
 * the TreeDisplayManager class.
 * - For each type of problem, also create a new class that extends the Problem class. 
 * The createDisplayManager() method in the Problem class should return the type of 
 * DisplayManager that corresponds to each problem.
 * - You may need to override the addEvaluateButton() method. I've commented out the 
 * code that was there specific to the TreeProblem, but it may be useful in the future.
 *
 */
public abstract class DisplayManager implements IsSerializable
{	
	private ArrayList<Widget> itemsInPanel;
	@SuppressWarnings("unused")
	private TraversalContainer cont;
	private Problem problem;
	
	private Button evaluateButton;
	private TextArea submitText;
	private AbsolutePanel leftButtonPanel;
	private AbsolutePanel middlePanel;
	private AbsolutePanel rightButtonPanel;
	private Button submitOkButton;
	
	private boolean showingSubMess;

	protected abstract void addResetButton();
	
	public void displayProblem()
	{
		cont = new TraversalContainer(this);
		addProblemTextArea();
		addLeftButtonPanel();
		addMiddlePanel();
		addRightButtonPanel();
		addBackButton();
		addResetButton();
		addEvaluateButton();
	}
	
	public void forceEvaluation()
	{
		evaluateButton.click();
	}
	
	public void removeWidgetsFromPanel()
	{
		for(int i = 0; i < itemsInPanel.size(); i++)
		{
			Proxy.getDST().remove(itemsInPanel.get(i));
		}
	}
	
	public void addToPanel(Widget w, int left, int top)
	{
		itemsInPanel.add(w);
		Proxy.getDST().add(w, left, top);
	}
	
	private void addProblemTextArea()
	{
		TextArea t = new TextArea();
		t.setStyleName("problem_statement");
		t.setPixelSize(598, 90);
		t.setReadOnly(true);
		t.setText(problem.getProblemText());
		Proxy.getDST().add(t, 2, 5);
	}

	private void addLeftButtonPanel()
	{
		leftButtonPanel = new AbsolutePanel();
		leftButtonPanel.setPixelSize(130, 30);
		leftButtonPanel.setStyleName("left_panel");
		Proxy.getDST().add(leftButtonPanel, 2, 100);
	}

	private void addMiddlePanel()
	{
		middlePanel = new AbsolutePanel();
		middlePanel.setPixelSize(214, 30);
		middlePanel.setStyleName("middle_panel");
		Proxy.getDST().add(middlePanel, 132, 100);
	}

	private void addRightButtonPanel()
	{
		rightButtonPanel = new AbsolutePanel();
		rightButtonPanel.setPixelSize(382, 30);
		rightButtonPanel.setStyleName("right_panel");
		Proxy.getDST().add(rightButtonPanel, 222, 100);
	}

	private void addBackButton()
	{
		Button backButton = new Button("Back");					
		backButton.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				Wags e = new Wags("dst");
				e.go();
			}
		});
		backButton.setStyleName("control_button");
		leftButtonPanel.add(backButton, 2, 2);
	}

	private void addEvaluateButton()
	{
		evaluateButton = new Button("Evaluate");
		evaluateButton.setWidth("124px");
		evaluateButton.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{				
				//setEdgeParentAndChildren();
				//String evalResult = problem.getEval().evaluate(problem.getName(), problem.getArguments(), getNodes(), getEdges());
				
				String evalResult = problem.evaluate();

				if(showingSubMess == true)
				{
					Proxy.getDST().remove(submitText);
					Proxy.getDST().remove(submitOkButton);
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
				Proxy.getDST().remove(submitText);
				Proxy.getDST().remove(submitOkButton);
				showingSubMess = false;
			}	
		});
	}
	
	public abstract ArrayList<Node> getNodes();

	public abstract ArrayList<EdgeParent> getEdges();
}
