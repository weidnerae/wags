package webEditor;

public interface ProxyFacilitator {
	public void handleSubjects(String[] subjects);
	public void handleGroups(String[] groups);
	public void handleExercises(String[] exercises);
	
	public void setExercises(String[] exercises);
	public void setCallback(String[] exercises, int status);
	public void getCallback(String[] exercises, int status);
}
