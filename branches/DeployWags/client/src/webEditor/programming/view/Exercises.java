package webEditor.programming.view;

//import webEditor.View;
//import webEditor.WEAnchor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Exercises extends Composite {

	private static ExercisesUiBinder uiBinder = GWT
			.create(ExercisesUiBinder.class);

	interface ExercisesUiBinder extends UiBinder<Widget, Exercises> {
	}

	public Exercises() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
