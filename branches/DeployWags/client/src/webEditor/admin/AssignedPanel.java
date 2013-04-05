package webEditor.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class AssignedPanel extends Composite {

	private static AssignedPanelUiBinder uiBinder = GWT
			.create(AssignedPanelUiBinder.class);

	interface AssignedPanelUiBinder extends UiBinder<Widget, AssignedPanel> {
	}
	
	@UiField TextArea txtAreaAssigned;
	@UiField Button	btnAssign;

	public AssignedPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		btnAssign.addClickHandler(new myClickHandler());
	}
	
	public void clear(){
		txtAreaAssigned.setText("");
	}
	
	public void add(String text){
		String tmpText = txtAreaAssigned.getText();
		tmpText += text + "\n";
		txtAreaAssigned.setText(tmpText);
	}
	
	public void remove(String text){
		String tmpText = txtAreaAssigned.getText();
		tmpText = tmpText.replace(text + "\n", "");
		txtAreaAssigned.setText(tmpText);
	}
	
	private class myClickHandler implements ClickHandler{
		
		public void onClick(ClickEvent event) {
			Window.alert("Would assign all");
		}
		
	}
}
