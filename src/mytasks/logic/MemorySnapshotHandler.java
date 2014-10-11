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
class MemorySnapshotHandler {
	
	private String[] currentSettings;
	
	//Constructor
	protected MemorySnapshotHandler() {
		currentSettings = MyTasks.DEFAULT_VIEW;
	}
	
	protected void setView(ArrayList<String> newSettings) {
		currentSettings = (String[]) newSettings.toArray();
	}

	/**
	 * getSnapshot takes looks at local memory and organizes it according to currentSettings. 
	 * @return data structure that is read and printed by UI
	 */
	public String getSnapshot(LocalMemory LocalMem) {
		/*
	protected String getSnapshot(LocalMemory LocalMem) {
		String snapshot = "";

		for (int i=0; i < LocalMem.getLocalMem().size(); i++){
			String result = LocalMem.getLocalMem().get(i).toString();
			snapshot += result + "\n";
		}	
		return snapshot;
		 */

		return sortByDate(LocalMem);
	}

	private String sortByDate(LocalMemory LocalMem){
		String snapshot = "";

		for (int i = 0; i < LocalMem.getLocalMem().size()-1; i++){
			int index = i;
			for (int j=i+1; j < LocalMem.getLocalMem().size(); j++){
				if (LocalMem.getLocalMem().get(i).getFromDateTime() == null && LocalMem.getLocalMem().get(j).getFromDateTime() != null ||
					LocalMem.getLocalMem().get(j).getFromDateTime() != null && 
					LocalMem.getLocalMem().get(j).getFromDateTime().compareTo(LocalMem.getLocalMem().get(i).getFromDateTime()) < 0){
					index = j;
				}				
			}

			Task temp = new Task(LocalMem.getLocalMem().get(index));
			LocalMem.getLocalMem().get(index).setTask(LocalMem.getLocalMem().get(i));
			LocalMem.getLocalMem().get(i).setTask(temp);			
		}

		for (int i=0; i < LocalMem.getLocalMem().size(); i++){
			String result = LocalMem.getLocalMem().get(i).toString();
			snapshot += result + "\n";
		}	

		return snapshot;

	}	
}
