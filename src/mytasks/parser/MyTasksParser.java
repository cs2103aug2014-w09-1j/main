package mytasks.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mytasks.file.CommandInfo;

/**
 * MyTasksParser interprets userinput to useable data structures to work with in the Logic component. Naive version for now
 * @author Wilson
 *
 */
public class MyTasksParser implements IParser {
	
	//Date and Time formats that are currently useable.
	@SuppressWarnings("serial")
	public static List<SimpleDateFormat> dateFormats = new ArrayList<SimpleDateFormat>() {{
		add(new SimpleDateFormat("dd.MM.yyyy"));
		add(new SimpleDateFormat("dd.MM.yyyy HH:mm"));
	    add(new SimpleDateFormat("dd.MMM.yyyy"));
		add(new SimpleDateFormat("dd.MMM.yyyy HH:mm"));
		add(new SimpleDateFormat("HH:mm"));
	}};
	private ArrayList<Integer> usedWords;
	
	//Constructor
	public MyTasksParser() {
		usedWords = new ArrayList<Integer>();
	}
	

	/**
	 * {@inheritDoc}
	 */
	public CommandInfo parseInput (String input){
		String[] words = input.split("\\s+");
		if (words.length != 0) {
			String comdType = words[0];
			String withoutComdType = removeCommand(input, comdType);
			switch(comdType) {
			case "add":
			case "search":
			case "delete":
				CommandInfo temp = convertStandard(withoutComdType, comdType);
				return temp;
			case "undo":
			case "redo":
				if (words.length!=1){					//Checks if there are any extra input
					return null;
				}
				return new CommandInfo(comdType, null, null, null, null, null);
			case "update":
				CommandInfo temp2 = convertUpdate(withoutComdType, comdType);
				return temp2;
			default:
				return null;
			}
			/*
			if (comdType.equals("update")){
				String[] updateSplit = splitMinus(messageAndDateTime);
				updateFrom = updateSplit[0].trim();
				try{
					taskDesc = removeDate(updateSplit[1].trim(),dateTime);
				} catch (IndexOutOfBoundsException e) {
					if (labels!=null){
						//Update labels only.
						taskDesc = null;
					} else {
						//Incorrect form of update is passed
						return null;
					}
				}
			}
			CommandInfo result = new CommandInfo(comdType, taskDesc, dateTime , null, labels, updateFrom);
			return null;*/
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
		if (taskDesc.equals("")|| taskDesc.length() == 0) { 		//Ie. no task description
			return null;
		}
		return new CommandInfo(comdType, taskDesc, dateFrom, dateTo, labels, null);
	}
	
	/**
	 * locateLabels looks at an array and extracts the labels denoted by "#". It then stores the words that are labels
	 * into an arraylist and returns the arraylist
	 * @param words
	 * @return arraylist of strings that represent labels
	 */
	protected ArrayList<String> locateLabels(String[] words) {
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i<words.length; i++){
			String curWord = words[i];
			char firstLetter = curWord.charAt(0);
			if (firstLetter == '#'){
				String toAdd = curWord.substring(1);
				result.add(toAdd);
			}
		}
		if (result.size()!=0){
			return result;
		}
		return null;
	}
	
	protected String[] removeLabels(String[] words) {
		String result = "";
		for (int i = 0; i<words.length; i++){
			String curWord = words[i];
			char firstLetter = curWord.charAt(0);
			if (firstLetter != '#'){
				result+=curWord + " ";
			}
		}
		return result.split("\\s+");
	}
	
