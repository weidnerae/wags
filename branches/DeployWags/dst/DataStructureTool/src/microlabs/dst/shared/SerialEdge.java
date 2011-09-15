package microlabs.dst.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SerialEdge implements IsSerializable
{	
	private char n1;
	private char n2;
	
	private SerialEdge(){}
	
	public SerialEdge(char n1, char n2)
	{
		this.n1 = n1;
		this.n2 = n2;
	}

	/**
	 * @return the n1
	 */
	public char getN1() {
		return n1;
	}

	/**
	 * @param n1 the n1 to set
	 */
	public void setN1(char n1) {
		this.n1 = n1;
	}

	/**
	 * @return the n2
	 */
	public char getN2() {
		return n2;
	}

	/**
	 * @param n2 the n2 to set
	 */
	public void setN2(char n2) {
		this.n2 = n2;
	}
}
