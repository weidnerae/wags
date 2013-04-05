package webEditor.admin;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CheckBoxPanel extends Composite {

	private static CheckBoxPanelUiBinder uiBinder = GWT
			.create(CheckBoxPanelUiBinder.class);

	interface CheckBoxPanelUiBinder extends UiBinder<Widget, CheckBoxPanel> {
	}
	
	@UiField Label title;
	@UiField VerticalPanel chkBoxHolder;
	@UiField Button btnSetExercises;
	private HashMap<String, CheckBox> allExercises = new HashMap<String, CheckBox>();
	private ArrayList<CheckBox> currentExercises = new ArrayList<CheckBox>();

	public CheckBoxPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		btnSetExercises.addClickHandler(new myClickHandler());
		btnSetExercises.setText("Set Exercises");
	}
	
	public void setTitle(String title){
		this.title.setText(title);
	}
	
	public void clearCheckBoxes(){
		for(int i = 0; i < currentExercises.size(); i++){
			currentExercises.get(i).setVisible(false);
		}
		currentExercises.clear();
	}
	
	public void addCheckBoxes(String[] chkBoxes){
		for(String chkBox: chkBoxes){
			// If the chkBox has been created already
			if(allExercises.containsKey(chkBox)){
				allExercises.get(chkBox).setVisible(true);
			} else {
				CheckBox tmpChk = new CheckBox(chkBox);	// create
				allExercises.put(chkBox, tmpChk);		// record
				chkBoxHolder.add(tmpChk);				// place
				tmpChk.setVisible(true);				// show
			}
			
			currentExercises.add(allExercises.get(chkBox));	// in use
		}
	}
	
	// Needed for unassinging currently assigned exercises
	public HashMap<String, CheckBox> getAssignments(){
		return allExercises;
	}
	
	private class myClickHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			Window.alert("Default clickhandler - maybe will be made abstract!");
		}
		
	}

}
