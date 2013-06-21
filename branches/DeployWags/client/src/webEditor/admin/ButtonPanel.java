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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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
	private int CELL_SPACING = 3;

	public ButtonPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		this.getElement().getStyle().setOverflowY(Overflow.AUTO);
		btnHolder.setSpacing(CELL_SPACING);
	}
	
	public void setTitle(String title){
		this.title.setText(title);
	}
	
	public void addButtons(String[] buttons){
		btnHolder.clear();
		myButtons.clear();
		Button tmpBtn;
		boolean createdButton = false;
		
		for(String button: buttons){
			if(button == "Created"){
				createdButton = true;
				continue;  // we want "created" to be last
			}
			tmpBtn = new Button(button);
			tmpBtn.setStyleName("button");
			tmpBtn.setVisible(true);
			this.btnHolder.add(tmpBtn);
			myButtons.add(tmpBtn);
		}
		
		// "Created" button last
		if(createdButton){
			tmpBtn = new Button("Created");
			tmpBtn.setStyleName("button");
			tmpBtn.setVisible(true);
			tmpBtn.setPixelSize(btnWidth, btnHeight);
			this.btnHolder.add(tmpBtn);
			myButtons.add(tmpBtn);
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
	
	public void colorBlack(){
		for(Button btn: myButtons){
			btn.getElement().getStyle().setColor("black");
		}
	}
	
	/**
	 * Adds clickhandlers for highlighting the currently selected button
	 */
	public void setClickHandlers(){
		for(Button btn: myButtons){
			btn.addClickHandler(new btnPanelClickHandler(btn));
		}
	}
	
	private class btnPanelClickHandler implements ClickHandler{
		Button btn;
		
		public btnPanelClickHandler(Button btn){
			this.btn = btn;
		}
		
		public void onClick(ClickEvent event) {
			colorBlack();
			btn.getElement().getStyle().setColor("blue");
		}
	
	}

}
