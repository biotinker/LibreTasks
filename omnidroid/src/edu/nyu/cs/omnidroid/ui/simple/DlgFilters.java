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
import edu.nyu.cs.omnidroid.ui.simple.model.ModelAttribute;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelFilter;

/**
 * This dialog displays a list of filters associated with a parent <code>ModelAttribute</code>. If
 * the user selects a filter from our list, we then construct a <code>DlgFilterInput</code> instance
 * so they can enter filter parameters for it.
 */
public class DlgFilters extends Dialog {

  private TextView mTextViewInfo;
  private ListView mListView;
  private AdapterFilters mAdapterFilters;
  private boolean mDlgFilterInputIsOpen;
  private SharedPreferences mState;
  private ModelAttribute mAttribute;

  public DlgFilters(Context context, ModelAttribute attribute) {
    super(context);
    setContentView(R.layout.dlg_filters);
    setTitle("Filters");
    mAttribute = attribute;

    mAdapterFilters = new AdapterFilters(getContext(), attribute);

    mListView = (ListView) findViewById(R.id.dlg_filters_listview);
    mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    mListView.setAdapter(mAdapterFilters);

    mTextViewInfo = (TextView) findViewById(R.id.dlg_filters_tv_info1);
    mTextViewInfo.setText("Select a filter to apply to [" + attribute.getTypeName() + "]:");

    Button btnOk = (Button) findViewById(R.id.dlg_filters_btnOk);
    btnOk.setOnClickListener(listenerBtnClickOk);
    Button btnInfo = (Button) findViewById(R.id.dlg_filters_btnInfo);
    btnInfo.setOnClickListener(listenerBtnClickInfo);
    Button btnCancel = (Button) findViewById(R.id.dlg_filters_btnCancel);
    btnCancel.setOnClickListener(listenerBtnClickCancel);

    UtilUI.inflateDialog((LinearLayout) findViewById(R.id.dlg_filters_ll_main));

    // Restore UI state if possible.
    mState = context.getSharedPreferences("StateDlgFilters", Context.MODE_PRIVATE);
    mListView.setItemChecked(mState.getInt("selectedFilter", -1), true);
    if (mState.getBoolean("mDlgFilterInputIsOpen", false)) {
      showDlgFilterInput();
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
    prefsEditor.putInt("selectedFilter", mListView.getCheckedItemPosition());
    prefsEditor.putBoolean("mDlgFilterInputIsOpen", mDlgFilterInputIsOpen);
    prefsEditor.commit();
  }

  private android.view.View.OnClickListener listenerBtnClickOk = new android.view.View.OnClickListener() {
    public void onClick(View v) {
      // The user has chosen an attribute, now get a list of filters associated
      // with that attribute data type.
      showDlgFilterInput();
    }
  };

  private android.view.View.OnClickListener listenerBtnClickInfo = new android.view.View.OnClickListener() {
    public void onClick(View v) {
      UtilUI.showAlert(v.getContext(), "Sorry!",
          "We'll implement an info dialog about the selected filter soon!");
    }
  };

  private android.view.View.OnClickListener listenerBtnClickCancel = new android.view.View.OnClickListener() {
    public void onClick(View v) {
      resetUI();
      dismiss();
    }
  };

  private void showDlgFilterInput() {
    int position = mListView.getCheckedItemPosition();
    if (position < 0) {
      UtilUI.showAlert(getContext(), "Sorry!",
          "Please select a filter from the list above, then hit OK!");
      return;
    }

    // This is the filter they want to use.
    ModelFilter filter = (ModelFilter) mAdapterFilters.getItem(position);

    // Now we construct a blank DlgFilterInput dialog, and let the UI
    // factory class generate its content to capture the info needed for
    // this particular filter.
    DlgFilterInput dlg = new DlgFilterInput(getContext(), mAttribute, filter, null);
    dlg.setOnDismissListener(new OnDismissListener() {
      public void onDismiss(DialogInterface dialog) {
        // If the user constructed a valid filter, also kill ourselves.
        ModelFilter filter = (ModelFilter)DlgItemBuilderStore.instance().getBuiltItem();
        if (filter != null) {
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
    mDlgFilterInputIsOpen = false;
  }

  /**
   * Here we display filters associated with our parent attribute.
   */
  public class AdapterFilters extends BaseAdapter {

    private Context mContext;
    private ArrayList<ModelFilter> mFilters;

    public AdapterFilters(Context c, ModelAttribute attribute) {
      mContext = c;

      // Fetch all available filters for this attribute, from the database.
      mFilters = DbInterfaceUI.instance().db().getFiltersForAttribute(attribute);
    }

    public int getCount() {
      return mFilters.size();
    }

    public Object getItem(int position) {
      return mFilters.get(position);
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
      iv.setImageResource(mFilters.get(position).getIconResId());
      iv.setAdjustViewBounds(true);
      iv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT,
          LayoutParams.WRAP_CONTENT));
      if (mListView.getCheckedItemPosition() == position) {
        iv.setBackgroundResource(R.drawable.icon_hilight);
      }

      TextView tv = new TextView(mContext);
      tv.setText(mFilters.get(position).getDescriptionShort());
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