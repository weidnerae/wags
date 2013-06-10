package webEditor.admin.builders;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * BasicDisplay
 * @author pmeznar
 *
 * This is the class you extend for Logical Problem creation.
 * This class automatically sets up the BasicCanvas for you, along with
 * add/delete node capability, a "calculate results" and "reset" button,
 * a "title" entry and an "instructions" entry.
 * 
 * You'll be necessary for adding additional widgets in the "construct"
 * abstract method, which will get called only once.  Use "construct" to
 * add to BasicDisplay.
 * 
 * The "calculate" abstract method gets called whenever the "Calculate 
 * Results" button is clicked, but you have to implement it yourself.  The 
 * "Reset" button will clear the title, instructions, and the canvas, but
 * you'll have to add any additional "reset" functionality in the "clear" 
 * method, which "Reset" calls.
 * 
 * "OnModify" is there in case you need to change some widgets if the canvas
 * changes.
 * 
 * "Fill Builder" is used to get the arguments from some other widget, or for
 * you to construct the arguments yourself.  BasicDisplay does not have a way
 * of calling this, you'll have to add that functionality.
 * 
 * You can look at LMTraversalDisplay as an example
 * 
 * Also, of course you can override any of the basic implementations.  For
 * example, LMInsertNodeDisplay will override the adding node logic
 */
public abstract class BasicDisplay extends Composite {

	private static BasicDisplayUiBinder uiBinder = GWT
			.create(BasicDisplayUiBinder.class);

	interface BasicDisplayUiBinder extends UiBinder<Widget, BasicDisplay> {
	}
	
	@UiField VerticalPanel basePanel;
	@UiField BasicCanvas canvas;
	@UiField Button btnAddNode, btnDeleteNode, btnCalculate;
	@UiField TextBox txtAddNode, txtTitle;
	@UiField TextArea txtDesc;
	private boolean built = false;
	LMBuilder builder;
	
	public BasicDisplay() {
		initWidget(uiBinder.createAndBindUi(this));
		addNodeHandling();
		deleteNodeHandling();
		canvas.setParent(this);
	}
	
	public void load(VerticalPanel panel, LMBuilder builder){
		this.builder = builder;
		panel.clear();
		if(!built){
			canvas.setParent(this);
			this.construct();
			built = true;
		}
		panel.add(this);
	}
	
	private void addNodeHandling(){
		// Add nodes
		btnAddNode.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String val = txtAddNode.getText();
				if(val.length() > 0){
					canvas.addNode(txtAddNode.getText());
					txtAddNode.setText("");
				}
			}
		});		
	}
	
	private void deleteNodeHandling(){
		btnDeleteNode.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				canvas.deleteNode(txtAddNode.getText());
			}
		});
	}
	
	@UiHandler("txtAddNode")
	void onEnterPress(KeyPressEvent event){
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)
		{
			btnAddNode.click();
		}
	}
	
	@UiHandler("btnReset")
	void onResetClick(ClickEvent event){
		canvas.clear();
		txtTitle.setText("");
		txtDesc.setText("");
		
		clear();
	}
	
	@UiHandler("btnCalculate")
	void onCalculateClick(ClickEvent event){
		this.calculate();
	}
	
	public abstract void construct();
	public abstract void calculate();
	public abstract void fillBuilder(ArgHolder child);
	public abstract void onModify();
	public abstract void clear();

}
