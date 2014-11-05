package mytasks.file;

//@author A0114302A
public class FeedbackObject {
	
	private boolean mIsValid;
	private String mFeedback;
	
	public FeedbackObject(String feedback, boolean isValid){
		mFeedback = feedback;
		mIsValid = isValid;
	}
	
	public boolean getValidity() {
		return mIsValid;
	}
	
	public String getFeedback() {
		return mFeedback;
	}
}
