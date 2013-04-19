package webEditor.admin.builders;

import webEditor.admin.LMDisplay;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class LMTraversalDisplay extends LMDisplay {

	private static LMTraversalDisplayUiBinder uiBinder = GWT
			.create(LMTraversalDisplayUiBinder.class);

	interface LMTraversalDisplayUiBinder extends
			UiBinder<Widget, LMTraversalDisplay> {
	}

	LMBuilder builder;
	
	public LMTraversalDisplay() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setBuilder(LMBuilder builder){
		this.builder = builder;
	}

}
