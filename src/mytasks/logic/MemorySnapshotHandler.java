package mytasks.logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

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
	
	//Constructor
	protected MemorySnapshotHandler() {
		currentSettings = MyTasks.DEFAULT_VIEW;
		snapshotList = new ArrayList<Task>();
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
			snapshotList = new ArrayList<Task>();
			for (int i=0; i < LocalMem.getLocalMem().size(); i++){
				snapshotList.add(LocalMem.getLocalMem().get(i));
			}

			timedTaskToNormalTask();		
			return sortByDate();
		}

		return null;
	}

	private String sortByDate(){
		for (int i=0; i < snapshotList.size()-1; i++){
			Date date = snapshotList.get(i).getFromDateTime();
			if (date == null){
				for (int j=i; j < snapshotList.size()-1; j++){
					Task temp = snapshotList.get(j);
					snapshotList.set(j, snapshotList.get(j+1));
					snapshotList.set(j+1, temp);
				}
			}
			else{
				for (int j=0; j < snapshotList.size()-1-i; j++){
					date = snapshotList.get(j).getFromDateTime();
					if (date == null || snapshotList.get(j+1).getFromDateTime() != null && date.after(snapshotList.get(j+1).getFromDateTime())){
						Task temp = snapshotList.get(j);
						snapshotList.set(j, snapshotList.get(j+1));
						snapshotList.set(j+1, temp);
					}
				}
			}
		}
		
		return convertSnapshotListToString(snapshotList);
	}

	private String convertSnapshotListToString(ArrayList<Task> snapshotList){	

		String snapshot = "";

		for (int i=0; i < snapshotList.size(); i++){
			Date date = snapshotList.get(i).getFromDateTime();
			if (date == null){
				snapshot += "N.A.\n";
				for (int j=i; j < snapshotList.size(); j++){
					snapshot += snapshotList.get(j).toString() + "\n";
				}
				break;
			}
			else{
				snapshot += MyTasksParser.dateFormats.get(2).format(date) + "\n";
				int j=i;
				Date currentDate = snapshotList.get(j).getFromDateTime();
				while (currentDate != null && MyTasksParser.dateFormats.get(2).format(currentDate).equals(MyTasksParser.dateFormats.get(2).format(date))){
					snapshot += taskToStringWithoutDate(snapshotList.get(j)) + "\n";
					j++;
					if (j > snapshotList.size()-1)
						break;
					currentDate = snapshotList.get(j).getFromDateTime();
				}
				i = j-1;
			}
		}

		return snapshot;
	}

	private void timedTaskToNormalTask(){	
		for (int i=0; i < snapshotList.size(); i++){
			if (snapshotList.get(i).getFromDateTime() != null && snapshotList.get(i).getToDateTime() != null){
				Date startDate = snapshotList.get(i).getFromDateTime();
				Date endDate = snapshotList.get(i).getToDateTime();
				Date date = startDate;
				while (!date.equals(endDate)){
					date = incrementDate(date);
					snapshotList.add(new Task(snapshotList.get(i).getDescription(), date, null, snapshotList.get(i).getLabels()));
				}
				snapshotList.set(i, new Task(snapshotList.get(i).getDescription(), startDate, null, snapshotList.get(i).getLabels()));
			}
		}
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

	private Date incrementDate(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}
}
