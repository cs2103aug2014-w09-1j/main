package mytasks.file;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mytasks.parser.MyTasksParser;

/**
 * Task object represents a single task with all the relevant fields that is
 * essential to a task
 * 
 * @author Wilson
 *
 */
public class Task {

	// Private local variables
	private String mDescription;
	private Date mFromDateTime = null;
	private Date mToDateTime = null;
	private ArrayList<String> mLabels;

	// Constructor
	public Task(String details, Date fromDateTime, Date toDateTime,
					ArrayList<String> labels) {
		mDescription = details;
		mFromDateTime = fromDateTime;
		mToDateTime = toDateTime;
		mLabels = labels;
	}

	public Task(Task newTask) {
		mDescription = newTask.getDescription();
		mFromDateTime = newTask.getFromDateTime();
		mToDateTime = newTask.getToDateTime();
		mLabels = newTask.getLabels();
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String updateDesc) {
		mDescription = updateDesc;
	}

	public Date getFromDateTime() {
		return mFromDateTime;
	}

	public Date getToDateTime() {
		return mToDateTime;
	}

	public void setFromDateTime(Date dateTime) {
		mFromDateTime = dateTime;
	}

	public void setToDateTime(Date dateTime) {
		mToDateTime = dateTime;
	}

	public ArrayList<String> getLabels() {
		return mLabels;
	}

	public void setLabels(ArrayList<String> labels) {
		mLabels = new ArrayList<String>(); // for resetting after every update
		mLabels = labels;
	}

	public void setTask(Task newTask) {
		setDescription(newTask.getDescription());
		setFromDateTime(newTask.getFromDateTime());
		setToDateTime(newTask.getToDateTime());
		setLabels(newTask.getLabels());
	}

	public Task getClone() {
		SimpleDateFormat form = MyTasksParser.dateTimeFormats.get(1);
		Date newDate1 = null;
		Date newDate2 = null;
		try {
			if (mFromDateTime != null) {
				newDate1 = form.parse(form.format(mFromDateTime));
			}
			if (mToDateTime != null) {
				newDate2 = form.parse(form.format(mToDateTime));
			}
		} catch (ParseException e) {
			Logger.getInstance().log("Task: Unexpected Error");
		}
		ArrayList<String> newLabels = null;
		if (mLabels!=null){
			newLabels = new ArrayList<String>(mLabels);
		}
		return new Task(mDescription, newDate1, newDate2, newLabels);
	}

	@Override
	public String toString() {
		String dateToString = "";
		String dateFromString = "";
		if (mFromDateTime != null) {
			dateFromString = MyTasksParser.dateTimeFormats.get(0).format(
							mFromDateTime);
		}
		if (dateFromString.contains("00:00")) {
			dateFromString = MyTasksParser.dateFormats.get(0).format(
							mFromDateTime);
		}
		if (mToDateTime != null) {
			dateToString = MyTasksParser.dateTimeFormats.get(0).format(mToDateTime);
		}
		if (dateToString.contains("00:00")) {
			dateToString = MyTasksParser.dateFormats.get(0).format(mToDateTime);
		}

		String labelsToString = "";
		if (mLabels != null) {
			for (String s : mLabels) {
				labelsToString += " " + "#" + s;
			}
		}
		String result = "";
		if (dateFromString.equals("")) {
			result = String.format("%s%s ", mDescription, labelsToString);
		} else if (dateToString.equals("")) {
			result = String.format("%s on %s%s", mDescription, dateFromString,
							labelsToString);
		} else {
			result = String.format("%s from %s to %s%s", mDescription,
							dateFromString, dateToString, labelsToString);
		}
		return result.trim();
	}

	@Override
	public boolean equals(Object otherTask) {
		if (otherTask == this) {
			return true;
		}

		if (!(otherTask instanceof Task)) {
			return false;
		} else {
			Task other = (Task) otherTask;
			if (other.getDescription() == null) {
				if (mDescription != null) {
					return false;
				}
			} else {
				if (mDescription == null) {
					return false;
				}
				if (!other.getDescription().equals(mDescription)) {
					return false;
				}
			}

			if (other.getFromDateTime() == null) {
				if (mFromDateTime != null) {
					return false;
				}
			} else {
				if (mFromDateTime == null) {
					return false;
				}
				if (!other.getFromDateTime().equals(mFromDateTime)) {
					return false;
				}
			}

			if (other.getToDateTime() == null) {
				if (mToDateTime != null) {
					return false;
				}
			} else {
				if (mToDateTime == null) {
					return false;
				}
				if (!other.getToDateTime().equals(mToDateTime)) {
					return false;
				}
			}

			if (other.getLabels() == null) {
				if (mLabels != null) {
					return false;
				}
			} else {
				if (mLabels == null) {
					return false;
				}
				if (other.getLabels().size() != mLabels.size()) {
					return false;
				}
				for (int i = 0; i < mLabels.size(); i++) {
					if (!mLabels.get(i).equals(other.getLabels().get(i))) {
						return false;
					}
				}
			}
		}
		return true;
	}
}
