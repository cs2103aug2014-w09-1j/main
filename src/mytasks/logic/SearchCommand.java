package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

/**
 * SearchCommand extends Command object to follow OOP standards
 * 
 * @author Wilson
 *
 */
public class SearchCommand extends Command {

	public SearchCommand(String comdType, String comdDes, Date fromDateTime,
			Date toDateTime, ArrayList<String> comdLabels, String updateDesc) {
		super(comdType, comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
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
