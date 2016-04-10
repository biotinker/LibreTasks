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
import android.widget.LinearLayout;
import libretasks.app.R;
import libretasks.app.view.simple.factoryui.ActionParameterViewFactory;
import libretasks.app.view.simple.model.ModelApplication;
import libretasks.app.view.simple.viewitem.ViewItemGroup;

/**
 * This dialog is a shell to contain UI elements specific to creating a login UI. Given an
 * application, we can construct the inner UI elements.
 */
public class ActivityDlgApplicationLoginInput extends Activity {

  /** Layout dynamically generated on our action type by FactoryActions. */
  private LinearLayout llContent;

  /** Main layout to which we append the dynamically generated layout. */
  private ViewItemGroup viewItems;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initializeUI();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  private void initializeUI() {
    setContentView(R.layout.activity_dlg_action_login_input);

    Button btnOk = (Button) findViewById(R.id.activity_dlg_action_login_input_btnOk);
    btnOk.setOnClickListener(listenerBtnClickOk);

    Button btnHelp = (Button) findViewById(R.id.activity_dlg_action_login_input_btnHelp);
    btnHelp.setOnClickListener(listenerBtnClickHelp);

    llContent = (LinearLayout) findViewById(R.id.activity_dlg_action_input_llDynamicContent);

    // Add dynamic content from the application
    ModelApplication modelApp = UIDbHelperStore.instance().db().getApplication(
        RuleBuilder.instance().getChosenApplication().getDatabaseId());

    viewItems = ActionParameterViewFactory.buildLoginUI(modelApp, this);

    llContent.addView(viewItems.getLayout());
  }

  private View.OnClickListener listenerBtnClickOk = new View.OnClickListener() {
    public void onClick(View v) {
      ModelApplication application = RuleBuilder.instance().getChosenApplication();
      try {
        ActionParameterViewFactory.buildApplicationFromLoginUI(application, viewItems);
        UIDbHelperStore.instance().db().updateApplicationLoginInfo(application);
      } catch (Exception e) {
        UtilUI.showAlert(v.getContext(), "", e.toString());
        return;
      }

      setResult(RESULT_OK, getIntent());
      finish();
    }
  };

  private View.OnClickListener listenerBtnClickHelp = new View.OnClickListener() {
    public void onClick(View v) {
      UtilUI.showAlert(v.getContext(), getString(R.string.login_info_title),
          getString(R.string.login_info_details));
    }
  };
}
