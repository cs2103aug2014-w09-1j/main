package mytasks.logic.command;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;
import mytasks.logic.MyTasksLogicController;
import mytasks.logic.parser.MyTasksParser;

//@author A0108543J
/**
 * HideCommand extends Command object to follow OOP standards
 */
public class ShowCommand extends Command {

	// private variables
	private MyTasksLogicController mController;

	public ShowCommand(String comdDes, Date fromDateTime, Date toDateTime,
			ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mController = MyTasksLogicController.getInstance(false);
	}

	@Override
	public FeedbackObject execute() {

		ArrayList<String> referenceLabelsToShow = cloneList(mController.getHideLabels());
		ArrayList<String> availableLabelsToShow = cloneList(mController.getHideLabels());
		ArrayList<String> toShow = super.getTask().getLabels();
		ArrayList<Integer> toUse = new ArrayList<Integer>(); 
		addHashtags(toShow);

		if (toShow == null) {
			FeedbackObject toReturn = new FeedbackObject("No arguments found",
					false);
			return toReturn;
		}

		for (int i = 0; i < toShow.size(); i++) {
			boolean haveThisWord = false;
			String thisWord = toShow.get(i);
			for (int j = 0; j<availableLabelsToShow.size(); j++){
				String curLabel = availableLabelsToShow.get(j);
				if (curLabel.equals(thisWord)){
					haveThisWord = true;
					toUse.add(j);
				} else if (curLabel.contains(thisWord)){
					haveThisWord = true;
					curLabel = curLabel.replace(thisWord, "");
					availableLabelsToShow.set(j, curLabel);
				}
			}
			if (!haveThisWord && !thisWord.equals("#all")) {
				FeedbackObject toReturn = new FeedbackObject("Invalid label", false);
				return toReturn;
			}
		}

		if (toShow.contains("#all")) {
			mController.clearHideLabels();
			mController.toggleHide(false);
			FeedbackObject toReturn = new FeedbackObject("All labels shown",
					true);
			return toReturn;
		} 

		ArrayList<String> newToHide = new ArrayList<String>();
		for (int i = 0; i < referenceLabelsToShow.size(); i++) {
			if (!toUse.contains(i)) {
				newToHide.add(referenceLabelsToShow.get(i));
			}
		}
		if (newToHide.size()==0) {
			mController.clearHideLabels();
			mController.toggleHide(false);
			FeedbackObject toReturn = new FeedbackObject("All labels shown",
					true);
			return toReturn;
		}
		mController.hideLabels(newToHide);
		FeedbackObject toReturn = new FeedbackObject("Labels shown", true);
		return toReturn;
	}

	private void addHashtags(ArrayList<String> toHide) {
		for (int i = 0; i < toHide.size(); i++) {
			if (!toHide.get(i).equals("N.A.") && !isDates(toHide.get(i))) {
				String temp = "#" + toHide.get(i);
				toHide.set(i, temp);
			}
		}
	}
	
	private ArrayList<String> cloneList(ArrayList<String> list) {
	    ArrayList<String> clone = new ArrayList<String>(list.size());
	    for(String item: list) clone.add(item);
	    return clone;
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
