package webEditor.admin.builders;


import webEditor.admin.LMDisplay;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LMTraversalDisplay extends LMDisplay {

	private static LMTraversalDisplayUiBinder uiBinder = GWT
			.create(LMTraversalDisplayUiBinder.class);

	interface LMTraversalDisplayUiBinder extends
			UiBinder<Widget, LMTraversalDisplay> {
	}

	LMBuilder builder;
	@UiField VerticalPanel basePanel;
	@UiField BasicBuilder canvas;
	@UiField Button btnAddNode;
	@UiField TextBox txtAddNode;
	
	public LMTraversalDisplay() {
		initWidget(uiBinder.createAndBindUi(this));
		
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
	
	public void setBuilder(LMBuilder builder){
		this.builder = builder;
	}
	

}
