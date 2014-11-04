package mytasks.parser;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.*;

import mytasks.logic.AddCommand;
import mytasks.logic.Command;
import mytasks.logic.DeleteCommand;
import mytasks.logic.DoneCommand;
import mytasks.logic.RedoCommand;
import mytasks.logic.SearchCommand;
import mytasks.logic.SortCommand;
import mytasks.logic.UndoCommand;
import mytasks.logic.UpdateCommand;

/**
 * MyTasksParser interprets userinput to useable data structures to work with in
 * the Logic component. Naive version for now
 * 
 * @author A0114302A
 */
public class MyTasksParser implements IParser {

	// Date and Time formats that are currently useable.
	@SuppressWarnings("serial")
	public static List<SimpleDateFormat> dateFormats = new ArrayList<SimpleDateFormat>() {
		{
			add(new SimpleDateFormat("dd.MM.yyyy"));
			add(new SimpleDateFormat("dd.MMM.yyyy"));
		}
	};

	@SuppressWarnings("serial")
	public static List<SimpleDateFormat> dateTimeFormats = new ArrayList<SimpleDateFormat>() {
		{
			add(new SimpleDateFormat("dd.MM.yyyy HH:mm"));
			add(new SimpleDateFormat("dd.MM.yyyy hha"));
			add(new SimpleDateFormat("dd.MM.yyyy hh:mma"));
			add(new SimpleDateFormat("dd.MMM.yyyy HH:mm"));
			add(new SimpleDateFormat("dd.MMM.yyyy hha"));
			add(new SimpleDateFormat("dd.MMM.yyyy hh:mma"));
		}
	};

	private static final int DAYSINWEEK = 7;
	private static final Logger LOGGER = Logger.getLogger(MyTasksParser.class
			.getName());
	private Handler fh = null;
	private final String MESSAGE_INVALIDTOFROM = "No time found after word 'to' or 'next': Taken as task description";
	private final String MESSAGE_INVALIDINDEX = "Unexpected error: Invalid indexes";
	private final String MESSAGE_INVALIDDATES = "Input dates do not follow order";

	private String[] keyWords = { "today", "tomorrow", "yesterday", "next",
			"monday", "tuesday", "wednesday", "thursday", "friday", "saturday",
			"sunday" };
	private String[] dateWords = { "monday", "tuesday", "wednesday",
			"thursday", "friday", "saturday", "sunday", "month", "year" };

	private ArrayList<Integer> usedWords;

	// Constructor
	public MyTasksParser() {
		usedWords = new ArrayList<Integer>();
	}

