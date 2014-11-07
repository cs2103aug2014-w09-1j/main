package mytasks.logic;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;

import mytasks.file.MyTasksController;
import mytasks.file.Task;
import mytasks.storage.IStorage;
import mytasks.storage.MyTasksStorage;

/**
 * LocalMemory holds all related task information in the current session and is
 * only required to read and write
 * to external memory minimally.
 * 
 * @author Huiwen, Michael, Shuan Siang 
 *
 */

@SuppressWarnings("serial")
class LocalMemory implements Serializable {

	// Private variables
	private static LocalMemory INSTANCE = null;
	private ArrayList<Task> mLocalMem = new ArrayList<Task>();
	private Stack<Command> undoStack = new Stack<Command>();
	private Stack<Command> redoStack = new Stack<Command>();
	private IStorage mStore;
	private static ArrayList<Integer> searchList;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MMM.yyyy");

	// Constructor
	private LocalMemory() {
		mStore = MyTasksStorage.getInstance();
	}

	protected static LocalMemory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LocalMemory();
		}
		return INSTANCE;
	}

	protected Object readResolve() {
		return INSTANCE;
	}

	protected void loadLocalMemory() {
		mLocalMem = mStore.readExtMem(MyTasksController.DEFAULT_FILENAME);
	}

	protected void saveLocalMemory() {
		mStore.writeExtMem(mLocalMem);
	}

	protected ArrayList<Task> getLocalMem() {
		return mLocalMem;
	}
	
	protected ArrayList<Integer> getSearchList(){
		return searchList;
	}

	protected void add(Task userRequest) {
		mLocalMem.add(userRequest);
	}

	protected void remove(Task userRequest) {
		for (int i = 0; i < mLocalMem.size(); i++) {
			if (userRequest.getDescription().equals(
							mLocalMem.get(i).getDescription())) {
				mLocalMem.remove(i);
			}
		}
	}
	
	protected void remove(int index) {
		mLocalMem.remove(index);
	}

	protected void update(String mToUpdateTaskDesc, Task userUpdate) {
		for (int i = 0; i < mLocalMem.size(); i++) {
			if (mToUpdateTaskDesc.equals(mLocalMem.get(i).getDescription())) {
				if (userUpdate.getDescription() != null) {
					mLocalMem.get(i)
									.setDescription(userUpdate.getDescription());
				}
				if (userUpdate.getFromDateTime() != null) {
					mLocalMem.get(i).setFromDateTime(
									userUpdate.getFromDateTime());
				}
				if (userUpdate.getToDateTime() != null) {
					mLocalMem.get(i).setToDateTime(userUpdate.getToDateTime());
				}
				if (userUpdate.getLabels() != null) {
					if (!userUpdate.getLabels().isEmpty()) {
						mLocalMem.get(i).setLabels(userUpdate.getLabels());
					}
				}
			}
		}

	}

	protected void print() {
		for (int i = 0; i < mLocalMem.size(); i++) {
			System.out.println("i: " + i);
			if (mLocalMem.get(i).getDescription() != null) {
				System.out.println(mLocalMem.get(i).getDescription());
			}
			if (mLocalMem.get(i).getFromDateTime() != null) {
				System.out.println(mLocalMem.get(i).getFromDateTime()
								.toString());
			}
			if (mLocalMem.get(i).getToDateTime() != null) {
				System.out.println(mLocalMem.get(i).getToDateTime().toString());
			}
			if (mLocalMem.get(i).getLabels() != null) {
				if (!mLocalMem.get(i).getLabels().isEmpty()) {
					for (int k = 0; k < mLocalMem.get(i).getLabels().size(); k++) {
						System.out.println("label: "
										+ mLocalMem.get(i).getLabels().get(k));
					}
				}
			}
		}
	}

	//@author A0112139R
	protected String search(Task userRequest) {
		String searchedTasks = "";
		searchList = new ArrayList<Integer>();
		String[] keywords = null;
		
		if (userRequest.getDescription() != null && !userRequest.getDescription().equals("")){
			keywords = userRequest.getDescription().split("\\s+");	
		}

		for (int i = 0; i < mLocalMem.size(); i++) {
			Task currentTask = mLocalMem.get(i);
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

	//@author A0108543J
	protected void undoPush(Command commandToUndo) {
		undoStack.push(commandToUndo);
	}

	protected Stack<Command> getUndoStack() {
		return undoStack;
	}
	
	protected void redoPush(Command commandToUndo) {
		redoStack.push(commandToUndo);
	}
	
	protected Stack<Command> getRedoStack() {
		return redoStack;
	}

	//Backdoor function to use for testing
	protected void clearMemory() {
		mLocalMem.clear();
	}

}
