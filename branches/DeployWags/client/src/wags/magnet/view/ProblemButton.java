package wags.magnet.view;

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
	private String title;

	public ProblemButton(String title, int id) {
		super(title);
		this.id = id;
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}

	public int getID() {
		return id;
	}

}
