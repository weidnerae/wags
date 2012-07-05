package webEditor.magnet.client;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

public class ProblemButton extends Button {
	int id;

	public ProblemButton(String title, int id, ClickHandler ch) {
		super(title, ch);
		this.id = id;
	}

	public int getID() {
		return id;
	}

}
