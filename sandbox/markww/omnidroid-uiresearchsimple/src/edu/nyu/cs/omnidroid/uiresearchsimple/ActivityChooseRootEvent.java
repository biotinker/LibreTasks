package edu.nyu.cs.omnidroid.uiresearchsimple;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelEvent;


/**
 * This Activity gives users a chance to pick a root
 * event for a new rule.
 */
public class ActivityChooseRootEvent extends Activity {
	
	private ListView listView;
	private EventAdapter eventAdapter;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_root_event);
        
        // Our adapter will fetch all event data from the database (eventually).
        eventAdapter = new EventAdapter(this);

        // Allow only a single choice in the list view, hook up the adapter.
        listView = (ListView)findViewById(R.id.activity_chooserootevent_listview);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(eventAdapter);
        
        // Link up click handlers with their buttons.
        Button btnCreateRule = (Button)findViewById(R.id.activity_chooserootevent_btnOk);
        btnCreateRule.setOnClickListener(listenerBtnClickCreateRule);

        Button btnViewRules = (Button)findViewById(R.id.activity_chooserootevent_btnCancel);
        btnViewRules.setOnClickListener(listenerBtnClickCancel);
        
        Button btnHelp = (Button)findViewById(R.id.activity_chooserootevent_btnHelp);
        btnHelp.setOnClickListener(listenerBtnClickHelp);     
    }
    
    private OnClickListener listenerBtnClickCreateRule = new OnClickListener() {
        public void onClick(View v) {
        	int selectedEventPosition = listView.getCheckedItemPosition();
        	if (selectedEventPosition > -1 && selectedEventPosition < eventAdapter.getCount()) {
        		// Set the root event.
        		RuleBuilder.instance().getRule().setRootEvent(eventAdapter.getItem(selectedEventPosition));
        		
        		// User wants to now pick some filters, move along to that Activity.
            	Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ActivityChooseFilters.class);
                startActivity(intent);
        	}
        	else {
            	Util.showAlert(v.getContext(), "Sorry!", "Please select an event from the list, then hit OK.");
        	}
        }
    };
    
    private OnClickListener listenerBtnClickCancel = new OnClickListener() {
        public void onClick(View v) {
        	finish();
        }
    };
    
    private OnClickListener listenerBtnClickHelp = new OnClickListener() {
        public void onClick(View v) {
        	Util.showAlert(v.getContext(), 
        		"Sorry!", 
        		"Help is not yet available for events. Eventually this would " +
        		"show details about each event type.");
        }
    };
    
    
    /**
     * Handles rendering of individual items for our ListView.
     * The data is taken from the global <code>ModelEvent</code>
     * list within <code>DataMemCache</code>.
     */
    public class EventAdapter extends BaseAdapter 
    {
    	private Context mContext;
    	
        public EventAdapter(Context c) {
        	mContext = c;
        }
        
        public int getCount() {
            return DatabaseMemCache.instance().getEvents().size();
        }

        public ModelEvent getItem(int position) {
            return DatabaseMemCache.instance().getEvents().get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
        	
        	LinearLayout ll = new LinearLayout(mContext);
        	ll.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        	ll.setMinimumHeight(50);
        	ll.setOrientation(LinearLayout.HORIZONTAL);
        	ll.setGravity(Gravity.CENTER_VERTICAL);
        	
        	ImageView iv = new ImageView(mContext);
        	iv.setImageResource(DatabaseMemCache.instance().getEvents().get(position).getDisplayInfo().getIconResId());
        	iv.setAdjustViewBounds(true);
        	iv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        	if (listView.getCheckedItemPosition() == position) {
        		iv.setBackgroundResource(R.drawable.icon_hilight);
        	}
        	
            TextView tv = new TextView(mContext);
            tv.setText(DatabaseMemCache.instance().getEvents().get(position).getDisplayInfo().getTitle());
            tv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setPadding(10, 0, 0, 0);
            tv.setTextSize(16.0f);
	        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
	        tv.setTextColor(0xFFFFFFFF);
            tv.setMinHeight(46);

            ll.addView(iv);
            ll.addView(tv);
            return ll;
        }
    }
}