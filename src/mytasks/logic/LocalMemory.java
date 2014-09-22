package mytasks.logic;

import java.util.ArrayList;

import mytasks.file.MyTasks;
import mytasks.file.Task;
import mytasks.storage.MyTasksStorage;

/**
 * LocalMemory holds all related task information in the current session and is only required to read and write
 * to external memory minimally.
 * @author Wilson
 *
 */
public class LocalMemory {
	
	//Private variables
	private ArrayList<Task> mLocalMem;
	private MyTasksStorage mStore;
	
	//Constructor
	public LocalMemory() {
		mLocalMem = new ArrayList<Task>();
	}
	
	public void loadLocalMemory() {
		mStore.readExtMem(MyTasks.DEFAULT_FILENAME);
	}
	
	public void saveLocalMemory() {
		mStore.writeExtMem(mLocalMem);
	}
	
	//TODO add return strings
	public String add(Task userRequest) {
		mLocalMem.add(userRequest);
		return "";
	}
	
	//TODO add return strings
	public String remove(Task userRequest) {
		//TODO insert body. NOTE: you can't simply use remove since they are different objects.
		return "";
	}
	
	//TODO add more functionality
}
