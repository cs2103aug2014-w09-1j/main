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

	public SortCommand(String comdType, String comdDes, Date fromDateTime,
			Date toDateTime, ArrayList<String> comdLabels, String updateDesc) {
		super(comdType, comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
	}

	@Override
	String execute() {
		// TODO Auto-generated method stub
		return null;
	}
}