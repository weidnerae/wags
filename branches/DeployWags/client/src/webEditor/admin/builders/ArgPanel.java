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
	
	@UiField Label lblArg;
	@UiField Button btnArg;
	@UiField TextBox txtArg;

	// Class renamed to "ArgPanel" as it really has nothing inherently to do
	// with traversals.  Didn't rename widgets though
	public ArgPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	/**
	 * setup
	 * @param lblTxt - String label will display
	 * @param btnTxt - String button will display
	 * Adds text to ArgPanel, disables button until argument added via fillText
	 */
	public void setup(String lblTxt, String btnTxt){
		lblArg.setText(lblTxt);
		btnArg.setText(btnTxt);
		txtArg.setReadOnly(true);
		btnArg.setEnabled(false);
	}
	
	public void setClickHandler(ClickHandler handler){
		btnArg.addClickHandler(handler);
	}
	
	/**
	 * fillText 
	 * @param text - Text that goes into textbox
	 * Fills textBox with argument, enables button
	 */
	public void fillText(String text){
		txtArg.setText(text);
		btnArg.setEnabled(true);
	}
	
	public String[] getArguments(){
		String[] args = new String[1];
		args[0] = txtArg.getText();
		return args;
	}
	
	public void clear(){
		txtArg.setText("");
	}
	
	public String getText() {
		return txtArg.getText();
	}
	
	/**
	 * We may want to use the functionality of the arg panel without the use of the button. For
	 * instance the Build BT problem creation will display both a preorder and inorder traversal
	 * but there is no need to have two buttons.
	 * 
	 * @param isVisible boolean value, will set the button visible is true, invisible if false
	 * 
	 */
	public void setButtonVisible(boolean isVisible) {
		btnArg.setVisible(isVisible);
		btnArg.setEnabled(isVisible);
	}

}