	/**
	 * extractDate checks if the given array of words contains a date/time that complies with the current
	 * useable formats and returns the date object if so.
	 * @param words arrays
	 * @return Date(object) of the task
	 */
	protected DoubleDate extractDate(String[] words) {
		Date dateTimeObj1 = null;
		Date dateTimeObj2 = null;
		int indexOfFrom = -1;
		int indexOfTo = -1;
		final int MAX_SPACING = 3;
		int indexOfDate1 = -1;
		int indexOfDate2 = -1;
		int indexOfFormat = -1;
		usedWords = new ArrayList<Integer>();
		
		//Loops to check if "from" and "to" exist in words
		for (int i = 0; i<words.length; i++) {
			String curWord = words[i];
			if (curWord.equals("from")) {
				indexOfFrom = i;
			}
			if (curWord.equals("to") && indexOfFrom>=i-MAX_SPACING && indexOfFrom!=-1) {
				indexOfTo = i;
				break;
			}
		}
		
		//Loops to check if any word fits any of the formats that we accept. Will be expanded to NLP
		for (int i = 0; i<words.length; i++) {
			for (int j = 0; j<dateFormats.size(); j++) {
				SimpleDateFormat dateForm = dateFormats.get(j);
				try {
					dateForm.setLenient(false);
					dateForm.parse(words[i]);
					if (indexOfDate1 == -1) {
						indexOfDate1 = i;
					} else {
						indexOfDate2 = i;
					}
					indexOfFormat = j;
				} catch (ParseException e) {
				}
			}
			//TODO: do other checks for natural words such as today, next monday here. by setting
			//indexOfDate to i OR refer to below, where it indicates no time is found
			
		}
		if (isTimedTask(indexOfFrom, indexOfTo)) {
			if (indexOfDate2 == -1){
				try {
					String date = words[indexOfDate1];
					String time1 = words[indexOfFrom+1];
					String time2 = words[indexOfTo+1];
					String dateTime1 = date + " " + time1;
					String dateTime2 = date + " " + time2;
					SimpleDateFormat dateForm = dateFormats.get(indexOfFormat+1);
					dateForm.setLenient(false);
					dateTimeObj1 = dateForm.parse(dateTime1);
					dateTimeObj2 = dateForm.parse(dateTime2);
					usedWords.add((Integer)indexOfFrom);
					usedWords.add((Integer)indexOfTo);
					usedWords.add((Integer)indexOfDate1);
					usedWords.add((Integer)indexOfFrom+1);
					usedWords.add((Integer)indexOfTo+1);
				} catch (IndexOutOfBoundsException e) {
					//Implying no date/time found
				} catch (ParseException e) {
					System.out.println("Unexpected error1");
				}
			} else {
				try {
					String time1 = words[indexOfDate1+1];
					String time2 = words[indexOfDate2+1];
					String date1 = words[indexOfDate1];
					String date2 = words[indexOfDate2];
					String dateTime1 = date1 + " " + time1;
					String dateTime2 = date2 + " " + time2;
					SimpleDateFormat dateForm = dateFormats.get(indexOfFormat+1);
					dateForm.setLenient(false);
					dateTimeObj1 = dateForm.parse(dateTime1);
					dateTimeObj2 = dateForm.parse(dateTime2);
					usedWords.add((Integer)indexOfFrom);
					usedWords.add((Integer)indexOfTo);
					usedWords.add((Integer)indexOfDate1);
					usedWords.add((Integer)indexOfDate2);
					usedWords.add((Integer)indexOfDate1+1);
					usedWords.add((Integer)indexOfDate2+1);
				} catch (IndexOutOfBoundsException e) {
					//Implying no time found
					try {
						String date1 = words[indexOfDate1];
						String date2 = words[indexOfDate2];
						SimpleDateFormat dateForm = dateFormats.get(indexOfFormat);
						dateForm.setLenient(false);
						dateTimeObj1 = dateForm.parse(date1);
						dateTimeObj2 = dateForm.parse(date2);
						usedWords.add((Integer)indexOfFrom);
						usedWords.add((Integer)indexOfTo);
						usedWords.add((Integer)indexOfDate1);
						usedWords.add((Integer)indexOfDate2);
					} catch (ParseException e1) {
						System.out.println("Unexpected error2");
					} catch (IndexOutOfBoundsException e1) {
						//Implying no date found
					}
				} catch (ParseException e) {
					System.out.println("Unexpected error2");
				}
			}
		} else {
			if (indexOfDate1!=words.length-1 && indexOfDate1>-1){
				String temp = words[indexOfDate1] + " " + words[indexOfDate1+1];
				try {
					dateTimeObj1 = dateFormats.get(1).parse(temp);
					usedWords.add((Integer)indexOfDate1);
					usedWords.add((Integer)indexOfDate1+1);
				} catch (ParseException ne) {
					try {
						SimpleDateFormat dateForm = dateFormats.get(indexOfFormat);
						dateTimeObj1 = dateForm.parse(words[indexOfDate1]);
						usedWords.add((Integer)indexOfDate1);
					} catch (ParseException e) {
						System.out.println("Unexpected error3");
					} catch (IndexOutOfBoundsException e) {
						//Implying no date found
					}
				}
			} else {
				try {
					SimpleDateFormat dateForm = dateFormats.get(indexOfFormat);
					dateForm.setLenient(false);
					dateTimeObj1 = dateForm.parse(words[indexOfDate1]);
					usedWords.add((Integer)indexOfDate1);
				} catch (ParseException e) {
					System.out.println("Unexpected error4");
				} catch (IndexOutOfBoundsException e) {
					//Implying no date found
				}
			}
		}
		return new DoubleDate(dateTimeObj1, dateTimeObj2);
	}
	
	private boolean isTimedTask(int indexFrom, int indexTo) {
		if (indexFrom!=-1 && indexTo!=-1){
			return true;
		}
		return false;
	}
	
	private String removeDate(String[] words) {
		String result = "";
		for (int i = 0; i<words.length; i++) {
			if (!usedWords.contains((Integer)i)){
				result+=words[i] + " ";
			}
		}
		return result.trim();
	}
	
	private CommandInfo convertUpdate(String message, String comdType){

		String delims = "[-]+";
		String[] messageSplit = message.split(delims);
		if (messageSplit.length!=2) {					//Wrong format of update command
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
		return new CommandInfo(comdType, taskDesc, dateFrom, dateTo, labels, toUpdateFrom);
	}
	
	protected class DoubleDate {
		
		private Date mDate1;
		private Date mDate2;
		
		private DoubleDate(Date date1, Date date2){
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
