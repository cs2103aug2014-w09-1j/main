package mytasks.logic.command;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;
import mytasks.file.Task;
import mytasks.logic.controller.LocalMemory;

//@author A0112139R
/**
 * SearchCommand extends Command object to follow OOP standards
 */
public class SearchCommand extends Command {
	
	private LocalMemory mLocalMem;
	private static String MESSAGE_SEARCH_FAIL = "unable to find task with keyword '%1$s'";
	private static String MESSAGE_SEARCH_SUCCESS = "task(s) with keyword '%1$s' searched";
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MMM.yyyy");

	public SearchCommand(String comdDes, Date fromDateTime,
			Date toDateTime, ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mLocalMem = LocalMemory.getInstance();
	}

	@Override
	public FeedbackObject execute() {
		String searchedTasks = search(super.getTask());
		FeedbackObject result = null;
		if (!searchedTasks.isEmpty()) {
			searchedTasks += String.format(MESSAGE_SEARCH_SUCCESS, super.getTask());
			result = new FeedbackObject(searchedTasks,true);
			hasSearched = true;
		} else {
			searchedTasks += String.format(MESSAGE_SEARCH_FAIL, super.getTask());
			result = new FeedbackObject(searchedTasks,false);
			hasSearched = false;
		}
		return result;
	}

	@Override
	public FeedbackObject undo() {
		throw new UnsupportedOperationException("Search does not have an undo function");
	}
	
	private String search(Task userRequest) {
		String searchedTasks = "";
		searchList = new ArrayList<Integer>();
		String[] keywords = null;
		
		if (userRequest.getDescription() != null && !userRequest.getDescription().equals("")){
			keywords = userRequest.getDescription().split("\\s+");	
		}

		for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
			Task currentTask = mLocalMem.getLocalMem().get(i);
			if (haveSameDesc(keywords, currentTask)) {
				searchList.add(i);
				searchedTasks += searchList.size() + ". " + currentTask.toString() + "\n";
			}
			else if (haveSameLabels(keywords, currentTask)) {
				searchList.add(i);
				searchedTasks += searchList.size() + ". " + currentTask.toString() + "\n";
			}
			else if (isBetweenStartDateAndEndDate(userRequest.getFromDateTime(), userRequest.getToDateTime(), currentTask)){
				searchList.add(i);
				searchedTasks += searchList.size() + ". " + currentTask.toString() + "\n";
			}
			else if (haveSameDate(userRequest.getFromDateTime(), currentTask)){
				searchList.add(i);
				searchedTasks += searchList.size() + ". " + currentTask.toString() + "\n";
			}
		}

		return searchedTasks;
	}

	private boolean haveSameDesc(String[] keywords, Task currentTask) {
		if (keywords == null || currentTask.getDescription() == null){
			return false;
		}
		
		for (int i=0; i < keywords.length; i++){
			String desc = keywords[i];

			if (currentTask.getDescription().toLowerCase().contains(desc.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	private boolean haveSameLabels(String[] keywords, Task currentTask) {
		if (keywords == null || currentTask.getLabels() == null){
			return false;
		}
		
		for (int i=0; i < keywords.length; i++){
			String desc = keywords[i];

			for (int j=0; j < currentTask.getLabels().size(); j++){
				if (currentTask.getLabels().get(j).toLowerCase().contains(desc.toLowerCase())){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isBetweenStartDateAndEndDate(Date fromDateTime, Date toDateTime, Task currentTask) {
		if (toDateTime == null || currentTask.getFromDateTime() == null){
			return false;
		}

		if (currentTask.getToDateTime() == null){
			if (fromDateTime.compareTo(currentTask.getFromDateTime()) <= 0 && toDateTime.compareTo(currentTask.getFromDateTime()) >= 0){
				return true;
			}
		}
		else{
			if (fromDateTime.compareTo(currentTask.getFromDateTime()) <= 0 && toDateTime.compareTo(currentTask.getFromDateTime()) >= 0 || 
					fromDateTime.compareTo(currentTask.getToDateTime()) <= 0 && toDateTime.compareTo(currentTask.getToDateTime()) >= 0){
				return true;
			}
		}

		return false;
	}
	
	private boolean haveSameDate(Date fromDateTime, Task currentTask) {
		if (fromDateTime == null || currentTask.getFromDateTime() == null){
			return false;
		}
		
		if (currentTask.getToDateTime() == null){
			if (dateFormat.format(fromDateTime).equals(dateFormat.format(currentTask.getFromDateTime()))){
				return true;
			}
		}
		else{
			if (currentTask.getFromDateTime().compareTo(fromDateTime) <= 0 && currentTask.getToDateTime().compareTo(fromDateTime) >= 0){ 
				return true;
			}
		}
		
		return false;
	}
}
