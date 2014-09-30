package mytasks.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import mytasks.file.MyTasks;
import mytasks.file.Task;
import mytasks.parser.MyTasksParser;

/**
 * MyTasksStorage handles the storage of tasks into external memory as well as
 * converting it to readable
 * local memory for logical processes
 * 
 * @author Tay Shuan Siang
 *
 */
public class MyTasksStorage implements IStorage {

	private ArrayList<Task> newMemory = new ArrayList<Task>();

	// Constructor
	public MyTasksStorage() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public ArrayList<Task> readExtMem(String fileName) {
		File f = new File(fileName);
		if (!f.exists()) {
			return null;
		}

		String line = null;
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(
							fileName));
			while ((line = bufferedReader.readLine()) != null) {
				// saving the new description
				String newDescription = bufferedReader.readLine();

				// saving the new date and time
				String[] newDateTimeArray = bufferedReader.readLine().split("\\s+");
				Date newDateTime = new Date();
				newDateTime = MyTasksParser.extractDate(newDateTimeArray);

				// saving the labels
				String[] newLabelsArray = bufferedReader.readLine().split(",");
				ArrayList<String> newLabels = new ArrayList<String>();
				for (int i=0; i<newLabelsArray.length; i++) {
					newLabels.add(newLabelsArray[i]);
				}

				newMemory.add(new Task(newDescription, newDateTime, newLabels));
				bufferedReader.readLine(); // read in the empty line
			}
			bufferedReader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return newMemory;
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeExtMem(ArrayList<Task> localMemory) {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
							MyTasks.DEFAULT_FILENAME));
			for (int i = 0; i < localMemory.size(); i++) {
				// to save the title of the task in the first line
				bufferedWriter.write(localMemory.get(i).getDescription()); 
				bufferedWriter.newLine();

				// to check if there is a date and time recorded with the task
				if (localMemory.get(i).getDateTime() != null) {	
					// save the date and time of the task in the second line
					bufferedWriter.write(MyTasksParser.dateTimeFormat.format(localMemory.get(i).getDateTime())); 
					bufferedWriter.newLine();
				}
				// to check if there are any labels included in the tasks
				if (localMemory.get(i).getLabels() != null) {	
					// save the n labels in the next n lines
					for (int j=0; j<localMemory.get(i).getLabels().size(); j++) {	
						bufferedWriter.write(localMemory.get(i).getLabels().get(j) + ", ");
					}
					bufferedWriter.newLine();
				}
				bufferedWriter.newLine(); // use an empty line to separate between tasks
			}
			bufferedWriter.close();
		} catch (IOException e) {
			System.out.println("Error with reading existing file");
		}
	}

	/**
	 * {@inheritDoc} //Does not need to be implemented in v0.1 as of yet
	 */
	public String exportFile(String fileName) {
		// TODO Auto-generated method stub
		// TODO for humans to read
		return null;
	}

}
