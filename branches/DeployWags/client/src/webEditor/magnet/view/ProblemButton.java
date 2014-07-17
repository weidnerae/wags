package webEditor.magnet.view;

import webEditor.Common.Tokens;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Label;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * 
 * A button that has an id associated with it.
 * 
 * This id corresponds to the id in the magnetProblem table in the database.
 *
 */
public class ProblemButton extends Composite {
	
	private Label problemTitle;
	private Button reviewButton;
	private Button attemptButton;

	public ProblemButton(final int id, String title,  int status) {
		reviewButton = new Button("Review");
		attemptButton = new Button("Attempt");

		problemTitle = new Label(title);
		
		attemptButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Timer timer = new Timer() {
            		public void run() {
            			History.newItem(Tokens.MAGNETPROBLEM + Tokens.DELIM + "id=" + id);
            		}
            	};
            	timer.schedule(1);
			}
		});
		
		reviewButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Timer timer = new Timer() {
            		public void run() {
            			History.newItem(Tokens.MAGNETPROBLEM + Tokens.DELIM + "id=" + id);
            		}
            	};
            	timer.schedule(1);
			}
		});
		
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(problemTitle);
		panel.add(reviewButton);
		panel.add(attemptButton);
		if (status == 0) {
			reviewButton.setVisible(false);
			problemTitle.addStyleName("min-width-label");
		} 
		
		this.initWidget(panel);
	}
}
