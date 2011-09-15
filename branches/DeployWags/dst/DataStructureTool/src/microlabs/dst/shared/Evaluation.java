package microlabs.dst.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

import microlabs.dst.client.EdgeParent;
import microlabs.dst.client.Node;

public abstract class Evaluation implements IsSerializable
{
	protected String errorMessage;
	
	public Evaluation()
	{
		errorMessage = "";
	}
	
	public abstract String evaluate(String[] arguments, ArrayList<Node> nodes, ArrayList<EdgeParent> edges);
}
