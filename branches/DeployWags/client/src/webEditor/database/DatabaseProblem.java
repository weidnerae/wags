package webEditor.database;

public class DatabaseProblem {


	int id;
	String title, directions,correct_query;
	public DatabaseProblem(String title, String directions, String correct_query) {
		this.title = title;
		this.directions = directions;
		this.correct_query = correct_query;
	}
}
