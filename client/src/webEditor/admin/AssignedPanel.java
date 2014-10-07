package webEditor.admin;

import java.util.ArrayList;

import webEditor.ProxyFacilitator;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
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
	
	private AssignedPanel partner;		//the partner, selected to assigned and assigned to selected
	private CheckBoxPanel exercises;
	private ProxyFacilitator parent;
	
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
	
	public void addAll(String[] text) {
		for(int i = 0; i < text.length; i++) {
				add(text[i]);
		}
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
	
	/**
	 * @author Dakota Murray
	 * @version 16 March 2014
	 * 
	 * Description: Handles behavior of the "add to assigned" button in logical
	 * 				problem creation. Does not allow suplicate exercises to be assigned.
	 * 
	 * Note: 		This is kind of messy complexity wise but I feel that all the operations are 
	 * 				necessary in order to ensure that there are no duplicates and that the 
	 * 				button displays the proper behavior. 
	 * 
	 * @param event an event triggered by clicking the "btnAssign" button
	 */
	@UiHandler("btnAssign")
	public void assignHandler(ClickEvent event) {
		//get the ones currently selected
		String[] toAssign = this.toStringArray();
		//get the ones already assigned from the partner panel
		String[] alreadyAssigned = partner.toStringArray();
		//concatenate the string arrays
		ArrayList<String> finalAssigned = new ArrayList<String>();
		for (int i = 0; i < alreadyAssigned.length; i++) {
      	    if(alreadyAssigned[i] != "") {
      	    	finalAssigned.add(alreadyAssigned[i]);
      	    }
        }
        boolean isDupe = false;
        for (int i = 0; i < toAssign.length; i++) {
        	for(int j = 0; j < alreadyAssigned.length; j++) {
        	    //if not a duplicate, add to assigned
            	if(toAssign[i].equals(alreadyAssigned[j])) {
            		isDupe = true;
        	    }
            }
        	if( !isDupe ){
        		finalAssigned.add(toAssign[i]);	
        	}
        	isDupe = false;
        }
        String[] finalArray = new String[finalAssigned.size()];
        finalArray = finalAssigned.toArray(finalArray);
        //assign both
        parent.setExercises(finalArray);
        //add the exercises from selected (this panel)
        //to the end of the contents of assigned (the partner panel)
        partner.clear();
		partner.addAll(finalArray);
	}
	
	@UiHandler("btnClearAssign")
	public void clearAssignHandler(ClickEvent event) {
		String[] bum = {""};
		parent.setExercises(bum);
	}
}
