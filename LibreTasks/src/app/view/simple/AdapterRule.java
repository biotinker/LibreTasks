/*  
 * Copyright (c) 2016  LibreTasks - https://github.com/biotinker/LibreTasks  
 *  
 *  This file is free software: you may copy, redistribute and/or modify it  
 *  under the terms of the GNU General Public License as published by the  
 *  Free Software Foundation, either version 3 of the License, or (at your  
 *  option) any later version.  
 *  
 *  This file is distributed in the hope that it will be useful, but  
 *  WITHOUT ANY WARRANTY; without even the implied warranty of  
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  
 *  General Public License for more details.  
 *  
 *  You should have received a copy of the GNU General Public License  
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.  
 *  
 * This file incorporates work covered by the following copyright and  
 * permission notice:  
 /*******************************************************************************
 * Copyright 2010 Omnidroid - http://code.google.com/p/omnidroid 
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
package libretasks.app.view.simple;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import libretasks.app.R;
import libretasks.app.view.simple.model.ModelEvent;
import libretasks.app.view.simple.model.ModelItem;
import libretasks.app.view.simple.model.ModelRuleAction;
import libretasks.app.view.simple.model.ModelRuleFilter;
import libretasks.app.view.simple.model.Rule;
import libretasks.app.view.simple.model.RuleNode;

/**
 * Our rule has a tree hierarchy to it, we want to display is as a tree in our UI too. Rendering as
 * a pure tree would be slow, so we have an internal representation of the rule tree as a flat 
 * list.
 * 
 * Android has a hierarchical list-tree widget, but it only handles a depth of one (1), while we
 * could have unlimited depth in our application. We could nest several list-trees inside one
 * another, but this might be overkill for our purposes. We also will probably want to custom 
 * render the tree links depending on AND/OR relationship, which we couldn't do using the 
 * built-in list-tree widget.
 * 
 * On each add/delete/replace, the whole dataset is iterated to build the flat-list representation.
 * This would be awful for large datasets, but we'll probably never have more than twenty items.
 * 
 * We can definitely improve the way we store these items if we want, for now it's ok with
 * development and easy to understand.
 */
public class AdapterRule extends BaseAdapter {

  private Context context;
  private ListView listView;
  
  /** The rule we're supposed to be rendering. */
  private Rule rule;
  
  /**
   * This is a 'flat' representation of our rule tree. The key is the integer index of a leaf, as 
   * if you were counting from the top of the tree.
   */
  private HashMap<Integer, NodeWrapper> flat;

  public AdapterRule(Context context, ListView listView) {
    this.context  = context;
    this.listView = listView;
    this.flat = new HashMap<Integer, NodeWrapper>();
  }

  public int getCount() {
    return flat.size();
  }

  public ModelItem getItem(int position) {
    return flat.get(position).getNode().getItem();
  }

  public NodeWrapper getNodeWrapper(int position) {
    return flat.get(position);
  }

  public long getItemId(int position) {
    return position;
  }

