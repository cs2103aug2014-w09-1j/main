package mytasks.logic.command;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;
import mytasks.file.Task;

//@author A0114302A
/**
 * CommandType instance used to access different fields of a command that has
 * been parsed.
 */
public abstract class Command {

	private Task mTask;
	private String mToUpdateTaskDesc;
	protected static ArrayList<Integer> searchList;
	protected static boolean hasSearched;
	protected static boolean isRedo;
	
	public final static String MESSAGE_NOTASK = "Unexpected Error: No Task Found to undo";

	// Constructor
	public Command(String comdDes, Date fromDateTime, Date toDateTime,
			ArrayList<String> comdLabels, String updateDesc) {
		Task thisTask = new Task(comdDes, fromDateTime, toDateTime, comdLabels);
		mTask = thisTask;
		mToUpdateTaskDesc = updateDesc;
		Command.isRedo = false;
	}
	
	public Command(String comdDes, Date fromDateTime, Date toDateTime,
			ArrayList<String> comdLabels, String updateDesc, boolean isRedo) {
		Task thisTask = new Task(comdDes, fromDateTime, toDateTime, comdLabels);
		mTask = thisTask;
		mToUpdateTaskDesc = updateDesc;
		Command.isRedo = isRedo;
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

	public abstract FeedbackObject execute();

	public abstract FeedbackObject undo();
}
