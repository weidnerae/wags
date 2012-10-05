package webEditor.magnet.client;

import webEditor.client.Proxy;
import webEditor.client.WEStatus;
import webEditor.client.view.Notification;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Widget;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
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
	private ModifiedCodePanel algoSolutionMCS;
	
	private String problemType = "";
	private Button algoButton = new Button("Algorithm");
	private Button basicButton = new Button("Basic");
	private Button advancedButton = new Button("Advanced");
	
	private TextBox titleBox = new TextBox();
	private TextBox descriptionBox = new TextBox();
	private TextArea solutionBox = new TextArea();
	
	private VerticalPanel titleDescriptionSolutionPanel = new VerticalPanel();
	private VerticalPanel titleDescriptionPanel = new VerticalPanel();
	private FormPanel titleDescriptionSolutionFormPanel = new FormPanel();
	private FormPanel titleDescriptionFormPanel = new FormPanel();
	private HorizontalPanel titlePanel = new HorizontalPanel();
	private HorizontalPanel descriptionPanel = new HorizontalPanel();
	
	
	private Label innerFunct = new Label("Inner Functions");
	private Label premade = new Label("Premade Segments/Structures");
	private Label solutionLabel = new Label("Solution");
	private Label dropDowns = new Label("CreationStation Dropdown Menu Choices");
	private VerticalPanel innerFunctPanel = new VerticalPanel();
	private VerticalPanel premadePanel = new VerticalPanel();
	private VerticalPanel dropDownsPanel = new VerticalPanel();
	private VerticalPanel solutionPanel = new VerticalPanel();
	private MainFunctionPanel mainFunction = new MainFunctionPanel();
	private ModifiedCreationStation innerFunctions;
	private ModifiedCreationStation premadeSegments;
	private ModifiedCreationStation algoSolution;
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
	
	private SubmitButton invisibleSubmitButton = new SubmitButton("submit");
	
	private TextArea invisibleProblemType = new TextArea();
	private TextArea invisiblePremadeSegments = new TextArea();
	private TextArea invisibleInnerSegments = new TextArea();
	private TextArea invisibleFor1 = new TextArea();
	private TextArea invisibleFor2 = new TextArea();
	private TextArea invisibleFor3 = new TextArea();
	private TextArea invisibleBoolean = new TextArea();
	private TextBox invisibleAlgoSolution = new TextBox();
	private FileUpload solutionUpload = new FileUpload();
	private FileUpload testClassUpload = new FileUpload();
	
	private boolean firstClick = true;
	
	public ProblemCreationPanel(RefrigeratorMagnet magnet){
		
		this.tabPanel = magnet.tabPanel;
		
		solutionUpload.setName("solutionUpload");
		testClassUpload.setName("testClassUpload");
		titleBox.setName("title");
		descriptionBox.setName("description");
		
		invisibleProblemType.setStyleName("invisible");
		invisibleProblemType.setName("problemType");
		invisiblePremadeSegments.setStyleName("invisible");
		invisiblePremadeSegments.setName("premadeSegments");
		invisibleInnerSegments.setStyleName("invisible");
		invisibleInnerSegments.setName("innerSegments");
		invisibleFor1.setStyleName("invisible");
		invisibleFor1.setName("for1");
		invisibleFor2.setStyleName("invisible");
		invisibleFor2.setName("for2");
		invisibleFor3.setStyleName("invisible");
		invisibleFor3.setName("for3");
		invisibleBoolean.setStyleName("invisible");
		invisibleBoolean.setName("boolean");
		invisibleAlgoSolution.setStyleName("invisible");
		invisibleAlgoSolution.setName("algoSolution");
		invisibleSubmitButton.setStyleName("invisible");
		invisibleSubmitButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("CLICKED");
				titleDescriptionFormPanel.submit();
			}
		});
		
	//	dragControl = magnet.dc;
		dragControl = new PickupDragController(RootPanel.get(), false);
		dragControl.setBehaviorDragProxy(true);

		innerFunctionsMCS =  new ModifiedCodePanel(new StackableContainer("Order your Inner Functions here! " + " {<br /><span id=\"inside_of_block\">" + Consts.TOP + Consts.INSIDE + Consts.BOTTOM + "</span><br />}", dragControl, Consts.MAIN),null, dragControl);
		premadeSegmentsMCS = new ModifiedCodePanel(new StackableContainer("Order your Premade Segments here!" + " {<br /><span id=\"inside_of_block\">" + Consts.TOP + Consts.INSIDE + Consts.BOTTOM + "</span><br />}", dragControl, Consts.MAIN),null, dragControl);
		algoSolutionMCS = new ModifiedCodePanel(new StackableContainer("Create your solution here!" + " {<br /><span id=\"inside_of_block\">" + Consts.TOP + Consts.INSIDE + Consts.BOTTOM + "</span><br />}", dragControl, Consts.MAIN),null, dragControl);
		
		innerFunctions = new ModifiedCreationStation(Consts.INNER_FUNCTIONS_STRUCTURES_LIST, innerFunctionsMCS);
		premadeSegments = new ModifiedCreationStation(Consts.PREMADE_SEGMENTS_STRUCTURES_LIST, premadeSegmentsMCS);
		algoSolution = new ModifiedCreationStation(Consts.ALGORITHM_SOLUTIONS_STRUCTURES_LIST, algoSolutionMCS);
		
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
		solutionPanel.add(solutionLabel);
		solutionPanel.add(algoSolution);
		solutionPanel.setWidth("100%");
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
		
		titleDescriptionSolutionPanel.add(titlePanel);
		titleDescriptionSolutionPanel.add(descriptionPanel);
		titleDescriptionSolutionPanel.add(solutionUpload);
		titleDescriptionSolutionPanel.add(testClassUpload);
		titleDescriptionSolutionPanel.add(invisiblePremadeSegments);
		titleDescriptionSolutionPanel.add(invisibleInnerSegments);
		titleDescriptionSolutionPanel.add(invisibleFor1);
		titleDescriptionSolutionPanel.add(invisibleFor2);
		titleDescriptionSolutionPanel.add(invisibleFor3);
		titleDescriptionSolutionPanel.add(invisibleBoolean);
		titleDescriptionSolutionPanel.add(invisibleSubmitButton);
		
		titleDescriptionSolutionFormPanel.setAction("cs.appstate.edu/wags/Test_Version/server.php?cmd=AddMagnetExercise");
		titleDescriptionSolutionFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		titleDescriptionSolutionFormPanel.setMethod(FormPanel.METHOD_POST);
		titleDescriptionSolutionFormPanel.addSubmitCompleteHandler(new SubmitCompleteHandler(){
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				WEStatus stat = new WEStatus(event.getResults());
				
				Notification.notify(stat.getStat(), stat.getMessage());	
			}
		});
		
		titleDescriptionSolutionFormPanel.add(titleDescriptionSolutionPanel);
		leftPanel.add(titleDescriptionSolutionFormPanel);
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

		titleDescriptionPanel.add(titlePanel);
		titleDescriptionPanel.add(descriptionPanel);
		titleDescriptionPanel.add(solutionUpload);
		titleDescriptionPanel.add(testClassUpload);
		titleDescriptionPanel.add(invisiblePremadeSegments);
		titleDescriptionPanel.add(invisibleInnerSegments);
		titleDescriptionPanel.add(invisibleFor1);
		titleDescriptionPanel.add(invisibleFor2);
		titleDescriptionPanel.add(invisibleFor3);
		titleDescriptionPanel.add(invisibleBoolean);
		
		titleDescriptionFormPanel.setAction(Proxy.getBaseURL() + "?cmd=AddMagnetExercise");
		titleDescriptionFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		titleDescriptionFormPanel.setMethod(FormPanel.METHOD_POST);
		titleDescriptionFormPanel.addSubmitCompleteHandler(new SubmitCompleteHandler(){
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				WEStatus stat = new WEStatus(event.getResults());			
				Notification.notify(stat.getStat(), stat.getMessage());
			}
		});
		
		titleDescriptionFormPanel.add(titleDescriptionPanel);
		titleDescriptionFormPanel.add(invisibleSubmitButton);
		leftPanel.add(titleDescriptionFormPanel);
		
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
		EditingPanelUi editingPanel = new EditingPanelUi(new RefrigeratorMagnet(titleBox.getText(), descriptionBox.getText(), new StackableContainer((mainFunction.getBoxContent()+Consts.FUNCTION),dragControl,Consts.MAIN), makeTestInsideSegments(), problemType , makeTestPremadeSegments(getTestPremadeIDs()), Consts.STRUCTURES_LIST, splitConditions(conditionOptions.getAreasText()[0]), splitConditions(conditionOptions.getAreasText()[1]), splitConditions(conditionOptions.getAreasText()[2]), splitConditions(conditionOptions.getAreasText()[3]),getTestSolution(),null,dragControl), tabPanel.getOffsetHeight(),titleBox.getText(), descriptionBox.getText(), new StackableContainer((mainFunction.getBoxContent()+Consts.FUNCTION),dragControl,Consts.MAIN), makeTestInsideSegments(), problemType , makeTestPremadeSegments(getTestPremadeIDs()), Consts.STRUCTURES_LIST, splitConditions(conditionOptions.getAreasText()[0]), splitConditions(conditionOptions.getAreasText()[1]), splitConditions(conditionOptions.getAreasText()[2]), splitConditions(conditionOptions.getAreasText()[3]),getTestSolution(),null,dragControl);
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
		for(int i=0;i<algoSolution.getMainFunction().getInsidePanel().getWidgetCount();i++){
			idAndCount = buildIDTestString((StackableContainer)algoSolution.getMainFunction().getInsidePanel().getWidget(i),idAndCount);
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
		for(int i=0;i<algoSolution.getMainFunction().getInsidePanel().getWidgetCount();i++){
			count = countWidgets((StackableContainer)algoSolution.getMainFunction().getInsidePanel().getWidget(i),count);
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
	        	rightPanel.add(algoSolution, "Solution"); 
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
	        	
	        	invisibleProblemType.setText(problemType);
	        	invisibleInnerSegments.setText(encodeStringArray(innerFunctions.getDBString().split(".:colon:.")));
	        	invisiblePremadeSegments.setText(encodeStringArray(premadeSegments.getPremadeDBString().split(".:colon:.")));
	        	
	        	invisibleFor1.setText(encodeStringArray(splitConditions(conditionOptions.getAreasText()[0])));
	        	invisibleFor2.setText(encodeStringArray(splitConditions(conditionOptions.getAreasText()[1])));
	        	invisibleFor3.setText(encodeStringArray(splitConditions(conditionOptions.getAreasText()[2])));
	        	invisibleBoolean.setText(encodeStringArray(splitConditions(conditionOptions.getAreasText()[3])));
	        	
	        	invisibleAlgoSolution.setText(getAlgoSolution());
	        	
	        	titleDescriptionFormPanel.submit();
	        	invisibleSubmitButton.click();
	        	Window.alert("clicked");
	        }
	 }
		public String getAlgoSolution(){
			String idChain = "";
			for(int i=0;i<algoSolution.getMainFunction().getInsidePanel().getWidgetCount();i++){
				idChain = buildIDString((StackableContainer)algoSolution.getMainFunction().getInsidePanel().getWidget(i),idChain);
			}
			return idChain;
		}
		public String buildIDString(StackableContainer sc, String idChain) {
			if(sc.getContent().contains(Consts.TOPANYORDER))
				idChain+="[";
			else
				idChain+="{";
			//idChain+=sc.getID();
			for (int i = 0; i < sc.getInsidePanel().getWidgetCount(); i++) {
				idChain=buildIDString((StackableContainer) sc.getInsidePanel().getWidget(i),idChain);
			}
			if(sc.getContent().contains(Consts.TOPANYORDER))
				idChain+="]";
			else
				idChain+="}";
			return idChain;
		}
		public String encodeStringArray(String[] arr){
			String encoded ="";
			for(String s:arr)
				encoded+=s+".:|:.";
			return encoded;
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
				  rightPanel.showStack(rightPanel.getWidgetIndex(algoSolution));
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
		 for(int i=0;i<algoSolution.getMainFunction().getInsidePanel().getWidgetCount();i++){
			 index = assignContainerIDs(((StackableContainer)algoSolution.getMainFunction().getInsidePanel().getWidget(i)),index,idArr);
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
