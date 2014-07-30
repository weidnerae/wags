package webEditor.admin;

import webEditor.Proxy;
import webEditor.ProxyFacilitator;
import webEditor.WEStatus;
import webEditor.admin.builders.BasicDisplay;
import webEditor.admin.builders.LMBuildBSTDisplay;
import webEditor.admin.builders.LMBuildBTDisplay;
import webEditor.admin.builders.LMBuilder;
import webEditor.admin.builders.LMBuilderFactory;
import webEditor.admin.builders.LMGraphsDisplay;
import webEditor.admin.builders.LMInsertNodeDisplay;
import webEditor.admin.builders.LMSimplePartitionDisplay;
import webEditor.admin.builders.LMTraversalDisplay;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class LMEditTab extends Composite implements ProxyFacilitator{

	private static LMEditTabUiBinder uiBinder = GWT
			.create(LMEditTabUiBinder.class);

	interface LMEditTabUiBinder extends UiBinder<Widget, LMEditTab> {
	}

	@UiField Panel vtDisplayHolder;
	@UiField ListBox listBox;

	public LMEditTab() {
		initWidget(uiBinder.createAndBindUi(this));
		//Sets the default graph to traversals
		BasicDisplay display = new LMTraversalDisplay();
		LMBuilder builder = LMBuilderFactory.getTraversalBuilder();
		vtDisplayHolder.addStyleName("center");
		display.load(vtDisplayHolder, builder);
		Proxy.getLMSubjects(this);
		//Add all items to the list box
		listBox.addItem("Traversals");
		listBox.addItem("Insert Node");
		listBox.addItem("Build BST");
		listBox.addItem("Build BT");
		listBox.addItem("SwagsTestGroup");
		listBox.addItem("Kruskal");
		listBox.addItem("Prims");
		listBox.addItem("Linear");
		listBox.addItem("Quadratic");
		listBox.addItem("Insert");
		listBox.addItem("Delete");
		listBox.addItem("Build");
		listBox.addItem("Heap Sort");
		listBox.addItem("Radix Sort");
		listBox.addItem("Quick Sort");
		//On change it finds which item was selected and loads the graph
		 listBox.addChangeHandler(new ChangeHandler()
		 {
		  public void onChange(ChangeEvent event)
		  {
			  int selectedIndex = listBox.getSelectedIndex();
			  if (selectedIndex > -1) 
			  {
				  BasicDisplay display;
				  LMBuilder builder;
				  
				  switch( listBox.getValue(selectedIndex)) {
				  case "Traversals":
					  display = new LMTraversalDisplay();
					  builder = LMBuilderFactory.getTraversalBuilder();
				  	  break;
				  case "Insert Node":
					  display = new LMInsertNodeDisplay();
					  builder = LMBuilderFactory.getInsertNodeBuilder();
				  	  break;
				  case "Kruskal":
					  display = new LMGraphsDisplay(false);
					  builder = LMBuilderFactory.getGraphsBuilder();
				  	  break;
				  case "Prims":
					  display = new LMGraphsDisplay(true);
					  builder = LMBuilderFactory.getGraphsBuilder();
				  	  break;
				  case "Quick Sort":
					  display = new LMSimplePartitionDisplay();
					  builder = LMBuilderFactory.getSimplePartitionBuilder();
				  	  break;
				  case "Build BST":
					  display = new LMBuildBSTDisplay();
					  builder = LMBuilderFactory.getBuildBSTBuilder();
				  	  break;
				  case "Build BT":
					  display = new LMBuildBTDisplay();
					  builder = LMBuilderFactory.getBuildBTBuilder();
				  	  break;
				  default :
					  Window.alert("Not yet implemented");
					  display = null;
					  builder = null; 
			      }
				  display.load(vtDisplayHolder, builder);
		      }
		  }
		 });
		 
	}

	public void handleExercises(String[] exercises) {}
	public void setExercises(String[] exercises) {}
	public void setCallback(String[] exercises, WEStatus status) {}
	public void getCallback(String[] exercises, WEStatus status, String request) {}
	public void reviewExercise(String exercise) {}
	public void reviewCallback(String[] data) {}
	public void handleSubjects(String[] subjects) {}
	public void handleGroups(String[] groups) {}

}
