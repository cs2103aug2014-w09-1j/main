package mytasks.file;

import java.util.ArrayList;
import java.util.Date;

/**
 * Task object represents a single task with all the relevant fields that is essential to a task
 * @author Wilson
 *
 */
public class Task {
	
	//Private local variables
	private String mDescription;
	private Date mDateTime;
	private ArrayList<String> mLabels;
	
	//Constructor
	public Task(String details, Date dateTime, ArrayList<String> labels) {
		mDescription = details;
		mDateTime = dateTime;
		mLabels = labels;
	}
	
	public String getDescription() {
		return mDescription;
	}
	
	public void setDescription(String updateDesc) {
		mDescription = updateDesc;
	}
	
	public Date getDateTime() {
		return mDateTime;
	}
	
	public void setDateTime(Date dateTime) {
		mDateTime = new Date(); 
		mDateTime = dateTime;
	}
	public ArrayList<String> getLabels() {
		return mLabels;
	}
	
	public void setLabels(ArrayList<String> labels) {
		mLabels.clear(); // for resetting after every update
		mLabels = labels;
	}
}
