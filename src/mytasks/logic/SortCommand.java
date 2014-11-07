package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;

/**
 * SortCommand extends Command object to follow OOP standards
 */

//@author A0112139R
public class SortCommand extends Command {
	
	private MemorySnapshotHandler mViewHandler;
	private LocalMemory mLocalMem;
	private MyTasksLogicController mController;

	public SortCommand(String comdDes, Date fromDateTime,
			Date toDateTime, ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mViewHandler = MemorySnapshotHandler.getInstance();
		mController = MyTasksLogicController.getInstance(false);
		mLocalMem = LocalMemory.getInstance();
	}

	@Override
	FeedbackObject execute() {
		String[] prevSettings = mViewHandler.getView();
		ArrayList<String> prevLabels = new ArrayList<String>();
		for (int i = 0; i<prevSettings.length; i++){
			prevLabels.add(prevSettings[i]);
		}
		Command commandToUndo = new SortCommand(null, null, null, prevLabels, null);
		mLocalMem.undoPush(commandToUndo);
		mViewHandler.setView(super.getTask().getLabels());
		haveSearched = false;
		
		String output = "";
		for (int i=0; i < super.getTask().getLabels().size(); i++){
			output +=  super.getTask().getLabels().get(i) + " ";
		}
		String resultString = output + "sorted";
		FeedbackObject result = new FeedbackObject(resultString,true);
		mController.toggleHide(false);
		return result;
	}
	
	//@author A0114302A
	@Override
	FeedbackObject undo() {
		String[] prevSettings = mViewHandler.getView();
		ArrayList<String> prevLabels = new ArrayList<String>();
		for (int i = 0; i<prevSettings.length; i++){
			prevLabels.add(prevSettings[i]);
		}
		Command commandToUndo = new SortCommand(null, null, null, prevLabels, null);
		mLocalMem.redoPush(commandToUndo);
		mViewHandler.setView(super.getTask().getLabels());
		mController.toggleHide(false);
		return null;
	}
}
