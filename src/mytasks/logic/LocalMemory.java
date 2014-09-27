package mytasks.logic;

import java.util.ArrayList;
import java.util.NoSuchElementException;

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
				mLocalMem.get(i).setDateTime(userUpdate.getDateTime());	

				if(!userUpdate.getLabels().isEmpty()) {
					mLocalMem.get(i).setLabels(userUpdate.getLabels()); 
				}
			} 
		}
	}	
	
	public void sort(Task userRequest){
		
		
	}
	
	public boolean search(Task userRequest){

		boolean isFound = false;

		for (int i=0; i < mLocalMem.size(); i++){
			if (haveDesc(userRequest, i) && haveLabels(userRequest, i)){
				System.out.println(mLocalMem.get(i).toString());
				isFound = true;
			}
		}	
		return isFound;
	}

	private boolean haveDesc(Task userRequest, int index){
		try{
			String desc = userRequest.getDescription();

			if (mLocalMem.get(index).getDescription().contains(desc)){
				return true;
			}	
		}catch(NoSuchElementException e){
		}
		return false;
	}

	private boolean haveLabels(Task userRequest, int index){
		String userRequestedTaskLabelsToString = "";
		String mLocalMemTaskLabelsToString = "";
		try{
			for (String s : userRequest.getLabels()){
				userRequestedTaskLabelsToString += s; 
			}
			for (String s : mLocalMem.get(index).getLabels()){
				mLocalMemTaskLabelsToString += s; 
			}
		}catch (NullPointerException e){
		}

		if (mLocalMemTaskLabelsToString.equals(userRequestedTaskLabelsToString)){
			return true;
		}
		return false;
	}
}
