package webEditor.magnet.view;

import webEditor.Common.Presenter;

import com.github.gwtbootstrap.client.ui.TabPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class MagnetProblem extends Composite implements MagnetProblemView {

	private static MagnetProblemUiBinder uiBinder = GWT
			.create(MagnetProblemUiBinder.class);

	interface MagnetProblemUiBinder extends UiBinder<Widget, MagnetProblem> {
	}
	
	private Presenter presenter;
	
	@UiField TabPanel tabPanel;
	@UiField Panel leftSide;
	@UiField Panel rightSide;
	@UiField Panel resultsPanel;
	@UiField Panel trashbin;
	@UiField Panel instructions;
	
	@UiField Button finalizeButton;
	@UiField Button stateButton;
	@UiField Button resetButton;
	
	public MagnetProblem()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
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
	public Composite getCodePanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Composite getConstructPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Composite getContentPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Composite getDirectionsContentPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Composite getTrashBin() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Composite getLayoutPanel() {
		// TODO Auto-generated method stub
		return null;
	}
}
