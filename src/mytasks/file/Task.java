package mytasks.file;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
	private Date mDateTime = null;
	private ArrayList<String> mLabels;
	public static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	public static DateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	
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
		mDateTime = dateTime;
	}
	public ArrayList<String> getLabels() {
		return mLabels;
	}
	
	public void setLabels(ArrayList<String> labels) {
		mLabels = new ArrayList<String>(); // for resetting after every update
		mLabels = labels;
	}
	
	public String toString(){
		String dateToString = "";
		if (mDateTime != null){			
			dateToString = dateTimeFormat.format(mDateTime);
		}
		if (dateToString.contains("00:00")){
			dateToString = dateFormat.format(mDateTime);
		}
		
		String labelsToString = "";
		if (mLabels!= null) {
			for (String s : mLabels){
				labelsToString += "#" + s + " ";
			}
		}
		return String.format("%s %s %s", mDescription, dateToString, labelsToString).trim();
	}
}
