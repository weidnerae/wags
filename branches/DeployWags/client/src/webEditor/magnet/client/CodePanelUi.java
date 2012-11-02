package webEditor.magnet.client;

import webEditor.client.Proxy;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * This is effectively the right side of the screen in the editing mode tab.
 *
 */
public class CodePanelUi extends Composite {
	public AbsolutePositionDropController dropControl;
	public StringBuilder plainText;
	public StackableContainer mainFunction;
	private String title;
	private int tabNumber = -1; // So the initial increment will give 0 tabs
	private RefrigeratorMagnet magnet;
	private PopupPanel popupPanel;
	
	@UiField ScrollPanel nestPanel;   //takes up the entirety of the CodePanel, used to let mainPanel scroll
	@UiField AbsolutePanel mainPanel;  //nested inside nestPanel, this is where mainFunction lives
	@UiField Button button; //finalize button 
	@UiField LayoutPanel layoutPanel; //the panel that all of these pieces are sitting in
//	@UiField PopupPanel popupPanel;
//	@UiField Button yesButton;
//	@UiField Button noButton;
	
	
	private static CodePanelUiUiBinder uiBinder = GWT
			.create(CodePanelUiUiBinder.class);

	interface CodePanelUiUiBinder extends UiBinder<Widget, CodePanelUi> {
	}

	/**
	 * Creates CodePanel for right side of editing panel.  
	 * 
	 * @param mainFunction The function that is to be built
	 * @param insideFunctions Possible inner functions nested into the function to be built.
	 */
	public CodePanelUi(RefrigeratorMagnet magnet, StackableContainer main,
			StackableContainer[] insideFunctions, PickupDragController dc, String title) {
		initWidget(uiBinder.createAndBindUi(this));
		this.magnet = magnet;
		this.mainFunction = main;
		this.title = title;
		setupPopupPanel();
		
//		popupPanel.setVisible(false);
		
		
		addInsideFunctions(insideFunctions);
		mainPanel.add(mainFunction);
	}
	public void setupPopupPanel(){
		popupPanel = new PopupPanel(true);
		VerticalPanel vPanel = new VerticalPanel();
		HorizontalPanel hPanel = new HorizontalPanel();
		Label pLabel = new Label("Are you sure you wish to finalize?");
		Button yesButton = new Button("Yes",new yesHandler());
		yesButton.addStyleName("big_popup_button");
		Button noButton = new Button("No",new noHandler());
		noButton.addStyleName("big_popup_button");
		hPanel.add(yesButton);	
		hPanel.add(noButton);
		hPanel.setCellWidth(yesButton, "100px");
		hPanel.setCellHeight(yesButton, "50px");
		hPanel.setCellWidth(noButton, "100px");
		hPanel.setCellHeight(noButton, "50px");
		vPanel.add(pLabel);
		vPanel.add(hPanel);
		popupPanel.add(vPanel);
	}
	
	//finalize button handler, calls methods that generate the content and evaluate it
	@UiHandler("button")
	void handleClick(ClickEvent e){
		popupPanel.setPopupPosition(button.getAbsoluteLeft(), button.getAbsoluteTop()-80);
		popupPanel.setVisible(true);
		popupPanel.show();
		

	}
	 private class yesHandler implements ClickHandler{
			public void finalize(){
				//algorithms are not yet implemented
				if (magnet.getProblemType().equals(Consts.ALGORITHM_PROBLEM)) {
					evaluateAlgorithmProblem();  //unfinished method
				}
				
				plainText = new StringBuilder();
				
				buildContent(mainFunction);
				String code = plainText.toString();
				ResultsPanelUi.setCodeText(code);
				code = code.replaceAll(Consts.HC_DELIMITER,"");
				Proxy.magnetReview(code, title);
				magnet.tabPanel.selectTab(1);
				tabNumber = -1;
			}
			
		 public void onClick(ClickEvent event) {
			 finalize();
			 popupPanel.setVisible(false);
		 }
	 }
	 private class noHandler implements ClickHandler{
		 public void onClick(ClickEvent event) {
			 popupPanel.setVisible(false);
		 }
	 }
//	@UiHandler("yesButton")
//	void handleYesClick(ClickEvent e){
//		finalize();
//		popupPanel.setVisible(false);
//	}
	//@UiHandler("noButton")
//	void handleNoClick(ClickEvent e){
//		popupPanel.setVisible(false);
//	}
	

	/**
	 * Places possible inside functions into the main function
	 * 
	 * @param insideFunctions the functions you would like to be placed inside the main function 
	 */
	public void addInsideFunctions(StackableContainer[] insideFunctions) {
		if (insideFunctions == null) {
			return;
		}
		
		for (int i = 0; i < insideFunctions.length && insideFunctions[i] != null; i++) {
			mainFunction.addInsideContainer(insideFunctions[i]);
		}
	}

	/**
	 * This executes when the finalize button is pressed.
	 * 
	 * It takes the mainFunction stackable container and builds the content 
	 * so that it can be formatted.
	 * 
	 * After this is run, plainText will contain the text of all the stackable 
	 * containers, but stripped of all the embedded HTML shenanigans.
	 * 
	 * @param sc 
	 * 		The mainFunction StackableContainer. All of the other 
	 * 		StackableContainers are grabbed recursively.
	 */
	public void buildContent(StackableContainer sc) {
        tabNumber++;
        String tabs = "";
        boolean isNested = false;
        if(sc.getInsidePanel().getWidgetCount() > 0) isNested = true;
        
        // For proper indentation
        for (int i = 0; i < tabNumber; i++) {
                tabs += "\t";
        }
        
        if (sc.getTopLabel() != null) {
                String rawHTML = sc.getTopLabel().getHTML();
                String procHTML = rawHTML.replaceAll("<br/>|<br />|<br>", "");
                HTML topLabel = new HTML(procHTML);
                if(isNested) plainText.append("\n");
                plainText.append(tabs + topLabel.getText() + "\n");
                
        }
        
        for (int i = 0; i < sc.getInsidePanel().getWidgetCount(); i++) {
                buildContent((StackableContainer) sc.getInsidePanel().getWidget(i));
        }
        
        if (sc.getBottomLabel() != null) {
                String rawHTML = sc.getBottomLabel().getHTML();
                String procHTML = rawHTML.replaceAll("<br/>|<br />|<br>", "\n");
                HTML bottomLabel = new HTML(procHTML);
                if(!bottomLabel.getText().equals(""))
                        plainText.append(tabs + bottomLabel.getText() + "\n");
                        if(isNested) plainText.append("\n");
        }        
        tabNumber--;

	}
	
	
	
	/********************************************************************
	 * These are unfinished methods that help with the                  *
	 *  unimplemented algorithm problems, hopefully to be completed soon*
	 ********************************************************************/
	
	
	public void evaluateAlgorithmProblem(){
		boolean done = false;
		String userSolution = getAlgoSolution();
		String givenSolution = magnet.getSolution();
		for(int i=0;i<givenSolution.length();i++){
			
		}
		
	}
	
	public String getAlgoSolution(){
		String idChain = "";
		for(int i=0;i<mainFunction.getInsidePanel().getWidgetCount();i++){
			idChain = buildIDString((StackableContainer)mainFunction.getInsidePanel().getWidget(i),idChain);
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

}
