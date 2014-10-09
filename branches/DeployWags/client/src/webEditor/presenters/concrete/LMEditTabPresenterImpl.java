package webEditor.presenters.concrete;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;

import webEditor.admin.builders.BasicDisplay;
import webEditor.admin.builders.LMBuildBSTDisplay;
import webEditor.admin.builders.LMBuildBTDisplay;
import webEditor.admin.builders.LMBuilder;
import webEditor.admin.builders.LMBuilderFactory;
import webEditor.admin.builders.LMGraphsDisplay;
import webEditor.admin.builders.LMInsertNodeDisplay;
import webEditor.admin.builders.LMSimplePartitionDisplay;
import webEditor.admin.builders.LMTraversalDisplay;
import webEditor.presenters.interfaces.LMEditTabPresenter;
import webEditor.views.concrete.LMEditTab;

public class LMEditTabPresenterImpl implements LMEditTabPresenter{

	private LMEditTab lmedittab;
	private boolean bound = false;
	
	public LMEditTabPresenterImpl(LMEditTab view) {
		this.lmedittab = view;
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(lmedittab.asWidget());
	}

	@Override
	public void bind() {
		lmedittab.setPresenter(this);
		bound = true;
	}

	@Override
	public boolean bound() {
		return bound;
	}

	@Override
	public void update(List<String> data) {
		// TODO Auto-generated method stub
		
	}	

	public void assignGrpBtnClickHandler(Button button){
		if(button.getText().equals("Traversals")){
			button.addClickHandler(new checkClickHandler(
			new LMTraversalDisplay(), LMBuilderFactory.getTraversalBuilder()));
		} else if (button.getText().equals("Insert Node")){
			button.addClickHandler(new checkClickHandler(
			new LMInsertNodeDisplay(), LMBuilderFactory.getInsertNodeBuilder()));
		} else if (button.getText().equals("Kruskal")){
			button.addClickHandler(new checkClickHandler(
			new LMGraphsDisplay(false), LMBuilderFactory.getGraphsBuilder()));
		} else if (button.getText().equals("Prim")){
			button.addClickHandler(new checkClickHandler(
			new LMGraphsDisplay(true), LMBuilderFactory.getGraphsBuilder()));
		} else if (button.getText().equals("Simple Partition")){
			button.addClickHandler(new checkClickHandler(
			new LMSimplePartitionDisplay(), LMBuilderFactory.getSimplePartitionBuilder()));
		} else if (button.getText().equals("Build BST")){
			button.addClickHandler(new checkClickHandler(
			new LMBuildBSTDisplay(), LMBuilderFactory.getBuildBSTBuilder()));
		} else if (button.getText().equals("Build BT")) {
			button.addClickHandler(new checkClickHandler(
					new LMBuildBTDisplay(), LMBuilderFactory.getBuildBTBuilder()));
		} else {
			button.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Window.alert("Not implemented yet!");
				}
			});
		}
	}

	private class checkClickHandler implements ClickHandler{
		BasicDisplay display;
		LMBuilder builder;
		
		public checkClickHandler(BasicDisplay display, LMBuilder builder){
			this.display = display;
			this.builder = builder;
		}
		
		public void onClick(ClickEvent event){
			display.load(lmedittab.getvtDisplayHolder(), builder);
		}
	}

}
