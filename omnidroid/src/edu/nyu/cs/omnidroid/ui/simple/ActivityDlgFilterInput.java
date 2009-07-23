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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
public class ActivityDlgFilterInput extends Activity implements FactoryDynamicUI.DlgDynamicInput {
  /**
   * When the user hits the OK button, we interpret it to mean that they entered valid filter info,
   * and we can try to construct a filter from it. In the OK handler, we execute the one function 
   * of this handler to see if their input is OK for the specific filter type chosen.
   */
  private FactoryDynamicUI.InputDone handlerInputDone;

  /**
   * We don't know what inner UI elements we have, but we need to support UI state between
   * orientation changes etc. We can execute the two methods of this handler to let the UI elements
   * handle state save/load themselves.
   */
  private FactoryDynamicUI.DlgPreserveState handlerStatePreserver;

  /** Our state keeper. */
  private SharedPreferences state;

  /**
   * If the user hits the OK button, and a filter is constructed OK, we set this flag to true so we
   * know not to bother saving any state information when closing.
   */
  private boolean preserveStateOnClose;

  private static final String KEY_STATE = "StateDlgFilterInput";

  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dlg_filter_input);
    // setTitle("Filters");

    Button btnOk = (Button) findViewById(R.id.activity_dlg_filter_input_btnOk);
    btnOk.setOnClickListener(listenerBtnClickOk);

    Button btnHelp = (Button) findViewById(R.id.activity_dlg_filter_input_btnHelp);
    btnHelp.setOnClickListener(listenerBtnClickInfo);

    Button btnCancel = (Button) findViewById(R.id.activity_dlg_filter_input_btnCancel);
    btnCancel.setOnClickListener(listenerBtnClickCancel);

    // Add dynamic content now based on our filter type.
    ModelFilter modelFilter = RuleBuilder.instance().getChosenModelFilter();
    DataType ruleFolterDataOld = RuleBuilder.instance().getChosenRuleFilterDataOld();
    FactoryDynamicUI.buildUIForFilter(this, modelFilter, ruleFolterDataOld);

    // Restore our UI state.
    state = getSharedPreferences(ActivityDlgFilterInput.KEY_STATE, Context.MODE_WORLD_READABLE
        | Context.MODE_WORLD_WRITEABLE);
    handlerStatePreserver.loadState(state);

    // By default, we want to save UI state on close.
    preserveStateOnClose = true;
  }

  @Override
  protected void onPause() {

    super.onPause();

    // Conditionally save our UI state.
    SharedPreferences.Editor prefsEditor = state.edit();
    prefsEditor.clear();
    prefsEditor.commit();
    if (preserveStateOnClose) {
      handlerStatePreserver.saveState(prefsEditor);
    }
    prefsEditor.commit();
  }

  private View.OnClickListener listenerBtnClickOk = new View.OnClickListener() {
    public void onClick(View v) {
      // Have the listener try to construct a full ModelFilter for us now
      // based on our dynamic UI content.
      ModelRuleFilter filter;
      try {
        filter = (ModelRuleFilter) handlerInputDone.onInputDone();
      } catch (Exception ex) {
        UtilUI.showAlert(v.getContext(), "Sorry!",
            "There was an error creating your filter, your input was probably bad!:\n"
                + ex.toString());
        return;
      }

      // Set our constructed filter so the parent activity can pick it up.
      RuleBuilder.instance().setChosenRuleFilter(filter);

      // We can now dismiss ourselves. Our parent listeners can pick up the
      // constructed filter once we unwind the dialog stack using the
      // RuleBuilder singleton instance. We don't need of preserve our UI 
      // state upon closing now.
      preserveStateOnClose = false;
      finish();
    }
  };

  private View.OnClickListener listenerBtnClickInfo = new View.OnClickListener() {
    public void onClick(View v) {
      // TODO: (markww) Add help info about filter.
      UtilUI.showAlert(v.getContext(), "Sorry!",
          "We'll implement an info dialog about this filter soon!");
    }
  };

  private View.OnClickListener listenerBtnClickCancel = new View.OnClickListener() {
    public void onClick(View v) {
      preserveStateOnClose = false;
      finish();
    }
  };

  /** Implements FactoryDynamicUI.DlgFilterInput. */
  public Context getContext() {
    return this;
  }

  /** Implements FactoryDynamicUI.DlgFilterInput. */
  public void addDynamicLayout(LinearLayout ll) {
    LinearLayout llContent = (LinearLayout) findViewById(
      R.id.activity_dlg_filter_input_llDynamicContent);
    llContent.addView(ll);
  }

  /** Implements FactoryDynamicUI.DlgFilterInput. */
  public void setHandlerOnFilterInputDone(FactoryDynamicUI.InputDone handler) {
    handlerInputDone = handler;
  }

  public void setHandlerPreserveState(FactoryDynamicUI.DlgPreserveState handler) {
    handlerStatePreserver = handler;
  }
}