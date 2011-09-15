package microlabs.dst.client;

import java.util.List;
import com.google.gwt.user.client.rpc.IsSerializable;

import microlabs.dst.shared.SerialEdge;
import microlabs.dst.shared.SerialNode;

public class ProblemResult implements IsSerializable
{
	private String problemName;
	private String problemText;
	private List<SerialNode> nodes;
	private List<SerialEdge> edges;
	private String currFeedback;
	private int attemptNum;
	
	private ProblemResult(){}

	public ProblemResult(String problemName, String problemText, List<SerialNode> nodes, List<SerialEdge> edges, String currFeedback, int attemptNum)
	{
		this.problemName = problemName;
		this.problemText = problemText;
		this.nodes = nodes;
		this.edges = edges;
		this.currFeedback = currFeedback;
		this.attemptNum = attemptNum;
	}
	
	/**
	 * @return the problemName
	 */
	public String getProblemName() {
		return problemName;
	}

	/**
	 * @param problemName the problemName to set
	 */
	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}

	/**
	 * @param problemText the problemText to set
	 */
	public void setProblemText(String problemText) {
		this.problemText = problemText;
	}

	/**
	 * @return the problem
	 */
	public String getProblemText() {
		return problemText;
	}

	public List<SerialNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<SerialNode> nodes) {
		this.nodes = nodes;
	}
	
	public List<SerialEdge> getEdges() {
		return edges;
	}


	public void setEdges(List<SerialEdge> edges) {
		this.edges = edges;
	}

	/**
	 * @return the currFeedback
	 */
	public String getCurrFeedback() {
		return currFeedback;
	}

	/**
	 * @param currFeedback the currFeedback to set
	 */
	public void setCurrFeedback(String currFeedback) {
		this.currFeedback = currFeedback;
	}

	/**
	 * @return the attemptNum
	 */
	public int getAttemptNum() {
		return attemptNum;
	}

	/**
	 * @param attemptNum the attemptNum to set
	 */
	public void setAttemptNum(int attemptNum) {
		this.attemptNum = attemptNum;
	}
}
