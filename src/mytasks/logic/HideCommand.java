package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;

/**
 * HideCommand extends Command object to follow OOP standards
 * 
 * @author Shuan Siang
 *
 */

//@author A0108543J
public class HideCommand extends Command {

	// private variables
	private LocalMemory mLocalMem;
	private MemorySnapshotHandler mViewHandler;
	private ArrayList<String> listOfStringAfterHiding = new ArrayList<String>();
	private boolean ifContainLabels;

	public HideCommand(String comdDes, Date fromDateTime, Date toDateTime,
					ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		// TODO Auto-generated constructor stub
		mLocalMem = LocalMemory.getInstance();
		mViewHandler = MemorySnapshotHandler.getInstance();
	}

	@Override
	FeedbackObject execute() {
		// TODO Auto-generated method stub
		ArrayList<String> temp = new ArrayList<String>(mViewHandler.getSnapshot(mLocalMem));
		ArrayList<String> labels = new ArrayList<String>();

		System.out.println("--- checking temp ---");
		for (int i=0; i<temp.size(); i++) {
			System.out.println(i + ": " + temp.get(i));
		}
		System.out.println("--- checking temp ---");

		for (int i=0; i<temp.size(); i++) {
			labels = locateLabels(temp.get(i));

			if (!ifContainLabels) {
				listOfStringAfterHiding.add(temp.get(i));
				continue;
			}
			
			for (int j=0; j<labels.size(); j++) {
				if (!labels.get(j).equals(super.getTaskDetails())) {
					System.out.println("insdie");
					listOfStringAfterHiding.add(temp.get(i));
					break;
				}
			}

			System.out.println("--- checking listOfStringAfterHiding --- start");
			for (int k=0; k<listOfStringAfterHiding.size(); k++) {
				System.out.println(k + ": " + listOfStringAfterHiding.get(k));
			}
			System.out.println("--- checking listOfStringAfterHiding --- end");

		}
		String resultString = "Label with '" + super.getTaskDetails() + "' hidden";
		FeedbackObject result = new FeedbackObject(resultString, true);
		return result;
	}

	ArrayList<String> locateLabels(String temp) {
		ifContainLabels = false;
		ArrayList<String> results = new ArrayList<String>();

		if (!temp.contains("#")) {
			return null;
		}
		
		String[] words = temp.split("\\s+");
		for (int i=0; i<words.length; i++) {
			String currentWord = words[i];
			char firstLetter = currentWord.charAt(0);
			if (firstLetter == '#') {
				results.add(currentWord.substring(1));
				ifContainLabels = true;
			}
		}
		if (results.size() != 0) {
			return results;
		}
		return null;
	}

	public ArrayList<String> getList() {
		return listOfStringAfterHiding;
	}
	
	@Override
	FeedbackObject undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
