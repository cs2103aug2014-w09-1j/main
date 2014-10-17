package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

/**
 * RedoCommand extends Command to follow OOP standard
 * 
 * @author Wilson
 *
 */
public class RedoCommand extends Command {

	public RedoCommand(String comdType, String comdDes, Date fromDateTime,
			Date toDateTime, ArrayList<String> comdLabels, String updateDesc) {
		super(comdType, comdDes, fromDateTime, toDateTime, comdLabels,
				updateDesc);
	}

	@Override
	String execute() {
		// TODO Auto-generated method stub
		return null;
	}

}
