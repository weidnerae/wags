package webEditor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;

public class Uploader extends View
{

	private static UploaderUiBinder uiBinder = GWT
			.create(UploaderUiBinder.class);

	interface UploaderUiBinder extends UiBinder<Widget, Uploader>{}
	
	@UiField FormPanel form;
	@UiField FileUpload upload;

	public Uploader()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public WEAnchor getLink()
	{
		return new WEAnchor("Upload File", this, "uploader");
	}
}
