package mytasks.logic;

import java.util.ArrayList;

import java.util.Date;

/**
 * SearchCommand extends Command object to follow OOP standards
 * 
 * @author Wilson
 *
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
	String execute() {
		String searchedTasks = mLocalMem.search(super.getTask());
		if (!searchedTasks.isEmpty()) {
			searchedTasks += String.format(MESSAGE_SEARCH_SUCCESS, super.getTask());
			super.haveSearched = true;
		} else {
			searchedTasks += String.format(MESSAGE_SEARCH_FAIL, super.getTask());
			super.haveSearched = false;
		}
		return searchedTasks;
	}

	@Override
	String undo() {
		throw new UnsupportedOperationException("Search does not have an undo function");
	}
}
