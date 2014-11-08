package mytasks.logic.command;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;
import mytasks.logic.LocalMemory;

//@author A0112139R
/**
 * SearchCommand extends Command object to follow OOP standards
 */
public class SearchCommand extends Command {
	
	private LocalMemory mLocalMem;
	private static String MESSAGE_SEARCH_FAIL = "unable to find task with keyword '%1$s'";
	private static String MESSAGE_SEARCH_SUCCESS = "task(s) with keyword '%1$s' searched";

	public SearchCommand(String comdDes, Date fromDateTime,
			Date toDateTime, ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mLocalMem = LocalMemory.getInstance();
	}

	@Override
	public FeedbackObject execute() {
		String searchedTasks = mLocalMem.search(super.getTask());
		FeedbackObject result = null;
		if (!searchedTasks.isEmpty()) {
			searchedTasks += String.format(MESSAGE_SEARCH_SUCCESS, super.getTask());
			result = new FeedbackObject(searchedTasks,true);
			haveSearched = true;
		} else {
			searchedTasks += String.format(MESSAGE_SEARCH_FAIL, super.getTask());
			result = new FeedbackObject(searchedTasks,false);
			haveSearched = false;
		}
		return result;
	}

	@Override
	public FeedbackObject undo() {
		throw new UnsupportedOperationException("Search does not have an undo function");
	}
}
