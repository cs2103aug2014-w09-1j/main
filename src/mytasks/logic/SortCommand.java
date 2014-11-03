package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

/**
 * SortCommand extends Command object to follow OOP standards
 * 
 * @author Wilson
 *
 */
public class SortCommand extends Command {
	
	private MemorySnapshotHandler mViewHandler;
	private LocalMemory mLocalMem;

	public SortCommand(String comdDes, Date fromDateTime,
			Date toDateTime, ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mViewHandler = MemorySnapshotHandler.getInstance();
		mLocalMem = LocalMemory.getInstance();
	}

	@Override
	String execute() {
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
		return output + "sorted";
	}

	@Override
	String undo() {
		String[] prevSettings = mViewHandler.getView();
		ArrayList<String> prevLabels = new ArrayList<String>();
		for (int i = 0; i<prevSettings.length; i++){
			prevLabels.add(prevSettings[i]);
		}
		Command commandToUndo = new SortCommand(null, null, null, prevLabels, null);
		mLocalMem.redoPush(commandToUndo);
		mViewHandler.setView(super.getTask().getLabels());
		return null;
	}
}
