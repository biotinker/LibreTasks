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

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import edu.nyu.cs.omnidroid.ui.simple.model.ModelAction;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelApplication;

/**
 * This dialog displays a list of filters associated with a parent <code>ModelAttribute</code>. If
 * the user selects a filter from our list, we then construct a <code>DlgFilterInput</code> instance
 * so they can enter filter parameters for it.
 */
public class DlgActions extends Dialog {

  private TextView mTextViewInfo;
  private ListView mListView;
  private AdapterActions mAdapterActions;
  private boolean mDlgActionInputIsOpen;
  private SharedPreferences mState;


  public DlgActions(Context context, ModelApplication application) {
    super(context);
    setContentView(R.layout.dlg_actions);
    setTitle("Applications");

    mAdapterActions = new AdapterActions(getContext(), application);

    mListView = (ListView) findViewById(R.id.dlg_actions_listview);
    mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    mListView.setAdapter(mAdapterActions);

    mTextViewInfo = (TextView) findViewById(R.id.dlg_actions_tv_info1);
    mTextViewInfo.setText("Select an application from the list below:");

    Button btnOk = (Button) findViewById(R.id.dlg_actions_btnOk);
    btnOk.setOnClickListener(listenerBtnClickOk);
    Button btnInfo = (Button) findViewById(R.id.dlg_actions_btnInfo);
    btnInfo.setOnClickListener(listenerBtnClickInfo);
    Button btnCancel = (Button) findViewById(R.id.dlg_actions_btnCancel);
    btnCancel.setOnClickListener(listenerBtnClickCancel);

    UtilUI.inflateDialog((LinearLayout) findViewById(R.id.dlg_actions_ll_main));

    // Restore UI state if possible.
    mState = context.getSharedPreferences("StateDlgActions", Context.MODE_PRIVATE);
    mListView.setItemChecked(mState.getInt("selectedAction", -1), true);
    if (mState.getBoolean("mDlgActionInputIsOpen", false)) {
    	showDlgActionInput();
    }
  }

  @Override
  protected void onStart() {
  }

  @Override
  protected void onStop() {

    super.onStop();

    // Save UI state.
    SharedPreferences.Editor prefsEditor = mState.edit();
    prefsEditor.putInt("selectedAction", mListView.getCheckedItemPosition());
    prefsEditor.putBoolean("mDlgActionInputIsOpen", mDlgActionInputIsOpen);
    prefsEditor.commit();
  }

  private android.view.View.OnClickListener listenerBtnClickOk = new android.view.View.OnClickListener() {
    public void onClick(View v) {
      // The user has chosen an action, now we need to capture their
      // input info for it.
      showDlgActionInput();
    }
  };

  private android.view.View.OnClickListener listenerBtnClickInfo = new android.view.View.OnClickListener() {
    public void onClick(View v) {
      UtilUI.showAlert(v.getContext(), "Sorry!",
          "We'll implement an info dialog about the selected action soon!");
    }
  };

  private android.view.View.OnClickListener listenerBtnClickCancel = new android.view.View.OnClickListener() {
    public void onClick(View v) {
      resetUI();
      dismiss();
    }
  };

  private void showDlgActionInput() {
    int position = mListView.getCheckedItemPosition();
    if (position < 0) {
      UtilUI.showAlert(getContext(), "Sorry!",
          "Please select an action from the list, then hit OK!");
      return;
    }

    // This is the action they want to use.
    ModelAction actionChosen = (ModelAction)mAdapterActions.getItem(position);

    // Now we construct a blank DlgFilterInput dialog, and let the UI
    // factory class generate its content to capture the info needed for
    // this particular filter.
    DlgActionInput dlg = new DlgActionInput(getContext(), actionChosen, null);
    dlg.setOnDismissListener(new OnDismissListener() {
      public void onDismiss(DialogInterface dialog) {
        // If the user constructed a valid filter, also kill ourselves.
        ModelAction action = (ModelAction)DlgItemBuilderStore.instance().getBuiltItem();
        if (action != null) {
          resetUI();
          dismiss();
        }
      }
    });
    dlg.show();
  }

  private void resetUI() {
    if (mListView.getCheckedItemPosition() > -1) {
      mListView.setItemChecked(mListView.getCheckedItemPosition(), false);
    }
    mDlgActionInputIsOpen = false;
  }

  
  /**
   * Here we display applications.
   */
  public class AdapterActions extends BaseAdapter {

    private Context mContext;
    private ArrayList<ModelAction> mActions;

    public AdapterActions(Context c, ModelApplication application) {
      mContext = c;

      // Fetch all available applications from the database.
      mActions = DbInterfaceUI.instance().db().getActionsForApplication(application);
    }

    public int getCount() {
      return mActions.size();
    }

    public Object getItem(int position) {
      return mActions.get(position);
    }

    public long getItemId(int position) {
      return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

      LinearLayout ll = new LinearLayout(mContext);
      ll.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
          LayoutParams.FILL_PARENT));
      ll.setMinimumHeight(50);
      ll.setOrientation(LinearLayout.HORIZONTAL);
      ll.setGravity(Gravity.CENTER_VERTICAL);

      ImageView iv = new ImageView(mContext);
      iv.setImageResource(mActions.get(position).getIconResId());
      iv.setAdjustViewBounds(true);
      iv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT,
          LayoutParams.WRAP_CONTENT));
      if (mListView.getCheckedItemPosition() == position) {
        iv.setBackgroundResource(R.drawable.icon_hilight);
      }

      TextView tv = new TextView(mContext);
      tv.setText(mActions.get(position).getDescriptionShort());
      tv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
          LayoutParams.FILL_PARENT));
      tv.setGravity(Gravity.CENTER_VERTICAL);
      tv.setPadding(10, 0, 0, 0);
      tv.setTextSize(14.0f);
      tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
      tv.setTextColor(0xFFFFFFFF);
      tv.setMinHeight(46);

      ll.addView(iv);
      ll.addView(tv);

      return ll;
    }
  }
}