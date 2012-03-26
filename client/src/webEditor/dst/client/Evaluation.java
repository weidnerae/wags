package webEditor.dst.client;

import java.util.ArrayList;

public abstract class Evaluation
{
	protected String errorMessage;
	
	public Evaluation()
	{
		errorMessage = "";
	}
	
	public abstract String evaluate(String problemName, String[] arguments, ArrayList<Node> nodes, ArrayList<EdgeParent> edges);
	
}