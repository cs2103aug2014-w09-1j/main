package mytasks.file;

import java.util.ArrayList;

/**
 * CommandType instance used to access different fields of a command that has been parsed.
 * @author Wilson
 *
 */
public class CommandType {
	
	//Private variables
	private String mType;
	private String mDesc;
	private ArrayList<String> mLabels;
	
	//Constructor
	public CommandType(String comdType, String comdDes, ArrayList<String> comdLabels) {
		
		mType = comdType;
		mDesc = comdDes;
		mLabels = comdLabels;
		
	}
	
	private String getType() {
		return mType;
	}
	
	private String getDesc() {
		return mDesc;
	}
	
	private ArrayList<String> getLabels() {
		return mLabels;
	}
	
}
