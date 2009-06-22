package edu.nyu.cs.omnidroid.uiresearchsimple.dlg;

import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelFilter;

/**
 * In Android we listen for dismissal of Dialog instances.
 * When being dismissed, we should be able to ask filter
 * dialogs whether the user filled out filter info correctly,
 * and if so, produce a <code>ModelFilter</code> instance
 * representing their input data.
 */
public interface IDlgFilter
{
	public ModelFilter getConstructedFilter();
    public boolean getDidUserConstructFilter();
}