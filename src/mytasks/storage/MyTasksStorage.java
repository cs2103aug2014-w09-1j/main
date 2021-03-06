package mytasks.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.*;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import mytasks.file.MyTasksController;
import mytasks.file.Task;
import mytasks.logic.parser.MyTasksParser;

//@author A0114302A
/**
 * MyTasksStorage handles the storage of tasks into external memory as well as
 * converting it to readable local memory for logical processes
 */
@SuppressWarnings("serial")
public class MyTasksStorage implements IStorage, Serializable {

	private static MyTasksStorage INSTANCE = null;
	private final String MESSAGE_CORPTDATA = "Corrupted data";
	private final String MESSAGE_FILEERROR = "Error with reading existing file";
	private final String TEXT_DELIMS = "//";
	private final String TEXT_EMPTYFIELD = " ";
	private static final Logger LOGGER = Logger.getLogger(MyTasksStorage.class
			.getName());
	private Handler fh = null;

	// Constructor
	private MyTasksStorage() {
	}

	public static MyTasksStorage getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MyTasksStorage();
		}
		return INSTANCE;
	}

	protected Object readResolve() {
		return INSTANCE;
	}
	
	private void runLogger() {
		try {
			fh = new FileHandler(mytasks.file.MyTasksController.default_log, 0,
					1, true);
			LOGGER.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			LOGGER.setUseParentHandlers(false);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * closeHandler prevents overflow of information and multiple logger files
	 * from appearing
	 */
	private void closeHandler() {
		fh.flush();
		fh.close();
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
		if (memString.length() == 0) {
			return result;
		}
		String[] memBlock = memString.split(TEXT_DELIMS);
		int noBlocks = memBlock.length;
		int sizeBlocks = 4;
		if (noBlocks % sizeBlocks != 0) {
			runLogger();
			LOGGER.log(Level.SEVERE, MESSAGE_CORPTDATA + " " + noBlocks);
			closeHandler();
			return result;
		}
		handleIndivBlocks(result, memBlock, noBlocks, sizeBlocks);
		assert result != null;
		return result;
	}
	
	/**
	 * handleIndivBlocks creates a new task for each block of memory (consecutive 4 blocks)
	 * @param result
	 * @param memBlock
	 * @param noBlocks
	 * @param sizeBlocks
	 */
	public void handleIndivBlocks(ArrayList<Task> result, String[] memBlock,
			int noBlocks, int sizeBlocks) {
		for (int i = 0; i < noBlocks / sizeBlocks; i++) {
			String taskDesc = null;
			Date dateFrom = null;
			Date dateTo = null;
			ArrayList<String> labels = null;
			for (int j = 0; j < sizeBlocks; j++) {
				int temp = (i * sizeBlocks) + j;
				String curBlock = memBlock[temp];
				if (j == 0) {
					taskDesc = curBlock;
				} else if (j == 1) {
					dateFrom = getDate(dateFrom, curBlock);
				} else if (j == 2) {
					dateTo = getDate(dateTo, curBlock);
				} else {
					labels = getLabels(labels, curBlock);
				}
			}
			result.add(new Task(taskDesc, dateFrom, dateTo, labels));
		}
	}

	public ArrayList<String> getLabels(ArrayList<String> labels, String curBlock) {
		if (!curBlock.equals(TEXT_EMPTYFIELD)) {
			String delims2 = "[,]+";
			String[] indivLabels = curBlock.split(delims2);
			ArrayList<String> tempLabels = new ArrayList<String>();
			for (int k = 0; k < indivLabels.length; k++) {
				tempLabels.add(indivLabels[k]);
			}
			labels = tempLabels;
		}
		return labels;
	}

	public Date getDate(Date prevDate, String curBlock) {
		try {
			prevDate = MyTasksParser.dateTimeFormats.get(0).parse(curBlock);
		} catch (ParseException e) {
		}
		return prevDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeExtMem(ArrayList<Task> localMemory) {
		String output = determineOutput(localMemory);
		printOutput(output, MyTasksController.DEFAULT_FILENAME);
	}
	
	/**
	 * determineOutput checks the arraylist that represents memory and converts it to
	 * a string format by having DELIMS seperate each field. An empty field is denoted by a
	 * single empty space
	 * @param localMem
	 * @return String to be printed
	 */
	protected String determineOutput(ArrayList<Task> localMem) {
		String result = "";
		for (int i = 0; i < localMem.size(); i++) {
			Task currentTask = localMem.get(i);
			result += currentTask.getDescription();
			result += addBreak();
			result = addDate(currentTask.getFromDateTime(), result);
			result += addBreak();
			result = addDate(currentTask.getToDateTime(), result);
			result += addBreak();
			result = addLabels(result, currentTask);
			result += addBreak();
		}
		return result;
	}

	public String addLabels(String result, Task currentTask) {
		ArrayList<String> labels = currentTask.getLabels();
		if (labels == null) {
			result += TEXT_EMPTYFIELD;
		} else if (labels.size()==0){
			result += TEXT_EMPTYFIELD;
		} else {
			for (int j = 0; j < labels.size(); j++) {
				result += labels.get(j);
				result += ",";
			}
		}
		return result;
	}

	private String addDate(Date currentDate, String result) {
		if (currentDate == null) {
			result += TEXT_EMPTYFIELD;
		} else {
			result += MyTasksParser.dateTimeFormats.get(0).format(currentDate);
		}
		return result;
	}

	private String addBreak() {
		return TEXT_DELIMS;
	}
	
	/**
	 * printOutput takes a string and prints it to a destinated fileName
	 * @param output
	 * @param fileName
	 */
	private void printOutput(String output, String fileName) {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(fileName));
			writer.print(output);
			writer.close();
		} catch (IOException e) {
			runLogger();
			LOGGER.log(Level.SEVERE, MESSAGE_FILEERROR, e);
			closeHandler();
		}
	}
}
