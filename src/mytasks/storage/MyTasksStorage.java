package mytasks.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import mytasks.file.Logger;
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
@SuppressWarnings("serial")
public class MyTasksStorage implements IStorage, Serializable {
	
	private static MyTasksStorage INSTANCE = null;
	private final String MESSAGE_CORPTDATA = "Corrupted data";
	private final String MESSAGE_FILEERROR = "Error with reading existing file";

	// Constructor
	private MyTasksStorage() {
	}
	
	public static MyTasksStorage getInstance(){
		if (INSTANCE == null){
			INSTANCE= new MyTasksStorage();
		}
		return INSTANCE;
	}
	
	protected Object readResolve() {
		return INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public ArrayList<Task> readExtMem(String fileName) {
		String pastMem = null;
		try {
			pastMem = readString(fileName);
		} catch (IOException e) {
		}
		ArrayList<Task> result = convertToTasks(pastMem);
		return result;
	}
	
	private String readString(String fileName) throws IOException {
		File tempFile = new File(fileName);
		String result = null;
		if (!tempFile.exists()) {
			return result;
		} else {
			result = new String(readAllBytes(get(fileName)));
		}
		return result;
	}
	
	protected ArrayList<Task> convertToTasks(String memString) {
		ArrayList<Task> result = new ArrayList<Task>();
		if (memString == null) {
			return result;
		}
		if (memString.length()==0) {
			return result;
		}
		String delims = "(//)";
		String[] memBlock = memString.split(delims);
		int noBlocks = memBlock.length;
		int sizeBlocks = 4;
		if (noBlocks%sizeBlocks != 0){
			Logger logger = Logger.getInstance();
			logger.log(MESSAGE_CORPTDATA);
			return result;
		}
		for (int i = 0; i<noBlocks/sizeBlocks; i++) {
			String taskDesc = null;
			Date dateFrom = null;
			Date dateTo = null;
			ArrayList<String> labels = null;
			for (int j = 0; j<sizeBlocks; j++) {
				int temp = (i*sizeBlocks)+j;
				String curBlock = memBlock[temp];
				if (j == 0) {
					taskDesc = curBlock;
				} else if (j == 1) {
					try {
						dateFrom = MyTasksParser.dateFormats.get(1).parse(curBlock);
					} catch (ParseException e) {
						//Implying empty space which is null date
					}
				} else if (j == 2) {
					try {
						dateTo = MyTasksParser.dateFormats.get(1).parse(curBlock);
					} catch (ParseException e) {
						//Implying empty space which is null date
					}
				} else {
					if (!curBlock.equals(" ")) {
						String delims2 = "[,]+";
						String[] indivLabels = curBlock.split(delims2);
						ArrayList<String> tempLabels = new ArrayList<String> ();
						for (int k = 0; k<indivLabels.length; k++) {
							tempLabels.add(indivLabels[k]);
						}
						labels = tempLabels;
					}
				}
			}
			result.add(new Task(taskDesc, dateFrom, dateTo, labels));
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeExtMem(ArrayList<Task> localMemory) {
		String output = determineOutput(localMemory);
		printOutput(output, MyTasks.DEFAULT_FILENAME);
	}
	
	protected String determineOutput(ArrayList<Task> localMem){
		String result = "";
		for (int i = 0; i<localMem.size(); i++){
			Task currentTask = localMem.get(i);
			result += currentTask.getDescription();
			result += addBreak();
			if (currentTask.getFromDateTime() == null){
				result += " ";
			} else {
				result += MyTasksParser.dateFormats.get(1).format(currentTask.getFromDateTime());
			}
			result += addBreak();
			if (currentTask.getToDateTime() == null){
				result += " ";
			} else {
				result += MyTasksParser.dateFormats.get(1).format(currentTask.getToDateTime());
			}
			result += addBreak();
			ArrayList<String> labels = currentTask.getLabels();
			if (labels == null) {
				result += " ";
			} else {
				for (int j = 0; j<labels.size(); j++) {
					result += labels.get(j);
					result += ",";
				}
			}
			result += addBreak();
		}
		return result;
	}
	
	private String addBreak() {
		return "//";
	}
	
	private void printOutput(String output, String fileName){
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(fileName));
			writer.print(output);
			writer.close();
		} catch (IOException e) {
			Logger logger = Logger.getInstance();
			logger.log(MESSAGE_FILEERROR);
		}
	}
}
