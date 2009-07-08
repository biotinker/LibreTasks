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
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelEvent;

/**
 * This activity gives users a chance to pick a root event for a new rule. After a user chooses a
 * root event, we move them to <code>ActivityChooseFilters</code> to continue building their rule.
 */
public class ActivityChooseRootEvent extends Activity {

  private ListView mListView;
  private AdapterEvents mAdapterEvents;
  private SharedPreferences mState;

  public static final String KEY_STATE = "StateActivityChooseRootEvent";

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_choose_root_event);

    mAdapterEvents = new AdapterEvents(this);

    mListView = (ListView) findViewById(R.id.activity_chooserootevent_listview);
    mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    mListView.setAdapter(mAdapterEvents);

    Button btnCreateRule = (Button) findViewById(R.id.activity_chooserootevent_btnOk);
    btnCreateRule.setOnClickListener(listenerBtnClickCreateRule);

    Button btnHelp = (Button) findViewById(R.id.activity_chooserootevent_btnHelp);
    btnHelp.setOnClickListener(listenerBtnClickHelp);

    LinearLayout llBottomButtons = (LinearLayout) findViewById(R.id.activity_chooserootevent_llBottomButtons);
    llBottomButtons.setBackgroundColor(0xFFBBBBBB);

    // Restore UI control values if possible.
    mState = getSharedPreferences(ActivityChooseRootEvent.KEY_STATE, Context.MODE_WORLD_READABLE
        | Context.MODE_WORLD_WRITEABLE);
    mListView.setItemChecked(mState.getInt("selectedEventItem", -1), true);
  }

  protected void onPause() {
    super.onPause();

    // Save UI state.
    SharedPreferences.Editor prefsEditor = mState.edit();
    prefsEditor.putInt("selectedEventItem", mListView.getCheckedItemPosition());
    prefsEditor.commit();
  }

  private OnClickListener listenerBtnClickCreateRule = new OnClickListener() {
    public void onClick(View v) {
      int selectedEventPosition = mListView.getCheckedItemPosition();
      if (selectedEventPosition > -1 && selectedEventPosition < mAdapterEvents.getCount()) {
        // The user has chosen a valid root event, store it
        // in the global RuleBuilder.
        RuleBuilder.instance().reset(mAdapterEvents.getItem(selectedEventPosition));

        // Wipe UI state for the activity.
        SharedPreferences state = v.getContext().getSharedPreferences(
            ActivityChooseFilters.KEY_STATE, Context.MODE_PRIVATE);
        state.edit().clear().commit();

        // Move them along to the ActivityChooseFilters activity where
        // they can start adding some filters.
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), ActivityChooseFilters.class);
        startActivity(intent);
      } else {
        UtilUI.showAlert(v.getContext(), "Sorry!",
            "Please select an event from the list, then hit OK.");
      }
    }
  };

  private OnClickListener listenerBtnClickHelp = new OnClickListener() {
    public void onClick(View v) {
      UtilUI.showAlert(v.getContext(), "Sorry!",
          "Help is not yet available for events. Eventually this would "
              + "show details about each event type.");
    }
  };

  /**
   * Handles rendering of individual event items for our ListView. For now, we get our
   * <code>ModelEvent</code> list from our dummy database in <code>DummyDatabase</code>.
   */
  public class AdapterEvents extends BaseAdapter {
    private Context mContext;
    private ArrayList<ModelEvent> mEvents;

    public AdapterEvents(Context c) {
      mContext = c;

      // We will most likely cache the list of all events we get from
      // the 'real' database.
      mEvents = DbInterfaceUI.instance().db().getEvents();
    }

    public int getCount() {
      return mEvents.size();
    }

    public ModelEvent getItem(int position) {
      return mEvents.get(position);
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
      iv.setImageResource(mEvents.get(position).getIconResId());
      iv.setAdjustViewBounds(true);
      iv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT,
          LayoutParams.WRAP_CONTENT));
      if (mListView.getCheckedItemPosition() == position) {
        iv.setBackgroundResource(R.drawable.icon_hilight);
      }

      TextView tv = new TextView(mContext);
      tv.setText(mEvents.get(position).getDescriptionShort());
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