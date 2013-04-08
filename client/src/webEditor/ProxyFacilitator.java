package webEditor;

public interface ProxyFacilitator {
	final String GET_STATUS = "status";
	final String GET_REVIEW = "getReview";
	final String LOGICAL = "dst";
	final String MAGNET = "magnet";
	
	public void handleSubjects(String[] subjects);
	public void handleGroups(String[] groups);
	public void handleExercises(String[] exercises);
	
	public void setExercises(String[] exercises);
	public void setCallback(String[] exercises, int status);
	public void getCallback(String[] exercises, int status, String request);
	
	public void reviewExercise(String exercise);
	public void reviewCallback(String[] data);
}
