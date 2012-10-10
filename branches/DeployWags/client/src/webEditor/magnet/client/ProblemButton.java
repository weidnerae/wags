package webEditor.magnet.client;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

/**
 * 
 * A button that has an id associated with it.
 * 
 * This id corresponds to the id in the magnetProblem table in the database.
 *
 */
public class ProblemButton extends Button {
	private int id;

	public ProblemButton(String title, int id, ClickHandler ch) {
		super(title, ch);
		this.id = id;
	}

	public int getID() {
		return id;
	}

}
