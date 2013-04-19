package webEditor.admin;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

import webEditor.admin.builders.LMBuilder;

public abstract class LMDisplay extends Composite{
	@UiField VerticalPanel basePanel;
	LMBuilder builder;
	
	public void setBuilder(LMBuilder builder){
		this.builder = builder;
	}
}

