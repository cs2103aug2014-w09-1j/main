package mytasks.logic;

import java.util.ArrayList;

import mytasks.file.MyTasks;
import mytasks.file.Task;

/**
 * MemorySnapshotHandler organizes the memory into a format that is readable by UI to display to user.
 * It is required to be able to categorize by labels that is listed in the currentSettings
 * @author Wilson
 *
 */
public class MemorySnapshotHandler {
	
	private String[] currentSettings;
	
	//Constructor
	public MemorySnapshotHandler() {
		currentSettings = MyTasks.DEFAULT_VIEW;
	}
	
	public void setView(ArrayList<String> newSettings) {
		currentSettings = (String[]) newSettings.toArray();
	}
	
	/**
	 * getSnapshot takes looks at local memory and organizes it according to currentSettings. 
	 * @return data structure that is read and printed by UI
	 */
	public String getSnapshot(LocalMemory mLocalMem) {
		String snapshot = "";
		
		for (int i=0; i < mLocalMem.getLocalMem().size(); i++){
			snapshot += mLocalMem.getLocalMem().get(i).toString() + "\n";
		}
		
		return snapshot;
	}
	
}
