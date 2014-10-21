package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

/**
 * DeleteCommand extends Command to follow OOP standards
 * 
 * @author Wilson
 *
 */
public class DeleteCommand extends Command {

	public DeleteCommand(String comdDes, Date fromDateTime, Date toDateTime,
			ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
	}

	@Override
	String execute() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
