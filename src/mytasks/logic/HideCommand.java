package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;

/**
 * HideCommand extends Command object to follow OOP standards
 * 
 * @author Shuan Siang
 *
 */

// @author A0108543J
public class HideCommand extends Command {

	// private variables
	private LocalMemory mLocalMem;
	private MyTasksLogicController mController;
	private MemorySnapshotHandler mViewHandler;

	public HideCommand(String comdDes, Date fromDateTime, Date toDateTime,
			ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mLocalMem = LocalMemory.getInstance();
		mController = MyTasksLogicController.getInstance(false);
		mViewHandler = MemorySnapshotHandler.getInstance();
	}

	@Override
	FeedbackObject execute() {
		mController.toggleHide(false);
		mController.clearHideLabels();
		
		ArrayList<String> temp = new ArrayList<String>(mViewHandler.getSnapshot(mLocalMem));
		ArrayList<String> availableLabels = new ArrayList<String>();
		ArrayList<String> toHide = super.getTask().getLabels();

		// if there are no labels to hide
		if (toHide == null) {
			FeedbackObject toReturn = new FeedbackObject("No arguments found", false);
			return toReturn;
		}
		
		// to hide all labels
		// TODO address issue where there will be a label #all (clash)
		if (toHide.get(0).equals("all")) {
			toHide.remove(0);
			for (int i=0; i<temp.size(); i++) {
				toHide.add(locateLabels(temp.get(i)));
			}
			mController.toggleHide(true);
			mController.hideLabels(toHide);
			
			ArrayList<String> labels = new ArrayList<String>();
			labels.add("all");
			ShowCommand commandToUndo = new ShowCommand(null, null, null, labels, null);
			mLocalMem.undoPush(commandToUndo);
			mLocalMem.saveLocalMemory();
			
			FeedbackObject toReturn = new FeedbackObject("All labels hidden", true);
			return toReturn;
		}
		
		
		for (int i = 0; i < temp.size(); i++) {
			availableLabels.add(locateLabels(temp.get(i)));
		}
		// if labels to hide not found in current list
		for (int i = 0; i < toHide.size(); i++) {
			if (!availableLabels.contains(toHide.get(i))) {
				FeedbackObject toReturn = new FeedbackObject("Invalid label", false);
				return toReturn;
			}
		}
		
		ArrayList<String> labels = new ArrayList<String>();
		for (int i=0; i<toHide.size(); i++) {
			labels.add(toHide.get(i));
		}
		ShowCommand commandToUndo = new ShowCommand(null, null, null, labels, null);
		mLocalMem.undoPush(commandToUndo);
		mLocalMem.saveLocalMemory();
		
		mController.toggleHide(true);
		mController.hideLabels(toHide);
		// Hw has to check toggleValue to see if she needs to hide anything.
		// She will checks content of hidelabels for what to hide
		// Remember to toggle off when view is changed. (think of cases when need to toggle
		// Do the converse for showlabels
		FeedbackObject toReturn = new FeedbackObject("Labels hidden", true);
		return toReturn;
	}

	private String locateLabels(String temp) {
		String firstWord = null;
		if (temp != null) {
			firstWord = temp.split("\\s+")[0];
			char firstLetter = firstWord.charAt(0);
			if (firstLetter == '#') {
				firstWord = firstWord.substring(1);
			}
		}
		return firstWord;
	}

	//TODO implement showing of time
	// TODO implement undo and redo
	@Override
	FeedbackObject undo() {
		ArrayList<String> labelsToRevert = super.getTask().getLabels();
		if (labelsToRevert.get(0).equals("all")) {
			ArrayList<String> temp = new ArrayList<String>(mViewHandler.getSnapshot(mLocalMem));
			ArrayList<String> toShow = new ArrayList<String>();
			for (int i=0; i<temp.size(); i++) {
				toShow.add(locateLabels(temp.get(i)));
			}
			mController.toggleShow(true);
			mController.showLabels(toShow);
		}
		
		FeedbackObject toReturn = new FeedbackObject("All labels shown", true);
		return toReturn;
	}

}
