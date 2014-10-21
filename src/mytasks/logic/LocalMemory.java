package mytasks.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

import mytasks.file.MyTasks;
import mytasks.file.Task;
import mytasks.storage.IStorage;
import mytasks.storage.MyTasksStorage;

/**
 * LocalMemory holds all related task information in the current session and is
 * only required to read and write
 * to external memory minimally.
 * 
 * @author Wilson
 *
 */

@SuppressWarnings("serial")
class LocalMemory implements Serializable {

	// Private variables
	private static LocalMemory INSTANCE = null;
	private ArrayList<Task> mLocalMem = new ArrayList<Task>();
	private Stack<Command> undoStack = new Stack<Command>();
	private Stack<Command> redoStack = new Stack<Command>();
	private IStorage mStore;

	// Constructor
	private LocalMemory() {
		mStore = MyTasksStorage.getInstance();
	}

	protected static LocalMemory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LocalMemory();
		}
		return INSTANCE;
	}

	protected Object readResolve() {
		return INSTANCE;
	}

	protected void loadLocalMemory() {
		mLocalMem = mStore.readExtMem(MyTasks.DEFAULT_FILENAME);
	}

	protected void saveLocalMemory() {
		mStore.writeExtMem(mLocalMem);
	}

	protected ArrayList<Task> getLocalMem() {
		return mLocalMem;
	}
	
	protected void clearMemory(){
		mLocalMem.clear();
	}

	protected void add(Task userRequest) {
		mLocalMem.add(userRequest);
	}

	protected void remove(Task userRequest) {
		for (int i = 0; i < mLocalMem.size(); i++) {
			if (userRequest.getDescription().equals(
							mLocalMem.get(i).getDescription())) {
				mLocalMem.remove(i);
			}
		}
	}

	protected void update(String mToUpdateTaskDesc, Task userUpdate) {
		for (int i = 0; i < mLocalMem.size(); i++) {
			if (mToUpdateTaskDesc.equals(mLocalMem.get(i).getDescription())) {
				if (userUpdate.getDescription() != null) {
					mLocalMem.get(i)
									.setDescription(userUpdate.getDescription());
				}
				if (userUpdate.getFromDateTime() != null) {
					mLocalMem.get(i).setFromDateTime(
									userUpdate.getFromDateTime());
				}
				if (userUpdate.getToDateTime() != null) {
					mLocalMem.get(i).setToDateTime(userUpdate.getToDateTime());
				}
				if (userUpdate.getLabels() != null) {
					if (!userUpdate.getLabels().isEmpty()) {
						mLocalMem.get(i).setLabels(userUpdate.getLabels());
					}
				}
			}
		}

	}

	protected void print() {
		for (int i = 0; i < mLocalMem.size(); i++) {
			System.out.println("i: " + i);
			if (mLocalMem.get(i).getDescription() != null) {
				System.out.println(mLocalMem.get(i).getDescription());
			}
			if (mLocalMem.get(i).getFromDateTime() != null) {
				System.out.println(mLocalMem.get(i).getFromDateTime()
								.toString());
			}
			if (mLocalMem.get(i).getToDateTime() != null) {
				System.out.println(mLocalMem.get(i).getToDateTime().toString());
			}
			if (mLocalMem.get(i).getLabels() != null) {
				if (!mLocalMem.get(i).getLabels().isEmpty()) {
					for (int k = 0; k < mLocalMem.get(i).getLabels().size(); k++) {
						System.out.println("label: "
										+ mLocalMem.get(i).getLabels().get(k));
					}
				}
			}
		}
	}

	protected boolean search(Task userRequest) {
		boolean isFound = false;
		assert userRequest != null;

		if (userRequest.getDescription() != null
						&& userRequest.getLabels() != null) {
			for (int i = 0; i < mLocalMem.size(); i++) {
				if (haveSameDesc(userRequest, i)
								&& haveSameLabels(userRequest, i)) {
					System.out.println(mLocalMem.get(i).toString());
					isFound = true;
				}
			}
		} else if (userRequest.getDescription() != null) {
			for (int i = 0; i < mLocalMem.size(); i++) {
				if (haveSameDesc(userRequest, i)) {
					System.out.println(mLocalMem.get(i).toString());
					isFound = true;
				}
			}
		} else if (userRequest.getLabels() != null) {
			for (int i = 0; i < mLocalMem.size(); i++) {
				if (haveSameLabels(userRequest, i)) {
					System.out.println(mLocalMem.get(i).toString());
					isFound = true;
				}
			}
		}

		return isFound;
	}

	protected void undoPush(Command commandToUndo) {
		undoStack.push(commandToUndo);
	}

	protected Stack<Command> getUndoStack() {
		return undoStack;
	}
	
	protected void redoPush(Command commandToUndo) {
		redoStack.push(commandToUndo);
	}
	
	protected Stack<Command> getRedoStack() {
		return redoStack;
	}
	
	private boolean haveSameDesc(Task userRequest, int index) {
		String desc = userRequest.getDescription();

		if (mLocalMem.get(index).getDescription() != null
						&& mLocalMem.get(index).getDescription().contains(desc)) {
			return true;
		}
		return false;
	}

	private boolean haveSameLabels(Task userRequest, int index) {
		String userRequestedTaskLabelsToString = "";
		String mLocalMemTaskLabelsToString = "";

		if (userRequest.getLabels() != null) {
			for (String s : userRequest.getLabels()) {
				userRequestedTaskLabelsToString += s;
			}
		}
		if (mLocalMem.get(index).getLabels() != null) {
			for (String s : mLocalMem.get(index).getLabels()) {
				mLocalMemTaskLabelsToString += s;
			}
		}

		if (mLocalMemTaskLabelsToString.equals(userRequestedTaskLabelsToString)) {
			return true;
		}
		return false;
	}
	
	//Backdoor function to use for testing
	protected void clearMemory() {
		mLocalMem.clear();
	}

}
