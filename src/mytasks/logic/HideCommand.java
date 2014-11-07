package mytasks.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mytasks.file.FeedbackObject;
import mytasks.parser.MyTasksParser;

/**
 * HideCommand extends Command object to follow OOP standards
 * 
 * @author Shuan Siang
 *
 */

// @author A0108543J
public class HideCommand extends Command {

	// private variables
	private MyTasksLogicController mController;

	public HideCommand(String comdDes, Date fromDateTime, Date toDateTime,
			ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mController = MyTasksLogicController.getInstance(false);
	}

	@Override
	FeedbackObject execute() {
		
		List<String> temp = mController.obtainPrintableOutput();
		ArrayList<String> availableLabels = new ArrayList<String>();
		ArrayList<String> toHide = super.getTask().getLabels();
		addHashtags(toHide);

		if (toHide == null) {
			FeedbackObject toReturn = new FeedbackObject("No arguments found", false);
			return toReturn;
		}
		// TODO known bug: cannot use all as label
		System.out.println(temp.size());
		for (int i = 0; i < temp.size(); i++) {
			System.out.println(temp.get(i));
			availableLabels.add(locateLabels(temp.get(i)));
		}
		
		for (int i = 0; i < toHide.size(); i++) {
			if (!availableLabels.contains(toHide.get(i)) && !toHide.get(i).equals("#all")) {
				FeedbackObject toReturn = new FeedbackObject("Invalid label", false);
				return toReturn;
			}
		}
		if(toHide.contains("#all")){
			toHide = availableLabels;
		}
		mController.toggleHide(true);
		mController.hideLabels(toHide);
		FeedbackObject toReturn = new FeedbackObject("Labels hidden", true);
		return toReturn;
	}

	private String locateLabels(String temp) {
		String firstWord = null;
		if (temp != null) {
			firstWord = temp.split("\\s+")[0];
		}
		return firstWord;
	}
	
	private void addHashtags(ArrayList<String> toHide){
		for (int i = 0; i<toHide.size(); i++) {
			if (!toHide.get(i).equals("N.A.") && !isDates(toHide.get(i))){
				String temp = "#" + toHide.get(i);
				toHide.set(i, temp);
			}
		}
	}
	
	private boolean isDates(String toCheck) {
		try {
			SimpleDateFormat curDateForm = MyTasksParser.dateFormats.get(1);
			curDateForm.parse(toCheck);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	@Override
	FeedbackObject undo() {
		throw new UnsupportedOperationException();
	}

}
