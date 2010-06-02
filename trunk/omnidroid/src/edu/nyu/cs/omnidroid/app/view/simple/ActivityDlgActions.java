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
import edu.nyu.cs.omnidroid.app.view.Constants;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelAction;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelApplication;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelRuleAction;

/**
 * This dialog shows a list of all actions associated with the selected application. After the user
 * selects an action, we can move them to a final dialog where they input data about the selected
 * action for use with the rule.
 */
public class ActivityDlgActions extends Activity {

  private static final String KEY_STATE = "StateDlgActions";
  
  private ListView listView;
  private AdapterActions adapterActions;
  private SharedPreferences state;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
 
    // Link up controls from the xml layout resource file.
    initializeUI();
    
    // Restore UI state if possible.
    state = getSharedPreferences(ActivityDlgActions.KEY_STATE, Context.MODE_WORLD_READABLE
        | Context.MODE_WORLD_WRITEABLE);
    listView.setItemChecked(state.getInt("selectedAction", -1), true);
  }

  @Override
  protected void onPause() {
    super.onPause();

    // Save UI state.
    SharedPreferences.Editor prefsEditor = state.edit();
    prefsEditor.putInt("selectedAction", listView.getCheckedItemPosition());
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
    setContentView(R.layout.activity_dlg_actions);

    // This is the application the user wants to use.
    ModelApplication application = RuleBuilder.instance().getChosenApplication();

    setTitle(application.getTypeName() + " Actions");

    adapterActions = new AdapterActions(this, application);

    listView = (ListView) findViewById(R.id.activity_dlg_actions_listview);
    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    listView.setAdapter(adapterActions);

    TextView mTextViewInfo = (TextView) findViewById(R.id.activity_dlg_actions_tv_info1);
    mTextViewInfo.setText("Select an action of [" + application.getTypeName() + "] to use:");

    Button btnOk = (Button) findViewById(R.id.activity_dlg_actions_btnOk);
    btnOk.setOnClickListener(listenerBtnClickOk);
    Button btnInfo = (Button) findViewById(R.id.activity_dlg_actions_btnInfo);
    btnInfo.setOnClickListener(listenerBtnClickInfo);
    Button btnCancel = (Button) findViewById(R.id.activity_dlg_actions_btnCancel);
    btnCancel.setOnClickListener(listenerBtnClickCancel);

    UtilUI.inflateDialog((LinearLayout) findViewById(R.id.activity_dlg_actions_ll_main));
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
      // The user has chosen an application, now get a list of actions associated
      // with that application type.
      showDlgActionInput(listView.getCheckedItemPosition());
    }
  };

  private View.OnClickListener listenerBtnClickInfo = new View.OnClickListener() {
    public void onClick(View v) {
      // TODO: (markww) add support for help info on action.
      UtilUI.showAlert(v.getContext(), "Sorry!",
          "We'll implement an info dialog about the selected action soon!");
    }
  };

  private View.OnClickListener listenerBtnClickCancel = new View.OnClickListener() {
    public void onClick(View v) {
      finish();
    }
  };

  /**
   * Start the action input activity, which will let the user input data for the selected action.
   */
  private void showDlgActionInput(int selectedItemPosition) {
    if (selectedItemPosition < 0) {
      UtilUI.showAlert(this, "Sorry!", "Please select an action from the list above, then hit OK!");
      return;
    }

    // Store the selected action in the RuleBuilder so the next activity can pick it up.
    ModelAction action = (ModelAction) adapterActions.getItem(selectedItemPosition);
    RuleBuilder.instance().setChosenModelAction(action);

    Intent intent = new Intent();
    intent.setClass(getApplicationContext(), ActivityDlgActionInput.class);
    startActivityForResult(intent, Constants.ACTIVITY_RESULT_ADD_ACTION);
  }

  /**
   * Here we display action associated with our parent application.
   */
  public class AdapterActions extends BaseAdapter {

    private Context context;
    private ArrayList<ModelAction> actions;

    public AdapterActions(Context context, ModelApplication application) {
      this.context = context;

      // Fetch all available actions for this application, from the database.
      actions = UIDbHelperStore.instance().db().getActionsForApplication(application);
    }

    public int getCount() {
      return actions.size();
    }

    public Object getItem(int position) {
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