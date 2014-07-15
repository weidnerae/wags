package webEditor.Common;

import java.util.HashMap;

import webEditor.ProxyFramework.AbstractServerCall;
import webEditor.ProxyFramework.BuildDSTCommand;
import webEditor.ProxyFramework.BuildDatabaseCommand;
import webEditor.ProxyFramework.BuildMagnetsCommand;
import webEditor.admin.LMEditTab;
import webEditor.admin.LogicalTab;
import webEditor.admin.MagnetTab;
import webEditor.admin.ProblemCreationPanel;
import webEditor.admin.ProgrammingTab;
import webEditor.admin.SectionTab;
import webEditor.admin.StudentTab;
import webEditor.presenters.concrete.DefaultPagePresenterImpl;
import webEditor.presenters.concrete.LoginPresenterImpl;
import webEditor.presenters.concrete.WagsPresenterImpl;
import webEditor.presenters.interfaces.DefaultPagePresenter;
import webEditor.presenters.interfaces.LoginPresenter;
import webEditor.views.concrete.DefaultPage;
import webEditor.views.concrete.Login;
import webEditor.views.concrete.Wags;

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
	
	private void bind()
	{
		History.addValueChangeHandler(this);
	}
	
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();
		boolean isLoggedIn = ClientFactory.getAppModel().isLoggedIn();
		boolean isAdmin = ClientFactory.getAppModel().isAdmin();
		
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
		loadPage(token, pres);
		RootLayoutPanel root = RootLayoutPanel.get();
		root.clear();
		root.add(main);
	}
	
	public void loadPage(String token, AcceptsOneWidget page) 
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
		default:
			loadDefaultPage(page);
		}	
	}
	
	public void loadLogicalProblemManagement(AcceptsOneWidget page)
	{
		LogicalTab view = ClientFactory.getLogicalManagementTab();
		page.setWidget(view);
	}
	
	public void loadLogicalProblemCreation(AcceptsOneWidget page)
	{
		LMEditTab view = ClientFactory.getLogicalCreationTab();
		page.setWidget(view);
	}
	
	public void loadMagnetProblemManagement(AcceptsOneWidget page)
	{
		MagnetTab view = ClientFactory.getMagnetManagementTab();
		page.setWidget(view);
	}
	
	public void loadMagnetProblemCreation(AcceptsOneWidget page)
	{
		ProblemCreationPanel view = ClientFactory.getMagnetProblemCreationTab();
		page.setWidget(view);
	}
	
	public void loadSectionManagement(AcceptsOneWidget page)
	{
		SectionTab view = ClientFactory.getSectionTab();
		page.setWidget(view);
	}
	
	public void loadProgrammingManagement(AcceptsOneWidget page)
	{
		ProgrammingTab view = ClientFactory.getProgrammingTab();
		page.setWidget(view);
	}
	
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
	
	public void loadEditor(AcceptsOneWidget page) {
		page.setWidget(ClientFactory.getEditorView());
	}
	
	public void loadDST(AcceptsOneWidget page) {
		AbstractServerCall cmd = new BuildDSTCommand(page);
		cmd.sendRequest();
	}
	
	public void loadMagnets(AcceptsOneWidget page) {
		AbstractServerCall cmd = new BuildMagnetsCommand(page);
		cmd.sendRequest();
	}
	
	public void loadDatabasePage(AcceptsOneWidget page) {
		AbstractServerCall cmd = new BuildDatabaseCommand(page);
		cmd.sendRequest();
	}
	
	public static void setUserDetails(HashMap<String, String> map)
	{
		AppModel model = ClientFactory.getAppModel();
		model.setId( new Integer(map.get("id")), false);
		model.setUsername( map.get("username"), false);
		model.setIsLoggedIn(true, false);
		model.setIsAdmin(map.get("admin").equals("1"), false);
		model.notifyObservers();
	}
}
