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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import edu.nyu.cs.omnidroid.app.R;

/**
 * This activity gives the user a chance to supply a rule name and description.
 */
public class ActivityDlgRuleName extends Activity {

  private static final String KEY_STATE = "StateActivityDlgRuleName";
  private static final String KEY_STATE_NAME = "enteredRuleName";
  private static final String KEY_STATE_DESC = "enteredRuleDescription";
  
  private EditText editTextRuleName;

  private EditText editTextRuleDescription;
  
  private SharedPreferences state;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Link up controls from the xml layout resource file.
    initializeUI();
    
    // The current rule name, if any.
    String currentName = RuleBuilder.instance().getRule().getName();
    if (currentName == null) {
      currentName = "";
    }
    
    // The current rule description, if any.
    String currentDescription = RuleBuilder.instance().getRule().getDescription();
    if (currentDescription == null) {
      currentDescription = "";
    }
    
    // Restore UI state if possible. We try using whatever UI state was saved for us and if
    // none is found, we populate with the values of the current rule inside RuleBuilder.
    state = getSharedPreferences(ActivityDlgRuleName.KEY_STATE, Context.MODE_WORLD_READABLE
        | Context.MODE_WORLD_WRITEABLE);
    editTextRuleName.setText(state.getString(KEY_STATE_NAME, currentName));
    editTextRuleDescription.setText(state.getString(KEY_STATE_DESC, currentDescription));
  }

  @Override
  protected void onPause() {
    super.onPause();

    // Save UI state.
    SharedPreferences.Editor prefsEditor = state.edit();
    prefsEditor.putString(KEY_STATE_NAME, editTextRuleName.getText().toString());
    prefsEditor.putString(KEY_STATE_DESC, editTextRuleDescription.getText().toString());
    prefsEditor.commit();
  }
  
  private void initializeUI() {
    setContentView(R.layout.activity_dlg_rule_name);

    editTextRuleName = (EditText) findViewById(
      R.id.activity_dlg_rule_name_et_rule_name);
    
    editTextRuleDescription = (EditText) findViewById(
      R.id.activity_dlg_rule_name_et_rule_description);
    
    Button btnOk = (Button) findViewById(R.id.activity_dlg_rule_name_btnOk);
    btnOk.setOnClickListener(listenerBtnClickOk);
  }

  private View.OnClickListener listenerBtnClickOk = new View.OnClickListener() {
    public void onClick(View v) {
      setRuleNameAndDescription(editTextRuleName.getText().toString(), 
        editTextRuleDescription.getText().toString());
    }
  };

  private void setRuleNameAndDescription(String name, String description) {
    if (name == null || name.length() == 0) {
      UtilUI.showAlert(this, "Rule Name", "Please enter a rule name before saving.");
      return;
    }
    
    RuleBuilder.instance().getRule().setName(name);
    RuleBuilder.instance().getRule().setDescription(description);
    setResult(RESULT_OK);
    finish();
  }
  
  /**
   * Wipes any UI state saves in {@link:state}. Activities which create this activity should
   * call this before launching so we appear as a brand new instance.
   * @param context  Context of caller.
   */
  public static void resetUI(Context context) {
    UtilUI.resetSharedPreferences(context, KEY_STATE);
  }
}