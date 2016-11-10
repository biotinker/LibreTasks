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

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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
import libretasks.app.R;
import libretasks.app.view.simple.model.ModelAttribute;
import libretasks.app.view.simple.model.ModelFilter;
import libretasks.app.view.simple.model.ModelRuleFilter;

/**
 * This activity shows a list of all filters associated with the selected attribute. After the user
 * selects a filter, we can move them to a final dialog where they input data about the selected
 * filter for use with the rule.
 */
public class ActivityDlgFilters extends Activity {
  // Options Menu IDs
  private static final int MENU_HELP = 0;

  // Context Menu Options
  private static final int MENU_INFO = 0;

  private ListView listView;
  private AdapterFilters adapterFilters;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Link up controls from the xml layout resource file.
    initializeUI();
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
    setContentView(R.layout.activity_dlg_list_selector);

    // This is the attribute the user chose to filter on.
    ModelAttribute attribute = RuleBuilder.instance().getChosenAttribute();

    setTitle(attribute.getTypeName() + " Filters");

    adapterFilters = new AdapterFilters(this, attribute);

    listView = (ListView) findViewById(R.id.activity_dlg_list_selector_listview);
    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    listView.setAdapter(adapterFilters);

    TextView mTextViewInfo = (TextView) findViewById(R.id.activity_dlg_list_selector_tv_info1);
    mTextViewInfo.setText(getString(R.string.select_filter_title, attribute.getTypeName()));

    listView.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        // Store the selected filter in the RuleBuilder so the next activity can pick it up.
        RuleBuilder.instance().setChosenModelFilter(adapterFilters.getItem(position));

        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), ActivityDlgFilterInput.class);
        startActivityForResult(intent, ActivityChooseFiltersAndActions.REQUEST_ADD_FILTER);
      }
    });

    UtilUI.inflateDialog((LinearLayout) findViewById(R.id.activity_dlg_list_selector_ll_main));

    // Provide context menu functionality
    // TODO(acase): Filter specific information
    // registerForContextMenu(listView);
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
    menu.setHeaderTitle(adapterFilters.getItem(info.position).getDescription());
    menu.add(ContextMenu.NONE, MENU_INFO, ContextMenu.NONE, R.string.info);
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case MENU_INFO:
      // TODO(acase): Filter specific information
      return true;
    default:
      return super.onContextItemSelected(item);
    }
  }

  /** Create an options menu */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(Menu.NONE, MENU_HELP, Menu.NONE, getString(R.string.help)).setIcon(
        android.R.drawable.ic_menu_help).setAlphabeticShortcut('h');
    return super.onCreateOptionsMenu(menu);
  }

  /** Called when an item of options menu is clicked */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case MENU_HELP:
      help();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void help() {
    Builder help = new AlertDialog.Builder(this);
    help.setIcon(android.R.drawable.ic_menu_help);
    help.setTitle(R.string.help);
    help.setMessage(Html.fromHtml(getString(R.string.help_dlgfilters)));
    help.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
      }
    });
    help.show();
  }

  /**
   *  Here we display filters associated with our parent attribute.
   */
  public class AdapterFilters extends BaseAdapter {
    private Context context;
    private final List<ModelFilter> filters;

    public AdapterFilters(Context context, ModelAttribute attribute) {
      this.context = context;

      // Fetch all available filters for this attribute, from the database.
      filters = UIDbHelperStore.instance().db().getFiltersForAttribute(attribute);
    }

    public int getCount() {
      return filters.size();
    }

    public ModelFilter getItem(int position) {
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

      // the icon of the filter
      ImageView iv = new ImageView(context);
      iv.setImageResource(filters.get(position).getIconResId());
      iv.setAdjustViewBounds(true);
      iv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT,
          LayoutParams.WRAP_CONTENT));
      if (listView.getCheckedItemPosition() == position) {
        iv.setBackgroundResource(R.drawable.icon_hilight);
      }

      // the description of the filter
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
