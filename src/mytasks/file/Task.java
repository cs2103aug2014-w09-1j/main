package mytasks.file;

import java.util.ArrayList;

/**
 * Task object represents a single task with all the relevant fields that is essential to a task
 * @author Wilson
 *
 */
public class Task {
	
	//Private local variables
	private String mDescription;
	private String mDateTime;
	private ArrayList<String> mLabels;
	
	//Constructor
	public Task(String details, String dateTime, ArrayList<String> labels) {
		mDescription = details;
		mDateTime = dateTime;
		mLabels = labels;
	}
	
	public String getDescription() {
		return mDescription;
	}
	
	public String getDateTime() {
		return mDateTime;
	}
	
	public ArrayList<String> getLabels() {
		return mLabels;
	}
}
