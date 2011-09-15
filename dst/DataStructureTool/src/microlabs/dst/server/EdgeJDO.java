package microlabs.dst.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import microlabs.dst.client.Node;
import microlabs.dst.shared.SerialNode;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class EdgeJDO {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private char n1;
	
	@Persistent
	private char n2;
	
	public EdgeJDO(char n1, char n2)
	{
		this.n1 = n1;
		this.n2 = n2;
	}

	/**
	 * @return the key
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * @return the node1
	 */
	public char getN1() {
		return n1;
	}

	/**
	 * @param node1 the node1 to set
	 */
	public void setNode1(char node1) {
		this.n1 = node1;
	}

	/**
	 * @return the node2
	 */
	public char getN2() {
		return n2;
	}

	/**
	 * @param node2 the node2 to set
	 */
	public void setNode2(char node2) {
		this.n2 = node2;
	}

		
}
