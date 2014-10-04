package mytasks.file;

import java.util.ArrayList;
import java.util.Date;

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
	public enum CommandType {ADD, DELETE, SORT, SEARCH, UPDATE, UNDO, REDO, INVALID};
	private CommandType mType;
	private Task mTask;
	//Variable used to store task description of task to be updated (if any)
	private String mToUpdateTaskDesc;
	
	//Constructor
	public CommandInfo(String comdType, String comdDes, Date dateTime, ArrayList<String> comdLabels, String updateDesc) {
		determineCommandType(comdType);
		Task thisTask = new Task(comdDes, dateTime, comdLabels);
		mTask = thisTask;
		mToUpdateTaskDesc = updateDesc;
	}

	private void determineCommandType(String comdType) {
		if (comdType.equals("add")) {
			mType = CommandType.ADD;
		} else if (comdType.equals("delete")) {
			mType = CommandType.DELETE;
		} else if (comdType.equals("sort")) {
			mType = CommandType.SORT;
		} else if (comdType.equals("search")) {
			mType = CommandType.SEARCH;
		} else if (comdType.equals("update")) {
			mType = CommandType.UPDATE;
		} else if (comdType.equals("undo")) {
			mType = CommandType.UNDO;
		} else if (comdType.equals("redo")) {
			mType = CommandType.REDO;
		} else {
			mType = CommandType.INVALID;
		}
	}
	
	public CommandType getType() {
		return mType;
	}
	
	public Task getTask() {
		return mTask;
	}
	
	/**
	 * getToUpdateTaskDesc is only used for update commands. Is null for all other command
	 * types.
	 * @return String of the task description of the task to be updated || old task 
	 */
	public String getToUpdateTaskDesc() {
		return mToUpdateTaskDesc;
	}
}
