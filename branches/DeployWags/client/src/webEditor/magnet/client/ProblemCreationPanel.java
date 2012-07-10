package webEditor.magnet.client;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class ProblemCreationPanel extends AbsolutePanel{
	private TabPanel tabPanel;
	
	private HorizontalPanel horizWrapperPanel = new HorizontalPanel();
	private PickupDragController dragControl;
	
	private VerticalPanel leftPanel = new VerticalPanel();
	private ModifiedCodePanel innerFunctionsMCS;
	private ModifiedCodePanel premadeSegmentsMCS;
	
	private String problemType = "";
	private Button algoButton = new Button("Algorithm");
	private Button basicButton = new Button("Basic");
	private Button advancedButton = new Button("Advanced");
	
	private TextBox titleBox = new TextBox();
	private TextBox descriptionBox = new TextBox();
	private TextArea solutionBox = new TextArea();
	
	private VerticalPanel titleDescriptionSolutionPanel = new VerticalPanel();
	private VerticalPanel titleDescriptionPanel = new VerticalPanel();
	private HorizontalPanel titlePanel = new HorizontalPanel();
	private HorizontalPanel descriptionPanel = new HorizontalPanel();
	private HorizontalPanel solutionPanel = new HorizontalPanel();
	
	private Label innerFunct = new Label("Inner Functions");
	private Label premade = new Label("Premade Segments/Structures");
	private Label dropDowns = new Label("CreationStation Dropdown Menu Choices");
	private VerticalPanel innerFunctPanel = new VerticalPanel();
	private VerticalPanel premadePanel = new VerticalPanel();
	private VerticalPanel dropDownsPanel = new VerticalPanel();
	private MainFunctionPanel mainFunction = new MainFunctionPanel();
	private ModifiedCreationStation innerFunctions;
	private ModifiedCreationStation premadeSegments;
	private MCSOptions conditionOptions = new MCSOptions(Consts.CONDITION_TYPES);
	
	private Button titleDescSolButton = new Button("Title, Description, Solution",new stackButtonHandler());
	private Button titleDescButton = new Button("Title and Description", new stackButtonHandler());         // used for algorithm problems because they have a different solution type
	private Button solutionButton = new Button("Solution", new stackButtonHandler());                      // for algorithm problems solution
	private Button mainFunctionButton = new Button("Set Main Function",new stackButtonHandler());
	private Button innerFunctionsButton = new Button("Set Inner Functions",new stackButtonHandler());
	private Button premadeSegmentsButton = new Button("Set Premade Segments",new stackButtonHandler());
	private Button csOptionsButton = new Button("Set CreationStation Options",new stackButtonHandler());

    private StackPanel rightPanel = new StackPanel();
	
	private HorizontalPanel testCreatePanel = new HorizontalPanel();
	private Button createProblemButton = new Button("Create Problem");
	private Button testProblemButton = new Button("Test Problem");
	
	private boolean firstClick = true;
	
	public ProblemCreationPanel(TabPanel tabPanel, PickupDragController dc){
		this.tabPanel = tabPanel;
		dragControl = dc;

		innerFunctionsMCS =  new ModifiedCodePanel(new StackableContainer("Order your Inner Functions here! " + " {<br /><span id=\"inside_of_block\">" + Consts.TOP + Consts.INSIDE + Consts.BOTTOM + "</span><br />}", dragControl, Consts.MAIN),null, dragControl);
		premadeSegmentsMCS = new ModifiedCodePanel(new StackableContainer("Order your Premade Segments here!" + " {<br /><span id=\"inside_of_block\">" + Consts.TOP + Consts.INSIDE + Consts.BOTTOM + "</span><br />}", dragControl, Consts.MAIN),null, dragControl);
		
		innerFunctions = new ModifiedCreationStation(Consts.INNER_FUNCTIONS_STRUCTURES_LIST, innerFunctionsMCS);
		premadeSegments = new ModifiedCreationStation(Consts.PREMADE_SEGMENTS_STRUCTURES_LIST, premadeSegmentsMCS);
		
		algoButton.addClickHandler(new AlgoHandler());
		basicButton.addClickHandler(new BasicHandler());
		advancedButton.addClickHandler(new AdvancedHandler());

		add(algoButton);
		add(basicButton);
		add(advancedButton);
		
		innerFunctPanel.add(innerFunct);
		innerFunctPanel.add(innerFunctions);
		innerFunctPanel.setWidth("100%");
		premadePanel.add(premade);
		premadePanel.add(premadeSegments);
		premadePanel.setWidth("100%");
		dropDownsPanel.add(dropDowns);
		dropDownsPanel.add(conditionOptions);
		dropDownsPanel.setWidth("100%");
		
		createProblemButton.addClickHandler(new CreateProblemHandler());
		testProblemButton.addClickHandler(new TestProblemHandler());
		
		setHeight("100%");
		leftPanel.setSpacing(10);
	}
	public void clearButtons(){
		clear();
	}
	private void addTitleDescriptionSolution(){
		titlePanel.add(new HTML("Title: "));
		titlePanel.add(titleBox);
		titlePanel.setCellHorizontalAlignment(titleBox, HasHorizontalAlignment.ALIGN_CENTER);
		
		descriptionPanel.add(new HTML("Description: "));
		descriptionPanel.add(descriptionBox);
		titlePanel.setCellHorizontalAlignment(descriptionBox, HasHorizontalAlignment.ALIGN_CENTER);
		
		solutionPanel.add(new HTML("Solution: "));
		solutionPanel.add(solutionBox);
		titlePanel.setCellHorizontalAlignment(solutionBox, HasHorizontalAlignment.ALIGN_CENTER);
		
		titleDescriptionSolutionPanel.add(titlePanel);
		titleDescriptionSolutionPanel.add(descriptionPanel);
		titleDescriptionSolutionPanel.add(solutionPanel);
		titleDescriptionSolutionPanel.setSpacing(20);
	
		leftPanel.add(titleDescriptionSolutionPanel);
	}
	private void addTitleDescription(){
		titlePanel.add(new HTML("Title: "));
		titlePanel.add(titleBox);
		titlePanel.setCellHorizontalAlignment(titleBox, HasHorizontalAlignment.ALIGN_CENTER);
		
		descriptionPanel.add(new HTML("Description: "));
		descriptionPanel.add(descriptionBox);
		titlePanel.setCellHorizontalAlignment(descriptionBox, HasHorizontalAlignment.ALIGN_CENTER);
		
		titleDescriptionPanel.add(titlePanel);
		titleDescriptionPanel.add(descriptionPanel);
		titleDescriptionPanel.setSpacing(20);
		
		leftPanel.add(titleDescriptionPanel);
		
	}
		
	private void addTestCreate(){
		testCreatePanel.add(testProblemButton);
		testCreatePanel.add(createProblemButton);
		testCreatePanel.setWidth("100%");
		testProblemButton.setWidth("100%");
		createProblemButton.setWidth("100%");
		add(testCreatePanel);
	}
	private void test(){
//		for(int i=0;i<tabPanel.getWidgetCount();i++){
//			if(tabPanel.getWidget(i) instanceof EditingPanelUi){
//				tabPanel.remove(i);
//			}
//		}
		EditingPanelUi editingPanel = new EditingPanelUi(tabPanel.getOffsetHeight(),titleBox.getText(), descriptionBox.getText(), new StackableContainer((mainFunction.getBoxContent()+Consts.FUNCTION),dragControl,Consts.MAIN), makeTestInsideSegments(), problemType , makeTestPremadeSegments(getTestPremadeIDs()), Consts.STRUCTURES_LIST, splitConditions(conditionOptions.getAreasText()[0]), splitConditions(conditionOptions.getAreasText()[1]), splitConditions(conditionOptions.getAreasText()[2]), splitConditions(conditionOptions.getAreasText()[3]),getTestSolution(),null,dragControl);
		//Window.alert("before editingpanel is inserted");
		tabPanel.insert(editingPanel,"Editing Panel", 0);
		//Window.alert("before editingpanel is starte");
		editingPanel.start();
		//Window.alert("afeter editingpanel is started");
	}
	public class IDchainAndCount{
		public String idChain;
		public int count;
		public IDchainAndCount(String idChain, int count){
			this.idChain = idChain;
			this.count = count;
		}
		public String getIDChain(){
			return idChain;
		}
		public int getCount(){
			return count;
		}
		public void incrementCount(){
			count++;
		}
		public void append(String s){
			idChain+=s;
		}
	}
	public String getTestSolution(){
		IDchainAndCount idAndCount = new IDchainAndCount("",0);
		for(int i=0;i<premadeSegments.getMainFunction().getInsidePanel().getWidgetCount();i++){
			idAndCount = buildIDTestString((StackableContainer)premadeSegments.getMainFunction().getInsidePanel().getWidget(i),idAndCount);
		}
		return idAndCount.getIDChain();
	}
	public IDchainAndCount buildIDTestString(StackableContainer sc, IDchainAndCount idAndCount) {
		idAndCount.append("{"+idAndCount.getCount());
		idAndCount.incrementCount();
		for (int i = 0; i < sc.getInsidePanel().getWidgetCount(); i++) {
			idAndCount = buildIDTestString((StackableContainer) sc.getInsidePanel().getWidget(i),idAndCount);
		}
		idAndCount.append("}");
		return idAndCount;
	}
	public String getTestPremadeIDs(){
		int count = 0;
		for(int i=0;i<premadeSegments.getMainFunction().getInsidePanel().getWidgetCount();i++){
			count = countWidgets((StackableContainer)premadeSegments.getMainFunction().getInsidePanel().getWidget(i),count);
		}
		
		String testIDs = "";
		for(int i=0;i<count;i++){
			testIDs+=i+",";
		}
		return testIDs;
	}
	 public int countWidgets(StackableContainer sc, int count){
		 count++;
		 for(int i=0;i<sc.getInsidePanel().getWidgetCount();i++){
			count = countWidgets(((StackableContainer)sc.getInsidePanel().getWidget(i)),count);
		 }
		 return count;
		 
	 }
	 public StackableContainer[] makeTestInsideSegments(){
		 return null;
	 }
		public class PremadeArrayAndIndex{
			public StackableContainer[] premadeSegmentsArr;
			public int index;
			
			public String[] idArr;
			public int idIndex;
			public PremadeArrayAndIndex(StackableContainer[] premadeSegmentsArr, int index, String[] idArr, int idIndex){
				this.premadeSegmentsArr = premadeSegmentsArr;
				this.index = index;
				this.idArr = idArr;
				this.idIndex = idIndex;
			}
			public StackableContainer[] getPremadeSegmentsArr(){
				return premadeSegmentsArr;
			}
			public int getIndex(){
				return index;
			}
			public void add(StackableContainer sc){
				premadeSegmentsArr[index] = sc;
				//sc.setID(idArr[idIndex]);
				idIndex++;
				index++;
			}
		}
	 public StackableContainer[] makeTestPremadeSegments(String idsAsString){
		 String[] idArr = idsAsString.split(",");
		 PremadeArrayAndIndex ArrAndIndex = new PremadeArrayAndIndex(new StackableContainer[getTestPremadeIDs().split(",").length], 0, idArr, 0);
		 for(int i=0;i<premadeSegments.getMainFunction().getInsidePanel().getWidgetCount();i++){
				ArrAndIndex = addTestPremadeSegment((StackableContainer)premadeSegments.getMainFunction().getInsidePanel().getWidget(i),ArrAndIndex);
			}
		 return ArrAndIndex.getPremadeSegmentsArr();
	 }
	 public PremadeArrayAndIndex  addTestPremadeSegment(StackableContainer sc, PremadeArrayAndIndex ArrAndIndex) {
			ArrAndIndex.add(new StackableContainer(sc.getContent(),dragControl));
			for (int i = 0; i < sc.getInsidePanel().getWidgetCount(); i++) {
				ArrAndIndex = addTestPremadeSegment((StackableContainer)sc.getInsidePanel().getWidget(i), ArrAndIndex);
			}
			return ArrAndIndex;
		}
	public StackableContainer[] makeInsideSegments(){
		String functionsList = innerFunctions.getDBString();
		String[] splitList = functionsList.split(".:colon:.");
		StackableContainer[] containers = new StackableContainer[splitList.length];
		for(int i=0;i<splitList.length;i++){
			containers[i] = new StackableContainer(splitList[i],dragControl,Consts.NONDRAGGABLE);
		}
		return containers;
	}
			
	public StackableContainer[] makePremadeSegments(){
		String functionsList = premadeSegments.getDBString();
		String[] splitList = functionsList.split(".:colon:.");
		StackableContainer[] containers = new StackableContainer[splitList.length];
		for(int i=0;i<splitList.length;i++){
			containers[i] = new StackableContainer(splitList[i],dragControl);
		}
		return containers;
	}
	
	public String[] splitConditions(String conditionList){
		String[] splitList = conditionList.split(".:colon:.");
		String[] conditions = new String[splitList.length-1];
		for(int i=1;i<splitList.length;i++){
			conditions[i-1] = splitList[i];
		}
		return conditions;
	}
	
	
	 private class AlgoHandler implements ClickHandler {
	        public void onClick(ClickEvent event) {
	        	problemType = Consts.ALGORITHM_PROBLEM;
	        	clearButtons();
	        	addTitleDescription();
	        	leftPanel.add(titleDescButton);
	        	titleDescButton.setWidth("100%");	
	        	leftPanel.add(solutionButton);
	        	solutionButton.setWidth("100%");
	        	leftPanel.add(mainFunctionButton);
	        	mainFunctionButton.setWidth("100%");
	        	horizWrapperPanel.add(leftPanel);
	        	
	        	rightPanel.add(titleDescriptionPanel,"Title and Description");
	        	rightPanel.add(premadeSegments, "Solution"); 
	        	rightPanel.add(mainFunction,"Main Function");
	        	horizWrapperPanel.add(rightPanel);
	        	rightPanel.setWidth("100%");
	        	
	        	add(horizWrapperPanel);
	        	addTestCreate();
	        	setupHorizWrapper();
	        }
	 }
	 private class BasicHandler implements ClickHandler {
	        public void onClick(ClickEvent event) {
	        	problemType = Consts.BASIC_PROBLEM;
	        	clearButtons();
	        	addTitleDescriptionSolution();
	        	leftPanel.add(titleDescSolButton);
	        	titleDescSolButton.setWidth("100%");
	        	leftPanel.add(mainFunctionButton);
	        	mainFunctionButton.setWidth("100%");
	        	leftPanel.add(innerFunctionsButton);
	        	innerFunctionsButton.setWidth("100%");
	        	leftPanel.add(premadeSegmentsButton);
	        	premadeSegmentsButton.setWidth("100%");
	        	horizWrapperPanel.add(leftPanel);
	        	
	        	rightPanel.add(titleDescriptionSolutionPanel,"Title, Description,Solution");
	        	rightPanel.add(mainFunction,"Main Function");
	        	rightPanel.add(innerFunctions, "Inner Functions");
	        	rightPanel.add(premadeSegments, "Premade Segments");
	        	horizWrapperPanel.add(rightPanel);
	        	rightPanel.setWidth("100%");
	        	
	        	add(horizWrapperPanel);
	        	addTestCreate();
	        	setupHorizWrapper();
	        }
	 }
	 private class AdvancedHandler implements ClickHandler {
	        public void onClick(ClickEvent event) {
	        	problemType = Consts.ADVANCED_PROBLEM;
	        	clearButtons();
	        	addTitleDescriptionSolution();
	        	clearButtons();
	        	leftPanel.add(titleDescSolButton);
	        	titleDescSolButton.setWidth("100%");
	        	leftPanel.add(mainFunctionButton);
	        	mainFunctionButton.setWidth("100%");
	        	leftPanel.add(innerFunctionsButton);
	        	innerFunctionsButton.setWidth("100%");
	        	leftPanel.add(premadeSegmentsButton);
	        	premadeSegmentsButton.setWidth("100%");
	        	leftPanel.add(csOptionsButton);
	        	csOptionsButton.setWidth("100%");
	        	horizWrapperPanel.add(leftPanel);
	        	
	        	rightPanel.add(titleDescriptionSolutionPanel,"Title, Description, Solution");
	        	rightPanel.add(mainFunction,"Main Function");
	        	rightPanel.add(innerFunctions, "Inner Functions");
	        	rightPanel.add(premadeSegments, "Premade Segments");
	        	rightPanel.add(conditionOptions,"CreationStation Options");
	        	horizWrapperPanel.add(rightPanel);
	        	rightPanel.setWidth("100%");
	        	add(horizWrapperPanel);
	        	
	        	addTestCreate();
	        	setupHorizWrapper();
	        }
	 }
	 private void setupHorizWrapper(){
		horizWrapperPanel.setWidth("100%");
		horizWrapperPanel.setCellWidth(leftPanel, "250px");
	 }
	 private class TestProblemHandler implements ClickHandler {
	        public void onClick(ClickEvent event) {
	        	test();
	        }
	 }
	 private class CreateProblemHandler implements ClickHandler {
	        public void onClick(ClickEvent event) {
	        	String title = titleBox.getText();
	        	String completeURL = "http://refrigerator.redirectme.net/New.php";	
				RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, completeURL);
				
				try {
					builder.setHeader("Content-Type", "application/x-www-form-urlencoded");
					builder.sendRequest("title="+title, new RequestCallback() {
						@Override
						public void onResponseReceived(Request request, Response response) {
							JSONValue vals = JSONParser.parseStrict(response.getText());
							JSONObject obj = vals.isObject();
							
							if(obj!=null){
								String id = obj.get("id").isString().stringValue();
								update(Integer.parseInt(id),"type",problemType);
								update(Integer.parseInt(id),"directions",descriptionBox.getText());
								if(problemType.equals(Consts.ADVANCED_PROBLEM))
									update(Integer.parseInt(id),"creationStation","1");
								else
									update(Integer.parseInt(id),"creationStation","-1");
								update(Integer.parseInt(id),"mainFunction",mainFunction.getBoxContent());
								update(Integer.parseInt(id),"innerFunctions",encodeStringArray(innerFunctions.getDBString().split(".:colon:.")));
								update(Integer.parseInt(id),"forLeft",encodeStringArray(splitConditions(conditionOptions.getAreasText()[0])));
								update(Integer.parseInt(id),"forMid",encodeStringArray(splitConditions(conditionOptions.getAreasText()[1])));
								update(Integer.parseInt(id),"forRight",encodeStringArray(splitConditions(conditionOptions.getAreasText()[2])));
								update(Integer.parseInt(id),"booleans",encodeStringArray(splitConditions(conditionOptions.getAreasText()[3])));
							//	  Window.alert("before segments");
								update(Integer.parseInt(id),"statements",encodeStringArray(premadeSegments.getPremadeDBString().split(".:colon:.")));
								//  Window.alert("after segments");
								if(problemType.equals(Consts.ALGORITHM_PROBLEM))
									update(Integer.parseInt(id),"solution",getAlgoSolution());

							}
						}
						public String encodeStringArray(String[] arr){
							String encoded ="";
							for(String s:arr)
								encoded+=s+".:|:.";
							return encoded;
						}
						
						@Override
						public void onError(Request request, Throwable exception) {
						}
					});
				} catch (RequestException e) {
					e.printStackTrace();
				}
				if(firstClick){
					firstClick=false;
					onClick(event);
				}
	        }
	 }
		public String getAlgoSolution(){
			String idChain = "";
			for(int i=0;i<premadeSegments.getMainFunction().getInsidePanel().getWidgetCount();i++){
				idChain = buildIDString((StackableContainer)premadeSegments.getMainFunction().getInsidePanel().getWidget(i),idChain);
			}
			return idChain;
		}
		public String buildIDString(StackableContainer sc, String idChain) {
			idChain+="{";
			//idChain+=sc.getID();
			for (int i = 0; i < sc.getInsidePanel().getWidgetCount(); i++) {
				idChain=buildIDString((StackableContainer) sc.getInsidePanel().getWidget(i),idChain);
			}
			idChain+="}";
			return idChain;
		}
	 private class stackButtonHandler implements ClickHandler{
		 public void onClick(ClickEvent event) {
			  String source = ((Button)event.getSource()).getText();
			  
			  if(source.equals("Title, Description, Solution"))
				  rightPanel.showStack(rightPanel.getWidgetIndex(titleDescriptionSolutionPanel));
			  if(source.equals("Title and Description"))
				  rightPanel.showStack(rightPanel.getWidgetIndex(titleDescriptionPanel));
			  if(source.equals("Set Main Function"))
				  rightPanel.showStack(rightPanel.getWidgetIndex(mainFunction));
			  if(source.equals("Set Inner Functions"))
				  rightPanel.showStack(rightPanel.getWidgetIndex(innerFunctions));
			  if(source.equals("Set Premade Segments"))
				  rightPanel.showStack(rightPanel.getWidgetIndex(premadeSegments));
			  if(source.equals("Solution"))
				  rightPanel.showStack(rightPanel.getWidgetIndex(premadeSegments));
			  if(source.equals("Set CreationStation Options"))
				  rightPanel.showStack(rightPanel.getWidgetIndex(conditionOptions));
		 }
		 
	 }
	 
	 public void update(int id, String column, String data){
		 String completeURL = "http://refrigerator.redirectme.net/New.php";
			RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, completeURL);
			try {
				builder.setHeader("Content-Type", "application/x-www-form-urlencoded");
				
				if(column.equals("statements")){
					@SuppressWarnings("unused")
					Request req = builder.sendRequest(("id=" + id + "&column=" + column + "&data=" + data).replaceAll("\\+", "%2B"), new RequestCallback() {
						@Override
						public void onResponseReceived(Request request, Response response) {
							JSONValue vals = JSONParser.parseStrict(response.getText());
							String containerIDs = vals.isString().stringValue();
							//Window.alert(containerIDs);
							setContainerIDs(containerIDs);
						}
						@Override
						public void onError(Request request, Throwable exception) {					
						}
					});
				}else{
				@SuppressWarnings("unused")
				Request req = builder.sendRequest(("id=" + id + "&column=" + column + "&data=" + data).replaceAll("\\+", "%2B"), new RequestCallback() {
					@Override
					public void onResponseReceived(Request request, Response response) {
						JSONValue vals = JSONParser.parseStrict(response.getText());
					}
					@Override
					public void onError(Request request, Throwable exception) {
					}
				});
				}
			} catch (RequestException e){
				e.printStackTrace();
			} 
	 }
	 public void setContainerIDs(String idsAsString){
		 String[] idArr = idsAsString.split(",");
		 int index =0;
		 for(int i=0;i<premadeSegments.getMainFunction().getInsidePanel().getWidgetCount();i++){
			 index = assignContainerIDs(((StackableContainer)premadeSegments.getMainFunction().getInsidePanel().getWidget(i)),index,idArr);
		 }
	 }
	 public int assignContainerIDs(StackableContainer sc, int index, String[] idArr){
		 //sc.setID(idArr[index]);
		// Window.alert(sc.getID());
		 index++;
		 for(int i=0;i<sc.getInsidePanel().getWidgetCount();i++){
			 index = assignContainerIDs(((StackableContainer)sc.getInsidePanel().getWidget(i)),index,idArr);
		 }
		 return index;
		 
	 }
}
