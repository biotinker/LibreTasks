/*******************************************************************************
 * Copyright 2009 OmniDroid - http://code.google.com/p/omnidroid 
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *     
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 *******************************************************************************/
package edu.nyu.cs.omnidroid.ui.simple;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.core.datatypes.DataType;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelFilter;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelRuleFilter;

/**
 * This dialog is a shell to contain UI elements specific to different filters. Given a filter ID,
 * we can construct the inner UI elements using <code>FactoryFilterToUI</code>.
 */
public class DlgFilterInput extends Dialog implements FactoryDynamicUI.IDlgDynamicInput {
  /**
   * When the user hits the OK button, we interpret it to mean that they entered valid filter info,
   * and we can try to construct a filter from it. In the OK handler, we execute the one function of
   * this handler to see if their input is OK for the specific filter type chosen.
   */
  private FactoryDynamicUI.IInputDone mHandlerInputDone;

  /**
   * We don't know what inner UI elements we have, but we need to support UI state between
   * orientation changes etc. We can execute the two methods of this handler to let the UI elements
   * handle state save/load themselves.
   */
  private FactoryDynamicUI.IDlgPreserveState mHandlerStatePreserver;

  /** Our state keeper. */
  private SharedPreferences mState;

  /**
   * If the user hits the OK button, and a filter is constructed OK, we set this flag to true so we
   * know not to bother saving any state information when closing.
   */
  private boolean mPreserveStateOnClose;

  public DlgFilterInput(Context context,  
                        ModelFilter modelFilter,
                        DataType dataOld) 
  {
    super(context);
    setContentView(R.layout.dlg_filter_input);

    Button btnOk = (Button) findViewById(R.id.dlg_filter_input_btnOk);
    btnOk.setOnClickListener(listenerBtnClickOk);

    Button btnHelp = (Button) findViewById(R.id.dlg_filter_input_btnHelp);
    btnHelp.setOnClickListener(listenerBtnClickInfo);

    Button btnCancel = (Button) findViewById(R.id.dlg_filter_input_btnCancel);
    btnCancel.setOnClickListener(listenerBtnClickCancel);

    // Add dynamic content now based on our filter type.
    FactoryDynamicUI.buildUIForFilter(this, modelFilter, dataOld);

    // Maximize ourselves.
    // UtilUI.inflateDialog((LinearLayout)findViewById(R.id.dlg_filter_input_ll_main));

    // Restore our UI state.
    mState = context.getSharedPreferences("StateDlgFilterInput", Context.MODE_PRIVATE);
    mHandlerStatePreserver.loadState(mState);

    // By default, we want to save UI state on close.
    mPreserveStateOnClose = false;
  }

  @Override
  protected void onStart() {
  }

  @Override
  protected void onStop() {

    super.onStop();

    // Conditionally save our UI state.
    mState.edit().clear();
    if (mPreserveStateOnClose) {
      mHandlerStatePreserver.saveState(mState);
    }
    mState.edit().commit();
  }

  private android.view.View.OnClickListener listenerBtnClickOk = new android.view.View.OnClickListener() {
    public void onClick(View v) {
      // Have the listener try to construct a full ModelFilter for us now
      // based on our dynamic UI content.
      ModelRuleFilter filter;
      try {
        filter = (ModelRuleFilter)mHandlerInputDone.onInputDone();
      } catch (Exception ex) {
        UtilUI.showAlert(v.getContext(), "Sorry!",
            "There was an error creating your filter, your input was probably bad!:\n"
                + ex.toString());
        return;
      }

      // Set our constructed filter so the parent activity can pick it up.
      DlgItemBuilderStore.instance().setBuiltItem(filter);

      // We can now dismiss ourselves. Our parent listeners can pick up the
      // constructed filter once we unwind the dialog stack using the
      // FilterBuilder singleton instance. We have no need of preserving our
      // UI state upon closing now.
      mPreserveStateOnClose = false;
      dismiss();
    }
  };

  private android.view.View.OnClickListener listenerBtnClickInfo = new android.view.View.OnClickListener() {
    public void onClick(View v) {
      UtilUI.showAlert(v.getContext(), "Sorry!",
          "We'll implement an info dialog about this filter soon!");
    }
  };

  private android.view.View.OnClickListener listenerBtnClickCancel = new android.view.View.OnClickListener() {
    public void onClick(View v) {
      mPreserveStateOnClose = false;
      dismiss();
    }
  };

  // Already implemented by Android's Dialog interface.
  // /** Implements FactoryFilterToUI.IDlgFilterInput. */
  // public Context getContext();

  // Already implemented by Android's Dialog interface.
  // /** Implements FactoryFilterToUI.IDlgFilterInput. */
  // public void setTitle(CharSequence title);

  /** Implements FactoryFilterToUI.IDlgFilterInput. */
  public void addDynamicLayout(LinearLayout ll) {
    LinearLayout llContent = (LinearLayout) findViewById(R.id.dlg_filter_input_llDynamicContent);
    llContent.addView(ll);
  }

  /** Implements FactoryFilterToUI.IDlgFilterInput. */
  public void setHandlerOnFilterInputDone(FactoryDynamicUI.IInputDone handler) {
	mHandlerInputDone = handler;
  }

  public void setHandlerPreserveState(FactoryDynamicUI.IDlgPreserveState handler) {
    mHandlerStatePreserver = handler;
  }
}