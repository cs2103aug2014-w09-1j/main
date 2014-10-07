package mytasks.logic;

import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import mytasks.file.MyTasks;

/**
 * MemorySnapshotHandler organizes the memory into a format that is readable by UI to display to user.
 * It is required to be able to categorize by labels that is listed in the currentSettings
 * @author Wilson
 *
 */
public class MemorySnapshotHandler {
	
	private String[] currentSettings;
	private static ArrayList<String> month = new ArrayList<String>();
	
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
		
		for (int i=0; i < LocalMem.getLocalMem().size(); i++){
			String result = LocalMem.getLocalMem().get(i).toString();
			snapshot += result + "\n";
		}	
		return snapshot;
	}
	
}