  /**
   * Generates a single item in the listview tree widget.
   */
  public View getView(int position, View convertView, ViewGroup parent) {

    NodeWrapper it = getNodeWrapper(position);

    LinearLayout ll = new LinearLayout(context);
    ll.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
        LayoutParams.FILL_PARENT));
    ll.setMinimumHeight(50);
    ll.setOrientation(LinearLayout.HORIZONTAL);
    ll.setGravity(Gravity.CENTER_VERTICAL);

    ImageView iv = new ImageView(context);
    iv.setImageResource(it.getNode().getItem().getIconResId());
    iv.setAdjustViewBounds(true);
    iv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT,
        LayoutParams.WRAP_CONTENT));
    if (listView.getCheckedItemPosition() == position) {
      iv.setBackgroundResource(R.drawable.icon_hilight);
    }

    TextView tv = new TextView(context);
    tv.setText(it.getNode().getItem().getDescriptionShort());
    tv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
        LayoutParams.FILL_PARENT));
    tv.setGravity(Gravity.CENTER_VERTICAL);
    tv.setPadding(10, 0, 0, 0);
    tv.setTextSize(14.0f);
    tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    tv.setTextColor(context.getResources().getColor(R.color.list_element_text));
    tv.setMinHeight(46);

    // This is where we figure out which tree branch graphics to stack
    // to the left of a node to give the appearance of a real tree widget.
    ArrayList<Integer> branches = it.getBranches();
    for (int i = 0; i < branches.size(); i++) {
      int imageResourceId;
      if (i == branches.size() - 1) {
        // You are whatever I say you are.
        imageResourceId = branches.get(i).intValue();
      } else {
        // Here we do replacements.
        if (branches.get(i).intValue() == R.drawable.treebranch_child_end) {
          // empty png
          imageResourceId = R.drawable.treebranch_parent_empty;
        } else {
          // straight pipe png
          imageResourceId = R.drawable.treebranch_parent;
        }
      }

      ImageView ivBranch = new ImageView(context);
      ivBranch.setImageResource(imageResourceId);
      ivBranch.setAdjustViewBounds(true);
      ivBranch.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT,
          LayoutParams.WRAP_CONTENT));
      ll.addView(ivBranch);
    }

    ll.addView(iv);
    ll.addView(tv);
    return ll;
  }

  /**
   * Set the rule we should render, causes the adapter to redraw itself.
   */
  public void setRule(Rule rule) {
      this.rule = rule;
      flat = (new TreeToFlatArray()).convert(rule.getRootNode());
      notifyDataSetChanged();
  }

  /**
   * Here we add the item to the rule, then refresh our UI view of it. We want to force actions to
   * go to the end of the tree, filters in the middle, so things are more organized for the
   * end-user.
   */
  public void addItemToParentPosition(int position, ModelItem item) {
    // Add the item.
    int positionOld = position;
    int positionNew;
    if (position < 0) {
      if (item instanceof ModelEvent) {
        rule.setRootEvent((ModelEvent) item);
        positionNew = 0;
      } else {
        throw new IllegalArgumentException(
            "Somehow you added a non-event item as the root element!");
      }
    } else {
      if (item instanceof ModelRuleFilter) {
        // If the user is adding a filter to the root event, we want to make
        // sure it gets added as a child before any actions.
        RuleNode nodeParent = getNodeWrapper(position).getNode();
        if (position == 0 && rule.getFirstActionPosition() > -1) {
          int insertionIndex = rule.getFirstActionPosition();
          nodeParent.addChild(item, insertionIndex);
          positionNew = insertionIndex;
        } else {
          // Either our parent is another filter, or there are just no
          // actions added to the root event yet.
          nodeParent.addChild(item);
          positionNew = position + nodeParent.getChildren().size();
        }
      } else if (item instanceof ModelRuleAction) {
        // Force adding of actions as siblings directly under the root event.
        RuleNode nodeParent = getNodeWrapper(0).getNode();
        nodeParent.addChild(item);
        positionNew = flat.size();
      } else {
        throw new IllegalArgumentException("Couldn't add unknown item type to node!");
      }
    }
    // Flatten the rule again, we can improve this later if we need to.
    flat = (new TreeToFlatArray()).convert(rule.getRootNode());

    // Notify that we need a redraw.
    notifyDataSetChanged();

    // Auto-select the item we just added.
    if (positionOld > -1) {
      listView.setItemChecked(positionOld, false);
    }
    listView.setItemChecked(positionNew, true);
  }

  public void removeItem(int position) {
    // Not allowed to remove the root event!
    if (position < 1) {
      throw new IllegalArgumentException(
          "The user shouldn't be able to remove the 0th root event item from the list view!");
    }
    // Remove this item from the rule.
    RuleNode parent = flat.get(position).getNode().getParent();
    RuleNode child = flat.get(position).getNode();
    parent.removeChild(child);
    // Flatten again.
    flat = (new TreeToFlatArray()).convert(rule.getRootNode());
    notifyDataSetChanged();
  }

  public void replaceItem(int position, ModelItem item) {
    // Not allowed to modify the root event!
    if (position < 1) {
      throw new IllegalArgumentException(
          "The user should not be allowed to replace the 0th/root item in the list!");
    }
    RuleNode node = flat.get(position).getNode();
    node.setItem(item);
    notifyDataSetChanged();
  }

  public void removeAllFiltersAndActions() {
    rule.getRootNode().removeAllChildren();
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
  private static class TreeToFlatArray {
    private int idAssigner;

    public HashMap<Integer, NodeWrapper> convert(RuleNode nodeRoot) {
      idAssigner = 0;
      HashMap<Integer, NodeWrapper> result = new HashMap<Integer, NodeWrapper>();
      flatten(result, nodeRoot, new ArrayList<Integer>());
      return result;
    }

    private void flatten(HashMap<Integer, NodeWrapper> result, RuleNode node,
        ArrayList<Integer> branches) {
      result.put(idAssigner++, new NodeWrapper(node, branches));
      for (int i = 0; i < node.getChildren().size(); i++) {
        ArrayList<Integer> branchesChild = new ArrayList<Integer>();
        for (int j = 0; j < branches.size(); j++) {
          branchesChild.add(new Integer(branches.get(j).intValue()));
        }
        if (i == node.getChildren().size() - 1) {
          // last child.
          branchesChild.add(new Integer(R.drawable.treebranch_child_end));
        } else {
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
    private RuleNode node;
    private ArrayList<Integer> branches;

    public NodeWrapper(RuleNode node, ArrayList<Integer> branches) {
      this.node = node;
      this.branches = branches;
    }

    public RuleNode getNode() {
      return node;
    }

    public ArrayList<Integer> getBranches() {
      return branches;
    }
  }
}
