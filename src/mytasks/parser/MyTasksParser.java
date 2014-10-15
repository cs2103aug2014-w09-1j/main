package mytasks.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mytasks.file.CommandInfo;

/**
 * MyTasksParser interprets userinput to useable data structures to work with in
 * the Logic component. Naive version for now
 * 
 * @author Wilson
 *
 */
public class MyTasksParser implements IParser {

	// Date and Time formats that are currently useable.
	@SuppressWarnings("serial")
	public static List<SimpleDateFormat> dateFormats = new ArrayList<SimpleDateFormat>() {
		{
			add(new SimpleDateFormat("dd.MM.yyyy"));
			add(new SimpleDateFormat("dd.MM.yyyy HH:mm"));
			add(new SimpleDateFormat("dd.MMM.yyyy"));
			add(new SimpleDateFormat("dd.MMM.yyyy HH:mm"));
		}
	};
	private ArrayList<Integer> usedWords;

	// Constructor
	public MyTasksParser() {
		usedWords = new ArrayList<Integer>();
	}

	/**
	 * {@inheritDoc}
	 */
	public CommandInfo parseInput(String input) {
		String[] words = input.split("\\s+");
		if (words.length != 0) {
			String comdType = words[0];
			String withoutComdType = removeCommand(input, comdType);
			switch (comdType) {
			case "add":
			case "search":
			case "delete":
				CommandInfo temp = convertStandard(withoutComdType, comdType);
				return temp;
			case "undo":
			case "redo":
				if (words.length != 1) { // Checks if there are any extra input
					return null;
				}
				return new CommandInfo(comdType, null, null, null, null, null);
			case "update":
				CommandInfo temp2 = convertUpdate(withoutComdType, comdType);
				return temp2;
			default:
				return null;
			}
		}
		return null;
	}

	private String removeCommand(String input, String comdType) {
		String messageAndDateAndLabels = input.substring(comdType.length());
		return messageAndDateAndLabels;
	}

	private CommandInfo convertStandard(String message, String comdType) {
		String[] words = message.trim().split("\\s+");
		ArrayList<String> labels = locateLabels(words);
		String[] withoutLabels = removeLabels(words);
		DoubleDate dates = extractDate(withoutLabels);
		Date dateFrom = dates.getDate1();
		Date dateTo = dates.getDate2();
		String taskDesc = removeDate(withoutLabels);
		if (taskDesc.equals("") || taskDesc.length() == 0) { // Ie. no task
																// description
			return null;
		}
		return new CommandInfo(comdType, taskDesc, dateFrom, dateTo, labels,
				null);
	}

