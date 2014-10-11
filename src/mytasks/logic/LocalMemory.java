package mytasks.logic;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import mytasks.file.MyTasks;
import mytasks.file.Task;
import mytasks.storage.IStorage;

/**
 * LocalMemory holds all related task information in the current session and is only required to read and write
 * to external memory minimally.
 * @author Wilson
 *
 */

class LocalMemory {
	
	//Private variables
	private ArrayList<Task> mLocalMem = new ArrayList<Task>();
	private IStorage mStore;
	
	//Constructor
	protected LocalMemory(IStorage store) {
		mStore = store;
	}
	
	protected void loadLocalMemory() {
		mLocalMem = mStore.readExtMem(MyTasks.DEFAULT_FILENAME);
	}
	
	protected void saveLocalMemory() {
		mStore.writeExtMem(mLocalMem);
	}
	
	protected ArrayList<Task> getLocalMem() {
		return mLocalMem;
	}
	
	protected void add(Task userRequest) {
		mLocalMem.add(userRequest);
	}
	
	protected void remove(Task userRequest) {
		for(int i = 0; i < mLocalMem.size(); i++) {
			if(userRequest.getDescription().equals(mLocalMem.get(i).getDescription())) {
				mLocalMem.remove(i);
			}
		}
	}

	protected void update(String mToUpdateTaskDesc, Task userUpdate) {
		for(int i = 0; i < mLocalMem.size(); i++) {
			if(mToUpdateTaskDesc.equals(mLocalMem.get(i).getDescription())) {
				if(userUpdate.getDescription() != null) {
					mLocalMem.get(i).setDescription(userUpdate.getDescription());	
				}
				if(userUpdate.getFromDateTime() != null) {
					mLocalMem.get(i).setFromDateTime(userUpdate.getFromDateTime());	
				}
				if(userUpdate.getToDateTime() != null) {
					mLocalMem.get(i).setToDateTime(userUpdate.getToDateTime());	
				}
				if(userUpdate.getLabels() != null) {
					if(!userUpdate.getLabels().isEmpty()) {
						mLocalMem.get(i).setLabels(userUpdate.getLabels()); 
					}
				}
			} 
		}
	}	
	
	protected void sort(Task userRequest){
		
	}
	
	protected void print() {
		for(int i = 0; i < mLocalMem.size(); i++) {
			System.out.println("i: " + i);
			if(mLocalMem.get(i).getDescription() != null) {
				System.out.println(mLocalMem.get(i).getDescription());
			}
			if(mLocalMem.get(i).getFromDateTime()!= null) {
				System.out.println(mLocalMem.get(i).getFromDateTime().toString());
			}
			if(mLocalMem.get(i).getToDateTime()!= null) {
				System.out.println(mLocalMem.get(i).getToDateTime().toString());
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
	
	protected boolean search(Task userRequest){
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
