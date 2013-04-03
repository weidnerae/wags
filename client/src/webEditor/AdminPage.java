package webEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AdminPage extends Composite {

	private static AdminPageUiBinder uiBinder = GWT
			.create(AdminPageUiBinder.class);

	interface AdminPageUiBinder extends UiBinder<Widget, AdminPage> {
	}

	public AdminPage() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
