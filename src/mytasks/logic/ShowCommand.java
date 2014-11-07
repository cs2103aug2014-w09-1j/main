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

//@author A0108543J
public class ShowCommand extends Command {

	// private variables
	private LocalMemory mLocalMem;
	private MyTasksLogicController mController;
	private MemorySnapshotHandler mViewHandler;

	public ShowCommand(String comdDes, Date fromDateTime, Date toDateTime,
					ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mLocalMem = LocalMemory.getInstance();
		mController = MyTasksLogicController.getInstance(false);
		mViewHandler = MemorySnapshotHandler.getInstance();
	}

	@Override
	FeedbackObject execute() {
		
		ArrayList<String> temp = new ArrayList<String>(mViewHandler.getSnapshot(mLocalMem));
		ArrayList<String> availableLabels = new ArrayList<String>();
		ArrayList<String> toShow = super.getTask().getLabels();
		
		// if there are no labels to show
		if (toShow == null) {
			FeedbackObject toReturn = new FeedbackObject("No arguments found", false);
			return toReturn;
		}
		
		// to show all labels
		if (toShow.get(0).equals("all")) {
			toShow.remove(0);
			for (int i=0; i<temp.size(); i++) {
				toShow.add(locateLabels(temp.get(i)));
			}
			mController.toggleHide(false);
			mController.showLabels(toShow);
			
			ArrayList<String> labels = new ArrayList<String>();
			labels.add("all");
			HideCommand commandToUndo = new HideCommand(null, null, null, labels, null);			
			mLocalMem.undoPush(commandToUndo);
			mLocalMem.saveLocalMemory();
			
			FeedbackObject toReturn = new FeedbackObject("All labels shown", true);
			return toReturn;
		}
		
		for (int i=0; i<temp.size(); i++) {
			availableLabels.add(locateLabels(temp.get(i)));
		}
		// if labels to show not found in current list
		for (int i=0; i<toShow.size(); i++) {
			if (!availableLabels.contains(toShow.get(i))) {
				FeedbackObject toReturn = new FeedbackObject("Invalid label", false);
				return toReturn;
			}
		}
		
		ArrayList<String> labels = new ArrayList<String>();
		for (int i=0; i<toShow.size(); i++) {
			labels.add(toShow.get(i));
		}
		HideCommand commandToUndo = new HideCommand(null, null, null, labels, null);			
		mLocalMem.undoPush(commandToUndo);
		mLocalMem.saveLocalMemory();
		mController.toggleHide(false);
		mController.showLabels(toShow);
		
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
	
	@Override
	FeedbackObject undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
