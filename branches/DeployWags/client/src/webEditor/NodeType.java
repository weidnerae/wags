package webEditor;

public enum NodeType {
	DRAGGABLE ("draggable"),
	CLICKABLE ("clickable"),
	FORCE_EVAL ("clickableforceeval"),
	STRING_DRAGGABLE ("stringdraggable"),
	REDBLACK ("redbalck"),
	NODE ("node");
	
	private String key;
	
	NodeType(String key){
		this.key = key;
	}
	
	public String toString() {
		return key;
	}
}
