package webEditor.admin;

import webEditor.ProxyFacilitator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

public class ReviewPanel extends Composite {

	private static ReviewPanelUiBinder uiBinder = GWT
			.create(ReviewPanelUiBinder.class);

	interface ReviewPanelUiBinder extends UiBinder<Widget, ReviewPanel> {
	}
	
	@UiField Button btnExTypes;
	@UiField ButtonPanel btnPnlCurrent, btnPnlReview;
	@UiField Grid grdGrades;
	
	ProxyFacilitator parent;
	boolean currentSet = true;  // keeps track of "Assign/Review" state

	public ReviewPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		btnPnlCurrent.title.setVisible(false);
		btnPnlReview.title.setVisible(false);
		btnPnlReview.setVisible(false);
		
		btnExTypes.addClickHandler(new switchClickHandler());
	}
	
	public void setParent(ProxyFacilitator parent){
		this.parent = parent;
	}
	
	public void fillGrid(String[] data){
        grdGrades.resize(data.length/3+1, 3);
  		grdGrades.setBorderWidth(1);
  		
  		//Sets the headers for the table
  		grdGrades.setHTML(0, 0, "<b> Username </b>");
  		grdGrades.setHTML(0, 1, "<b> Correct </b>");
  		grdGrades.setHTML(0, 2, "<b> NumAttempts </b>");
  		
  		int k = 0, row = 1;
  		//Fills table with results - stops before last row (the summary)
  	    for (row = 1; row < data.length/3; ++row) {
  	      for (int col = 0; col < 3; ++col)
  	        grdGrades.setText(row, col, data[k++]);
  	    }
  	    // Last row
  	    grdGrades.setHTML(row, 0, "<b> " + data[data.length - 3] + " </b>");
  	    grdGrades.setHTML(row, 1, "<b> " + data[data.length - 2] + " </b>");
  	    grdGrades.setHTML(row, 2, "<b> " + data[data.length - 1] + " </b>");
	}
	
	public void setCurrent(String[] exercises){
		Button btn;
		btnPnlCurrent.addButtons(exercises);
		
		for(int i = 0; i < btnPnlCurrent.myButtons.size(); i++){
			btn = btnPnlCurrent.myButtons.get(i);
			btn.addClickHandler(new reviewClickHandler(btn));
		}
	}
	
	public void setReview(String[] exercises){
		Button btn;
		btnPnlReview.addButtons(exercises);
		
		for(int i = 0; i < btnPnlReview.myButtons.size(); i++){
			btn = btnPnlReview.myButtons.get(i);
			btn.addClickHandler(new reviewClickHandler(btn));
		}
	}
	
	private class switchClickHandler implements ClickHandler{

		public void onClick(ClickEvent event) {
			currentSet = !currentSet;
			if(currentSet){
				btnExTypes.setText("Switch to Review");
				btnPnlReview.setVisible(false);
				btnPnlCurrent.setVisible(true);
			} else {
				btnExTypes.setText("Switch to Current");
				btnPnlCurrent.setVisible(false);
				btnPnlReview.setVisible(true);
			}
		}
		
	}
	
	private class reviewClickHandler implements ClickHandler{
		Button btn;

		public reviewClickHandler(Button btn){
			this.btn = btn;
		}
		@Override
		public void onClick(ClickEvent event) {
			parent.reviewExercise(btn.getText());			
		}
		
	}
	
	

}
