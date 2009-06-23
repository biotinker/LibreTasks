package edu.nyu.cs.omnidroid.uiresearchsimple.model;

import edu.nyu.cs.omnidroid.uiresearchsimple.R;


/**
 * Sends an SMS.
 */
public class ModelTaskSms extends ModelTask
{
	private String mNumber;
	private String mText;
	
	public ModelTaskSms(String number, String text) {
		super("SMS");
		mNumber = number;
		mText = text;
	}
	
	public String getNumber() {
		return mNumber;
	}
	
	public String getText() {
		return mText;
	}
	
	public ModelDisplayInfo getDisplayInfo() {
		String messageFragment;
		if (mText != null && mText.length() > 15) {
			messageFragment = mText.substring(0, 14) + "...";
		}
		else {
			messageFragment = mText + "...";
		}
		return new ModelDisplayInfo(R.drawable.icon_task_sms, getTypeName() + ": " + mNumber + ", " + messageFragment, "Contact filter description...");
	}
	
	public String getDlgClassName() {
		return "edu.nyu.cs.omnidroid.uiresearchsimple.dlg.DlgTaskSms";
	}
}