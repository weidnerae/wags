package webEditor;

public interface ProxyFacilitator extends Receiver {
	public void handleSubjects(String[] subjects);
	public void handleGroups(String[] groups);
	public void handleExercises(String[] exercises);
	
	public void setExercises(String[] exercises);
	public void setCallback(String[] exercises, WEStatus status);
}
