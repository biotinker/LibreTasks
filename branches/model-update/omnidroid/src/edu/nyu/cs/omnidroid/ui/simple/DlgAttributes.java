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
import edu.nyu.cs.omnidroid.ui.simple.model.ModelEvent;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelFilter;

/**
 * This dialog takes a <code>ModelEvent</code> as input, and generates a list of
 * <code>ModelAttribute</code> instances associated with it. The user can choose an attribute to
 * filter on - we look up all filters associated with the selected attribute.
 */
public class DlgAttributes extends Dialog {

  private TextView mTextViewInfo;
  private ListView mListView;
  private AdapterAttributes mAdapterAttributes;
  private boolean mDlgFiltersIsOpen;
  private SharedPreferences mState;

  public DlgAttributes(Context context, ModelEvent eventRoot) {
    super(context);
    setContentView(R.layout.dlg_attributes);
    setTitle("Attributes");

    mAdapterAttributes = new AdapterAttributes(getContext(), eventRoot);

    mListView = (ListView) findViewById(R.id.dlg_attributes_listview);
    mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    mListView.setAdapter(mAdapterAttributes);

    mTextViewInfo = (TextView) findViewById(R.id.dlg_attributes_tv_info1);
    mTextViewInfo.setText("Select an attribute of [" + eventRoot.getTypeName() + "] to filter on:");

    Button btnOk = (Button) findViewById(R.id.dlg_attributes_btnOk);
    btnOk.setOnClickListener(listenerBtnClickOk);
    Button btnInfo = (Button) findViewById(R.id.dlg_attributes_btnInfo);
    btnInfo.setOnClickListener(listenerBtnClickInfo);
    Button btnCancel = (Button) findViewById(R.id.dlg_attributes_btnCancel);
    btnCancel.setOnClickListener(listenerBtnClickCancel);

    UtilUI.inflateDialog((LinearLayout) findViewById(R.id.dlg_attributes_ll_main));

    // Restore UI state.
    mState = context.getSharedPreferences("StateDlgAttributes", Context.MODE_PRIVATE);
    mListView.setItemChecked(mState.getInt("selectedAttribute", -1), true);
    if (mState.getBoolean("mDlgFiltersIsOpen", false)) {
      showDlgFilters();
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
    prefsEditor.putInt("selectedAttribute", mListView.getCheckedItemPosition());
    prefsEditor.putBoolean("mDlgAttributesIsOpen", mDlgFiltersIsOpen);
    prefsEditor.commit();
  }

  private android.view.View.OnClickListener listenerBtnClickOk = new android.view.View.OnClickListener() {
    public void onClick(View v) {
      // The user has chosen an attribute, now get a list of filters associated
      // with that attribute, from the database.
      int position = mListView.getCheckedItemPosition();
      if (position < 0) {
        UtilUI.showAlert(v.getContext(), "Sorry!",
            "Please select an attribute from the list above to filter on!");
        return;
      }

      // Show a dialog with only these filters listed.
      showDlgFilters();
    }
  };

  private android.view.View.OnClickListener listenerBtnClickInfo = new android.view.View.OnClickListener() {
    public void onClick(View v) {
      UtilUI.showAlert(v.getContext(), "Sorry!",
          "We'll implement an info dialog about the selected attribute soon!");
    }
  };

  private android.view.View.OnClickListener listenerBtnClickCancel = new android.view.View.OnClickListener() {
    public void onClick(View v) {
      // Be sure to wipe our UI state, otherwise the onStop will save it!
      resetUI();
      dismiss();
    }
  };

  private void showDlgFilters() {
    int position = mListView.getCheckedItemPosition();
    DlgFilters dlg = new DlgFilters(DlgAttributes.this.getContext(),
        (ModelAttribute) mAdapterAttributes.getItem(position));
    dlg.setOnDismissListener(new OnDismissListener() {
      public void onDismiss(DialogInterface dialog) {
        // If the user constructed a valid filter, also kill ourselves.
        ModelFilter filter = FilterBuilder.instance().getFilter();
        if (filter != null) {
          // Be sure to wipe our UI state, otherwise the onStop will save it!
          resetUI();
          dismiss();
        }
      }
    });
    dlg.show();

    mDlgFiltersIsOpen = true;
  }

  private void resetUI() {
    if (mListView.getCheckedItemPosition() > -1) {
      mListView.setItemChecked(mListView.getCheckedItemPosition(), false);
    }
    mDlgFiltersIsOpen = false;
  }

  /**
   * Here we display attributes associated with our parent root event.
   */
  public class AdapterAttributes extends BaseAdapter {
    private Context mContext;
    private ArrayList<ModelAttribute> mAttributes;

    public AdapterAttributes(Context c, ModelEvent eventRoot) {
      mContext = c;

      // Fetch all available attributes for the root event from the
      // database.
      mAttributes = DbInterfaceUI.instance().db().getAttributesForEvent(eventRoot);
    }

    public int getCount() {
      return mAttributes.size();
    }

    public Object getItem(int position) {
      return mAttributes.get(position);
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
      iv.setImageResource(mAttributes.get(position).getIconResId());
      iv.setAdjustViewBounds(true);
      iv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT,
          LayoutParams.WRAP_CONTENT));
      if (mListView.getCheckedItemPosition() == position) {
        iv.setBackgroundResource(R.drawable.icon_hilight);
      }

      TextView tv = new TextView(mContext);
      tv.setText(mAttributes.get(position).getDescriptionShort());
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