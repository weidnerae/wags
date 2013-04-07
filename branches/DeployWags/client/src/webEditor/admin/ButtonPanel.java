package webEditor.admin;

/*  
 * ButtonPanel
 * 
 * Parent class that automates adding buttons to a panel and assigning
 * clickhandlers to each button
 */

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ButtonPanel extends Composite {

	private static ButtonPanelUiBinder uiBinder = GWT
			.create(ButtonPanelUiBinder.class);

	interface ButtonPanelUiBinder extends UiBinder<Widget, ButtonPanel> {
	}
	
	@UiField Label title;
	@UiField VerticalPanel btnHolder;
	ArrayList<Button> myButtons = new ArrayList<Button>();
	private int btnWidth = 175;
	private int btnHeight = 45;
	private int CELL_SPACING = 5;

	public ButtonPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		this.getElement().getStyle().setOverflowY(Overflow.AUTO);
	}
	
	public void setTitle(String title){
		this.title.setText(title);
	}
	
	public void addButtons(String[] buttons){
		btnHolder.clear();
		myButtons.clear();
		Button tmpBtn;
		
		for(String button: buttons){
			tmpBtn = new Button(button);
			tmpBtn.setStyleName("problem");
			tmpBtn.setVisible(true);
			tmpBtn.setPixelSize(btnWidth, btnHeight);
			this.btnHolder.add(tmpBtn);
			myButtons.add(tmpBtn);
		}
		
		/*
		// Attaching stuff to the DOM takes too long.....
		Timer timer = new Timer(){
			public void run(){
				sizeButtons();
			}
		};
		
		timer.schedule(1);
		*/
	}
	
	public void sizeButtons(){
		this.btnHolder.setSpacing(CELL_SPACING);
		
		for(int i = 0; i < myButtons.size(); i++){
			Button tmpButton = myButtons.get(i);
			tmpButton.setVisible(true);
			
			// make btnWidth & btnHeight larger if necessary
			if(tmpButton.getOffsetWidth() > btnWidth){
				btnWidth = tmpButton.getOffsetHeight();
			}
		}
		
		// See if buttons need to be longer to align with title
		if(btnWidth < this.title.getOffsetWidth()){
			btnWidth = this.title.getOffsetWidth();
		} else {
			// To stop split second "shrinking" when changing buttons
			this.title.setWidth(btnWidth+"px");
		}
		
		// Set proportions to something that looks nice
		btnHeight = btnWidth * 4 / 10;
		
		// Actually size buttons
		for(int i = 0; i < myButtons.size(); i++){
			myButtons.get(i).setSize(btnWidth+"px", btnHeight+"px");
		}
	}
	
	public int getButtonWidth(){
		return btnWidth;
	}
	
	public int getButtonHeight(){
		return btnHeight;
	}
	
	public void setButtonWidth(int pixels){
		btnWidth = pixels;
	}
	
	public void setButtonHeight(int pixels){
		btnHeight = pixels;
	}

}
