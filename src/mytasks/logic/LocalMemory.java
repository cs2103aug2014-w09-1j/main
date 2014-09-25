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
	
	public ArrayList<Task> getLocalMem() {
		return mLocalMem;
	}
	
	public void add(Task userRequest) {
		mLocalMem.add(userRequest);
	}
	
	public void remove(Task userRequest) {
		for(int i = 0; i < mLocalMem.size(); i++) {
			if(userRequest.getDescription().equals(mLocalMem.get(i).getDescription())) {
				mLocalMem.remove(i);
			}
		}
	}
	
	public void update(String mToUpdateTaskDesc, Task userUpdate) {
		for(int i = 0; i < mLocalMem.size(); i++) {
			if(mToUpdateTaskDesc.equals(mLocalMem.get(i).getDescription())) {
				mLocalMem.get(i).setDescription(userUpdate.getDescription());				
				if(!userUpdate.getLabels().isEmpty()) {
					mLocalMem.get(i).setLabels(userUpdate.getLabels()); 
				}
			} 
		}
	}	
	//TODO add more functionality
}
