package mytasks.storage;

import java.util.ArrayList;
import mytasks.file.Task;

/**
 * Istorage interface defines abstract methods to be used by storage component
 * @author Wilson
 *
 */
public interface IStorage {
	
	/**
	 * readExtMem converts external memory into local memory for logic usage
	 * @param fileName
	 * @return Data Structure that is to be used to local memory. May be changed at later date
	 */
	public ArrayList<Task> readExtMem(String fileName);
	
	/**
	 * writeExtMem converts local memory to external memory by writing it to a file that is stored in root
	 * @param localMem whose data structure may be changed at a later date
	 */
	public void writeExtMem(ArrayList<Task> localMem);
	
}
