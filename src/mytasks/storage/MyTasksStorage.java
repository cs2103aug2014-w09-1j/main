package mytasks.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import mytasks.file.MyTasks;
import mytasks.file.Task;

/**
 * MyTasksStorage handles the storage of tasks into external memory as well as converting it to readable
 * local memory for logical processes
 * @author Wilson
 *
 */
public class MyTasksStorage implements IStorage {

	private ArrayList<String> localMemory = new ArrayList<String>();

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
			writeExtMem(localMemory); //This line doesnt make sense.
		}

		String line = null;
		// BufferedReader bufferedReader;
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(
							fileName));
			while ((line = bufferedReader.readLine()) != null) {
				localMemory.add(line);
			}
			bufferedReader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return localMemory;
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeExtMem(ArrayList<Task> localMemory) {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(MyTasks.DEFAULT_FILENAME));
			for (int i=0; i<localMemory.size(); i++) {
				bufferedWriter.write(localMemory.get(i));
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc} //Does not need to be implemented in v0.1 as of yet
	 */
	public String exportFile(String fileName) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
