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
		
		ArrayList<String> temp = new ArrayList<String>(mViewHandler.getSnapshot(mLocalMem));
		ArrayList<String> availableLabels = new ArrayList<String>();
		ArrayList<String> toHide = super.getTask().getLabels();
		addHashtags(toHide);

		if (toHide == null) {
			FeedbackObject toReturn = new FeedbackObject("No arguments found", false);
			return toReturn;
		}
		// TODO known bug: cannot use all as label
		for (int i = 0; i < temp.size(); i++) {
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
			if (!toHide.get(i).equals("N.A.")){
				String temp = "#" + toHide.get(i);
				toHide.set(i, temp);
			}
		}
	}

	@Override
	FeedbackObject undo() {
		throw new UnsupportedOperationException();
	}

}
