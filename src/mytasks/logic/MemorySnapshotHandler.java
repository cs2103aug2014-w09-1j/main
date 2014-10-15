package mytasks.logic;

import java.util.ArrayList;
import java.util.Collections;

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
		ArrayList<Task> snapshotList = new ArrayList<Task>();
		snapshotList.addAll(Collections.nCopies(LocalMem.getLocalMem().size(), new Task(null, null, null, null)));

		for (int i = 0; i < LocalMem.getLocalMem().size(); i++){
			int rank = 0;
			for (int j = 0; j < LocalMem.getLocalMem().size(); j++){
				if ((LocalMem.getLocalMem().get(i).getFromDateTime() == null && LocalMem.getLocalMem().get(j).getFromDateTime() != null) 
						|| LocalMem.getLocalMem().get(i).getFromDateTime() == null && LocalMem.getLocalMem().get(j).getFromDateTime() == null && i > j
						|| LocalMem.getLocalMem().get(i).getFromDateTime() != null && LocalMem.getLocalMem().get(j).getFromDateTime() != null && 
						LocalMem.getLocalMem().get(i).getFromDateTime().compareTo(LocalMem.getLocalMem().get(j).getFromDateTime()) > 0
						|| LocalMem.getLocalMem().get(i).getFromDateTime() != null && LocalMem.getLocalMem().get(j).getFromDateTime() != null &&
						LocalMem.getLocalMem().get(i).getFromDateTime().compareTo(LocalMem.getLocalMem().get(j).getFromDateTime()) == 0 && 
						LocalMem.getLocalMem().get(i).getToDateTime() != null && LocalMem.getLocalMem().get(j).getToDateTime() != null &&
						LocalMem.getLocalMem().get(i).getToDateTime().compareTo(LocalMem.getLocalMem().get(j).getToDateTime()) > 0
						|| LocalMem.getLocalMem().get(i).getFromDateTime() != null && LocalMem.getLocalMem().get(j).getFromDateTime() != null && 
					    LocalMem.getLocalMem().get(i).getFromDateTime().compareTo(LocalMem.getLocalMem().get(j).getFromDateTime()) == 0 &&
					    LocalMem.getLocalMem().get(i).getToDateTime() == null && LocalMem.getLocalMem().get(j).getToDateTime() == null && i > j){
					rank++;
				}
			}
			
			snapshotList.set(rank, LocalMem.getLocalMem().get(i));
		}
		return convertSnapshotToString(snapshotList);
	}

	private String convertSnapshotToString(ArrayList<Task> snapshotList){
		String snapshot = "";
		for (int i=0; i < snapshotList.size(); i++){
			String result = snapshotList.get(i).toString();
			snapshot += result + "\n";
		}	
		return snapshot;
	}

}
