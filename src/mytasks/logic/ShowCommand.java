package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;

public class ShowCommand extends Command {

	public ShowCommand(String comdDes, Date fromDateTime, Date toDateTime,
					ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		// TODO Auto-generated constructor stub
	}

	@Override
	FeedbackObject execute() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	FeedbackObject undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
