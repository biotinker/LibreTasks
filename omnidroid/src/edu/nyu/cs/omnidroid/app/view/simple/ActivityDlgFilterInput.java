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
package edu.nyu.cs.omnidroid.app.view.simple;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.controller.datatypes.DataType;
import edu.nyu.cs.omnidroid.app.controller.util.DataTypeValidationException;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelFilter;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelRuleFilter;

/**
 * This dialog is a shell to contain UI elements specific to different filters. Given a filter ID,
 * we can construct the inner UI elements using <code>FactoryDynamicUI</code>.
 */
public class ActivityDlgFilterInput extends Activity implements FactoryDynamicUI.DlgDynamicInput {

  private static final String KEY_STATE = "StateDlgFilterInput";
  public static final int TIME_DIALOG_ID = 0;

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
   * By default true, we want to save the UI state when onPause is called. If the user hits the
   * OK button, and their input constructs a valid filter, we set this to false to skip saving
   * the UI state. We need this to distinguish between onPause being called in response to the
   * phone orientation being changed, or the user explicitly telling the dialog to close.
   */
  private boolean preserveStateOnClose;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Link up controls from the xml layout resource file.
    initializeUI();

    // Restore our UI state.
    state = getSharedPreferences(ActivityDlgFilterInput.KEY_STATE, Context.MODE_WORLD_READABLE
        | Context.MODE_WORLD_WRITEABLE);
    try {
      handlerStatePreserver.loadState(state);
    } catch (DataTypeValidationException e) {
      Log.e("ActivityDlgFilterInput", "Can't load state, " + e);
    }

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
      prefsEditor.commit();
    }
  }

  private void initializeUI() {
    setContentView(R.layout.activity_dlg_filter_input);

    Button btnOk = (Button) findViewById(R.id.activity_dlg_filter_input_btnOk);
    btnOk.setOnClickListener(listenerBtnClickOk);

    Button btnHelp = (Button) findViewById(R.id.activity_dlg_filter_input_btnHelp);
    btnHelp.setOnClickListener(listenerBtnClickInfo);

    Button btnCancel = (Button) findViewById(R.id.activity_dlg_filter_input_btnCancel);
    btnCancel.setOnClickListener(listenerBtnClickCancel);

    // Add dynamic content now based on our filter type.
    ModelFilter modelFilter = RuleBuilder.instance().getChosenModelFilter();
    DataType ruleFilterDataOld = RuleBuilder.instance().getChosenRuleFilterDataOld();
    FactoryDynamicUI.buildUIForFilter(this, modelFilter, ruleFilterDataOld);

    setTitle(modelFilter.getAttribute().getTypeName() + " " + modelFilter.getTypeName() + " Filter");
  }

  private View.OnClickListener listenerBtnClickOk = new View.OnClickListener() {
    public void onClick(View v) {
      // Have the listener try to construct a full ModelFilter for us now
      // based on our dynamic UI content.
      ModelRuleFilter filter;
      try {
        filter = (ModelRuleFilter) handlerInputDone.onInputDone(v.getContext());
      } catch (Exception ex) {
        // TODO: (markww) Make sure DataType classes are providing meaningful error output, then 
        // remove the static string below and only use the contents of the exception.
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

  /** Implements FactoryDynamicUI.DlgDynamicInput. */
  public Context getContext() {
    return this;
  }

  /** Implements FactoryDynamicUI.DlgDynamicInput. */
  public void addDynamicLayout(LinearLayout ll) {
    LinearLayout llContent = (LinearLayout) findViewById(
        R.id.activity_dlg_filter_input_llDynamicContent);
    llContent.addView(ll);
  }

  /** Implements FactoryDynamicUI.DlgDynamicInput. */
  public void setHandlerOnFilterInputDone(FactoryDynamicUI.InputDone handler) {
    handlerInputDone = handler;
  }

  public void setHandlerPreserveState(FactoryDynamicUI.DlgPreserveState handler) {
    handlerStatePreserver = handler;
  }

  public void showActivityDialog(int id) {
    showDialog(id);
  }
}