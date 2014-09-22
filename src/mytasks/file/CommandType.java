package mytasks.file;

import java.util.ArrayList;

/**
 * CommandType instance used to access different fields of a command that has been parsed.
 * 
 * TODO research NLP and its relevant libraries. This object may no longer be needed but will be left
 * here for the time being
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
		//TODO missing datetime field
		mType = comdType;
		mDesc = comdDes;
		mLabels = comdLabels;
		
	}
	
	public String getType() {
		return mType;
	}
	
	public String getDesc() {
		return mDesc;
	}
	
	public ArrayList<String> getLabels() {
		return mLabels;
	}
	
}
