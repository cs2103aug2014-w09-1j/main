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
		ArrayList<Task> s = new ArrayList<Task>();
		s.addAll(Collections.nCopies(LocalMem.getLocalMem().size(), new Task(null, null, null, null)));

		for (int i = 0; i < LocalMem.getLocalMem().size(); i++){
			int smaller = 0;
			for (int j = 0; j < LocalMem.getLocalMem().size(); j++){
				if ((LocalMem.getLocalMem().get(i).getFromDateTime() == null && LocalMem.getLocalMem().get(j).getFromDateTime() != null) ||
						(LocalMem.getLocalMem().get(j).getFromDateTime() != null && 
						LocalMem.getLocalMem().get(i).getFromDateTime().compareTo(LocalMem.getLocalMem().get(j).getFromDateTime()) > 0)){
					smaller++;
				}
			}
			s.set(smaller, LocalMem.getLocalMem().get(i));
		}
		return convertSnapshotToString(s);
	}


	private String convertSnapshotToString(ArrayList<Task> s){
		String snapshot = "";
		for (int i=0; i < s.size(); i++){
			String result = s.get(i).toString();
			snapshot += result + "\n";
		}	
		return snapshot;
	}

}
