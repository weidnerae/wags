package microlabs.dst.server;

import java.util.ArrayList;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class ProblemResultJDO 
{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String problemName;
	
	@Persistent
	private String emailAddress;
	
	@Persistent
	private int attemptNum;
	
	@Persistent
	private String currFeedback;
	
	@Persistent
	private String problemText;
	
	@Persistent 
	private ArrayList<NodeJDO> nodes;
	
	@Persistent 
	private ArrayList<EdgeJDO> edges;
	
	public ProblemResultJDO(String emailAddress, int attemptNum, String problemName, String problem,
			ArrayList<NodeJDO> nodes, ArrayList<EdgeJDO> edges, String currFeedback)
	{
		this.emailAddress = emailAddress;
		this.attemptNum = attemptNum;
		this.currFeedback = currFeedback;
		this.problemName = problemName;
		this.problemText = problem;
		this.nodes = nodes;
		this.edges = edges;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
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
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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

	public String getCurrFeedback() {
		return currFeedback;
	}

	public void setCurrFeedback(String currFeedback) {
		this.currFeedback = currFeedback;
	}

	public String getProblemText() {
		return problemText;
	}

	public void setProblemText(String problem) {
		this.problemText = problem;
	}

	public ArrayList<NodeJDO> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<NodeJDO> nodes) {
		this.nodes = nodes;
	}

	public ArrayList<EdgeJDO> getEdges() {
		return edges;
	}

	public void setEdges(ArrayList<EdgeJDO> edges) {
		this.edges = edges;
	}
}
