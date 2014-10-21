package mytasks.logic;

import java.text.ParseException;
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
 * @author Michael
 *
 */
class MemorySnapshotHandler {
	
	private String[] currentSettings;
	private ArrayList<Task> snapshotList;
	private ArrayList<String> labelsInSortedOrder;
	
	//Constructor
	protected MemorySnapshotHandler() {
		currentSettings = MyTasks.DEFAULT_VIEW;
		assert currentSettings[0] == "date" : "wrong default setting";
	}
	
	protected void setView(ArrayList<String> newSettings) {
		currentSettings = newSettings.toArray((new String[newSettings.size()]));
		assert currentSettings != null : "invalid setting";
	}
	
	private void initSnapshotList(LocalMemory LocalMem){
		snapshotList = new ArrayList<Task>();
		for (int i=0; i < LocalMem.getLocalMem().size(); i++){
			snapshotList.add(LocalMem.getLocalMem().get(i));
		}
		labelsInSortedOrder = new ArrayList<String>();
	}

	/**
	 * getSnapshot takes looks at local memory and organizes it according to currentSettings. 
	 * @return data structure that is read and printed by UI
	 */
	public String getSnapshot(LocalMemory LocalMem) {
		assert currentSettings != null : "invalid setting";
		initSnapshotList(LocalMem);
		assert !snapshotList.isEmpty() : "initialize fail";  
		
		for (int i=0; i < currentSettings.length; i++){
			if (currentSettings[i].equals("date")){
				timedTaskToNormalTask();		
				sortByDate();
			}
			else{
				labelsInSortedOrder.add(currentSettings[i]);
			}
		}
		
		if (!labelsInSortedOrder.isEmpty()){
			sortByLabels();
			
			String snapshot = "";
			for (int i=0; i < snapshotList.size(); i++){
				snapshot += snapshotList.get(i).toString() + "\n";
			}
			return snapshot;
		}

		return convertSnapshotListToString(snapshotList);
	}

	private void sortByDate(){
		for (int i=0; i < snapshotList.size()-1; i++){
			for (int j=0; j < snapshotList.size()-1-i; j++){
				Date currentDate = snapshotList.get(j).getFromDateTime();
				Date nextDate = snapshotList.get(j+1).getFromDateTime();
				if (currentDate == null && nextDate != null || nextDate != null && currentDate.after(nextDate)){
					Task temp = snapshotList.get(j);
					snapshotList.set(j, snapshotList.get(j+1));
					snapshotList.set(j+1, temp);
				}
			}
		}
	}
	
	private void sortByLabels(){
		for (int i=0; i < snapshotList.size()-1; i++){
			for (int j=0; j < snapshotList.size()-1-i; j++){
				if (!haveLabels(j) && haveLabels(j+1)){
					Task temp = snapshotList.get(j);
					snapshotList.set(j, snapshotList.get(j+1));
					snapshotList.set(j+1, temp);
				}
			}
		}
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
				while (!MyTasksParser.dateFormats.get(2).format(date).equals(MyTasksParser.dateFormats.get(2).format(endDate))){
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
		assert task.getFromDateTime() != null;
		
		if (!MyTasksParser.dateFormats.get(4).format(task.getFromDateTime()).equals("00:00")){
			timeToString += MyTasksParser.dateFormats.get(4).format(task.getFromDateTime());
		}
		if (task.getToDateTime() != null && !MyTasksParser.dateFormats.get(4).format(task.getToDateTime()).equals("00:00")){
			timeToString += MyTasksParser.dateFormats.get(4).format(task.getToDateTime());
		}

		return timeToString;
	}

	private boolean haveLabels(int index){
		if (snapshotList.get(index).getLabels() == null){
			return false;
		}
		
		for (int i=0; i < snapshotList.get(index).getLabels().size(); i++){
			for (int j=0; j < labelsInSortedOrder.size(); j++){
				if (snapshotList.get(index).getLabels().get(i).equals(labelsInSortedOrder.get(j))){
					return true;
				}
			}
		}
		
		return false;
	}

	private Date incrementDate(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}
}
