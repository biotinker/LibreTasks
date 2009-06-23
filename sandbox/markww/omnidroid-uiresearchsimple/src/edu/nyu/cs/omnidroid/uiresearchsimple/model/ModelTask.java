package edu.nyu.cs.omnidroid.uiresearchsimple.model;


/**
 * Dummy representation of a Task. We'll modify
 * this to match the database model. These are 
 * actions to perform if a user's rule is true.
 */
public abstract class ModelTask extends ModelItem
{
	public ModelTask(String typeName) {
		super(typeName);
	}
}