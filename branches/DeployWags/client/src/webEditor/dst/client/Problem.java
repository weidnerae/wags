package webEditor.dst.client;

import org.vaadin.gwtgraphics.client.DrawingArea;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.AbsolutePanel;

public abstract class Problem implements IsSerializable
{	
	private String name;
	private String problemText;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getProblemText() {
		return problemText;
	}

	public void setProblemText(String problemText) {
		this.problemText = problemText;
	}
	
	public abstract DisplayManager createDisplayManager(AbsolutePanel panel, DrawingArea canvas);
	public abstract String[] getArguments();
	public abstract Evaluation getEval();
	public abstract String evaluate();
	public abstract String getNodeType();
}
