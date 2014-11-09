package mytasks.logic.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

import mytasks.file.MyTasksController;
import mytasks.file.Task;
import mytasks.logic.command.Command;
import mytasks.storage.IStorage;
import mytasks.storage.MyTasksStorage;

/**
 * LocalMemory holds all related task information in the current session and is
 * only required to read and write
 * to external memory minimally.
 * 
 * @author Huiwen, Michael, Shuan Siang 
 *
 */

@SuppressWarnings("serial")
public class LocalMemory implements Serializable {

	// Private variables
	private static LocalMemory INSTANCE = null;
	private static ArrayList<Task> mLocalMem = new ArrayList<Task>();
	private Stack<Command> undoStack = new Stack<Command>();
	private Stack<Command> redoStack = new Stack<Command>();
	private IStorage mStore;

	// Constructor
	private LocalMemory() {
		mStore = MyTasksStorage.getInstance();
	}

	public static LocalMemory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LocalMemory();
		}
		return INSTANCE;
	}
	
	// for testing purposes 
	protected static void clearLocalMem() {
		mLocalMem = new ArrayList<Task>(); 
	}

	protected Object readResolve() {
		return INSTANCE;
	}

	protected void loadLocalMemory() {
		mLocalMem = mStore.readExtMem(MyTasksController.DEFAULT_FILENAME);
	}

	public void saveLocalMemory() {
		mStore.writeExtMem(mLocalMem);
	}

	public ArrayList<Task> getLocalMem() {
		return mLocalMem;
	}

	public void add(Task userRequest) {
		mLocalMem.add(userRequest);
	}

	public void remove(Task userRequest) {
		for (int i = 0; i < mLocalMem.size(); i++) {
			if (userRequest.getDescription().equals(
							mLocalMem.get(i).getDescription())) {
				mLocalMem.remove(i);
			}
		}
	}
	
	public void remove(int index) {
		mLocalMem.remove(index);
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


	//@author A0108543J
	public void undoPush(Command commandToUndo) {
		undoStack.push(commandToUndo);
	}

	public Stack<Command> getUndoStack() {
		return undoStack;
	}
	
	public void redoPush(Command commandToUndo) {
		redoStack.push(commandToUndo);
	}
	
	public Stack<Command> getRedoStack() {
		return redoStack;
	}

	//Backdoor function to use for testing
	public void clearMemory() {
		mLocalMem.clear();
	}

}
