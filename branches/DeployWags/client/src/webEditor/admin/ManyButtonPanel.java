package webEditor.admin;

import java.util.ArrayList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ManyButtonPanel extends Composite
{

	private static ManyButtonPanelUiBinder uiBinder = GWT.create( ManyButtonPanelUiBinder.class );

	interface ManyButtonPanelUiBinder extends UiBinder< Widget, ManyButtonPanel >
	{}

	
	@UiField Label title;
	@UiField VerticalPanel btnHolder;
	@UiField VerticalPanel btnHolder2;
	@UiField VerticalPanel btnHolder3;
	@UiField VerticalPanel btnHolder4;
	@UiField HorizontalPanel btnHolderHolder;
	ArrayList<Button> myButtons = new ArrayList<Button>();
	private int btnWidth = 175;
	private int btnHeight = 45;
	private int CELL_SPACING = 3;
	private int numButtons;

	public ManyButtonPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		this.getElement().getStyle().setOverflowY(Overflow.AUTO);
		btnHolder.setSpacing(CELL_SPACING);
		btnHolder2.setSpacing(CELL_SPACING);
		btnHolder3.setSpacing(CELL_SPACING);
		btnHolder4.setSpacing(CELL_SPACING);
	}
	
	public void setTitle(String title){
		this.title.setText(title);
	}
	
	public void addButtons(String[] buttons){
		this.numButtons = buttons.length/4 +1;
		btnHolder.clear();
		btnHolder2.clear();
		btnHolder3.clear();
		btnHolder4.clear();
		myButtons.clear();
		Button tmpBtn;
		
		for(String button: buttons){
			tmpBtn = new Button(button);
			tmpBtn.setStyleName("button");
			tmpBtn.setVisible(true);
			myButtons.add(tmpBtn);
			addButton(tmpBtn);
		}
	}
	
	private void addButton( Button tmpBtn )
	{
		int numberOfButtons = myButtons.size();
		if(numButtons*4 <=60){
			if (numberOfButtons <= 15) {
				this.btnHolder.add(tmpBtn);
			} else if (numberOfButtons <= 30) {
				this.btnHolder2.add(tmpBtn);
			} else if (numberOfButtons <= 45) {
				this.btnHolder3.add(tmpBtn);
			} else if (numberOfButtons <= 60) {
				this.btnHolder4.add(tmpBtn);
			}
		}else{
			if (numberOfButtons <= numButtons) {
				this.btnHolder.add(tmpBtn);
			} else if (numberOfButtons <= numButtons * 2) {
				this.btnHolder2.add(tmpBtn);
			} else if (numberOfButtons <= numButtons * 3) {
				this.btnHolder3.add(tmpBtn);
			} else {
				this.btnHolder4.add(tmpBtn);
			}
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
	
	private class btnClickHandler implements ClickHandler{
		Button btn;
		
		public btnClickHandler(Button btn){
			this.btn = btn;
		}
		
		public void onClick(ClickEvent event) {
			colorBlack();
			btn.getElement().getStyle().setColor("blue");
		}
		
	}
	
	void colorBlack(){
		for(Button btn: myButtons){
			btn.getElement().getStyle().setColor("black");
		}
	}
	
	public void setClickHandlers(){
		for(Button btn: myButtons){
			btn.addClickHandler(new btnClickHandler(btn));
		}
	}
	
	


}