	private void runLogger() {
		try {
			fh = new FileHandler(mytasks.file.MyTasksController.default_log, 0, 1, true);
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

	private void closeHandler() {
		fh.flush();
		fh.close();
	}

	/**
	 * {@inheritDoc}
	 */
	public Command parseInput(String input) {
		String[] words = input.split("\\s+");
		if (words.length != 0) {
			String comdType = words[0];
			String withoutComdType = removeCommand(input, comdType);
			if (withoutComdType.trim().length() == 0
					&& !comdType.trim().equals("un")
					&& !comdType.equals("re")) {
				return null;
			}
			switch (comdType) {
			case "ad":
			case "se":
			case "de":
			case "do":
				Command temp = convertStandard(withoutComdType, comdType);
				return temp;
			case "un":
				if (words.length != 1) {
					return null;
				}
				return new UndoCommand(null, null, null, null, null);
			case "re":
				if (words.length != 1) {
					return null;
				}
				return new RedoCommand(null, null, null, null, null);
			case "up":
				Command temp2 = convertUpdate(withoutComdType, comdType);
				return temp2;
			case "so":
				Command temp3 = convertSort(withoutComdType, comdType);
				return temp3;
			case "?":
			case "he":
			case "help":
				//TODO: add code for help command here
				break;
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

	/**
	 * convertStandard parses the parameters and returns the corresponding
	 * CommandInfo object. This is only used for standard command types that are
	 * not update
	 * 
	 * @param message
	 * @param comdType
	 * @return
	 */
	private Command convertStandard(String message, String comdType) {
		String[] words = message.trim().split("\\s+");
		ArrayList<String> labels = locateLabels(words);
		String[] withoutLabels = removeLabels(words);
		DoubleDate dates = extractDate(withoutLabels);
		Date dateFrom = null;
		Date dateTo = null;
		if (dates != null) {
			dateFrom = dates.getDate1();
			dateTo = dates.getDate2();
		}
		String taskDesc = removeDate(withoutLabels);
		if (taskDesc.equals("") || taskDesc.length() == 0) {
			return null;
		}
		switch (comdType) {
		case "ad":
			if (dateFrom!= null && dateTo!= null){
				if (dateFrom.compareTo(dateTo)>=0){
					runLogger();
					LOGGER.log(Level.WARNING, MESSAGE_INVALIDDATES);
					closeHandler();
					return null;
				}
			}
			return new AddCommand(taskDesc, dateFrom, dateTo, labels, null);
		case "se":
			return new SearchCommand(taskDesc, dateFrom, dateTo, labels, null);
		case "de":
			return new DeleteCommand(taskDesc, dateFrom, dateTo, labels, null);
		case "do":
			return new DoneCommand(taskDesc, dateFrom, dateTo, labels, null);
		}
		return null;
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

	/**
	 * extractDate checks if the given array of words contains a date/time that
	 * complies with the current useable formats and returns the date object if
	 * so.
	 * 
	 * @param words
	 *            arrays
	 * @return DoubleDate(object) of the task
	 */
	protected DoubleDate extractDate(String[] words) {
		// Reinitialize usedWords because it should be empty for a new function
		// call.
		usedWords = new ArrayList<Integer>();

		int[] indexFromTo = checkFromAndTo(words);
		int indexOfFrom = indexFromTo[0];
		int indexOfTo = indexFromTo[1];

		int[] indexDatesFormat = updateDateIndexAndFormat(words);
		DoubleDate result = null;
		if (isTimedTask(indexOfFrom, indexOfTo)) {
			int indexOfDate1 = 0;
			int indexOfDate2 = 0;
			if (indexOfFrom>indexOfTo){
				indexOfDate1 = indexDatesFormat[1];
				indexOfDate2 = indexDatesFormat[0];
			} else {
				indexOfDate1 = indexDatesFormat[0];
				indexOfDate2 = indexDatesFormat[1];
			}
			result = handleTimedTask(words, indexOfDate1, indexOfDate2,
					indexOfFrom, indexOfTo);
		} else {
			int indexOfDate1 = indexDatesFormat[0];
			int indexOfDate2 = indexDatesFormat[1];
			result = handleUntimedTask(words, indexOfDate1, indexOfDate2,
					indexOfFrom, indexOfTo);
		}
		return result;
	}

	/**
	 * checkFromAndTo looks at an array of words and checks if words "from" and
	 * "to" exist. If both words exist,their respective indexes are returned in
	 * an array. Else if one or more don't exists, they are represented by a -1
	 * 
	 * @param words
	 * @return int[] where first int represents index of "from" and second int
	 *         represents index of "to"
	 */
	private int[] checkFromAndTo(String[] words) {
		int indexFrom = -1;
		int indexTo = -1;
		for (int i = 0; i < words.length; i++) {
			String curWord = words[i];
			if (curWord.equals("from")) {
				indexFrom = i;
			}
			if (curWord.equals("to")) {
				indexTo = i;
			}
		}
		if (indexTo == -1 || indexFrom == -1) {
			int[] result = { -1, -1 };
			return result;
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
		int toSkip = -1;
		for (int i = 0; i < words.length; i++) {
			if (i != toSkip) {
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
					} catch (ParseException e) {
					}
				}
				int[] otherResults = checkForOtherFormats(words, indexDate1,
						indexDate2, i);
				if (otherResults[0] != -1) {
					indexDate1 = otherResults[0];
					if (otherResults[2] == 1) {
						toSkip = i + 1;
					}
				} else if (otherResults[1] != -1) {
					indexDate2 = otherResults[1];
					if (otherResults[2] == 1) {
						toSkip = i + 1;
					}
				}
			}
		}
		int[] result = { indexDate1, indexDate2 };
		return result;
	}

	private int[] checkForOtherFormats(String[] words, int indexDate1,
			int indexDate2, int curIndex) {
		int[] result = { -1, -1, 0 };
		int indexKey = checkIfIsKeyWord(words[curIndex]);
		if (indexKey != -1) {
			if (indexKey == 3) {
				if (curIndex != words.length - 1) {
					int dateKey = checkIfIsDateWord(words[curIndex + 1]);
					if (dateKey != -1) {
						if (indexDate1 == -1) {
							result[0] = curIndex;
							result[2] = 1;
						} else {
							result[1] = curIndex;
							result[2] = 1;
						}
					}
				}
			} else {
				if (indexDate1 == -1) {
					result[0] = curIndex;
				} else {
					result[1] = curIndex;
				}
			}
		}
		return result;
	}

	private int checkIfIsKeyWord(String word) {
		for (int i = 0; i < keyWords.length; i++) {
			if (word.equals(keyWords[i])) {
				return i;
			}
		}
		return -1;
	}

	private int checkIfIsDateWord(String word) {
		for (int i = 0; i < dateWords.length; i++) {
			if (word.equals(dateWords[i])) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * handleTimedTask uses the variables provided to parse a word array into
	 * the corresponding two date objects. usedWords is also updated here to
	 * that it can be used later for removal of all date related words
	 * individual date objects are null when no date(s) are found
	 * 
	 * @param words
	 * @param indexOfDate1
	 * @param indexOfDate2
	 * @param indexOfFrom
	 * @param indexOfTo
	 * @param indexOfFormat
	 * @return DoubleDate object representing DateFrom and DateTo respectively
	 */
	private DoubleDate handleTimedTask(String[] words, int indexOfDate1,
			int indexOfDate2, int indexOfFrom, int indexOfTo) {
		DoubleDate results = null;
		if (indexOfDate2 == -1) {
			try {
				String toDateFormat = convertToDateFormat(words, indexOfDate1);
				String time1 = words[indexOfFrom + 1];
				String time2 = words[indexOfTo + 1];
				results = getDoubleDateTime(toDateFormat, null, time1, time2);
				if (results.getDate1() != null && results.getDate2() != null) {
					if (isNextDate(words[indexOfDate1])) {
						int[] toAdd = { indexOfFrom, indexOfTo, indexOfDate1,
								indexOfDate1 + 1, indexOfFrom + 1,
								indexOfTo + 1 };
						addToUsedWords(toAdd);
					} else {
						int[] toAdd = { indexOfFrom, indexOfTo, indexOfDate1,
								indexOfFrom + 1, indexOfTo + 1 };
						addToUsedWords(toAdd);
					}
				}
			} catch (IndexOutOfBoundsException e) {
				runLogger();
				LOGGER.log(Level.WARNING, MESSAGE_INVALIDTOFROM, e);
				closeHandler();
			}

		} else {
			boolean noTime = false;
			try {
				boolean isNext1 = isNextDate(words[indexOfDate1]);
				boolean isNext2 = isNextDate(words[indexOfDate2]);
				String time1 = words[indexOfDate1 + 1];
				if (isNext1 && indexOfDate1 < words.length - 2) {
					time1 = words[indexOfDate1 + 2];
				}
				String time2 = words[indexOfDate2 + 1];
				if (isNext2 && indexOfDate2 < words.length - 2) {
					time2 = words[indexOfDate2 + 2];
				}
				String toDateFormat1 = convertToDateFormat(words, indexOfDate1);
				String toDateFormat2 = convertToDateFormat(words, indexOfDate2);
				results = getDoubleDateTime(toDateFormat1, toDateFormat2,
						time1, time2);
				if (results.getDate1() != null && results.getDate2() != null) {
					if (isNext1 && isNext2) {
						int[] toAdd = { indexOfFrom, indexOfTo, indexOfDate1,
								indexOfDate2, indexOfDate1 + 1,
								indexOfDate1 + 2, indexOfDate2 + 1,
								indexOfDate2 + 2 };
						addToUsedWords(toAdd);
					} else if (isNext1) {
						int[] toAdd = { indexOfFrom, indexOfTo, indexOfDate1,
								indexOfDate2, indexOfDate1 + 1,
								indexOfDate1 + 2, indexOfDate2 + 1 };
						addToUsedWords(toAdd);
					} else if (isNext2) {
						int[] toAdd = { indexOfFrom, indexOfTo, indexOfDate1,
								indexOfDate2, indexOfDate1 + 1,
								indexOfDate2 + 1, indexOfDate2 + 2 };
						addToUsedWords(toAdd);
					} else {
						int[] toAdd = { indexOfFrom, indexOfTo, indexOfDate1,
								indexOfDate2, indexOfDate1 + 1,
								indexOfDate2 + 1 };
						addToUsedWords(toAdd);
					}
				} else {
					noTime = true;
				}
			} catch (IndexOutOfBoundsException e) {
				noTime = true;
			}
			if (noTime) {
				results = handleTimedDateOnly(words, indexOfDate1,
						indexOfDate2, indexOfFrom, indexOfTo);
			}
		}
		return results;
	}

	public DoubleDate handleTimedDateOnly(String[] words, int indexOfDate1,
			int indexOfDate2, int indexOfFrom, int indexOfTo) {
		DoubleDate results;
		String toDateFormat1 = convertToDateFormat(words, indexOfDate1);
		String toDateFormat2 = convertToDateFormat(words, indexOfDate2);
		results = getDoubleDate(toDateFormat1, toDateFormat2);
		if (results.getDate1() != null && results.getDate2() != null) {
			if (isNextDate(words[indexOfDate1])
					&& isNextDate(words[indexOfDate2])) {
				int[] toAdd = { indexOfDate1 + 1, indexOfFrom, indexOfTo,
						indexOfDate1, indexOfDate2, indexOfDate2 + 1 };
				addToUsedWords(toAdd);
			} else if (isNextDate(words[indexOfDate1])) {
				int[] toAdd = { indexOfDate1 + 1, indexOfFrom, indexOfTo,
						indexOfDate1, indexOfDate2 };
				addToUsedWords(toAdd);
			} else if (isNextDate(words[indexOfDate2])) {
				int[] toAdd = { indexOfFrom, indexOfTo, indexOfDate1,
						indexOfDate2, indexOfDate2 + 1 };
				addToUsedWords(toAdd);
			} else {
				int[] toAdd = { indexOfFrom, indexOfTo, indexOfDate1,
						indexOfDate2 };
				addToUsedWords(toAdd);
			}
		}
		return results;
	}

	/**
	 * convertToDateFormat checks the word against an inbuilt set of keywords to
	 * return strings of the dates that they are supposed to represent
	 * 
	 * @param words
	 * @param index
	 * @return strings that represent the dates
	 */
	private String convertToDateFormat(String[] words, int index) {
		String curWord = words[index];
		String result = "";
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		String nextWord = "";
		switch (curWord) {
		case ("today"):
			result = dateFormats.get(0).format(today);
			break;
		case ("tomorrow"):
			cal.setTime(today);
			cal.add(Calendar.DATE, 1);
			Date tomorrow = cal.getTime();
			result = dateFormats.get(0).format(tomorrow);
			break;
		case ("yesterday"):
			cal.setTime(today);
			cal.add(Calendar.DATE, -1);
			Date ysd = cal.getTime();
			result = dateFormats.get(0).format(ysd);
			break;
		case ("next"):
			nextWord = words[index + 1];
			if (nextWord.equals("month")) {
				cal.setTime(today);
				cal.add(Calendar.MONTH, 1);
				Date nextMonth = cal.getTime();
				result = dateFormats.get(0).format(nextMonth);
			} else if (nextWord.equals("year")) {
				cal.setTime(today);
				cal.add(Calendar.YEAR, 1);
				Date nextYear = cal.getTime();
				result = dateFormats.get(0).format(nextYear);
			} else {
				result = findNextWeekday(nextWord, DAYSINWEEK);
			}
			break;
		default:
			// Deals with the days (ie. if type day of today, taken as today
			result = findNextWeekday(curWord, 0);
		}
		return result;
	}

	private String findNextWeekday(String day, int offset) {
		String result = day;
		Calendar cal = Calendar.getInstance();
		int today = cal.get(Calendar.DAY_OF_WEEK);
		int toAdd = -1;
		boolean isInvalidDay = false;
		switch (day) {
		case ("monday"):
			if (today == Calendar.MONDAY) {
				toAdd = DAYSINWEEK;
			} else {
				toAdd = (Calendar.SATURDAY - today + 2) % DAYSINWEEK;
			}
			break;
		case ("tuesday"):
			if (today == Calendar.TUESDAY) {
				toAdd = DAYSINWEEK;
			} else {
				toAdd = (Calendar.SATURDAY - today + 3) % DAYSINWEEK;
			}
			break;
		case ("wednesday"):
			if (today == Calendar.WEDNESDAY) {
				toAdd = DAYSINWEEK;
			} else {
				toAdd = (Calendar.SATURDAY - today + 4) % DAYSINWEEK;
			}
			break;
		case ("thursday"):
			if (today == Calendar.THURSDAY) {
				toAdd = DAYSINWEEK;
			} else {
				toAdd = (Calendar.SATURDAY - today + 5) % DAYSINWEEK;
			}
			break;
		case ("friday"):
			if (today == Calendar.FRIDAY) {
				toAdd = DAYSINWEEK;
			} else {
				toAdd = (Calendar.SATURDAY - today + 6) % DAYSINWEEK;
			}
			break;
		case ("saturday"):
			if (today == Calendar.SATURDAY) {
				toAdd = DAYSINWEEK;
			} else {
				toAdd = (Calendar.SATURDAY - today + 7) % DAYSINWEEK;
			}
			break;
		case ("sunday"):
			if (today == Calendar.SUNDAY) {
				toAdd = DAYSINWEEK;
			} else {
				toAdd = (Calendar.SATURDAY - today + 8) % DAYSINWEEK;
			}
			break;
		default:
			// Implying that it is not one of the "new" NLP formats
			isInvalidDay = true;
		}
		if (!isInvalidDay) {
			Date todayDate = new Date();
			cal.setTime(todayDate);
			toAdd += offset;
			cal.add(Calendar.DATE, toAdd);
			Date nextXDay = cal.getTime();
			result = dateFormats.get(0).format(nextXDay);
		}
		return result;
	}

	private DoubleDate getDoubleDateTime(String date1, String date2,
			String time1, String time2) {
		Date dateTimeObj1 = null;
		Date dateTimeObj2 = null;
		String dateTime1 = date1 + " " + time1;
		String dateTime2 = "";
		if (date2 == null && time2 != null) {
			dateTime2 = date1 + " " + time2;
		} else {
			dateTime2 = date2 + " " + time2;
		}
		for (int i = 0; i < dateTimeFormats.size(); i++) {
			SimpleDateFormat dateForm = dateTimeFormats.get(i);
			dateForm.setLenient(false);
			try {
				dateTimeObj1 = dateForm.parse(dateTime1);
				break;
			} catch (ParseException e) {
			}

		}
		for (int i = 0; i < dateTimeFormats.size(); i++) {
			SimpleDateFormat dateForm = dateTimeFormats.get(i);
			dateForm.setLenient(false);
			try {
				dateTimeObj2 = dateForm.parse(dateTime2);
				break;
			} catch (ParseException e) {
			}
		}
		return new DoubleDate(dateTimeObj1, dateTimeObj2);
	}

	private DoubleDate getDoubleDate(String date1, String date2) {
		Date dateObj1 = null;
		Date dateObj2 = null;
		if (date1 != null) {
			for (int i = 0; i < dateFormats.size(); i++) {
				SimpleDateFormat dateForm = dateFormats.get(i);
				dateForm.setLenient(false);
				try {
					dateObj1 = dateForm.parse(date1);
					break;
				} catch (ParseException e) {
				}
			}
		}
		if (date2 != null) {
			for (int i = 0; i < dateFormats.size(); i++) {
				SimpleDateFormat dateForm = dateFormats.get(i);
				dateForm.setLenient(false);
				try {
					dateObj2 = dateForm.parse(date2);
					break;
				} catch (ParseException e) {
				}
			}
		}
		return new DoubleDate(dateObj1, dateObj2);
	}

	/**
	 * handleUntimedTask uses the variables provided to parse a word array into
	 * the corresponding date object. usedWords is also updated here to that it
	 * can be used later for removal of all date related words individual date
	 * objects are null when no date(s) are found
	 * 
	 * @param words
	 * @param indexOfDate1
	 * @param indexOfDate2
	 * @param indexOfFrom
	 * @param indexOfTo
	 * @param indexOfFormat
	 * @return DoubleDate object representing DateFrom and DateTo respectively
	 */
	private DoubleDate handleUntimedTask(String[] words, int indexOfDate1,
			int indexOfDate2, int indexOfFrom, int indexOfTo) {
		DoubleDate result = null;
		boolean noTime = false;
		boolean isNext = false;
		if (indexOfDate1 != words.length - 1 && indexOfDate1 > -1) {
			String toDateFormat = convertToDateFormat(words, indexOfDate1);
			String time1 = "";
			isNext = isNextDate(words[indexOfDate1]);
			if (isNext && indexOfDate1 != words.length - 2) {
				time1 = words[indexOfDate1 + 2];
			} else {
				time1 = words[indexOfDate1 + 1];
			}
			result = getDoubleDateTime(toDateFormat, null, time1, null);
			if (result.getDate1() != null) {
				if (isNext) {
					int[] toAdd = { indexOfDate1, indexOfDate1 + 1,
							indexOfDate1 + 2 };
					addToUsedWords(toAdd);
				} else {
					int[] toAdd = { indexOfDate1, indexOfDate1 + 1 };
					addToUsedWords(toAdd);
				}
			} else {
				noTime = true;
			}
		} else if (indexOfDate1 < 0) {
			return new DoubleDate(null, null);
		} else {
			noTime = true;
		}
		if (noTime) {
			result = handleUntimedDateOnly(words, indexOfDate1, isNext);
		}
		return result;
	}

	private void addToUsedWords(int[] toAdd) {
		for (int i = 0; i < toAdd.length; i++) {
			usedWords.add(toAdd[i]);
		}
	}

	public DoubleDate handleUntimedDateOnly(String[] words, int indexOfDate1,
			boolean isNext) {
		DoubleDate result;
		String toDateFormat = convertToDateFormat(words, indexOfDate1);
		result = getDoubleDate(toDateFormat, null);
		if (result.getDate1() != null) {
			if (isNext) {
				usedWords.add((Integer) indexOfDate1 + 1);
			}
			usedWords.add((Integer) indexOfDate1);
		} else {
			runLogger();
			LOGGER.log(Level.SEVERE, MESSAGE_INVALIDINDEX);
			closeHandler();
		}
		return result;
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

	private boolean isNextDate(String word) {
		return word.equals("next");
	}

	/**
	 * convertUpdate returns a commandInfo object representing the input
	 * strings. This is only used when update is the comdType
	 * 
	 * @param message
	 * @param comdType
	 * @return CommandInfo
	 */
	private Command convertUpdate(String message, String comdType) {

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
		if (dateFrom!= null && dateTo!= null){
			if (dateFrom.compareTo(dateTo)>=0){
				runLogger();
				LOGGER.log(Level.WARNING, MESSAGE_INVALIDDATES);
				closeHandler();
				return null;
			}
		}
		return new UpdateCommand(taskDesc, dateFrom, dateTo, labels,
				toUpdateFrom);
	}

	/**
	 * convertSort converts a string into a sequence of labels represented in
	 * the task object
	 * 
	 * @param message
	 * @param comdType
	 * @return the command object representing the sortcommand
	 */
	private Command convertSort(String message, String comdType) {
		ArrayList<String> labels = new ArrayList<String>();
		String[] words = message.trim().split("\\s+");
		if (words.length > 0) {
			labels = new ArrayList<String>();
			for (int i = 0; i < words.length; i++) {
				labels.add(words[i]);
			}
		}
		return new SortCommand(null, null, null, labels, null);
	}
}
