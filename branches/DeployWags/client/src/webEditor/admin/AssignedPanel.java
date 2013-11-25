package webEditor.admin;

import webEditor.ProxyFacilitator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
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
	@UiField Button	btnAssign;	//add the contents to the group of assigned problems
	@UiField Button	btnClearSel;	//simply clear the problems selected, empty the text area
	@UiField Button	btnClearAssign;	//clear the text area and unassigns all the problems that are assigned
	@UiField Label title;
	AssignedPanel partner;		//the partner, selected to assigned and assigned to selected
	CheckBoxPanel exercises;
	ProxyFacilitator parent;

	public AssignedPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void clear(){
		txtAreaAssigned.setText("");
	}
	
	public void setExercises(CheckBoxPanel e) {
		this.exercises = e;
	}
	
	public void setPartner(AssignedPanel partner) {
		this.partner = partner;
	}
	
	public AssignedPanel getPartner() {
		return partner;
	}
	
	public void setAssigned(boolean assigned) {
		if (assigned) {
			btnClearAssign.setVisible( true );
		} else {
			btnClearSel.setVisible( true );
			btnAssign.setVisible( true );
		}
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
	
	public void clearExercises() {
		exercises.unsetAll();
	}
	
	@UiHandler("btnClearSel")
	public void clearSelectedHandler(ClickEvent event) {
		clear();
		clearExercises();
	}
	
	@UiHandler("btnAssign")
	public void assignHandler(ClickEvent event) {
		//get the ones currently selected
		String exSel = txtAreaAssigned.getText();
		exSel = exSel.substring( 0, exSel.length() - 1 );
		String[] exSelArray = txtAreaAssigned.getText().split("\n");
		//get the ones already assigned from the partner panel
		String exAssigned = partner.txtAreaAssigned.getText();
		exAssigned = exAssigned.substring( 0, exSel.length() - 1 );
		String[] exAssignedArray = partner.txtAreaAssigned.getText().split("\n");
		
		//concatenate the string arrays
      String[] both = new String[exAssignedArray.length + exSelArray.length];
      int i;
      for (i = 0; i < exAssignedArray.length; i++) {
      	both[i] = exAssignedArray[i];
      }
      for (i = 0; i < exSelArray.length; i++) {
              both[exAssignedArray.length + i] = exSelArray[i];
      }
      //assign both
      parent.setExercises(both);
      
      //add the exercises from selected (this panel)
      //to the end of the contents of assigned (the partner panel)
		partner.txtAreaAssigned.setText( partner.txtAreaAssigned.getText() + txtAreaAssigned.getText() );
	}
	
	@UiHandler("btnClearAssign")
	public void clearAssignHandler(ClickEvent event) {
		String[] bum = {""};
		parent.setExercises(bum);
	}
}
