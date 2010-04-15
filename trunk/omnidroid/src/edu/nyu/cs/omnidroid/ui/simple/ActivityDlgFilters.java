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
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.ui.Constants;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelAttribute;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelFilter;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelRuleFilter;

/**
 * This activity shows a list of all filters associated with the selected attribute. After the 
 * user selects a filter, we can move them to a final dialog where they input data about the
 * selected filter for use with the rule.
 */
public class ActivityDlgFilters extends Activity {

  private static final String KEY_STATE = "StateDlgFilters";
  
  private ListView listView;
  private AdapterFilters adapterFilters;
  private SharedPreferences state;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Link up controls from the xml layout resource file.
    initializeUI();
    
    // Restore UI state if possible.
    state = getSharedPreferences(ActivityDlgFilters.KEY_STATE, Context.MODE_WORLD_READABLE
        | Context.MODE_WORLD_WRITEABLE);
    listView.setItemChecked(state.getInt("selectedFilter", -1), true);
  }

  @Override
  protected void onPause() {
    super.onPause();

    // Save UI state.
    SharedPreferences.Editor prefsEditor = state.edit();
    prefsEditor.putInt("selectedFilter", listView.getCheckedItemPosition());
    prefsEditor.commit();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // If the user constructed a valid filter, also kill ourselves.
    ModelRuleFilter filter = RuleBuilder.instance().getChosenRuleFilter();
    if (filter != null) {
      finish();
    }
  }

  private void initializeUI() {
    setContentView(R.layout.activity_dlg_filters);

    // This is the attribute the user chose to filter on.
    ModelAttribute attribute = RuleBuilder.instance().getChosenAttribute();

    setTitle(attribute.getTypeName() + " Filters");

    adapterFilters = new AdapterFilters(this, attribute);

    listView = (ListView) findViewById(R.id.activity_dlg_filters_listview);
    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    listView.setAdapter(adapterFilters);

    TextView mTextViewInfo = (TextView) findViewById(R.id.activity_dlg_filters_tv_info1);
    mTextViewInfo.setText("Select a filter to apply to [ " + attribute.getTypeName() + " ]:");

    Button btnOk = (Button) findViewById(R.id.activity_dlg_filters_btnOk);
    btnOk.setOnClickListener(listenerBtnClickOk);
    Button btnInfo = (Button) findViewById(R.id.activity_dlg_filters_btnInfo);
    btnInfo.setOnClickListener(listenerBtnClickInfo);
    Button btnCancel = (Button) findViewById(R.id.activity_dlg_filters_btnCancel);
    btnCancel.setOnClickListener(listenerBtnClickCancel);

    UtilUI.inflateDialog((LinearLayout) findViewById(R.id.activity_dlg_filters_ll_main));
  }
  
  /**
   * Wipes any UI state saves in {@link:state}. Activities which create this activity should
   * call this before launching so we appear as a brand new instance.
   * @param context  Context of caller.
   */
  public static void resetUI(Context context) {
    UtilUI.resetSharedPreferences(context, KEY_STATE);
  }

  private View.OnClickListener listenerBtnClickOk = new View.OnClickListener() {
    public void onClick(View v) {
      // The user has chosen an attribute, now get a list of filters associated
      // with that attribute data type.
      showDlgFilterInput(listView.getCheckedItemPosition());
    }
  };

  private View.OnClickListener listenerBtnClickInfo = new View.OnClickListener() {
    public void onClick(View v) {
      // TODO: (markww) add support for help info on filter.
      UtilUI.showAlert(v.getContext(), "Sorry!",
        "We'll implement an info dialog about the selected filter soon!");
    }
  };

  private View.OnClickListener listenerBtnClickCancel = new View.OnClickListener() {
    public void onClick(View v) {
      finish();
    }
  };

  /**
   * Start the filter input activity, which will let the user input data for the selected filter.
   */
  private void showDlgFilterInput(int selectedItemPosition) {
    if (selectedItemPosition < 0) {
      UtilUI.showAlert(this, "Sorry!", "Please select a filter from the list, then hit OK!");
      return;
    }

    // Store the selected filter in the RuleBuilder so the next activity can pick it up.
    ModelFilter filter = (ModelFilter) adapterFilters.getItem(selectedItemPosition);
    RuleBuilder.instance().setChosenModelFilter(filter);

    Intent intent = new Intent();
    intent.setClass(getApplicationContext(), ActivityDlgFilterInput.class);
    startActivityForResult(intent, Constants.ACTIVITY_RESULT_ADD_FILTER);
  }

  /**
   * Here we display filters associated with our parent attribute.
   */
  public class AdapterFilters extends BaseAdapter {

    private Context context;
    private ArrayList<ModelFilter> filters;

    public AdapterFilters(Context context, ModelAttribute attribute) {
      this.context = context;

      // Fetch all available filters for this attribute, from the database.
      filters = UIDbHelperStore.instance().db().getFiltersForAttribute(attribute);
    }

    public int getCount() {
      return filters.size();
    }

    public Object getItem(int position) {
      return filters.get(position);
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

      //the icon of the filter
      ImageView iv = new ImageView(context);
      iv.setImageResource(filters.get(position).getIconResId());
      iv.setAdjustViewBounds(true);
      iv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT,
          LayoutParams.WRAP_CONTENT));
      if (listView.getCheckedItemPosition() == position) {
        iv.setBackgroundResource(R.drawable.icon_hilight);
      }

      //the description of the filter
      TextView tv = new TextView(context);
      tv.setText(filters.get(position).getDescriptionShort());
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