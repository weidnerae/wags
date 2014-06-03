package webEditor;

public interface Reviewer extends Receiver{
	final String GET_STATUS = "status";
	final String GET_REVIEW = "getReview";
	final String LOGICAL = "dst";
	final String MAGNET = "magnet";
	final String NONE = "";
	
	public void review(String name);
	public void reviewCallback(String[] list);
}
