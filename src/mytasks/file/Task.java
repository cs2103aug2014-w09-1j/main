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
	private Date mFromDateTime = null;
	private Date mToDateTime = null;
	private ArrayList<String> mLabels;
	public static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	public static DateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	
	//Constructor
	public Task(String details, Date fromDateTime, Date toDateTime, ArrayList<String> labels) {
		mDescription = details;
		mFromDateTime = fromDateTime;
		mToDateTime = toDateTime;
		mLabels = labels;
	}
	
	public String getDescription() {
		return mDescription;
	}
	
	public void setDescription(String updateDesc) {
		mDescription = updateDesc;
	}
	
	public Date getFromDateTime() {
		return mFromDateTime;
	}
	
	public Date getToDateTime() {
		return mToDateTime;
	}
	
	public void setFromDateTime(Date dateTime) {
		mFromDateTime = dateTime;
	}
	
	public void setToDateTime(Date dateTime) {
		mToDateTime = dateTime;
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
		if (mFromDateTime != null){			
			dateToString = dateTimeFormat.format(mFromDateTime);
		}
		if (dateToString.contains("00:00")){
			dateToString = dateFormat.format(mFromDateTime);
		}
		if (mToDateTime != null){			
			dateToString = dateTimeFormat.format(mToDateTime);
		}
		if (dateToString.contains("00:00")){
			dateToString = dateFormat.format(mToDateTime);
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
