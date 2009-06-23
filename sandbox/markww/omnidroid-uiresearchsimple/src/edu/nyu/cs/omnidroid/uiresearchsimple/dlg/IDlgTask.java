package edu.nyu.cs.omnidroid.uiresearchsimple.dlg;

import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelTask;

/**
 * In Android we listen for dismissal of Dialog instances.
 * When being dismissed, we should be able to ask task
 * dialogs whether the user filled out task info correctly,
 * and if so, produce a <code>ModelTask</code> instance
 * representing their input data.
 */
public interface IDlgTask
{
	public ModelTask getConstructedTask();
    public boolean getDidUserConstructTask();
}