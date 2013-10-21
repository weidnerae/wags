package webEditor.admin;

import webEditor.Proxy;
import webEditor.Reviewer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class created the review panel which shows up on the admin page as well as other sections of the website.
 * The information in the review panel will differer depending on its location in the website as well as the privileges
 * of the user viewing. 
 */

public class ReviewPanel extends Composite {

	private static ReviewPanelUiBinder uiBinder = GWT
			.create(ReviewPanelUiBinder.class);

	interface ReviewPanelUiBinder extends UiBinder<Widget, ReviewPanel> {
	}
	
	@UiField Button btnExTypes;
	@UiField ManyButtonPanel btnPnlCurrent, btnPnlReview, btnPnlStudent;
	@UiField Grid grdGrades;
	@UiField Label title;
	@UiField Button removeStudentButton;
	
	Reviewer parent;
	boolean currentSet = true;  // keeps track of "Assign/Review" state
    private Button selectedUser = new Button();
    
	public ReviewPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		btnPnlCurrent.title.setVisible(false);
		btnPnlReview.title.setVisible(false);
		btnPnlReview.setVisible(false);
		removeStudentButton.setVisible(false);
		btnExTypes.addClickHandler(new switchClickHandler());
	}
	
	
	/**
	 * Overloaded method so that you can create a review panel for review based on student
	 * @param studentReview
	 */

	public ReviewPanel(boolean studentReview) {
		initWidget(uiBinder.createAndBindUi( this ));
		if (studentReview) {
			btnExTypes.setVisible(false); //make current/review button invisible
			btnPnlReview.setVisible( false ); //we will not use the review panel
			btnPnlCurrent.setVisible( false ); //or the current panel
			//make the labels invisible as well
			btnPnlCurrent.title.setVisible(false);
			btnPnlReview.title.setVisible(false);
			removeStudentButton.setVisible(false);
			removeStudentButton.addClickHandler(new removeStudentClickHandler());
			
			//finally, make student button panel visible
			btnPnlStudent.setVisible( true );
		} else {
			//do what the normal constructor would do
			btnPnlCurrent.title.setVisible(false);
			btnPnlReview.title.setVisible(false);
			btnPnlReview.setVisible(false);
			btnExTypes.addClickHandler(new switchClickHandler());
		}
	}

	
	public void setParent(Reviewer parent){
		this.parent = parent;
	}
	
	public void setTitle(String title){
		this.title.setText(title);
	}
	

	public void fillGrid(String[] data, boolean studentReview) {
		if (studentReview) {
			fillGrid(data);
			
			//adjust the titles to reflect student based review
	  		grdGrades.setHTML(0, 0, "<b> Excercise </b>");
	  		grdGrades.setHTML(0, 1, "<b> NumAttempts </b>");
	  		grdGrades.setHTML(0, 2, "<b> Correct </b>");

		} else {
			fillGrid(data);
		}
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

	/**
	 * Adds a button to the list on the left side of the screen. There is a button corresonding to
	 * each student in the current admin's section
	 * 
	 * @param students A list of student names to put on the buttons
	 */
	public void setStudents(String[] students){
		Button btn;
		btnPnlStudent.addButtons(students);
		
		for(int i = 0; i < btnPnlStudent.myButtons.size(); i++){
			btn = btnPnlStudent.myButtons.get(i);
			//change style because usernames tend to be longer
			btn.setStyleName("student_button");
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
			//clear all button panels of blue markings
			btnPnlCurrent.colorBlack();
			btnPnlReview.colorBlack();
			btnPnlStudent.colorBlack();
			//apply blue to clicked button
			btn.getElement().getStyle().setColor("blue");
			selectedUser = btn;
			parent.review(btn.getText());			
			removeStudentButton.setVisible(true);
		}
		
	}
	
	/**
	 * Handles removing a student from the database with the remove student
	 * button. 
	 */
	private class removeStudentClickHandler implements ClickHandler{
		
		/**
		 * Once the "Remove Student" button is clicked, the user is presented with a dialog box with which
		 * they must verify the removal of the student form their section. If the admin chooses yet then
		 * the user will be removed from their section and their corresponding button will be removed from 
		 * the list on the left-hand side of the screen
		 */
        @Override
		public void onClick(ClickEvent event) {
			final DialogBox deleteBox = new DialogBox(false);
			Label deleteLbl = new Label("Delete User: " + selectedUser.getText());
			Button yes = new Button("YES");
			Button no = new Button("NO");
			
			VerticalPanel base = new VerticalPanel();
			base.setWidth("100%");
			HorizontalPanel buttonPanel = new HorizontalPanel();
			buttonPanel.setWidth("100%");
			buttonPanel.setHeight("22px");
			
			buttonPanel.add(yes);
			buttonPanel.add(no);
			yes.setHeight("20px");
			yes.setWidth("100%");
			no.setHeight("20px");
			no.setWidth("100%");
			base.add(deleteLbl);
			base.add(buttonPanel);
			deleteBox.add(base);
			
			yes.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Proxy.RemoveUserFromSection(selectedUser.getText());
					selectedUser.removeFromParent();
					selectedUser = null;
					deleteBox.hide();
				}	
			});
			
			no.addClickHandler(new ClickHandler() {	
				public void onClick(ClickEvent event) {
					deleteBox.hide();
				}
			});
			
			deleteBox.center();
		}
			
		
	}
}
