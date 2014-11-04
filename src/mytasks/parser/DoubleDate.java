package mytasks.parser;

//@author A0114302A
import java.util.Date;

/**
 * DoubleDate is a data structure to hold 2 dates for the parser to use. This is
 * a protected class that cannot be accessed from outside package
 */
class DoubleDate {

	private Date mDate1 = null;
	private Date mDate2 = null;

	protected DoubleDate(Date date1, Date date2) {
		mDate1 = date1;
		mDate2 = date2;
	}

	public Date getDate1() {
		return mDate1;
	}

	public Date getDate2() {
		return mDate2;
	}

	@Override
	public boolean equals(Object otherTask) {
		if (otherTask == this) {
			return true;
		}

		if (!(otherTask instanceof DoubleDate)) {
			return false;
		} else {
			DoubleDate otherDate = (DoubleDate) otherTask;
			if (otherDate.getDate1() == null) {
				if (mDate1 != null) {
					return false;
				}
			} else {
				if (!otherDate.getDate1().equals(mDate1)) {
					return false;
				}
			}
			if (otherDate.getDate2() == null) {
				if (mDate2 != null) {
					return false;
				}
			} else {
				if (!otherDate.getDate2().equals(mDate2)) {
					return false;
				}
			}
		}
		return true;
	}
}