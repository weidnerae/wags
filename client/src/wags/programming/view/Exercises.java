package wags.programming.view;

import wags.View;
import wags.WEAnchor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class Exercises extends View {

	private static ExercisesUiBinder uiBinder = GWT
			.create(ExercisesUiBinder.class);

	interface ExercisesUiBinder extends UiBinder<Widget, Exercises> {
	}

	public Exercises() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public WEAnchor getLink() {
		return new WEAnchor("View Exercises", this, "exercises");
	}

}
