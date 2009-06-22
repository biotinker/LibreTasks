package edu.nyu.cs.omnidroid.uiresearchsimple;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import edu.nyu.cs.omnidroid.uiresearchsimple.dlg.DlgFilterList;
import edu.nyu.cs.omnidroid.uiresearchsimple.dlg.FilterDlgFactory;
import edu.nyu.cs.omnidroid.uiresearchsimple.dlg.IDlgFilter;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelEvent;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelFilter;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelItem;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.Rule;


/**
 * This Activity gives users a chance to combine multiple
 * filters into a rule.
 */
public class ActivityChooseFilters extends Activity 
{	
	private ListView listView;
	private RulesetAdapter rulesetAdapter;
	private Handler mHandler = new Handler();
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_filters);

        // Our adapter will fetch all event data from the database (eventually).
        rulesetAdapter = new RulesetAdapter(this, (ModelEvent)RuleBuilder.instance().getRule().getRootEvent().getItem());

        // Allow only a single choice in the list view, hook up the adapter.
        listView = (ListView)findViewById(R.id.activity_choosefilters_listview);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(rulesetAdapter);
        
        // Link up click handlers with their buttons.
        Button btnAddFilter = (Button)findViewById(R.id.activity_choosefilters_btnAddFilter);
        btnAddFilter.setOnClickListener(listenerBtnClickAddFilter);
        
        Button btnRemoveFilter = (Button)findViewById(R.id.activity_choosefilters_btnRemoveFilter);
        btnRemoveFilter.setOnClickListener(listenerBtnClickRemoveFilter);
        
        Button btnEditFilter = (Button)findViewById(R.id.activity_choosefilters_btnEditFilter);
        btnEditFilter.setOnClickListener(listenerBtnClickEditFilter);
        
        Button btnDone= (Button)findViewById(R.id.activity_choosefilters_btnDone);
        btnDone.setOnClickListener(listenerBtnClickDone);
        
        Button btnCancel = (Button)findViewById(R.id.activity_choosefilters_btnCancel);
        btnCancel.setOnClickListener(listenerBtnClickCancel);
    }
    
    private OnClickListener listenerBtnClickAddFilter = new OnClickListener() {
        public void onClick(View v) {
        	final int selectedEventPosition = listView.getCheckedItemPosition();
        	if (selectedEventPosition > -1 && selectedEventPosition < rulesetAdapter.getCount()) {
        		// User wants to either add a filter to the currently selected item.
        		// Let them choose a new filter to add.
        		DlgFilterList dlg = new DlgFilterList(ActivityChooseFilters.this);
        		dlg.setOnDismissListener(new OnDismissListener() {
					//@Override
					public void onDismiss(DialogInterface dialog) {
						// Did the user construct a valid filter?
						if (((IDlgFilter)dialog).getDidUserConstructFilter()) {
							// Just point to the constructed filter then dismiss ourselves.
							// Our parent can then pick up the filter as we die.
							ModelFilter filter = ((IDlgFilter)dialog).getConstructedFilter();
							rulesetAdapter.addItem(selectedEventPosition, filter);
						}
					}
        		});
        		dlg.show();
        	}
        	else {
            	Util.showAlert(v.getContext(), "Sorry!", "Please select an item from the list before adding a filter!");
        	}
        }
    };
    
    private OnClickListener listenerBtnClickRemoveFilter = new OnClickListener() {
        public void onClick(View v) {
        	final int selectedEventPosition = listView.getCheckedItemPosition();
        	if (selectedEventPosition == 0) {
        		Util.showAlert(v.getContext(), "Sorry!", "The root event cannot be removed!");
        	}
        	else if (selectedEventPosition > 0 && selectedEventPosition < rulesetAdapter.getCount()) {
        		rulesetAdapter.removeItem(selectedEventPosition);
        	}
        	else {
            	Util.showAlert(v.getContext(), "Sorry!", "Please select a filter from the list for removal!");
        	}
        }
    };
    
    private OnClickListener listenerBtnClickEditFilter = new OnClickListener() {
        public void onClick(View v) {
        	final int selectedEventPosition = listView.getCheckedItemPosition();
        	if (selectedEventPosition == 0) {
        		Util.showAlert(v.getContext(), "Sorry!", "The root event cannot be modified! (yet?)");
        	}
        	else if (selectedEventPosition > 0 && selectedEventPosition < rulesetAdapter.getCount()) {
        		ModelItem item = rulesetAdapter.getItem(selectedEventPosition);
	        	Dialog dlg = FilterDlgFactory.getDialogForFilter(v.getContext(), item);
	    		dlg.setOnDismissListener(new OnDismissListener() {
					//@Override
					public void onDismiss(DialogInterface arg0) {
						// When the user is done modifying this filter, we just replace 
						// the original copy back here.
						if (((IDlgFilter)arg0).getDidUserConstructFilter()) {
							rulesetAdapter.replaceItem(selectedEventPosition, ((IDlgFilter)arg0).getConstructedFilter());
						}
					}
	    		});
	    		dlg.show();
        	}
        	else {
        		Util.showAlert(v.getContext(), "Sorry!", "Please select a filter from the list for editing!");
        	}
        }
    };
    
    private OnClickListener listenerBtnClickDone = new OnClickListener() {
        public void onClick(View v) {
        	// TODO: Now let the user add tasks to be executed if the rule is true.
        	Util.showAlert(v.getContext(), "Sorry!", "After adding filters, we'll take the user to another Activity where they can add tasks.");
        }
    };
    
    private OnClickListener listenerBtnClickCancel = new OnClickListener() {
        public void onClick(View v) {
        	// Warn the user that their rule data will be lost if exiting.
        	new AlertDialog.Builder(v.getContext()).setTitle("Warning!")
    	    .setIcon(0)
    	    .setMessage("Your rule data will be lost, are you sure you continue?")
    	    .setCancelable(true)
    	    .setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    	// Reset the global rule.
                    	RuleBuilder.instance().reset();
                  	    finish();
                    }
                }
    	    )
    	    .setNegativeButton("No", 
    	    	new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
              	    }
            	}
    	    )
    	    .show();
        }
    };
    
	
	/**
	 * Our rule has a tree hierarchy to it, and we want to
	 * display it as such in our ListView. Rendering as a 
	 * pure tree would be slow, so we have an internal 
	 * representation of the dataset as a flat list.
	 * 
	 * Android has a hierarchical list-tree widget, but it
	 * only handles a depth of one, while we could have 
	 * unlimited depth. We could nest several list-trees 
	 * inside one another, but this might be overkill for our
	 * purposes. We also will probably want to custom render
	 * the tree links depending on AND/OR relationship, which
	 * we couldn't do using the built-in list-tree widget.
	 * 
	 * On each add/delete/replace, the whole dataset is iterated
	 * to build the flat-list representation in <code>mCheats</code>.
	 * This would be awful for large datasets, but we'll probably 
	 * never have more than twenty items.
	 * 
	 * We can definitely improve the way we store these items
	 * if we want, for now it's ok with development.
	 */
	public class RulesetAdapter extends BaseAdapter 
    {
    	private Context mContext;
    	private HashMap<Integer, Rule.RuleNode> mCheat; // Map positionIndeces to tree elements, ugh.
    	private int mIdAssigner;
    	
    	
    	/**
    	 * Always has to start out with a root Event, which is
    	 * like SMS_RECV, etc.
    	 * @param c
    	 * @param item
    	 */
        public RulesetAdapter(Context c, ModelEvent rootItem) {
        	mContext = c;
        	mCheat = new HashMap<Integer, Rule.RuleNode>();
        	
        	// Use the public routine for adding the root element.
        	addItem(-1, rootItem);
        }
        
        public int getCount() {
            return mCheat.size();
        }

        public ModelItem getItem(int position) {
            return mCheat.get(position).getItem();
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
        	
        	Rule.RuleNode it = mCheat.get(position);
        	
        	LinearLayout ll = new LinearLayout(mContext);
        	ll.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        	ll.setMinimumHeight(50);
        	ll.setOrientation(LinearLayout.HORIZONTAL);
        	ll.setGravity(Gravity.CENTER_VERTICAL);
        	
        	ImageView iv = new ImageView(mContext);
        	iv.setImageResource(it.getItem().getDisplayInfo().getIconResId());
        	iv.setAdjustViewBounds(true);
        	iv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        	if (listView.getCheckedItemPosition() == position) {
        		iv.setBackgroundResource(R.drawable.icon_hilight);
        	}
        	
            TextView tv = new TextView(mContext);
            tv.setText(it.getItem().getDisplayInfo().getTitle());
            tv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setPadding(10, 0, 0, 0);
            tv.setTextSize(16.0f);
	        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
	        tv.setTextColor(0xFFFFFFFF);
            tv.setMinHeight(46);

            if (it.getDepth() > 0) {
            	FrameLayout flPadding = new FrameLayout(mContext);
            	flPadding.setLayoutParams(new FrameLayout.LayoutParams(it.getDepth()*20, 40));
            	ll.addView(flPadding);
        	}
            ll.addView(iv);
            ll.addView(tv);
            
            return ll;
        }
 
        private void rewriteCheats(Rule.RuleNode node, int depth)
        {	
        	node.setDepth(depth++);
        	mCheat.put(mIdAssigner++, node);
        	for (int i = 0; i < node.getChildren().size(); i++) {
        		rewriteCheats(node.getChildren().get(i), depth);
        	}
        }
        
        public void addItem(int position, ModelItem item) {
        	// Add the item.
        	final int positionAddTo = position;
        	final int positionAdded;
        	if (position < 0) {
        		// TODO: Type-check this or make a separate proxy call for it.
        		RuleBuilder.instance().getRule().setRootEvent((ModelEvent)item);
        		positionAdded = 0;
        	}
        	else {
        		Rule.RuleNode node = mCheat.get(position);
        		node.addChild(item);
        		positionAdded = position + node.getChildren().size();
        	}
        	// Rewrite all cheats from this point onward, very slow.
        	mCheat.clear();
        	mIdAssigner = 0;
        	rewriteCheats(RuleBuilder.instance().getRule().getRootEvent(), 0);	
        	// Notify that we need a redraw.
            notifyDataSetChanged();

            // Auto-select the item we just added. This needs to be queued 
            // because the list won't be updated with the new item here.
            mHandler.post(
    			new Thread() {
    				public void run() {
    					if (positionAddTo > -1) {
    		            	listView.setItemChecked(positionAddTo, false);
    		            }
    		            listView.setItemChecked(positionAdded, true);
    				}
    			}
    		);
        }
        
        public void removeItem(int position) {
        	// Not allowed to remove the root event!
        	if (position < 1) {
        		return;
        	}
        	// Remove this item from the rule, we have to proxy this for safety later.
        	Rule.RuleNode parent = mCheat.get(position).getParent();
        	Rule.RuleNode child  = mCheat.get(position);
        	parent.removeChild(child);
        	// Rewrite all cheats.
        	mCheat.clear();
        	mIdAssigner = 0;
        	rewriteCheats(RuleBuilder.instance().getRule().getRootEvent(), 0);
            notifyDataSetChanged();
        }
        
        public void replaceItem(int position, ModelItem item) {
        	// Not allowed to modify the root event!
        	if (position < 1) {
        		return;
        	}
        	Rule.RuleNode node = mCheat.get(position);
        	node.setItem(item);
            notifyDataSetChanged();
        }
    }
}