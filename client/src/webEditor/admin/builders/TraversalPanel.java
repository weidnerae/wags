package webEditor.admin.builders;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class TraversalPanel extends Composite {

	private static TraversalPanelUiBinder uiBinder = GWT
			.create(TraversalPanelUiBinder.class);

	interface TraversalPanelUiBinder extends UiBinder<Widget, TraversalPanel> {
	}
	
	@UiField Label lblTraversal;
	@UiField Button btnTraversal;
	@UiField TextBox txtTraversal;

	public TraversalPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setup(String lblTxt, String btnTxt){
		lblTraversal.setText(lblTxt);
		btnTraversal.setText(btnTxt);
	}
	
	public void setClickHandler(ClickHandler handler){
		btnTraversal.addClickHandler(handler);
	}
	
	public void fillText(String text){
		txtTraversal.setText(text);
	}

}
