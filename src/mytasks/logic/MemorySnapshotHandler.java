package mytasks.logic;

import java.util.ArrayList;
import java.util.Calendar;
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
			Date currentDate = snapshotList.get(i).getFromDateTime();
			Date nextDate = null;
			if (i != snapshotList.size()-1){
				nextDate = snapshotList.get(i+1).getFromDateTime();
			}
			String FromDateTimeToString = "";

			if (snapshotList.get(i).getFromDateTime() != null){
				FromDateTimeToString = MyTasksParser.dateFormats.get(2).format(snapshotList.get(i).getFromDateTime());
				if (!snapshot.contains(FromDateTimeToString)){
					snapshot += FromDateTimeToString + "\n";
				}
				if(snapshotList.get(i).getToDateTime() != null){
					timedTasks.add(snapshotList.get(i));
				}
				result = taskToStringWithoutDate(snapshotList.get(i));
			}
			else{
				if (!snapshot.contains("N.A.")){
					snapshot += "N.A." + "\n";
				}
			}
			
			snapshot += result + "\n";
			snapshot += duplicate(currentDate, nextDate);
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

	private String duplicate(Date currentDate, Date nextDate){
		String duplicateTasks = "";
		if (currentDate == null){
			return duplicateTasks;
		}

		for (int i=0; i < timedTasks.size(); i++){
			if (timedTasks.get(i).getFromDateTime().compareTo(currentDate) < 0 && timedTasks.get(i).getToDateTime().compareTo(currentDate) >= 0){
				duplicateTasks += taskToStringWithoutDate(timedTasks.get(i)) + "\n";
				if (nextDate != null){			
					Date date = currentDate;
					String dateToString = MyTasksParser.dateFormats.get(2).format(currentDate);
					String nextDateToString = MyTasksParser.dateFormats.get(2).format(nextDate);

					while (!dateToString.equals(nextDateToString)){
						date = incrementDate(date);
						if (timedTasks.get(i).getFromDateTime().compareTo(date) < 0 && timedTasks.get(i).getToDateTime().compareTo(date) >= 0){
							String FromDateTimeToString = MyTasksParser.dateFormats.get(2).format(date);
							duplicateTasks += FromDateTimeToString + "\n" + taskToStringWithoutDate(timedTasks.get(i)) + "\n";
						}
						dateToString = MyTasksParser.dateFormats.get(2).format(date);
					}
				}
			}
		}

		return duplicateTasks;
	}

	private Date incrementDate(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}
}
