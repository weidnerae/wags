package webEditor.database;

import com.google.gwt.user.client.ui.Button;

/**
 * 
 * A button that has an id associated with it.
 * 
 * This id corresponds to the id in the databaseProblem table in the database.
 *
 */
public class DatabaseProblemButton extends Button {
	private int id;
	private String title;

	public DatabaseProblemButton(String title, int id) {
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
