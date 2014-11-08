package mytasks.logic.command;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mytasks.file.FeedbackObject;
import mytasks.logic.controller.MyTasksLogicController;
import mytasks.logic.parser.MyTasksParser;

//@author A0108543J
/**
 * HideCommand extends Command object to follow OOP standards
 */
public class HideCommand extends Command {

	// private variables
	private MyTasksLogicController mController;

	public HideCommand(String comdDes, Date fromDateTime, Date toDateTime,
			ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mController = MyTasksLogicController.getInstance(false);
	}

	@Override
	public FeedbackObject execute() {
		
		List<String> temp = mController.obtainPrintableOutput();
		ArrayList<String> referenceLabels = new ArrayList<String>();
		ArrayList<String> availableLabels = new ArrayList<String>();
		ArrayList<String> toHide = super.getTask().getLabels();
		ArrayList<String> toReturnArray = new ArrayList<String>();
		ArrayList<Integer> toUse = new ArrayList<Integer>();
		addHashtags(toHide);
		hasSearched = false;

		if (toHide == null) {
			FeedbackObject toReturn = new FeedbackObject("No arguments found", false);
			return toReturn;
		}
		// TODO known bug: cannot use all as label
		for (int i = 0; i < temp.size(); i++) {
			availableLabels.add(locateLabels(temp.get(i)));
			referenceLabels.add(locateLabels(temp.get(i)));
		}
		
		for (int i = 0; i < toHide.size(); i++) {
			boolean haveThisWord = false;
			String thisWord = toHide.get(i);
			for (int j = 0; j<availableLabels.size(); j++){
				String curLabel = availableLabels.get(j);
				if (curLabel.equals(thisWord)){
					haveThisWord = true;
					toUse.add(j);
				} else if (curLabel.contains(thisWord)){
					haveThisWord = true;
					curLabel = curLabel.replace(thisWord, "");
					availableLabels.set(j, curLabel);
				}
			}
			if (!haveThisWord && !thisWord.equals("#all")) {
				FeedbackObject toReturn = new FeedbackObject("Invalid label", false);
				return toReturn;
			}
		}
		if(toHide.contains("#all")){
			toReturnArray = referenceLabels;
		} else {
			for (int i = 0; i<toUse.size(); i++){
				toReturnArray.add(referenceLabels.get(toUse.get(i)));
			}
		}
		mController.toggleHide(true);
		mController.hideLabels(toReturnArray);
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
	public FeedbackObject undo() {
		throw new UnsupportedOperationException();
	}

}