	/**
	 * locateLabels looks at an array and extracts the labels denoted by "#". It
	 * then stores the words that are labels into an arraylist and returns the
	 * arraylist
	 * 
	 * @param words
	 * @return arraylist of strings that represent labels
	 */
	protected ArrayList<String> locateLabels(String[] words) {
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < words.length; i++) {
			String curWord = words[i];
			char firstLetter = curWord.charAt(0);
			if (firstLetter == '#') {
				String toAdd = curWord.substring(1);
				result.add(toAdd);
			}
		}
		if (result.size() != 0) {
			return result;
		}
		return null;
	}

	protected String[] removeLabels(String[] words) {
		String result = "";
		for (int i = 0; i < words.length; i++) {
			String curWord = words[i];
			char firstLetter = curWord.charAt(0);
			if (firstLetter != '#') {
				result += curWord + " ";
			}
		}
		return result.split("\\s+");
	}

	// TODO: remove all incorrect usage of exception handling
	/**
	 * extractDate checks if the given array of words contains a date/time that
	 * complies with the current useable formats and returns the date object if
	 * so.
	 * 
	 * @param words arrays
	 * @return DoubleDate(object) of the task
	 */
	protected DoubleDate extractDate(String[] words) {
		//Reinitialize usedWords because it should be empty for a new function call.
		usedWords = new ArrayList<Integer>();

		int[] indexFromTo = checkFromAndTo(words);
		int indexOfFrom = indexFromTo[0];
		int indexOfTo = indexFromTo[1];

		int[] indexDatesFormat = updateDateIndexAndFormat(words);
		int indexOfDate1 = indexDatesFormat[0];
		int indexOfDate2 = indexDatesFormat[1];
		int indexOfFormat = indexDatesFormat[2];

		DoubleDate result = null;

		if (isTimedTask(indexOfFrom, indexOfTo)) {
			result = handleTimedTask(words, indexOfDate1,
					indexOfDate2, indexOfFrom, indexOfTo, indexOfFormat);
		} else {
			result = handleUntimedTask( words, indexOfDate1,
					indexOfDate2, indexOfFrom, indexOfTo, indexOfFormat);
		}
		return result;
	}

	/**
	 * checkFromAndTo looks at an array of words and checks if words "from" and
	 * "to" exist. If both words exist, and are not seperated by more than
	 * MAX_SPACING, their respective indexes are returned in an array. Else if
	 * one or more don't exists, they are represented by a -1
	 * 
	 * @param words
	 * @return int[] where first int represents index of "from" and second int
	 *         represents index of "to"
	 */
	private int[] checkFromAndTo(String[] words) {
		final int MAX_SPACING = 3;
		int indexFrom = -1;
		int indexTo = -1;
		for (int i = 0; i < words.length; i++) {
			String curWord = words[i];
			if (curWord.equals("from")) {
				indexFrom = i;
			}
			if (curWord.equals("to") && indexFrom >= i - MAX_SPACING
					&& indexFrom != -1) {
				indexTo = i;
				break;
			}
		}
		int[] result = { indexFrom, indexTo };
		return result;
	}

	/**
	 * updateDateIndexAndFormat checks if any word fits any of the formats that
	 * are currently accepted. If any of these are absent, they are represented
	 * by a -1. Will be expanded to NLP
	 * 
	 * @param words
	 * @return int[] where index 0 is index of 1st date, 1 is index of 2nd date,
	 *         2 is index of format
	 */
	private int[] updateDateIndexAndFormat(String[] words) {
		int indexDate1 = -1;
		int indexDate2 = -1;
		int indexFormat = -1;
		for (int i = 0; i < words.length; i++) {
			for (int j = 0; j < dateFormats.size(); j++) {
				SimpleDateFormat dateForm = dateFormats.get(j);
				try {
					dateForm.setLenient(false);
					dateForm.parse(words[i]);
					if (indexDate1 == -1) {
						indexDate1 = i;
					} else {
						indexDate2 = i;
					}
					indexFormat = j;
				} catch (ParseException e) {
				}
			}
		}
		int[] result = { indexDate1, indexDate2, indexFormat };
		return result;
	}

	private DoubleDate handleTimedTask(String[] words, int indexOfDate1,
			int indexOfDate2, int indexOfFrom, int indexOfTo, int indexOfFormat) {
		Date dateTimeObj1 = null;
		Date dateTimeObj2 = null;
		if (indexOfDate2 == -1) {
			try {
				String date = words[indexOfDate1];
				String time1 = words[indexOfFrom + 1];
				String time2 = words[indexOfTo + 1];
				String dateTime1 = date + " " + time1;
				String dateTime2 = date + " " + time2;
				SimpleDateFormat dateForm = dateFormats.get(indexOfFormat + 1);
				dateForm.setLenient(false);
				dateTimeObj1 = dateForm.parse(dateTime1);
				dateTimeObj2 = dateForm.parse(dateTime2);
				usedWords.add((Integer) indexOfFrom);
				usedWords.add((Integer) indexOfTo);
				usedWords.add((Integer) indexOfDate1);
				usedWords.add((Integer) indexOfFrom + 1);
				usedWords.add((Integer) indexOfTo + 1);
			} catch (IndexOutOfBoundsException e) {
				// Implying no date/time found
			} catch (ParseException e) {
				System.out.println("Unexpected error1");
			}
		} else {
			try {
				String time1 = words[indexOfDate1 + 1];
				String time2 = words[indexOfDate2 + 1];
				String date1 = words[indexOfDate1];
				String date2 = words[indexOfDate2];
				String dateTime1 = date1 + " " + time1;
				String dateTime2 = date2 + " " + time2;
				SimpleDateFormat dateForm = dateFormats.get(indexOfFormat + 1);
				dateForm.setLenient(false);
				dateTimeObj1 = dateForm.parse(dateTime1);
				dateTimeObj2 = dateForm.parse(dateTime2);
				usedWords.add((Integer) indexOfFrom);
				usedWords.add((Integer) indexOfTo);
				usedWords.add((Integer) indexOfDate1);
				usedWords.add((Integer) indexOfDate2);
				usedWords.add((Integer) indexOfDate1 + 1);
				usedWords.add((Integer) indexOfDate2 + 1);
			} catch (IndexOutOfBoundsException e) {
				// Implying no time found
				try {
					String date1 = words[indexOfDate1];
					String date2 = words[indexOfDate2];
					SimpleDateFormat dateForm = dateFormats.get(indexOfFormat);
					dateForm.setLenient(false);
					dateTimeObj1 = dateForm.parse(date1);
					dateTimeObj2 = dateForm.parse(date2);
					usedWords.add((Integer) indexOfFrom);
					usedWords.add((Integer) indexOfTo);
					usedWords.add((Integer) indexOfDate1);
					usedWords.add((Integer) indexOfDate2);
				} catch (ParseException e1) {
					System.out.println("Unexpected error2");
				} catch (IndexOutOfBoundsException e1) {
					// Implying no date found
				}
			} catch (ParseException e) {
				System.out.println("Unexpected error2");
			}
		}
		return new DoubleDate(dateTimeObj1, dateTimeObj2);
	}
	
	private DoubleDate handleUntimedTask(String[] words, int indexOfDate1,
			int indexOfDate2, int indexOfFrom, int indexOfTo, int indexOfFormat) {
		Date dateTimeObj1 = null;
		Date dateTimeObj2 = null;
		if (indexOfDate1 != words.length - 1 && indexOfDate1 > -1) {
			String temp = words[indexOfDate1] + " "
					+ words[indexOfDate1 + 1];
			try {
				dateTimeObj1 = dateFormats.get(1).parse(temp);
				usedWords.add((Integer) indexOfDate1);
				usedWords.add((Integer) indexOfDate1 + 1);
			} catch (ParseException ne) {
				try {
					SimpleDateFormat dateForm = dateFormats
							.get(indexOfFormat);
					dateTimeObj1 = dateForm.parse(words[indexOfDate1]);
					usedWords.add((Integer) indexOfDate1);
				} catch (ParseException e) {
					System.out.println("Unexpected error3");
				} catch (IndexOutOfBoundsException e) {
					// Implying no date found
				}
			}
		} else {
			try {
				SimpleDateFormat dateForm = dateFormats.get(indexOfFormat);
				dateForm.setLenient(false);
				dateTimeObj1 = dateForm.parse(words[indexOfDate1]);
				usedWords.add((Integer) indexOfDate1);
			} catch (ParseException e) {
				System.out.println("Unexpected error4");
			} catch (IndexOutOfBoundsException e) {
				// Implying no date found
			}
		}
		return new DoubleDate(dateTimeObj1, dateTimeObj2);
	}

	private boolean isTimedTask(int indexFrom, int indexTo) {
		if (indexFrom != -1 && indexTo != -1) {
			return true;
		}
		return false;
	}

	private String removeDate(String[] words) {
		String result = "";
		for (int i = 0; i < words.length; i++) {
			if (!usedWords.contains((Integer) i)) {
				result += words[i] + " ";
			}
		}
		return result.trim();
	}

	private CommandInfo convertUpdate(String message, String comdType) {

		String delims = "[-]+";
		String[] messageSplit = message.split(delims);
		if (messageSplit.length != 2) { // Wrong format of update command
			return null;
		}
		String toUpdateFrom = messageSplit[0].trim();
		String remainingMessage = messageSplit[1].trim();
		String[] words = remainingMessage.split("\\s+");
		ArrayList<String> labels = locateLabels(words);
		String[] withoutLabels = removeLabels(words);
		DoubleDate dates = extractDate(withoutLabels);
		Date dateFrom = dates.getDate1();
		Date dateTo = dates.getDate2();
		String taskDesc = removeDate(withoutLabels);
		if (taskDesc.equals("") || taskDesc.length() == 0) {
			taskDesc = null;
		}
		return new CommandInfo(comdType, taskDesc, dateFrom, dateTo, labels,
				toUpdateFrom);
	}

	protected class DoubleDate {

		private Date mDate1;
		private Date mDate2;

		private DoubleDate(Date date1, Date date2) {
			mDate1 = date1;
			mDate2 = date2;
		}

		public Date getDate1() {
			return mDate1;
		}

		public Date getDate2() {
			return mDate2;
		}
	}
}
