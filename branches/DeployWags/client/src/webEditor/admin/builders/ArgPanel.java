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

public class ArgPanel extends Composite implements ArgHolder{

	private static TraversalPanelUiBinder uiBinder = GWT
			.create(TraversalPanelUiBinder.class);

	// refactoring doesn't change this interface, but no harm done
	interface TraversalPanelUiBinder extends UiBinder<Widget, ArgPanel> {
	}
	
	@UiField Label lblTraversal;
	@UiField Button btnTraversal;
	@UiField TextBox txtTraversal;

	// Class renamed to "ArgPanel" as it really has nothing inherently to do
	// with traversals.  Didn't rename widgets though
	public ArgPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	/**
	 * setup
	 * @param lblTxt - String label will display
	 * @param btnTxt - String ubtton will display
	 * Adds text to ArgPanel, disables button until argument added via fillText
	 */
	public void setup(String lblTxt, String btnTxt){
		lblTraversal.setText(lblTxt);
		btnTraversal.setText(btnTxt);
		txtTraversal.setReadOnly(true);
		btnTraversal.setEnabled(false);
	}
	
	public void setClickHandler(ClickHandler handler){
		btnTraversal.addClickHandler(handler);
	}
	
	/**
	 * fillText 
	 * @param text - Text that goes into textbox
	 * Fills textBox with argument, enables button
	 */
	public void fillText(String text){
		txtTraversal.setText(text);
		btnTraversal.setEnabled(true);
	}
	
	public String[] getArguments(){
		String[] args = new String[1];
		args[0] = txtTraversal.getText();
		return args;
	}
	
	public void clear(){
		txtTraversal.setText("");
	}

}
