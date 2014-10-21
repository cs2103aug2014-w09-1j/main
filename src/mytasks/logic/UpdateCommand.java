package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

/**
 * UpdateCommand extends Command to follow OOP standards
 * 
 * @author Wilson
 *
 */
public class UpdateCommand extends Command {

	public UpdateCommand(String comdType, String comdDes, Date fromDateTime,
			Date toDateTime, ArrayList<String> comdLabels, String updateDesc) {
		super(comdType, comdDes, fromDateTime, toDateTime, comdLabels,
				updateDesc);
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
