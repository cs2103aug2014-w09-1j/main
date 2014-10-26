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

	public SortCommand(String comdDes, Date fromDateTime,
			Date toDateTime, ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mViewHandler = MemorySnapshotHandler.getInstance();
	}

	@Override
	String execute() {
		mViewHandler.setView(super.getTask().getLabels());
		return null;
	}

	@Override
	String undo() {
		// TODO Auto-generated method stub
		return null;
	}
}
