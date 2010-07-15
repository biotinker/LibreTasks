/*******************************************************************************
 * Copyright 2009 Omnidroid - http://code.google.com/p/omnidroid 
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
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelAttribute;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelEvent;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelRuleFilter;

/**
 * This dialog shows a list of attributes linked to the selected root event. After the user selects
 * an attribute to filter on, we move them to the {@link ActivityDlgFilters} dialog.
 */
public class ActivityDlgAttributes extends Activity {
  private static final String KEY_STATE = "StateDlgAttributes";
  private static final String KEY_PREF = "selectedAttribute";
  private ListView listView;
  private AdapterAttributes adapterAttributes;
  private SharedPreferences state;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Link up controls from the xml layout resource file.
    initializeUI();

    // Restore UI state.
    state = getSharedPreferences(ActivityDlgAttributes.KEY_STATE, Context.MODE_WORLD_READABLE
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
    // If the user constructed a valid filter, also kill ourselves.
    ModelRuleFilter filter = RuleBuilder.instance().getChosenRuleFilter();
    if (filter != null) {
      finish();
    }
  }

  private void initializeUI() {
    setContentView(R.layout.activity_dlg_attributes);

    ModelEvent event = RuleBuilder.instance().getChosenEvent();
    adapterAttributes = new AdapterAttributes(this, event);

    setTitle(event.getTypeName() + " Attributes");

    listView = (ListView) findViewById(R.id.activity_dlg_attributes_listview);
    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    listView.setAdapter(adapterAttributes);

    TextView mTextViewInfo = (TextView) findViewById(R.id.activity_dlg_attributes_tv_info1);
    mTextViewInfo.setText("Select an attribute of [" + event.getTypeName() + "] to filter on:");

    Button btnOk = (Button) findViewById(R.id.activity_dlg_attributes_btnOk);
    btnOk.setOnClickListener(listenerBtnClickOk);
    Button btnInfo = (Button) findViewById(R.id.activity_dlg_attributes_btnInfo);
    btnInfo.setOnClickListener(listenerBtnClickInfo);

    UtilUI.inflateDialog((LinearLayout) findViewById(R.id.activity_dlg_attributes_ll_main));
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
      showDlgFilters(listView.getCheckedItemPosition());
    }
  };

  private View.OnClickListener listenerBtnClickInfo = new View.OnClickListener() {
    public void onClick(View v) {
      // TODO: (markww) Add help info about attribute.
      UtilUI.showAlert(v.getContext(), getString(R.string.sorry), getString(R.string.coming_soon));
    }
  };

  /**
   * Start the filters activity, which will show all possible filters for the selected attribute.
   */
  private void showDlgFilters(int selectedItemPosition) {
    if (selectedItemPosition < 0) {
      UtilUI.showAlert(this, getString(R.string.sorry),
          getString(R.string.select_attribute_alert_inst));
      return;
    }

    // Store the selected attribute in the RuleBuilder so the next activity can pick it up.
    ModelAttribute attribute = (ModelAttribute) adapterAttributes.getItem(selectedItemPosition);
    RuleBuilder.instance().setChosenAttribute(attribute);

    ActivityDlgFilters.resetUI(this);

    Intent intent = new Intent();
    intent.setClass(getApplicationContext(), ActivityDlgFilters.class);
    startActivityForResult(intent, ActivityChooseFiltersAndActions.REQUEST_ADD_FILTER);
  }

  /**
   * Here we display attributes associated with our parent root event.
   */
  private class AdapterAttributes extends BaseAdapter {
    private Context context;
    private ArrayList<ModelAttribute> attributes;

    public AdapterAttributes(Context context, ModelEvent eventRoot) {
      this.context = context;

      // Fetch all available attributes for the root event from the
      // database.
      attributes = UIDbHelperStore.instance().db().getAttributesForEvent(eventRoot);
    }

    public int getCount() {
      return attributes.size();
    }

    public Object getItem(int position) {
      return attributes.get(position);
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
      iv.setImageResource(attributes.get(position).getIconResId());
      iv.setAdjustViewBounds(true);
      iv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT,
          LayoutParams.WRAP_CONTENT));
      if (listView.getCheckedItemPosition() == position) {
        iv.setBackgroundResource(R.drawable.icon_hilight);
      }

      TextView tv = new TextView(context);
      tv.setText(attributes.get(position).getDescriptionShort());
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