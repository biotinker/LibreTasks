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
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
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
import edu.nyu.cs.omnidroid.ui.simple.model.ModelAction;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelEvent;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelFilter;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelItem;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelRuleAction;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelRuleFilter;
import edu.nyu.cs.omnidroid.ui.simple.model.RuleNode;

/**
 * This Activity gives users a chance to combine multiple filters into a rule.
 */
public class ActivityChooseFilters extends Activity {

  private ListView mListview;
  private LinearLayout mLayoutButtonsFilter;
  private LinearLayout mLayoutButtonsTask;
  private mAdapterRule mAdapterRule;
  private boolean mDlgAttributesIsOpen;
  private boolean mDlgApplicationsIsOpen;
  private boolean mDlgEditFilterIsOpen;
  private boolean mDlgEditActionIsOpen;
  private SharedPreferences mState;

  public static final String KEY_STATE = "StateActivityChooseFilters";

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_choose_filters);

    mAdapterRule = new mAdapterRule(this);
    mAdapterRule.restoreFromRule();

    mListview = (ListView) findViewById(R.id.activity_choosefilters_listview);
    mListview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    mListview.setAdapter(mAdapterRule);

    // Link up bottom button panel areas.
    mLayoutButtonsFilter = (LinearLayout) findViewById(R.id.activity_choosefilters_llButtonsFilter);
    mLayoutButtonsTask = (LinearLayout) findViewById(R.id.activity_choosefilters_llButtonsAction);

    Button btnAddFilter = (Button) findViewById(R.id.activity_choosefilters_btnAddFilter);
    btnAddFilter.setOnClickListener(listenerBtnClickAddFilter);

    Button btnRemoveFilter = (Button) findViewById(R.id.activity_choosefilters_btnRemoveFilter);
    btnRemoveFilter.setOnClickListener(listenerBtnClickRemoveFilter);

    Button btnEditFilter = (Button) findViewById(R.id.activity_choosefilters_btnEditFilter);
    btnEditFilter.setOnClickListener(listenerBtnClickEditFilter);

    Button btnTasks = (Button) findViewById(R.id.activity_choosefilters_btnTasks);
    btnTasks.setOnClickListener(listenerBtnClickTasks);

    Button btnAddAction = (Button) findViewById(R.id.activity_choosefilters_btnAddAction);
    btnAddAction.setOnClickListener(listenerBtnClickAddAction);

    Button btnRemoveAction = (Button) findViewById(R.id.activity_choosefilters_btnRemoveAction);
    btnRemoveAction.setOnClickListener(listenerBtnClickRemoveAction);

    Button btnEditAction = (Button) findViewById(R.id.activity_choosefilters_btnEditAction);
    btnEditAction.setOnClickListener(listenerBtnClickEditAction);

    Button btnFilters = (Button) findViewById(R.id.activity_choosefilters_btnFilters);
    btnFilters.setOnClickListener(listenerBtnClickFilters);

    LinearLayout llBottomButtons = (LinearLayout) findViewById(R.id.activity_choosefilters_llBottomButtons);
    llBottomButtons.setBackgroundColor(0xFFBBBBBB);

    // Restore UI control values if possible.
    mState = getSharedPreferences(
      ActivityChooseFilters.KEY_STATE, 
      Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
    mListview.setItemChecked(mState.getInt("selectedRuleItem", 0), true);
    if (mState.getBoolean("mDlgAttributesIsOpen", false)) {
      showDlgAttributes();
    }
    if (mState.getBoolean("mDlgApplicationsIsOpen", false)) {
      showDlgApplications();
    }
    if (mState.getBoolean("mDlgEditFilterIsOpen", false)) {
        editFilter(mListview.getCheckedItemPosition(), (ModelRuleFilter)mAdapterRule.getItem(mListview.getCheckedItemPosition()));
    }
    if (mState.getBoolean("mDlgEditActionIsOpen", false)) {
        editAction(mListview.getCheckedItemPosition(), (ModelRuleAction)mAdapterRule.getItem(mListview.getCheckedItemPosition()));
    }
  }

  protected void onPause() {
    super.onPause();

    // Save UI state.
    SharedPreferences.Editor prefsEditor = mState.edit();
    prefsEditor.putInt("selectedRuleItem", mListview.getCheckedItemPosition());
    prefsEditor.putBoolean("mDlgAttributesIsOpen", mDlgAttributesIsOpen);
    prefsEditor.putBoolean("mDlgApplicationsIsOpen", mDlgApplicationsIsOpen);
    prefsEditor.putBoolean("mDlgEditFilterIsOpen", mDlgEditFilterIsOpen);
    prefsEditor.putBoolean("mDlgEditActionIsOpen", mDlgEditActionIsOpen);
    prefsEditor.commit();
  }

  private OnClickListener listenerBtnClickAddFilter = new OnClickListener() {
    public void onClick(View v) {
      int position = mListview.getCheckedItemPosition();
      if (position > -1 && position < mAdapterRule.getCount()) {
        ModelItem selectedItem = mAdapterRule.getItem(position);
        if (!(selectedItem instanceof ModelEvent) && !(selectedItem instanceof ModelFilter)) {
          UtilUI.showAlert(v.getContext(), "Sorry!", "Filters can only be added to the root event and other filters!");
          return;
        }

        // Reset our filter builder, which we can query for the final
        // constructed filter if the user sets one up ok.
        DlgItemBuilderStore.instance().reset();

        // Now we present the user with a list of attributes they can
        // filter on for their chosen root event.
        showDlgAttributes();
      } 
      else {
        UtilUI.showAlert(v.getContext(), "Sorry!", "Please select an item from the list before adding a filter!");
      }
    }
  };

  private OnClickListener listenerBtnClickRemoveFilter = new OnClickListener() {
    public void onClick(View v) {
      int position = mListview.getCheckedItemPosition();
      if (position == 0) {
        UtilUI.showAlert(v.getContext(), "Sorry!", "The root event cannot be removed!");
      } 
      else if (position > 0 && position < mAdapterRule.getCount()) {
        mAdapterRule.removeItem(position);
      } 
      else {
        UtilUI.showAlert(v.getContext(), "Sorry!",
            "Please select a filter from the list for removal!");
      }
    }
  };

  private OnClickListener listenerBtnClickEditFilter = new OnClickListener() {
    public void onClick(View v) {
      int position = mListview.getCheckedItemPosition();
      if (position == 0) {
        UtilUI.showAlert(v.getContext(), "Sorry!", "The root event cannot be modified!");
      } 
      else if (position > 0 && position < mAdapterRule.getCount()) {
        ModelItem item = mAdapterRule.getItem(position);
        if (item instanceof ModelRuleFilter) {
          editFilter(position, (ModelRuleFilter)item);
        } 
        else {
          UtilUI.showAlert(v.getContext(), "Sorry!", "Please select a filter to edit!");
        }
      } 
      else {
        UtilUI.showAlert(v.getContext(), "Sorry!",
            "Please select a filter from the list for editing!");
      }
    }
  };

  private OnClickListener listenerBtnClickTasks = new OnClickListener() {
    public void onClick(View v) {
      mLayoutButtonsFilter.setVisibility(View.GONE);
      mLayoutButtonsTask.setVisibility(View.VISIBLE);
    }
  };

  private OnClickListener listenerBtnClickAddAction = new OnClickListener() {
    public void onClick(View v) {
      // For actions, we can simply ignore what item they have selected and 
      // add the action directly to the root event. We may want to move 
      // action additions to a separate activity later on anyway.
          
      // Reset our filter builder, which we can query for the final
      // constructed filter if the user sets one up ok.
      DlgItemBuilderStore.instance().reset();

      // Now we present the user with a list of attributes they can
      // filter on for their chosen root event.
      showDlgApplications();
    }
  };

  private OnClickListener listenerBtnClickEditAction = new OnClickListener() {
    public void onClick(View v) {
      int position = mListview.getCheckedItemPosition();
      if (position > -1) {
    	  ModelItem item = mAdapterRule.getItem(position);
    	  if (item instanceof ModelRuleAction) {
    		  editAction(position, (ModelRuleAction)item);
    		  return;
    	  }
      } 
      UtilUI.showAlert(v.getContext(), "Sorry!", "Please select an action to edit!");
    }
  };

  private OnClickListener listenerBtnClickRemoveAction = new OnClickListener() {
    public void onClick(View v) {
      int position = mListview.getCheckedItemPosition();
      ModelItem selectedItem = mAdapterRule.getItem(position);
      if (selectedItem instanceof ModelAction) {

      } 
      else {
        UtilUI.showAlert(v.getContext(), "Sorry!", "Please select an action from the list to delete.");
      }
    }
  };

  private OnClickListener listenerBtnClickFilters = new OnClickListener() {
    public void onClick(View v) {
      mLayoutButtonsTask.setVisibility(View.GONE);
      mLayoutButtonsFilter.setVisibility(View.VISIBLE);
    }
  };

  private void showDlgAttributes() {
    // This is our root event.
    ModelEvent eventRoot = (ModelEvent) RuleBuilder.instance().getRule().getRootNode().getItem();

    // Create the attributes dialog using the root event as the parent.
    // When the dialog is killed, we check if the user successfully
    // finished the entire process of setting up a filter. If so, we
    // can add it to our rule builder.
    DlgAttributes dlg = new DlgAttributes(this, eventRoot);
    dlg.setOnDismissListener(new OnDismissListener() {
      public void onDismiss(DialogInterface dialog) {
        // Did the user construct a valid filter?
        ModelRuleFilter filter = (ModelRuleFilter)DlgItemBuilderStore.instance().getBuiltItem();
        if (filter != null) {
          // Add the filter to the rule builder and the UI tree.
          mAdapterRule.addItemToParentPosition(mListview.getCheckedItemPosition(), filter);
        }

        // Reset the filter builder for next time.
        DlgItemBuilderStore.instance().reset();

        // The attributes dialog is no longer open.
        mDlgAttributesIsOpen = false;
      }
    });
    dlg.show();

    // The attributes dialog is now open.
    mDlgAttributesIsOpen = true;
  }

  private void editFilter(final int position, ModelRuleFilter filter) {
    DlgFilterInput dlg = new DlgFilterInput(this, filter.getModelFilter(), filter.getData());
    dlg.setOnDismissListener(new OnDismissListener() {
      public void onDismiss(DialogInterface dialog) {
        // If the user constructed the filter ok, then replace the old
        // filter instance, otherwise do nothing.
        ModelRuleFilter filter = (ModelRuleFilter)DlgItemBuilderStore.instance().getBuiltItem();
        if (filter != null) {
          mAdapterRule.replaceItem(position, filter);
        }
        
        // The edit filter dialog is now closed.
        mDlgEditFilterIsOpen = false;
      }
    });
    dlg.show();

    // The attributes dialog is now open.
    mDlgEditFilterIsOpen = true;
  }
  
  private void editAction(final int position, ModelRuleAction action) {
    DlgActionInput dlg = 
	  new DlgActionInput(this, action.getModelAction(), action.getDatas());
	  dlg.setOnDismissListener(new OnDismissListener() {
	  public void onDismiss(DialogInterface dialog) {
	    // If the user constructed the action ok, then replace the old
	    // action instance, otherwise do nothing.
	    ModelRuleAction action = (ModelRuleAction)DlgItemBuilderStore.instance().getBuiltItem();
	    if (action != null) {
	      mAdapterRule.replaceItem(position, action);
	    }

        // The edit action dialog is now closed.
        mDlgEditActionIsOpen = false;
	  }
	});
    dlg.show();

    // The edit action dialog is now open.
    mDlgEditActionIsOpen = true;
  }
  
  private void showDlgApplications() {

    // Create the attributes dialog using the root event as the parent.
    // When the dialog is killed, we check if the user successfully
    // finished the entire process of setting up a filter. If so, we
    // can add it to our rule builder.
    DlgApplications dlg = new DlgApplications(this);
    dlg.setOnDismissListener(new OnDismissListener() {
      public void onDismiss(DialogInterface dialog) {

        // Did the user construct a valid action?
        ModelRuleAction action = (ModelRuleAction)DlgItemBuilderStore.instance().getBuiltItem();
        if (action != null) {
          // Add the filter to the rule builder and the UI tree.
          mAdapterRule.addItemToParentPosition(0, action);
        }

        // Reset the filter builder for next time.
        DlgItemBuilderStore.instance().reset();

        // The attributes dialog is no longer open.
        mDlgApplicationsIsOpen = false;
      }
    });
    dlg.show();

    // The applications dialog is now open.
    mDlgApplicationsIsOpen = true;
  }
  
  

  /**
   * Our rule has a tree hierarchy to it, we want to display is as a tree in our UI too. Rendering
   * as a pure tree would be slow, so we have an internal representation of the rule tree as a flat
   * list.
   * 
   * Android has a hierarchical list-tree widget, but it only handles a depth of one (1), while we
   * could have unlimited depth in our application. We could nest several list-trees inside one
   * another, but this might be overkill for our purposes. We also will probably want to custom
   * render the tree links depending on AND/OR relationship, which we couldn't do using the built-in
   * list-tree widget.
   * 
   * On each add/delete/replace, the whole dataset is iterated to build the flat-list
   * representation. This would be awful for large datasets, but we'll probably never have more than
   * twenty items.
   * 
   * We can definitely improve the way we store these items if we want, for now it's ok with
   * development and easy to understand.
   */
  public class mAdapterRule extends BaseAdapter {

    private Context mContext;

    /**
     * This is a 'flat' representation of our rule tree. The key is the integer index of a leaf, as
     * if you were counting from the top of the tree.
     */
    private HashMap<Integer, NodeWrapper> mFlat;

    public mAdapterRule(Context c) {
      mContext = c;
    }

    public int getCount() {
      return mFlat.size();
    }

    public ModelItem getItem(int position) {
      return mFlat.get(position).getNode().getItem();
    }

    public NodeWrapper getNodeWrapper(int position) {
      return mFlat.get(position);
    }

    public long getItemId(int position) {
      return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

      NodeWrapper it = getNodeWrapper(position);

      LinearLayout ll = new LinearLayout(mContext);
      ll.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
          LayoutParams.FILL_PARENT));
      ll.setMinimumHeight(50);
      ll.setOrientation(LinearLayout.HORIZONTAL);
      ll.setGravity(Gravity.CENTER_VERTICAL);

      ImageView iv = new ImageView(mContext);
      iv.setImageResource(it.getNode().getItem().getIconResId());
      iv.setAdjustViewBounds(true);
      iv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
      if (mListview.getCheckedItemPosition() == position) {
        iv.setBackgroundResource(R.drawable.icon_hilight);
      }

      TextView tv = new TextView(mContext);
      tv.setText(it.getNode().getItem().getDescriptionShort());
      tv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
      tv.setGravity(Gravity.CENTER_VERTICAL);
      tv.setPadding(10, 0, 0, 0);
      tv.setTextSize(14.0f);
      tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
      tv.setTextColor(0xFFFFFFFF);
      tv.setMinHeight(46);

      // This is where we figure out which tree branch graphics to stack
      // to the left of a node to give the appearance of a real tree widget.
      ArrayList<Integer> branches = it.getBranches();
      for (int i = 0; i < branches.size(); i++) {
        int imageResourceId;
        if (i == branches.size() - 1) {
          // You are whatever I say you are.
          imageResourceId = branches.get(i).intValue();
        } 
        else {
          // Here we do replacements.
          if (branches.get(i).intValue() == R.drawable.treebranch_child_end) {
            // empty png
            imageResourceId = R.drawable.treebranch_parent_empty;
          } 
          else {
            // straight pipe png
            imageResourceId = R.drawable.treebranch_parent;
          }
        }

        ImageView ivBranch = new ImageView(mContext);
        ivBranch.setImageResource(imageResourceId);
        ivBranch.setAdjustViewBounds(true);
        ivBranch.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        ll.addView(ivBranch);
      }

      ll.addView(iv);
      ll.addView(tv);
      return ll;
    }

    /**
     * Build ourselves from the current Rule state.
     */
    public void restoreFromRule() {
      mFlat = (new TreeToFlatArray()).convert(RuleBuilder.instance().getRule().getRootNode());
      notifyDataSetChanged();
    }

    /**
     * Here we add the item to the rule, then refresh our UI view of it.
     */
    public void addItemToParentPosition(int position, ModelItem item) {
      // Add the item.
      int positionOld = position;
      int positionNew;
      if (position < 0) {
        if (item instanceof ModelEvent) {
          RuleBuilder.instance().getRule().setRootEvent((ModelEvent) item);
          positionNew = 0;
        } 
        else {
          throw new IllegalArgumentException(
              "Somehow you added a non-event item as the root element!");
        }
      } 
      else {
        if (item instanceof ModelRuleFilter) {
          // If the user is adding a filter to the root event, we want to make
          // sure it gets added as a child before any actions.
          RuleNode nodeParent = getNodeWrapper(position).getNode();
          if (position == 0 && RuleBuilder.instance().getRule().getFirstActionPosition() > -1) {
            int insertionIndex = RuleBuilder.instance().getRule().getFirstActionPosition();
            nodeParent.addChild(item, insertionIndex);
            positionNew = insertionIndex;
          } 
          else {
            // Either our parent is another filter, or there are just no
            // actions added to the root event yet.
            nodeParent.addChild(item);
            positionNew = position + nodeParent.getChildren().size();
          }
        } 
        else if (item instanceof ModelRuleAction) {
          // Force adding of actions as siblings directly under the root event.
          RuleNode nodeParent = getNodeWrapper(0).getNode();
          nodeParent.addChild(item);
          positionNew = mFlat.size();
        } 
        else {
          throw new IllegalArgumentException("Couldn't add unknown item type to node!");
        }
      }
      // Flatten the rule again, we can improve this later if we need to.
      mFlat = (new TreeToFlatArray()).convert(RuleBuilder.instance().getRule().getRootNode());

      // Notify that we need a redraw.
      notifyDataSetChanged();

      // Auto-select the item we just added.
      if (positionOld > -1) {
        mListview.setItemChecked(positionOld, false);
      }
      mListview.setItemChecked(positionNew, true);
    }

    public void removeItem(int position) {
      // Not allowed to remove the root event!
      if (position < 1) {
        throw new IllegalArgumentException(
            "The user shouldn't be able to remove the 0th root event item from the list view!");
      }
      // Remove this item from the rule.
      RuleNode parent = mFlat.get(position).getNode().getParent();
      RuleNode child = mFlat.get(position).getNode();
      parent.removeChild(child);
      // Flatten again.
      mFlat = (new TreeToFlatArray()).convert(RuleBuilder.instance().getRule().getRootNode());
      notifyDataSetChanged();
    }

    public void replaceItem(int position, ModelItem item) {
      // Not allowed to modify the root event!
      if (position < 1) {
        throw new IllegalArgumentException(
            "The user should not be allowed to replace the 0th/root item in the list!");
      }
      RuleNode node = mFlat.get(position).getNode();
      node.setItem(item);
      notifyDataSetChanged();
    }

    public void removeAllFiltersAndTasks() {
      RuleBuilder.instance().getRule().getRootNode().removeAllChildren();
    }
  }

  /**
   * Converts our <code>Rule</code> node elements (which are in a tree form) into a flat
   * <code>HashMap</code> for fast drawing. Since adds and deletes aren't likely to happen often,
   * it's faster to maintain this form rather than searching throughout the tree struct every time
   * we need to draw an element.
   * 
   * As the tree is converted into a flat HashMap, we also generate which thumbnail images should be
   * shown next to each node in the tree UI widget.
   */
  public static class TreeToFlatArray {
    private int mIdAssigner;

    public HashMap<Integer, NodeWrapper> convert(RuleNode nodeRoot) {
      mIdAssigner = 0;
      HashMap<Integer, NodeWrapper> result = new HashMap<Integer, NodeWrapper>();
      flatten(result, nodeRoot, new ArrayList<Integer>());
      return result;
    }

    private void flatten(HashMap<Integer, NodeWrapper> result, RuleNode node,
        ArrayList<Integer> branches) {
      result.put(mIdAssigner++, new NodeWrapper(node, branches));
      for (int i = 0; i < node.getChildren().size(); i++) {
        ArrayList<Integer> branchesChild = new ArrayList<Integer>();
        for (int j = 0; j < branches.size(); j++) {
          branchesChild.add(new Integer(branches.get(j).intValue()));
        }
        if (i == node.getChildren().size() - 1) {
          // last child.
          branchesChild.add(new Integer(R.drawable.treebranch_child_end));
        } 
        else {
          // sibling child.
          branchesChild.add(new Integer(R.drawable.treebranch_child));
        }
        flatten(result, node.getChildren().get(i), branchesChild);
      }
    }
  }

  /**
   * Simply bundles a <code>RuleNode</code> with its tree-image int array for display in the UI.
   */
  public static class NodeWrapper {
    private RuleNode mNode;
    private ArrayList<Integer> mBranches;

    public NodeWrapper(RuleNode node, ArrayList<Integer> branches) {
      mNode = node;
      mBranches = branches;
    }

    public RuleNode getNode() {
      return mNode;
    }

    public ArrayList<Integer> getBranches() {
      return mBranches;
    }
  }
}