package mytasks.logic;

import mytasks.file.MyTasks;

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
	
	public void setView(String[] newSettings) {
		currentSettings = newSettings;
	}
	
	/**
	 * getSnapshot takes looks at local memory and organizes it according to currentSettings. 
	 * @return data structure that is read and printed by UI
	 */
	public String getSnapshot() {
		//TODO main body
		return null;
	}
	
}
