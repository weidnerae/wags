package webEditor.views.concrete;

import webEditor.Common.Presenter;
import webEditor.presenters.interfaces.ProblemPagePresenter;
import webEditor.views.interfaces.ProblemPageView;

import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * This is the "landing page" for magnets problems. It displays a list of 
 * buttons corresponding to the code magnet exercises that have been assigned 
 * to the user. If they have successfully completed the problem, the text on 
 * the button will be green. Otherwise, it will simply be black test displaying 
 * the name of the exercise.
 * 
 *
 */
public class ProblemPage extends Composite implements ProblemPageView {
	
	private static ProblemPageUiBinder uiBinder = GWT
			.create(ProblemPageUiBinder.class);

	interface ProblemPageUiBinder extends UiBinder<Widget, ProblemPage> {
	}
	
	@UiField ComplexPanel magnetPanel;
	@UiField ComplexPanel logicalPanel;
	@UiField ComplexPanel databasePanel;
	
	@UiField Button magnetCategory;
	@UiField Button logicalCategory;
	@UiField Button databaseCategory;
	
	private ProblemPagePresenter presenter;
	
	public ProblemPage()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = (ProblemPagePresenter) presenter;
	}

	@Override
	public boolean hasPresenter() {
		return presenter != null;
	}

	@Override
	public Presenter getPresenter() {
		return presenter;
	}

	@Override
	public ComplexPanel getMagnetPanel() {
		return magnetPanel;
	}

	@Override
	public ComplexPanel getLogicalPanel() {
		return logicalPanel;
	}

	@Override
	public ComplexPanel getDatabasePanel() {
		return databasePanel;
	}
	
	@UiHandler ("magnetCategory")
	public void onMagnetCategoryClick(ClickEvent event) {
		presenter.onMagnetCategoryClick();
	}
	
	@UiHandler ("logicalCategory") 
	public void onLogicalCategoryClick(ClickEvent event) {
		presenter.onLogicalCategoryClick();
	}
	
	@UiHandler ("databaseCategory")
	public void onDatabaseCategoryClick(ClickEvent event) {
		presenter.onDatabaseCategroryClick();
	}

	@Override
	public Button getMagnetCategory() {
		return magnetCategory;
	}

	@Override
	public Button getLogicalCategory() {
		return logicalCategory;
	}

	@Override
	public Button getDatabaseCategory() {
		return databaseCategory;
	}
}
