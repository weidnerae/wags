package webEditor.admin;

import webEditor.ProxyFacilitator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class AssignedPanel extends Composite {

	private static AssignedPanelUiBinder uiBinder = GWT
			.create(AssignedPanelUiBinder.class);

	interface AssignedPanelUiBinder extends UiBinder<Widget, AssignedPanel> {
	}
	
	@UiField TextArea txtAreaAssigned;
	@UiField Button	btnAssign;
	@UiField Label title;
	ProxyFacilitator parent;

	public AssignedPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		btnAssign.addClickHandler(new myClickHandler());
	}
	
	public void clear(){
		txtAreaAssigned.setText("");
	}
	
	public void setTitle(String title){
		this.title.setText(title);
	}
	
	public void setParent(ProxyFacilitator parent){
		this.parent = parent;
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
	
	public String[] toStringArray(){
		String exercises = txtAreaAssigned.getText();
		exercises = exercises.substring(0, exercises.length()-1);
		return exercises.split("\n");
	}
	
	private class myClickHandler implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			String exercises = txtAreaAssigned.getText();
			exercises = exercises.substring(0, exercises.length()-1); // drop last \n
			String[] exerciseArray = txtAreaAssigned.getText().split("\n");
			
			parent.setExercises(exerciseArray);
		}
	}
}
