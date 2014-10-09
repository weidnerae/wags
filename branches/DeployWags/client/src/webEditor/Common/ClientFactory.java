package webEditor.Common;


import webEditor.admin.ProblemCreationPanel;
import webEditor.magnet.view.MagnetProblem;
import webEditor.views.concrete.DefaultPage;
import webEditor.views.concrete.Editor;
import webEditor.views.concrete.LMEditTab;
import webEditor.views.concrete.LogicalTab;
import webEditor.views.concrete.Login;
import webEditor.views.concrete.MagnetTab;
import webEditor.views.concrete.ProblemPage;
import webEditor.views.concrete.ProgrammingTab;
import webEditor.views.concrete.ReviewTab;
import webEditor.views.concrete.StudentTab;
import webEditor.views.concrete.Wags;
import webEditor.views.concrete.SectionTab;

/**
 * @author   Dakota Murray
 * @version  21 July 2014
 * 
 * A class which provides singleton access to all views and application wide objects. Views and UI 
 * elements are expensive to instantiate. The ClientFactory ensures that only one instance of a view 
 * can ever be instantiated. 
 *
 * It is pointless to document each individual method in this class as they all follow an identical format. 
 * A static null instance of the object is created as a field. Whenever a class requests access to a 
 * ClientFactory object for the first time then that object is instantiated. If the object being requested has
 * already been instantiated then the existing instance is returned. 
 */
public class ClientFactory {
	
	private static AppController app;
	private static Login login;
	private static Wags wags;
	private static AppModel model;
	private static Editor editor;
	private static DefaultPage defaultPage;
	private static ProblemCreationPanel mpcPanel;
	private static SectionTab sectionTab;
	private static LogicalTab logicalTab;
	private static LMEditTab logicalCreation;
	private static MagnetTab magnetManagement;
	private static StudentTab studentTab;
	private static ProgrammingTab progTab;
	private static ReviewTab reviewTab;
	private static ProblemPage problemPage;
	private static MagnetProblem magnetProblem;
	
	public static AppController getAppController()
	{
		if (app == null) {
			app = new AppController();
		}
		return app;
	}
	
	public static AppModel getAppModel()
	{
		if (model == null) {
			model = new AppModel();
		}
		return model;
	}
	
	public static Wags getWagsView()
	{
		if (wags == null) {
			wags = new Wags();
		}
		return wags;
	}
	
	public static Login getLoginView()
	{
		if( login == null) {
			login = new Login();
		}
		return login;
	}
	
	public static Editor getEditorView()
	{
		if( editor == null) {
			editor = new Editor();
		}
		return editor;
	}
	
	public static DefaultPage getDefaultView()
	{
		if( defaultPage == null)
		{
			defaultPage = new DefaultPage();
		}
		return defaultPage;
	}
	
	public static ProblemCreationPanel getMagnetProblemCreationTab()
	{
		if (mpcPanel == null) {
			mpcPanel = new ProblemCreationPanel();
		}
		return mpcPanel;
	}
	
	public static SectionTab getSectionTab()
	{
		if (sectionTab == null) {
			sectionTab = new SectionTab();
		}
		return sectionTab;
	}
	
	public static LogicalTab getLogicalManagementTab()
	{
		if (logicalTab == null) {
			logicalTab = new LogicalTab();
		}
		return logicalTab;
	}
	
	public static LMEditTab getLogicalCreationTab()
	{
		if (logicalCreation == null) {
			logicalCreation = new LMEditTab();
		}
		return logicalCreation;
	}
	
	public static MagnetTab getMagnetManagementTab()
	{
		if (magnetManagement == null) {
			magnetManagement = new MagnetTab();
		}
		return magnetManagement;
	}
	
	public static StudentTab getStudentManagementTab()
	{
		if (studentTab == null) {
			studentTab = new StudentTab();
		}
		return studentTab;
	}
	
	public static ProgrammingTab getProgrammingTab()
	{
		if (progTab == null) {
			progTab = new ProgrammingTab();
		}
		return progTab;
	}
	
	public static ReviewTab getReviewTab()
	{
		if (reviewTab == null) {
			reviewTab = new ReviewTab();
		}
		return reviewTab;
	}
	
	public static ProblemPage getMagnetPageView()
	{
		if (problemPage == null) {
			problemPage = new ProblemPage();
		}
		return problemPage;
	}
	
	public static MagnetProblem getMagnetProblemView()
	{
		if (magnetProblem == null) {
			magnetProblem = new MagnetProblem();
		}
		return magnetProblem;
	}
	
	
}
