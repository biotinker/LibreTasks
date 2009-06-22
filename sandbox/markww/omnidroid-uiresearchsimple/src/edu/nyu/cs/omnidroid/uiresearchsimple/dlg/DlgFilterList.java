package edu.nyu.cs.omnidroid.uiresearchsimple.dlg;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import edu.nyu.cs.omnidroid.uiresearchsimple.DatabaseMemCache;
import edu.nyu.cs.omnidroid.uiresearchsimple.R;
import edu.nyu.cs.omnidroid.uiresearchsimple.Util;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelFilter;


/**
 * Presents users with a list of all filters as fetched
 * from the database.
 */
public class DlgFilterList extends Dialog 
	implements IDlgFilter
{
	private ModelFilter mFilter;
	private ListView listView;
	private FiltersAdapter filtersAdapter;
 
	
	public DlgFilterList(Context context)
	{
		super(context);
        setContentView(R.layout.dlg_filter_list);
		setTitle("Choose A Filter");
		
		
		// Our adapter will fetch all event data from the database (eventually).
		filtersAdapter = new FiltersAdapter(getContext());

        // Eh.
        listView = (ListView)findViewById(R.id.dlg_filters_listview);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(filtersAdapter);
        
        // Link up click handlers with their buttons.
        Button btnOk = (Button)findViewById(R.id.dlg_filters_btnOk);
        btnOk.setOnClickListener(listenerBtnClickOk);
        Button btnInfo = (Button)findViewById(R.id.dlg_filters_btnInfo);
        btnInfo.setOnClickListener(listenerBtnClickInfo);
        Button btnCancel = (Button)findViewById(R.id.dlg_filters_btnCancel);
        btnCancel.setOnClickListener(listenerBtnClickCancel);

        // Force the dialog to use up most of the visible screen area.
		Util.inflateDialog(context, (LinearLayout)findViewById(R.id.dlg_filters_ll));
    }
	
	@Override
	protected void onStart() {
	}
	
	public ModelFilter getConstructedFilter()
	{
		return mFilter;
	}
	
	public boolean getDidUserConstructFilter()
	{
		return mFilter != null;
	}

	private android.view.View.OnClickListener listenerBtnClickOk = new android.view.View.OnClickListener() {
        public void onClick(View v) {
        	int selectedEventPosition = listView.getCheckedItemPosition();
        	if (selectedEventPosition > -1 && selectedEventPosition < filtersAdapter.getCount()) {
        		// User wants to either add a filter to the currently selected item.
        		// Let them choose a new filter to add.
        		Dialog dlg = FilterDlgFactory.getDialogForFilter(
        			v.getContext(), 
        			filtersAdapter.getItemDlgClassName(selectedEventPosition));
        		dlg.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface arg0) {
						// We can asl the dying dialog if it was able to build a filter
						// instance. If so, we can dismiss ourselves to propagate back
						// up to our own parent. Otherwise we do nothing, maybe the user
						// picked the wrong filter and wants to try again.
						if (((IDlgFilter)arg0).getDidUserConstructFilter()) {
							// Just point to the constructed filter then dismiss ourselves.
							// Our parent can then pick up the filter as we die.
							mFilter = ((IDlgFilter)arg0).getConstructedFilter();
							dismiss();
						}
					}
        		});
        		dlg.show();
        	}
        	else {
            	Util.showAlert(v.getContext(), "Sorry!", "Please select a filter from the list!");
        	}
        }
    };
    
    private android.view.View.OnClickListener listenerBtnClickInfo = new android.view.View.OnClickListener() {
        public void onClick(View v) {
        	Util.showAlert(v.getContext(), "Sorry!", "We'll implement info dialogs about each filter soon.");
        }
    };
    
    private android.view.View.OnClickListener listenerBtnClickCancel = new android.view.View.OnClickListener() {
        public void onClick(View v) {
        	dismiss();
        }
    };
	
	/**
     * Provides data for the listview, in this case all
     * Filters provided by the Omnidroid system, as read
     * from the database.
     */
    public class FiltersAdapter extends BaseAdapter 
    {
    	private Context mContext;
    	
        public FiltersAdapter(Context c) {
        	mContext = c;
        }
        
        public int getCount() {
            return DatabaseMemCache.instance().getFilters().size();
        }
        
        @Override
        public Object getItem(int position) {
        	return DatabaseMemCache.instance().getFilters().get(position);
        }

        public String getItemDlgClassName(int position) {
            return DatabaseMemCache.instance().getFilters().get(position).getDlgClassName();
        }
        
        public String getItemName(int position) {
            return DatabaseMemCache.instance().getFilters().get(position).getTypeName();
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
        	iv.setImageResource(DatabaseMemCache.instance().getFilters().get(position).getDisplayInfo().getIconResId());
        	iv.setAdjustViewBounds(true);
        	iv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        	if (listView.getCheckedItemPosition() == position) {
        		iv.setBackgroundResource(R.drawable.icon_hilight);
        	}
        	
            TextView tv = new TextView(mContext);
            tv.setText(DatabaseMemCache.instance().getFilters().get(position).getDisplayInfo().getTitle());
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