package webEditor.admin;

import com.google.gwt.user.client.ui.Composite;

import webEditor.admin.builders.ArgHolder;
import webEditor.admin.builders.LMBuilder;

public abstract class LMDisplay extends Composite{
	LMBuilder builder;
	
	public void setBuilder(LMBuilder builder){
		this.builder = builder;
	}
	
	public abstract void fillBuilder(ArgHolder child);
}

