package webEditor.Common;

import com.google.gwt.user.client.ui.IsWidget;

public interface View extends IsWidget {
	void setPresenter(Presenter presenter);
	boolean hasPresenter();
	Presenter getPresenter();
}
