package mytasks.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mytasks.file.CommandInfo;

/**
 * MyTasksParser interprets userinput to useable data structures to work with in the Logic component. Naive version for now
 * @author Wilson
 *
 */
public class MyTasksParser {
	
	public static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	public static DateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	
	//Constructor
	public MyTasksParser() {
		dateFormat.setLenient(false);
		dateTimeFormat.setLenient(false);
	}
	
	/**
	 * parseInput reads and converts String input into its equivalent CommandType object with the respective
	 * fields
	 * @param input from user
	 * @return CommandInfo object that is used to execute input
	 */
	public CommandInfo parseInput (String input){
		String[] words = input.split("\\s+");
		if (words.length != 0) {
			String comdType = words[0];
			String messageAndDateTimeAndLabels = removeCommand(input, comdType);
			String[] hashtagged = splitHashtag(messageAndDateTimeAndLabels);
			String messageAndDateTime = hashtagged[0].trim();
			Date dateTime = extractDate(words);
			String taskDesc = removeDate(messageAndDateTime,dateTime);
			ArrayList<String> labels = splitLabels(hashtagged);
			
			CommandInfo result = new CommandInfo(comdType,taskDesc,dateTime,labels);
			return result;
		}
		return null;
	}

	private String removeCommand(String input, String comdType) {
		String messageAndDateAndLabels = input.substring(comdType.length());
		return messageAndDateAndLabels;
	}

	private String[] splitHashtag(String messageAndDateAndLabels) {
		String[] hashtagged = messageAndDateAndLabels.split("[#]+");
		return hashtagged;
	}
	
	private Date extractDate(String[] words) {
		Date dateTime = null;
		for (int i = 0; i<words.length; i++) {
			try {
				dateFormat.parse(words[i]);
				if (i!=words.length-1){
					String temp = words[i] + " " + words[i+1];
					try {
						dateTimeFormat.parse(temp);
						dateTime = dateTimeFormat.parse(temp);
					} catch (ParseException ne) {
						dateTime = dateFormat.parse(words[i]);
					}
				} else {
					dateTime = dateFormat.parse(words[i]);
				}
				break;
			} catch (ParseException e) {
			}
		}
		return dateTime;
	}
	
	private String removeDate(String message, Date dateTime) {
		if (dateTime==null) {
			return message;
		}
		String dateAndTime = dateTimeFormat.format(dateTime);
		String temp = message.replace(dateAndTime, "");
		String date = dateFormat.format(dateTime);
		return temp.replace(date,"").trim();
	}
	
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
				result.add(hashtagged[i]);
			}
		}
		return result;
	}

//	public static void main(String[] args) {
//		MyTasksParser test = new MyTasksParser();
//		CommandInfo testcase = test.parseInput("add dinner 23.09.2014 12:00");
//		System.out.println(testcase.getTask().getDateTime().toString());
//		
//	}
}
