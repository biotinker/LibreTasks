/*******************************************************************************
 * Copyright 2009, 2010 OmniDroid - http://code.google.com/p/omnidroid 
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

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelAction;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelApplication;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelRuleAction;

/**
 * This dialog shows a list of all applications available for use with actions. After the user
 * selects an application, we move them to the {@link ActivityDlgActions} dialog which shows all
 * actions available for that application.
 */
public class ActivityDlgApplications extends Activity {
  // State storage
  private static final String KEY_STATE = "StateDlgApplications";
  private static final String KEY_PREF = "selectedApplication";

  private ListView listView;
  private AdapterApplications adapterApplications;
  private SharedPreferences state;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Link up controls from the xml layout resource file.
    initializeUI();

    // Restore UI state.
    state = getSharedPreferences(ActivityDlgApplications.KEY_STATE, Context.MODE_WORLD_READABLE
        | Context.MODE_WORLD_WRITEABLE);
    listView.setItemChecked(state.getInt(KEY_PREF, -1), true);
  }

  @Override
  protected void onPause() {
    super.onPause();

    // Save UI state.
    SharedPreferences.Editor prefsEditor = state.edit();
    prefsEditor.putInt(KEY_PREF, listView.getCheckedItemPosition());
    prefsEditor.commit();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // If the user constructed a valid action, also kill ourselves.
    ModelRuleAction action = RuleBuilder.instance().getChosenRuleAction();
    if (action != null) {
      finish();
    }
  }

  private void initializeUI() {
    setContentView(R.layout.activity_dlg_applications);
    setTitle(getString(R.string.applications_title));

    adapterApplications = new AdapterApplications(this);

    listView = (ListView) findViewById(R.id.activity_dlg_applications_listview);
    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    listView.setAdapter(adapterApplications);

    TextView mTextViewInfo = (TextView) findViewById(R.id.activity_dlg_applications_tv_info1);
    mTextViewInfo.setText(getString(R.string.select_application_inst));

    // TODO(acase): Use item select instead of OK button
    Button btnOk = (Button) findViewById(R.id.activity_dlg_applications_btnOk);
    btnOk.setOnClickListener(listenerBtnClickOk);

    // TODO(acase): Move Info to context selection menu
    Button btnInfo = (Button) findViewById(R.id.activity_dlg_applications_btnInfo);
    btnInfo.setOnClickListener(listenerBtnClickInfo);

    UtilUI.inflateDialog((LinearLayout) findViewById(R.id.activity_dlg_applications_ll_main));
  }

  /**
   * Wipes any UI state saves in {@link:state}. Activities which create this activity should call
   * this before launching so we appear as a brand new instance.
   * 
   * @param context
   *          Context of caller.
   */
  public static void resetUI(Context context) {
    UtilUI.resetSharedPreferences(context, KEY_STATE);
  }

  private View.OnClickListener listenerBtnClickOk = new View.OnClickListener() {
    public void onClick(View v) {
      // The user has chosen an attribute, now get a list of filters associated
      // with that attribute, from the database.
      showDlgActions(listView.getCheckedItemPosition());
    }
  };

  private View.OnClickListener listenerBtnClickInfo = new View.OnClickListener() {
    public void onClick(View v) {
      // TODO: (markww) Add help info about the application.
      UtilUI.showAlert(v.getContext(), getString(R.string.sorry), getString(R.string.coming_soon));
    }
  };

  /**
   * Start the filters activity, which will show all possible filters for the selected attribute.
   */
  private void showDlgActions(int selectedItemPosition) {
    if (selectedItemPosition < 0) {
      UtilUI.showAlert(this, getString(R.string.sorry),
          getString(R.string.select_application_alert_inst));
      return;
    }

    // Store the selected attribute in the RuleBuilder so the next activity can pick it up.
    ModelApplication application = (ModelApplication) adapterApplications
        .getItem(selectedItemPosition);
    RuleBuilder.instance().setChosenApplication(application);

    Intent intent = new Intent();
    intent.setClass(getApplicationContext(), ActivityDlgActions.class);
    startActivityForResult(intent, ActivityChooseFiltersAndActions.ACTIVITY_RESULT_ADD_ACTION);
  }

  /**
   * Here we display all applications offered by the system.
   */
  private class AdapterApplications extends BaseAdapter {
    private Context context;
    private ArrayList<ModelApplication> applications;

    public AdapterApplications(Context context) {
      this.context = context;

      /*
       * TODO (dvo203): We shouldn't have to filter the applications here, it should be done in DB
       * layer. UIDbHelper in particular.
       */

      // Fetch all available applications.
      ArrayList<ModelApplication> allApplications = UIDbHelperStore.instance().db()
          .getAllApplications();

      applications = new ArrayList<ModelApplication>();

      // Look for applications that have actions and put them on the
      for (ModelApplication application : allApplications) {
        ArrayList<ModelAction> actions = UIDbHelperStore.instance().db().getActionsForApplication(
            application);
        if (!actions.isEmpty()) {
          applications.add(application);
        }
      }
    }

    public int getCount() {
      return applications.size();
    }

    public Object getItem(int position) {
      return applications.get(position);
    }

    public long getItemId(int position) {
      return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

      LinearLayout ll = new LinearLayout(context);
      ll.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
          LayoutParams.FILL_PARENT));
      ll.setMinimumHeight(50);
      ll.setOrientation(LinearLayout.HORIZONTAL);
      ll.setGravity(Gravity.CENTER_VERTICAL);

      ImageView iv = new ImageView(context);
      iv.setImageResource(applications.get(position).getIconResId());
      iv.setAdjustViewBounds(true);
      iv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT,
          LayoutParams.WRAP_CONTENT));
      if (listView.getCheckedItemPosition() == position) {
        iv.setBackgroundResource(R.drawable.icon_hilight);
      }

      TextView tv = new TextView(context);
      tv.setText(applications.get(position).getDescriptionShort());
      tv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
          LayoutParams.FILL_PARENT));
      tv.setGravity(Gravity.CENTER_VERTICAL);
      tv.setPadding(10, 0, 0, 0);
      tv.setTextSize(14.0f);
      tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
      tv.setTextColor(context.getResources().getColor(R.color.list_element_text));
      tv.setMinHeight(46);

      ll.addView(iv);
      ll.addView(tv);

      return ll;
    }
  }
}