package webEditor.Common;

/**
 * @author  : Dakota Murray
 * @version : 21 July 2014
 * 
 * Controls all page transitions of the web by means of GWT's native History class. Whenever a page transition 
 * takes place, the class triggering the transition adds a new history token to the url. The AppController notices
 * the change in the url and responds by executing the page transition. 
 * 
 * Page transitions are not specific to each presenter and thus the AppController decouples the specific presenter logic 
 * from the more general page transition logic.
 * 
 * In order to add a new view the following steps must be carried out in the AppController:
 *     -Ensure that there is a token in the Tokens class corresponding to the new page
 *     -Add any special rules for the page transition inside the onValueChange method (redirects, privilege checks, etc)
 *     -Create a new method that when called will load the new page (view and presenter) and set it as the current page,
 *      use the other such method as a guide.
 *     -Create a new case corresponding to the new page for the switch statement inside the laodPage method which when trigered
 *      will call the method to load the new page.
 *     -Congratulations! A new page transition has just been created!
 *  
 * Send any questions, comments, or concerns to murrayds@email.appstate.edu
 *  
 */
import java.util.HashMap;

import webEditor.ProxyFramework.AbstractServerCall;
import webEditor.ProxyFramework.BuildDSTCommand;
import webEditor.ProxyFramework.BuildDatabaseCommand;
import webEditor.admin.LMEditTab;
import webEditor.admin.LogicalTab;
import webEditor.admin.MagnetTab;
import webEditor.admin.ProblemCreationPanel;
import webEditor.admin.ProgrammingTab;
import webEditor.admin.ReviewTab;
import webEditor.admin.SectionTab;
import webEditor.admin.StudentTab;
import webEditor.magnet.view.MagnetProblem;
import webEditor.magnet.view.MagnetProblemPresenter;
import webEditor.magnet.view.MagnetProblemPresenterImpl;
import webEditor.presenters.concrete.DefaultPagePresenterImpl;
import webEditor.presenters.concrete.LoginPresenterImpl;
import webEditor.presenters.concrete.MagnetPagePresenterImpl;
import webEditor.presenters.concrete.WagsPresenterImpl;
import webEditor.presenters.interfaces.DefaultPagePresenter;
import webEditor.presenters.interfaces.LoginPresenter;
import webEditor.presenters.interfaces.MagnetPagePresenter;
import webEditor.views.concrete.DefaultPage;
import webEditor.views.concrete.Login;
import webEditor.views.concrete.Wags;
import webEditor.views.interfaces.MagnetPageView;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class AppController implements ValueChangeHandler<String> {

	public AppController()
	{
		bind();
	}
	
	/**
	 * Binds this class to the History change handler thus allowing the AppController's onValueChanged
	 * method to be called when the underlying History class notices a change. 
	 */
	private void bind()
	{
		History.addValueChangeHandler(this);
	}
	
	/**
	 * Called whenever GWT's History class notices a change (ie: the url is changed). Is the entry point
	 * for all page transitions. Special page transition rules (privilage checks, redirectgs, etc) go in here.
	 * 
	 * @param event An event which is triggered when the History value changes. The event object contains the 
	 * 				updated url (the new token) which is used to carry out page transitions. 
	 */
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String url = event.getValue();
		boolean isLoggedIn = ClientFactory.getAppModel().isLoggedIn();
		boolean isAdmin = ClientFactory.getAppModel().isAdmin();
		
		String[] args = url.split("&");
		String token = args[0];
		String arg = args[1];
		if (token == null) {
			token = Tokens.DEFAULT;
		}
		if (! isLoggedIn ) {
			token = Tokens.LOGIN;
		}
		if ( token.startsWith(Tokens.LOGIN)) {
			if ( isLoggedIn ) {
				token = Tokens.DEFAULT;
			}
			else {
				token = Tokens.LOGIN;
			}
		}
		else if(token.startsWith(Tokens.ADMIN) && !isAdmin) {
			Window.alert("You do not have admin privelages");
			token = Tokens.DEFAULT;
		}
		
		Wags main = ClientFactory.getWagsView();
		AcceptsOneWidget pres = new WagsPresenterImpl(main);
		loadPage(token, pres, arg);
		RootLayoutPanel root = RootLayoutPanel.get();
		root.clear();
		root.add(main);
	}
	
	/**
	 * Simply delegates the page transition logic to the appropriate method based on the token passed in
	 * 
	 * @param token A History token added to the url
	 * @param page  The main application page (Wags.java and corresponding files)
	 * @param arg   An optional String argument used for transitions where more specific information is needed
	 */
	public void loadPage(String token, AcceptsOneWidget page, String arg) 
	{
		switch(token)
		{
		case Tokens.DST:
			loadDST(page);
			break;
		case Tokens.MAGNET:
			loadMagnets(page);
			break;
		case Tokens.DATABASE:
			loadDatabasePage(page);
			break;
		case Tokens.LOGIN:
			loadLoginPage(page);
			break;
		case Tokens.EDITOR:
			loadEditor(page);
			break;
		case Tokens.LOGICALCREATION:
			loadLogicalProblemCreation(page);
			break;
		case Tokens.LOGICALMANAGEMENT:
			loadLogicalProblemManagement(page);
			break;
		case Tokens.MAGNETMANAGEMENT:
			loadMagnetProblemManagement(page);
			break;
		case Tokens.MAGNETCREATION:
			loadMagnetProblemCreation(page);
			break;
		case Tokens.MANAGESECTION:
			loadSectionManagement(page);
			break;
		case Tokens.MANAGESTUDENT:
			loadStudentManagement(page);
			break;
		case Tokens.PROGRAMMINGMANAGEMENT:
			loadProgrammingManagement(page);
			break;
		case Tokens.REVIEW:
			loadReviewTab(page);
			break;
		case Tokens.MAGNETPROBLEM:
			loadMagnetProblem(page, arg);
			break;
		default:
			loadDefaultPage(page);
		}	
	}
	
	/**
	 * A method called upon login and logout which will set the necessary details of the application
	 * wide model with updated information about user status. 
	 * 
	 * @param map A HashMap where the keys are made up of the names of the arguments and the values are
	 * 		  	  the values of those arguments specific to the current user. 
	 */
	public static void setUserDetails(HashMap<String, String> map)
	{
		AppModel model = ClientFactory.getAppModel();
		model.setId( new Integer(map.get("id")), false);
		model.setUsername( map.get("username"), false);
		model.setIsLoggedIn(true, false);
		model.setIsAdmin(map.get("admin").equals("1"), false);
		model.notifyObservers();
	}
	
	
	/**
	 * Below are all method which handle specific page transition logic. There is no point in commenting each one as 
	 * most follow similar logic. THe basic idea is to either instantiate or retrieve an existing view, give it a presenter
	 * if it does not already have one, and make it the current page. 
	 */
	
	//As of right now not all of these transitions have been implemented using the new MVP pattern
	
	private void loadMagnetProblem(AcceptsOneWidget page, String arg) {
		Window.alert("Loading Magnet Problem");
		MagnetProblem view = ClientFactory.getMagnetProblemView();
		if (!view.hasPresenter()) {
			MagnetProblemPresenter pres = new MagnetProblemPresenterImpl(view);
			pres.bind();
		}
		page.setWidget(view);
	}

	//TODO Implement in MVP
	public void loadReviewTab(AcceptsOneWidget page) 
	{
		ReviewTab tab = ClientFactory.getReviewTab();
		page.setWidget(tab);
		
	}
	
	//TODO implement in MVP
	public void loadLogicalProblemManagement(AcceptsOneWidget page)
	{
		LogicalTab view = ClientFactory.getLogicalManagementTab();
		page.setWidget(view);
	}
	
	//TODO implement in MVP
	public void loadLogicalProblemCreation(AcceptsOneWidget page)
	{
		LMEditTab view = ClientFactory.getLogicalCreationTab();
		page.setWidget(view);
	}
	
	
	//TODO Implement in MVP
	public void loadMagnetProblemManagement(AcceptsOneWidget page)
	{
		MagnetTab view = ClientFactory.getMagnetManagementTab();
		page.setWidget(view);
	}
	
	//TODO implement in MVP (save this one, we are make a new design after all)
	public void loadMagnetProblemCreation(AcceptsOneWidget page)
	{
		ProblemCreationPanel view = ClientFactory.getMagnetProblemCreationTab();
		page.setWidget(view);
	}
	
	//TODO implement in MVP
	public void loadSectionManagement(AcceptsOneWidget page)
	{
		SectionTab view = ClientFactory.getSectionTab();
		page.setWidget(view);
	}
	
	//TODO implement in MVP
	public void loadProgrammingManagement(AcceptsOneWidget page)
	{
		ProgrammingTab view = ClientFactory.getProgrammingTab();
		page.setWidget(view);
	}
	
	//TODO implement in MVP
	public void loadStudentManagement(AcceptsOneWidget page)
	{
		StudentTab view = ClientFactory.getStudentManagementTab();
		page.setWidget(view);
	}
	
	public void loadLoginPage(AcceptsOneWidget page)
	{
		Login view = ClientFactory.getLoginView();
		if ( !view.hasPresenter()) {
			LoginPresenter pres = new LoginPresenterImpl(view);
			pres.bind();
		}
		page.setWidget(view);
	}
	
	public void loadDefaultPage(AcceptsOneWidget page) {
		DefaultPage view = ClientFactory.getDefaultView();
		if ( !view.hasPresenter()) {
			DefaultPagePresenter pres = new DefaultPagePresenterImpl(view);
			pres.bind();
		}
		page.setWidget(view);
	}
	
	//TODO implement in MVP (do we really need this?)
	public void loadEditor(AcceptsOneWidget page) {
		page.setWidget(ClientFactory.getEditorView());
	}
	
	//TODO implement in MVP (look at the magnet page as a guide)
	public void loadDST(AcceptsOneWidget page) {
		AbstractServerCall cmd = new BuildDSTCommand(page);
		cmd.sendRequest();
	}
	
	public void loadMagnets(AcceptsOneWidget page) {
		MagnetPageView view = ClientFactory.getMagnetPageView();
		if (!view.hasPresenter()) {
			MagnetPagePresenter pres = new MagnetPagePresenterImpl(view);
			pres.bind();
		}
		page.setWidget(view);
	}
	
	//TODO implement MVP (look at the magnet page as a guide)
	public void loadDatabasePage(AcceptsOneWidget page) {
		AbstractServerCall cmd = new BuildDatabaseCommand(page);
		cmd.sendRequest();
	}

}
