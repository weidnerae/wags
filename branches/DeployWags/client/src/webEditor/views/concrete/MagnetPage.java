package webEditor.views.concrete;

import webEditor.Common.Presenter;
import webEditor.presenters.interfaces.MagnetPagePresenter;
import webEditor.views.interfaces.MagnetPageView;


import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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
public class MagnetPage extends Composite implements MagnetPageView {
	
	private static MagnetPageUiBinder uiBinder = GWT
			.create(MagnetPageUiBinder.class);

	interface MagnetPageUiBinder extends UiBinder<Widget, MagnetPage> {
	}
	
	@UiField ComplexPanel problemPanel;
	private MagnetPagePresenter presenter;
	
	public MagnetPage()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = (MagnetPagePresenter) presenter;
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
	public ComplexPanel getProblemPanel() {
		return problemPanel;
	}
	
}