package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;

//@author A0108543J
public class HideCommand extends Command {

	// private variables
	private LocalMemory mLocalMem;
	
	public HideCommand(String comdDes, Date fromDateTime, Date toDateTime,
					ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		// TODO Auto-generated constructor stub
		mLocalMem = LocalMemory.getInstance();
	}

	@Override
	FeedbackObject execute() {
		// TODO Auto-generated method stub
		
		
		
		String resultString = "Label with '" + super.getTaskDetails() + "' hidden";
		FeedbackObject result = new FeedbackObject(resultString, true);
		return result;
	}

	@Override
	FeedbackObject undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
