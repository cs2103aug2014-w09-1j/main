package mytasks.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;
import mytasks.parser.MyTasksParser;

/**
 * HideCommand extends Command object to follow OOP standards
 * 
 * @author Shuan Siang
 *
 */

// @author A0108543J
public class ShowCommand extends Command {

	// private variables
	private MyTasksLogicController mController;

	public ShowCommand(String comdDes, Date fromDateTime, Date toDateTime,
			ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mController = MyTasksLogicController.getInstance(false);
	}

	@Override
	FeedbackObject execute() {

		ArrayList<String> availableLabelsToShow = mController.getHideLabels();
		ArrayList<String> toShow = super.getTask().getLabels();
		addHashtags(toShow);

		if (toShow == null) {
			FeedbackObject toReturn = new FeedbackObject("No arguments found",
					false);
			return toReturn;
		}

		for (int i = 0; i < toShow.size(); i++) {
			if (!availableLabelsToShow.contains(toShow.get(i))
					&& !toShow.get(i).equals("#all")) {
				FeedbackObject toReturn = new FeedbackObject("Invalid label",
						false);
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

		for (int i = 0; i < toShow.size(); i++) {
			availableLabelsToShow.remove(toShow.get(i));
		}
		if (availableLabelsToShow.size()==0) {
			mController.toggleHide(false);
		}
		
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

	private boolean isDates(String toCheck) {
		try {
			SimpleDateFormat curDateForm = MyTasksParser.dateFormats.get(1);
			curDateForm.parse(toCheck);
		} catch (ParseException e) {
			System.out.println("should see this");
			return false;
		}
		return true;
	}

	@Override
	FeedbackObject undo() {
		throw new UnsupportedOperationException();
	}

}
