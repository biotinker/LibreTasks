/*  
 * Copyright (c) 2016  LibreTasks - https://github.com/biotinker/LibreTasks  
 *  
 *  This file is free software: you may copy, redistribute and/or modify it  
 *  under the terms of the GNU General Public License as published by the  
 *  Free Software Foundation, either version 3 of the License, or (at your  
 *  option) any later version.  
 *  
 *  This file is distributed in the hope that it will be useful, but  
 *  WITHOUT ANY WARRANTY; without even the implied warranty of  
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  
 *  General Public License for more details.  
 *  
 *  You should have received a copy of the GNU General Public License  
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.  
 *  
 * This file incorporates work covered by the following copyright and  
 * permission notice:  
 /*******************************************************************************
 * Copyright 2009, 2010 Omnidroid - http://code.google.com/p/omnidroid 
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
package libretasks.app.view.simple;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import libretasks.app.R;
import libretasks.app.view.simple.model.Rule;

/**
 * This activity gives the user a chance to supply a rule name and description.
 */
public class ActivityDlgRuleName extends Activity {
  private static final String KEY_STATE_NAME = "enteredRuleName";
  private static final String KEY_STATE_DESC = "enteredRuleDescription";

  private EditText editTextRuleName;
  private EditText editTextRuleDescription;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Link up controls from the xml layout resource file.
    initializeUI(savedInstanceState);
  }

  @Override
  protected void onSaveInstanceState(Bundle bundle) {
    bundle.putString(KEY_STATE_NAME, editTextRuleName.getText().toString());
    bundle.putString(KEY_STATE_DESC, editTextRuleDescription.getText().toString());
  }

  private void initializeUI(Bundle bundle) {
    setContentView(R.layout.activity_dlg_rule_name);

    editTextRuleName = (EditText) findViewById(R.id.activity_dlg_rule_name_et_rule_name);
    editTextRuleDescription = (EditText) findViewById(R.id.activity_dlg_rule_name_et_rule_description);
    
    Button btnOk = (Button) findViewById(R.id.activity_dlg_rule_name_btnOk);
    btnOk.setOnClickListener(listenerBtnClickOk);
    
    if (bundle == null) {
      // Get the current rule name, if any.
      String currentName = RuleBuilder.instance().getRule().getName();
      if (currentName.equals("")) {
        // Default to the event name as a name for this rule until the user sets it.
        currentName = RuleBuilder.instance().getChosenEvent().getDescriptionShort();
      }
      // Get the current rule description, if any.
      String currentDescription = RuleBuilder.instance().getRule().getDescription();
      long ruleId = RuleBuilder.instance().getRule().getDatabaseId();
      if ((currentDescription.equals("")) && (ruleId == Rule.NEW_RULE_ID)) {
        // Default to the our pre-built description
        currentDescription = RuleBuilder.instance().getRule().getNaturalLanguageString();
      }
      editTextRuleName.setText(currentName);
      editTextRuleDescription.setText(currentDescription);
    }
    else {
      editTextRuleName.setText(bundle.getString(KEY_STATE_NAME));
      editTextRuleDescription.setText(bundle.getString(KEY_STATE_DESC));      
    }
  }

  private View.OnClickListener listenerBtnClickOk = new View.OnClickListener() {
    public void onClick(View v) {
      setRuleNameAndDescription(editTextRuleName.getText().toString(), editTextRuleDescription
          .getText().toString());
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
}
