package microlabs.dst.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SerialNode implements IsSerializable
{

	private int top;
	private int left;
	private char value;
	
	private SerialNode(){}
	
	/**
	 * @param top
	 * @param left
	 * @param value
	 */
	public SerialNode(char value, int top, int left) 
	{
		this.top = top;
		this.left = left;
		this.value = value;
	}

	/**
	 * @return the top
	 */
	public int getTop() {
		return top;
	}

	/**
	 * @param top the top to set
	 */
	public void setTop(int top) {
		this.top = top;
	}

	/**
	 * @return the left
	 */
	public int getLeft() {
		return left;
	}

	/**
	 * @param left the left to set
	 */
	public void setLeft(int left) {
		this.left = left;
	}

	/**
	 * @return the value
	 */
	public char getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(char value) {
		this.value = value;
	}
}
