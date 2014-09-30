package mytasks.logic;

import java.util.ArrayList;

import mytasks.file.MyTasks;
import mytasks.file.Task;
import mytasks.parser.MyTasksParser;

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
	public String getSnapshot(LocalMemory LocalMem) {
		String snapshot = "";
		
		//Temporary fix 30Sep14. TODO fix it.
		if (LocalMem.getLocalMem()!= null) {
			for (int i=0; i < LocalMem.getLocalMem().size(); i++){
				Task currentTask = LocalMem.getLocalMem().get(i);
				String taskDesc = currentTask.getDescription();
				String dateTime = "";
				if (currentTask.getDateTime()!= null){
					dateTime = MyTasksParser.dateTimeFormat.format(currentTask.getDateTime());
				}
				ArrayList<String> labels = currentTask.getLabels();
				String temp = "";
				if (labels!= null) {
					for (int j = 0; j<labels.size(); j++) {
						temp += "#" + labels.get(j);
					}
				}
				snapshot += taskDesc + " " + dateTime + " " + temp + "\n";
			}
		}
		return snapshot;
	}
	
}
