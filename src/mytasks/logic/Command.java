package mytasks.logic;

//@author A0114302A
import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;
import mytasks.file.Task;

/**
 * CommandType instance used to access different fields of a command that has
 * been parsed.
 */
public abstract class Command {

	// Private variables
	private Task mTask;
	// Variable used to store task description of task to be updated (if any)
	private String mToUpdateTaskDesc;
	protected static boolean haveSearched;
	
	public final static String MESSAGE_NOTASK = "Unexpected Error: No Task Found to undo";

	// Constructor
	public Command(String comdDes, Date fromDateTime, Date toDateTime,
			ArrayList<String> comdLabels, String updateDesc) {
		Task thisTask = new Task(comdDes, fromDateTime, toDateTime, comdLabels);
		mTask = thisTask;
		mToUpdateTaskDesc = updateDesc;
	}

	protected String getTaskDetails() {
		return mTask.getDescription();
	}

	public Task getTask() {
		return mTask;
	}

	/**
	 * getToUpdateTaskDesc is only used for update commands. Is null for all
	 * other command types.
	 * 
	 * @return String of the task description of the task to be updated || old
	 *         task
	 */
	public String getToUpdateTaskDesc() {
		return mToUpdateTaskDesc;
	}

	abstract FeedbackObject execute();

	abstract FeedbackObject undo();
}
