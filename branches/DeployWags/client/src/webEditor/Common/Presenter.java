package webEditor.Common;

import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;

public interface Presenter {
	public void go(final HasWidgets container);
	public void bind();
	public boolean bound();
	public void update(List<String> data);
}