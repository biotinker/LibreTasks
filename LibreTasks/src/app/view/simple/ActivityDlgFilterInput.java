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
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import libretasks.app.R;
import libretasks.app.controller.datatypes.DataType;
import libretasks.app.view.simple.factoryui.RuleFilterViewFactory;
import libretasks.app.view.simple.model.ModelFilter;
import libretasks.app.view.simple.model.ModelRuleFilter;
import libretasks.app.view.simple.viewitem.ViewItemGroup;

/**
 * This dialog is a shell to contain UI elements specific to different filters. Given a filter ID,
 * we can construct the inner UI elements using {@link RuleFilterViewFactory}.
 */
public class ActivityDlgFilterInput extends Activity {
  private static final String TAG = ActivityDlgFilterInput.class.getSimpleName();

  // Container for the dynamically created view
  private ViewItemGroup viewItems;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Link up controls from the xml layout resource file.
    initializeUI(savedInstanceState);
  }

  @Override
  protected void onSaveInstanceState(Bundle bundle) {
    viewItems.saveState(bundle);
  }

  private void initializeUI(Bundle savedInstanceState) {
    setContentView(R.layout.activity_dlg_filter_input);

    Button btnOk = (Button) findViewById(R.id.activity_dlg_filter_input_btnOk);
    btnOk.setOnClickListener(listenerBtnClickOk);

    Button btnHelp = (Button) findViewById(R.id.activity_dlg_filter_input_btnHelp);
    btnHelp.setOnClickListener(listenerBtnClickHelp);

    // Add dynamic content now based on our filter type.
    ModelFilter modelFilter = RuleBuilder.instance().getChosenModelFilter();
    DataType ruleFilterDataOld = RuleBuilder.instance().getChosenRuleFilterDataOld();

    viewItems = RuleFilterViewFactory.buildUIForFilter(modelFilter, ruleFilterDataOld, this);
    LinearLayout llContent = (LinearLayout) findViewById(R.id.activity_dlg_filter_input_llDynamicContent);
    llContent.addView(viewItems.getLayout());

    try {
      viewItems.loadState(savedInstanceState);
    } catch (Exception e) {
      Log.e(TAG, "Failed during loadState", e);
    }

    setTitle(modelFilter.getAttribute().getTypeName() + " " + modelFilter.getTypeName() + " Filter");
  }

  private View.OnClickListener listenerBtnClickOk = new View.OnClickListener() {
    public void onClick(View v) {
      ModelRuleFilter filter;
      try {
        filter = RuleFilterViewFactory.buildFilterFromUI(RuleBuilder.instance()
            .getChosenModelFilter(), viewItems);
      } catch (Exception ex) {
        // TODO: (markww) Make sure DataType classes are providing meaningful error output, then
        // remove the static string below and only use the contents of the exception.
        Resources resource = v.getContext().getResources();
        UtilUI.showAlert(v.getContext(), resource.getString(R.string.sorry), resource
            .getString(R.string.bad_data_format)
            + ex.getMessage());
        return;
      }

      // Set our constructed filter so the parent activity can pick it up.
      RuleBuilder.instance().setChosenRuleFilter(filter);

      setResult(RESULT_OK);
      finish();
    }
  };

  private View.OnClickListener listenerBtnClickHelp = new View.OnClickListener() {
    public void onClick(View v) {
      Builder help = new AlertDialog.Builder(v.getContext());
      help.setIcon(android.R.drawable.ic_menu_help);
      help.setTitle(R.string.help);
      help.setMessage(Html.fromHtml(getString(R.string.help_dlgfilterinput)));
      help.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
        }
      });
      help.show();
    }
  };
}
