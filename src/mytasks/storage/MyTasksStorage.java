package mytasks.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
 * @author Tay Shuan Siang, Wilson
 *
 */
public class MyTasksStorage implements IStorage {

	// Constructor
	public MyTasksStorage() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public ArrayList<Task> readExtMem(String fileName) {
		//TODO fix readExtMem. 30sep14
		//1) returns null instead of empty arraylist if file doesnt exist (introduced quickfix here)
		//2) does not work if file already exist
		//For junit test, may want to create external textfiles to run ur test cases.
		//You may keep it local without pushing for now.
		ArrayList<Task> newMemory = new ArrayList<Task>();
		File f = new File(fileName);
		if (!f.exists()) {
			return newMemory;
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
		String output = determineOutput(localMemory);
		printOutput(output, MyTasks.DEFAULT_FILENAME);
	}
	
	public String determineOutput(ArrayList<Task> localMem){
		for (int i = 0; i<localMem.size(); i++){
			
		}
		return null;
	}
	
	private void printOutput(String output, String fileName){
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(fileName));
			writer.print(output);
			writer.close();
		} catch (IOException e) {
			System.out.println("Error with reading existing file");
		}
	}
	
	/**
	 * {@inheritDoc} //Does not need to be implemented in v0.1 as of yet
	 */
	public String exportFile(String fileName) {
		// TODO for humans to read
		return null;
	}

}
