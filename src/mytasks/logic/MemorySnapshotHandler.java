package mytasks.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import mytasks.file.MyTasks;
import mytasks.file.Task;
import mytasks.parser.MyTasksParser;

/**
 * MemorySnapshotHandler organizes the memory into a format that is readable by UI to display to user.
 * It is required to be able to categorize by labels that is listed in the currentSettings
 * @author Wilson
 *
 */
class MemorySnapshotHandler {
	
	private String[] currentSettings;
	private ArrayList<Task> snapshotList;
	private ArrayList<Task> timedTasks;
	
	//Constructor
	protected MemorySnapshotHandler() {
		currentSettings = MyTasks.DEFAULT_VIEW;
		snapshotList = new ArrayList<Task>();
		timedTasks = new ArrayList<Task>();
		assert currentSettings[0] == "date" : "wrong default setting";
	}
	
	protected void setView(ArrayList<String> newSettings) {
		currentSettings = newSettings.toArray((new String[newSettings.size()]));
		assert currentSettings != null : "invalid setting";
	}

	/**
	 * getSnapshot takes looks at local memory and organizes it according to currentSettings. 
	 * @return data structure that is read and printed by UI
	 */
	public String getSnapshot(LocalMemory LocalMem) {
		assert currentSettings != null : "invalid setting";
		
		/*
		sortByDate(LocalMem);
		String s = "";
		for (int i=0; i < snapshotList.size(); i++){
			s += snapshotList.get(i).toString() + "\n";
		}
		*/
		
		if (currentSettings[0].equals("date")){
			return sortByDate(LocalMem);
		}

		return null;
	}

	private String sortByDate(LocalMemory LocalMem){
		snapshotList = new ArrayList<Task>();
		snapshotList.addAll(Collections.nCopies(LocalMem.getLocalMem().size(), new Task(null, null, null, null)));
		timedTasks = new ArrayList<Task>();

		assert snapshotList.size() == LocalMem.getLocalMem().size();

		for (int i = 0; i < LocalMem.getLocalMem().size(); i++){
			int rank = 0;

			for (int j = 0; j < LocalMem.getLocalMem().size(); j++){
				if (LocalMem.getLocalMem().get(i).getFromDateTime() == null){
					if (LocalMem.getLocalMem().get(j).getFromDateTime() != null || LocalMem.getLocalMem().get(j).getFromDateTime() == null && i > j){
						rank++;
					}
				}
				else if (LocalMem.getLocalMem().get(j).getFromDateTime() != null){
					if (LocalMem.getLocalMem().get(i).getFromDateTime().compareTo(LocalMem.getLocalMem().get(j).getFromDateTime()) > 0){
						rank++;
					}
					else if (LocalMem.getLocalMem().get(i).getFromDateTime().compareTo(LocalMem.getLocalMem().get(j).getFromDateTime()) == 0){
						if ( LocalMem.getLocalMem().get(i).getToDateTime() == null && LocalMem.getLocalMem().get(j).getToDateTime() == null && i > j){
							rank ++;
						}
						else if (LocalMem.getLocalMem().get(i).getToDateTime() != null && LocalMem.getLocalMem().get(j).getToDateTime() != null && 
								LocalMem.getLocalMem().get(i).getToDateTime().compareTo(LocalMem.getLocalMem().get(j).getToDateTime()) > 0){
							rank++;
						}
					}
				}

				
			}
			assert rank < LocalMem.getLocalMem().size() : "rank out of bound";
			
			snapshotList.set(rank, LocalMem.getLocalMem().get(i));
		}
		return convertSnapshotToString(snapshotList);
	}

	private String convertSnapshotToString(ArrayList<Task> snapshotList){
		
		String snapshot = "";
		for (int i=0; i < snapshotList.size(); i++){
			String result = snapshotList.get(i).toString();
			String FromDateTimeToString = "";

			if (snapshotList.get(i).getFromDateTime() != null){
				FromDateTimeToString = MyTasksParser.dateFormats.get(2).format(snapshotList.get(i).getFromDateTime());
				if (!snapshot.contains(FromDateTimeToString)){
					snapshot += FromDateTimeToString + "\n";
				}

				result = taskToStringWithoutDate(snapshotList.get(i));
			}
			else{
				if (!snapshot.contains("N.A.")){
					snapshot += "N.A." + "\n";
				}
			}

			snapshot += result + "\n";
		}	
		return snapshot;
	}
	
	private String taskToStringWithoutDate(Task task){
		String labelsToString = "";
		if (task.getLabels() != null) {
			for (String s : task.getLabels()){
				labelsToString += " " + "#" + s;
			}
		}
		
		String timeToString = getTime(task);
		
		if (timeToString.equals("")){
			return String.format("%s%s", task.getDescription(), labelsToString);
		}

		return String.format("%s %s%s", task.getDescription(), timeToString, labelsToString);
	}
	
	private String getTime(Task task){		
		String timeToString = "";
		if (!MyTasksParser.dateFormats.get(4).format(task.getFromDateTime()).equals("00:00")){
			timeToString += MyTasksParser.dateFormats.get(4).format(task.getFromDateTime());
		}
		if (task.getToDateTime() != null && !MyTasksParser.dateFormats.get(4).format(task.getToDateTime()).equals("00:00")){
			timeToString += MyTasksParser.dateFormats.get(4).format(task.getToDateTime());
		}
		
		return timeToString;
	}

	private String duplicate(Date prevDate, Date currentDate){
		String duplicateTasks = "";
		for (int i=0; i < timedTasks.size(); i++){
			if (currentDate == null){
				continue;
			}
			if (timedTasks.get(i).getFromDateTime().compareTo(currentDate) < 0 && timedTasks.get(i).getToDateTime().compareTo(currentDate) >= 0){
				duplicateTasks += timedTasks.get(i) + "\n";
			}
		}
		
		return duplicateTasks;
	}
}
