package mytasks.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * MyTasksStorage handles the storage of tasks into external memory as well as
 * converting it to readable
 * local memory for logical processes
 * 
 * @author Tay Shuan Siang
 *
 */
public class MyTasksStorage implements IStorage {

	private String externalMemoryFile = "externalmemoryfile.txt";
	private ArrayList<String> localMemory = new ArrayList<String>();

	// Constructor
	public MyTasksStorage() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IOException
	 */
	public ArrayList<String> readExtMem(String fileName) {
		File f = new File(externalMemoryFile);
		if (!f.exists()) {
			writeExtMem(localMemory);
		}

		String line = null;
		// BufferedReader bufferedReader;
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(
							externalMemoryFile));
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
	public void writeExtMem(ArrayList<String> localMemory) {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(externalMemoryFile));
			for (int i=0; i<localMemory.size(); i++) {
				bufferedWriter.write(localMemory.get(i));
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
