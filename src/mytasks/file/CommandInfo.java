package mytasks.file;

import java.util.ArrayList;

/**
 * CommandType instance used to access different fields of a command that has been parsed.
 * 
 * TODO research NLP and its relevant libraries. This object may no longer be needed but will be left
 * here for the time being
 * @author Wilson
 *
 */
public class CommandInfo {
	
	//Private variables
	private String mType;
	private Task mTask;
	
	//Constructor
	public CommandInfo(String comdType, String comdDes, String dateTime, ArrayList<String> comdLabels) {
		mType = comdType;
		Task thisTask = new Task(comdDes, dateTime, comdLabels);
		mTask = thisTask;
	}
	
	public String getType() {
		return mType;
	}
	
	public Task getTask() {
		return mTask;
	}
	
}
