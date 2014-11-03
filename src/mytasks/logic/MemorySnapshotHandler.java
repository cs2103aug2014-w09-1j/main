package mytasks.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	
	private static MemorySnapshotHandler INSTANCE = null;
	private String[] currentSettings;
	private ArrayList<Task> snapshotList;
	private ArrayList<String> labelsInSortedOrder;
	private ArrayList<ArrayList<String>> labelCombinations;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MMM.yyyy");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

	
	//Constructor
	private MemorySnapshotHandler() {
		currentSettings = MyTasks.DEFAULT_VIEW;
		assert currentSettings[0] == "date" : "wrong default setting";
	}
	
	protected static MemorySnapshotHandler getInstance(){
		if (INSTANCE == null){
			INSTANCE = new MemorySnapshotHandler();
		}
		return INSTANCE;
	}
	
	protected void setView(ArrayList<String> newSettings) {
		currentSettings = newSettings.toArray((new String[newSettings.size()]));
		assert currentSettings != null : "invalid setting";
	}
	
	protected String[] getView() {
		return currentSettings;
	}
	
	private void initSnapshotHandler(LocalMemory LocalMem){
		snapshotList = new ArrayList<Task>();
		for (int i=0; i < LocalMem.getLocalMem().size(); i++){
			snapshotList.add(LocalMem.getLocalMem().get(i));
		}
		labelsInSortedOrder = new ArrayList<String>();
		labelCombinations = new ArrayList<ArrayList<String>>();
	}

	/**
	 * getSnapshot takes looks at local memory and organizes it according to currentSettings. 
	 * @return data structure that is read and printed by UI
	 */
	public ArrayList<String> getSnapshot(LocalMemory LocalMem) {
		assert currentSettings != null : "invalid setting";
		initSnapshotHandler(LocalMem);
		
		for (int i=0; i < currentSettings.length; i++){
			if (currentSettings[i].toLowerCase().equals("date")){
				timedTaskToNormalTask();		
				sortByDate();
			}
			else{
				if (currentSettings[i].equals(""))
					continue;
				labelsInSortedOrder.add(currentSettings[i]);
			}
		}
		
		if (!labelsInSortedOrder.isEmpty()){
			combinationOfLabels();
			sortByLabels();
			return convertSnapshotListToStringInLabelsFormat(snapshotList);
		}

		return convertSnapshotListToStringInDateFormat(snapshotList);
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
				if (labelOrder(j) > labelOrder(j+1)){
					Task temp = snapshotList.get(j);
					snapshotList.set(j, snapshotList.get(j+1));
					snapshotList.set(j+1, temp);
				}
			}
		}
	}

	private ArrayList<String> convertSnapshotListToStringInDateFormat(ArrayList<Task> snapshotList){	
		ArrayList<String> output = new ArrayList<String>();

		for (int i=0; i < snapshotList.size(); i++){
			if (wantDoneTasks() == false && isDone(snapshotList.get(i))){
				continue;
			}
			
			String snapshot = "";
			Date date = snapshotList.get(i).getFromDateTime();
			if (date == null){
				snapshot += "N.A.\n";
				for (int j=i; j < snapshotList.size(); j++){
					if (wantDoneTasks() == false && isDone(snapshotList.get(j))){
						continue;
					}
					snapshot += snapshotList.get(j).toString() + "\n";
				}
				output.add(snapshot);
				break;
			}
			else{
				snapshot += dateFormat.format(date) + "\n";
				int j=i;
				Date currentDate = snapshotList.get(j).getFromDateTime();
				while (currentDate != null && dateFormat.format(currentDate).equals(dateFormat.format(date))){
					if (wantDoneTasks() == false && isDone(snapshotList.get(j))){
						continue;
					}

					snapshot += taskToStringWithoutDate(snapshotList.get(j)) + "\n";
					j++;
					if (j > snapshotList.size()-1)
						break;
					currentDate = snapshotList.get(j).getFromDateTime();
				}
				i = j-1;
			}
			output.add(snapshot);
		}

		return output;
	}

	private ArrayList<String> convertSnapshotListToStringInLabelsFormat(ArrayList<Task> snapshotList){		
		ArrayList<String> output = new ArrayList<String>();

		for (int i=0; i < snapshotList.size(); i++){
			if (wantDoneTasks() == false && isDone(snapshotList.get(i))){
				continue;
			}

			String snapshot = "";
			int order = labelOrder(i);
			if (order == labelCombinations.size()){
				snapshot += "N.A.\n";
				for (int j=i; j < snapshotList.size(); j++){
					if (wantDoneTasks() == false && isDone(snapshotList.get(j))){
						continue;
					}
					snapshot += snapshotList.get(j).toString() + "\n";
				}
				output.add(snapshot);
				break;
			}
			else{
				for (int j=0; j < labelCombinations.get(order).size(); j++){
					snapshot += "#" + labelCombinations.get(order).get(j);
				}
				snapshot += "\n";
				int j=i;
				while (j < snapshotList.size() && labelOrder(j) == labelOrder(i)){
					if (wantDoneTasks() == false && isDone(snapshotList.get(j))){
						continue;
					}

					snapshot += snapshotList.get(j).toString() + "\n";
					j++;
				}
				i = j-1;
			}
			output.add(snapshot);
		}

		return output;
	}

	private void timedTaskToNormalTask(){	
		for (int i=0; i < snapshotList.size(); i++){
			if (snapshotList.get(i).getFromDateTime() != null && snapshotList.get(i).getToDateTime() != null){
				Date startDate = snapshotList.get(i).getFromDateTime();
				Date endDate = snapshotList.get(i).getToDateTime();
				Date date = startDate;
				if (dateFormat.format(startDate).equals(dateFormat.format(endDate))){
					snapshotList.set(i, new Task(snapshotList.get(i).getDescription(), startDate, endDate, snapshotList.get(i).getLabels()));
					continue;
				}
				
				while (!dateFormat.format(date).equals(dateFormat.format(endDate))){
					date = incrementDate(date);
					if (dateFormat.format(date).equals(dateFormat.format(endDate))){
						snapshotList.add(new Task(snapshotList.get(i).getDescription(), endDate, endDate, snapshotList.get(i).getLabels()));
					}
					else{
						snapshotList.add(new Task(snapshotList.get(i).getDescription(), getDate(date), null, snapshotList.get(i).getLabels()));
					}
				}
				snapshotList.set(i, new Task(snapshotList.get(i).getDescription(), startDate, null, snapshotList.get(i).getLabels()));
			}
		}
	}

	private void combinationOfLabels(){
		ArrayList<String> chosen = new ArrayList<String>();
		ArrayList<String> remaining = new ArrayList<String>();		
		for (int j=0; j < labelsInSortedOrder.size(); j++){
			remaining.add(labelsInSortedOrder.get(j));
		}
		combinationOfLabelsRec(remaining, chosen);

		for (int i=0; i < labelCombinations.size()-1; i++){
			for (int j=0; j < labelCombinations.size()-1-i; j++){
				if (labelCombinations.get(j).size() < labelCombinations.get(j+1).size()){
					ArrayList<String> temp = labelCombinations.get(j);
					labelCombinations.set(j, labelCombinations.get(j+1));
					labelCombinations.set(j+1, temp);
				}
			}
		}

		/*
		for (int i=0; i < labels.size(); i++){
			System.out.print(i + ": ");
			for (int j=0; j < labels.get(i).size(); j++){
				System.out.print(labels.get(i).get(j) + "-");
			}
			System.out.println();
		}
		*/
	}

	private void combinationOfLabelsRec(ArrayList<String> remaining, ArrayList<String> chosen){
		if (remaining.isEmpty()){
			labelCombinations.add(chosen);
		}
		else{
			ArrayList<String> r2 = (ArrayList<String>) remaining.clone();
			String s = r2.remove(0);
			ArrayList<String> c2 = (ArrayList<String>) chosen.clone();
			c2.add(s);
			combinationOfLabelsRec(r2, c2);
			combinationOfLabelsRec(r2, chosen);
			
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
		
		if (!timeFormat.format(task.getFromDateTime()).equals("00:00")){
			timeToString += timeFormat.format(task.getFromDateTime());
		}
		if (task.getToDateTime() != null && !timeFormat.format(task.getToDateTime()).equals("00:00")){
			String startTime = timeToString;
			String endTime = timeFormat.format(task.getToDateTime());
			if (startTime.equals(endTime)){
				timeToString = "till " + endTime;
			}else{
				timeToString = "from " + startTime + " to " + endTime;
			}
		}

		return timeToString;
	}

	private Date getDate(Date date){
		String dateToString = dateFormat.format(date);
		Date dateWithoutTime = null;
		try {
			dateWithoutTime = dateFormat.parse(dateToString);
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		return dateWithoutTime;
	}

	private boolean haveLabel(int listIndex, int labelIndex){
		if (snapshotList.get(listIndex).getLabels() == null){
			return false;
		}

		for (int i=0; i < snapshotList.get(listIndex).getLabels().size(); i++){
			if (snapshotList.get(listIndex).getLabels().get(i).equals(labelsInSortedOrder.get(labelIndex))){
				return true;
			}
		}

		return false;
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

	private int labelOrder(int index){
		int order = labelCombinations.size();
		if (snapshotList.get(index).getLabels() == null){
			return order;
		}
		
		int match=0;
		for (int i=0; i < snapshotList.get(index).getLabels().size(); i++){
			for (int j=0; j < labelsInSortedOrder.size(); j++){
				if (snapshotList.get(index).getLabels().get(i).equals(labelsInSortedOrder.get(j))){
					match++;
				}
			}
		}
		if (match == 0){
			return order;
		}
		
		for (int i=0; i < labelCombinations.size(); i++){
			if (labelCombinations.get(i).size() == match){
				int mm=0;
				for (int k=0; k < snapshotList.get(index).getLabels().size(); k++){
					for (int j=0; j < labelCombinations.get(i).size(); j++){
						if (snapshotList.get(index).getLabels().get(k).equals(labelCombinations.get(i).get(j))){
							mm++;
						}
					}
				}
				if (mm == match){
					order = i;
				}
			}
		}
		return order;
	}
	
	private boolean wantDoneTasks(){
		for (int i=0; i < currentSettings.length; i++){
			if (currentSettings[i].toLowerCase().equals("done")){
				return true;
			}
		}
		return false;
	}

	private boolean isDone(Task task){
		if (task.getLabels() == null){
			return false;
		}
		
		for (int i=0; i < task.getLabels().size(); i++){
			if (task.getLabels().get(i).toLowerCase().equals("done")){
				return true;
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
