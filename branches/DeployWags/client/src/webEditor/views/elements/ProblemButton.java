package webEditor.views.elements;

import webEditor.Common.Tokens;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * A button that has an id associated with it.
 * 
 * This id corresponds to the id in the magnetProblem table in the database.
 *
 */
public class ProblemButton extends Composite {
	
	private static ProblemButtonUiBinder uiBinder = GWT
			.create(ProblemButtonUiBinder.class);

	interface ProblemButtonUiBinder extends UiBinder<Widget, ProblemButton> {
	}
	
	private static final int MAX_STRING_SIZE = 50;
	
	@UiField Icon statusIcon;
	@UiField Button button;

	public ProblemButton(final int id, String title,  int status) {
		initWidget(uiBinder.createAndBindUi(this));
		if (title.length() > MAX_STRING_SIZE) {
			title = title.substring(0, MAX_STRING_SIZE) + "...";
		}
		button.setText(title);
		
		if (status == 0) {
			statusIcon.setIcon(IconType.EXCLAMATION);
			button.addStyleName("problem_due");
		} else if (status == 1) {
			button.addStyleName("problem_complete");
		} else {
			button.addStyleName("problem_review");
		}
		
		button.addClickHandler(new ClickHandler() {
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

	}

}
