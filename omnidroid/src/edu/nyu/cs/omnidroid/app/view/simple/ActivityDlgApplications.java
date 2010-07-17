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
package edu.nyu.cs.omnidroid.app.view.simple;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
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
  private ListView listView;
  private AdapterApplications adapterApplications;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Link up controls from the xml layout resource file.
    initializeUI();
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

    listView.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        // Store the selected attribute in the RuleBuilder so the next activity can pick it up.
        RuleBuilder.instance().setChosenApplication(adapterApplications.getItem(position));

        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), ActivityDlgActions.class);
        startActivityForResult(intent, ActivityChooseFiltersAndActions.REQUEST_ADD_ACTION);
      }
    });

    UtilUI.inflateDialog((LinearLayout) findViewById(R.id.activity_dlg_applications_ll_main));
  }

  /**
   * Here we display all applications offered by the system.
   */
  private class AdapterApplications extends BaseAdapter {
    private Context context;
    private final List<ModelApplication> applications;

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

    public ModelApplication getItem(int position) {
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