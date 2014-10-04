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
		mStore = new MyTasksStorage();
	}
	
	public void loadLocalMemory() {
		mLocalMem = mStore.readExtMem(MyTasks.DEFAULT_FILENAME);
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
				if(userUpdate.getDescription() != null) {
					mLocalMem.get(i).setDescription(userUpdate.getDescription());	
				}
				if(userUpdate.getDateTime() != null) {
					mLocalMem.get(i).setDateTime(userUpdate.getDateTime());	
				}
				if(userUpdate.getLabels() != null) {
					if(!userUpdate.getLabels().isEmpty()) {
						mLocalMem.get(i).setLabels(userUpdate.getLabels()); 
					}
				}
			} 
		}
	}	
	
	public void sort(Task userRequest){
		
	}
	
	public void print() {
		for(int i = 0; i < mLocalMem.size(); i++) {
			System.out.println("i: " + i);
			if(mLocalMem.get(i).getDescription() != null) {
				System.out.println(mLocalMem.get(i).getDescription());
			}
			if(mLocalMem.get(i).getDateTime()!= null) {
				System.out.println(mLocalMem.get(i).getDateTime().toString());
			}
			if(mLocalMem.get(i).getLabels() != null) {
				if(!mLocalMem.get(i).getLabels().isEmpty()) {
					for(int k = 0; k < mLocalMem.get(i).getLabels().size() ; k++) {
					System.out.println("label: " +  mLocalMem.get(i).getLabels().get(k));
					}
				}
			}			
		}
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
			//TODO: what is the purpose of catch function?
		}
		return false;
	}
	
	//TODO: comments
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
