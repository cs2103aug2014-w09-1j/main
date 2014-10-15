package mytasks.file;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Logger class implements singleton pattern and has the basic capabilities of a
 * Java logger
 * 
 * @author Wilson
 *
 */
@SuppressWarnings("serial")
public class Logger implements Serializable {

	private static Logger INSTANCE = null;
	private ArrayList<String> info;

	private Logger() {
		info = new ArrayList<String>();
	}

	public static Logger getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Logger();
		}
		return INSTANCE;
	}

	protected Object readResolve() {
		return INSTANCE;
	}

	public void log(String input) {
		info.add(input);
	}

	public String print() {
		String result = "";
		for (int i = 0; i < info.size(); i++) {
			result += info.get(i) + "\n";
		}
		return result;
	}
}