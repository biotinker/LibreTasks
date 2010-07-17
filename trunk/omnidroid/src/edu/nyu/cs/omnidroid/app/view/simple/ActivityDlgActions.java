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
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.controller.datatypes.DataType;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelAction;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelApplication;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelRuleAction;

/**
 * This dialog shows a list of all actions associated with the selected application. After the user
 * selects an action, we can move them to a final dialog where they input data about the selected
 * action for use with the rule.
 */
public class ActivityDlgActions extends Activity {
  // Options Menu IDs
  private static final int MENU_HELP = 0;

  // Context Menu Options
  private static final int MENU_INFO = 0;

  private ListView listView;
  private AdapterActions adapterActions;

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
    setContentView(R.layout.activity_dlg_list_selector);

    // This is the application the user wants to use.
    ModelApplication application = RuleBuilder.instance().getChosenApplication();

    setTitle(application.getTypeName() + " " + getString(R.string.actions));

    adapterActions = new AdapterActions(this, application);

    listView = (ListView) findViewById(R.id.activity_dlg_list_selector_listview);
    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    listView.setAdapter(adapterActions);

    TextView mTextViewInfo = (TextView) findViewById(R.id.activity_dlg_list_selector_tv_info1);
    // TODO(acase): use %s in the string resource and use the string formatter to input
    // application.getTypeName()
    mTextViewInfo.setText("Select an action of [" + application.getTypeName() + "] to use:");
    listView.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        // Store the selected action in the RuleBuilder so the next activity can pick it up.
        ModelAction action = adapterActions.getItem(position);
        RuleBuilder.instance().setChosenModelAction(action);

        if (!action.getParameters().isEmpty()) {
          Intent intent = new Intent();
          intent.setClass(getApplicationContext(), ActivityDlgActionInput.class);
          startActivityForResult(intent, ActivityChooseFiltersAndActions.REQUEST_ADD_ACTION);
        } else {
          /*
           * Build rule using a ModelRuleAction with empty list of DataType since there are no
           * action parameters. Done here because ActivityActionInput is being skipped.
           */
          RuleBuilder.instance().setChosenRuleAction(
              new ModelRuleAction(-1, action, new ArrayList<DataType>()));
          finish();
        }
      }
    });

    UtilUI.inflateDialog((LinearLayout) findViewById(R.id.activity_dlg_list_selector_ll_main));

    // Provide context menu functionality
    // TODO(acase): Action specific information
    // registerForContextMenu(listView);
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
    menu.setHeaderTitle(adapterActions.getItem(info.position).getDescription());
    menu.add(ContextMenu.NONE, MENU_INFO, ContextMenu.NONE, R.string.info);
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    switch (item.getItemId()) {
    case MENU_INFO:
      // TODO(acase): Action specific information
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
    help.setMessage(Html.fromHtml(getString(R.string.help_dlgactions)));
    help.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
      }
    });
    help.show();
  }

  /**
   * Here we display action associated with our parent application.
   */
  public class AdapterActions extends BaseAdapter {
    private Context context;
    private final List<ModelAction> actions;

    public AdapterActions(Context context, ModelApplication application) {
      this.context = context;

      // Fetch all available actions for this application, from the database.
      actions = UIDbHelperStore.instance().db().getActionsForApplication(application);
    }

    public int getCount() {
      return actions.size();
    }

    public ModelAction getItem(int position) {
      return actions.get(position);
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
      iv.setImageResource(actions.get(position).getIconResId());
      iv.setAdjustViewBounds(true);
      iv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT,
          LayoutParams.WRAP_CONTENT));
      if (listView.getCheckedItemPosition() == position) {
        iv.setBackgroundResource(R.drawable.icon_hilight);
      }

      TextView tv = new TextView(context);
      tv.setText(actions.get(position).getDescriptionShort());
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