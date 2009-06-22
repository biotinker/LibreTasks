package edu.nyu.cs.omnidroid.uiresearchsimple;

import edu.nyu.cs.omnidroid.uiresearchsimple.model.Rule;


/**
 * When a user wants to create a new rule, we can
 * store their progress here. ListViews in different
 * Activities should directly render the Rule's contents.
 * This class should be able to save and restore to/from
 * the database.
 * 
 * We will probably change 
 */
public class RuleBuilder
{
	private static RuleBuilder mInstance;
	private Rule mRule;
	
	
	private RuleBuilder() {
		reset();
	}
	
	public static RuleBuilder instance() {
		if (mInstance == null) {
			mInstance = new RuleBuilder();
		}
		return mInstance;
	}
	
	public void reset() {
		mRule = null;
		mRule = new Rule();
	}
	
	public Rule getRule() {
		return mRule;
	}
}