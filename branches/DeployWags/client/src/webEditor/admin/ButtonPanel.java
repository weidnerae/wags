package webEditor.admin;

/*  
 * ButtonPanel
 * 
 * Parent class that automates adding buttons to a panel and assigning
 * clickhandlers to each button
 */

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
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
	private int btnWidth = 0;
	private int btnHeight = 0;
	private int CELL_SPACING = 5;

	public ButtonPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setTitle(String title){
		this.title.setText(title);
	}
	
	public void addButtons(String[] buttons){
		for(String button: buttons){
			this.btnHolder.add(new Button(button));
		}
		
		this.addClickHandlers();
	}
	
	private void addClickHandlers(){
		for(int i = 0; i < btnHolder.getWidgetCount(); i++){
			// If some non-button snuck into btnHolder, bail
			if(!(btnHolder.getWidget(i) instanceof Button)){
				Window.alert("That's not a button!!");
				break;
			}
			
			// Add click handlers to the button
			Button tmpButton = (Button) btnHolder.getWidget(i);
			myButtons.add(tmpButton);
			tmpButton.addClickHandler(new myClickHandler(tmpButton.getText()));
		}
		
		// Attaching stuff to the DOM takes too long.....
		Timer timer = new Timer(){
			public void run(){
				sizeButtons();
			}
		};
		
		timer.schedule(1);
	}
	
	private void sizeButtons(){
		this.btnHolder.setSpacing(CELL_SPACING);
		
		for(int i = 0; i < myButtons.size(); i++){
			Button tmpButton = myButtons.get(i);
			tmpButton.setStyleName("problem");
			
			// make btnWidth & btnHeight larger if necessary
			if(tmpButton.getOffsetWidth() > btnWidth){
				btnWidth = tmpButton.getOffsetHeight();
			}
		}
		
		// See if buttons need to be longer to align with title
		if(btnWidth < this.title.getOffsetWidth()){
			btnWidth = this.title.getOffsetWidth();
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
	
	private class myClickHandler implements ClickHandler{
		String name;
		
		public myClickHandler(String name){
			this.name = name;
		}
		
		@Override
		public void onClick(ClickEvent event) {
			Window.alert(name + " has default clickhandler!");
		}
		
	}

}
