package mytasks.parser;

import java.text.DateFormat;
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
//	public static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
//	public static DateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	public static List<SimpleDateFormat> dateFormats = new ArrayList<SimpleDateFormat>() {{
		add(new SimpleDateFormat("dd.MM.yyyy"));
		add(new SimpleDateFormat("dd.MM.yyyy HH:mm"));
	    add(new SimpleDateFormat("dd-MMM-yyyy"));
		
	}};
	
	//Constructor
	public MyTasksParser() {
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
				break;
			case "undo":
			case "redo":
				if (words.length!=1){					//Checks if there are any extra input
					return null;
				}
				return new CommandInfo(comdType, null, null, null, null, null);
			case "update":
				break;
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
			CommandInfo result = new CommandInfo(comdType, taskDesc, dateTime , null, labels, updateFrom);*/
			return null;
		}
		return null;
	}

	private String removeCommand(String input, String comdType) {
		String messageAndDateAndLabels = input.substring(comdType.length());
		return messageAndDateAndLabels;
	}
	
	private CommandInfo convertStandard(String message, String comdType) {
		String[] words = message.split("\\s+");
		ArrayList<String> labels = locateLabels(words);
		String[] withoutLabels = removeLabels(words);
		//Find date objects
		//Remove date terms to get description
		//return commandinfo object
		String[] hashtagged = splitHashtag(message);
		String messageAndDateTime = hashtagged[0].trim();
		Date dateTime = extractDate(words);
		String taskDesc = null;
		String updateFrom = null;
		//taskDesc = removeDate(messageAndDateTime,dateTime);
		return new CommandInfo(comdType, taskDesc, dateTime, null, labels, null);
	}
	
	/**
	 * locateLabels looks at an array and extracts the labels denoted by "#". It then stores the words that are labels
	 * into an arraylist and returns the arraylist
	 * @param words
	 * @return arraylist of strings that represent labels
	 */
	public ArrayList<String> locateLabels(String[] words) {
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
	
	public String[] removeLabels(String[] words) {
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
	
	private String[] splitHashtag(String messageAndDateAndLabels) {
		String[] hashtagged = messageAndDateAndLabels.split("[#]+");
		return hashtagged;
	}
	
	/**
	 * extractDate checks if the given array of words contains a date/time that complies with the current
	 * useable formats and returns the date object if so.
	 * @param words arrays
	 * @return Date(object) of the task
	 */
	public Date extractDate(String[] words) {
		Date dateTime = null;
		int indexOfFrom = -1;
		int indexOfTo = -1;
		final int MAX_SPACING = 3;
		int indexOfDate = -1;
		int indexOfFormat = -1;
		
		//Loops to check if "from" and "to" exist in words
		for (int i = 0; i<words.length; i++) {
			String curWord = words[i];
			if (curWord == "from") {
				indexOfFrom = i;
			}
			if (curWord == "to" && indexOfFrom>=i-MAX_SPACING && indexOfFrom!=-1) {
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
					indexOfDate = i;
					indexOfFormat = j;
					break;
				} catch (ParseException e) {
				}
			}
			if (dateTime == null) {
				//TODO: do other checks for natural words such as today, next monday here. by setting
				//indexOfDate to i
			}
		}
		
		if (isTimedTask(indexOfFrom, indexOfTo)) {
			//TODO: add timed task handling
		} else {
			if (indexOfDate!=words.length-1 && indexOfDate>-1){
				String temp = words[indexOfDate] + " " + words[indexOfDate+1];
				try {
					dateTime = dateFormats.get(1).parse(temp);
				} catch (ParseException ne) {
					try {
						SimpleDateFormat dateForm = dateFormats.get(indexOfFormat);
						dateTime = dateForm.parse(words[indexOfDate]);
					} catch (ParseException e) {
						System.out.println("Unexpected error");
					} catch (IndexOutOfBoundsException e) {
						//Implying no date found
					}
				}
			} else {
				try {
					SimpleDateFormat dateForm = dateFormats.get(indexOfFormat);
					dateForm.setLenient(false);
					dateTime = dateForm.parse(words[indexOfDate]);
				} catch (ParseException e) {
					System.out.println("Unexpected error");
				} catch (IndexOutOfBoundsException e) {
					//Implying no date found
				}
			}
		}
		return dateTime;
	}
	
	private boolean isTimedTask(int indexFrom, int indexTo) {
		if (indexFrom!=-1 && indexTo!=-1){
			return true;
		}
		return false;
	}
	/*
	private String removeDate(String message, Date dateTime) {
		if (dateTime==null) {
			return message;
		}
		String dateAndTime = dateTimeFormat.format(dateTime);
		String temp = message.replace(dateAndTime, "");
		String date = dateFormat.format(dateTime);
		String temp2 = temp.replace(date,"").trim();
		if(temp2.equals("")){
			return null;
		}
		return temp2;
	}*/
	
	/**
	 * splitLabels converts a string array that has been split by "#" into an arraylist.
	 * @param hashtagged is an array of strings that have been split by "#"
	 * @return ArrayList<String> containing all labels
	 */
	private ArrayList<String> splitLabels (String[] hashtagged) {
		if (hashtagged.length<=1) {
			return null;
		}
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 1; i< hashtagged.length; i++){
			if (i == hashtagged.length-1) {
				String[] tempWords = hashtagged[i].split("\\s+");
				result.add(tempWords[0]);
			} else {
				result.add(hashtagged[i].trim());
			}
		}
		return result;
	}
	
	private String[] splitMinus(String line){
		String delims = "[-]+";
		String[] newLine = line.split(delims);
		return newLine;
	}
}
