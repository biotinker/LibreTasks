package edu.nyu.cs.omnidroid.core;

import java.util.Map;

import android.content.Intent;
import edu.nyu.cs.omnidroid.util.ExceptionMessageMap;
import edu.nyu.cs.omnidroid.util.OmnidroidException;

public class NotePadAction extends Action {
	public static final String ACTION_NAME = "Display Note";
	public static final String APP_NAME = ".NotePad";
	public static final String PARAM_PRESET_TEXT = "preset text";
	
	private String text = null;

	public NotePadAction(Map<String, String> parameters) throws OmnidroidException {
		super("nyu.edu.android.simplenotepad.NotePad", Action.BY_ACTIVITY);
		text = parameters.get(PARAM_PRESET_TEXT);
		if (text == null) {
			throw new OmnidroidException(120002, ExceptionMessageMap.getMessage(new Integer(120002)
			.toString()));
		}
	}

	@Override
	public Intent getIntent() {
		Intent intent = new Intent();
		intent.setClassName("nyu.edu.android.simplenotepad", "nyu.edu.android.simplenotepad.NotePad");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("TEXT", text);
		return intent;
	}

}
